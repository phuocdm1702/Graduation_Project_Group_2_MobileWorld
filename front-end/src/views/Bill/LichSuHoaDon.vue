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

    <!-- Bảng dữ liệu -->
    <div class="overflow-x-auto mt-6 bg-white shadow-lg rounded-lg p-4">
      <table class="w-full min-w-max table-auto border-collapse">
        <thead>
        <tr class="bg-gray-100 text-gray-700 text-sm font-semibold">
          <th class="th-cell">STT</th>
          <th class="th-cell">Mã</th>
          <th class="th-cell">Hóa đơn</th>
          <th class="th-cell">Nhân viên</th>
          <th class="th-cell">Hành động</th>
          <th class="th-cell">Thời gian ghi lại</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(lshd, index) in dataTableLSHD" :key="lshd.id" class="text-gray-700 border-b hover:bg-gray-50">
          <td class="td-cell">{{ index + 1 }}</td>
          <td class="td-cell">{{ lshd.ma }}</td>
          <td class="td-cell">{{ lshd.idHoaDon.ma }}</td>
          <td class="td-cell">{{ lshd.idNhanVien.ma }}</td>
          <td class="td-cell">{{ lshd.hanhDong }}</td>
          <td class="td-cell">{{ new Date(lshd.thoiGian).toLocaleDateString() }}</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import useLichSuHoaDonLineList from "@/views/Bill/LichSuHoaDon";

const {
  dataTableLSHD
} = useLichSuHoaDonLineList();
</script>

<style>
.th-cell {
  @apply px-4 py-3 text-left border-b;
}

.td-cell {
  @apply px-4 py-2 text-sm;
}
</style>
