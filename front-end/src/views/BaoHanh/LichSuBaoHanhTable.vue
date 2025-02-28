<template>
  <div class="mt-8">
    <h4 class="text-gray-600">Danh sách Phiếu Bảo Hành</h4>
    <div class="mt-4">
      <table class="w-full bg-white rounded-md shadow-md">
        <thead>
        <tr class="bg-gray-200 text-gray-700">
          <th class="px-4 py-2">Mã</th>
          <th class="px-4 py-2">Phiếu Bảo Hành</th>
          <th class="px-4 py-2">Mô tả lỗi</th>
          <th class="px-4 py-2">Phương Thức Sửa Chữa</th>
          <th class="px-4 py-2">Chi Phí</th>
          <th class="px-4 py-2">Trạng Thái</th>
          <th class="px-4 py-2">Ghi Chú</th>
          <th class="px-4 py-2">Hành động</th>
        </tr>
        </thead>
        <tbody>
        <tr class="border-t text-center" v-for="(lsbh, index) in dataTablelsbh" :key="index">
          <td class="px-4 py-2">{{ lsbh.ma }}</td>
          <td class="px-4 py-2">{{ lsbh.idPhieuBaoHanh.id }}</td>
          <td class="px-4 py-2">{{ lsbh.moTaLoi }}</td>
          <td class="px-4 py-2">{{ lsbh.phuongThucSuaChua }}</td>
          <td class="px-4 py-2">{{ lsbh.chiPhi.toLocaleString() }} VND</td>
          <td class="px-4 py-2"
              :class="{ 'text-green-600': lsbh.trangThai === 1, 'text-red-600': lsbh.trangThai !== 1 }">
            {{ lsbh.trangThai === 1 ? "Hoàn thành" : "Đang sửa" }}
          </td>
          <td class="px-4 py-2">{{ lsbh.ghiChu }}</td>
          <td class="px-4 py-2">
            <button class="text-blue-600 hover:underline mr-2">Sửa</button>
            <button class="text-red-600 hover:underline">Xóa</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";

const dataTablelsbh = ref([]);
onMounted(async () => {
  try {
    const res = await axios.get("http://localhost:8080/lich-su-bao-hanh/home");
    console.log("Dữ liệu từ API:", res.data);
    dataTablelsbh.value = res.data;
  } catch (error) {
    console.error("Lỗi:", error);
  }
})
</script>