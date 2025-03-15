<template>
  <div class="mt-2 mx-auto">
    <h2 class="bg-white shadow-lg rounded-lg p-5 mb-2 mt-2 text-2xl font-semibold text-gray-700">
      Quản Lý Dòng Sản Phẩm
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
          placeholder="Tìm kiếm theo mã hoặc tên..."
          class="input-field"
        />
      </div>

      <!-- Dropdown chọn dòng sản phẩm -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Tên dòng sản phẩm</label>
        <select
          v-model="searchDongSanPham"
          class="input-field"
        >
          <option value="">Tất cả</option>
          <option v-for="name in uniqueDongSanPhamList" :key="name" :value="name">{{ name }}</option>
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

    <!-- Nút xóa các dòng sản phẩm đã chọn -->
    <div v-if="selectedProducts.length" class="bg-white shadow-lg rounded-lg p-5 mb-4 mt-4 flex justify-end">
      <button
        @click="confirmDeleteSelected"
        class="flex items-center gap-2 px-4 py-2 bg-red-500 text-white font-semibold rounded-lg shadow-md hover:bg-red-600 transition"
      >
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
             stroke="currentColor" class="w-5 h-5 mr-1">
          <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12"/>
        </svg>
        Xóa {{ selectedProducts.length }} dòng sản phẩm đã chọn
      </button>
    </div>

    <!-- Bảng danh sách dòng sản phẩm -->
    <DynamicTable
      class="dynamic-table"
      :data="productLines"
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
import useProductLineList from '@/views/Products/Brand/ProductLine/ProductLine.js';
import ToastNotification from '@/components/ToastNotification.vue';
import Pagination from '@/components/Pagination.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';
import DynamicTable from '@/components/DynamicTable.vue';
import axios from 'axios';

const router = useRouter();

const {
  toast,
  productLines,
  searchKeyword,
  searchDongSanPham,
  currentPage,
  pageSize,
  selectedProducts,
  isSearching,
  showConfirmModal,
  confirmMessage,
  totalPages,
  fetchData,
  goToPage,
  searchProductLine,
  resetSearch,
  checkDuplicate,
  confirmDelete,
  confirmDeleteSelected,
  confirmAction,
  executeConfirmedAction,
  closeConfirmModal,
  toggleSelectAll,
} = useProductLineList();

const navigateToAddPage = () => {
  router.push('/product-line/add');
};

const getNestedValue = (obj, path) => {
  return path.split('.').reduce((acc, part) => acc && acc[part], obj);
};

const uniqueDongSanPhamList = ref([]);

const fetchProductLineNames = async () => {
  try {
    const { data } = await axios.get('http://localhost:8080/api/dong-san-pham/all-names');
    uniqueDongSanPhamList.value = data;
  } catch (error) {
    console.error('Error fetching product line names:', error);
  }
};

const resetFilters = () => {
  searchKeyword.value = '';
  searchDongSanPham.value = '';
  resetSearch();
};

// Logic để kiểm tra trạng thái của checkbox "Chọn tất cả"
const isAllSelected = computed(() => {
  if (productLines.value.length === 0) return false;
  return productLines.value.every(item => selectedProducts.value.includes(item.id));
});

// Hàm xử lý sự kiện toggle select cho từng dòng
const toggleSelect = (id) => {
  if (selectedProducts.value.includes(id)) {
    selectedProducts.value = selectedProducts.value.filter(selectedId => selectedId !== id);
  } else {
    selectedProducts.value.push(id);
  }
};

watch([searchKeyword, searchDongSanPham], () => {
  currentPage.value = 0;
  if (searchKeyword.value || searchDongSanPham.value) {
    isSearching.value = true;
    searchProductLine();
  } else {
    isSearching.value = false;
    fetchData();
  }
});

const columns = [
  {
    key: 'select',
    label: '', // Không cần cung cấp label nữa vì slot sẽ xử lý
  },
  {
    key: '#',
    label: '#',
    formatter: (value, item, index) => {
      return (currentPage.value * pageSize.value) + index + 1;
    },
  },
  { key: 'ma', label: 'Mã' },
  { key: 'dongSanPham', label: 'Tên Dòng Sản Phẩm' },
  {
    key: 'trangThai',
    label: 'Trạng Thái',
    formatter: (value, item) => {
      return item.deleted ?
        '<span class="text-red-600">Hết hàng</span>' :
        '<span class="text-green-600">Còn hàng</span>';
    },
  },
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
    router.push(`/product-line/edit/${event.detail.id}`);
  });
  document.addEventListener('confirmDelete', (event) => {
    confirmDelete(event.detail);
  });
};

onMounted(() => {
  fetchData();
  fetchProductLineNames();
});

handleCustomEvents();
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