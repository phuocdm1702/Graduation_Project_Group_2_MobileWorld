<template>
  <div>
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />
    <div>
      <ToastNotification ref="toast" />

      <!-- Form lọc -->
      <div class="bg-white shadow-lg rounded-lg p-5 mb-4 space-y-4 form-container">
        <!-- Dòng 1: Tìm kiếm + Khoảng giá -->
        <div class="flex flex-wrap items-end gap-4">
          <!-- Tìm kiếm -->
          <div class="flex-1 min-w-[220px]">
            <label class="block text-sm font-medium text-gray-700 mb-1">Tìm kiếm</label>
            <input
              v-model.trim="searchKeyword"
              @input="debouncedSearch"
              type="text"
              placeholder="Tìm kiếm theo mã, tên, màu, RAM, ROM, giá..."
              class="input-field p-2 border border-gray-300 w-full"
            />
          </div>

          <!-- Khoảng Giá -->
          <div class="flex-1 min-w-[320px]">
            <label class="block text-sm font-medium text-gray-700 mb-1">Khoảng Giá</label>
            <div class="slider-container">
              <div class="slider-track"></div>
              <div
                class="slider-range"
                :style="{
                  left: `${((priceRange[0] - minPrice) / (maxPrice - minPrice) * 100)}%`,
                  width: `${((priceRange[1] - priceRange[0]) / (maxPrice - minPrice) * 100)}%`
                }"
              ></div>

              <div
                class="slider-thumb"
                :style="{ left: `${((priceRange[0] - minPrice) / (maxPrice - minPrice) * 100)}%` }"
                @mousedown="(e) => startDrag('min', e)"
              ></div>

              <div
                class="slider-thumb"
                :style="{ left: `${((priceRange[1] - minPrice) / (maxPrice - minPrice) * 100)}%` }"
                @mousedown="(e) => startDrag('max', e)"
              ></div>

              <input
                type="range"
                v-model="priceRange[0]"
                :min="minPrice"
                :max="priceRange[1]"
                class="absolute opacity-0 w-full h-full"
              />
              <input
                type="range"
                v-model="priceRange[1]"
                :min="priceRange[0]"
                :max="maxPrice"
                class="absolute opacity-0 w-full h-full"
              />
            </div>
            <div class="flex justify-between text-sm text-gray-600 mt-1">
              <span>{{ formatPrice(priceRange[0]) }}</span>
              <span>{{ formatPrice(priceRange[1]) }}</span>
            </div>
          </div>
        </div>

        <!-- Dòng 2: Màu Sắc - ROM - RAM -->
        <div class="flex flex-wrap gap-4">
          <div class="flex-1 min-w-[160px]">
            <label class="block text-sm font-medium text-gray-700 mb-1">Màu Sắc</label>
            <v-select
              v-model="searchFilters.idMauSac"
              :options="mauSacOptions"
              label="tenMau"
              :reduce="option => option.id"
              placeholder="Tất cả"
              class="input-field"
              @update:model-value="searchProductDetails"
              :append-to-body="true"
              clearable
            />
          </div>

          <div class="flex-1 min-w-[160px]">
            <label class="block text-sm font-medium text-gray-700 mb-1">ROM</label>
            <v-select
              v-model="searchFilters.idBoNhoTrong"
              :options="boNhoTrongOptions"
              :get-option-label="option => `${option.dungLuong}`"
              :reduce="option => option.id"
              placeholder="Tất cả"
              class="input-field"
              @update:model-value="searchProductDetails"
              :append-to-body="true"
              clearable
            />
          </div>

          <div class="flex-1 min-w-[160px]">
            <label class="block text-sm font-medium text-gray-700 mb-1">RAM</label>
            <v-select
              v-model="searchFilters.idRam"
              :options="ramOptions"
              :get-option-label="option => `${option.dungLuong}`"
              :reduce="option => option.id"
              placeholder="Tất cả"
              class="input-field"
              @update:model-value="searchProductDetails"
              :append-to-body="true"
              clearable
            />
          </div>
        </div>

        <!-- Nút -->
        <div class="flex justify-end gap-2 pt-2 border-t border-gray-200">
          <button
            @click="resetAllFilters"
            class="flex items-center gap-2 px-4 py-2 bg-orange-500 text-white font-semibold rounded-lg shadow hover:bg-orange-600 transition"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="h-5 w-5"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"
              />
            </svg>
            Đặt lại
          </button>
          <button
            @click="goBack"
            class="px-4 py-2 bg-orange-500 text-white font-semibold rounded-lg shadow hover:bg-orange-600 transition"
          >
            Quay lại
          </button>
        </div>
      </div>

      <!-- Thông báo khi không có dữ liệu -->
      <div v-if="productDetails.length === 0" class="text-center text-gray-500 py-4">
        Không tìm thấy chi tiết sản phẩm nào.
      </div>

      <!-- Bảng danh sách chi tiết sản phẩm -->
      <div class="table-container">
        <DynamicTable
          v-if="productDetails.length > 0"
          class="dynamic-table"
          :data="productDetails"
          :columns="columns"
          :get-nested-value="getNestedValue"
        />
      </div>

      <!-- Phân trang -->
      <footer
        v-if="productDetails.length > 0"
        class="bg-white shadow-lg rounded-lg p-4 flex justify-center items-center mt-2 form-container"
      >
        <Pagination :current-page="currentPage" :total-pages="totalPages" @page-changed="goToPage" />
      </footer>

      <!-- Modal hiển thị danh sách IMEI -->
      <div
        v-if="showImeiModal"
        class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50 w-full"
      >
        <div class="bg-white rounded-lg p-6 w-full max-w-4xl">
          <h2 class="text-lg font-semibold mb-4">Danh sách IMEI</h2>
          <DynamicTable
            :data="selectedImeis"
            :columns="imeiColumns"
            :get-nested-value="getNestedValue"
            class="max-h-60 overflow-y-auto"
          />
          <div v-if="selectedImeis.length === 0" class="text-center text-gray-500 py-4">
            Không có IMEI nào.
          </div>
          <div class="mt-4 flex justify-end">
            <button
              @click="showImeiModal = false"
              class="px-4 py-2 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
            >
              Đóng
            </button>
          </div>
        </div>
      </div>

      <!-- Modal chỉnh sửa IMEI -->
      <div
        v-if="showEditImeiModal"
        class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50"
      >
        <div class="bg-white rounded-lg p-6 w-full max-w-md">
          <div class="flex justify-between items-center mb-4">
            <h2 class="text-lg font-semibold">Chỉnh sửa IMEI</h2>
            <button
              @click="showEditImeiModal = false"
              class="px-4 py-2 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
            >
              Đóng
            </button>
          </div>
          <div class="space-y-4">
            <div class="flex items-center space-x-4 flex-col">
              <div class="flex-1">
                <label class="block text-sm font-medium text-gray-700 mb-1">IMEI</label>
                <input
                  v-model="editingImei.imei"
                  type="text"
                  class="w-full p-2 border border-gray-300 rounded text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                  placeholder="Nhập IMEI..."
                />
              </div>
              <div>
                <img :id="`edit-barcode-${editingImei.id}`" class="h-10 w-auto" alt="Barcode" />
              </div>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Trạng Thái</label>
              <span
                class="inline-block px-3 py-1 border rounded-full text-sm font-semibold bg-gray-200 text-blue-600"
              >
                Chưa bán
              </span>
            </div>
          </div>
          <div class="mt-4 flex justify-end space-x-2">
            <button
              @click="showEditImeiModal = false"
              class="px-4 py-2 bg-gray-300 text-gray-700 rounded-lg hover:bg-gray-400 transition"
            >
              Hủy
            </button>
            <button
              @click="updateImei"
              class="px-4 py-2 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
            >
              Lưu
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue';
import ToastNotification from '@/components/ToastNotification.vue';
import Pagination from '@/components/Pagination.vue';
import DynamicTable from '@/components/DynamicTable.vue';
import VSelect from 'vue-select';
import '@vueform/slider/themes/default.css';
import 'vue-select/dist/vue-select.css';
import JsBarcode from 'jsbarcode';
import axios from 'axios';
import useSanPhamChiTiet from '@/views/Products/SanPham/composables/sanPhamChiTiet';

export default {
  name: 'SanPhamChiTiet',
  components: {
    BreadcrumbWrapper,
    ToastNotification,
    Pagination,
    DynamicTable,
    VSelect,
  },
  setup() {
    const route = useRoute();
    const productId = route.params.id;
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
      imeiColumns,
      goToPage,
      debouncedSearch,
      searchProductDetails,
      goBack,
      getNestedValue,
      showImeiModal,
      showEditImeiModal,
      selectedImeis,
      editingImei,
      updateImei,
    } = useSanPhamChiTiet();

    // Biến cho khoảng giá
    const minPrice = ref(0);
    const maxPrice = ref(10000000);
    const priceRange = ref([0, 10000000]);

    // Hàm định dạng giá
    const formatPrice = (value) => {
      if (isNaN(value)) return '0 VNĐ';
      return new Intl.NumberFormat('vi-VN').format(value) + ' VNĐ';
    };

    // Lấy khoảng giá từ API
    const fetchPriceRange = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/chi-tiet-san-pham/${productId}/price-range`);
        minPrice.value = Number(response.data.minPrice) || 0;
        maxPrice.value = Number(response.data.maxPrice) || 10000000;
        if (minPrice.value > maxPrice.value) {
          minPrice.value = 0;
          maxPrice.value = 10000000;
        }
        priceRange.value = [minPrice.value, maxPrice.value];
        searchFilters.value.minPrice = minPrice.value;
        searchFilters.value.maxPrice = maxPrice.value;
        updatePriceFilter();
      } catch (error) {
        toast.value?.kshowToast('error', 'Không thể tải khoảng giá!');
        console.error('Lỗi khi tải khoảng giá:', error);
        minPrice.value = 0;
        maxPrice.value = 10000000;
        priceRange.value = [0, 10000000];
        updatePriceFilter();
      }
    };

    // Cập nhật bộ lọc giá
    const updatePriceFilter = () => {
      searchFilters.value.minPrice = priceRange.value[0];
      searchFilters.value.maxPrice = priceRange.value[1];
      searchProductDetails();
    };

    // Reset bộ lọc
    const resetAllFilters = () => {
      searchKeyword.value = '';
      searchFilters.value = {
        status: '',
        idMauSac: '',
        idBoNhoTrong: '',
        idRam: '',
        minPrice: minPrice.value,
        maxPrice: maxPrice.value,
      };
      priceRange.value = [minPrice.value, maxPrice.value];
      searchProductDetails();
    };

    // Cập nhật barcode khi chỉnh sửa IMEI
    const updateEditBarcode = () => {
      if (!showEditImeiModal.value || !editingImei.value.id) return;
      const barcodeId = `edit-barcode-${editingImei.value.id}`;
      setTimeout(() => {
        const canvas = document.createElement('canvas');
        JsBarcode(canvas, editingImei.value.imei || 'N/A', {
          format: 'CODE128',
          width: 2,
          height: 40,
          displayValue: false,
        });
        const img = document.getElementById(barcodeId);
        if (img) {
          img.src = canvas.toDataURL('image/png');
        }
      }, 0);
    };

    watch(() => editingImei.value.imei, () => {
      updateEditBarcode();
    });

    watch(showImeiModal, (newVal) => {
      if (newVal) {
        updateEditBarcode();
      }
    });

    const handleMouseDown = (event) => {
      try {
        if (window.getSelection) {
          const selection = window.getSelection();
          if (selection && selection.toString()) {
            // Không làm gì thêm trừ khi cần
          }
        }
      } catch (error) {
        console.warn('Lỗi xử lý getSelection:', error);
      }
    };

    const startDrag = (type, event) => {
      const slider = event.currentTarget.closest('.slider-container');
      const rect = slider.getBoundingClientRect();

      const handleMove = (e) => {
        const percent = Math.min(Math.max((e.clientX - rect.left) / rect.width, 0), 1);
        const rawValue = minPrice.value + percent * (maxPrice.value - minPrice.value);
        const value = Math.round(rawValue / 100000) * 100000;

        if (type === 'min') {
          const newMin = Math.max(value, minPrice.value);
          priceRange.value[0] = Math.min(newMin, priceRange.value[1]);
        } else {
          const newMax = Math.min(value, maxPrice.value);
          priceRange.value[1] = Math.max(newMax, priceRange.value[0]);
        }

        updatePriceFilter();
      };

      const handleUp = () => {
        document.removeEventListener('mousemove', handleMove);
        document.removeEventListener('mouseup', handleUp);
      };

      document.addEventListener('mousemove', handleMove);
      document.addEventListener('mouseup', handleUp);
    };

    onMounted(() => {
      document.addEventListener('mousedown', handleMouseDown);
      fetchPriceRange().then(() => {
        searchProductDetails();
      });
    });

    onUnmounted(() => {
      document.removeEventListener('mousedown', handleMouseDown);
    });

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
      imeiColumns,
      goToPage,
      debouncedSearch,
      searchProductDetails,
      goBack,
      getNestedValue,
      resetAllFilters,
      showImeiModal,
      showEditImeiModal,
      selectedImeis,
      editingImei,
      updateImei,
      minPrice,
      maxPrice,
      priceRange,
      updatePriceFilter,
      formatPrice,
      startDrag,
    };
  },
};
</script>

<style scoped>
.mx-auto {
  @apply w-full max-w-screen-2xl px-4;
}

.form-container {
  @apply w-full overflow-x-auto;
}

.table-container {
  @apply w-full overflow-x-auto;
}

.form-container,
.table-container,
.dynamic-table {
  min-width: 1000px;
}

.table-container::-webkit-scrollbar {
  width: 6px;
}

.table-container::-webkit-scrollbar-thumb {
  background-color: #9ca3af;
  border-radius: 3px;
}

.table-container::-webkit-scrollbar-track {
  background-color: #f3f4f6;
}

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

:deep(.dynamic-table td:nth-child(4)) {
  @apply p-2 align-middle;
}

:deep(.dynamic-table td:nth-child(4) img) {
  @apply max-h-10 w-auto;
}

.dynamic-table {
  border-top-left-radius: 0px;
  border-top-right-radius: 0px;
  @apply w-full;
}

:deep(.slider-connect) {
  @apply bg-orange-500;
}

:deep(.slider-tooltip) {
  @apply bg-orange-500 text-white;
}

/* Slider container */
.slider-container {
  position: relative;
  height: 30px;
  width: 100%;
  display: flex;
  align-items: center;
}

/* Track styling */
.slider-track {
  height: 4px;
  width: 100%;
  background-color: #e5e7eb;
  border-radius: 2px;
  position: absolute;
}

/* Active range */
.slider-range {
  height: 4px;
  background-color: #f97316; /* Màu cam */
  border-radius: 2px;
  position: absolute;
}

/* Thumb styling */
.slider-thumb {
  width: 20px;
  height: 20px;
  background-color: #f97316;
  border-radius: 50%;
  border: 2px solid white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  position: absolute;
  top: 50%;
  transform: translate(-50%, -50%);
  cursor: pointer;
  z-index: 2;
}

.slider-thumb:hover {
  transform: translate(-50%, -50%) scale(1.1);
}

/* Hiển thị giá trị */
.slider-values {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
  font-size: 0.875rem;
  color: #4b5563;
}
</style>