<template>
  <head>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
  </head>

  <div>
    <ToastNotification ref="toast" />
    <Breadcrumb breadcrumb="Đợt Giảm Giá"/>
    <h4 class="text-gray-600">Quản lý Đợt Giảm Giá</h4>
    <div class="bg-white p-6 rounded-lg shadow-lg grid grid-cols-4 gap-4">
      <!-- Ô tìm kiếm -->
      <div class="relative col-span-2">
        <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"
             xmlns="http://www.w3.org/2000/svg">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M21 21l-4.35-4.35m0 0A7.5 7.5 0 1011 3.5a7.5 7.5 0 005.65 13.15z"></path>
        </svg>
        <input
          type="text"
          v-model="searchQuery"
          placeholder="Tìm theo mã hoặc tên đợt giảm giá..."
          class="border px-10 py-2 rounded-lg w-full focus:outline-none focus:ring-2 focus:ring-blue-400"
          @input="currentPage = 0; fetchData()"
        />
      </div>

      <!-- Loại Phiếu -->
      <div class="relative">
        <select v-model="filterType"
                class="border px-3 py-2 rounded-lg w-full focus:outline-none focus:ring-2 focus:ring-blue-400"
                @change="currentPage = 0; fetchData()">
          <option value="">Tất cả loại phiếu</option>
          <option value="Phần trăm">Phần trăm</option>
          <option value="Tiền mặt">Tiền mặt</option>
        </select>
      </div>

      <!-- Trạng thái -->
      <div class="relative">
        <select v-model="filterStatus"
                class="border px-3 py-2 rounded-lg w-full focus:outline-none focus:ring-2 focus:ring-blue-400"
                @change="currentPage = 0; fetchData()">
          <option value="">Các trạng thái hoạt động</option>
          <option value="0">Đang diễn ra</option>
          <option value="1">Sắp diễn ra</option>
        </select>
      </div>

      <!-- Đã kết thúc -->
      <div class="flex items-center space-x-2">
        <input type="checkbox" v-model="deleted" @change="currentPage = 0; fetchData()"
               class="w-5 h-5 rounded focus:ring-2 focus:ring-blue-400"/>
        <label class="text-gray-700">Đã kết thúc</label>
      </div>

      <!-- Ngày bắt đầu -->
      <div class="relative">
        <input type="date" v-model="startDate"
               class="border px-3 py-2 rounded-lg w-full focus:outline-none focus:ring-2 focus:ring-blue-400"
               @change="currentPage = 0; fetchData()"/>
      </div>

      <!-- Ngày kết thúc -->
      <div class="relative">
        <input type="date" v-model="endDate"
               class="border px-3 py-2 rounded-lg w-full focus:outline-none focus:ring-2 focus:ring-blue-400"
               @change="currentPage = 0; fetchData()"/>
      </div>

      <!-- Giá trị giảm giá -->
      <div class="relative">
        <input type="number" v-model="saleValue" placeholder="Giá trị giảm"
               class="border px-3 py-2 rounded-lg w-full focus:outline-none focus:ring-2 focus:ring-blue-400"
               @input="currentPage = 0; fetchData()"/>
      </div>

      <div class="relative">
        <input type="number" v-model="minOrder" placeholder="Số tiền giảm tối đa"
               class="border px-3 py-2 rounded-lg w-full focus:outline-none focus:ring-2 focus:ring-blue-400"
               @input="currentPage = 0; fetchData()"/>
      </div>
    </div>


    <div class="mt-4 flex justify-between items-center">
      <RouterLink
        to="/ViewAddDotGiamGia"
        class="bg-blue-600 text-white px-4 py-2 rounded-md flex items-center hover:bg-blue-700 btn-modal"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          class="h-5 w-5 mr-2"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
        >
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/>
        </svg>
        Thêm Đợt Giảm Giá
      </RouterLink>

    </div>

    <div class="mt-8">
      <h4 class="text-gray-600">Danh sách Đợt Giảm Giá</h4>
      <div class="mt-4">
        <DynamicTable
          :data="dataTable"
          :columns="columns"
          :getNestedValue="getNestedValue"
        />
      </div>
    </div>

  </div>
  <div class="pagination flex justify-center items-center space-x-2 py-4">
    <button
      class="bg-blue-600 text-white px-4 py-2 rounded-lg flex items-center justify-center hover:bg-blue-700 transition duration-200 transform hover:scale-105 disabled:opacity-50"
      @click="changePage(page)"
      v-for="page in totalPages"
      :key="page"
      :disabled="currentPage === page - 1">
      <span class="font-semibold">{{ page }}</span> <!-- Bắt đầu từ trăng 1 -->
    </button>
  </div>


</template>

<script setup>
import {useDiscountManagement} from './DotVoucher.js';
import "@vuepic/vue-datepicker/dist/main.css";
// import ToastNotification from '@/components/ToastNotification.vue';
import DynamicTable from "@/components/DynamicTable.vue";

const {
  router,
  toast,
  currentPage,
  pageSize,
  totalPages,
  searchQuery,
  filterType,
  filterStatus,
  startDate,
  endDate,
  minOrder,
  saleValue,
  deleted,
  isModalOpen,
  openModal,
  dataTable,
  dotGiamGia,
  changePage,
  fetchData,
  confirmDelete,
  deleteDotGiamGia,
  viewUpdate,
  columns,
  getNestedValue
} = useDiscountManagement();
</script>


<style src="@/assets/VoucherCss/dotVoucher.css"></style>
