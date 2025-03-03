import { ref, onMounted, computed, watch } from 'vue';
import axios from 'axios';

export default function useCongNgheManHinh() {
  const toast = ref(null);
  const congNgheManHinhs = ref([]);
  const congNgheManHinh = ref({ id: null, ma: '', congNgheManHinh: '', chuanManHinh: '' });
  const searchKeyword = ref('');
  const currentPage = ref(0);
  const pageSize = ref(5);
  const totalItems = ref(0);
  const selectedCongNgheManHinh = ref([]);
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
    if (newValue !== searchKeyword.value) {
      currentPage.value = 0;
      searchCongNgheManHinh();
    }
  });

  const fetchData = async () => {
    try {
      const { data } = await axios.get('http://localhost:8080/api/cong-nghe-man-hinh', {
        params: { page: currentPage.value, size: pageSize.value },
      });
      congNgheManHinhs.value = data.content;
      totalItems.value = data.totalElements;
    } catch (error) {
      if (toast.value) toast.value.showToast('error', 'Không thể tải dữ liệu!');
      console.error('Fetch error:', error);
    }
  };

  const goToPage = async (page) => {
    currentPage.value = page;
    if (isSearching.value && searchKeyword.value.trim()) {
      await searchCongNgheManHinh();
    } else {
      await fetchData();
    }
  };

  const searchCongNgheManHinh = async () => {
    const keyword = searchKeyword.value.replace(/\s+/g, '').trim();
    if (!keyword) {
      isSearching.value = false;
      currentPage.value = 0;
      await fetchData();
      return;
    }
    isSearching.value = true;
    try {
      const { data } = await axios.get('http://localhost:8080/api/cong-nghe-man-hinh/search', {
        params: { keyword, page: currentPage.value, size: pageSize.value },
      });
      congNgheManHinhs.value = data.content;
      totalItems.value = data.totalElements;
    } catch (error) {
      if (toast.value) toast.value.showToast('error', 'Lỗi tìm kiếm!');
      console.error('Search error:', error);
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
      let endpoint;
      let paramName;
      switch (field) {
        case 'ma':
          endpoint = 'ma';
          paramName = 'ma';
          break;
        case 'cong-nghe-man-hinh': // Sử dụng 'cong-nghe-man-hinh' để kiểm tra tên công nghệ
          endpoint = 'cong-nghe-man-hinh';
          paramName = 'congNgheManHinh'; // Tham số backend mong đợi
          break;
        case 'chuan-man-hinh':
          endpoint = 'chuan-man-hinh';
          paramName = 'chuanManHinh'; // Tham số backend mong đợi
          break;
        default:
          throw new Error(`Invalid field: ${field}`);
      }
      console.log(`Checking duplicate: ${endpoint}, ${paramName}=${value}`); // Debug
      const { data } = await axios.get(`http://localhost:8080/api/cong-nghe-man-hinh/exists/${endpoint}`, {
        params: { [paramName]: value, ...(excludeId && { excludeId }) },
      });
      return data;
    } catch (error) {
      if (toast.value) toast.value.showToast('error', `Lỗi kiểm tra ${field}!`);
      console.error('Check duplicate error:', error.message, error.response?.data);
      return false;
    }
  };

  const saveCongNgheManHinh = async () => {
    console.log('Saving:', congNgheManHinh.value);
    const data = congNgheManHinh.value;
    const { ma, congNgheManHinh: techName, chuanManHinh } = data;
    if (!ma || !techName || !chuanManHinh) {
      if (toast.value) toast.value.showToast('error', 'Vui lòng nhập đầy đủ thông tin!');
      return;
    }
    if (await checkDuplicate('ma', ma)) {
      if (toast.value) toast.value.showToast('error', 'Mã công nghệ đã tồn tại!');
      return;
    }
    if (await checkDuplicate('cong-nghe-man-hinh', techName)) { // Sửa từ 'ten' thành 'cong-nghe-man-hinh'
      if (toast.value) toast.value.showToast('error', 'Tên công nghệ đã tồn tại!');
      return;
    }
    if (await checkDuplicate('chuan-man-hinh', chuanManHinh)) {
      if (toast.value) toast.value.showToast('error', 'Chuẩn màn hình đã tồn tại!');
      return;
    }
    try {
      const response = await axios.post('http://localhost:8080/api/cong-nghe-man-hinh', data);
      if (toast.value) toast.value.showToast('success', 'Thêm mới thành công!');
      congNgheManHinhs.value.unshift(response.data);
      totalItems.value += 1;
      if (congNgheManHinhs.value.length > pageSize.value) {
        congNgheManHinhs.value.pop();
      }
      closeModal();
    } catch (error) {
      if (toast.value) toast.value.showToast('error', 'Lỗi khi lưu dữ liệu!');
      console.error('Save error:', error);
    }
  };

  const updateCongNgheManHinh = async () => {
    console.log('Updating:', congNgheManHinh.value);
    const data = congNgheManHinh.value;
    const { id, ma, congNgheManHinh: techName, chuanManHinh } = data;
    if (!ma || !techName || !chuanManHinh) {
      if (toast.value) toast.value.showToast('error', 'Vui lòng nhập đầy đủ thông tin!');
      return;
    }
    const originalItem = congNgheManHinhs.value.find((i) => i.id === id);
    const originalMa = originalItem?.ma;
    const originalTechName = originalItem?.congNgheManHinh;
    const originalChuanManHinh = originalItem?.chuanManHinh;

    if (ma !== originalMa && (await checkDuplicate('ma', ma, id))) {
      if (toast.value) toast.value.showToast('error', 'Mã công nghệ đã tồn tại!');
      return;
    }
    if (techName !== originalTechName && (await checkDuplicate('cong-nghe-man-hinh', techName, id))) { // Sửa từ 'ten' thành 'cong-nghe-man-hinh'
      if (toast.value) toast.value.showToast('error', 'Tên công nghệ đã tồn tại!');
      return;
    }
    if (chuanManHinh !== originalChuanManHinh && (await checkDuplicate('chuan-man-hinh', chuanManHinh, id))) {
      if (toast.value) toast.value.showToast('error', 'Chuẩn màn hình đã tồn tại!');
      return;
    }
    try {
      const response = await axios.put(`http://localhost:8080/api/cong-nghe-man-hinh/${id}`, data);
      if (toast.value) toast.value.showToast('success', 'Cập nhật thành công!');
      const index = congNgheManHinhs.value.findIndex((i) => i.id === id);
      if (index !== -1) {
        congNgheManHinhs.value[index] = response.data;
      }
      closeModal();
    } catch (error) {
      if (toast.value) toast.value.showToast('error', 'Lỗi khi cập nhật dữ liệu!');
      console.error('Update error:', error);
    }
  };

  const deleteCongNgheManHinh = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/cong-nghe-man-hinh/${id}`);
      if (toast.value) toast.value.showToast('success', 'Xóa thành công!');
      totalItems.value -= 1;
      if (totalItems.value > 0 && currentPage.value >= totalPages.value) {
        currentPage.value = totalPages.value - 1;
      } else if (totalItems.value <= 0) {
        currentPage.value = 0;
      }
      if (isSearching.value && searchKeyword.value.trim()) {
        await searchCongNgheManHinh();
      } else {
        await fetchData();
      }
    } catch (error) {
      if (toast.value) toast.value.showToast('error', 'Lỗi khi xóa!');
      console.error('Delete error:', error);
    }
  };

  const deleteSelectedCongNgheManHinh = async () => {
    try {
      await axios.delete('http://localhost:8080/api/cong-nghe-man-hinh/bulk', {
        data: { ids: selectedCongNgheManHinh.value },
      });
      if (toast.value) toast.value.showToast('success', 'Xóa thành công!');
      totalItems.value -= selectedCongNgheManHinh.value.length;
      if (totalItems.value > 0 && currentPage.value >= totalPages.value) {
        currentPage.value = totalPages.value - 1;
      } else if (totalItems.value <= 0) {
        currentPage.value = 0;
      }
      selectedCongNgheManHinh.value = [];
      selectAll.value = false;
      if (isSearching.value && searchKeyword.value.trim()) {
        await searchCongNgheManHinh();
      } else {
        await fetchData();
      }
    } catch (error) {
      if (toast.value) toast.value.showToast('error', 'Lỗi khi xóa nhiều công nghệ màn hình!');
      console.error('Bulk delete error:', error);
    }
  };

  const openAddModal = () => {
    congNgheManHinh.value = { id: null, ma: '', congNgheManHinh: '', chuanManHinh: '' };
    showAddModal.value = true;
  };

  const openEditModal = (item) => {
    congNgheManHinh.value = { ...item };
    showEditModal.value = true;
  };

  const closeModal = () => {
    showAddModal.value = false;
    showEditModal.value = false;
  };

  const handleFormSubmit = () => {
    if (showAddModal.value) {
      confirmAction('Bạn có chắc chắn muốn thêm công nghệ màn hình này?', saveCongNgheManHinh);
    } else if (showEditModal.value) {
      confirmAction('Bạn có chắc chắn muốn cập nhật công nghệ màn hình này?', updateCongNgheManHinh);
    }
  };

  const confirmDelete = (id) => {
    confirmAction('Bạn có chắc chắn muốn xóa công nghệ màn hình này?', () => deleteCongNgheManHinh(id));
  };

  const confirmDeleteSelected = () => {
    confirmAction(`Bạn có chắc chắn muốn xóa ${selectedCongNgheManHinh.value.length} công nghệ màn hình đã chọn?`, deleteSelectedCongNgheManHinh);
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
    selectedCongNgheManHinh.value = selectAll.value ? congNgheManHinhs.value.map((i) => i.id) : [];
  };

  onMounted(fetchData);

  return {
    toast,
    congNgheManHinhs,
    congNgheManHinh,
    searchKeyword,
    currentPage,
    pageSize,
    totalItems,
    selectedCongNgheManHinh,
    selectAll,
    isSearching,
    showAddModal,
    showEditModal,
    showConfirmModal,
    confirmMessage,
    totalPages,
    fetchData,
    goToPage,
    searchCongNgheManHinh,
    resetSearch,
    checkDuplicate,
    saveCongNgheManHinh,
    updateCongNgheManHinh,
    deleteCongNgheManHinh,
    deleteSelectedCongNgheManHinh,
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