import { ref, onMounted, computed, watch } from 'vue';
import axios from 'axios';

export default function useProductLineList() {
  const toast = ref(null);
  const productLines = ref([]);
  const productLine = ref({ id: null, ma: '', dongSanPham: '' });
  const searchKeyword = ref('');
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

  watch(searchKeyword, (newValue) => {
    isSearching.value = !!newValue.trim();
  });

  const fetchData = async () => {
    try {
      const { data } = await axios.get('http://localhost:8080/api/dong-san-pham', {
        params: { page: currentPage.value, size: pageSize.value },
      });
      productLines.value = data.content;
      totalItems.value = data.totalElements;
    } catch (error) {
      toast.value?.showToast('error', 'Không thể tải dữ liệu!');
      console.error('Fetch error:', error);
    }
  };

  const goToPage = async (page) => {
    currentPage.value = page;
    if (isSearching.value && searchKeyword.value.trim()) {
      await searchProductLine();
    } else {
      await fetchData();
    }
  };

  const searchProductLine = async () => {
    const keyword = searchKeyword.value.replace(/\s+/g, '').trim();
    if (!keyword) {
      isSearching.value = false;
      currentPage.value = 0;
      await fetchData();
      return;
    }
    isSearching.value = true;
    currentPage.value = 0;
    try {
      const { data } = await axios.get('http://localhost:8080/api/dong-san-pham/search', {
        params: { keyword, page: currentPage.value, size: pageSize.value },
      });
      productLines.value = data.content;
      totalItems.value = data.totalElements;
    } catch (error) {
      toast.value?.showToast('error', 'Lỗi tìm kiếm!');
    }
  };

  const resetSearch = () => {
    searchKeyword.value = '';
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
      toast.value?.showToast('error', `Lỗi kiểm tra ${field}!`);
      return false;
    }
  };

  const saveProductLine = async () => {
    const { ma, dongSanPham } = productLine.value;
    if (!ma || !dongSanPham) {
      toast.value?.showToast('error', 'Vui lòng nhập đầy đủ thông tin!');
      return;
    }
    if (await checkDuplicate('ma', ma)) {
      toast.value?.showToast('error', 'Mã dòng sản phẩm đã tồn tại!');
      return;
    }
    if (await checkDuplicate('dongSanPham', dongSanPham)) {
      toast.value?.showToast('error', 'Tên dòng sản phẩm đã tồn tại!');
      return;
    }
    try {
      const response = await axios.post('http://localhost:8080/api/dong-san-pham', productLine.value);
      toast.value?.showToast('success', 'Thêm mới thành công!');
      productLines.value.unshift(response.data);
      totalItems.value += 1;
      if (productLines.value.length > pageSize.value) {
        productLines.value.pop();
      }
      closeModal();
    } catch (error) {
      toast.value?.showToast('error', 'Lỗi khi lưu dữ liệu!');
    }
  };

  const updateProductLine = async () => {
    const { id, ma, dongSanPham } = productLine.value;
    if (!ma || !dongSanPham) {
      toast.value?.showToast('error', 'Vui lòng nhập đầy đủ thông tin!');
      return;
    }
    const originalProduct = productLines.value.find((p) => p.id === id);
    const originalMa = originalProduct?.ma;
    const originalDongSanPham = originalProduct?.dongSanPham;

    if (ma !== originalMa && (await checkDuplicate('ma', ma, id))) {
      toast.value?.showToast('error', 'Mã dòng sản phẩm đã tồn tại!');
      return;
    }
    if (dongSanPham !== originalDongSanPham && (await checkDuplicate('dongSanPham', dongSanPham, id))) {
      toast.value?.showToast('error', 'Tên dòng sản phẩm đã tồn tại!');
      return;
    }
    try {
      const response = await axios.put(`http://localhost:8080/api/dong-san-pham/${id}`, productLine.value);
      toast.value?.showToast('success', 'Cập nhật thành công!');
      const index = productLines.value.findIndex((p) => p.id === id);
      if (index !== -1) {
        productLines.value[index] = response.data;
      }
      closeModal();
    } catch (error) {
      toast.value?.showToast('error', 'Lỗi khi cập nhật dữ liệu!');
    }
  };

  const deleteProductLine = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/dong-san-pham/${id}`);
      toast.value?.showToast('success', 'Xóa thành công!');
      await fetchData();
    } catch (error) {
      toast.value?.showToast('error', 'Lỗi khi xóa!');
    }
  };

  const deleteSelectedProducts = async () => {
    try {
      await axios.delete('http://localhost:8080/api/dong-san-pham/bulk', {
        data: { ids: selectedProducts.value },
      });
      toast.value?.showToast('success', 'Xóa thành công!');
      selectedProducts.value = [];
      selectAll.value = false;
      await fetchData();
    } catch (error) {
      toast.value?.showToast('error', 'Lỗi khi xóa nhiều sản phẩm!');
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
    console.log('Received data from form:', data); // Debug dữ liệu nhận được
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