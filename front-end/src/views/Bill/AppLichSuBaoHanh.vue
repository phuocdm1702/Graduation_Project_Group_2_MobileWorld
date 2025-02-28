<template>
  <div class="mt-8 max-w-screen-xl mx-auto">
    <h4 class="text-xl font-semibold text-gray-700">🛠️ Lịch Sử Bảo Hành</h4>

    <!-- Form lọc -->
    <div
      class="bg-white shadow-lg rounded-lg p-5 mt-4 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4">
      <input v-model="searchQuery" type="text" placeholder="🔍 Tìm kiếm..." class="input-field"/>
      <input v-model="minCost" type="number" placeholder="💰 Chi phí tối thiểu" class="input-field"/>
      <input v-model="maxCost" type="number" placeholder="💰 Chi phí tối đa" class="input-field"/>

      <select v-model="repairMethod" class="input-field">
        <option value="">🔧 Tất cả phương thức sửa chữa</option>
        <option value="Sửa chữa">Sửa chữa</option>
        <option value="Thay thế linh kiện">Thay thế linh kiện</option>
      </select>

      <input v-model="startDate" type="date" class="input-field"/>
      <input v-model="endDate" type="date" class="input-field"/>

      <button @click="applyFilters"
              class="col-span-full sm:col-span-2 md:col-span-3 lg:col-span-1 bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition">
        🔍 Lọc
      </button>
    </div>

    <LichSuBaoHanhTable/>
  </div>
</template>

<script setup>
import LichSuBaoHanhTable from "@/views/Warranty/LichSuBaoHanhTable.vue";
</script>

<style scoped>
.input-field {
  @apply w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-sm;
}
</style>
