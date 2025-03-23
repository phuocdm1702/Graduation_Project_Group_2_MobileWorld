import { ref, computed, watch, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

export default function sanPham(toast) {
  const router = useRouter();

  // State
  const productDetails = ref([]);
  const searchKeyword = ref('');
  const searchFilters = ref({
    idNhaSanXuat: '',
    idHeDieuHanh: '',
    idManHinh: '',
    idMauSac: '',
    idPin: '',
    idRam: '',
    idBoNhoTrong: '',
    idCpu: '',
    idGpu: '',
    idCumCamera: '',
    idThietKe: '',
    idSim: '',
    idCongSac: '',
    idHoTroCongNgheSac: '',
    idCongNgheMang: '',
    idLoaiTinhTrang: '',
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

  // Computed properties
  const totalPages = computed(() => Math.ceil(totalItems.value / pageSize.value));

  const isAllSelected = computed(() => {
    if (productDetails.value.length === 0) return false;
    return productDetails.value.every(item => selectedProducts.value.includes(item.id));
  });

  // Fetch product details
  const fetchData = async () => {
    try {
      const { data } = await axios.get('http://localhost:8080/api/san-pham', {
        params: { page: currentPage.value, size: pageSize.value },
      });
      productDetails.value = data.content;
      totalItems.value = data.totalElements;
    } catch (error) {
      toast.value?.kshowToast('error', 'Không thể tải dữ liệu!');
      console.error('Fetch error:', error);
    }
  };

  // Fetch options for filters
  const fetchOptions = async () => {
    try {
      const [nhaSanXuatRes, heDieuHanhRes, manHinhRes] = await Promise.all([
        axios.get('http://localhost:8080/api/nha-san-xuat'),
        axios.get('http://localhost:8080/api/he-dieu-hanh'),
        axios.get('http://localhost:8080/api/man-hinh'),
      ]);
      nhaSanXuatOptions.value = nhaSanXuatRes.data.content || nhaSanXuatRes.data || [];
      heDieuHanhOptions.value = heDieuHanhRes.data.content || heDieuHanhRes.data || [];
      manHinhOptions.value = manHinhRes.data.content || manHinhRes.data || [];
    } catch (error) {
      toast.value?.kshowToast('error', 'Lỗi khi tải danh sách tùy chọn!');
      console.error('Fetch options error:', error);
    }
  };

  // Go to specific page
  const goToPage = async (page) => {
    currentPage.value = page;
    if (isSearching.value) {
      await searchProductDetails();
    } else {
      await fetchData();
    }
  };

  // Search product details
  const searchProductDetails = async () => {
    const keyword = searchKeyword.value.replace(/\s+/g, '').trim();
    const filters = { ...searchFilters.value };

    try {
      const { data } = await axios.get('http://localhost:8080/api/san-pham/search', {
        params: {
          keyword: keyword || undefined,
          idNhaSanXuat: searchFilters.value.idNhaSanXuat || undefined,
          idHeDieuHanh: searchFilters.value.idHeDieuHanh || undefined,
          idManHinh: searchFilters.value.idManHinh || undefined,
          page: currentPage.value,
          size: pageSize.value,
        },
      });
      productDetails.value = data.content;
      totalItems.value = data.totalElements;
    } catch (error) {
      toast.value?.kshowToast('error', 'Lỗi tìm kiếm!');
      console.error('Search error:', error);
    }
  };

  // Reset search
  const resetSearch = () => {
    searchKeyword.value = '';
    searchFilters.value = {
      idNhaSanXuat: '',
      idHeDieuHanh: '',
      idManHinh: '',
      idMauSac: '',
      idPin: '',
      idRam: '',
      idBoNhoTrong: '',
      idCpu: '',
      idGpu: '',
      idCumCamera: '',
      idThietKe: '',
      idSim: '',
      idCongSac: '',
      idHoTroCongNgheSac: '',
      idCongNgheMang: '',
      idLoaiTinhTrang: '',
    };
    isSearching.value = false;
    currentPage.value = 0;
    fetchData();
  };

  // Delete single product
  const deleteProductDetail = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/san-pham/${id}`);
      toast.value?.kshowToast('success', 'Xóa thành công!');
      totalItems.value -= 1;
      adjustPageAfterDelete();
      await refreshData();
    } catch (error) {
      toast.value?.kshowToast('error', 'Lỗi khi xóa!');
      console.error('Delete error:', error);
    }
  };

  // Delete selected products
  const deleteSelectedProducts = async () => {
    try {
      await axios.delete('http://localhost:8080/api/san-pham/bulk', {
        data: { ids: selectedProducts.value },
      });
      toast.value?.kshowToast('success', 'Xóa thành công!');
      totalItems.value -= selectedProducts.value.length;
      adjustPageAfterDelete();
      selectedProducts.value = [];
      await refreshData();
    } catch (error) {
      toast.value?.kshowToast('error', 'Lỗi khi xóa nhiều sản phẩm!');
      console.error('Bulk delete error:', error);
    }
  };

  // Helper to adjust page after deletion
  const adjustPageAfterDelete = () => {
    if (totalItems.value > 0 && currentPage.value >= totalPages.value) {
      currentPage.value = totalPages.value - 1;
    } else if (totalItems.value <= 0) {
      currentPage.value = 0;
    }
  };

  // Refresh data based on search state
  const refreshData = async () => {
    if (isSearching.value) {
      await searchProductDetails();
    } else {
      await fetchData();
    }
  };

  // Confirm actions
  const confirmDelete = (id) => {
    confirmAction('Bạn có chắc chắn muốn xóa sản phẩm này?', () => deleteProductDetail(id));
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
    if (confirmedAction.value) confirmedAction.value();
    closeConfirmModal();
  };

  const closeConfirmModal = () => {
    showConfirmModal.value = false;
    confirmedAction.value = null;
  };

  // Selection handling
  const toggleSelectAll = () => {
    if (isAllSelected.value) {
      selectedProducts.value = [];
    } else {
      selectedProducts.value = productDetails.value.map(item => item.id);
    }
  };

  const toggleSelect = (id) => {
    const index = selectedProducts.value.indexOf(id);
    if (index === -1) {
      selectedProducts.value.push(id);
    } else {
      selectedProducts.value.splice(index, 1);
    }
  };

  // Navigation
  const navigateToAddPage = () => router.push('/products/add');

  // Utility
  const getNestedValue = (obj, path) => path.split('.').reduce((acc, part) => acc && acc[part], obj);

  // Event handlers
  const handleCustomEvents = () => {
    document.addEventListener('openEditModal', (event) => {
      router.push(`/product/edit/${event.detail.id}`);
    });
    document.addEventListener('confirmDelete', (event) => {
      confirmDelete(event.detail);
    });
  };

  // Watchers
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

  // Lifecycle
  onMounted(() => {
    fetchData();
    fetchOptions();
    handleCustomEvents();
  });

  return {
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
    isAllSelected,
    nhaSanXuatOptions,
    heDieuHanhOptions,
    manHinhOptions,
    fetchData,
    fetchOptions,
    goToPage,
    searchProductDetails,
    resetSearch,
    confirmDelete,
    confirmDeleteSelected,
    confirmAction,
    executeConfirmedAction,
    closeConfirmModal,
    toggleSelectAll,
    toggleSelect,
    navigateToAddPage,
    getNestedValue,
  };
}