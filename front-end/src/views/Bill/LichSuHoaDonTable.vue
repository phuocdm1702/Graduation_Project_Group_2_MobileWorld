<template>
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
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";

const dataTableLSHD = ref([]);

onMounted(async () => {
  try {
    const res = await axios.get("http://localhost:8080/lich-su-hoa-don/home");
    console.log("Dữ liệu từ API:", res.data);
    dataTableLSHD.value = res.data;
  } catch (error) {
    console.error("Lỗi:", error);
  }
});
</script>

<style scoped>
.th-cell {
  @apply px-4 py-3 text-left border-b;
}

.td-cell {
  @apply px-4 py-2 text-sm;
}
</style>