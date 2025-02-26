<template>
  <div>
    <!-- Breadcrumb -->
    <Breadcrumb breadcrumb="Đợt Giảm Giá" />
    <h4 class="text-gray-600">Quản lý Đợt Giảm Giá</h4>
    <div class="bg-white p-4 rounded-md shadow-md flex flex-wrap gap-3">
      
      <div class="relative w-1/4">
        <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-4.35-4.35m0 0A7.5 7.5 0 1011 3.5a7.5 7.5 0 005.65 13.15z"></path>
        </svg>
        <input
          type="text"
          v-model="searchQuery"
          placeholder="Tìm theo mã hoặc tên đợt giảm giá..."
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
      <button @click="openModal" class="bg-blue-600 text-white px-4 py-2 rounded-md flex items-center hover:bg-blue-700 btn-modal">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/>
        </svg>
        Thêm Đợt Giảm Giá
      </button>
    </div>

    <div class="mt-8">
      <h4 class="text-gray-600">Danh sách Đợt Giảm Giá</h4>
      <div class="mt-4">
        <table class="w-full bg-white rounded-md shadow-md">
          <thead>
          <tr class="bg-gray-200 text-gray-700">
            <th class="px-4 py-2">Mã</th>
            <th class="px-4 py-2">Tên đợt giảm giá</th>
            <th class="px-4 py-2">Loại giảm giá</th>
            <th class="px-4 py-2">Giá trị</th>
            <th class="px-4 py-2">Số tiền giảm tối đa</th>
            <th class="px-4 py-2">Ngày bắt đầu</th>
            <th class="px-4 py-2">Ngày kết thúc</th>
            <th class="px-4 py-2">Trạng thái</th>
          </tr>
          </thead>
          <tbody>
          <tr class="border-t text-center" v-for="discount in discounts" :key="discount.id">
            <td class="px-4 py-2">{{ discount.id }}</td>
            <td class="px-4 py-2">{{ discount.name }}</td>
            <td class="px-4 py-2">{{ discount.type }}</td>
            <td class="px-4 py-2">{{ discount.value }}</td>
            <td class="px-4 py-2">{{ discount.maxDiscount }}</td>
            <td class="px-4 py-2">{{ discount.startDate }}</td>
            <td class="px-4 py-2">{{ discount.endDate }}</td>
            <td class="px-4 py-2">{{ discount.status }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div v-if="isModalOpen" class="modal">
      <div class="mt-8">
        <h4 class="text-gray-600">Quản lý Đợt Giảm Giá</h4>

        <div class="mt-6 flex gap-3">
          <div class="w-3/5 overflow-hidden bg-white border rounded-md shadow-md p-4">
            <form>
              <div class="flex items-center justify-between px-4 py-3 text-gray-700 border-b">
                <h3 class="text-md">Thêm Đợt Giảm Giá</h3>
                <button>
                  <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                  </svg>
                </button>
              </div>

              <div class="p-4 text-gray-700 bg-gray-200 border-b grid grid-cols-2 gap-4">
                <div>
                  <label class="text-xs">Mã</label>
                  <input type="text" class="w-full px-3 py-2 mt-2 border rounded-md" />
                </div>
                <div>
                  <label class="text-xs">Tên đợt giảm giá</label>
                  <input type="text" class="w-full px-3 py-2 mt-2 border rounded-md" />
                </div>
                <div>
                  <label class="text-xs">Loại giảm giá</label>
                  <select class="w-full px-3 py-2 mt-2 border rounded-md">
                    <option>Phần trăm</option>
                    <option>Giảm giá cố định</option>
                  </select>
                </div>
                <div>
                  <label class="text-xs">Giá trị</label>
                  <input type="text" class="w-full px-3 py-2 mt-2 border rounded-md" />
                </div>
                <div>
                  <label class="text-xs">Số tiền giảm tối đa</label>
                  <input type="text" class="w-full px-3 py-2 mt-2 border rounded-md" />
                </div>
                <div>
                  <label class="text-xs">Ngày bắt đầu</label>
                  <input type="date" class="w-full px-3 py-2 mt-2 border rounded-md" />
                </div>
                <div>
                  <label class="text-xs">Ngày kết thúc</label>
                  <input type="date" class="w-full px-3 py-2 mt-2 border rounded-md" />
                </div>
                <div class="col-span-2">
                  <label class="text-xs">Trạng thái</label>
                  <select class="w-full px-3 py-2 mt-2 border rounded-md">
                    <option>Đang diễn ra</option>
                    <option>Sắp diễn ra</option>
                    <option>Đã kết thúc</option>
                  </select>
                </div>
              </div>

              <div class="px-4 py-3 flex justify-between">
                <button class="px-3 py-2 text-sm text-gray-700 bg-gray-200 rounded-md hover:bg-gray-300">Hủy</button>
                <button class="px-3 py-2 text-sm text-white bg-indigo-600 rounded-md hover:bg-indigo-500">Lưu</button>
              </div>
            </form>
          </div>

          <div class="w-3/5 bg-white rounded-md shadow-md">
            <h4 class="px-4 py-3 text-gray-700 border-b">Dòng Sản Phẩm</h4>
            <table class="w-full bg-white text-sm">
              <thead>
              <tr class="bg-gray-200 text-gray-700">
                <th class="px-4 py-2">Mã</th>
                <th class="px-4 py-2">Tên Sản Phẩm</th>
                <th class="px-4 py-2">Trạng thái</th>
              </tr>
              </thead>
              <tbody>
              <tr class="border-t text-center" v-for="product in products" :key="product.id">
                <td class="px-4 py-2">{{ product.id }}</td>
                <td class="px-4 py-2">{{ product.name }}</td>
                <td class="px-4 py-2">{{ product.status }}</td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <div class="mt-8">
        <div class="mt-4">
          <table class="w-full bg-white rounded-md shadow-md text-sm">
            <thead>
            <tr class="bg-gray-200 text-gray-700">
              <th class="px-4 py-2">Thương hiệu</th>
              <th class="px-4 py-2">Ảnh</th>
              <th class="px-4 py-2">Hệ điều hành</th>
              <th class="px-4 py-2">Chip</th>
              <th class="px-4 py-2">Camera</th>
              <th class="px-4 py-2">SSD</th>
              <th class="px-4 py-2">Sạc</th>
              <th class="px-4 py-2">Màn hình</th>
              <th class="px-4 py-2">Màu</th>
              <th class="px-4 py-2">Pin</th>
              <th class="px-4 py-2">Trạng thái</th>
            </tr>
            </thead>
            <tbody>
            <tr class="border-t text-center" v-for="product in products" :key="product.id">
              <td class="px-4 py-2">{{ product.brand }}</td>
              <td class="px-4 py-2"><img :src="product.image" alt="Ảnh sản phẩm" class="h-10" /></td>
              <td class="px-4 py-2">{{ product.os }}</td>
              <td class="px-4 py-2">{{ product.chip }}</td>
              <td class="px-4 py-2">{{ product.camera }}</td>
              <td class="px-4 py-2">{{ product.ssd }}</td>
              <td class="px-4 py-2">{{ product.charge }}</td>
              <td class="px-4 py-2">{{ product.screen }}</td>
              <td class="px-4 py-2">{{ product.color }}</td>
              <td class="px-4 py-2">{{ product.battery }}</td>
              <td class="px-4 py-2">{{ product.status }}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    
    <div v-if="isModalOpen" class="overlay"></div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
const isModalOpen = ref(false);

const openModal = () => {
  isModalOpen.value = true;
};



</script>

<style src="@/assets/VoucherCss/dotVoucher.css"></style>
