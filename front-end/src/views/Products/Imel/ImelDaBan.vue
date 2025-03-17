<template>
  <div>
    <!-- Thêm BreadcrumbWrapper -->
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />

    <div class="mt-2 mx-auto">
      <ToastNotification ref="toast" />

      <!-- Bảng danh sách Imel đã bán -->
      <DynamicTable
        class="dynamic-table"
        :data="imelDaBans"
        :columns="columns"
        :get-nested-value="getNestedValue"
      />

      <!-- Phân trang -->
      <footer class="bg-white shadow-lg rounded-lg p-4 flex justify-center items-center mt-2">
        <Pagination
          :current-page="currentPage"
          :total-pages="totalPages"
          @page-changed="goToPage"
        />
      </footer>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute } from 'vue-router';
import useImelDaBan from './ImelDaBan.js';
import ToastNotification from '@/components/ToastNotification.vue';
import Pagination from '@/components/Pagination.vue';
import DynamicTable from '@/components/DynamicTable.vue';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue'; // Import BreadcrumbWrapper

const route = useRoute();

// Tính toán breadcrumb dựa trên meta của route
const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === "function") {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || ["Danh Sách Imel Đã Bán"]; // Mặc định nếu không có breadcrumb
});

const {
  toast,
  imelDaBans,
  currentPage,
  pageSize,
  totalItems,
  totalPages,
  fetchData,
  goToPage,
} = useImelDaBan();

const getNestedValue = (obj, path) => {
  return path.split('.').reduce((acc, part) => acc && acc[part], obj);
};

const columns = [
  {
    key: '#',
    label: '#',
    formatter: (value, item, index) => (currentPage.value * pageSize.value) + index + 1,
  },
  { key: 'ma', label: 'Mã' },
  { key: 'imel', label: 'Imel' },
  { key: 'ngayBan', label: 'Ngày bán', formatter: value => new Date(value).toLocaleDateString('vi-VN') },
  { key: 'ghiChu', label: 'Ghi chú' },
];

onMounted(fetchData);
</script>

<style scoped>
.dynamic-table {
  border-top-left-radius: 0px;
  border-top-right-radius: 0px;
}
</style>