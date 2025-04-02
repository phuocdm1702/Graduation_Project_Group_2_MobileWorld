<template>
  <div class="mt-2 mx-auto">
    <ToastNotification ref="toast"/>
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />

    <section>
      <!-- Form lọc -->
      <div class="bg-white shadow-lg rounded-lg p-5 mb-4 mt-4 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">
        <!-- Ô tìm kiếm -->
        <div>
          <label for="searchQuery" class="block text-sm font-medium text-gray-700 mb-1">Tìm kiếm hóa đơn</label>
          <input
            v-model="keyword"
            id="searchQuery"
            type="text"
            placeholder="Tìm kiếm hóa đơn..."
            class="input-field"
            @input="currentPage = 0; applyFilters()"
          />
        </div>

        <!-- Combobox lọc theo loại đơn -->
        <div>
          <label for="orderType" class="block text-sm font-medium text-gray-700 mb-1">Loại đơn</label>
          <select
            v-model="selectedOrderType"
            id="orderType"
            class="input-field"
            @change="currentPage = 0; applyFilters()"
          >
            <option value="">Tất cả loại đơn</option>
            <option v-for="type in orderTypes" :key="type.value" :value="type.value">
              {{ type.label }}
            </option>
          </select>
        </div>

        <!-- Thanh scroll lọc khoảng giá -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Khoảng giá</label>
          <div class="relative pt-1">
            <div class="flex mb-2 items-center justify-between">
              <span class="text-xs font-semibold text-gray-700">{{ minAmount.toLocaleString() }} VND</span>
              <span class="text-xs font-semibold text-gray-700">{{ maxAmount.toLocaleString() }} VND</span>
            </div>
            <div class="range-slider">
              <input
                type="range"
                v-model.number="minAmount"
                :min="minRange"
                :max="maxRange"
                class="range-input range-min"
                @input="adjustMin(); currentPage = 0; applyFilters()"
              />
              <input
                type="range"
                v-model.number="maxAmount"
                :min="minRange"
                :max="maxRange"
                class="range-input range-max"
                @input="adjustMax(); currentPage = 0; applyFilters()"
              />
            </div>
          </div>
        </div>

        <!-- Chọn ngày bắt đầu -->
        <div>
          <label for="startDate" class="block text-sm font-medium text-gray-700 mb-1">Ngày bắt đầu</label>
          <input
            v-model="startDate"
            id="startDate"
            type="date"
            class="input-field"
            @change="currentPage = 0; applyFilters()"
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
            @change="currentPage = 0; applyFilters()"
          />
        </div>

        <!-- Nút hành động -->
        <div class="flex justify-end w-full col-span-full gap-2">
          <button
            @click="exportExcel"
            class="px-4 py-2 bg-green-600 text-white font-semibold rounded-lg shadow-md hover:bg-green-700 transition flex items-center gap-2"
          >
            <i class="fa fa-file-excel text-white text-lg"></i>
            Xuất Excel
          </button>
          <button
            @click="openQrScanner"
            class="flex items-center gap-2 px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M3 3h4v4H3z"></path>
              <path d="M17 3h4v4h-4z"></path>
              <path d="M3 17h4v4H3z"></path>
              <path d="M17 17h4v4h-4z"></path>
              <path d="M7 7h4v4H7z"></path>
              <path d="M7 17h4"></path>
              <path d="M7 13h8v8"></path>
              <path d="M17 7h-4v4"></path>
            </svg>
            Quét QR
          </button>
          <RouterLink to="/create-hoa-don">
            <button class="flex items-center gap-2 px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5 mr-1">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15"/>
              </svg>
              Tạo Hóa Đơn
            </button>
          </RouterLink>
        </div>
      </div>

      <!-- QR Scanner Modal -->
      <div v-if="showQrScanner" class="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center">
        <div class="bg-white p-4 rounded-lg">
          <video ref="videoElement" class="w-64 h-64"></video>
          <canvas ref="canvasElement" style="display: none;"></canvas>
          <button
            @click="stopQrScanner"
            class="mt-4 bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded w-full"
          >
            Đóng
          </button>
        </div>
      </div>
    </section>

    <section>
      <StatusBar @filter-by-status="handleStatusFilter" />
      <DynamicTable
        class="dynamic-table"
        :data="dataTable"
        :columns="columns"
        :get-nested-value="getNestedValue"
      />
      <div class="bg-white shadow-lg rounded-lg p-4 flex justify-center items-center mt-2">
        <Pagination
          :current-page="currentPage"
          :total-pages="totalPages"
          @page-changed="goToPage"
        />
      </div>
    </section>

    <ConfirmModal
      :show="showConfirmModal"
      :message="confirmMessage"
      @confirm="executeConfirmedAction"
      @cancel="closeConfirmModal"
    />
  </div>
</template>

<script setup>
import { computed, onUnmounted } from "vue";
import { useRoute } from "vue-router";
import useHoaDonLineList from "@/views/Bill/JS/HoaDon.js";
import DynamicTable from "@/components/DynamicTable.vue";
import Pagination from "@/components/Pagination.vue";
import StatusBar from "@/components/statusBar.vue";
import ToastNotification from '@/components/ToastNotification.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue';

const {
  toast,
  dataTable,
  currentPage,
  totalPages,
  keyword,
  minAmount,
  maxAmount,
  selectedOrderType,
  startDate,
  endDate,
  orderTypes,
  columns,
  getNestedValue,
  applyFilters,
  goToPage,
  exportExcel,
  showConfirmModal,
  confirmMessage,
  executeConfirmedAction,
  closeConfirmModal,
  confirmAction,
  minRange,
  maxRange,
  adjustMin,
  adjustMax,
  showQrScanner,
  openQrScanner,
  stopQrScanner,
  videoElement,
  canvasElement,
  downloadQrCode, // Import the downloadQrCode function
  selectedStatus,
  handleStatusFilter,
} = useHoaDonLineList();

const route = useRoute();

const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === "function") {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || ["Quản lý hóa đơn"];
});

onUnmounted(() => {
  stopQrScanner();
});
</script>

<style scoped>
.input-field {
  @apply w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-sm;
}

.dynamic-table {
  border-top-left-radius: 0px;
  border-top-right-radius: 0px;
}

/* Range slider styles */
.range-slider {
  position: relative;
  width: 100%;
  height: 20px;
}

.range-input {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
  background: none;
  -webkit-appearance: none;
  pointer-events: none;
}

.range-input::-webkit-slider-runnable-track {
  width: 100%;
  height: 6px;
  background: #ddd;
  border-radius: 3px;
}

.range-input::-webkit-slider-thumb {
  height: 16px;
  width: 16px;
  border-radius: 50%;
  background: #007bff;
  cursor: pointer;
  -webkit-appearance: none;
  margin-top: -5px;
  pointer-events: auto;
}

.range-input::-moz-range-track {
  width: 100%;
  height: 6px;
  background: #ddd;
  border-radius: 3px;
}

.range-input::-moz-range-thumb {
  height: 16px;
  width: 16px;
  border-radius: 50%;
  background: #007bff;
  cursor: pointer;
}

.range-min {
  z-index: 2;
}

.range-max {
  z-index: 1;
}
</style>