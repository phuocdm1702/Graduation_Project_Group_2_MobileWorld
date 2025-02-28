<template>
    <!-- Bảng dữ liệu -->
    <div class="mt-4">
      <table class="w-full bg-white rounded-md shadow-md">
        <thead>
        <tr class="bg-gray-200 text-gray-700">
          <th class="px-4 py-2">STT</th>
          <th class="px-4 py-2">ID Hóa đơn</th>
          <th class="px-4 py-2">ID Nhân viên</th>
          <th class="px-4 py-2">Mã</th>
          <th class="px-4 py-2">Hành động</th>
          <th class="px-4 py-2">Thời gian ghi lại</th>
        </tr>
        </thead>
        <tbody>
        <tr class="border-t text-center" v-for="(lshd, index) in dataTableLSHD" :key="index">
          <td class="px-4 py-2">{{ index + 1 }}</td>
          <td class="px-4 py-2">{{ lshd.ma }}</td>
          <td class="px-4 py-2">{{ lshd.idHoaDon.ma }}</td>
          <td class="px-4 py-2">{{ lshd.idNhanVien.ma }}</td>
          <td class="px-4 py-2">{{ lshd.hanhDong }}</td>
          <td class="px-4 py-2">{{ lshd.thoiGian }}</td>
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
<style>
.table-container {
  display: flex;
  justify-content: center;
  padding: 20px;
}
.table {
  width: 80%;
  border-collapse: collapse;
}
</style>
