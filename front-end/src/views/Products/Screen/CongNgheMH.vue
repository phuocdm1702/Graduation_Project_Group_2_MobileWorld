<template>
  <div>
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />
    <div class="mt-2 mx-auto">
      <ToastNotification ref="toast" />
      <div class="bg-white shadow-lg rounded-lg p-5 mb-2 mt-2 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-4 gap-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Tìm kiếm</label>
          <input v-model.trim="searchKeyword" type="text" placeholder="Tìm kiếm theo mã, tên hoặc chuẩn màn hình..." class="input-field" />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Tên Công Nghệ</label>
          <select v-model="searchCongNgheManHinh" class="input-field">
            <option value="">Tất cả</option>
            <option v-for="tech in uniqueCongNgheManHinhList" :key="tech" :value="tech">{{ tech }}</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Chuẩn Màn Hình</label>
          <select v-model="searchChuanManHinh" class="input-field">
            <option value="">Tất cả</option>
            <option v-for="chuan in uniqueChuanManHinhList" :key="chuan" :value="chuan">{{ chuan }}</option>
          </select>
        </div>
        <div class="flex justify-end w-full col-span-full gap-2">
          <button @click="resetSearch" class="flex items-center gap-2 px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
              <path stroke-linecap="round" stroke-linejoin="round" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
            </svg>
            Đặt lại
          </button>
          <button @click="navigateToAddPage" class="flex items-center gap-2 px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5 mr-1">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15"/>
            </svg>
            Thêm mới
          </button>
        </div>
      </div>
      <DynamicTable class="dynamic-table" :data="congNgheManHinhs" :columns="columns" :get-nested-value="getNestedValue"></DynamicTable>
      <footer class="bg-white shadow-lg rounded-lg p-4 flex justify-center items-center mt-2">
        <Pagination :current-page="currentPage + 1" :total-pages="totalPages" @page-changed="goToPage" />
      </footer>
      <ConfirmModal :show="showConfirmModal" :message="confirmMessage" @confirm="executeConfirmedAction" @cancel="closeConfirmModal" />
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import ToastNotification from '@/components/ToastNotification.vue';
import Pagination from '@/components/Pagination.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';
import DynamicTable from '@/components/DynamicTable.vue';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue';
import CongNgheManHinh from '@/views/Products/Screen/CongNgheManHinh'; // Sửa đường dẫn

const router = useRouter();
const route = useRoute();
const toast = ref(null);
const {
  congNgheManHinhs,
  congNgheManHinh,
  searchKeyword,
  searchCongNgheManHinh,
  searchChuanManHinh,
  uniqueCongNgheManHinhList,
  uniqueChuanManHinhList,
  currentPage,
  pageSize,
  totalItems,
  isSearching,
  showConfirmModal,
  confirmMessage,
  totalPages,
  fetchData,
  goToPage,
  searchCongNgheManHinh: searchCongNgheManHinhFunc,
  resetSearch,
  deleteCongNgheManHinh,
  confirmAction,
  executeConfirmedAction,
  closeConfirmModal,
} = CongNgheManHinh(toast);

const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === "function") {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || ["Quản Lý Công Nghệ Màn Hình"];
});

const navigateToAddPage = () => {
  router.push('/screens/technology/add');
};

const getNestedValue = (obj, path) => {
  return path.split('.').reduce((acc, part) => acc && acc[part], obj);
};

const columns = [
  { key: '#', label: '#', formatter: (value, item, index) => (currentPage.value * pageSize.value) + index + 1 },
  { key: 'ma', label: 'Mã' },
  { key: 'congNgheManHinh', label: 'Tên' },
  { key: 'chuanManHinh', label: 'Chuẩn màn hình' },
  {
    key: 'actions',
    label: 'Hành động',
    formatter: (value, item) => {
      const safeItem = JSON.stringify(item);
      return `
        <div class="space-x-4">
          <button class="text-orange-600 hover:text-orange-800 transition" data-item='${safeItem}' onclick="document.dispatchEvent(new CustomEvent('openEditModal', { detail: JSON.parse(this.dataset.item) }))">
            <i class="fa-solid fa-edit text-orange-500"></i>
          </button>
          <button class="text-red-600 hover:text-red-800 transition" data-id="${item.id}" onclick="document.dispatchEvent(new CustomEvent('confirmDelete', { detail: this.dataset.id }))">
            <i class="fa-solid fa-trash text-red-500"></i>
          </button>
        </div>
      `;
    },
  },
];

const handleCustomEvents = () => {
  document.addEventListener('openEditModal', (event) => {
    console.log('Open Edit Modal with item:', event.detail);
    if (event.detail && event.detail.id) {
      router.push(`/screens/technology/edit/${event.detail.id}`);
    } else {
      console.error('Invalid item data:', event.detail);
    }
  });
  document.addEventListener('confirmDelete', (event) => {
    console.log('Confirm Delete with id:', event.detail);
    confirmAction('Bạn có chắc chắn muốn xóa công nghệ màn hình này?', () => deleteCongNgheManHinh(event.detail));
  });
};

handleCustomEvents();
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