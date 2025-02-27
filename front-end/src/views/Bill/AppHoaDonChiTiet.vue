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

    <!-- Bảng dữ liệu -->
    <div class="overflow-x-auto mt-6 bg-white shadow-lg rounded-lg p-4">
      <table class="w-full min-w-max table-auto border-collapse">
        <thead>
        <tr class="bg-gray-100 text-gray-700 text-sm font-semibold">
          <th class="th-cell">STT</th>
          <th class="th-cell">ID</th>
          <th class="th-cell">ID Hóa đơn</th>
          <th class="th-cell">ID Chi tiết Sản Phẩm</th>
          <th class="th-cell">Mã</th>
          <th class="th-cell">Tổng tiền</th>
          <th class="th-cell">Trạng thái</th>
          <th class="th-cell">Ghi chú</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(hdct, index) in dataTableHDCT" :key="hdct.id" class="text-gray-700 border-b hover:bg-gray-50">
          <td class="td-cell">{{ index + 1 }}</td>
          <td class="td-cell">{{ hdct.id }}</td>
          <td class="td-cell">{{ hdct.idHoaDon.id }}</td>
          <td class="td-cell">{{ hdct.idChiTietSanPham.id }}</td>
          <td class="td-cell">{{ hdct.ma }}</td>
          <td class="td-cell">{{ hdct.gia.toLocaleString() }} VND</td>
          <td class="td-cell">
              <span :class="hdct.trangThai === 1 ? 'text-green-500' : 'text-red-500'">
                {{ hdct.trangThai === 1 ? 'Đã thanh toán' : 'Chờ thanh toán' }}
              </span>
          </td>
          <td class="td-cell">{{ hdct.ghiChu || 'Không có' }}</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";

const dataTableHDCT = ref([]);

onMounted(async () => {
  try {
    const res = await axios.get("http://localhost:8080/hoa-don-chi-tiet/home");
    console.log("Dữ liệu từ API:", res.data);
    dataTableHDCT.value = res.data;
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
