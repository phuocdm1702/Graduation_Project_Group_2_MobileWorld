import { ref, onMounted, computed, watch } from 'vue';
import axios from 'axios';
import ToastNotification from '@/components/ToastNotification.vue';
import Pagination from '@/components/Pagination.vue';
import ProductLineFormModal from '@/components/FormModal.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';

export default function useImel() {
  const toast = ref(null);
  const imels = ref([]);
  const imel = ref({ id: null, ma: '', imel: '' });
  const searchKeyword = ref('');
  const currentPage = ref(0);
  const pageSize = ref(5);
  const totalItems = ref(0);
  const selectedImels = ref([]);
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
      searchImel();
    }
  });

  const fetchData = async () => {
    try {
      const { data } = await axios.get('http://localhost:8080/api/imel', {
        params: { page: currentPage.value, size: pageSize.value },
      });
      imels.value = data.content;
      totalItems.value = data.totalElements;
    } catch (error) {
      toast.value?.showToast('error', 'Không thể tải dữ liệu!');
      console.error('Fetch error:', error);
    }
  };

  const goToPage = async (page) => {
    currentPage.value = page;
    if (isSearching.value && searchKeyword.value.trim()) {
      await searchImel();
    } else {
      await fetchData();
    }
  };

  const searchImel = async () => {
    const keyword = searchKeyword.value.replace(/\s+/g, '').trim();
    if (!keyword) {
      isSearching.value = false;
      currentPage.value = 0;
      await fetchData();
      return;
    }
    isSearching.value = true;
    try {
      const { data } = await axios.get('http://localhost:8080/api/imel/search', {
        params: { keyword, page: currentPage.value, size: pageSize.value },
      });
      imels.value = data.content;
      totalItems.value = data.totalElements;
    } catch (error) {
      toast.value?.showToast('error', 'Lỗi tìm kiếm!');
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
      const { data } = await axios.get(`http://localhost:8080/api/imel/exists/${field}`, {
        params: { [field]: value, excludeId },
      });
      return data;
    } catch (error) {
      toast.value?.showToast('error', `Lỗi kiểm tra ${field}!`);
      return false;
    }
  };

  const saveImel = async () => {
    const { ma, imel: imelValue } = imel.value;
    if (!ma || !imelValue) {
      toast.value?.showToast('error', 'Vui lòng nhập đầy đủ thông tin!');
      return;
    }
    if (await checkDuplicate('ma', ma)) {
      toast.value?.showToast('error', 'Mã Imel đã tồn tại!');
      return;
    }
    if (await checkDuplicate('imel', imelValue)) {
      toast.value?.showToast('error', 'Tên Imel đã tồn tại!');
      return;
    }
    try {
      const response = await axios.post('http://localhost:8080/api/imel', imel.value);
      toast.value?.showToast('success', 'Thêm mới thành công!');
      imels.value.unshift(response.data);
      totalItems.value += 1;
      if (imels.value.length > pageSize.value) {
        imels.value.pop();
      }
      closeModal();
    } catch (error) {
      toast.value?.showToast('error', 'Lỗi khi lưu dữ liệu!');
      console.error('Save error:', error);
    }
  };

  const updateImel = async () => {
    const { id, ma, imel: imelValue } = imel.value;
    if (!ma || !imelValue) {
      toast.value?.showToast('error', 'Vui lòng nhập đầy đủ thông tin!');
      return;
    }
    const originalImel = imels.value.find((i) => i.id === id);
    const originalMa = originalImel?.ma;
    const originalImelValue = originalImel?.imel;

    if (ma !== originalMa && (await checkDuplicate('ma', ma, id))) {
      toast.value?.showToast('error', 'Mã Imel đã tồn tại!');
      return;
    }
    if (imelValue !== originalImelValue && (await checkDuplicate('imel', imelValue, id))) {
      toast.value?.showToast('error', 'Tên Imel đã tồn tại!');
      return;
    }
    try {
      const response = await axios.put(`http://localhost:8080/api/imel/${id}`, imel.value);
      toast.value?.showToast('success', 'Cập nhật thành công!');
      const index = imels.value.findIndex((i) => i.id === id);
      if (index !== -1) {
        imels.value[index] = response.data;
      }
      closeModal();
    } catch (error) {
      toast.value?.showToast('error', 'Lỗi khi cập nhật dữ liệu!');
      console.error('Update error:', error);
    }
  };

  const deleteImel = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/imel/${id}`);
      toast.value?.showToast('success', 'Xóa thành công!');
      totalItems.value -= 1; // Giảm số lượng bản ghi
      // Kiểm tra xem trang hiện tại có hợp lệ không
      if (totalItems.value > 0 && currentPage.value >= totalPages.value) {
        currentPage.value = totalPages.value - 1; // Chuyển về trang cuối cùng còn dữ liệu
      } else if (totalItems.value <= 0) {
        currentPage.value = 0; // Nếu không còn dữ liệu, đặt về trang 0
      }
      if (isSearching.value && searchKeyword.value.trim()) {
        await searchImel();
      } else {
        await fetchData();
      }
    } catch (error) {
      toast.value?.showToast('error', 'Lỗi khi xóa!');
      console.error('Delete error:', error);
    }
  };

  const deleteSelectedImels = async () => {
    try {
      await axios.delete('http://localhost:8080/api/imel/bulk', {
        data: { ids: selectedImels.value },
      });
      toast.value?.showToast('success', 'Xóa thành công!');
      totalItems.value -= selectedImels.value.length; // Giảm số lượng bản ghi
      // Kiểm tra xem trang hiện tại có hợp lệ không
      if (totalItems.value > 0 && currentPage.value >= totalPages.value) {
        currentPage.value = totalPages.value - 1; // Chuyển về trang cuối cùng còn dữ liệu
      } else if (totalItems.value <= 0) {
        currentPage.value = 0; // Nếu không còn dữ liệu, đặt về trang 0
      }
      selectedImels.value = [];
      selectAll.value = false;
      if (isSearching.value && searchKeyword.value.trim()) {
        await searchImel();
      } else {
        await fetchData();
      }
    } catch (error) {
      toast.value?.showToast('error', 'Lỗi khi xóa nhiều Imel!');
      console.error('Bulk delete error:', error);
    }
  };

  const openAddModal = () => {
    imel.value = { id: null, ma: '', imel: '' };
    showAddModal.value = true;
  };

  const openEditModal = (imelData) => {
    imel.value = { ...imelData };
    showEditModal.value = true;
  };

  const closeModal = () => {
    showAddModal.value = false;
    showEditModal.value = false;
  };

  const handleFormSubmit = (data) => {
    imel.value = data;
    if (showAddModal.value) {
      confirmAction('Bạn có chắc chắn muốn thêm Imel này?', saveImel);
    } else if (showEditModal.value) {
      confirmAction('Bạn có chắc chắn muốn cập nhật Imel này?', updateImel);
    }
  };

  const confirmDelete = (id) => {
    confirmAction('Bạn có chắc chắn muốn xóa Imel này?', () => deleteImel(id));
  };

  const confirmDeleteSelected = () => {
    confirmAction(`Bạn có chắc chắn muốn xóa ${selectedImels.value.length} Imel đã chọn?`, deleteSelectedImels);
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
    selectedImels.value = selectAll.value ? imels.value.map((i) => i.id) : [];
  };

  onMounted(fetchData);

  return {
    toast,
    imels,
    imel,
    searchKeyword,
    currentPage,
    pageSize,
    totalItems,
    selectedImels,
    selectAll,
    isSearching,
    showAddModal,
    showEditModal,
    showConfirmModal,
    confirmMessage,
    totalPages,
    fetchData,
    goToPage,
    searchImel,
    resetSearch,
    checkDuplicate,
    saveImel,
    updateImel,
    deleteImel,
    deleteSelectedImels,
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