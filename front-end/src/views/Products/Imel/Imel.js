import { ref, onMounted, computed, watch } from 'vue';
import axios from 'axios';

export default function useImel() {
  const toast = ref(null);
  const imels = ref([]);
  const imel = ref({ id: null, ma: '', imel: '' });
  const searchKeyword = ref('');
  const searchImel = ref('');
  const currentPage = ref(0);
  const pageSize = ref(5);
  const totalItems = ref(0);
  const isSearching = ref(false);
  const showConfirmModal = ref(false);
  const confirmMessage = ref('');
  const confirmedAction = ref(null);

  const totalPages = computed(() => Math.ceil(totalItems.value / pageSize.value));

  const fetchData = async () => {
    try {
      const { data } = await axios.get('http://localhost:8080/api/imel', {
        params: { page: currentPage.value, size: pageSize.value },
      });
      imels.value = data.content;
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
      await searchImels();
    } else {
      await fetchData();
    }
  };

  const searchImels = async () => {
    const keyword = searchKeyword.value.replace(/\s+/g, '').trim();
    const imelFilter = searchImel.value || '';
    if (!keyword && !imelFilter) {
      isSearching.value = false;
      currentPage.value = 0;
      await fetchData();
      return;
    }
    isSearching.value = true;
    try {
      const { data } = await axios.get('http://localhost:8080/api/imel/search', {
        params: {
          keyword: keyword || undefined,
          imel: imelFilter || undefined,
          page: currentPage.value,
          size: pageSize.value,
        },
      });
      imels.value = data.content;
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
    searchImel.value = '';
    isSearching.value = false;
    currentPage.value = 0;
    fetchData();
  };

  const checkDuplicate = async (field, value, excludeId = null) => {
    try {
      const { data } = await axios.get(`http://localhost:8080/api/imel/exists/${field}`, {
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

  const saveImel = async () => {
    const { ma, imel: imelValue } = imel.value;
    if (!ma || !imelValue) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Vui lòng nhập đầy đủ thông tin!');
      }
      return;
    }
    try {
      const response = await axios.post('http://localhost:8080/api/imel', imel.value);
      if (toast.value) {
        toast.value?.kshowToast('success', 'Thêm mới thành công!');
      }
      imels.value.unshift(response.data);
      totalItems.value += 1;
      if (imels.value.length > pageSize.value) {
        imels.value.pop();
      }
    } catch (error) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Lỗi khi lưu dữ liệu: ' + (error.response?.data?.error || error.message));
      }
      console.error('Save error:', error);
    }
  };

  const updateImel = async () => {
    const { id, ma, imel: imelValue } = imel.value;
    if (!id || !ma || !imelValue) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Dữ liệu không hợp lệ! Vui lòng kiểm tra lại.');
      }
      return;
    }
    const originalImel = imels.value.find((i) => i.id === id);
    const originalMa = originalImel?.ma;
    const originalImelValue = originalImel?.imel;

    if (ma !== originalMa && (await checkDuplicate('ma', ma, id))) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Mã Imel đã tồn tại!');
      }
      return;
    }
    if (imelValue !== originalImelValue && (await checkDuplicate('imel', imelValue, id))) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Tên Imel đã tồn tại!');
      }
      return;
    }
    try {
      const response = await axios.put(`http://localhost:8080/api/imel/${id}`, imel.value);
      if (toast.value) {
        toast.value?.kshowToast('success', 'Cập nhật thành công!');
      }
      const index = imels.value.findIndex((i) => i.id === id);
      if (index !== -1) {
        imels.value[index] = response.data;
      }
    } catch (error) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Lỗi khi cập nhật dữ liệu: ' + (error.response?.data?.error || error.message));
      }
      console.error('Update error:', error);
    }
  };

  const deleteImel = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/imel/${id}`);
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
        await searchImels();
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

  const confirmDelete = (id) => {
    confirmAction('Bạn có chắc chắn muốn xóa Imel này?', () => deleteImel(id));
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

  watch([searchKeyword, searchImel], () => {
    currentPage.value = 0;
    searchImels();
  });

  onMounted(fetchData);

  return {
    toast,
    imels,
    imel,
    searchKeyword,
    searchImel,
    currentPage,
    pageSize,
    totalItems,
    isSearching,
    showConfirmModal,
    confirmMessage,
    totalPages,
    fetchData,
    goToPage,
    searchImels,
    resetSearch,
    checkDuplicate,
    saveImel,
    updateImel,
    deleteImel,
    confirmDelete,
    confirmAction,
    executeConfirmedAction,
    closeConfirmModal,
  };
}