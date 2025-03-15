<template>
  <div class="mt-2 mx-auto">
    <header>
<!--      <h2 class="bg-white shadow-lg rounded-lg p-5 mb-2 mt-2 text-2xl font-semibold text-gray-700">-->
<!--        Quản Lý Hóa Đơn-->
<!--      </h2>-->
    </header>

    <section>
      <!-- Form lọc -->
      <div
        class="bg-white shadow-lg rounded-lg p-5 mb-4 mt-4 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">

        <!-- Ô tìm kiếm -->
        <div>
          <label for="searchQuery" class="block text-sm font-medium text-gray-700 mb-1">Tìm kiếm hóa đơn</label>
          <input v-model="searchQuery" id="searchQuery" type="text" placeholder="Tìm kiếm hóa đơn..."
                 class="input-field"/>
        </div>

        <!-- Dropdown chọn loại đơn -->
        <div>
          <label for="orderType" class="block text-sm font-medium text-gray-700 mb-1">Loại đơn</label>
          <select v-model="selectedOrderType" id="orderType" class="input-field">
            <option value="">Tất cả loại đơn</option>
            <option value="Online">Online</option>
            <option value="Tại cửa hàng">Tại cửa hàng</option>
          </select>
        </div>

        <!-- Thanh trượt chọn khoảng tiền -->
        <div>
          <label for="labels-range-input" class="block text-sm font-medium text-gray-700 mb-1">
            Khoảng giá
          </label>
          <input id="labels-range-input" type="range" v-model="priceRange" min="100" max="1500"
                 class="w-full h-2 bg-gray-200 rounded-lg appearance-none cursor-pointer 
         accent-[#f97316]">
          <div class="flex justify-between text-sm text-gray-500 dark:text-gray-400 mt-1">
            <span>Min ($100)</span>
            <span>Max ($1500)</span>
          </div>
        </div>

        <!-- Chọn ngày bắt đầu -->
        <div>
          <label for="startDate" class="block text-sm font-medium text-gray-700 mb-1">Ngày bắt đầu</label>
          <input v-model="startDate" id="startDate" type="date" class="input-field"/>
        </div>

        <!-- Chọn ngày kết thúc -->
        <div>
          <label for="endDate" class="block text-sm font-medium text-gray-700 mb-1">Ngày kết thúc</label>
          <input v-model="endDate" id="endDate" type="date" class="input-field"/>
        </div>

        <div class="flex justify-end w-full col-span-full gap-2">
          <!-- Button Quét QR -->
          <button
            class="flex items-center gap-2 px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 24 24" fill="none"
                 stroke="currentColor"
                 stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M3 3h4v4H3z"></path>
              <path d="M17 3h4v4h-4z"></path>
              <path d="M3 17h4v4H3z"></path>
              <path d="M17 17h4v4h-4z"></path>
              <path d="M7 7h4v4H7z"></path>
              <path d="M7 17h4"></path>
              <path d="M7 13h8v8"></path>
              <path d="M17 7h-4v4"></path>
            </svg>
            Quét QR
          </button>

          <!-- Button Tạo Hóa Đơn -->
          <button
            class="flex items-center gap-2 px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                 stroke="currentColor" class="w-5 h-5 mr-1">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15"/>
            </svg>
            Tạo Hóa Đơn
          </button>
        </div>
      </div>
    </section>

    <section>
      <!-- Bảng danh sách hóa đơn -->
      <status-bar/>
      <DynamicTable class="dynamic-table"
                    :data="dataTable"
                    :columns="columns"
                    :get-nested-value="getNestedValue"
      />


      <div class="bg-white shadow-lg rounded-lg p-4 flex justify-center items-center mt-2">
        <Pagination
          :current-page="currentPage"
          :total-pages="totalPages"
          @page-changed="goToPage"
        />
      </div>
    </section>

  </div>
</template>

<script setup>
import useHoaDonLineList from "@/views/Bill/HoaDon";
import DynamicTable from "@/components/DynamicTable.vue";
import Pagination from '@/components/Pagination.vue';
import {useRouter} from "vue-router";
import StatusBar from "@/components/statusBar.vue";

const router = useRouter();
const {
  dataTable,
  currentPage,
  goToPage,
  totalPages,
  searchQuery,
  priceRange,
  selectedOrderType,
  startDate,
  endDate,
  columns,
  getNestedValue,
} = useHoaDonLineList();
</script>

<style scoped>
.input-field {
  @apply w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-sm;
}

.dynamic-table {
  border-top-left-radius: 0px;
  border-top-right-radius: 0px;
}
</style>