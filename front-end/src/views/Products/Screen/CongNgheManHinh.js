import { ref, computed, watch } from 'vue';
import axios from 'axios';

export default function CongNgheManHinh(toastRef) {
  const toast = ref(null);
  const congNgheManHinhs = ref([]);
  const congNgheManHinh = ref({ id: null, ma: '', congNgheManHinh: '', chuanManHinh: '' });
  const searchKeyword = ref('');
  const searchCongNgheManHinh = ref('');
  const searchChuanManHinh = ref('');
  const currentPage = ref(0);
  const pageSize = ref(5);
  const totalItems = ref(0);
  const isSearching = ref(false); // Đã khai báo ở đây
  const showConfirmModal = ref(false);
  const confirmMessage = ref('');
  const confirmedAction = ref(null);

  if (toastRef) {
    toast.value = toastRef.value;
  }

  const congNgheManHinhData = [
    "OLED", "AMOLED", "Super AMOLED", "Dynamic AMOLED", "IPS LCD", "TFT LCD", "Mini-LED", "Micro-LED", "QLED"
  ];
  const chuanManHinhData = [
    "HD", "Full HD", "Full HD+", "2K", "2K+", "4K", "4K+", "8K", "Retina", "Super Retina XDR"
  ];
  const uniqueCongNgheManHinhList = ref([...new Set(congNgheManHinhData)].sort());
  const uniqueChuanManHinhList = ref([...new Set(chuanManHinhData)].sort());

  const totalPages = computed(() => Math.ceil(totalItems.value / pageSize.value));

  const fetchData = async () => {
    try {
      const { data } = await axios.get('http://localhost:8080/cong-nghe-man-hinh', {
        params: { page: currentPage.value, size: pageSize.value },
      });
      console.log('Fetch data:', data);
      congNgheManHinhs.value = data.content || [];
      totalItems.value = data.totalElements || 0;
    } catch (error) {
      if (toast.value) toast.value.showToast('error', 'Không thể tải dữ liệu!');
      console.error('Fetch error:', error);
    }
  };

  const goToPage = async (page) => {
    if (page < 1) page = 1;
    if (page > totalPages.value) page = totalPages.value || 1;
    currentPage.value = page - 1; // 0-based index cho API
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
      currentPage.value = 0;
      await fetchData();
      return;
    }

    try {
      isSearching.value = true;
      const { data } = await axios.get('http://localhost:8080/cong-nghe-man-hinh/search', {
        params: {
          keyword: keyword || undefined,
          congNgheManHinh: congNgheManHinhFilter || undefined,
          chuanManHinh: chuanManHinhFilter || undefined,
          page: currentPage.value,
          size: pageSize.value,
        },
      });
      console.log('Search result:', data);
      congNgheManHinhs.value = data.content || [];
      totalItems.value = data.totalElements || 0;
    } catch (error) {
      if (toast.value) toast.value.showToast('error', 'Lỗi tìm kiếm!');
      console.error('Search error:', error);
    }
  };

  const resetSearch = () => {
    searchKeyword.value = '';
    searchCongNgheManHinh.value = '';
    searchChuanManHinh.value = '';
    currentPage.value = 0;
    isSearching.value = false;
    fetchData();
  };

  const saveCongNgheManHinh = async () => {
    try {
      const response = await axios.post('http://localhost:8080/cong-nghe-man-hinh', congNgheManHinh.value);
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
      const response = await axios.put(`http://localhost:8080/cong-nghe-man-hinh/${congNgheManHinh.value.id}`, congNgheManHinh.value);
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
      await axios.delete(`http://localhost:8080/cong-nghe-man-hinh/${id}`);
      if (toast.value) toast.value.showToast('success', 'Xóa thành công!');
      congNgheManHinhs.value = congNgheManHinhs.value.filter((item) => item.id !== id);
      totalItems.value -= 1;
    } catch (error) {
      if (toast.value) toast.value.showToast('error', 'Lỗi khi xóa!');
      console.error('Delete error:', error);
    }
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

  watch([searchKeyword, searchCongNgheManHinh, searchChuanManHinh], () => {
    currentPage.value = 0;
    if (searchKeyword.value || searchCongNgheManHinh.value || searchChuanManHinh.value) {
      isSearching.value = true;
      searchCongNgheManHinhFunc();
    } else {
      isSearching.value = false;
      fetchData();
    }
  });

  fetchData();

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
    isSearching, // Thêm isSearching vào return
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
    confirmAction,
    executeConfirmedAction,
    closeConfirmModal,
  };
}