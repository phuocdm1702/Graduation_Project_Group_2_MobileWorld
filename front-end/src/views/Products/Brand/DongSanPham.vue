<template>
  <div class="container mx-auto p-4 relative">
    <ToastNotification ref="toast" />
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-xl text-gray-600">Quản lý Dòng Sản Phẩm</h2>
      <button
        @click="openAddModal"
        class="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition"
      >
        Thêm mới
      </button>
    </div>
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
        <label class="block text-sm font-medium text-gray-700 mb-1">Tên dòng sản phẩm</label>
        <select
          v-model="searchDongSanPham"
          class="w-full border rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
        >
          <option value="">Tất cả</option>
          <option v-for="name in uniqueDongSanPhamList" :key="name" :value="name">{{ name }}</option>
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
    <div v-if="selectedProducts.length" class="mb-6 flex justify-end">
      <button
        @click="confirmDeleteSelected"
        class="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 transition"
      >
        Xóa {{ selectedProducts.length }} dòng sản phẩm đã chọn
      </button>
    </div>
    <DynamicTable
      :data="productLines"
      :columns="columns"
      :getNestedValue="getNestedValue"
    />
    <Pagination
      :current-page="currentPage"
      :total-pages="totalPages"
      @page-changed="goToPage"
      class="mt-6"
    />
    <ProductLineFormModal
      :show="showAddModal || showEditModal"
      :is-edit="showEditModal"
      :entity-name="'Dòng Sản Phẩm'"
      :entity-data="productLine"
      :icon-class="showEditModal ? 'fa-edit' : 'fa-plus-circle'"
      :icon-color="showEditModal ? 'text-blue-500' : 'text-green-500'"
      @submit="handleFormSubmit"
      @close="closeModal"
    >
      <template #default="{ entityData }">
        <div class="grid grid-cols-1 gap-6">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Mã dòng sản phẩm</label>
            <input
              v-model.trim="entityData.ma"
              type="text"
              placeholder="Nhập mã dòng sản phẩm"
              class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
              required
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Tên dòng sản phẩm</label>
            <input
              v-model.trim="entityData.dongSanPham"
              type="text"
              placeholder="Nhập tên dòng sản phẩm"
              class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
              required
            />
          </div>
        </div>
      </template>
    </ProductLineFormModal>
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
import useProductLineList from './DongSanPham.js';
import ToastNotification from '@/components/ToastNotification.vue';
import Pagination from '@/components/Pagination.vue';
import ProductLineFormModal from '@/components/FormModal.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';
import DynamicTable from '@/components/DynamicTable.vue';
import axios from 'axios';

const {
  toast,
  productLines,
  productLine,
  searchKeyword,
  searchDongSanPham,
  currentPage,
  pageSize,
  totalItems,
  selectedProducts,
  selectAll,
  isSearching,
  showAddModal,
  showEditModal,
  showConfirmModal,
  confirmMessage,
  totalPages,
  fetchData,
  goToPage,
  searchProductLine,
  resetSearch,
  checkDuplicate,
  saveProductLine,
  updateProductLine,
  deleteProductLine,
  deleteSelectedProducts,
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
} = useProductLineList();

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

watch([searchKeyword, searchDongSanPham], () => {
  currentPage.value = 0; // Reset về trang đầu khi thay đổi bộ lọc
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
    label: '',
    formatter: (value, item) => {
      return `
        <input type="checkbox" value="${item.id}" class="w-4 h-4 rounded" ${selectedProducts.value.includes(item.id) ? 'checked' : ''} onchange="document.dispatchEvent(new CustomEvent('toggleSelect', { detail: ${item.id} }))">
      `;
    },
  },
  {
    key: 'stt',
    label: 'STT',
    formatter: (value, item, index) => index + 1,
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

const handleCustomEvents = () => {
  document.addEventListener('openEditModal', (event) => {
    openEditModal(event.detail);
  });
  document.addEventListener('confirmDelete', (event) => {
    confirmDelete(event.detail);
  });
  document.addEventListener('toggleSelect', (event) => {
    const id = event.detail;
    if (selectedProducts.value.includes(id)) {
      selectedProducts.value = selectedProducts.value.filter((selectedId) => selectedId !== id);
    } else {
      selectedProducts.value.push(id);
    }
  });
};

onMounted(() => {
  fetchData();
  fetchProductLineNames();
});

handleCustomEvents();
</script>

<style scoped>
.fixed.inset-0 {
  overflow-y: auto;
}
</style>