import { ref, computed, watch } from 'vue';
import axios from 'axios';

export default function useManHinh(toastRef) {
  const toast = ref(null);
  const manHinhs = ref([]);
  const congNgheManHinhs = ref([]);
  const manHinh = ref({ id: null, idCongNgheManHinh: '', ma: '', kichThuoc: '', doPhanGiai: '', doSangToiDa: '', tanSoQuet: '', kieuManHinh: '', deleted: false });

  const searchKeyword = ref('');
  const searchKieuManHinh = ref('');
  const searchIdCongNgheManHinh = ref('');
  const searchKichThuoc = ref('');
  const searchDoPhanGiai = ref('');
  const searchDoSangToiDa = ref('');
  const searchTanSoQuet = ref('');

  const currentPage = ref(0);
  const pageSize = ref(5);
  const totalItems = ref(0);
  const isSearching = ref(false);
  const showConfirmModal = ref(false);
  const confirmMessage = ref('');
  const confirmedAction = ref(null);

  if (toastRef) {
    toast.value = toastRef.value;
  }

  const totalPages = computed(() => Math.ceil(totalItems.value / pageSize.value));

  const fetchCongNgheManHinhs = async () => {
    try {
      const { data } = await axios.get('http://localhost:8080/cong-nghe-man-hinh', {
        params: { page: 0, size: 100 },
      });
      congNgheManHinhs.value = data.content;
    } catch (error) {
      if (toast.value?.showToast) toast.value.showToast('error', 'Không thể tải danh sách công nghệ màn hình!');
      console.error('Fetch cong nghe man hinh error:', error);
    }
  };

  const fetchData = async () => {
    try {
      const { data } = await axios.get('http://localhost:8080/man-hinh', {
        params: { page: currentPage.value, size: pageSize.value },
      });
      manHinhs.value = data.content;
      totalItems.value = data.totalElements;
    } catch (error) {
      if (toast.value?.showToast) toast.value.showToast('error', 'Không thể tải dữ liệu!');
      console.error('Fetch error:', error);
    }
  };

  const goToPage = async (page) => {
    currentPage.value = page;
    if (isSearching.value) await searchManHinh();
    else await fetchData();
  };

  const searchManHinh = async () => {
    const keyword = searchKeyword.value.replace(/\s+/g, '').trim();
    const kieuManHinhFilter = searchKieuManHinh.value || '';
    const idCongNgheManHinhFilter = searchIdCongNgheManHinh.value || '';
    const kichThuocFilter = searchKichThuoc.value ? searchKichThuoc.value.replace(/\s+/g, '').trim() : '';
    const doPhanGiaiFilter = searchDoPhanGiai.value ? searchDoPhanGiai.value.replace(/\s+/g, '').trim() : '';
    const doSangToiDaFilter = searchDoSangToiDa.value ? searchDoSangToiDa.value.replace(/\s+/g, '').trim() : '';
    const tanSoQuetFilter = searchTanSoQuet.value ? searchTanSoQuet.value.replace(/\s+/g, '').trim() : '';

    if (!keyword && !kieuManHinhFilter && !idCongNgheManHinhFilter && !kichThuocFilter && !doPhanGiaiFilter && !doSangToiDaFilter && !tanSoQuetFilter) {
      isSearching.value = false;
      currentPage.value = 0;
      await fetchData();
      return;
    }

    isSearching.value = true;
    try {
      const { data } = await axios.get('http://localhost:8080/man-hinh/search', {
        params: {
          keyword: keyword || undefined,
          kieuManHinh: kieuManHinhFilter || undefined,
          idCongNgheManHinh: idCongNgheManHinhFilter || undefined,
          kichThuoc: kichThuocFilter || undefined,
          doPhanGiai: doPhanGiaiFilter || undefined,
          doSangToiDa: doSangToiDaFilter || undefined,
          tanSoQuet: tanSoQuetFilter || undefined,
          page: currentPage.value,
          size: pageSize.value,
        },
      });
      manHinhs.value = data.content;
      totalItems.value = data.totalElements;
    } catch (error) {
      if (toast.value?.showToast) toast.value.showToast('error', 'Lỗi tìm kiếm!');
      console.error('Search error:', error);
    }
  };

  const resetSearch = () => {
    searchKeyword.value = '';
    searchKieuManHinh.value = '';
    searchIdCongNgheManHinh.value = '';
    searchKichThuoc.value = '';
    searchDoPhanGiai.value = '';
    searchDoSangToiDa.value = '';
    searchTanSoQuet.value = '';
    isSearching.value = false;
    currentPage.value = 0;
    fetchData();
  };

  const checkDuplicate = async (field, value, excludeId = null) => {
    try {
      const endpoint = field === 'kieuManHinh' ? 'kieu-man-hinh' : 'ma';
      const paramName = field;
      const { data } = await axios.get(`http://localhost:8080/man-hinh/exists/${endpoint}`, {
        params: { [paramName]: value, ...(excludeId && { excludeId }) },
      });
      return data;
    } catch (error) {
      if (toast.value?.showToast) toast.value.showToast('error', `Lỗi kiểm tra ${field}!`);
      console.error('Check duplicate error:', error.message, error.response?.data);
      return false;
    }
  };

  const saveManHinh = async () => {
    const { ma, kieuManHinh, idCongNgheManHinh } = manHinh.value;
    if (!ma || !kieuManHinh || !idCongNgheManHinh) {
      if (toast.value?.showToast) toast.value.showToast('error', 'Vui lòng nhập đầy đủ thông tin!');
      return;
    }
    if (await checkDuplicate('ma', ma)) {
      if (toast.value?.showToast) toast.value.showToast('error', 'Mã màn hình đã tồn tại!');
      return;
    }
    try {
      const response = await axios.post('http://localhost:8080/man-hinh', manHinh.value);
      if (toast.value?.showToast) toast.value.showToast('success', 'Thêm mới thành công!');
      manHinhs.value.unshift(response.data);
      totalItems.value += 1;
      if (manHinhs.value.length > pageSize.value) manHinhs.value.pop();
    } catch (error) {
      if (toast.value?.showToast) toast.value.showToast('error', 'Lỗi khi lưu dữ liệu: ' + (error.response?.data?.error || error.message));
      console.error('Save error:', error);
    }
  };

  const updateManHinh = async () => {
    const { id, ma, kieuManHinh, idCongNgheManHinh } = manHinh.value;
    if (!id || !ma || !kieuManHinh || !idCongNgheManHinh) {
      if (toast.value?.showToast) toast.value.showToast('error', 'Dữ liệu không hợp lệ! Vui lòng kiểm tra lại.');
      return;
    }
    try {
      const response = await axios.put(`http://localhost:8080/man-hinh/${id}`, manHinh.value);
      if (toast.value?.showToast) toast.value.showToast('success', 'Cập nhật thành công!');
      const index = manHinhs.value.findIndex((i) => i.id === id);
      if (index !== -1) manHinhs.value[index] = response.data;
    } catch (error) {
      if (toast.value?.showToast) toast.value.showToast('error', 'Lỗi khi cập nhật dữ liệu: ' + (error.response?.data?.error || error.message));
      console.error('Update error:', error);
    }
  };

  const deleteManHinh = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/man-hinh/${id}`);
      if (toast.value?.showToast) toast.value.showToast('success', 'Xóa thành công!');
      totalItems.value -= 1;
      if (totalItems.value > 0 && currentPage.value >= totalPages.value) currentPage.value = totalPages.value - 1;
      else if (totalItems.value <= 0) currentPage.value = 0;
      if (isSearching.value) await searchManHinh();
      else await fetchData();
    } catch (error) {
      if (toast.value?.showToast) toast.value.showToast('error', 'Lỗi khi xóa!');
      console.error('Delete error:', error);
    }
  };

  const confirmDelete = (id) => confirmAction('Bạn có chắc chắn muốn xóa màn hình này?', () => deleteManHinh(id));
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

  watch([searchKeyword, searchKieuManHinh, searchIdCongNgheManHinh, searchKichThuoc, searchDoPhanGiai, searchDoSangToiDa, searchTanSoQuet], () => {
    currentPage.value = 0;
    if (searchKeyword.value || searchKieuManHinh.value || searchIdCongNgheManHinh.value || searchKichThuoc.value || searchDoPhanGiai.value || searchDoSangToiDa.value || searchTanSoQuet.value) {
      isSearching.value = true;
      searchManHinh();
    } else {
      isSearching.value = false;
      fetchData();
    }
  });

  return {
    toast,
    manHinhs,
    congNgheManHinhs,
    manHinh,
    searchKeyword,
    searchKieuManHinh,
    searchIdCongNgheManHinh,
    searchKichThuoc,
    searchDoPhanGiai,
    searchDoSangToiDa,
    searchTanSoQuet,
    currentPage,
    pageSize,
    totalItems,
    isSearching,
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
    confirmDelete,
    confirmAction,
    executeConfirmedAction,
    closeConfirmModal,
  };
}