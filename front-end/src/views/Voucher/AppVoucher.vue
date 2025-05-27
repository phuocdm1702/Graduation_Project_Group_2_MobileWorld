<template>
  <div>
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems"/>
    <div className="mt-2 mx-auto">
      <div
        className="bg-white shadow-lg rounded-lg p-5 mb-2 mt-2 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-3 gap-4">
        <div className="w-full">
          <label htmlFor="searchQuery" className="block text-sm font-medium text-gray-700 mb-1">Tìm kiếm</label>
          <input v-model="searchQuery" id="searchQuery" type="text" placeholder="Tìm theo mã hoặc tên phiếu..."
                 className="input-field"/>
        </div>
        <div className="w-full">
          <label htmlFor="filterType" className="block text-sm font-medium text-gray-700 mb-1">Loại phiếu</label>
          <select v-model="filterType" id="filterType" className="input-field">
            <option value="">Tất cả loại phiếu</option>
            <option value="Phần trăm">Phần trăm</option>
            <option value="Tiền mặt">Tiền mặt</option>
          </select>
        </div>
        <div className="w-full">
          <label htmlFor="filterStatus" className="block text-sm font-medium text-gray-700 mb-1">Trạng thái</label>
          <select v-model="filterStatus" id="filterStatus" className="input-field">
            <option value="">Tất cả trạng thái</option>
            <option value="Chưa diễn ra">Chưa diễn ra</option>
            <option value="Đang diễn ra">Đang diễn ra</option>
            <option value="Không hoạt động">Không hoạt động</option>
          </select>
        </div>
        <div className="w-full">
          <label htmlFor="startDate" className="block text-sm font-medium text-gray-700 mb-1">Ngày bắt đầu</label>
          <input v-model="startDate" id="startDate" type="date" className="input-field"/>
        </div>
        <div className="w-full">
          <label htmlFor="endDate" className="block text-sm font-medium text-gray-700 mb-1">Ngày kết thúc</label>
          <input v-model="endDate" id="endDate" type="date" className="input-field"/>
        </div>
        <div className="w-full">
          <label htmlFor="minOrder" className="block text-sm font-medium text-gray-700 mb-1">Hóa đơn tối thiểu</label>
          <input v-model="formattedMinOrder" id="minOrder" type="text" placeholder="Hóa đơn tối thiểu"
                 className="input-field" @input="parseMinOrder"/>
        </div>
        <div className="w-full">
          <label htmlFor="valueFilter" className="block text-sm font-medium text-gray-700 mb-1">Giá trị phiếu</label>
          <input v-model="formattedValueFilter" id="valueFilter" type="text" placeholder="Giá trị phiếu"
                 className="input-field" @input="parseValueFilter"/>
        </div>
        <div className="flex justify-end w-full col-span-full gap-2">
          <router-link to="/add-phieu-giam-gia">
            <button
              className="flex items-center gap-2 px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke-width="1.5"
                stroke="currentColor"
                class="w-5 h-5 mr-1"
              >
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
              </svg>
              Thêm phiếu giảm giá
            </button>
          </router-link>
        </div>
      </div>
      <DynamicTable class="dynamic-table" :data="vouchers" :columns="columns" :get-nested-value="getNestedValue"
                    @toggle-status="toggleStatusPGG"/>
      <footer className="bg-white shadow-lg rounded-lg p-4 flex justify-center items-center mt-2">
        <Pagination :current-page="currentPage" :total-pages="totalPages" @page-changed="goToPage"/>
      </footer>
    </div>
  </div>
</template>

<style scoped>
.input-field {
  @apply w-full h-12 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-sm border border-gray-300;
  box-sizing: border-box;
}

/* Điều chỉnh cho select để khớp với v-select trong ảnh 2 */
.input-field select {
  @apply w-full h-12 px-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-sm;
}

.dynamic-table td {
  max-width: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.dynamic-table td:hover {
  overflow: visible;
  white-space: normal;
  text-overflow: clip;
}

.dynamic-table table {
  width: 100% !important;
  border-collapse: collapse;
  table-layout: fixed;
}

.dynamic-table th,
.dynamic-table td {
  padding: 6px 2px;
  text-align: left;
  border: 1px solid #ddd;
  word-wrap: break-word;
  white-space: normal;
  font-size: 13px;
  max-width: 100px;
  min-width: 70px;
}

.dynamic-table th:nth-child(5),
.dynamic-table th:nth-child(6),
.dynamic-table th:nth-child(8) {
  max-width: 80px;
  line-height: 1.1;
}

.dynamic-table th:nth-child(9),
.dynamic-table th:nth-child(10) {
  max-width: 90px;
}

@media (max-width: 1024px) {
  .dynamic-table th,
  .dynamic-table td {
    max-width: 90px;
    font-size: 11px;
  }
}

@media (max-width: 768px) {
  .dynamic-table th,
  .dynamic-table td {
    max-width: 70px;
    font-size: 10px;
  }
}
</style>

<script setup>
import {onMounted, watch, computed, ref} from "vue";
import {useRoute} from "vue-router";
import usePhieuGiamGia from "@/views/Voucher/JS/AppVoucher"; // Đảm bảo đường dẫn đúng
import DynamicTable from "@/components/DynamicTable.vue";
import Pagination from "@/components/Pagination.vue";
import BreadcrumbWrapper from "@/components/BreadcrumbWrapper.vue";

// Destructure from usePhieuGiamGia
const {
  vouchers,
  columns,
  getNestedValue,
  searchQuery,
  filterType,
  filterStatus,
  startDate,
  endDate,
  minOrder,
  valueFilter,
  currentPage,
  totalPages,
  goToPage,
  toggleStatusPGG,
  filterPGG,
  searchPGG,
  fetchDataPGG,
} = usePhieuGiamGia();

// Computed properties để format giá trị hiển thị
const formattedMinOrder = computed({
  get: () => (minOrder.value ? formatCurrency(minOrder.value) : ''),
  set: (value) => parseMinOrder({target: {value}})
});

const formattedValueFilter = computed({
  get: () => (valueFilter.value ? formatCurrency(valueFilter.value) : ''),
  set: (value) => parseValueFilter({target: {value}})
});

// Hàm format tiền tệ
const formatCurrency = (value) => {
  if (!value) return '';
  return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.') + ' VNĐ';
};

// Hàm parse giá trị từ input về số nguyên
const parseMinOrder = (event) => {
  const value = event.target.value.replace(/[^\d]/g, ''); // Loại bỏ tất cả ký tự không phải số
  minOrder.value = value ? parseInt(value, 10) : 0;
};

const parseValueFilter = (event) => {
  const value = event.target.value.replace(/[^\d]/g, ''); // Loại bỏ tất cả ký tự không phải số
  valueFilter.value = value ? parseInt(value, 10) : 0;
};

// Watch và logic hiện tại
watch(searchQuery, (newQuery) => {
  if (newQuery.trim().length > 0) {
    searchPGG(0);
  } else {
    fetchDataPGG(0);
  }
});

watch([filterType, filterStatus, startDate, endDate, minOrder, valueFilter], () => {
  filterPGG(0);
});

const route = useRoute();
const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === "function") {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || ["Phiếu Giảm Giá"];
});

onMounted(() => fetchDataPGG(0));
</script>