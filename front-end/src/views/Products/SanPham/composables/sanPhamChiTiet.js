import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import debounce from 'lodash/debounce';

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
    minPrice: null,
    maxPrice: null
  });
  const currentPage = ref(0);
  const pageSize = ref(5);
  const totalItems = ref(0);
  const productId = computed(() => route.params.id);

  const mauSacOptions = ref([]);
  const boNhoTrongOptions = ref([]);
  const ramOptions = ref([]);
  const priceRange = ref({
    min: 0,
    max: 10000000 // Giá trị mặc định
  });

  const priceRangeValue = ref([priceRange.value.min, priceRange.value.max]);

  const breadcrumbItems = computed(() => route.meta?.breadcrumb || ['Sản Phẩm Chi Tiết']);

  const columns = [
    { key: '#', label: 'STT', formatter: (value, item, index) => currentPage.value * 5 + index + 1 },
    { key: 'ma', label: 'Mã Sản Phẩm' },
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
      key: 'variants[0].idImel.imel',
      label: 'IMEI',
      formatter: (value) => value || 'N/A',
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
  ];

  const fetchProductDetails = async () => {
    try {
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
    } catch (error) {
      toast.value?.kshowToast('error', 'Không thể tải dữ liệu!');
      console.error('Fetch error:', error);
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

      const item = productDetails.value.find((item) => item.id === parsedId);
      if (item) {
        item.giaBan = price;
      }

      toast.value?.kshowToast('success', response.data.message || 'Cập nhật giá thành công!');
    } catch (error) {
      const errorMessage = error.response?.data?.message || 'Không thể cập nhật giá!';
      toast.value?.kshowToast('error', errorMessage);
      console.error('Update price error:', error);
    }
  };

  const fetchOptions = async () => {
    try {
      const [mauSacRes, boNhoTrongRes, ramRes, priceRangeRes] = await Promise.all([
        axios.get('http://localhost:8080/mau-sac'),
        axios.get('http://localhost:8080/bo-nho-trong'),
        axios.get('http://localhost:8080/ram'),
        axios.get(`http://localhost:8080/chi-tiet-san-pham/${productId.value}/price-range`)
      ]);

      mauSacOptions.value = mauSacRes.data.content || mauSacRes.data || [];
      boNhoTrongOptions.value = boNhoTrongRes.data.content || boNhoTrongRes.data || [];
      ramOptions.value = ramRes.data.content || ramRes.data || [];

      if (priceRangeRes.data) {
        priceRange.value = {
          min: priceRangeRes.data.minPrice ? Math.floor(priceRangeRes.data.minPrice) : 0,
          max: priceRangeRes.data.maxPrice ? Math.ceil(priceRangeRes.data.maxPrice) : 10000000
        };
      }
    } catch (error) {
      toast.value?.kshowToast('error', 'Lỗi khi tải danh sách tùy chọn!');
      console.error('Fetch options error:', error);
    }
  };

  const fetchPriceRange = async () => {
    try {
      const { data } = await axios.get(`http://localhost:8080/chi-tiet-san-pham/${productId.value}/price-range`);
      if (data) {
        priceRange.value = {
          min: data.minPrice ? Math.floor(data.minPrice) : 0,
          max: data.maxPrice ? Math.ceil(data.maxPrice) : 10000000
        };
        priceRangeValue.value = [priceRange.value.min, priceRange.value.max]; // Cập nhật giá trị slider
      }
    } catch (error) {
      console.error('Error fetching price range:', error);
      // Đặt giá trị mặc định nếu có lỗi
      priceRange.value = { min: 0, max: 10000000 };
      priceRangeValue.value = [priceRange.value.min, priceRange.value.max];
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
      console.error(`Error accessing path ${path}:`, e);
      return null;
    }
  };

  onMounted(() => {
    fetchProductDetails();
    fetchOptions();
    fetchPriceRange(); // Thêm dòng này
    window.updatePrice = updatePrice;
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
    priceRange,
    breadcrumbItems,
    columns,
    goToPage,
    debouncedSearch,
    searchProductDetails,
    goBack,
    getNestedValue,
    fetchPriceRange,
    priceRangeValue,
  };
}