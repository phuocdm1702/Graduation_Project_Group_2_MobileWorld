import { ref, onMounted, computed } from 'vue';
import axios from 'axios';

export default function useProductLineList() {
  const toast = ref(null);
  const productLines = ref([]);
  const productLine = ref({ id: null, ma: '', dongSanPham: '' });
  const searchKeyword = ref('');
  const searchDongSanPham = ref('');
  const currentPage = ref(0);
  const pageSize = ref(5);
  const totalItems = ref(0);
  const selectedProducts = ref([]);
  const selectAll = ref(false);
  const isSearching = ref(false);
  const showAddModal = ref(false);
  const showEditModal = ref(false);
  const showConfirmModal = ref(false);
  const confirmMessage = ref('');
  const confirmedAction = ref(null);

  const totalPages = computed(() => Math.ceil(totalItems.value / pageSize.value));

  const fetchData = async () => {
    try {
      const { data } = await axios.get('http://localhost:8080/api/dong-san-pham', {
        params: { page: currentPage.value, size: pageSize.value },
      });
      productLines.value = data.content;
      totalItems.value = data.totalElements;
    } catch (error) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Không thể tải dữ liệu!');
      }
      console.error('Fetch error:', error);
    }
  };

  const goToPage = async (page) => {
    currentPage.value = page;
    if (isSearching.value) {
      await searchProductLine();
    } else {
      await fetchData();
    }
  };

  const searchProductLine = async () => {
    const keyword = searchKeyword.value.replace(/\s+/g, '').trim();
    const dongSanPhamFilter = searchDongSanPham.value || '';
    if (!keyword && !dongSanPhamFilter) {
      isSearching.value = false;
      currentPage.value = 0;
      await fetchData();
      return;
    }
    isSearching.value = true;
    try {
      const { data } = await axios.get('http://localhost:8080/api/dong-san-pham/search', {
        params: {
          keyword: keyword || undefined,
          dongSanPham: dongSanPhamFilter || undefined,
          page: currentPage.value,
          size: pageSize.value,
        },
      });
      productLines.value = data.content;
      totalItems.value = data.totalElements;
    } catch (error) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Lỗi tìm kiếm!');
      }
      console.error('Search error:', error);
    }
  };

  const resetSearch = () => {
    searchKeyword.value = '';
    searchDongSanPham.value = '';
    isSearching.value = false;
    currentPage.value = 0;
    fetchData();
  };

  const checkDuplicate = async (field, value, excludeId = null) => {
    try {
      const { data } = await axios.get(`http://localhost:8080/api/dong-san-pham/exists/${field}`, {
        params: { [field]: value, excludeId },
      });
      return data;
    } catch (error) {
      if (toast.value) {
        toast.value?.kshowToast('error', `Lỗi kiểm tra ${field}!`);
      }
      return false;
    }
  };

  const saveProductLine = async () => {
    const { ma, dongSanPham } = productLine.value;
    if (!ma || !dongSanPham) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Vui lòng nhập đầy đủ thông tin!');
      }
      return;
    }
    try {
      const response = await axios.post('http://localhost:8080/api/dong-san-pham', productLine.value);
      if (toast.value) {
        toast.value?.kshowToast('success', 'Thêm mới thành công!');
      }
      productLines.value.unshift(response.data);
      totalItems.value += 1;
      if (productLines.value.length > pageSize.value) {
        productLines.value.pop();
      }
      closeModal();
    } catch (error) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Lỗi khi lưu dữ liệu: ' + (error.response?.data?.error || error.message));
      }
      console.error('Save error:', error);
    }
  };

  const updateProductLine = async () => {
    const { id, ma, dongSanPham } = productLine.value;
    if (!id || !ma || !dongSanPham) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Dữ liệu không hợp lệ! Vui lòng kiểm tra lại.');
      }
      return;
    }
    const originalProduct = productLines.value.find((p) => p.id === id);
    const originalMa = originalProduct?.ma;
    const originalDongSanPham = originalProduct?.dongSanPham;

    if (ma !== originalMa && (await checkDuplicate('ma', ma, id))) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Mã dòng sản phẩm đã tồn tại!');
      }
      return;
    }
    if (dongSanPham !== originalDongSanPham && (await checkDuplicate('dongSanPham', dongSanPham, id))) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Tên dòng sản phẩm đã tồn tại!');
      }
      return;
    }
    try {
      const response = await axios.put(`http://localhost:8080/api/dong-san-pham/${id}`, productLine.value);
      if (toast.value) {
        toast.value?.kshowToast('success', 'Cập nhật thành công!');
      }
      const index = productLines.value.findIndex((p) => p.id === id);
      if (index !== -1) {
        productLines.value[index] = response.data;
      }
      closeModal();
    } catch (error) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Lỗi khi cập nhật dữ liệu: ' + (error.response?.data?.error || error.message));
      }
      console.error('Update error:', error);
    }
  };

  const deleteProductLine = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/dong-san-pham/${id}`);
      if (toast.value) {
        toast.value?.kshowToast('success', 'Xóa thành công!');
      }
      totalItems.value -= 1;
      if (totalItems.value > 0 && currentPage.value >= totalPages.value) {
        currentPage.value = totalPages.value - 1;
      } else if (totalItems.value <= 0) {
        currentPage.value = 0;
      }
      if (isSearching.value) {
        await searchProductLine();
      } else {
        await fetchData();
      }
    } catch (error) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Lỗi khi xóa!');
      }
      console.error('Delete error:', error);
    }
  };

  const deleteSelectedProducts = async () => {
    try {
      await axios.delete('http://localhost:8080/api/dong-san-pham/bulk', {
        data: { ids: selectedProducts.value },
      });
      if (toast.value) {
        toast.value?.kshowToast('success', 'Xóa thành công!');
      }
      totalItems.value -= selectedProducts.value.length;
      if (totalItems.value > 0 && currentPage.value >= totalPages.value) {
        currentPage.value = totalPages.value - 1;
      } else if (totalItems.value <= 0) {
        currentPage.value = 0;
      }
      selectedProducts.value = [];
      selectAll.value = false;
      if (isSearching.value) {
        await searchProductLine();
      } else {
        await fetchData();
      }
    } catch (error) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Lỗi khi xóa nhiều dòng sản phẩm!');
      }
      console.error('Bulk delete error:', error);
    }
  };

  const openAddModal = () => {
    productLine.value = { id: null, ma: '', dongSanPham: '' };
    showAddModal.value = true;
  };

  const openEditModal = (product) => {
    productLine.value = { ...product };
    showEditModal.value = true;
  };

  const closeModal = () => {
    showAddModal.value = false;
    showEditModal.value = false;
  };

  const handleFormSubmit = (data) => {
    productLine.value = data;
    if (showAddModal.value) {
      confirmAction('Bạn có chắc chắn muốn thêm dòng sản phẩm này?', saveProductLine);
    } else if (showEditModal.value) {
      confirmAction('Bạn có chắc chắn muốn cập nhật dòng sản phẩm này?', updateProductLine);
    }
  };

  const confirmDelete = (id) => {
    confirmAction('Bạn có chắc chắn muốn xóa dòng sản phẩm này?', () => deleteProductLine(id));
  };

  const confirmDeleteSelected = () => {
    confirmAction(`Bạn có chắc chắn muốn xóa ${selectedProducts.value.length} dòng sản phẩm đã chọn?`, deleteSelectedProducts);
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
    selectedProducts.value = selectAll.value ? productLines.value.map((p) => p.id) : [];
  };

  onMounted(fetchData);

  return {
    toast,
    productLines,
    productLine,
    searchKeyword,
    searchDongSanPham,
    currentPage,
    pageSize,
    totalItems,
    selectedProducts,
    selectAll,
    isSearching,
    showAddModal,
    showEditModal,
    showConfirmModal,
    confirmMessage,
    totalPages,
    fetchData,
    goToPage,
    searchProductLine,
    resetSearch,
    checkDuplicate,
    saveProductLine,
    updateProductLine,
    deleteProductLine,
    deleteSelectedProducts,
    openAddModal,
    openEditModal,
    closeModal,
    handleFormSubmit,
    confirmDelete,
    confirmDeleteSelected,
    confirmAction,
    executeConfirmedAction,
    closeConfirmModal,
    toggleSelectAll,
  };
}