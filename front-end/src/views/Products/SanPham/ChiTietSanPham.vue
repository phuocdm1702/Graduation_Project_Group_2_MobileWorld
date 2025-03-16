<template>
  <div class="mt-2 mx-auto">
    <h2 class="bg-white shadow-lg rounded-lg p-5 mb-2 mt-2 text-2xl font-semibold text-gray-700">
      Quản Lý Chi Tiết Sản Phẩm
    </h2>
    <ToastNotification ref="toast" />

    <!-- Form lọc -->
    <div
      class="bg-white shadow-lg rounded-lg p-5 mb-2 mt-2 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">
      <!-- Ô tìm kiếm -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Tìm kiếm</label>
        <input
          v-model.trim="searchKeyword"
          type="text"
          placeholder="Tìm kiếm theo ID hoặc các trường khác..."
          class="input-field"
        />
      </div>

      <!-- Bộ lọc cho các trường select -->
      <div v-for="field in filterableFields" :key="field.id">
        <label class="block text-sm font-medium text-gray-700 mb-1">{{ field.label }}</label>
        <select v-model="searchFilters[field.id]" class="input-field">
          <option value="">Tất cả</option>
          <option v-for="option in field.options" :key="option" :value="option">{{ option }}</option>
        </select>
      </div>

      <!-- Nút chức năng -->
      <div class="flex justify-end w-full col-span-full gap-2">
        <!-- Button Đặt lại -->
        <button
          @click="resetFilters"
          class="flex items-center gap-2 px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition"
        >
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
               stroke="currentColor" class="w-5 h-5 mr-1">
            <path stroke-linecap="round" stroke-linejoin="round"
                  d="M16.023 9.348h4.992v-.001M2.985 19.644v-4.992m0 0h4.992m-4.993 0l3.181 3.183a8.25 8.25 0 0013.803-3.7M4.031 9.865a8.25 8.25 0 0113.803-3.7l3.181 3.182m0-4.991v4.992"/>
          </svg>
          Đặt lại
        </button>

        <!-- Button Thêm mới -->
        <button
          @click="navigateToAddPage"
          class="flex items-center gap-2 px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition"
        >
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
               stroke="currentColor" class="w-5 h-5 mr-1">
            <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15"/>
          </svg>
          Thêm mới
        </button>
      </div>
    </div>

    <!-- Nút xóa các sản phẩm đã chọn -->
    <div v-if="selectedProducts.length" class="bg-white shadow-lg rounded-lg p-5 mb-4 mt-4 flex justify-end">
      <button
        @click="confirmDeleteSelected"
        class="flex items-center gap-2 px-4 py-2 bg-red-500 text-white font-semibold rounded-lg shadow-md hover:bg-red-600 transition"
      >
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
             stroke="currentColor" class="w-5 h-5 mr-1">
          <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12"/>
        </svg>
        Xóa {{ selectedProducts.length }} sản phẩm đã chọn
      </button>
    </div>

    <!-- Bảng danh sách chi tiết sản phẩm -->
    <DynamicTable
      class="dynamic-table"
      :data="productDetails"
      :columns="columns"
      :get-nested-value="getNestedValue"
      :selected-products="selectedProducts"
      @toggle-select="toggleSelect"
    >
      <!-- Slot để render checkbox "Chọn tất cả" trong tiêu đề cột select -->
      <template #header-select>
        <input
          type="checkbox"
          class="w-4 h-4 rounded"
          :checked="isAllSelected"
          @change="toggleSelectAll"
        >
      </template>
      <!-- Slot để render checkbox trong các dòng -->
      <template #cell-select="{ item }">
        <input
          type="checkbox"
          class="w-4 h-4 rounded"
          :checked="selectedProducts.includes(item.id)"
          @change="toggleSelect(item.id)"
        >
      </template>
    </DynamicTable>

    <!-- Phân trang -->
    <footer class="bg-white shadow-lg rounded-lg p-4 flex justify-center items-center mt-2">
      <Pagination
        :current-page="currentPage"
        :total-pages="totalPages"
        @page-changed="goToPage"
      />
    </footer>

    <!-- Modal xác nhận -->
    <ConfirmModal
      :show="showConfirmModal"
      :message="confirmMessage"
      @confirm="executeConfirmedAction"
      @cancel="closeConfirmModal"
    />
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import ToastNotification from '@/components/ToastNotification.vue';
import Pagination from '@/components/Pagination.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';
import DynamicTable from '@/components/DynamicTable.vue';
import axios from 'axios';

// Hook quản lý danh sách chi tiết sản phẩm
const useProductDetailList = () => {
  const toast = ref(null);
  const productDetails = ref([]);
  const searchKeyword = ref('');
  const searchFilters = ref({});
  const currentPage = ref(0);
  const pageSize = ref(5);
  const totalItems = ref(0);
  const selectedProducts = ref([]);
  const isSearching = ref(false);
  const showConfirmModal = ref(false);
  const confirmMessage = ref('');
  const confirmedAction = ref(null);

  const totalPages = computed(() => Math.ceil(totalItems.value / pageSize.value));

  // Logic để kiểm tra trạng thái của checkbox "Chọn tất cả"
  const isAllSelected = computed(() => {
    if (productDetails.value.length === 0) return false;
    return productDetails.value.every(item => selectedProducts.value.includes(item.id));
  });

  const fetchData = async () => {
    try {
      const { data } = await axios.get('http://localhost:8080/api/chi-tiet-san-pham', {
        params: { page: currentPage.value, size: pageSize.value },
      });
      productDetails.value = data.content;
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
      await searchProductDetails();
    } else {
      await fetchData();
    }
  };

  const searchProductDetails = async () => {
    const keyword = searchKeyword.value.replace(/\s+/g, '').trim();
    const filters = { ...searchFilters.value };
    if (!keyword && Object.values(filters).every(val => !val)) {
      isSearching.value = false;
      currentPage.value = 0;
      await fetchData();
      return;
    }
    isSearching.value = true;
    try {
      const { data } = await axios.get('http://localhost:8080/api/chi-tiet-san-pham/search', {
        params: {
          keyword: keyword || undefined,
          ...filters,
          page: currentPage.value,
          size: pageSize.value,
        },
      });
      productDetails.value = data.content;
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
    searchFilters.value = {};
    isSearching.value = false;
    currentPage.value = 0;
    fetchData();
  };

  const deleteProductDetail = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/chi-tiet-san-pham/${id}`);
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
        await searchProductDetails();
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

  const deleteSelectedProducts = async () => {
    try {
      await axios.delete('http://localhost:8080/api/chi-tiet-san-pham/bulk', {
        data: { ids: selectedProducts.value },
      });
      if (toast.value) {
        toast.value?.kshowToast('success', 'Xóa thành công!');
      }
      totalItems.value -= selectedProducts.value.length;
      if (totalItems.value > 0 && currentPage.value >= totalPages.value) {
        currentPage.value = totalPages.value - 1;
      } else if (totalItems.value <= 0) {
        currentPage.value = 0;
      }
      selectedProducts.value = [];
      if (isSearching.value) {
        await searchProductDetails();
      } else {
        await fetchData();
      }
    } catch (error) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Lỗi khi xóa nhiều sản phẩm!');
      }
      console.error('Bulk delete error:', error);
    }
  };

  const confirmDelete = (id) => {
    confirmAction('Bạn có chắc chắn muốn xóa chi tiết sản phẩm này?', () => deleteProductDetail(id));
  };

  const confirmDeleteSelected = () => {
    confirmAction(`Bạn có chắc chắn muốn xóa ${selectedProducts.value.length} sản phẩm đã chọn?`, deleteSelectedProducts);
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
    if (isAllSelected.value) {
      selectedProducts.value = [];
    } else {
      selectedProducts.value = productDetails.value.map(item => item.id);
    }
  };

  onMounted(fetchData);

  return {
    toast,
    productDetails,
    searchKeyword,
    searchFilters,
    currentPage,
    pageSize,
    totalItems,
    selectedProducts,
    isSearching,
    showConfirmModal,
    confirmMessage,
    totalPages,
    fetchData,
    goToPage,
    searchProductDetails,
    resetSearch,
    confirmDelete,
    confirmDeleteSelected,
    confirmAction,
    executeConfirmedAction,
    closeConfirmModal,
    toggleSelectAll,
    isAllSelected,
  };
};

const router = useRouter();

const {
  toast,
  productDetails,
  searchKeyword,
  searchFilters,
  currentPage,
  totalPages,
  selectedProducts,
  isSearching,
  showConfirmModal,
  confirmMessage,
  fetchData,
  goToPage,
  searchProductDetails,
  resetSearch,
  confirmDelete,
  confirmDeleteSelected,
  confirmAction,
  executeConfirmedAction,
  closeConfirmModal,
  toggleSelectAll,
  isAllSelected,
} = useProductDetailList();

const navigateToAddPage = () => {
  router.push('/product-detail/add');
};

const getNestedValue = (obj, path) => {
  return path.split('.').reduce((acc, part) => acc && acc[part], obj);
};

const toggleSelect = (id) => {
  if (selectedProducts.value.includes(id)) {
    selectedProducts.value = selectedProducts.value.filter(selectedId => selectedId !== id);
  } else {
    selectedProducts.value.push(id);
  }
};

watch([searchKeyword, searchFilters], () => {
  currentPage.value = 0;
  if (searchKeyword.value || Object.values(searchFilters.value).some(val => val)) {
    isSearching.value = true;
    searchProductDetails();
  } else {
    isSearching.value = false;
    fetchData();
  }
}, { deep: true });

// Danh sách các trường dữ liệu sản phẩm
const productFields = ref([
  { id: "id", label: "ID", type: "text" },
  { id: "id_imel", label: "ID Imel", type: "text" },
  { id: "id_anh_sp", label: "ID Ảnh Sản Phẩm", type: "select", options: ["Ảnh 1", "Ảnh 2", "Ảnh 3"] },
  { id: "id_nha_sx", label: "ID Nhà Sản Xuất", type: "select", options: ["Apple", "Samsung", "Xiaomi"] },
  { id: "id_dong_sp", label: "ID Dòng Sản Phẩm", type: "select", options: ["iPhone 15", "Galaxy S24", "Xiaomi 14"] },
  { id: "id_mau_sac", label: "ID Màu Sắc", type: "select", options: ["Đen", "Trắng", "Xanh"] },
  { id: "id_pin", label: "ID Pin", type: "select", options: ["4500mAh", "5000mAh", "6000mAh"] },
  { id: "id_man_hinh", label: "ID Màn Hình", type: "select", options: ["AMOLED", "OLED", "LCD"] },
  { id: "id_ram", label: "ID RAM", type: "select", options: ["8GB", "12GB", "16GB"] },
  { id: "id_bo_nho", label: "ID Bộ Nhớ Trong", type: "select", options: ["128GB", "256GB", "512GB"] },
  { id: "id_ho_tro_bn", label: "ID Hỗ Trợ Bộ Nhớ Ngoài", type: "select", options: ["Có", "Không"] },
  { id: "id_cpu", label: "ID CPU", type: "text" },
  { id: "id_gpu", label: "ID GPU", type: "text" },
  { id: "id_camera", label: "ID Cụm Camera", type: "select", options: ["Camera kép", "Camera đơn", "Camera 3 ống kính"] },
  { id: "id_he_dieu_hanh", label: "ID Hệ Điều Hành", type: "select", options: ["iOS", "Android"] },
  { id: "id_khang_bui", label: "ID Chỉ Số Kháng Bụi Kháng Nước", type: "select", options: ["IP68", "IP67", "IPX5"] },
  { id: "id_thiet_ke", label: "ID Thiết Kế", type: "select", options: ["Nhôm", "Kính", "Nhựa"] },
  { id: "id_sim", label: "ID Sim", type: "select", options: ["1 Sim", "2 Sim"] },
  { id: "id_sac", label: "ID Công Nghệ Sạc", type: "select", options: ["Fast Charge", "Wireless Charging"] },
  { id: "id_ho_tro_sac", label: "ID Hỗ Trợ Công Nghệ Sạc", type: "select", options: ["Có", "Không"] },
  { id: "id_cong_nghe_mang", label: "ID Công Nghệ Mạng", type: "select", options: ["5G", "4G"] },
  { id: "id_loai_tinh_trang", label: "ID Loại Tình Trạng", type: "select", options: ["Mới", "Đã sử dụng"] },
  { id: "tien_ich_dac_biet", label: "Tiện Ích Đặc Biệt", type: "text" },
  { id: "gia_ban", label: "Giá Bán", type: "text" },
]);

// Lọc các trường có thể sử dụng làm bộ lọc (chỉ lấy các trường kiểu select)
const filterableFields = computed(() => productFields.value.filter(field => field.type === 'select'));

const columns = [
  {
    key: 'select',
    label: '',
  },
  {
    key: '#',
    label: '#',
    formatter: (value, item, index) => {
      return (currentPage.value * pageSize.value) + index + 1;
    },
  },
  ...productFields.value.map(field => ({
    key: field.id,
    label: field.label,
  })),
  {
    key: 'actions',
    label: 'Hành động',
    formatter: (value, item) => {
      const safeItem = JSON.stringify(item);
      return `
        <div class="space-x-4">
          <button class="text-orange-600 hover:text-orange-800 transition" data-item='${safeItem}' onclick="document.dispatchEvent(new CustomEvent('openEditModal', { detail: JSON.parse(this.dataset.item) }))">
            <i class="fa-solid fa-edit text-orange-500"></i>
          </button>
          <button class="text-red-600 hover:text-red-800 transition" data-id="${item.id}" onclick="document.dispatchEvent(new CustomEvent('confirmDelete', { detail: this.dataset.id }))">
            <i class="fa-solid fa-trash"></i>
          </button>
        </div>
      `;
    },
  },
];

const handleCustomEvents = () => {
  document.addEventListener('openEditModal', (event) => {
    router.push(`/product-detail/edit/${event.detail.id}`);
  });
  document.addEventListener('confirmDelete', (event) => {
    confirmDelete(event.detail);
  });
};

onMounted(() => {
  fetchData();
  handleCustomEvents();
});

const resetFilters = () => {
  searchKeyword.value = '';
  searchFilters.value = {};
  resetSearch();
};
</script>

<style scoped>
.input-field {
  @apply w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-sm;
}

.dynamic-table {
  border-top-left-radius: 0px;
  border-top-right-radius: 0px;
}
</style>