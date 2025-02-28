<template>
  <!-- Bảng dữ liệu Lịch sử bảo hành -->
  <div class="overflow-x-auto mt-6 bg-white shadow-lg rounded-lg p-4">
    <table class="w-full min-w-max table-auto border-collapse">
      <thead>
      <tr class="bg-gray-100 text-gray-700 text-sm font-semibold">
        <th class="th-cell">Lịch sử bảo hành ID</th>
        <th class="th-cell">ID Phiếu bảo hành</th>
        <th class="th-cell">Mã</th>
        <th class="th-cell">Mô tả</th>
        <th class="th-cell">Phương thức sửa chữa</th>
        <th class="th-cell">Chi phí</th>
        <th class="th-cell">Trạng thái</th>
        <th class="th-cell">Ghi chú</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(baoHanh) in dataTable" :key="baoHanh.id" class="text-gray-700 border-b hover:bg-gray-50">
        <td class="td-cell">{{ baoHanh.id }}</td>
        <td class="td-cell">{{ baoHanh.idPhieuBaoHanh }}</td>
        <td class="td-cell">{{ baoHanh.ma }}</td>
        <td class="td-cell">{{ baoHanh.moTa }}</td>
        <td class="td-cell">{{ baoHanh.phuongThucSuaChua }}</td>
        <td class="td-cell">{{ baoHanh.chiPhi.toLocaleString() }} VND</td>
        <td class="td-cell">
          <span :class="baoHanh.trangThai === 1 ? 'text-green-500' : 'text-red-500'">
            {{ baoHanh.trangThai === 1 ? "Hoàn thành" : "Chờ xử lý" }}
          </span>
        </td>
        <td class="td-cell">{{ baoHanh.ghiChu }}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";


</script>

<style scoped>
.th-cell {
  @apply px-4 py-3 text-left border-b;
}

.td-cell {
  @apply px-4 py-2 text-sm;
}
</style>
