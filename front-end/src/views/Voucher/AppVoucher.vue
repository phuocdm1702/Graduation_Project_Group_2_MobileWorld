<template>
  <div>
    <!-- Thay Breadcrumb bằng BreadcrumbWrapper -->
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />

    <div class="mt-2 mx-auto">
<!--      <h2 class="bg-white shadow-lg rounded-lg p-5 mb-2 mt-2 text-2xl font-semibold text-gray-700">-->
<!--        Quản Lý Phiếu Giảm Giá-->
<!--      </h2>-->

      <!-- Form lọc -->
      <div
        class="bg-white shadow-lg rounded-lg p-5 mb-2 mt-2 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">

        <!-- Ô tìm kiếm -->
        <div>
          <label for="searchQuery" class="block text-sm font-medium text-gray-700 mb-1">Tìm kiếm phiếu</label>
          <input
            v-model="searchQuery"
            id="searchQuery"
            type="text"
            placeholder="Tìm theo mã hoặc tên phiếu..."
            class="input-field"
          />
        </div>

        <!-- Dropdown chọn loại phiếu -->
        <div>
          <label for="filterType" class="block text-sm font-medium text-gray-700 mb-1">Loại phiếu</label>
          <select
            v-model="filterType"
            id="filterType"
            @change="filterPGG"
            class="input-field"
          >
            <option value="">Tất cả loại phiếu</option>
            <option value="Phần trăm">Phần trăm</option>
            <option value="Tiền mặt">Tiền mặt</option>
          </select>
        </div>

        <!-- Dropdown chọn trạng thái -->
        <div>
          <label for="filterStatus" class="block text-sm font-medium text-gray-700 mb-1">Trạng thái</label>
          <select
            v-model="filterStatus"
            id="filterStatus"
            @change="filterPGG"
            class="input-field"
          >
            <option value="">Tất cả trạng thái</option>
            <option value="Chưa diễn ra">Chưa diễn ra</option>
            <option value="Đang diễn ra">Đang diễn ra</option>
          </select>
        </div>

        <!-- Chọn ngày bắt đầu -->
        <div>
          <label for="startDate" class="block text-sm font-medium text-gray-700 mb-1">Ngày bắt đầu</label>
          <input
            v-model="startDate"
            id="startDate"
            type="date"
            @change="filterPGG"
            class="input-field"
          />
        </div>

        <!-- Chọn ngày kết thúc -->
        <div>
          <label for="endDate" class="block text-sm font-medium text-gray-700 mb-1">Ngày kết thúc</label>
          <input
            v-model="endDate"
            id="endDate"
            type="date"
            @change="filterPGG"
            class="input-field"
          />
        </div>

        <!-- Ô nhập hóa đơn tối thiểu -->
        <div>
          <label for="minOrder" class="block text-sm font-medium text-gray-700 mb-1">Hóa đơn tối thiểu</label>
          <input
            v-model="minOrder"
            id="minOrder"
            type="number"
            placeholder="Hóa đơn tối thiểu"
            @change="filterPGG"
            class="input-field"
          />
        </div>

        <!-- Ô nhập giá trị phiếu -->
        <div>
          <label for="valueFilter" class="block text-sm font-medium text-gray-700 mb-1">Giá trị phiếu</label>
          <input
            v-model="valueFilter"
            id="valueFilter"
            type="number"
            placeholder="Giá trị phiếu"
            @change="filterPGG"
            class="input-field"
          />
        </div>

        <!-- Nút tìm kiếm và thêm phiếu -->
        <div class="flex justify-end w-full col-span-full gap-2">
          <!-- Button Thêm Phiếu -->
          <router-link to="/add-phieu-giam-gia">
            <button
              class="flex items-center gap-2 px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition"
            >
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5 mr-1">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15"/>
              </svg>
              Thêm Phiếu
            </button>
          </router-link>
        </div>
      </div>

      <!-- Bảng danh sách phiếu giảm giá -->
      <DynamicTable
        class="dynamic-table"
        :data="vouchers"
        :columns="columns"
        :get-nested-value="getNestedValue"
        @toggle-status="toggleStatusPGG"
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

<style scoped>
.input-field {
  @apply w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-400 outline-none;
}

.dynamic-table td {
  max-width: 100px;
  overflow: hidden;
  text-overflow: ellipsis; /* Cắt bớt nội dung dài */
  white-space: nowrap; /* Ngăn xuống dòng trong ô dữ liệu */
}

.dynamic-table td:hover {
  overflow: visible;
  white-space: normal; /* Hiển thị đầy đủ khi hover */
  text-overflow: clip;
}

/* Định dạng bảng từ lớp cha */
.dynamic-table table {
  width: 100% !important; /* Đảm bảo bảng chiếm toàn bộ chiều rộng */
  border-collapse: collapse;
  table-layout: fixed; /* Cố định kích thước cột */
}

.dynamic-table th,
.dynamic-table td {
  padding: 6px 2px; /* Giảm padding để tiết kiệm không gian hơn */
  text-align: left;
  border: 1px solid #ddd;
  word-wrap: break-word; /* Cho phép xuống dòng */
  white-space: normal; /* Đảm bảo văn bản xuống dòng tự nhiên */
  font-size: 13px; /* Giảm kích thước chữ thêm */
  max-width: 100px; /* Giới hạn chiều rộng tối đa cho tất cả cột */
  min-width: 70px; /* Giảm min-width để cột nhỏ hơn */
}

/* Tùy chỉnh các cột có nhãn dài, thu nhỏ hơn */
.dynamic-table th:nth-child(5), /* Phần trăm giảm giá */
.dynamic-table th:nth-child(6), /* Số tiền giảm tối đa */
.dynamic-table th:nth-child(8) { /* Hóa đơn tối thiểu */
  max-width: 80px; /* Thu nhỏ chiều rộng hơn */
  line-height: 1.1; 
}

/* Giữ nguyên các cột khác */
.dynamic-table th:nth-child(9), /* Ngày bắt đầu */
.dynamic-table th:nth-child(10) { /* Ngày kết thúc */
  max-width: 90px; /* Điều chỉnh nhẹ để vừa khít */
}



/* Responsive design */
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
import { onMounted, watch, ref, computed } from "vue";
import { useRoute } from "vue-router";
import AppVoucher from "@/views/Voucher/JS/AppVoucher";
import DynamicTable from "@/components/DynamicTable.vue";
import Pagination from '@/components/Pagination.vue';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue'; // Import BreadcrumbWrapper

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
  fetchDataPGG
} = AppVoucher();

// Theo dõi thay đổi ô tìm kiếm
watch(searchQuery, (newQuery) =>  {
  if (newQuery.trim().length > 0) {
    searchPGG();
  }
});

watch([filterType, filterStatus, startDate, endDate, minOrder, valueFilter], () => {
  goToPage(0);
});

// Lấy route hiện tại
const route = useRoute();

// Tính toán breadcrumb dựa trên meta của route
const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === "function") {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || ["Phiếu Giảm Giá"]; // Mặc định nếu không có breadcrumb
});

// Tải dữ liệu khi mounted
onMounted(fetchDataPGG);
</script>