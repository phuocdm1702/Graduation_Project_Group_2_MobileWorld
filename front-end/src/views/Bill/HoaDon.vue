<template>
  <div class="mt-8 max-w-screen-xl mx-auto">
    <h4 class="text-xl font-semibold text-gray-700">📋 Danh sách Hóa Đơn</h4>

    <!-- Form lọc -->
    <div
      class="bg-white shadow-lg rounded-lg p-5 mt-4 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4">
      <input v-model="searchQuery" type="text" placeholder="🔍 Tìm kiếm hóa đơn..." class="input-field"/>
      <input v-model="minAmount" type="number" placeholder="💰 Giá tối thiểu" class="input-field"/>
      <input v-model="maxAmount" type="number" placeholder="💰 Giá tối đa" class="input-field"/>

      <select v-model="selectedOrderType" class="input-field">
        <option value="">📦 Tất cả loại đơn</option>
        <option value="Online">Online</option>
        <option value="Tại cửa hàng">Tại cửa hàng</option>
      </select>

      <input v-model="startDate" type="date" class="input-field"/>
      <input v-model="endDate" type="date" class="input-field"/>

      <button @click="applyFilters"
              class="col-span-full sm:col-span-2 md:col-span-3 lg:col-span-1 bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition">
        🔍 Lọc
      </button>
    </div>
    
    <!-- Sử dụng DynamicTable -->
    <DynamicTable
      :data="dataTable"
      :columns="columns"
      :get-nested-value="getNestedValue"
    />

    <!-- Phân trang -->
    <div class="mt-4 flex justify-between items-center gap-2">
      <!-- Nút Trang đầu -->
      <button @click="goToFirstPage" :disabled="currentPage === 1"
              class="px-3 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:bg-gray-400">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
          <path stroke-linecap="round" stroke-linejoin="round" d="M19.5 4.5L10.5 12l9 7.5M4.5 4.5v15"/>
        </svg>
      </button>

      <!-- Nút Trang trước -->
      <button @click="prevPage" :disabled="currentPage === 1"
              class="px-3 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:bg-gray-400">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
          <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 19.5 8.25 12l7.5-7.5"/>
        </svg>
      </button>

      <span>Trang {{ currentPage }} / {{ totalPages }}</span>

      <!-- Nút Trang sau -->
      <button @click="nextPage" :disabled="currentPage === totalPages"
              class="px-3 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:bg-gray-400">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
          <path stroke-linecap="round" stroke-linejoin="round" d="M8.25 4.5l7.5 7.5-7.5 7.5"/>
        </svg>
      </button>

      <!-- Nút Trang cuối -->
      <button @click="goToLastPage" :disabled="currentPage === totalPages"
              class="px-3 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:bg-gray-400">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
          <path stroke-linecap="round" stroke-linejoin="round" d="M4.5 4.5l9 7.5-9 7.5M19.5 4.5v15"/>
        </svg>
      </button>
    </div>

  </div>
</template>

<script setup>
import useHoaDonLineList from "@/views/Bill/HoaDon";
import HoaDonChiTiet from "@/views/Bill/HoaDonChiTiet.vue";
import LichSuHoaDon from "@/views/Bill/LichSuHoaDon.vue";
import DynamicTable from "@/components/DynamicTable.vue";

const {
  dataTable,
  currentPage,
  pageSize,
  totalPages,
  prevPage,
  nextPage,
  searchQuery,
  minAmount,
  maxAmount,
  selectedOrderType,
  startDate,
  endDate,
  applyFilters,
  columns,
  getNestedValue,
  goToFirstPage,
  goToLastPage,
} = useHoaDonLineList();
</script>

<style scoped>
.input-field {
  @apply w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-sm;
}
</style>