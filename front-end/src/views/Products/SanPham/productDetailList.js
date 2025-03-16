import { ref, computed, watch, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

export default function productDetailList(toast) {
  const router = useRouter();
  const productDetails = ref([]);
  const searchKeyword = ref('');
  const searchFilters = ref({
    idNhaSanXuat: '',
    idHeDieuHanh: '',
    idManHinh: '',
  });
  const currentPage = ref(0);
  const pageSize = ref(5);
  const totalItems = ref(0);
  const selectedProducts = ref([]);
  const isSearching = ref(false);
  const showConfirmModal = ref(false);
  const confirmMessage = ref('');
  const confirmedAction = ref(null);

  const nhaSanXuatOptions = ref([]);
  const heDieuHanhOptions = ref([]);
  const manHinhOptions = ref([]);

  const totalPages = computed(() => Math.ceil(totalItems.value / pageSize.value));

  const isAllSelected = computed(() => {
    if (productDetails.value.length === 0) return false;
    return productDetails.value.every(item => selectedProducts.value.includes(item.id));
  });

  const fetchOptions = async () => {
    try {
      // Sử dụng Promise.all để gọi nhiều API cùng lúc
      const responses = await Promise.all([
        axios.get('http://localhost:8080/api/chi-tiet-san-pham/nha-san-xuats'),
        axios.get('http://localhost:8080/api/chi-tiet-san-pham/he-dieu-hanhs'),
        axios.get('http://localhost:8080/api/chi-tiet-san-pham/man-hinhs'),
      ]);

      // Gán giá trị từ responses vào các biến tương ứng
      nhaSanXuatOptions.value = responses[0].data; // Lấy data từ response đầu tiên
      heDieuHanhOptions.value = responses[1].data; // Lấy data từ response thứ hai
      manHinhOptions.value = responses[2].data;    // Lấy data từ response thứ ba
    } catch (error) {
      if (toast.value) {
        toast.value.kshowToast('error', 'Lỗi khi tải danh sách tùy chọn!');
      }
      console.error('Fetch options error:', error);
    }
  };

  const fetchData = async () => {
    try {
      const { data } = await axios.get('http://localhost:8080/api/chi-tiet-san-pham', {
        params: { page: currentPage.value, size: pageSize.value },
      });
      productDetails.value = data.content;
      totalItems.value = data.totalElements;
    } catch (error) {
      if (toast.value) {
        toast.value.kshowToast('error', 'Không thể tải dữ liệu!');
      }
      console.error('Fetch error:', error);
    }
  };

  const goToPage = async (page) => {
    currentPage.value = page;
    if (isSearching.value) {
      await searchProductDetails();
    } else {
      await fetchData();
    }
  };

  const searchProductDetails = async () => {
    const keyword = searchKeyword.value.replace(/\s+/g, '').trim();
    const filters = { ...searchFilters.value };

    try {
      const { data } = await axios.get('http://localhost:8080/api/chi-tiet-san-pham/search', {
        params: {
          keyword: keyword || undefined,
          idNhaSanXuat: filters.idNhaSanXuat || undefined,
          idMauSac: filters.idMauSac || undefined,
          idPin: filters.idPin || undefined,
          idManHinh: filters.idManHinh || undefined,
          idRam: filters.idRam || undefined,
          idBoNhoTrong: filters.idBoNhoTrong || undefined,
          idCpu: filters.idCpu || undefined,
          idGpu: filters.idGpu || undefined,
          idCumCamera: filters.idCumCamera || undefined,
          idHeDieuHanh: filters.idHeDieuHanh || undefined,
          idThietKe: filters.idThietKe || undefined,
          idSim: filters.idSim || undefined,
          idCongSac: filters.idCongSac || undefined,
          idHoTroCongNgheSac: filters.idHoTroCongNgheSac || undefined,
          idCongNgheMang: filters.idCongNgheMang || undefined,
          idLoaiTinhTrang: filters.idLoaiTinhTrang || undefined,
          page: currentPage.value,
          size: pageSize.value,
        },
      });
      productDetails.value = data.content;
      totalItems.value = data.totalElements;
    } catch (error) {
      if (toast.value) {
        toast.value.kshowToast('error', 'Lỗi tìm kiếm!');
      }
      console.error('Search error:', error);
    }
  };

  const resetSearch = () => {
    searchKeyword.value = '';
    searchFilters.value = { idNhaSanXuat: '', idHeDieuHanh: '', idManHinh: '' };
    isSearching.value = false;
    currentPage.value = 0;
    fetchData();
  };

  const deleteProductDetail = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/chi-tiet-san-pham/${id}`);
      if (toast.value) {
        toast.value.kshowToast('success', 'Xóa thành công!');
      }
      totalItems.value -= 1;
      if (totalItems.value > 0 && currentPage.value >= totalPages.value) {
        currentPage.value = totalPages.value - 1;
      } else if (totalItems.value <= 0) {
        currentPage.value = 0;
      }
      if (isSearching.value) {
        await searchProductDetails();
      } else {
        await fetchData();
      }
    } catch (error) {
      if (toast.value) {
        toast.value.kshowToast('error', 'Lỗi khi xóa!');
      }
      console.error('Delete error:', error);
    }
  };

  const deleteSelectedProducts = async () => {
    try {
      await axios.delete('http://localhost:8080/api/chi-tiet-san-pham/bulk', {
        data: { ids: selectedProducts.value },
      });
      if (toast.value) {
        toast.value.kshowToast('success', 'Xóa thành công!');
      }
      totalItems.value -= selectedProducts.value.length;
      if (totalItems.value > 0 && currentPage.value >= totalPages.value) {
        currentPage.value = totalPages.value - 1;
      } else if (totalItems.value <= 0) {
        currentPage.value = 0;
      }
      selectedProducts.value = [];
      if (isSearching.value) {
        await searchProductDetails();
      } else {
        await fetchData();
      }
    } catch (error) {
      if (toast.value) {
        toast.value.kshowToast('error', 'Lỗi khi xóa nhiều sản phẩm!');
      }
      console.error('Bulk delete error:', error);
    }
  };

  const confirmDelete = (id) => {
    confirmAction('Bạn có chắc chắn muốn xóa chi tiết sản phẩm này?', () => deleteProductDetail(id));
  };

  const confirmDeleteSelected = () => {
    confirmAction(`Bạn có chắc chắn muốn xóa ${selectedProducts.value.length} sản phẩm đã chọn?`, deleteSelectedProducts);
  };

  const confirmAction = (message, action) => {
    confirmMessage.value = message;
    confirmedAction.value = action;
    showConfirmModal.value = true;
  };

  const executeConfirmedAction = () => {
    if (confirmedAction.value) {
      confirmedAction.value();
    }
    closeConfirmModal();
  };

  const closeConfirmModal = () => {
    showConfirmModal.value = false;
    confirmedAction.value = null;
  };

  const toggleSelectAll = () => {
    if (isAllSelected.value) {
      selectedProducts.value = [];
    } else {
      selectedProducts.value = productDetails.value.map(item => item.id);
    }
  };

  const navigateToAddPage = () => {
    router.push('/product-detail/add');
  };

  const getNestedValue = (obj, path) => {
    return path.split('.').reduce((acc, part) => acc && acc[part], obj);
  };

  const toggleSelect = (id) => {
    if (selectedProducts.value.includes(id)) {
      selectedProducts.value = selectedProducts.value.filter(selectedId => selectedId !== id);
    } else {
      selectedProducts.value.push(id);
    }
  };

  const handleCustomEvents = () => {
    document.addEventListener('openEditModal', (event) => {
      router.push(`/product-detail/edit/${event.detail.id}`);
    });
    document.addEventListener('confirmDelete', (event) => {
      confirmDelete(event.detail);
    });
  };

  watch([searchKeyword, searchFilters], () => {
    currentPage.value = 0;
    if (searchKeyword.value || Object.values(searchFilters.value).some(val => val)) {
      isSearching.value = true;
      searchProductDetails();
    } else {
      isSearching.value = false;
      fetchData();
    }
  }, { deep: true });

  onMounted(() => {
    fetchData();
    fetchOptions();
    handleCustomEvents();
  });

  return {
    toast,
    productDetails,
    searchKeyword,
    searchFilters,
    currentPage,
    pageSize,
    totalItems,
    selectedProducts,
    isSearching,
    showConfirmModal,
    confirmMessage,
    totalPages,
    fetchData,
    goToPage,
    searchProductDetails,
    resetSearch,
    confirmDelete,
    confirmDeleteSelected,
    confirmAction,
    executeConfirmedAction,
    closeConfirmModal,
    toggleSelectAll,
    isAllSelected,
    nhaSanXuatOptions,
    heDieuHanhOptions,
    manHinhOptions,
    getNestedValue,
    toggleSelect,
    handleCustomEvents,
    navigateToAddPage,
  };
}