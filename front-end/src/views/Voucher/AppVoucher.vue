<template>
  <div>
    <!-- Breadcrumb -->
    <Breadcrumb breadcrumb="Phiếu Giảm Giá" />
    
    <div class="mt-8">
      <h4 class="text-gray-600">Danh sách Phiếu Giảm Giá</h4>
      
        <div class="bg-white p-4 rounded-md shadow-md flex flex-wrap gap-3">
          <!-- Tìm kiếm -->
          <div class="relative w-1/4">
            <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-4.35-4.35m0 0A7.5 7.5 0 1011 3.5a7.5 7.5 0 005.65 13.15z"></path>
            </svg>
            <input
              type="text"
              v-model="searchQuery"
              placeholder="Tìm theo mã hoặc tên phiếu..."
              class="border px-10 py-2 rounded-md w-full"
            />
          </div>

          <!-- Loại Phiếu -->
          <div class="relative w-1/6">
            <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 10l7-7m0 0l7 7m-7-7v18"></path>
            </svg>
            <select v-model="filterType" class="border px-10 py-2 rounded-md w-full">
              <option value="">Tất cả loại phiếu</option>
              <option value="Phần trăm">Phần trăm</option>
              <option value="Tiền mặt">Tiền mặt</option>
            </select>
          </div>

          <!-- Trạng thái -->
          <div class="relative w-1/6">
            <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m0 0l-4 4m4-4V3m0 9V3"></path>
            </svg>
            <select v-model="filterStatus" class="border px-10 py-2 rounded-md w-full">
              <option value="">Tất cả trạng thái</option>
              <option value="Hoạt động">Hoạt động</option>
              <option value="Hết hạn">Hết hạn</option>
            </select>
          </div>

          <!-- Ngày bắt đầu -->
          <div class="relative">
            <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10m-10 4h7m-7 4h4"></path>
            </svg>
            <input type="date" v-model="startDate" class="border px-10 py-2 rounded-md" />
          </div>

          <!-- Ngày kết thúc -->
          <div class="relative">
            <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10m-10 4h7m-7 4h4"></path>
            </svg>
            <input type="date" v-model="endDate" class="border px-10 py-2 rounded-md" />
          </div>

          <!-- Hóa đơn tối thiểu -->
          <div class="relative w-1/6">
            <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 10l7-7m0 0l7 7m-7-7v18"></path>
            </svg>
            <input type="number" v-model="minOrder" placeholder="Hóa đơn tối thiểu" class="border px-10 py-2 rounded-md w-full" />
          </div>

          <!-- Giá trị phiếu -->
          <div class="relative w-1/6">
            <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8V4m0 0l-4 4m4-4l4 4m0 0V20"></path>
            </svg>
            <input type="number" v-model="valueFilter" placeholder="Giá trị phiếu" class="border px-10 py-2 rounded-md w-full" />
          </div>
        </div>


      <div class="mt-4 flex justify-between items-center">
        <router-link to="/add-phieu-giam-gia" class="bg-blue-600 text-white px-4 py-2 rounded-md flex items-center hover:bg-blue-700 btn-modal">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/>
          </svg>
          Thêm Phiếu
        </router-link>
      </div>
      
      <div class="mt-4">
        <table class="w-full bg-white rounded-md shadow-md">
          <thead>
          <tr class="bg-gray-200 text-gray-700">
            <th class="px-4 py-2">Mã</th>
            <th class="px-4 py-2">Tên Phiếu</th>
            <th class="px-4 py-2">Loại Phiếu</th>
            <th class="px-4 py-2">Phần trăm giảm giá</th>
            <th class="px-4 py-2">Số tiền giảm tối đa</th>
            <th class="px-4 py-2">Số lượng</th>
            <th class="px-4 py-2">Hóa đơn tối thiểu</th>
            <th class="px-4 py-2">Ngày bắt đầu</th>
            <th class="px-4 py-2">Ngày kết thúc</th>
            <th class="px-4 py-2">Trạng thái</th>
            <th class="px-4 py-2">Riêng tư</th>
            <th class="px-4 py-2">Mô tả</th>
            <th class="px-4 py-2">Hành động</th>
          </tr>
          </thead>
          <tbody>
          <tr class="border-t text-center" v-for="voucher in vouchers" :key="voucher.id">
            <td class="px-4 py-2">{{ voucher.ma }}</td>
            <td class="px-4 py-2">{{ voucher.tenPhieuGiamGia }}</td>
            <td class="px-4 py-2">{{ voucher.loaiPhieuGiamGia }}</td>
            <td class="px-4 py-2">{{ voucher.phanTramGiamGia }}%</td>
            <td class="px-4 py-2">{{ voucher.soTienGiamToiDa }}</td>
            <td class="px-4 py-2">{{ voucher.soLuongDung }}</td>
            <td class="px-4 py-2">{{ voucher.hoaDonToiThieu }}</td>
            <td class="px-4 py-2">{{ new Date(voucher.ngayBatDau).toLocaleDateString('vi-VN') }}</td>
            <td class="px-4 py-2">{{ new Date(voucher.ngayKetThuc).toLocaleDateString('vi-VN') }}</td>
            <td class="px-4 py-2">{{ voucher.trangThai == "1" ? "Còn hạn" : "Hết hạn" }}</td>
            <td class="px-4 py-2">{{ voucher.riengTu == "1" ? "Có" : "Không" }}</td>
            <td class="px-4 py-2">{{ voucher.moTa }}</td>
            <td class="px-4 py-2">
              <button class="text-blue-600 hover:underline mr-2">Sửa</button>
              <button class="text-red-600 hover:underline">Xóa</button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    
    
    
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";

import PhieuGGService from "@/views/Voucher/PhieuGiamGiaService/PhieuGGService.js"
const vouchers = ref([]);

// Load data
const fetchDataPGG = async () => {
  try {
    const response = await PhieuGGService.getData();
    vouchers.value = response.data;
  } catch (error) {
    console.log("Error data");
  }
};
onMounted(fetchDataPGG);

</script>

<style src="@/assets/VoucherCss/voucher.css"></style>



