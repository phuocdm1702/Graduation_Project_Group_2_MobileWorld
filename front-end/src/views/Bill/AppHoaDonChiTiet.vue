<template>
  <div class="mt-8">
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
    <div class="mt-4">
      <table class="w-full bg-white rounded-md shadow-md">
        <thead>
          <tr class="bg-gray-200 text-gray-700">
            <th class="px-4 py-2">ID</th>
            <th class="px-4 py-2">ID Hóa đơn</th>
            <th class="px-4 py-2">ID Chi tiết Sản Phẩm</th>
            <th class="px-4 py-2">Mã</th>
            <th class="px-4 py-2">Tổng tiền</th>
            <th class="px-4 py-2">Trạng thái</th>
            <th class="px-4 py-2">Ghi chú</th>
          </tr>
        </thead>
        <tbody>
          <tr class="border-t text-center" v-for="(hdct, index) in dataTableHDCT" :key="index">
            <td class="px-4 py-2">{{ hdct.id }}</td>
            <td class="px-4 py-2">{{ hdct.idHoaDon.id }}</td>
            <td class="px-4 py-2">{{ hdct.idChiTietSanPham.id }}</td>
            <td class="px-4 py-2">{{ hdct.ma }}</td>
            <td class="px-4 py-2">{{ hdct.gia }}</td>
            <td class="px-4 py-2">{{ hdct.trangThai === 1 ? 'Đã thanh toán' : 'Chờ thanh toán'}}</td>
            <td class="px-4 py-2">{{ hdct.ghiChu }}</td>
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
