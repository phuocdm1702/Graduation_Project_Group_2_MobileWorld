<template>
  <div class="p-6">
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems"/>

    <!-- Thống kê -->
    <div v-if="statistics.length" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
      <div
        v-for="(stat, index) in statistics"
        :key="index"
        class="p-6 rounded-2xl shadow-lg text-white flex flex-col items-center text-center"
        :class="stat.bgColor"
      >
        <h2 class="text-2xl font-semibold mb-2">{{ stat.title }}</h2>
        <p class="text-lg">Doanh thu: <strong>{{ formatCurrency(stat.revenue) }}</strong></p>
        <p class="text-lg">Sản phẩm đã bán: <strong>{{ stat.sold }}</strong></p>
        <p class="text-lg">Tổng đơn hàng: <strong>{{ stat.orders }}</strong></p>
      </div>
    </div>
    <div v-else class="text-center text-gray-500">Loading statistics...</div>

    <!-- Bộ lọc -->
    <div class="bg-white p-4 rounded-xl shadow-md mb-6">
      <div class="flex flex-wrap items-center gap-4">
        <label class="font-medium">Lọc theo:</label>
        <select v-model="filterType" @change="fetchData" class="p-2 border rounded-md">
          <option value="day">Ngày</option>
          <option value="month">Tháng</option>
          <option value="year">Năm</option>
        </select>

        <label class="text-gray-600">Từ:</label>
        <input type="date" v-model="startDate" class="p-2 border rounded-md"/>
        <label class="text-gray-600">Đến:</label>
        <input type="date" v-model="endDate" class="p-2 border rounded-md"/>

        <button
          @click="fetchData"
          class="p-2 bg-blue-500 hover:bg-blue-600 transition text-white font-medium rounded-lg shadow-md"
        >
          Lọc
        </button>
      </div>
    </div>

    <!-- Bảng sản phẩm bán chạy -->
    <DynamicTable
      :data="topProducts"
      :columns="columns"
      :get-nested-value="getNestedValue"
    />

    <!-- Phân trang -->
    <footer v-if="totalPages > 1" class="bg-white shadow-lg rounded-lg p-4 flex justify-center items-center mt-4">
      <Pagination
        :current-page="currentPage"
        :total-pages="totalPages"
        @page-changed="changePage"
      />
    </footer>
    <div v-else class="text-center mt-4 text-gray-500">Không có phân trang (tổng số trang: {{ totalPages }})</div>
  </div>
</template>


<script setup>
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue';
import DynamicTable from '@/components/DynamicTable.vue';
import Pagination from '@/components/Pagination.vue';
import {ThongKeJs} from './js/ThongKe.js';
import {computed} from 'vue';
import {useRoute} from 'vue-router';

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
  topProducts,
  columns,
  filterType,
  startDate,
  endDate,
  changePage,
  fetchData,
  currentPage,
  totalPages
} = ThongKeJs();

const getNestedValue = (item, key) => {
  return item[key];
};
</script>