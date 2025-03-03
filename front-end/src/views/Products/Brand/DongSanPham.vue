<template>
  <div class="container mx-auto p-4 relative">
    <!-- Toast thông báo -->
    <ToastNotification ref="toast" />

    <!-- Tiêu đề và nút mở modal -->
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-xl text-gray-600">Quản lý Dòng Sản Phẩm</h2>
      <button
        @click="openAddModal"
        class="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition"
      >
        Thêm mới
      </button>
    </div>

    <!-- Search Section -->
    <div class="flex gap-3 mb-6">
      <input
        v-model.trim="searchKeyword"
        type="text"
        placeholder="Tìm kiếm theo mã hoặc tên..."
        class="flex-1 border rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
      />
      <button
        @click="searchProductLine"
        class="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition"
      >
        Tìm kiếm
      </button>
      <button
        @click="resetSearch"
        class="bg-gray-400 text-white px-4 py-2 rounded-lg hover:bg-gray-500 transition"
      >
        Đặt lại
      </button>
    </div>

    <!-- Bulk Delete Button -->
    <div v-if="selectedProducts.length" class="mb-6 flex justify-end">
      <button
        @click="confirmDeleteSelected"
        class="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 transition"
      >
        Xóa {{ selectedProducts.length }} dòng sản phẩm đã chọn
      </button>
    </div>

    <!-- Product Lines Table -->
    <div class="overflow-x-auto shadow-md rounded-lg">
      <table class="w-full text-sm text-gray-500">
        <thead class="bg-blue-100 text-blue-700 uppercase">
        <tr>
          <th class="px-6 py-3 text-center">ID</th>
          <th class="px-6 py-3 text-center">Mã</th>
          <th class="px-6 py-3 text-center">Tên Dòng Sản Phẩm</th>
          <th class="px-6 py-3 text-center">Hành động</th>
          <th class="px-6 py-3 text-center">
            <input
              type="checkbox"
              v-model="selectAll"
              @change="toggleSelectAll"
              class="w-4 h-4 rounded"
            />
          </th>
        </tr>
        </thead>
        <tbody>
        <tr
          v-for="product in productLines"
          :key="product.id"
          class="bg-white border-b hover:bg-gray-50 transition"
        >
          <td class="px-6 py-4 text-center">{{ product.id }}</td>
          <td class="px-6 py-4 text-center">{{ product.ma }}</td>
          <td class="px-6 py-4 text-center">{{ product.dongSanPham }}</td>
          <td class="px-6 py-4 text-center space-x-4">
            <button
              @click="openEditModal(product)"
              class="text-blue-600 hover:text-blue-800 transition"
            >
              <i class="fa-solid fa-edit"></i>
            </button>
            <button
              @click="confirmDelete(product.id)"
              class="text-red-600 hover:text-red-800 transition"
            >
              <i class="fa-solid fa-trash"></i>
            </button>
          </td>
          <td class="px-6 py-4 text-center">
            <input
              type="checkbox"
              v-model="selectedProducts"
              :value="product.id"
              class="w-4 h-4 rounded"
            />
          </td>
        </tr>
        </tbody>
      </table>
    </div>

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
import useProductLineList from './DongSanPham.js';
import ToastNotification from '@/components/ToastNotification.vue';
import Pagination from '@/components/Pagination.vue';
import ProductLineFormModal from '@/components/FormModal.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';

// Lấy tất cả từ useProductLineList
const {
  toast,
  productLines,
  productLine,
  searchKeyword,
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
</script>

<style scoped>
.fixed.inset-0 {
  overflow-y: auto;
}
</style>