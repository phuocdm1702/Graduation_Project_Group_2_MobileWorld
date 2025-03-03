<template>
  <div class="mt-8">
    <h4 class="text-gray-600">Danh sách Phiếu Bảo Hành</h4>
    <div class="mt-4">
      <table class="w-full bg-white rounded-md shadow-md">
        <thead>
        <tr class="bg-gray-200 text-gray-700">
          <th class="px-4 py-2">STT</th>
          <th class="px-4 py-2">Mã</th>
          <th class="px-4 py-2">Imel</th>
          <th class="px-4 py-2">Ngày Bắt Đầu</th>
          <th class="px-4 py-2">Ngày Kết Thúc</th>
          <th class="px-4 py-2">Ghi Chú</th>
          <th class="px-4 py-2">Hành động</th>
        </tr>
        </thead>
        <tbody>
        <tr class="border-t text-center" v-for="(pbh, index) in dataTablePBH" :key="index">
          <td class="px-4 py-2">{{ index + 1 }}</td>
          <td class="px-4 py-2">{{ pbh.ma }}</td>
          <td class="px-4 py-2">{{ pbh.idImelDaBan.imel }}</td>
          <td class="px-4 py-2">{{ pbh.ngayBatDau }}</td>
          <td class="px-4 py-2">{{ pbh.ngayKetThuc }}</td>
          <td class="px-4 py-2">{{ pbh.ghiChu }}</td>
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
import usePhieuBaoHanhLineList from "@/views/BaoHanh/PhieuBaoHanh";
const {
  dataTablePBH
} = usePhieuBaoHanhLineList();
</script>