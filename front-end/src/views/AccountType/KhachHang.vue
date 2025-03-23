<template>
  <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />
  <ToastNotification ref="toastRef" />
  <div>
    <div class="mt-2 mx-auto">
      <div class="mt-2 bg-white border rounded-md shadow-md">
        <form @submit.prevent>
          <div class="p-5 flex flex-col gap-6">
            <div class="flex items-center gap-6">
              <div class="flex-1">
                <label class="text-sm font-semibold block mb-2">Nhập thông tin tìm kiếm</label>
                <input
                  v-model="searchKH"
                  @input="btnSearch"
                  placeholder="Tìm theo mã, tên, email, sdt..."
                  type="text"
                  class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-400 outline-none"
                />
              </div>
              <div class="w-60">
                <label class="text-sm font-semibold block mb-2">Trạng thái</label>
                <select
                  v-model="filterStatus"
                  @change="onFilterChange"
                  class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-400 outline-none"
                >
                  <option value="tat-ca">Tất cả</option>
                  <option value="kich-hoat">Kích hoạt</option>
                  <option value="huy-kich-hoat">Hủy kích hoạt</option>
                </select>
              </div>
            </div>
            <div class="flex justify-end gap-4">
              <router-link to="/them-khach-hang">
                <button
                  class="flex items-center gap-2 px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke-width="1.5"
                    stroke="currentColor"
                    class="w-5 h-5 mr-1"
                  >
                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
                  </svg>
                  Thêm Khách Hàng
                </button>
              </router-link>
              <label
                for="excel-upload"
                class="px-4 py-2 bg-blue-500 text-white rounded-md flex items-center cursor-pointer hover:bg-blue-600 transition"
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
                Nhập từ Excel
                <input
                  id="excel-upload"
                  ref="fileInputRef"
                  type="file"
                  @change="importExcel"
                  accept=".xlsx, .xls"
                  class="hidden"
                />
              </label>
              <button
                @click="exportToExcel"
                class="flex items-center gap-2 px-4 py-2 bg-green-500 text-white font-semibold rounded-lg shadow-md hover:bg-green-600 transition"
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
                Xuất Excel
              </button>
              <button
                @click="downloadTemplate"
                class="flex items-center gap-2 px-4 py-2 bg-gray-500 text-white font-semibold rounded-lg shadow-md hover:bg-gray-600 transition"
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
                Tài liệu mẫu
              </button>
            </div>
          </div>
        </form>
      </div>

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
      :message="'Bạn có chắc chắn muốn hủy kích hoạt của khách hàng?'"
      @confirm="confirmDelete"
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

  <div v-if="isLoading" class="fixed inset-0 flex items-center justify-center bg-gray-500 bg-opacity-50 z-50">
    <div class="animate-spin rounded-full h-16 w-16 border-t-4 border-blue-500"></div>
  </div>
</template>

<script setup>
import { onMounted, ref, computed, nextTick } from "vue";
import { useRoute } from "vue-router";
import ConfirmModal from "@/components/ConfirmModal.vue";
import DynamicTable from "@/components/DynamicTable.vue";
import Pagination from "@/components/Pagination.vue";
import BreadcrumbWrapper from "@/components/BreadcrumbWrapper.vue";
import ToastNotification from "@/components/ToastNotification.vue";
import useCustomerManagement from "./useCustomerManagement";

const toastRef = ref(null);
const fileInputRef = ref(null);

const customerManagement = useCustomerManagement(toastRef);

const {
  dataTable,
  searchKH,
  filterStatus,
  currentPage,
  totalPages,
  visible,
  message,
  type,
  showConfirmModal,
  selectedCustomerId,
  showToast,
  fetchCustomers,
  btnSearch,
  onFilterChange,
  backSearch,
  showDeleteConfirm,
  confirmDelete,
  goToPage,
  importExcel,
  exportToExcel,
  downloadTemplate,
  tableColumns,
  getNestedValue,
  isLoading,
  fileInputRef: composableFileInputRef,
} = customerManagement;

// Sync the template ref with the composable ref
onMounted(() => {
  composableFileInputRef.value = fileInputRef.value;
});

const route = useRoute();

const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === "function") {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || ["Quản lý Khách Hàng"];
});

onMounted(async () => {
  fetchCustomers();
  await nextTick();
  if (route.query.status === "success") {
    if (toastRef.value) {
      toastRef.value.kshowToast("success", "Thêm khách hàng thành công!");
      window.history.replaceState({}, document.title, "/khach-hang");
    } else {
      console.error("toastRef is null trong CustomerManagement.vue");
    }
  }
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
  z-index: 1000;
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

input[type="file"] {
  display: none;
}
</style>