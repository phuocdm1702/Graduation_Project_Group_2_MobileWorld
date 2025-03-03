<template>
  <div class="container mx-auto p-4 relative">
    <!-- Toast thông báo -->
    <ToastNotification ref="toastRef" />

    <!-- Tiêu đề và nút mở modal -->
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-xl text-gray-600">Quản lý Màn Hình</h2>
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
        placeholder="Tìm kiếm theo mã, kích thước, độ phân giải..."
        class="flex-1 border rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
      />
      <button
        @click="searchManHinh"
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
    <div v-if="selectedManHinh.length" class="mb-6 flex justify-end">
      <button
        @click="confirmDeleteSelected"
        class="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 transition"
      >
        Xóa {{ selectedManHinh.length }} Màn hình đã chọn
      </button>
    </div>

    <!-- Danh sách Màn hình -->
    <div class="overflow-x-auto shadow-md rounded-lg max-w-full">
      <table class="min-w-full text-sm text-gray-500 table-auto">
        <thead class="bg-blue-100 text-blue-700 uppercase sticky top-0 z-10">
        <tr>
          <th class="px-4 py-3 text-center whitespace-nowrap">ID</th>
          <th class="px-4 py-3 text-center whitespace-nowrap">ID Công nghệ</th>
          <th class="px-4 py-3 text-center whitespace-nowrap">Mã</th>
          <th class="px-4 py-3 text-center whitespace-nowrap">Kích thước</th>
          <th class="px-4 py-3 text-center whitespace-nowrap">Độ phân giải</th>
          <th class="px-4 py-3 text-center whitespace-nowrap">Độ sáng tối đa</th>
          <th class="px-4 py-3 text-center whitespace-nowrap">Tần số quét</th>
          <th class="px-4 py-3 text-center whitespace-nowrap">Kiểu màn hình</th>
          <th class="px-4 py-3 text-center whitespace-nowrap">Hành động</th>
          <th class="px-4 py-3 text-center whitespace-nowrap">
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
          v-for="item in manHinhs"
          :key="item.id"
          class="bg-white border-b hover:bg-gray-50 transition"
        >
          <td class="px-4 py-4 text-center whitespace-nowrap">{{ item.id }}</td>
          <td class="px-4 py-4 text-center whitespace-nowrap">{{ item.idCongNgheManHinh }}</td>
          <td class="px-4 py-4 text-center whitespace-nowrap">{{ item.ma }}</td>
          <td class="px-4 py-4 text-center whitespace-nowrap">{{ item.kichThuoc }}</td>
          <td class="px-4 py-4 text-center whitespace-nowrap">{{ item.doPhanGiai }}</td>
          <td class="px-4 py-4 text-center whitespace-nowrap">{{ item.doSangToiDa }}</td>
          <td class="px-4 py-4 text-center whitespace-nowrap">{{ item.tanSoQuet }}</td>
          <td class="px-4 py-4 text-center whitespace-nowrap">{{ item.kieuManHinh }}</td>
          <td class="px-4 py-4 text-center whitespace-nowrap space-x-4">
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
          <td class="px-4 py-4 text-center whitespace-nowrap">
            <input
              type="checkbox"
              v-model="selectedManHinh"
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
      <div class="bg-white p-6 rounded-xl shadow-lg border border-gray-100 w-full max-w-2xl z-50 max-h-[90vh] overflow-y-auto">
        <h2 class="text-2xl font-semibold text-gray-800 mb-6 flex items-center">
          <i :class="`fa-solid ${showEditModal ? 'fa-edit' : 'fa-plus-circle'} ${showEditModal ? 'text-blue-500' : 'text-green-500'} mr-2`"></i>
          {{ showEditModal ? 'Cập Nhật Màn Hình' : 'Thêm Màn Hình' }}
        </h2>
        <form @submit.prevent="handleFormSubmit" class="space-y-4">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">ID Công nghệ màn hình</label>
              <select
                v-model="manHinh.idCongNgheManHinh"
                class="w-full border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
                required
              >
                <option v-for="tech in congNgheManHinhs" :key="tech.id" :value="tech.id">{{ tech.congNgheManHinh }}</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Mã</label>
              <input
                v-model.trim="manHinh.ma"
                type="text"
                placeholder="Nhập mã màn hình"
                class="w-full border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
                required
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Kích thước</label>
              <input
                v-model.trim="manHinh.kichThuoc"
                type="text"
                placeholder="Nhập kích thước (inch)"
                class="w-full border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
                required
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Độ phân giải</label>
              <input
                v-model.trim="manHinh.doPhanGiai"
                type="text"
                placeholder="Nhập độ phân giải"
                class="w-full border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
                required
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Độ sáng tối đa</label>
              <input
                v-model.trim="manHinh.doSangToiDa"
                type="text"
                placeholder="Nhập độ sáng tối đa (nits)"
                class="w-full border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
                required
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Tần số quét</label>
              <input
                v-model.trim="manHinh.tanSoQuet"
                type="text"
                placeholder="Nhập tần số quét (Hz)"
                class="w-full border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
                required
              />
            </div>
            <div class="md:col-span-2">
              <label class="block text-sm font-medium text-gray-700 mb-1">Kiểu màn hình</label>
              <input
                v-model.trim="manHinh.kieuManHinh"
                type="text"
                placeholder="Nhập kiểu màn hình"
                class="w-full border border-gray-300 rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
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
import useManHinh from './useManHinh.js';
import ToastNotification from '@/components/ToastNotification.vue';
import Pagination from '@/components/Pagination.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';
import { ref, onMounted } from 'vue';

const toastRef = ref(null);
const {
  toast,
  manHinhs,
  congNgheManHinhs,
  manHinh,
  searchKeyword,
  currentPage,
  pageSize,
  totalItems,
  selectedManHinh,
  selectAll,
  isSearching,
  showAddModal,
  showEditModal,
  showConfirmModal,
  confirmMessage,
  totalPages,
  fetchCongNgheManHinhs,
  fetchData,
  goToPage,
  searchManHinh,
  resetSearch,
  checkDuplicate,
  saveManHinh,
  updateManHinh,
  deleteManHinh,
  deleteSelectedManHinh,
  openAddModal,
  openEditModal,
  closeModal,
  handleFormSubmit,
  confirmDelete,
  confirmDeleteSelected,
  confirmAction,
  executeConfirmedAction,
  closeConfirmModal,
  toggleSelectAll
} = useManHinh(toastRef);

onMounted(() => {
  toast.value = toastRef;
  fetchCongNgheManHinhs(); // Lấy danh sách công nghệ màn hình
  fetchData(); // Lấy danh sách màn hình
});
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
.min-w-full {
  min-width: 100%;
}
.table-auto {
  table-layout: auto;
}
.whitespace-nowrap {
  white-space: nowrap;
}
.max-w-2xl {
  max-width: 48rem; /* Tăng kích thước modal */
}
.max-h-[90vh] {
  max-height: 90vh; /* Giới hạn chiều cao modal */
}
.overflow-y-auto {
  overflow-y: auto; /* Thêm thanh cuộn dọc nếu nội dung vượt quá */
}
</style>