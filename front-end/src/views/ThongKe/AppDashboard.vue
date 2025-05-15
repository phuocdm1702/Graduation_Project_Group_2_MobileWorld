<template>
  <div class="container mx-auto p-6">
    <!-- Breadcrumb -->
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" class="mb-6" />
    
    
    <!-- Thống kê -->
    <section class="mb-8">
      <div class="flex items-center justify-between mb-4">
        <h2 class="text-2xl font-semibold">Thống Kê Tổng Quan</h2>
        <button
          @click="exportExcel"
          class="px-4 py-2 bg-green-600 text-white font-semibold rounded-lg shadow-md hover:bg-blue-600 transition flex items-center gap-2">
          <i class="fa fa-file-excel text-white text-lg"></i>
          Xuất Excel
        </button>
      </div>

      <div v-if="statistics.length" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <div
          v-for="(stat, index) in statistics"
          :key="index"
          class="p-6 rounded-xl text-white shadow-md transition-transform transform hover:scale-105"
          :class="stat.bgColor"
        >
          <h3 class="text-xl font-semibold mb-2">{{ stat.title }}</h3>
          <p class="text-sm">Doanh thu đơn hàng: {{ formatCurrency(stat.revenue) }}</p>
          <p class="text-sm">Sản phẩm đã bán: {{ stat.sold }}</p>
          <p class="text-sm">Tổng số đơn hàng: {{ stat.orders }}</p>
        </div>
      </div>
      <div v-else class="text-center text-gray-500">Đang tải thống kê...</div>
    </section>


    <!-- Thống kê mới: Tỷ lệ doanh thu theo hãng, Phân phối đa kênh, Sản phẩm sắp hết hàng -->
    <section class="mb-8">
      <h2 class="text-2xl font-semibold mb-4">Thống Kê Bán Hàng</h2>
      <div class="flex flex-col lg:flex-row gap-6">
        <!-- Tỷ lệ doanh thu theo hãng -->
        <div class="flex-1 flex flex-col">
          <h2 class="text-xl font-semibold mb-4 text-center">Tỷ Lệ Doanh Thu Theo Hãng</h2>
          <div class="bg-white shadow-lg rounded-lg p-6 w-full flex-1 min-h-[300px] flex flex-col justify-between">
            <div class="flex-1 flex justify-center items-center">
              <canvas id="hangBanChayChart" class="max-w-full max-h-full"></canvas>
            </div>
          </div>
        </div>

        <!-- Phân phối đa kênh -->
        <div class="flex-1 flex flex-col">
          <h2 class="text-xl font-semibold mb-4 text-center">Phân Phối Đa Kênh</h2>
          <div class="bg-white shadow-lg rounded-lg p-6 w-full flex-1 min-h-[300px] flex flex-col justify-between">
            <div class="flex-1 flex justify-center items-center">
              <canvas id="loaiHoaDonChart" class="max-w-full max-h-full"></canvas>
            </div>
          </div>
        </div>

        <!-- Sản phẩm sắp hết hàng -->
        <div class="flex-1 flex flex-col">
          <h2 class="text-xl font-semibold mb-4 text-center">Sản Phẩm Sắp Hết Hàng</h2>
          <div class="bg-white shadow-lg rounded-lg p-6 w-full flex-1 min-h-[300px] flex flex-col justify-between">
            <DynamicTable
              :data="sanPhamHetHang"
              :columns="columnsSanPhamHetHang"
              :get-nested-value="getNestedValue"
              class="bg-white rounded-lg min-w-[400px] max-w-[400px] h-[380px]"/>
            <footer
              v-if="sanPhamHetHangTotalPages > 1"
              class="bg-white shadow-lg rounded-lg p-4 flex justify-center items-center mt-2"
            >
              <Pagination
                :current-page="sanPhamHetHangCurrentPage"
                :total-pages="sanPhamHetHangTotalPages"
                @page-changed="changeSanPhamHetHangPage"
              />
            </footer>
          </div>
        </div>
      </div>
    </section>

    <!-- Bảng sản phẩm bán chạy -->
    <section class="mb-8">
      <h2 class="text-2xl font-semibold mb-4">Sản Phẩm Bán Chạy</h2>
      <section class="mb-2">
        <div class="flex flex-col sm:flex-row items-center gap-4 bg-gray-50 p-4 rounded-lg shadow-sm">
          <div class="flex items-center gap-2">
            <label class="font-medium text-gray-700">Lọc theo:</label>
            <select
              v-model="filterType"
              @change="fetchData"
              class="p-2 border rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none"
            >
              <option value="day">Ngày</option>
              <option value="month">Tháng</option>
              <option value="year">Năm</option>
              <option value="custom">Tùy chỉnh</option>
            </select>
          </div>
          <div v-if="filterType === 'custom'" class="flex items-center gap-2">
            <label class="font-medium text-gray-700">Từ ngày:</label>
            <input
              type="date"
              v-model="startDate"
              class="p-2 border rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none"
            />
            <label class="font-medium text-gray-700">Đến ngày:</label>
            <input
              type="date"
              v-model="endDate"
              class="p-2 border rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none"
            />
            <button
              @click="fetchData"
              class="p-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors"
            >
              Lọc
            </button>
          </div>
        </div>
      </section>
      <DynamicTable
        :data="topProducts"
        :columns="columnsTopProducts"
        :get-nested-value="getNestedValue"
        class="bg-white shadow-lg rounded-lg"
      />
      <footer
        v-if="totalPages > 1"
        class="bg-white shadow-lg rounded-lg p-4 flex justify-center items-center mt-4"
      >
        <Pagination
          :current-page="currentPage"
          :total-pages="totalPages"
          @page-changed="changePage"
        />
      </footer>
    </section>

    <!-- Bảng tăng trưởng và Biểu đồ trạng thái đơn hàng -->
    <section class="mb-8">
      <div class="flex flex-col lg:flex-row gap-6">
        <!-- Bảng tăng trưởng -->
        <div class="flex-1 flex flex-col">
          <h2 class="text-2xl font-semibold mb-4 text-center">Tốc Độ Tăng Trưởng Cửa Hàng</h2>
          <div class="bg-white shadow-lg rounded-lg p-6 w-full flex-1 min-h-[400px] flex flex-col justify-between">
            <div class="grid grid-cols-3 gap-4 py-3 border-b items-center text-center hover:bg-gray-50 transition-colors">
              <span class="font-semibold text-gray-700">Doanh thu ngày:</span>
              <span class="text-gray-900">{{ formatCurrency(growthData.doanhThuNgay) }}</span>
              <span v-html="formatGrowth(growthData.growthDoanhThuNgay)" class="text-sm"></span>
            </div>
            <div class="grid grid-cols-3 gap-4 py-3 border-b items-center text-center hover:bg-gray-50 transition-colors">
              <span class="font-semibold text-gray-700">Doanh thu tháng:</span>
              <span class="text-gray-900">{{ formatCurrency(growthData.doanhThuThang) }}</span>
              <span v-html="formatGrowth(growthData.growthDoanhThuThang)" class="text-sm"></span>
            </div>
            <div class="grid grid-cols-3 gap-4 py-3 border-b items-center text-center hover:bg-gray-50 transition-colors">
              <span class="font-semibold text-gray-700">Doanh thu năm:</span>
              <span class="text-gray-900">{{ formatCurrency(growthData.doanhThuNam) }}</span>
              <span v-html="formatGrowth(growthData.growthDoanhThuNam)" class="text-sm"></span>
            </div>
            <div class="grid grid-cols-3 gap-4 py-3 border-b items-center text-center hover:bg-gray-50 transition-colors">
              <span class="font-semibold text-gray-700">Sản phẩm tháng:</span>
              <span class="text-gray-900">{{ growthData.sanPhamDaBanThang }} sản phẩm</span>
              <span v-html="formatGrowth(growthData.growthSanPhamDaBanThang)" class="text-sm"></span>
            </div>
            <div class="grid grid-cols-3 gap-4 py-3 border-b items-center text-center hover:bg-gray-50 transition-colors">
              <span class="font-semibold text-gray-700">Hóa đơn ngày:</span>
              <span class="text-gray-900">{{ growthData.hoaDonTheoNgay }} hóa đơn</span>
              <span v-html="formatGrowth(growthData.growthHoaDonTheoNgay)" class="text-sm"></span>
            </div>
            <div class="grid grid-cols-3 gap-4 py-3 items-center text-center hover:bg-gray-50 transition-colors">
              <span class="font-semibold text-gray-700">Hóa đơn năm:</span>
              <span class="text-gray-900">{{ growthData.hoaDonTheoNam }} hóa đơn</span>
              <span v-html="formatGrowth(growthData.growthHoaDonTheoNam)" class="text-sm"></span>
            </div>
          </div>
        </div>

        <!-- Biểu đồ trạng thái đơn hàng -->
        <div class="flex-1 flex flex-col">
          <h2 class="text-2xl font-semibold mb-4 text-center">Biểu Đồ Trạng Thái Đơn Hàng</h2>
          <div class="bg-white shadow-lg rounded-lg p-6 w-full flex-1 min-h-[400px] flex flex-col justify-between">
            <div class="flex justify-center mb-4">
              <button
                @click="chartFilterType = 'day'; fetchOrderStatusStats()"
                :class="chartFilterType === 'day' ? 'bg-blue-500 text-white' : 'bg-gray-200 text-gray-700'"
                class="px-3 py-1 rounded-l-lg border-r border-gray-300 hover:bg-blue-600 hover:text-white transition-colors text-sm"
              >
                Ngày
              </button>
              <button
                @click="chartFilterType = 'month'; fetchOrderStatusStats()"
                :class="chartFilterType === 'month' ? 'bg-blue-500 text-white' : 'bg-gray-200 text-gray-700'"
                class="px-3 py-1 border-r border-gray-300 hover:bg-blue-600 hover:text-white transition-colors text-sm"
              >
                Tháng
              </button>
              <button
                @click="chartFilterType = 'year'; fetchOrderStatusStats()"
                :class="chartFilterType === 'year' ? 'bg-blue-500 text-white' : 'bg-gray-200 text-gray-700'"
                class="px-3 py-1 rounded-r-lg hover:bg-blue-600 hover:text-white transition-colors text-sm"
              >
                Năm
              </button>
            </div>
            <div class="flex-1 flex justify-center items-center">
              <canvas id="orderStatusChart" class="max-w-full max-h-full"></canvas>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue';
import DynamicTable from '@/components/DynamicTable.vue';
import Pagination from '@/components/Pagination.vue';
import { ThongKeJs } from './js/ThongKe.js';
import { computed } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();

const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === "function") {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || ["Thống Kê"];
});

const {
  statistics,
  formatCurrency,
  formatGrowth,
  topProducts,
  columnsTopProducts,
  growthData,
  orderStatusStats,
  filterType,
  startDate,
  endDate,
  chartFilterType,
  fetchData,
  fetchOrderStatusStats,
  changePage,
  currentPage,
  totalPages,
  hangBanChay,
  loaiHoaDon,
  sanPhamHetHang,
  columnsSanPhamHetHang,
  changeSanPhamHetHangPage,
  sanPhamHetHangCurrentPage,
  sanPhamHetHangTotalPages,
  exportExcel
} = ThongKeJs();

const getNestedValue = (item, key) => {
  return item[key];
};
</script>