<template>
  <div>
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />
    <div class="mt-2 mx-auto">
      <ToastNotification ref="toast" />

      <!-- Form lọc -->
      <div
        class="bg-white shadow-lg rounded-lg p-5 mb-2 mt-2 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-3 gap-4"
      >
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Tìm kiếm</label>
          <input
            v-model.trim="searchKeyword"
            @input="debouncedSearch"
            type="text"
            placeholder="Tìm kiếm theo tên sản phẩm..."
            class="input-field p-2 border border-gray-300"
          />
        </div>

        <!-- Filter for Manufacturer -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Hãng</label>
          <v-select
            v-model="searchFilters.idNhaSanXuat"
            :options="nhaSanXuatOptions"
            label="nhaSanXuat"
            :reduce="option => option.id"
            placeholder="Tất cả"
            class="input-field"
            @update:modelValue="searchProductDetails"
            clearable
          />
        </div>

        <!-- Filter for OS -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Hệ Điều Hành</label>
          <v-select
            v-model="searchFilters.idHeDieuHanh"
            :options="heDieuHanhOptions"
            :get-option-label="option => `${option.heDieuHanh} ${option.phienBan}`"
            :reduce="option => option.id" 
            placeholder="Tất cả"
            class="input-field"
            @update:modelValue="searchProductDetails"
            clearable
          />
        </div>

        <!-- Filter for Screen Technology -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Công nghệ màn hình</label>
          <v-select
            v-model="searchFilters.idCongNgheManHinh"
            :options="congNgheManHinhOptions"
            :get-option-label="option => `${option.chuanManHinh} ${option.congNgheManHinh}`"
            :reduce="option => option.id"
            placeholder="Tất cả"
            class="input-field"
            @update:modelValue="searchProductDetails"
            clearable
          />
        </div>

        <!-- Filter for Battery -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Pin</label>
          <v-select
            v-model="searchFilters.idPin"
            :options="pinOptions"
            :get-option-label="option => `${option.loaiPin} ${option.dungLuongPin}`"
            :reduce="option => option.id"
            placeholder="Tất cả"
            class="input-field"
            @update:model-value="searchProductDetails"
            clearable
          />
        </div>

        <!-- Filter for Stock Status -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Trạng thái tồn kho</label>
          <div class="flex items-center gap-4 mt-3">
            <label class="flex items-center gap-1">
              <input
                type="radio"
                v-model="searchFilters.stockStatus"
                value=""
                @change="searchProductDetails"
                class="form-radio"
              />
              <span class="text-sm">Tất cả</span>
            </label>
            <label class="flex items-center gap-1">
              <input
                type="radio"
                v-model="searchFilters.stockStatus"
                value="inStock"
                @change="searchProductDetails"
                class="form-radio"
              />
              <span class="text-sm">Còn hàng</span>
            </label>
            <label class="flex items-center gap-1">
              <input
                type="radio"
                v-model="searchFilters.stockStatus"
                value="outOfStock"
                @change="searchProductDetails"
                class="form-radio"
              />
              <span class="text-sm">Hết hàng</span>
            </label>
          </div>
        </div>

        <!-- Action buttons -->
        <div class="flex justify-end w-full col-span-full gap-2">
          <button
            @click="resetSearch"
            class="flex items-center gap-2 px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke-width="1.5"
              stroke="currentColor"
              class="w-5 h-5 mr-1"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="M16.023 9.348h4.992v-.001M2.985 19.644v-4.992m0 0h4.992m-4.993 0l3.181 3.183a8.25 8.25 0 0013.803-3.7M4.031 9.865a8.25 8.25 0 0113.803-3.7l3.181 3.182m0-4.991v4.992"
              />
            </svg>
            Đặt lại
          </button>
          <button
            @click="navigateToAddPage"
            class="flex items-center gap-2 px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition"
          >
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
            Thêm chi tiết sản phẩm
          </button>
        </div>
      </div>

      <!-- Thông báo khi không có dữ liệu -->
      <div v-if="productDetails.length === 0" class="text-center text-gray-500 py-4">
        Không tìm thấy sản phẩm nào.
      </div>

      <!-- Bảng danh sách sản phẩm -->
      <DynamicTable
        v-else
        class="dynamic-table"
        :data="productDetails"
        :columns="columns"
        :get-nested-value="getNestedValue"
      />

      <!-- Phân trang -->
      <footer
        v-if="productDetails.length > 0"
        class="bg-white shadow-lg rounded-lg p-4 flex justify-center items-center mt-2"
      >
        <Pagination :current-page="currentPage" :total-pages="totalPages" @page-changed="goToPage" />
      </footer>

      <!-- Modal xác nhận -->
      <ConfirmModal
        :show="showConfirmModal"
        :message="confirmMessage"
        @confirm="executeConfirmedAction"
        @cancel="closeConfirmModal"
      />
    </div>
  </div>
</template>

<script>
import { defineComponent, ref, computed } from 'vue';
import { useRoute } from 'vue-router';
import ToastNotification from '@/components/ToastNotification.vue';
import Pagination from '@/components/Pagination.vue';
import DynamicTable from '@/components/DynamicTable.vue';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';
import VSelect from 'vue-select';
import 'vue-select/dist/vue-select.css';
import debounce from 'lodash/debounce';
import sanPham from './composables/SanPham.js';

export default defineComponent({
  name: 'SanPham',
  components: {
    ToastNotification,
    Pagination,
    DynamicTable,
    BreadcrumbWrapper,
    ConfirmModal,
    VSelect,
  },
  setup() {
    const toast = ref(null);
    const route = useRoute();

    const {
      productDetails,
      searchKeyword,
      searchFilters,
      currentPage,
      totalPages,
      nhaSanXuatOptions,
      heDieuHanhOptions,
      congNgheManHinhOptions,
      pinOptions,
      cpuOptions,
      gpuOptions,
      cumCameraOptions,
      thietKeOptions,
      simOptions,
      congSacOptions,
      hoTroCongNgheSacOptions,
      congNgheMangOptions,
      loaiTinhTrangOptions,
      fetchData,
      goToPage,
      searchProductDetails,
      resetSearch,
      navigateToEditPage,
      navigateToAddPage,
      getNestedValue,
      showConfirmModal,
      confirmMessage,
      confirmAction,
      executeConfirmedAction,
      closeConfirmModal,
    } = sanPham(toast);

    const breadcrumbItems = computed(() => route.meta?.breadcrumb || ['Sản Phẩm']);

    const columns = [
      { key: '#', label: 'STT', formatter: (value, item, index) => currentPage.value * 5 + index + 1 },
      { key: 'tenSanPham', label: 'Tên Sản Phẩm' },
      { key: 'nhaSanXuat', label: 'Hãng' },
      {
        key: 'heDieuHanh',
        label: 'Hệ Điều Hành',
        formatter: (value, item) => {
          const heDieuHanh = item.heDieuHanh || 'N/A';
          const phienBan = item.phienBan || 'N/A';
          return `${heDieuHanh} ${phienBan}`;
        },
      },
      { key: 'congNgheManHinh', label: 'Màn hình' },
      { key: 'tenCpu', label: 'CPU' },
      { key: 'dungLuongPin', label: 'Pin' },
      {
        key: 'imeiCount',
        label: 'Số lượng',
        formatter: (value) => value || '0',
      },
      {
        key: 'priceRange',
        label: 'Khoảng giá',
        formatter: (value, item) => {
          const minPrice = item.minPrice || 0;
          const maxPrice = item.maxPrice || 0;
          return minPrice === maxPrice
            ? `${minPrice.toLocaleString('vi-VN')} VNĐ`
            : `${minPrice.toLocaleString('vi-VN')} - ${maxPrice.toLocaleString('vi-VN')} VNĐ`;
        },
      },
      {
        key: 'stockStatus',
        label: 'Trạng Thái',
        formatter: (value, item) => {
          const isOutOfStock = item.imeiCount === 0;
          return isOutOfStock
            ? '<span class="inline-block px-3 py-1 border rounded-full text-sm font-semibold bg-gray-200 text-red-600">Hết hàng</span>'
            : '<span class="inline-block px-3 py-1 border rounded-full text-sm font-semibold bg-gray-200 text-green-600">Còn hàng</span>';
        },
      },
      {
        key: 'actions',
        label: 'Hành động',
        formatter: (value, item) => {
          const safeItem = JSON.stringify(item);
          return `
            <div class="space-x-4">
              <button class="text-orange-500 hover:text-orange-700 transition" data-item='${safeItem}' onclick="document.dispatchEvent(new CustomEvent('viewDetails', { detail: JSON.parse(this.dataset.item) }))">
                <i class="fa-solid fa-eye"></i>
              </button>
              <button class="text-orange-500 hover:text-orange-700 transition" data-id="${item.id}" onclick="document.dispatchEvent(new CustomEvent('editProduct', { detail: this.dataset.id }))">
                <i class="fa-solid fa-edit"></i>
              </button>
            </div>
          `;
        },
      },
    ];

    const debouncedSearch = debounce(() => {
      currentPage.value = 0;
      searchProductDetails();
    }, 500);
    

    return {
      toast,
      productDetails,
      searchKeyword,
      searchFilters,
      currentPage,
      totalPages,
      nhaSanXuatOptions,
      heDieuHanhOptions,
      congNgheManHinhOptions,
      pinOptions,
      cpuOptions,
      gpuOptions,
      cumCameraOptions,
      thietKeOptions,
      simOptions,
      congSacOptions,
      hoTroCongNgheSacOptions,
      congNgheMangOptions,
      loaiTinhTrangOptions,
      fetchData,
      goToPage,
      debouncedSearch,
      searchProductDetails,
      resetSearch,
      navigateToEditPage,
      navigateToAddPage,
      getNestedValue,
      showConfirmModal,
      confirmMessage,
      confirmAction,
      executeConfirmedAction,
      closeConfirmModal,
      breadcrumbItems,
      columns,
    };
  },
});
</script>

<style scoped>
.input-field {
  @apply w-full h-12 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-sm;
}

.input-field :deep(.v-select) {
  @apply w-full border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-sm;
}

.input-field :deep(.vs__dropdown-toggle) {
  @apply border border-gray-300 rounded-lg;
}

.input-field :deep(.vs__search) {
  @apply p-2;
}

.form-radio {
  @apply h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300;
}

.dynamic-table {
  border-top-left-radius: 0px;
  border-top-right-radius: 0px;
}
</style>