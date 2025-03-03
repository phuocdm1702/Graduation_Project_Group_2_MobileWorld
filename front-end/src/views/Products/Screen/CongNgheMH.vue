<template>
  <div class="container mx-auto p-4 relative">
    <!-- Toast thông báo -->
    <ToastNotification ref="toast" />

    <!-- Tiêu đề và nút mở modal -->
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-xl text-gray-600">Quản lý Công Nghệ Màn Hình</h2>
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
        placeholder="Tìm kiếm theo mã, tên hoặc chuẩn màn hình..."
        class="flex-1 border rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
      />
      <button
        @click="searchCongNgheManHinh"
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
    <div v-if="selectedCongNgheManHinh.length" class="mb-6 flex justify-end">
      <button
        @click="confirmDeleteSelected"
        class="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 transition"
      >
        Xóa {{ selectedCongNgheManHinh.length }} Công nghệ màn hình đã chọn
      </button>
    </div>

    <!-- Công nghệ màn hình Table -->
    <div class="overflow-x-auto shadow-md rounded-lg">
      <table class="w-full text-sm text-gray-500">
        <thead class="bg-blue-100 text-blue-700 uppercase">
        <tr>
          <th class="px-6 py-3 text-center">ID</th>
          <th class="px-6 py-3 text-center">Mã</th>
          <th class="px-6 py-3 text-center">Tên</th>
          <th class="px-6 py-3 text-center">Chuẩn màn hình</th>
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
          v-for="item in congNgheManHinhs"
          :key="item.id"
          class="bg-white border-b hover:bg-gray-50 transition"
        >
          <td class="px-6 py-4 text-center">{{ item.id }}</td>
          <td class="px-6 py-4 text-center">{{ item.ma }}</td>
          <td class="px-6 py-4 text-center">{{ item.congNgheManHinh }}</td>
          <td class="px-6 py-4 text-center">{{ item.chuanManHinh }}</td>
          <td class="px-6 py-4 text-center space-x-4">
            <button
              @click="openEditModal(item)"
              class="text-blue-600 hover:text-blue-800 transition"
            >
              <i class="fa-solid fa-edit"></i>
            </button>
            <button
              @click="confirmDelete(item.id)"
              class="text-red-600 hover:text-red-800 transition"
            >
              <i class="fa-solid fa-trash"></i>
            </button>
          </td>
          <td class="px-6 py-4 text-center">
            <input
              type="checkbox"
              v-model="selectedCongNgheManHinh"
              :value="item.id"
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
    <div v-if="showAddModal || showEditModal" class="fixed inset-0 flex items-center justify-center z-50">
      <!-- Overlay tối màu -->
      <div class="fixed inset-0 bg-black bg-opacity-50 z-40" @click="closeModal"></div>

      <!-- Modal -->
      <div class="bg-white p-6 rounded-xl shadow-lg border border-gray-100 max-w-md w-full z-50">
        <h2 class="text-2xl font-semibold text-gray-800 mb-6 flex items-center">
          <i :class="`fa-solid ${showEditModal ? 'fa-edit' : 'fa-plus-circle'} ${showEditModal ? 'text-blue-500' : 'text-green-500'} mr-2`"></i>
          {{ showEditModal ? 'Cập Nhật Công Nghệ Màn Hình' : 'Thêm Công Nghệ Màn Hình' }}
        </h2>
        <form @submit.prevent="handleFormSubmit" class="space-y-6">
          <div class="grid grid-cols-1 gap-6">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Mã Công Nghệ</label>
              <input
                v-model.trim="congNgheManHinh.ma"
                type="text"
                placeholder="Nhập mã công nghệ"
                class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
                required
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Tên Công Nghệ</label>
              <input
                v-model.trim="congNgheManHinh.congNgheManHinh"
                type="text"
                placeholder="Nhập tên công nghệ"
                class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
                required
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Chuẩn Màn Hình</label>
              <input
                v-model.trim="congNgheManHinh.chuanManHinh"
                type="text"
                placeholder="Nhập chuẩn màn hình"
                class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
                required
              />
            </div>
          </div>
          <div class="flex justify-end gap-4">
            <button
              type="button"
              @click="closeModal"
              class="bg-gray-200 text-gray-700 px-5 py-2 rounded-lg hover:bg-gray-300 transition-all duration-200 font-medium"
            >
              Hủy
            </button>
            <button
              type="submit"
              :class="showEditModal ? 'bg-blue-600 hover:bg-blue-700' : 'bg-green-600 hover:bg-green-700'"
              class="text-white px-5 py-2 rounded-lg transition-all duration-200 font-medium flex items-center"
            >
              <i class="fa-solid fa-save mr-2"></i> {{ showEditModal ? 'Cập nhật' : 'Thêm mới' }}
            </button>
          </div>
        </form>
      </div>
    </div>

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
import useCongNgheManHinh from './useCongNgheManHinh.js'; // Sửa tên file import
import ToastNotification from '@/components/ToastNotification.vue';
import Pagination from '@/components/Pagination.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';

// Gọi hàm useCongNgheManHinh và lấy tất cả giá trị trả về
const {
  toast,
  congNgheManHinhs,
  congNgheManHinh,
  searchKeyword,
  currentPage,
  pageSize,
  totalItems,
  selectedCongNgheManHinh,
  selectAll,
  isSearching,
  showAddModal,
  showEditModal,
  showConfirmModal,
  confirmMessage,
  totalPages,
  fetchData,
  goToPage,
  searchCongNgheManHinh,
  resetSearch,
  checkDuplicate,
  saveCongNgheManHinh,
  updateCongNgheManHinh,
  deleteCongNgheManHinh,
  deleteSelectedCongNgheManHinh,
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
} = useCongNgheManHinh(); // Gọi hàm với ()
</script>

<style scoped>
.fixed.inset-0 {
  overflow-y: auto;
}
:root {
  --ring-color: #10b981; /* Màu xanh lá cho Thêm mới */
}
div[z-50] {
  --ring-color: #3b82f6; /* Màu xanh dương cho Cập nhật */
}
</style>