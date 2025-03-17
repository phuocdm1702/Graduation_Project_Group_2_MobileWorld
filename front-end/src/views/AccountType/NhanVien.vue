<template>
  <!-- Thay Breadcrumb bằng BreadcrumbWrapper -->
  <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />
  <div v-if="visible" :class="`toast ${type}`">
    <span v-if="type === 'success'" class="checkmark">✔</span>
    <span v-if="type === 'error'" class="crossmark">✖</span>
    {{ message }}
  </div>
  <div>
    <div class="mt-2 mx-auto">
      <!-- Phần form (search và trạng thái) -->
      <div class="mt-2 w-full bg-white border rounded-md shadow-md">
        <form @submit.prevent>
          <div class="p-5 flex flex-col gap-6">
            <!-- Div chứa Search và Trạng thái -->
            <div class="flex items-center gap-6">
              <!-- Search -->
              <div class="flex-1">
                <label class="text-sm font-semibold block mb-2">Nhập thông tin tìm kiếm</label>
                <input
                  v-model="searchNV"
                  @input="btnSearch"
                  placeholder="Tìm theo mã,tên,email,sđt...."
                  type="text"
                  class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-400 outline-none"
                />
              </div>

              <!-- Trạng thái (Combobox) -->
              <div class="w-60">
                <label class="text-sm font-semibold block mb-2">Trạng thái</label>
                <select
                  v-model="filterStatus"
                  @change="onFilterChange"
                  class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-400 outline-none"
                >
                  <option value="tat-ca">Tất cả</option>
                  <option value="dang-lam">Đang làm</option>
                  <option value="da-nghi">Đã nghỉ</option>
                </select>
              </div>
            </div>

            <!-- Div chứa các button Thêm Nhân Viên và Nhập bằng Excel -->
            <div class="flex justify-end gap-4">
              <router-link to="/them-nhan-vien">
                <button class="flex items-center gap-2 px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition">
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5 mr-1">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15"/>
                  </svg>
                  Thêm Nhân Viên
                </button>
              </router-link>
              <button
                @click="importExcel"
                class="px-4 py-2 bg-green-500 text-white rounded-md flex items-center"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  viewBox="0 0 24 24"
                  fill="currentColor"
                  class="w-5 h-5 mr-1"
                >
                  <path
                    d="M19.5 3h-4.5V1.5H9V3H4.5C3.675 3 3 3.675 3 4.5v15c0 .825.675 1.5 1.5 1.5h15c.825 0 1.5-.675 1.5-1.5v-15c0-.825-.675-1.5-1.5-1.5zM9 19.5H7.5v-1.5H9v1.5zm0-3H7.5v-1.5H9v1.5zm0-3H7.5v-1.5H9v1.5zm0-3H7.5V9H9v1.5zm4.5 9H12v-1.5h1.5v1.5zm0-3H12v-1.5h1.5v1.5zm0-3H12v-1.5h1.5v1.5zm0-3H12V9h1.5v1.5zm3 9H15v-1.5h1.5v1.5zm0-3H15v-1.5h1.5v1.5zm0-3H15v-1.5h1.5v1.5zm0-3H15V9h1.5v1.5zM18 7.5H6V6h12v1.5z"
                  />
                </svg>
                Nhập bằng Excel
              </button>
            </div>
          </div>
        </form>
      </div>

      <!-- Phần bảng (DynamicTable) -->
      <div class="mt-2">
        <DynamicTable
          :data="dataTable"
          :columns="tableColumns"
          :get-nested-value="getNestedValue"
        />
      </div>
    </div>
    <br />

    <ConfirmModal
      :show="showConfirmModal"
      :message="'Bạn có chắc chắn muốn cho nhân viên này nghỉ?'"
      @confirm="deleteNv"
      @cancel="showConfirmModal = false"
    />
  </div>

  <footer class="bg-white shadow-lg rounded-lg p-4 flex justify-center items-center">
    <Pagination
      :current-page="currentPage"
      :total-pages="totalPages"
      @page-changed="goToPage"
    />
  </footer>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import ConfirmModal from "@/components/ConfirmModal.vue";
import DynamicTable from "@/components/DynamicTable.vue";
import Pagination from '@/components/Pagination.vue';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue'; // Import BreadcrumbWrapper
import useEmployeeManagement from "./useEmployeeManagement";

// Sử dụng logic từ file JS
const {
  visible,
  message,
  type,
  currentPage,
  goToPage,
  totalPages,
  employeeImage,
  previewImage,
  dataTable,
  showConfirmModal,
  selectedNVId,
  showDeleteConfirm,
  showToast,
  filterStatus,
  fetchNhanVien,
  deleteNv,
  searchNV,
  btnSearch,
  onFilterChange,
  tableColumns,
  getNestedValue,
  importExcel,
} = useEmployeeManagement();

// Lấy route hiện tại
const route = useRoute();

// Tính toán breadcrumb dựa trên meta của route
const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === "function") {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || ["Quản lý Nhân Viên"]; // Mặc định nếu không có breadcrumb
});
</script>

<style scoped>
.toast {
  position: fixed;
  top: 5%;
  right: 20px;
  padding: 10px 20px;
  border-radius: 5px;
  color: white;
}
.success {
  background: green;
}
.error {
  background: red;
}
.info {
  background: blue;
}
.anh-nhan-vien {
  width: 60px;
  height: 60px;
}
</style>