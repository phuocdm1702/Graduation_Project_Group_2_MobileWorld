<template>
  <div class="container mx-auto p-4 relative">
    <!-- Toast thông báo -->
    <ToastNotification ref="toast" />

    <!-- Tiêu đề và nút mở modal -->
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-xl text-gray-600">Quản lý Nhà Sản Xuất</h2>
      <button
        @click="openAddModal"
        class="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition"
      >
        Thêm mới
      </button>
    </div>

    <!-- Search Section -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-3 mb-6">
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Tìm kiếm</label>
        <input
          v-model.trim="searchKeyword"
          type="text"
          placeholder="Tìm kiếm theo mã hoặc tên..."
          class="w-full border rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Tên nhà sản xuất</label>
        <select
          v-model="searchNhaSanXuat"
          class="w-full border rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
        >
          <option value="">Tất cả</option>
          <option v-for="name in uniqueNhaSanXuatList" :key="name" :value="name">{{ name }}</option>
        </select>
      </div>
      <div class="flex gap-3 items-end">
        <button
          @click="resetFilters"
          class="bg-gray-400 text-white px-4 py-2 rounded-lg hover:bg-gray-500 transition"
        >
          Đặt lại
        </button>
      </div>
    </div>

    <!-- Bulk Delete Button -->
    <div v-if="selectedManufacturers.length" class="mb-6 flex justify-end">
      <button
        @click="confirmDeleteSelected"
        class="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 transition"
      >
        Xóa {{ selectedManufacturers.length }} nhà sản xuất đã chọn
      </button>
    </div>

    <!-- DynamicTable -->
    <DynamicTable
      :data="filteredManufacturers"
      :columns="columns"
      :getNestedValue="getNestedValue"
    />

    <!-- Pagination -->
    <Pagination
      :current-page="currentPage"
      :total-pages="totalPages"
      @page-changed="goToPage"
      class="mt-6"
    />

    <!-- Modal Form (Thêm mới và Cập nhật) -->
    <ProductLineFormModal
      :show="showAddModal || showEditModal"
      :is-edit="showEditModal"
      :entity-name="'Nhà Sản Xuất'"
      :entity-data="manufacturer"
      :icon-class="showEditModal ? 'fa-edit' : 'fa-plus-circle'"
      :icon-color="showEditModal ? 'text-blue-500' : 'text-green-500'"
      @submit="handleFormSubmit"
      @close="closeModal"
    >
      <template #default="{ entityData }">
        <div class="grid grid-cols-1 gap-6">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Mã nhà sản xuất</label>
            <input
              v-model.trim="entityData.ma"
              type="text"
              placeholder="Nhập mã nhà sản xuất"
              class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
              required
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Tên nhà sản xuất</label>
            <input
              v-model.trim="entityData.nhaSanXuat"
              type="text"
              placeholder="Nhập tên nhà sản xuất"
              class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
              required
            />
          </div>
        </div>
      </template>
    </ProductLineFormModal>

    <!-- Modal Confirm -->
    <ConfirmModal
      :show="showConfirmModal"
      :message="confirmMessage"
      @confirm="executeConfirmedAction"
      @cancel="closeConfirmModal"
    />
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import useNhaSanXuat from './NhaSanXuat.js';
import ToastNotification from '@/components/ToastNotification.vue';
import Pagination from '@/components/Pagination.vue';
import ProductLineFormModal from '@/components/FormModal.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';
import DynamicTable from '@/components/DynamicTable.vue';

// Lấy tất cả từ useNhaSanXuat
const {
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
} = useNhaSanXuat();

// Hàm lấy giá trị lồng nhau
const getNestedValue = (obj, path) => {
  return path.split('.').reduce((acc, part) => acc && acc[part], obj);
};

// Bộ lọc bằng combobox
const searchNhaSanXuat = ref('');

// Danh sách tên nhà sản xuất duy nhất
const uniqueNhaSanXuatList = computed(() => {
  return [...new Set(manufacturers.value.map((item) => item.nhaSanXuat))].sort();
});

// Dữ liệu đã lọc
const filteredManufacturers = computed(() => {
  let filtered = manufacturers.value;
  if (searchKeyword.value) {
    filtered = filtered.filter((item) =>
      item.ma.toLowerCase().includes(searchKeyword.value.toLowerCase()) ||
      item.nhaSanXuat.toLowerCase().includes(searchKeyword.value.toLowerCase())
    );
  }
  if (searchNhaSanXuat.value) {
    filtered = filtered.filter((item) => item.nhaSanXuat === searchNhaSanXuat.value);
  }
  return filtered;
});

// Đặt lại bộ lọc
const resetFilters = () => {
  searchKeyword.value = '';
  searchNhaSanXuat.value = '';
  resetSearch();
};

// Theo dõi thay đổi để lọc tự động
watch([searchKeyword, searchNhaSanXuat], () => {
  if (searchKeyword.value || searchNhaSanXuat.value) {
    isSearching.value = true;
    searchManufacturer();
  } else {
    isSearching.value = false;
    fetchData();
  }
});

// Định nghĩa các cột cho DynamicTable
const columns = [
  {
    key: 'select',
    label: '',
    formatter: (value, item) => {
      return `
        <input type="checkbox" value="${item.id}" class="w-4 h-4 rounded" ${selectedManufacturers.value.includes(item.id) ? 'checked' : ''} onchange="document.dispatchEvent(new CustomEvent('toggleSelect', { detail: ${item.id} }))">
      `;
    },
  },
  {
    key: 'stt',
    label: 'STT',
    formatter: (value, item, index) => index + 1,
  },
  { key: 'ma', label: 'Mã' },
  { key: 'nhaSanXuat', label: 'Tên Nhà Sản Xuất' },
  {
    key: 'actions',
    label: 'Hành động',
    formatter: (value, item) => {
      const safeItem = JSON.stringify(item);
      return `
        <div class="space-x-4">
          <button class="text-blue-600 hover:text-blue-800 transition" data-item='${safeItem}' onclick="document.dispatchEvent(new CustomEvent('openEditModal', { detail: JSON.parse(this.dataset.item) }))">
            <i class="fa-solid fa-edit"></i>
          </button>
          <button class="text-red-600 hover:text-red-800 transition" data-id="${item.id}" onclick="document.dispatchEvent(new CustomEvent('confirmDelete', { detail: this.dataset.id }))">
            <i class="fa-solid fa-trash"></i>
          </button>
        </div>
      `;
    },
  },
];

// Xử lý các sự kiện từ DynamicTable
const handleCustomEvents = () => {
  document.addEventListener('openEditModal', (event) => {
    openEditModal(event.detail);
  });

  document.addEventListener('confirmDelete', (event) => {
    confirmDelete(event.detail);
  });

  document.addEventListener('toggleSelect', (event) => {
    const id = event.detail;
    if (selectedManufacturers.value.includes(id)) {
      selectedManufacturers.value = selectedManufacturers.value.filter((selectedId) => selectedId !== id);
    } else {
      selectedManufacturers.value.push(id);
    }
  });
};

handleCustomEvents();
</script>

<style scoped>
.fixed.inset-0 {
  overflow-y: auto;
}
</style>