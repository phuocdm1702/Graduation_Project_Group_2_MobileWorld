import { ref, computed, watch } from 'vue';
import axios from 'axios';

export default function useManHinh(toastRef) {
  const toast = ref(toastRef); // Nhận toastRef từ component cha
  const manHinhs = ref([]);
  const congNgheManHinhs = ref([]); // Danh sách công nghệ màn hình
  const manHinh = ref({ id: null, idCongNgheManHinh: '', ma: '', kichThuoc: '', doPhanGiai: '', doSangToiDa: '', tanSoQuet: '', kieuManHinh: '' });
  const searchKeyword = ref('');
  const currentPage = ref(0);
  const pageSize = ref(5);
  const totalItems = ref(0);
  const selectedManHinh = ref([]);
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
      searchManHinh();
    }
  });

  // Lấy danh sách công nghệ màn hình
  const fetchCongNgheManHinhs = async () => {
    try {
      const { data } = await axios.get('http://localhost:8080/api/cong-nghe-man-hinh', {
        params: { page: 0, size: 100 }, // Lấy tất cả công nghệ màn hình
      });
      congNgheManHinhs.value = data.content;
    } catch (error) {
      if (toast.value) toast.value.showToast('error', 'Không thể tải danh sách công nghệ màn hình!');
      console.error('Fetch cong nghe man hinh error:', error);
    }
  };

  const fetchData = async () => {
    try {
      const { data } = await axios.get('http://localhost:8080/api/man-hinh', {
        params: { page: currentPage.value, size: pageSize.value },
      });
      manHinhs.value = data.content;
      totalItems.value = data.totalElements;
    } catch (error) {
      if (toast.value) toast.value.showToast('error', 'Không thể tải dữ liệu!');
      console.error('Fetch error:', error);
    }
  };

  const goToPage = async (page) => {
    currentPage.value = page;
    if (isSearching.value && searchKeyword.value.trim()) {
      await searchManHinh();
    } else {
      await fetchData();
    }
  };

  const searchManHinh = async () => {
    const keyword = searchKeyword.value.replace(/\s+/g, '').trim();
    if (!keyword) {
      isSearching.value = false;
      currentPage.value = 0;
      await fetchData();
      return;
    }
    isSearching.value = true;
    try {
      const { data } = await axios.get('http://localhost:8080/api/man-hinh/search', {
        params: { keyword, page: currentPage.value, size: pageSize.value },
      });
      manHinhs.value = data.content;
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
      const endpoint = field === 'kieuManHinh' ? 'kieu-man-hinh' : 'ma';
      const paramName = field;
      console.log(`Checking duplicate: ${endpoint}, ${paramName}=${value}`);
      const { data } = await axios.get(`http://localhost:8080/api/man-hinh/exists/${endpoint}`, {
        params: { [paramName]: value, ...(excludeId && { excludeId }) },
      });
      return data;
    } catch (error) {
      if (toast.value) toast.value.showToast('error', `Lỗi kiểm tra ${field}!`);
      console.error('Check duplicate error:', error.message, error.response?.data);
      return false;
    }
  };

  const saveManHinh = async () => {
    console.log('Saving:', manHinh.value);
    const { ma, kieuManHinh } = manHinh.value;
    if (!ma || !kieuManHinh || !manHinh.value.idCongNgheManHinh) {
      if (toast.value) toast.value.showToast('error', 'Vui lòng nhập đầy đủ thông tin!');
      return;
    }
    if (await checkDuplicate('ma', ma)) {
      if (toast.value) toast.value.showToast('error', 'Mã màn hình đã tồn tại!');
      return;
    }
    if (await checkDuplicate('kieuManHinh', kieuManHinh)) {
      if (toast.value) toast.value.showToast('error', 'Kiểu màn hình đã tồn tại!');
      return;
    }
    try {
      const response = await axios.post('http://localhost:8080/api/man-hinh', manHinh.value);
      if (toast.value) toast.value.showToast('success', 'Thêm mới thành công!');
      manHinhs.value.unshift(response.data);
      totalItems.value += 1;
      if (manHinhs.value.length > pageSize.value) {
        manHinhs.value.pop();
      }
      closeModal();
    } catch (error) {
      if (toast.value) toast.value.showToast('error', 'Lỗi khi lưu dữ liệu!');
      console.error('Save error:', error);
    }
  };

  const updateManHinh = async () => {
    console.log('Updating:', manHinh.value);
    const { id, ma, kieuManHinh } = manHinh.value;
    if (!ma || !kieuManHinh || !manHinh.value.idCongNgheManHinh) {
      if (toast.value) toast.value.showToast('error', 'Vui lòng nhập đầy đủ thông tin!');
      return;
    }
    const originalItem = manHinhs.value.find((i) => i.id === id);
    const originalMa = originalItem?.ma;
    const originalKieuManHinh = originalItem?.kieuManHinh;

    if (ma !== originalMa && (await checkDuplicate('ma', ma, id))) {
      if (toast.value) toast.value.showToast('error', 'Mã màn hình đã tồn tại!');
      return;
    }
    if (kieuManHinh !== originalKieuManHinh && (await checkDuplicate('kieuManHinh', kieuManHinh, id))) {
      if (toast.value) toast.value.showToast('error', 'Kiểu màn hình đã tồn tại!');
      return;
    }
    try {
      const response = await axios.put(`http://localhost:8080/api/man-hinh/${id}`, manHinh.value);
      if (toast.value) toast.value.showToast('success', 'Cập nhật thành công!');
      const index = manHinhs.value.findIndex((i) => i.id === id);
      if (index !== -1) {
        manHinhs.value[index] = response.data;
      }
      closeModal();
    } catch (error) {
      if (toast.value) toast.value.showToast('error', 'Lỗi khi cập nhật dữ liệu!');
      console.error('Update error:', error);
    }
  };

  const deleteManHinh = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/man-hinh/${id}`);
      if (toast.value) toast.value.showToast('success', 'Xóa thành công!');
      totalItems.value -= 1;
      if (totalItems.value > 0 && currentPage.value >= totalPages.value) {
        currentPage.value = totalPages.value - 1;
      } else if (totalItems.value <= 0) {
        currentPage.value = 0;
      }
      if (isSearching.value && searchKeyword.value.trim()) {
        await searchManHinh();
      } else {
        await fetchData();
      }
    } catch (error) {
      if (toast.value) toast.value.showToast('error', 'Lỗi khi xóa!');
      console.error('Delete error:', error);
    }
  };

  const deleteSelectedManHinh = async () => {
    try {
      await axios.delete('http://localhost:8080/api/man-hinh/bulk', {
        data: { ids: selectedManHinh.value },
      });
      if (toast.value) toast.value.showToast('success', 'Xóa thành công!');
      totalItems.value -= selectedManHinh.value.length;
      if (totalItems.value > 0 && currentPage.value >= totalPages.value) {
        currentPage.value = totalPages.value - 1;
      } else if (totalItems.value <= 0) {
        currentPage.value = 0;
      }
      selectedManHinh.value = [];
      selectAll.value = false;
      if (isSearching.value && searchKeyword.value.trim()) {
        await searchManHinh();
      } else {
        await fetchData();
      }
    } catch (error) {
      if (toast.value) toast.value.showToast('error', 'Lỗi khi xóa nhiều màn hình!');
      console.error('Bulk delete error:', error);
    }
  };

  const openAddModal = () => {
    manHinh.value = { id: null, idCongNgheManHinh: '', ma: '', kichThuoc: '', doPhanGiai: '', doSangToiDa: '', tanSoQuet: '', kieuManHinh: '' };
    showAddModal.value = true;
  };

  const openEditModal = (item) => {
    manHinh.value = { ...item };
    showEditModal.value = true;
  };

  const closeModal = () => {
    showAddModal.value = false;
    showEditModal.value = false;
  };

  const handleFormSubmit = () => {
    if (showAddModal.value) {
      confirmAction('Bạn có chắc chắn muốn thêm màn hình này?', saveManHinh);
    } else if (showEditModal.value) {
      confirmAction('Bạn có chắc chắn muốn cập nhật màn hình này?', updateManHinh);
    }
  };

  const confirmDelete = (id) => {
    confirmAction('Bạn có chắc chắn muốn xóa màn hình này?', () => deleteManHinh(id));
  };

  const confirmDeleteSelected = () => {
    confirmAction(`Bạn có chắc chắn muốn xóa ${selectedManHinh.value.length} màn hình đã chọn?`, deleteSelectedManHinh);
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
    selectedManHinh.value = selectAll.value ? manHinhs.value.map((i) => i.id) : [];
  };

  return {
    toast,
    manHinhs,
    congNgheManHinhs,
    manHinh,
    searchKeyword,
    currentPage,
    pageSize,
    totalItems,
    selectedManHinh,
    selectAll,
    isSearching,
    showAddModal,
    showEditModal,
    showConfirmModal,
    confirmMessage,
    totalPages,
    fetchCongNgheManHinhs,
    fetchData,
    goToPage,
    searchManHinh,
    resetSearch,
    checkDuplicate,
    saveManHinh,
    updateManHinh,
    deleteManHinh,
    deleteSelectedManHinh,
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