<template>
  <div class="mt-2 mx-auto">
    <h2 class="bg-white shadow-lg rounded-lg p-5 mb-2 mt-2 text-2xl font-semibold text-gray-700">
      Quản Lý Màn Hình
    </h2>
    <ToastNotification ref="toastRef" />
    <div class="bg-white shadow-lg rounded-lg p-5 mb-2 mt-2 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-4 gap-4">
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Tìm kiếm</label>
        <input v-model.trim="searchKeyword" type="text" placeholder="Tìm kiếm theo mã, kích thước, độ phân giải..." class="input-field" />
      </div>
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Công nghệ màn hình</label>
        <select v-model="searchIdCongNgheManHinh" class="input-field">
          <option value="">Tất cả</option>
          <option v-for="tech in congNgheManHinhs" :key="tech.id" :value="tech.id">{{ tech.congNgheManHinh }}</option>
        </select>
      </div>
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Kích thước</label>
        <select v-model="searchKichThuoc" class="input-field">
          <option value="">Tất cả</option>
          <option v-for="kichThuoc in uniqueKichThuocList" :key="kichThuoc" :value="kichThuoc.replace(/\s+/g, '')">{{ kichThuoc }}</option>
        </select>
      </div>
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Độ phân giải</label>
        <select v-model="searchDoPhanGiai" class="input-field">
          <option value="">Tất cả</option>
          <option v-for="doPhanGiai in uniqueDoPhanGiaiList" :key="doPhanGiai" :value="doPhanGiai">{{ doPhanGiai }}</option>
        </select>
      </div>
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Độ sáng tối đa</label>
        <select v-model="searchDoSangToiDa" class="input-field">
          <option value="">Tất cả</option>
          <option v-for="doSang in uniqueDoSangToiDaList" :key="doSang" :value="doSang.replace(/\s+/g, '')">{{ doSang }}</option>
        </select>
      </div>
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Tần số quét</label>
        <select v-model="searchTanSoQuet" class="input-field">
          <option value="">Tất cả</option>
          <option v-for="tanSo in uniqueTanSoQuetList" :key="tanSo" :value="tanSo">{{ tanSo }}</option>
        </select>
      </div>
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Kiểu màn hình</label>
        <select v-model="searchKieuManHinh" class="input-field">
          <option value="">Tất cả</option>
          <option v-for="kieu in uniqueKieuManHinhList" :key="kieu" :value="kieu">{{ kieu }}</option>
        </select>
      </div>
      <div class="flex justify-end w-full col-span-full gap-2">
        <button @click="resetFilters" class="flex items-center gap-2 px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition">
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

    <div v-if="selectedManHinh.length" class="bg-white shadow-lg rounded-lg p-5 mb-4 mt-4 flex justify-end">
      <button @click="confirmDeleteSelected" class="flex items-center gap-2 px-4 py-2 bg-red-500 text-white font-semibold rounded-lg shadow-md hover:bg-red-600 transition">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5 mr-1">
          <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12"/>
        </svg>
        Xóa {{ selectedManHinh.length }} màn hình đã chọn
      </button>
    </div>

    <DynamicTable class="dynamic-table" :data="manHinhs" :columns="columns" :get-nested-value="getNestedValue" :selected-products="selectedManHinh" @toggle-select="toggleSelect">
      <template #header-select>
        <input type="checkbox" class="w-4 h-4 rounded" :checked="isAllSelected" @change="toggleSelectAll">
      </template>
      <template #cell-select="{ item }">
        <input type="checkbox" class="w-4 h-4 rounded" :checked="selectedManHinh.includes(item.id)" @change="toggleSelect(item.id)">
      </template>
    </DynamicTable>

    <footer class="bg-white shadow-lg rounded-lg p-4 flex justify-center items-center mt-2">
      <Pagination :current-page="currentPage" :total-pages="totalPages" @page-changed="goToPage" />
    </footer>

    <ConfirmModal :show="showConfirmModal" :message="confirmMessage" @confirm="executeConfirmedAction" @cancel="closeConfirmModal" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import useManHinh from './useManHinh.js';
import ToastNotification from '@/components/ToastNotification.vue';
import Pagination from '@/components/Pagination.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';
import DynamicTable from '@/components/DynamicTable.vue';
import axios from 'axios';

const router = useRouter();
const toastRef = ref(null);

const {
  manHinhs,
  congNgheManHinhs,
  manHinh,
  searchKeyword,
  searchKieuManHinh,
  searchIdCongNgheManHinh,
  searchKichThuoc,
  searchDoPhanGiai,
  searchDoSangToiDa,
  searchTanSoQuet,
  currentPage,
  pageSize,
  totalItems,
  selectedManHinh,
  isSearching,
  showConfirmModal,
  confirmMessage,
  totalPages,
  fetchCongNgheManHinhs,
  fetchData,
  goToPage,
  searchManHinh,
  resetSearch,
  deleteManHinh,
  deleteSelectedManHinh,
  confirmDelete,
  confirmDeleteSelected,
  confirmAction,
  executeConfirmedAction,
  closeConfirmModal,
  toggleSelectAll,
} = useManHinh(toastRef);

// Dữ liệu cứng cho các combobox
const kichThuocData = [
  "6.1 inches", "6.1 inches", "6.1 inches", "6.9 inches", "6.2 inches", "6.7 inches", "6.8 inches",
  "6.1 inches", "6.8 inches", "6.7 inches", "6.7 inches", "6.6 inches", "7.6 inches", "6.3 inches",
  "6.74 inches", "6.67 inches", "6.67 inches", "6.67 inches", "6.67 inches", "6.43 inches", "6.56 inches",
  "6.7 inches", "6.7 inches", "6.7 inches", "6.67 inches", "6.59 inches", "7.82 inches", "6.31 inches",
  "6.31 inches", "6.1 inches", "6.3 inches", "7.6 inches", "6.79 inches", "6.67 inches"
];
const doPhanGiaiData = [
  "1170x2532px", "1179x2556px", "1290x2796px", "1320x2868px", "1080x2340px", "1080x2340px",
  "1440x3120px", "1080x2340px", "1440x3088px", "1080x2640px", "720x1600px", "1080x2340px",
  "1856x2160px", "968x2376px", "720x1600px", "1080x2400px", "1220x2712px", "1220x2712px",
  "1220x2712px", "1080x2400px", "720x1612px", "1080x2412px", "1080x2040px", "1080x2412px",
  "1080x2040px", "1256x2760px", "2268x2440px", "1116x2484px", "1284x2778px", "1179x2556px",
  "1206x2622px", "1812x2176px", "1080x2460px", "1080x2400px"
];
const doSangToiDaData = [
  "1200 nits", "2000 nits", "2000 nits", "2000 nits", "2600 nits", "1900 nits", "2600 nits",
  "1750 nits", "1750 nits", "1750 nits", "1750 nits", "1000 nits", "2600 nits", "2600 nits",
  "600 nits", "1000 nits", "1200 nits", "4000 nits", "3000 nits", "800 nits", "480 nits",
  "500 nits", "1200 nits", "1200 nits", "1200 nits", "4500 nits", "2800 nits", "2800 nits",
  "1200 nits", "2000 nits", "2000 nits", "1200 nits", "550 nits", "1300 nits"
];
const tanSoQuetData = [
  "60Hz", "120Hz", "120Hz", "120Hz", "120Hz", "120Hz", "120Hz", "120Hz", "120Hz", "120Hz",
  "60Hz", "120Hz", "120Hz", "120Hz", "90Hz", "120Hz", "120Hz", "144Hz", "120Hz", "90Hz",
  "90Hz", "120Hz", "120Hz", "120Hz", "120Hz", "120Hz", "120Hz", "120Hz", "60Hz", "60Hz",
  "120Hz", "120Hz", "90Hz", "120Hz"
];
const kieuManHinhData = [
  "tai thỏ", "dynamic island", "dynamic island", "dynamic island", "đục lỗ", "đục lỗ", "đục lỗ", "đục lỗ",
  "đục lỗ", "đục lỗ", "đục lỗ", "đục lỗ", "đục lỗ", "đục lỗ", "đục lỗ", "đục lỗ", "đục lỗ", "đục lỗ",
  "đục lỗ", "đục lỗ", "đục lỗ", "đục lỗ", "đục lỗ", "đục lỗ", "đục lỗ", "đục lỗ", "đục lỗ", "tai thỏ",
  "dynamic island", "dynamic island", "đục lỗ", "đục lỗ", "đục lỗ"
];

// Loại bỏ giá trị trùng lặp và sắp xếp
const uniqueKichThuocList = ref([...new Set(kichThuocData)].sort());
const uniqueDoPhanGiaiList = ref([...new Set(doPhanGiaiData)].sort());
const uniqueDoSangToiDaList = ref([...new Set(doSangToiDaData)].sort((a, b) => parseInt(a) - parseInt(b)));
const uniqueTanSoQuetList = ref([...new Set(tanSoQuetData)].sort((a, b) => parseInt(a) - parseInt(b)));
const uniqueKieuManHinhList = ref([...new Set(kieuManHinhData)].sort());

onMounted(() => {
  fetchCongNgheManHinhs();
  fetchData();
  // Không cần fetchKieuManHinhNames nữa vì dùng dữ liệu cứng
});

const resetFilters = () => {
  resetSearch();
};

const navigateToAddPage = () => {
  router.push('/screen/add');
};

const getNestedValue = (obj, path) => {
  return path.split('.').reduce((acc, part) => acc && acc[part], obj);
};

const isAllSelected = computed(() => {
  if (manHinhs.value.length === 0) return false;
  return manHinhs.value.every(item => selectedManHinh.value.includes(item.id));
});

const toggleSelect = (id) => {
  if (selectedManHinh.value.includes(id)) {
    selectedManHinh.value = selectedManHinh.value.filter(selectedId => selectedId !== id);
  } else {
    selectedManHinh.value.push(id);
  }
};

const getCongNgheManHinhName = (id) => {
  const congNghe = congNgheManHinhs.value.find(c => c.id === id);
  return congNghe ? congNghe.congNgheManHinh : 'Không xác định';
};

const columns = [
  { key: 'select', label: '' },
  {
    key: '#',
    label: '#',
    formatter: (value, item, index) => (currentPage.value * pageSize.value) + index + 1,
  },
  {
    key: 'idCongNgheManHinh',
    label: 'Công nghệ màn hình',
    formatter: (value) => getCongNgheManHinhName(value),
  },
  { key: 'ma', label: 'Mã' },
  { key: 'kichThuoc', label: 'Kích thước' },
  { key: 'doPhanGiai', label: 'Độ phân giải' },
  { key: 'doSangToiDa', label: 'Độ sáng tối đa' },
  { key: 'tanSoQuet', label: 'Tần số quét' },
  { key: 'kieuManHinh', label: 'Kiểu màn hình' },
  {
    key: 'trangThai',
    label: 'Trạng Thái',
    formatter: (value, item) => item.deleted ? '<span class="text-red-600">Hết hàng</span>' : '<span class="text-green-600">Còn hàng</span>',
  },
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
            <i class="fa-solid fa-trash"></i>
          </button>
        </div>
      `;
    },
  },
];

const handleCustomEvents = () => {
  document.addEventListener('openEditModal', (event) => {
    router.push(`/screen/edit/${event.detail.id}`);
  });
  document.addEventListener('confirmDelete', (event) => {
    confirmDelete(event.detail);
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