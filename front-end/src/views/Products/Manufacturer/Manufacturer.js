import { ref, onMounted, computed } from 'vue';
import axios from 'axios';

export default function useNhaSanXuat() {
  const toast = ref(null);
  const manufacturers = ref([]);
  const manufacturer = ref({ id: null, ma: '', nhaSanXuat: '' });
  const searchKeyword = ref('');
  const searchNhaSanXuat = ref('');
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
      const { data } = await axios.get('http://localhost:8080/nha-san-xuat', {
        params: { page: currentPage.value, size: pageSize.value },
      });
      manufacturers.value = data.content;
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
      await searchManufacturer();
    } else {
      await fetchData();
    }
  };

  const searchManufacturer = async () => {
    const keyword = searchKeyword.value.replace(/\s+/g, '').trim();
    const nhaSanXuatFilter = searchNhaSanXuat.value || '';
    if (!keyword && !nhaSanXuatFilter) {
      isSearching.value = false;
      currentPage.value = 0;
      await fetchData();
      return;
    }
    isSearching.value = true;
    try {
      const { data } = await axios.get('http://localhost:8080/nha-san-xuat/search', {
        params: {
          keyword: keyword || undefined,
          nhaSanXuat: nhaSanXuatFilter || undefined,
          page: currentPage.value,
          size: pageSize.value,
        },
      });
      manufacturers.value = data.content;
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
    searchNhaSanXuat.value = '';
    isSearching.value = false;
    currentPage.value = 0;
    fetchData();
  };

  const checkDuplicate = async (field, value, excludeId = null) => {
    try {
      const { data } = await axios.get(`http://localhost:8080/nha-san-xuat/exists/${field}`, {
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

  const saveManufacturer = async () => {
    const { ma, nhaSanXuat } = manufacturer.value;
    if (!ma || !nhaSanXuat) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Vui lòng nhập đầy đủ thông tin!');
      }
      return;
    }
    try {
      const response = await axios.post('http://localhost:8080/nha-san-xuat', manufacturer.value);
      if (toast.value) {
        toast.value?.kshowToast('success', 'Thêm mới thành công!');
      }
      manufacturers.value.unshift(response.data);
      totalItems.value += 1;
      if (manufacturers.value.length > pageSize.value) {
        manufacturers.value.pop();
      }
    } catch (error) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Lỗi khi lưu dữ liệu: ' + (error.response?.data?.error || error.message));
      }
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
      const response = await axios.put(`http://localhost:8080/nha-san-xuat/${id}`, manufacturer.value);
      if (toast.value) {
        toast.value?.kshowToast('success', 'Cập nhật thành công!');
      }
      const index = manufacturers.value.findIndex((m) => m.id === id);
      if (index !== -1) {
        manufacturers.value[index] = response.data;
      }
    } catch (error) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Lỗi khi cập nhật dữ liệu: ' + (error.response?.data?.error || error.message));
      }
      console.error('Update error:', error);
    }
  };

  const deleteManufacturer = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/nha-san-xuat/${id}`);
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
        await searchManufacturer();
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
    confirmAction('Bạn có chắc chắn muốn xóa nhà sản xuất này?', () => deleteManufacturer(id));
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

  onMounted(fetchData);

  return {
    toast,
    manufacturers,
    manufacturer,
    searchKeyword,
    searchNhaSanXuat,
    currentPage,
    pageSize,
    totalItems,
    isSearching,
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
    confirmDelete,
    confirmAction,
    executeConfirmedAction,
    closeConfirmModal,
  };
}