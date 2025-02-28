<template>
  <div class="mt-8 max-w-screen-xl mx-auto">
    <h4 class="text-gray-600">Hóa Đơn Chi Tiết</h4>

    <!-- Bộ lọc -->
    <div class="flex space-x-4 my-4">
      <select v-model="selectedStatus" class="border p-2 rounded-md">
        <option value="">Tất cả trạng thái</option>
        <option value="Hoàn thành">Hoàn thành</option>
        <option value="Đang xử lý">Đang xử lý</option>
        <option value="Đã thanh toán">Đã thanh toán</option>
        <option value="Chờ xác nhận">Chờ xác nhận</option>
      </select>

      <select v-model="selectedBillId" class="border p-2 rounded-md">
        <option value="">Tất cả hóa đơn</option>
        <option v-for="bill in uniqueBillIds" :key="bill" :value="bill">{{ bill }}</option>
      </select>
    </div>
 <HoaDonChiTietTable/>
  </div>
</template>

<script setup>
import HoaDonChiTietTable from "@/views/Bill/HoaDonChiTietTable.vue";
</script>

