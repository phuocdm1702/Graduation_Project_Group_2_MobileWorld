<template>
  <div className="mt-2 mx-auto">
    <h2 className="bg-white shadow-lg rounded-lg p-5 mb-2 mt-2 text-2xl font-semibold text-gray-700">
      Danh Sách Imel Đã Bán
    </h2>
    <ToastNotification ref="toast"/> <!-- Thêm dòng này để gắn ref -->

    <!-- Bảng danh sách Imel đã bán -->
    <DynamicTable
      class="dynamic-table"
      :data="imelDaBans"
      :columns="columns"
      :get-nested-value="getNestedValue"
    />

    <!-- Phân trang -->
    <footer className="bg-white shadow-lg rounded-lg p-4 flex justify-center items-center mt-2">
      <Pagination
        :current-page="currentPage"
        :total-pages="totalPages"
        @page-changed="goToPage"
      />
    </footer>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue';
import useImelDaBan from './ImelDaBan.js';
import ToastNotification from '@/components/ToastNotification.vue';
import Pagination from '@/components/Pagination.vue';
import DynamicTable from '@/components/DynamicTable.vue';

const {
  toast, // Lấy toast từ composable
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
  {key: 'ma', label: 'Mã'},
  {key: 'imel', label: 'Imel'},
  {key: 'ngayBan', label: 'Ngày bán', formatter: value => new Date(value).toLocaleDateString('vi-VN')},
  {key: 'ghiChu', label: 'Ghi chú'},
];

onMounted(fetchData);
</script>

<style scoped>
.dynamic-table {
  border-top-left-radius: 0px;
  border-top-right-radius: 0px;
}
</style>