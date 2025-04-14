<template>
  <div>
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />
    <div class="mt-2 mx-auto">
      <ToastNotification ref="toast" />

      <!-- Form lọc -->
      <div class="bg-white shadow-lg rounded-lg p-5 mb-2 mt-2 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-4 gap-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Tìm kiếm</label>
          <input
            v-model.trim="searchKeyword"
            @input="debouncedSearch"
            type="text"
            placeholder="Tìm kiếm theo mã sản phẩm hoặc IMEI..."
            class="input-field p-2 border border-gray-300"
          />
        </div>

        <!-- Filter for Color -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Màu Sắc</label>
          <v-select
            v-model="searchFilters.idMauSac"
            :options="mauSacOptions"
            label="tenMau"
            :reduce="option => option.id"
            placeholder="Tất cả"
            class="input-field"
            @update:modelValue="searchProductDetails"
            clearable
          />
        </div>

        <!-- Filter for ROM -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">ROM</label>
          <v-select
            v-model="searchFilters.idBoNhoTrong"
            :options="boNhoTrongOptions"
            :get-option-label="option => `${option.dungLuong}`"
            :reduce="option => option.id"
            placeholder="Tất cả"
            class="input-field"
            @update:modelValue="searchProductDetails"
            clearable
          />
        </div>

        <!-- Filter for RAM -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">RAM</label>
          <v-select
            v-model="searchFilters.idRam"
            :options="ramOptions"
            :get-option-label="option => `${option.dungLuongRam}`"
            :reduce="option => option.id"
            placeholder="Tất cả"
            class="input-field"
            @update:modelValue="searchProductDetails"
            clearable
          />
        </div>

        <!-- Filter for Price Range -->
        <div class="col-span-full">
          <label class="block text-sm font-medium text-gray-700 mb-1">Khoảng giá</label>
          <div class="flex items-center gap-4">
            <div class="w-full">
              <div class="flex justify-between text-xs text-gray-500 mb-1">
                <span>{{ formatCurrency(priceRange.min) }}</span>
                <span>{{ formatCurrency(priceRange.max) }}</span>
              </div>
              <vue-slider
                v-model="priceRangeValue"
                :min="priceRange.min"
                :max="priceRange.max"
                :interval="100000"
                :tooltip-formatter="formatCurrency"
                @change="handlePriceRangeChange"
              />
              <div class="flex justify-between mt-2">
                <span class="text-sm">{{ formatCurrency(searchFilters.minPrice || priceRange.min) }}</span>
                <span class="text-sm">{{ formatCurrency(searchFilters.maxPrice || priceRange.max) }}</span>
              </div>
            </div>
            <button
              @click="resetPriceRange"
              class="text-sm text-orange-500 hover:text-orange-700 whitespace-nowrap"
            >
              Đặt lại
            </button>
          </div>
        </div>

        <div class="flex justify-end w-full col-span-full gap-2">
          <button
            @click="resetAllFilters"
            class="flex items-center gap-2 px-4 py-2 bg-gray-500 text-white font-semibold rounded-lg shadow-md hover:bg-gray-600 transition"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
            </svg>
            Đặt lại tất cả
          </button>
          <button @click="goBack" class="px-4 py-2 bg-gray-500 text-white font-semibold rounded-lg shadow-md hover:bg-gray-600 transition">
            Quay lại
          </button>
        </div>
      </div>

      <!-- Thông báo khi không có dữ liệu -->
      <div v-if="productDetails.length === 0" class="text-center text-gray-500 py-4">
        Không tìm thấy chi tiết sản phẩm nào.
      </div>

      <!-- Bảng danh sách chi tiết sản phẩm -->
      <DynamicTable
        v-else
        class="dynamic-table"
        :data="productDetails"
        :columns="columns"
        :get-nested-value="getNestedValue"
      />

      <!-- Phân trang -->
      <footer v-if="productDetails.length > 0" class="bg-white shadow-lg rounded-lg p-4 flex justify-center items-center mt-2">
        <Pagination :current-page="currentPage" :total-pages="totalPages" @page-changed="goToPage" />
      </footer>
    </div>
  </div>
</template>

<script>
// Thêm các import cần thiết từ Vue
import { ref } from 'vue';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue';
import ToastNotification from '@/components/ToastNotification.vue';
import Pagination from '@/components/Pagination.vue';
import DynamicTable from '@/components/DynamicTable.vue';
import useSanPhamChiTiet from '@/views/Products/SanPham/composables/sanPhamChiTiet';
import VSelect from 'vue-select';
import 'vue-select/dist/vue-select.css';
import VueSlider from 'vue-slider-component';
import 'vue-slider-component/theme/default.css';

export default {
  name: 'SanPhamChiTiet',
  components: {
    BreadcrumbWrapper,
    ToastNotification,
    Pagination,
    DynamicTable,
    VSelect,
    VueSlider
  },
  setup() {
    // Sử dụng composable để lấy tất cả logic
    const {
      toast,
      productDetails,
      searchKeyword,
      searchFilters,
      currentPage,
      totalPages,
      mauSacOptions,
      boNhoTrongOptions,
      ramOptions,
      breadcrumbItems,
      columns,
      goToPage,
      debouncedSearch,
      searchProductDetails,
      goBack,
      getNestedValue,
      priceRange,
      priceRangeValue,
      fetchPriceRange,
    } = useSanPhamChiTiet();
    

    const handlePriceRangeChange = (value) => {
      searchFilters.value.minPrice = value[0];
      searchFilters.value.maxPrice = value[1];
      searchProductDetails();
    };

    const resetPriceRange = () => {
      searchFilters.value.minPrice = null;
      searchFilters.value.maxPrice = null;
      priceRangeValue.value = [priceRange.value.min, priceRange.value.max];
      searchProductDetails();
    };

    const resetAllFilters = () => {
      searchKeyword.value = '';
      searchFilters.value = {
        status: '',
        idMauSac: '',
        idBoNhoTrong: '',
        idRam: '',
        minPrice: null,
        maxPrice: null
      };
      priceRangeValue.value = [priceRange.value.min, priceRange.value.max];
      searchProductDetails();
    };

    const formatCurrency = (value) => {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(value).replace('₫', 'VND');
    };

    return {
      toast,
      productDetails,
      searchKeyword,
      searchFilters,
      currentPage,
      totalPages,
      mauSacOptions,
      boNhoTrongOptions,
      ramOptions,
      breadcrumbItems,
      columns,
      goToPage,
      debouncedSearch,
      searchProductDetails,
      goBack,
      getNestedValue,
      priceRange,
      priceRangeValue,
      handlePriceRangeChange,
      resetPriceRange,
      resetAllFilters,
      formatCurrency,
    };
  },
};
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

.dynamic-table {
  border-top-left-radius: 0px;
  border-top-right-radius: 0px;
}

:deep(.vue-slider-rail) {
  @apply bg-gray-200;
}

:deep(.vue-slider-process) {
  @apply bg-orange-500;
}

:deep(.vue-slider-dot-handle) {
  @apply border-orange-500;
}

:deep(.vue-slider-dot-tooltip-inner) {
  @apply bg-orange-500 border-orange-500 text-white;
}
</style>