<template>
  <div class="mt-8 max-w-screen-xl mx-auto">
    <h4 class="text-gray-600">Lịch sử Hóa Đơn</h4>
    <!-- Bộ lọc -->
    <div class="flex space-x-4 my-4">
      <select v-model="selectedBillId" class="border p-2 rounded-md">
        <option value="">Tất cả hóa đơn</option>
        <option v-for="bill in uniqueBillIds" :key="bill" :value="bill">{{ bill }}</option>
      </select>

      <select v-model="selectedStaffId" class="border p-2 rounded-md">
        <option value="">Tất cả nhân viên</option>
        <option v-for="staff in uniqueStaffIds" :key="staff" :value="staff">{{ staff }}</option>
      </select>
    </div>

  <LichSuHoaDonTable/>
  </div>
</template>

<script setup>
import LichSuHoaDonTable from "@/views/Bill/LichSuHoaDonTable.vue";
</script>

<style>

</style>
