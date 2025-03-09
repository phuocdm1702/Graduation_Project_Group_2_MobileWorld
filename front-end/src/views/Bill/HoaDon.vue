<template>
  <div class="mt-8 max-w-screen-xl mx-auto">
    <h4 class="text-xl font-semibold text-gray-700">📋 Danh sách Hóa Đơn</h4>

    <!-- Form lọc -->
    <div class="bg-white shadow-lg rounded-lg p-5 mt-4 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4">
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

      <button @click="applyFilters" class="col-span-full sm:col-span-2 md:col-span-3 lg:col-span-1 bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition">
        🔍 Lọc
      </button>
    </div>

    <!-- Bảng dữ liệu động -->
    <div class="overflow-x-auto mt-6 bg-white shadow-lg rounded-lg p-4">
      <table class="w-full min-w-max table-auto border-collapse">
        <thead>
        <tr class="bg-gray-100 text-gray-700 text-sm font-semibold">
          <th v-for="column in columns" :key="column.key" class="th-cell">
            {{ column.label }}
          </th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(hoaDon, index) in dataTable" :key="hoaDon.id" class="text-gray-700 border-b hover:bg-gray-50">
          <td v-for="column in columns" :key="column.key" class="td-cell">
            <span v-if="column.formatter" v-html="column.formatter(getNestedValue(hoaDon, column.key), hoaDon, index)"></span>
            <span v-else>{{ getNestedValue(hoaDon, column.key) || 'N/A' }}</span>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- Phân trang -->
    <div class="mt-4 flex justify-between items-center">
      <button @click="prevPage" :disabled="currentPage === 1" class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:bg-gray-400">
        Trang trước
      </button>
      <span>Trang {{ currentPage }} / {{ totalPages }}</span>
      <button @click="nextPage" :disabled="currentPage === totalPages" class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:bg-gray-400">
        Trang sau
      </button>
    </div>
  </div>
</template>

<script setup>
import useHoaDonLineList from "@/views/Bill/HoaDon";

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
} = useHoaDonLineList();
</script>

<style scoped>
.input-field {
  @apply w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-sm;
}
.th-cell {
  @apply px-4 py-3 text-left border-b;
}
.td-cell {
  @apply px-4 py-2 text-sm;
}
</style>