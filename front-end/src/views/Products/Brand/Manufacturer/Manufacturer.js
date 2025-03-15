import { ref, onMounted, computed, watch } from 'vue';
import axios from 'axios';
import ToastNotification from '@/components/ToastNotification.vue';
import Pagination from '@/components/Pagination.vue';
import ProductLineFormModal from '@/components/FormModal.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';

export default function useNhaSanXuat() {
  const toast = ref(null);
  const manufacturers = ref([]);
  const manufacturer = ref({ id: null, ma: '', nhaSanXuat: '' });
  const searchKeyword = ref('');
  const currentPage = ref(0);
  const pageSize = ref(5);
  const totalItems = ref(0);
  const selectedManufacturers = ref([]);
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
      searchManufacturer();
    }
  });

  const fetchData = async () => {
    try {
      const { data } = await axios.get('http://localhost:8080/api/nha-san-xuat', {
        params: { page: currentPage.value, size: pageSize.value },
      });
      manufacturers.value = data.content;
      totalItems.value = data.totalElements;
    } catch (error) {
      toast.value?.kshowToast('error', 'Không thể tải dữ liệu!');
      console.error('Fetch error:', error);
    }
  };

  const goToPage = async (page) => {
    currentPage.value = page;
    if (isSearching.value && searchKeyword.value.trim()) {
      await searchManufacturer();
    } else {
      await fetchData();
    }
  };

  const searchManufacturer = async () => {
    const keyword = searchKeyword.value.replace(/\s+/g, '').trim();
    if (!keyword) {
      isSearching.value = false;
      currentPage.value = 0;
      await fetchData();
      return;
    }
    isSearching.value = true;
    try {
      const { data } = await axios.get('http://localhost:8080/api/nha-san-xuat/search', {
        params: { keyword, page: currentPage.value, size: pageSize.value },
      });
      manufacturers.value = data.content;
      totalItems.value = data.totalElements;
    } catch (error) {
      toast.value?.kshowToast('error', 'Lỗi tìm kiếm!');
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
      const { data } = await axios.get(`http://localhost:8080/api/nha-san-xuat/exists/${field}`, {
        params: { [field]: value, excludeId },
      });
      return data;
    } catch (error) {
      toast.value?.kshowToast('error', `Lỗi kiểm tra ${field}!`);
      return false;
    }
  };

  const saveManufacturer = async () => {
    const { ma, nhaSanXuat } = manufacturer.value;
    if (!ma || !nhaSanXuat) {
      toast.value?.kshowToast('error', 'Vui lòng nhập đầy đủ thông tin!');
      return;
    }
    try {
      const response = await axios.post('http://localhost:8080/api/nha-san-xuat', manufacturer.value);
      toast.value?.kshowToast('success', 'Thêm mới thành công!');
      manufacturers.value.unshift(response.data);
      totalItems.value += 1;
      if (manufacturers.value.length > pageSize.value) {
        manufacturers.value.pop();
      }
      closeModal();
    } catch (error) {
      toast.value?.kshowToast('error', 'Lỗi khi lưu dữ liệu: ' + (error.response?.data || error.message));
      console.error('Save error:', error);
    }
  };

  const updateManufacturer = async () => {
    const { id, ma, nhaSanXuat } = manufacturer.value;
    if (!id || !ma || !nhaSanXuat) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Dữ liệu không hợp lệ! Vui lòng kiểm tra lại.');
      }
      return;
    }
    const originalManufacturer = manufacturers.value.find((m) => m.id === id);
    const originalMa = originalManufacturer?.ma;
    const originalNhaSanXuat = originalManufacturer?.nhaSanXuat;

    if (ma !== originalMa && (await checkDuplicate('ma', ma, id))) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Mã nhà sản xuất đã tồn tại!');
      }
      return;
    }
    if (nhaSanXuat !== originalNhaSanXuat && (await checkDuplicate('nhaSanXuat', nhaSanXuat, id))) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Tên nhà sản xuất đã tồn tại!');
      }
      return;
    }
    try {
      console.log('Sending update request with data:', manufacturer.value);
      const response = await axios.put(`http://localhost:8080/api/nha-san-xuat/${id}`, manufacturer.value);
      console.log('Response from server:', response.data);
      if (!response.data || typeof response.data !== 'object') {
        throw new Error('Dữ liệu trả về từ server không hợp lệ');
      }
      if (toast.value) {
        toast.value?.kshowToast('success', 'Cập nhật thành công!');
      }
      const index = manufacturers.value.findIndex((m) => m.id === id);
      if (index !== -1) {
        manufacturers.value[index] = response.data;
      }
      closeModal();
    } catch (error) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Lỗi khi cập nhật dữ liệu: ' + (error.response?.data || error.message));
      }
      console.error('Update error:', error);
    }
  };

  const deleteManufacturer = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/nha-san-xuat/${id}`);
      toast.value?.kshowToast('success', 'Xóa thành công!');
      totalItems.value -= 1;
      if (totalItems.value > 0 && currentPage.value >= totalPages.value) {
        currentPage.value = totalPages.value - 1;
      } else if (totalItems.value <= 0) {
        currentPage.value = 0;
      }
      if (isSearching.value && searchKeyword.value.trim()) {
        await searchManufacturer();
      } else {
        await fetchData();
      }
    } catch (error) {
      toast.value?.kshowToast('error', 'Lỗi khi xóa!');
      console.error('Delete error:', error);
    }
  };

  const deleteSelectedManufacturers = async () => {
    try {
      await axios.delete('http://localhost:8080/api/nha-san-xuat/bulk', {
        data: { ids: selectedManufacturers.value },
      });
      toast.value?.kshowToast('success', 'Xóa thành công!');
      totalItems.value -= selectedManufacturers.value.length;
      if (totalItems.value > 0 && currentPage.value >= totalPages.value) {
        currentPage.value = totalPages.value - 1;
      } else if (totalItems.value <= 0) {
        currentPage.value = 0;
      }
      selectedManufacturers.value = [];
      selectAll.value = false;
      if (isSearching.value && searchKeyword.value.trim()) {
        await searchManufacturer();
      } else {
        await fetchData();
      }
    } catch (error) {
      toast.value?.kshowToast('error', 'Lỗi khi xóa nhiều nhà sản xuất!');
      console.error('Bulk delete error:', error);
    }
  };

  const openAddModal = () => {
    manufacturer.value = { id: null, ma: '', nhaSanXuat: '' };
    showAddModal.value = true;
  };

  const openEditModal = (manufacturerData) => {
    manufacturer.value = { ...manufacturerData };
    showEditModal.value = true;
  };

  const closeModal = () => {
    showAddModal.value = false;
    showEditModal.value = false;
  };

  const handleFormSubmit = (data) => {
    manufacturer.value = data;
    if (showAddModal.value) {
      confirmAction('Bạn có chắc chắn muốn thêm nhà sản xuất này?', saveManufacturer);
    } else if (showEditModal.value) {
      confirmAction('Bạn có chắc chắn muốn cập nhật nhà sản xuất này?', updateManufacturer);
    }
  };

  const confirmDelete = (id) => {
    confirmAction('Bạn có chắc chắn muốn xóa nhà sản xuất này?', () => deleteManufacturer(id));
  };

  const confirmDeleteSelected = () => {
    confirmAction(`Bạn có chắc chắn muốn xóa ${selectedManufacturers.value.length} nhà sản xuất đã chọn?`, deleteSelectedManufacturers);
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
    selectedManufacturers.value = selectAll.value ? manufacturers.value.map((m) => m.id) : [];
  };

  onMounted(fetchData);

  return {
    toast,
    manufacturers,
    manufacturer,
    searchKeyword,
    currentPage,
    pageSize,
    totalItems,
    selectedManufacturers,
    selectAll,
    isSearching,
    showAddModal,
    showEditModal,
    showConfirmModal,
    confirmMessage,
    totalPages,
    fetchData,
    goToPage,
    searchManufacturer,
    resetSearch,
    checkDuplicate,
    saveManufacturer,
    updateManufacturer,
    deleteManufacturer,
    deleteSelectedManufacturers,
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