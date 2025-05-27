import { ref, onMounted, onUnmounted, computed, nextTick } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import debounce from 'lodash/debounce';
import JsBarcode from 'jsbarcode';
import '@/assets/sanPham.css';

export default function useSanPhamChiTiet() {
  const route = useRoute();
  const router = useRouter();
  const toast = ref(null);
  const productDetails = ref([]);
  const searchKeyword = ref('');
  const searchFilters = ref({
    status: '',
    idMauSac: '',
    idBoNhoTrong: '',
    idRam: '',
    minPrice: 0,
    maxPrice: 10000000,
  });
  const currentPage = ref(0);
  const pageSize = ref(5);
  const totalItems = ref(0);
  const productId = computed(() => route.params.id);
  const showImeiModal = ref(false);
  const showEditImeiModal = ref(false);
  const selectedImeis = ref([]);
  const editingImei = ref({ id: null, imei: '' });
  const barcodeCache = ref({});

  const mauSacOptions = ref([]);
  const boNhoTrongOptions = ref([]);
  const ramOptions = ref([]);

  const breadcrumbItems = computed(() => route.meta?.breadcrumb || ['Sản Phẩm Chi Tiết']);

  const columns = [
    { key: '#', label: 'STT', formatter: (value, item, index) => currentPage.value * 5 + index + 1 },
    { key: 'maSanPham', label: 'Mã Sản Phẩm' },
    { key: 'tenSanPham', label: 'Tên Sản Phẩm' },
    {
      key: 'variants[0].mauSac',
      label: 'Màu Sắc',
      formatter: (value) => value || 'N/A',
    },
    {
      key: 'variants[0].dungLuongRam',
      label: 'RAM',
      formatter: (value) => (value ? `${value}` : 'N/A'),
    },
    {
      key: 'variants[0].dungLuongBoNhoTrong',
      label: 'ROM',
      formatter: (value) => (value ? `${value}` : 'N/A'),
    },
    {
      key: 'variants[0].quantity',
      label: 'Số Lượng',
      formatter: (value) => value || 0,
    },
    {
      key: 'giaBan',
      label: 'Đơn Giá',
      formatter: (value, item) => {
        const id = item.id || '';
        if (!id) {
          return `
            <input
              type="number"
              value="${value || 0}"
              class="w-full p-1 border border-gray-300 rounded text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              disabled
              title="Không thể cập nhật giá do thiếu ID"
            />
          `;
        }
        return `
          <input
            type="number"
            value="${value || 0}"
            class="w-full p-1 border border-gray-300 rounded text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
            onblur="updatePrice('${id}', this.value)"
          />
        `;
      },
    },
    {
      key: 'deleted',
      label: 'Trạng Thái',
      formatter: (value) => {
        return value
          ? '<span class="inline-block px-3 py-1 border rounded-full text-sm font-semibold bg-gray-200 text-red-600">Đã đóng</span>'
          : '<span class="inline-block px-3 py-1 border rounded-full text-sm font-semibold bg-gray-200 text-green-600">Hoạt động</span>';
      },
    },
    {
      key: 'actions',
      label: 'Hành động',
      formatter: (value, item) => {
        const safeItem = JSON.stringify(item);
        return `
            <div class="space-x-4">
              <button class="text-orange-500 hover:text-orange-700 transition" data-item='${safeItem}' onclick="document.dispatchEvent(new CustomEvent('viewImeis', { detail: JSON.parse(this.dataset.item) }))">
                <i class="fa-solid fa-eye"></i>
              </button>
              <button class="text-orange-500 hover:text-orange-700 transition" data-id="${item.id}" onclick="document.dispatchEvent(new CustomEvent('editProduct', { detail: this.dataset.id }))">
                <i class="fa-solid fa-edit"></i>
              </button>
            </div>
          `;
      },
    },
  ];

  const imeiColumns = [
    {
      key: '#',
      label: 'STT',
      formatter: (value, item, index) => index + 1,
    },
    {
      key: 'ma',
      label: 'Mã CTSP',
      formatter: (value) => value || 'N/A',
    },
    {
      key: 'imei',
      label: 'IMEI',
      formatter: (value) => value || 'N/A',
    },
    {
      key: 'barcode',
      label: 'Barcode',
      formatter: (value, item) => {
        const barcodeId = `barcode-${item.id}`;
        const imei = item.imei || 'N/A';
        if (!barcodeCache.value[imei]) {
          const canvas = document.createElement('canvas');
          JsBarcode(canvas, imei, {
            format: 'CODE128',
            width: 2,
            height: 40,
            displayValue: false,
          });
          barcodeCache.value[imei] = canvas.toDataURL('image/png');
        }
        return `<img id="${barcodeId}" class="h-10 w-auto" src="${barcodeCache.value[imei]}" alt="Barcode" />`;
      },
    },
    {
      key: 'status',
      label: 'Trạng Thái',
      formatter: () => '<span class="inline-block px-3 py-1 border rounded-full text-sm font-semibold bg-gray-200 text-blue-600">Chưa bán</span>',
    },
    {
      key: 'actions',
      label: 'Hành động',
      formatter: (value, item) => {
        const safeItem = JSON.stringify(item);
        return `
          <div class="space-x-4">
            <button class="text-orange-500 hover:text-orange-700 transition" data-item='${safeItem}' onclick="document.dispatchEvent(new CustomEvent('editImei', { detail: JSON.parse(this.dataset.item) }))">
              <i class="fa-solid fa-edit"></i>
            </button>
            <button class="text-blue-500 hover:text-blue-700 transition" data-barcode="${item.imei}" onclick="document.dispatchEvent(new CustomEvent('downloadBarcode', { detail: this.dataset.barcode }))">
              <i class="fa-solid fa-download"></i>
            </button>
          </div>
        `;
      },
    },
  ];

  const fetchProductDetails = async () => {
    try {
      console.log('Fetching product details with params:', {
        page: currentPage.value,
        size: pageSize.value,
        keyword: searchKeyword.value,
        ...searchFilters.value,
      });
      const { data } = await axios.get(`http://localhost:8080/chi-tiet-san-pham/${productId.value}/details`, {
        params: {
          page: currentPage.value,
          size: pageSize.value,
          keyword: searchKeyword.value || undefined,
          status: searchFilters.value.status || undefined,
          idMauSac: searchFilters.value.idMauSac || undefined,
          idBoNhoTrong: searchFilters.value.idBoNhoTrong || undefined,
          idRam: searchFilters.value.idRam || undefined,
          minPrice: searchFilters.value.minPrice || undefined,
          maxPrice: searchFilters.value.maxPrice || undefined,
        },
      });
      productDetails.value = data.content || data;
      totalItems.value = data.totalElements || productDetails.value.length;
      if (productDetails.value.length === 0 && searchKeyword.value) {
        toast.value?.kshowToast('info', 'Không tìm thấy kết quả phù hợp với từ khóa!');
      }
    } catch (error) {
      toast.value?.kshowToast('error', 'Không thể tải dữ liệu!');
      console.error('Lỗi khi tải chi tiết sản phẩm:', error);
      productDetails.value = [];
      totalItems.value = 0;
    }
  };

  const updatePrice = async (id, newPrice) => {
    try {
      const parsedId = parseInt(id);
      if (!id || isNaN(parsedId)) {
        toast.value?.kshowToast('error', 'ID sản phẩm không hợp lệ!');
        return;
      }
      const price = parseFloat(newPrice);
      if (isNaN(price)) {
        toast.value?.kshowToast('error', 'Giá không hợp lệ!');
        return;
      }
      const response = await axios.put(`http://localhost:8080/chi-tiet-san-pham/${id}/update-price`, null, {
        params: { newPrice: price },
      });
      productDetails.value.forEach((item) => {
        item.giaBan = price;
      });
      toast.value?.kshowToast('success', response.data.message || 'Cập nhật giá thành công!');
    } catch (error) {
      const errorMessage = error.response?.data?.message || 'Không thể cập nhật giá!';
      toast.value?.kshowToast('error', errorMessage);
      console.error('Lỗi cập nhật giá:', error);
    }
  };

  const fetchImeis = async (item) => {
    try {
      console.log('Fetching IMEIs with params:', {
        idSanPham: productId.value,
        idMauSac: item.variants[0].idMauSac,
        idRam: item.variants[0].idRam,
        idBoNhoTrong: item.variants[0].idBoNhoTrong,
        giaBan: item.giaBan,
        expectedQuantity: item.variants[0].quantity,
      });
      const { data } = await axios.get(`http://localhost:8080/chi-tiet-san-pham/${productId.value}/imeis`, {
        params: {
          idMauSac: item.variants[0].idMauSac,
          idRam: item.variants[0].idRam,
          idBoNhoTrong: item.variants[0].idBoNhoTrong,
          giaBan: item.giaBan,
        },
      });
      console.log('Received IMEIs:', data);
      if (data.length !== item.variants[0].quantity) {
        console.warn(`Số lượng IMEI (${data.length}) không khớp với số lượng dự kiến (${item.variants[0].quantity})`);
        toast.value?.kshowToast('warning', `Số lượng IMEI (${data.length}) không khớp với số lượng dự kiến (${item.variants[0].quantity})`);
      }
      selectedImeis.value = data;
      showImeiModal.value = true;

      await nextTick();
      data.forEach((item) => {
        const imei = item.imei || 'N/A';
        if (!barcodeCache.value[imei]) {
          const canvas = document.createElement('canvas');
          JsBarcode(canvas, imei, {
            format: 'CODE128',
            width: 2,
            height: 40,
            displayValue: false,
          });
          barcodeCache.value[imei] = canvas.toDataURL('image/png');
        }
      });
    } catch (error) {
      toast.value?.kshowToast('error', 'Không thể tải danh sách IMEI!');
      console.error('Lỗi khi tải IMEI:', error);
    }
  };

  const fetchImelDetail = async (id) => {
    try {
      const { data } = await axios.get(`http://localhost:8080/imel/${id}`);
      return data;
    } catch (error) {
      console.error('Lỗi khi tải chi tiết IMEI:', error);
      throw new Error('Không thể tải chi tiết IMEI!');
    }
  };

  const checkExists = async (field, value, excludeId) => {
    try {
      const response = await axios.get(`http://localhost:8080/imel/exists/${field}`, {
        params: { [field]: value, excludeId },
      });
      return response.data;
    } catch (error) {
      console.error(`Lỗi khi kiểm tra ${field}:`, error);
      return false;
    }
  };

  const updateImei = async () => {
    try {
      const { id, imei } = editingImei.value;
      if (!id) throw new Error('ID IMEI không hợp lệ!');
      if (!imei || !/^\d{15}$/.test(imei)) {
        throw new Error('IMEI phải là chuỗi 15 chữ số!');
      }
      const payload = { imei };
      console.log('Sending IMEI update:', { id, payload });
      const response = await axios.put(`http://localhost:8080/imel/${id}`, payload);
      const index = selectedImeis.value.findIndex((i) => i.id === id);
      if (index !== -1) {
        selectedImeis.value[index].imei = imei;
      }
      if (!barcodeCache.value[imei]) {
        const canvas = document.createElement('canvas');
        JsBarcode(canvas, imei, {
          format: 'CODE128',
          width: 2,
          height: 40,
          displayValue: false,
        });
        barcodeCache.value[imei] = canvas.toDataURL('image/png');
      }
      toast.value?.kshowToast('success', 'Cập nhật IMEI thành công!');
      showEditImeiModal.value = false;
    } catch (error) {
      console.error('Update error:', error.response?.data);
      const errorMsg = error.response?.data?.error || error.message;
      toast.value?.kshowToast('error', errorMsg || 'Lỗi khi cập nhật IMEI');
    }
  };

  const downloadBarcode = (barcode) => {
    let barcodeUrl = barcodeCache.value[barcode];
    if (!barcodeUrl) {
      const canvas = document.createElement('canvas');
      JsBarcode(canvas, barcode, { format: 'CODE128', width: 2, height: 100 });
      barcodeUrl = canvas.toDataURL('image/png');
      barcodeCache.value[barcode] = barcodeUrl;
    }
    const link = document.createElement('a');
    link.href = barcodeUrl;
    link.download = `barcode_${barcode}.png`;
    link.click();
    link.remove();
  };

  const fetchOptions = async () => {
    try {
      const [mauSacRes, boNhoTrongRes, ramRes] = await Promise.all([
        axios.get('http://localhost:8080/mau-sac/all'),
        axios.get('http://localhost:8080/bo-nho-trong/all'),
        axios.get('http://localhost:8080/ram/all'),
      ]);
      mauSacOptions.value = mauSacRes.data.content || mauSacRes.data || [];
      boNhoTrongOptions.value = boNhoTrongRes.data.content || boNhoTrongRes.data || [];
      ramOptions.value = ramRes.data.content || ramRes.data || [];
    } catch (error) {
      toast.value?.kshowToast('error', 'Lỗi khi tải danh sách tùy chọn!');
      console.error('Lỗi khi tải tùy chọn:', error);
    }
  };

  const searchProductDetails = () => {
    currentPage.value = 0;
    fetchProductDetails();
  };

  const goToPage = async (page) => {
    currentPage.value = page;
    await fetchProductDetails();
  };

  const debouncedSearch = debounce(() => {
    currentPage.value = 0;
    fetchProductDetails();
  }, 500);

  const goBack = () => {
    router.push('/products');
  };

  const totalPages = computed(() => Math.ceil(totalItems.value / pageSize.value));

  const getNestedValue = (obj, path) => {
    try {
      return path.split('.').reduce((acc, part) => {
        if (part.includes('[')) {
          const [key, index] = part.split('[');
          const idx = parseInt(index.replace(']', ''), 10);
          return acc[key][idx];
        }
        return acc && acc[part];
      }, obj);
    } catch (e) {
      console.error(`Lỗi khi truy cập đường dẫn ${path}:`, e);
      return null;
    }
  };

  onMounted(() => {
    fetchProductDetails();
    fetchOptions();
    window.updatePrice = updatePrice;

    const handleViewImeis = (event) => {
      fetchImeis(event.detail);
    };

    const handleEditProduct = (event) => {
      console.log('Navigating to edit with detailId:', event.detail);
      router.push(`/products/details/${productId.value}/edit/${event.detail}`);
    };

    const handleEditImei = (event) => {
      editingImei.value = {
        id: event.detail.id,
        imei: event.detail.imei || '',
      };
      showEditImeiModal.value = true;
    };

    const handleDownloadBarcode = (event) => {
      downloadBarcode(event.detail);
    };

    document.addEventListener('viewImeis', handleViewImeis);
    document.addEventListener('editProduct', handleEditProduct);
    document.addEventListener('editImei', handleEditImei);
    document.addEventListener('downloadBarcode', handleDownloadBarcode);

    onUnmounted(() => {
      document.removeEventListener('viewImeis', handleViewImeis);
      document.removeEventListener('editProduct', handleEditProduct);
      document.removeEventListener('editImei', handleEditImei);
      document.removeEventListener('downloadBarcode', handleDownloadBarcode);
      delete window.updatePrice;
    });
  });

  return {
    toast,
    productDetails,
    searchKeyword,
    searchFilters,
    currentPage,
    totalPages,
    mauSacOptions,
    boNhoTrongOptions,
    ramOptions,
    breadcrumbItems,
    columns,
    imeiColumns,
    goToPage,
    debouncedSearch,
    searchProductDetails,
    goBack,
    getNestedValue,
    showImeiModal,
    showEditImeiModal,
    selectedImeis,
    editingImei,
    updateImei,
  };
}