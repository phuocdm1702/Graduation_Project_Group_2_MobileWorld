import { ref, computed, watch } from 'vue';
import axios from 'axios';

export default function useCongNgheManHinh(toastRef) {
  const toast = ref(null);
  const congNgheManHinhs = ref([]);
  const congNgheManHinh = ref({ id: null, ma: '', congNgheManHinh: '', chuanManHinh: '' });
  const searchKeyword = ref('');
  const searchCongNgheManHinh = ref(''); // Biến lọc theo tên công nghệ
  const searchChuanManHinh = ref('');   // Biến lọc theo chuẩn màn hình
  const currentPage = ref(1);
  const pageSize = ref(5);
  const totalItems = ref(0);
  const selectedCongNgheManHinh = ref([]);
  const isSearching = ref(false);
  const showConfirmModal = ref(false);
  const confirmMessage = ref('');
  const confirmedAction = ref(null);

  if (toastRef) {
    toast.value = toastRef.value;
  }

  // Dữ liệu cứng cho combobox
  const congNgheManHinhData = [
    "OLED", "AMOLED", "Super AMOLED", "Dynamic AMOLED", "IPS LCD", "TFT LCD", "Mini-LED", "Micro-LED", "QLED"
  ];
  const chuanManHinhData = [
    "HD", "Full HD", "Full HD+", "2K", "2K+", "4K", "4K+", "8K", "Retina", "Super Retina XDR"
  ];
  const uniqueCongNgheManHinhList = ref([...new Set(congNgheManHinhData)].sort());
  const uniqueChuanManHinhList = ref([...new Set(chuanManHinhData)].sort());

  const totalPages = computed(() => Math.ceil(totalItems.value / pageSize.value));

  const isAllSelected = computed(() => {
    if (congNgheManHinhs.value.length === 0) return false;
    return congNgheManHinhs.value.every((item) => selectedCongNgheManHinh.value.includes(item.id));
  });

  const fetchData = async () => {
    try {
      const { data } = await axios.get('http://localhost:8080/api/cong-nghe-man-hinh', {
        params: { page: currentPage.value - 1, size: pageSize.value },
      });
      console.log('Fetch data:', data);
      congNgheManHinhs.value = data.content;
      totalItems.value = data.totalElements;
    } catch (error) {
      if (toast.value) toast.value.showToast('error', 'Không thể tải dữ liệu!');
      console.error('Fetch error:', error);
    }
  };

  const goToPage = async (page) => {
    if (page < 1) page = 1;
    if (page > totalPages.value) page = totalPages.value || 1;
    currentPage.value = page;
    if (isSearching.value) {
      await searchCongNgheManHinhFunc();
    } else {
      await fetchData();
    }
  };

  const searchCongNgheManHinhFunc = async () => {
    const keyword = searchKeyword.value.trim();
    const congNgheManHinhFilter = searchCongNgheManHinh.value || '';
    const chuanManHinhFilter = searchChuanManHinh.value || '';

    if (!keyword && !congNgheManHinhFilter && !chuanManHinhFilter) {
      isSearching.value = false;
      currentPage.value = 1;
      await fetchData();
      return;
    }

    try {
      isSearching.value = true;
      const { data } = await axios.get('http://localhost:8080/api/cong-nghe-man-hinh/search', {
        params: {
          keyword: keyword || undefined,
          congNgheManHinh: congNgheManHinhFilter || undefined,
          chuanManHinh: chuanManHinhFilter || undefined,
          page: currentPage.value - 1,
          size: pageSize.value,
        },
      });
      console.log('Search result:', data);
      congNgheManHinhs.value = data.content;
      totalItems.value = data.totalElements;
    } catch (error) {
      if (toast.value) toast.value.showToast('error', 'Lỗi tìm kiếm!');
      console.error('Search error:', error);
    }
  };

  const resetSearch = () => {
    searchKeyword.value = '';
    searchCongNgheManHinh.value = '';
    searchChuanManHinh.value = '';
    currentPage.value = 1;
    isSearching.value = false;
    fetchData();
  };

  const saveCongNgheManHinh = async () => {
    try {
      const response = await axios.post('http://localhost:8080/api/cong-nghe-man-hinh', congNgheManHinh.value);
      if (toast.value) toast.value.showToast('success', 'Thêm mới thành công!');
      congNgheManHinhs.value.unshift(response.data);
      totalItems.value += 1;
    } catch (error) {
      if (toast.value) toast.value.showToast('error', 'Lỗi khi lưu dữ liệu!');
      console.error('Save error:', error);
    }
  };

  const updateCongNgheManHinh = async () => {
    try {
      const response = await axios.put(`http://localhost:8080/api/cong-nghe-man-hinh/${congNgheManHinh.value.id}`, congNgheManHinh.value);
      if (toast.value) toast.value.showToast('success', 'Cập nhật thành công!');
      const index = congNgheManHinhs.value.findIndex((item) => item.id === congNgheManHinh.value.id);
      if (index !== -1) congNgheManHinhs.value[index] = response.data;
    } catch (error) {
      if (toast.value) toast.value.showToast('error', 'Lỗi khi cập nhật dữ liệu!');
      console.error('Update error:', error);
    }
  };

  const deleteCongNgheManHinh = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/cong-nghe-man-hinh/${id}`);
      if (toast.value) toast.value.showToast('success', 'Xóa thành công!');
      congNgheManHinhs.value = congNgheManHinhs.value.filter((item) => item.id !== id);
      totalItems.value -= 1;
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
      if (totalItems.value > 0 && currentPage.value >= totalPages.value) currentPage.value = totalPages.value - 1;
      else if (totalItems.value <= 0) currentPage.value = 1;
      selectedCongNgheManHinh.value = [];
      if (isSearching.value) await searchCongNgheManHinhFunc();
      else await fetchData();
    } catch (error) {
      if (toast.value) toast.value.showToast('error', 'Lỗi khi xóa nhiều công nghệ màn hình!');
      console.error('Bulk delete error:', error);
    }
  };

  const confirmAction = (message, action) => {
    confirmMessage.value = message;
    confirmedAction.value = action;
    showConfirmModal.value = true;
  };

  const confirmDeleteSelected = () => confirmAction(`Bạn có chắc chắn muốn xóa ${selectedCongNgheManHinh.value.length} công nghệ màn hình đã chọn?`, deleteSelectedCongNgheManHinh);

  const executeConfirmedAction = () => {
    if (confirmedAction.value) confirmedAction.value();
    closeConfirmModal();
  };

  const closeConfirmModal = () => {
    showConfirmModal.value = false;
    confirmedAction.value = null;
  };

  const toggleSelectAll = () => {
    if (isAllSelected.value) {
      selectedCongNgheManHinh.value = [];
    } else {
      selectedCongNgheManHinh.value = congNgheManHinhs.value.map((item) => item.id);
    }
  };

  watch([searchKeyword, searchCongNgheManHinh, searchChuanManHinh], () => {
    currentPage.value = 1;
    if (searchKeyword.value || searchCongNgheManHinh.value || searchChuanManHinh.value) {
      isSearching.value = true;
      searchCongNgheManHinhFunc();
    } else {
      isSearching.value = false;
      fetchData();
    }
  });

  fetchData(); // Tải dữ liệu mặc định khi khởi tạo

  return {
    toast,
    congNgheManHinhs,
    congNgheManHinh,
    searchKeyword,
    searchCongNgheManHinh,
    searchChuanManHinh,
    uniqueCongNgheManHinhList,
    uniqueChuanManHinhList,
    currentPage,
    pageSize,
    totalItems,
    selectedCongNgheManHinh,
    isSearching,
    showConfirmModal,
    confirmMessage,
    totalPages,
    fetchData,
    goToPage,
    searchCongNgheManHinhFunc,
    resetSearch,
    saveCongNgheManHinh,
    updateCongNgheManHinh,
    deleteCongNgheManHinh,
    deleteSelectedCongNgheManHinh,
    confirmAction,
    confirmDeleteSelected,
    executeConfirmedAction,
    closeConfirmModal,
    isAllSelected,
    toggleSelectAll,
  };
}