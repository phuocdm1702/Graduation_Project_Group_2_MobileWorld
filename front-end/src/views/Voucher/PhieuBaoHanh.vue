<template>
  <div>
    <!-- Breadcrumb -->
    <Breadcrumb breadcrumb="Phiếu Bảo Hành" />

    <div class="mt-8">
      <h4 class="text-gray-600">Danh sách Phiếu Bảo Hành</h4>

      <div class="bg-white p-4 rounded-md shadow-md flex flex-wrap gap-3">
        <!-- Tìm kiếm -->
        <div class="relative w-1/3">
          <input
            type="text"
            v-model="searchQuery"
            placeholder="Tìm theo mã hoặc ID IMEI..."
            class="border px-4 py-2 rounded-md w-full"
          />
        </div>
      </div>

      <div class="mt-4 flex justify-between items-center">
        <button @click="showForm = true" class="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700">
          + Thêm Phiếu Bảo Hành
        </button>
      </div>

      <div class="mt-4">
        <table class="w-full bg-white rounded-md shadow-md">
          <thead>
          <tr class="bg-gray-200 text-gray-700">
            <th class="px-4 py-2">Mã</th>
            <th class="px-4 py-2">ID IMEI Đã Bán</th>
            <th class="px-4 py-2">Ngày Bắt Đầu</th>
            <th class="px-4 py-2">Ngày Kết Thúc</th>
            <th class="px-4 py-2">Ghi Chú</th>
            <th class="px-4 py-2">Hành Động</th>
          </tr>
          </thead>
          <tbody>
          <tr class="border-t text-center" v-for="warranty in warranties" :key="warranty.id">
            <td class="px-4 py-2">{{ warranty.ma }}</td>
            <td class="px-4 py-2">{{ warranty.idImei }}</td>
            <td class="px-4 py-2">{{ new Date(warranty.ngayBatDau).toLocaleDateString('vi-VN') }}</td>
            <td class="px-4 py-2">{{ new Date(warranty.ngayKetThuc).toLocaleDateString('vi-VN') }}</td>
            <td class="px-4 py-2">{{ warranty.ghiChu }}</td>
            <td class="px-4 py-2">
              <button class="text-blue-600 hover:underline mr-2">Sửa</button>
              <button class="text-red-600 hover:underline">Xóa</button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Form thêm phiếu bảo hành -->
    <div v-if="showForm" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex justify-center items-center">
      <div class="bg-white p-6 rounded-md shadow-lg w-1/3">
        <h3 class="text-lg font-bold mb-4">Thêm Phiếu Bảo Hành</h3>
        <label class="block mb-2">Mã</label>
        <input type="text" v-model="newWarranty.ma" class="border px-4 py-2 rounded-md w-full mb-2" />
        <label class="block mb-2">ID IMEI Đã Bán</label>
        <input type="text" v-model="newWarranty.idImei" class="border px-4 py-2 rounded-md w-full mb-2" />
        <label class="block mb-2">Ngày Bắt Đầu</label>
        <input type="date" v-model="newWarranty.ngayBatDau" class="border px-4 py-2 rounded-md w-full mb-2" />
        <label class="block mb-2">Ngày Kết Thúc</label>
        <input type="date" v-model="newWarranty.ngayKetThuc" class="border px-4 py-2 rounded-md w-full mb-2" />
        <label class="block mb-2">Ghi Chú</label>
        <textarea v-model="newWarranty.ghiChu" class="border px-4 py-2 rounded-md w-full mb-2"></textarea>
        <div class="flex justify-end gap-2">
          <button @click="showForm = false" class="bg-gray-400 text-white px-4 py-2 rounded-md">Hủy</button>
          <button @click="addWarranty" class="bg-blue-600 text-white px-4 py-2 rounded-md">Lưu</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";


</script>
