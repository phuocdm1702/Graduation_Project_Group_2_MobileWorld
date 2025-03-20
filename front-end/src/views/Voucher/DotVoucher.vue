<template>
  <div>
    <!-- Thay Breadcrumb bằng BreadcrumbWrapper -->
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />
    <div class="mt-2 mx-auto">
      <ToastNotification ref="toast"/>
      <!-- Form lọc -->
      <div class="bg-white shadow-lg rounded-lg p-5 mb-2 mt-2 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">
        <!-- Ô tìm kiếm -->
        <div>
          <label for="searchQuery" class="block text-sm font-medium text-gray-700 mb-1">Tìm kiếm đợt giảm giá</label>
          <input
            v-model="searchQuery"
            id="searchQuery"
            type="text"
            placeholder="Tìm theo mã hoặc tên đợt giảm giá..."
            class="input-field"
            @input="currentPage = 0; fetchData()"
          />
        </div>

        <!-- Dropdown chọn loại phiếu -->
        <div>
          <label for="filterType" class="block text-sm font-medium text-gray-700 mb-1">Loại phiếu</label>
          <select
            v-model="filterType"
            id="filterType"
            class="input-field"
            @change="currentPage = 0; fetchData()"
          >
            <option value="">Tất cả loại phiếu</option>
            <option value="Phần trăm">Phần trăm</option>
            <option value="Tiền mặt">Tiền mặt</option>
          </select>
        </div>

        <!-- Dropdown chọn trạng thái -->
        <div>
          <label for="filterStatus" class="block text-sm font-medium text-gray-700 mb-1">Trạng thái</label>
          <select
            v-model="filterStatus"
            id="filterStatus"
            class="input-field"
            @change="currentPage = 0; fetchData()"
          >
            <option value="">Tất cả trạng thái</option>
            <option value="0">Đang diễn ra</option>
            <option value="1">Sắp diễn ra</option>
            <option value="deleted">Đã kết thúc</option>
          </select>
        </div>

        <!-- Chọn ngày bắt đầu -->
        <div>
          <label for="startDate" class="block text-sm font-medium text-gray-700 mb-1">Ngày bắt đầu</label>
          <input
            v-model="startDate"
            id="startDate"
            type="date"
            class="input-field"
            @change="currentPage = 1; fetchData()"
          />
        </div>

        <!-- Chọn ngày kết thúc -->
        <div>
          <label for="endDate" class="block text-sm font-medium text-gray-700 mb-1">Ngày kết thúc</label>
          <input
            v-model="endDate"
            id="endDate"
            type="date"
            class="input-field"
            @change="currentPage = 1; fetchData()"
          />
        </div>

        <!-- Ô nhập giá trị giảm -->
        <div>
          <label for="saleValue" class="block text-sm font-medium text-gray-700 mb-1">Giá trị giảm</label>
          <input
            v-model="saleValue"
            id="saleValue"
            type="number"
            placeholder="Giá trị giảm"
            class="input-field"
            @input="currentPage = 1; fetchData()"
          />
        </div>

        <!-- Ô nhập số tiền giảm tối đa -->
        <div>
          <label for="minOrder" class="block text-sm font-medium text-gray-700 mb-1">Số tiền giảm tối đa</label>
          <input
            v-model="minOrder"
            id="minOrder"
            type="number"
            placeholder="Số tiền giảm tối đa"
            class="input-field"
            @input="currentPage = 1; fetchData()"
          />
        </div>

        <!-- Checkbox Đã kết thúc -->
<!--        <div class="flex items-center space-x-2">-->
<!--          <input-->
<!--            type="checkbox"-->
<!--            v-model="deleted"-->
<!--            id="deleted"-->
<!--            @change="currentPage = 1; fetchData()"-->
<!--            class="w-5 h-5 rounded focus:ring-2 focus:ring-blue-400"-->
<!--          />-->
<!--          <label for="deleted" class="block text-sm font-medium text-gray-700">Đã kết thúc</label>-->
<!--        </div>-->

        <div class="flex justify-end w-full col-span-full gap-2">
          <!-- Xuất Excel -->
          <button @click="exportExcel"
                  class="px-4 py-2 bg-green-600 text-white font-semibold rounded-lg shadow-md hover:bg-blue-600 transition flex items-center gap-2">
            <i class="fa fa-file-excel text-white text-lg"></i>
            Xuất Excel
          </button>

          <!-- View Add -->
          <RouterLink to="/ViewAddDotGiamGia">
            <button
              class="flex items-center gap-2 px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                   stroke="currentColor" class="w-5 h-5 mr-1">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15"/>
              </svg>
              Thêm Đợt Giảm Giá
            </button>
          </RouterLink>
        </div>
      </div>

      <!-- Bảng danh sách đợt giảm giá -->
      <DynamicTable
        class="dynamic-table"
        :data="dataTable"
        :columns="columns"
        :get-nested-value="getNestedValue"
      />

      <!-- Phân trang -->
      <footer class="bg-white shadow-lg rounded-lg p-4 flex justify-center items-center mt-2">
        <Pagination
          :current-page="currentPage"
          :total-pages="totalPages"
          @page-changed="changePage"
        />
      </footer>
    </div>

    <ConfirmModal
      :show="showConfirmModal"
      :message="confirmMessage"
      @confirm="executeConfirmedAction"
      @cancel="closeConfirmModal"
    />
  </div>
</template>

<script setup>
import { useDiscountManagement } from './DotVoucher.js';
import "@vuepic/vue-datepicker/dist/main.css";
import DynamicTable from "@/components/DynamicTable.vue";
import Pagination from '@/components/Pagination.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';
import ToastNotification from '@/components/ToastNotification.vue';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue'; // Import BreadcrumbWrapper
import { computed } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();

// Tính toán breadcrumb dựa trên meta của route
const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === "function") {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || ["Đợt Giảm Giá"]; // Mặc định nếu không có breadcrumb
});

const {
  toast,
  currentPage,
  pageSize,
  totalPages,
  searchQuery,
  filterType,
  filterStatus,
  startDate,
  endDate,
  minOrder,
  saleValue,
  deleted,
  dataTable,
  changePage,
  fetchData,
  columns,
  getNestedValue,
  showConfirmModal,
  confirmMessage,
  executeConfirmedAction,
  closeConfirmModal,
  exportExcel
} = useDiscountManagement();
</script>

<style scoped>
.input-field {
  @apply w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-400 outline-none;
}
</style>