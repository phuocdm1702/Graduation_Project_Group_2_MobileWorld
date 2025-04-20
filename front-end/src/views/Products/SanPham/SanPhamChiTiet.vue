<template>
  <div>
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />
    <div class="mt-2 mx-auto">
      <ToastNotification ref="toast" />

      <!-- Form lọc -->
      <div class="bg-white shadow-lg rounded-lg p-5 mb-2 mt-2 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-4 gap-4 form-container">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Tìm kiếm</label>
          <input
            v-model.trim="searchKeyword"
            @input="debouncedSearch"
            type="text"
            placeholder="Tìm kiếm theo mã sản phẩm..."
            class="input-field p-2 border border-gray-300"
          />
        </div>

        <!-- Bộ lọc màu sắc -->
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
            append-to-body
          />
        </div>

        <!-- Bộ lọc ROM -->
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
            append-to-body
          />
        </div>

        <!-- Bộ lọc RAM -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">RAM</label>
          <v-select
            v-model="searchFilters.idRam"
            :options="ramOptions"
            :get-option-label="option => `${option.dungLuong}`"
            :reduce="option => option.id"
            placeholder="Tất cả"
            class="input-field"
            @update:modelValue="searchProductDetails"
            clearable
            append-to-body
          />
        </div>

        <div class="flex justify-end w-full col-span-full gap-2">
          <button
            @click="resetAllFilters"
            class="flex items-center gap-2 px-4 py-2 bg-orange-500 text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
            </svg>
            Đặt lại tất cả
          </button>
          <button @click="goBack" class="px-4 py-2 bg-orange-500 text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition">
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
      <footer v-if="productDetails.length > 0" class="bg-white shadow-lg rounded-lg p-4 flex justify-center items-center mt-2 form-container">
        <Pagination :current-page="currentPage" :total-pages="totalPages" @page-changed="goToPage" />
      </footer>

      <!-- Modal hiển thị danh sách IMEI -->
      <div v-if="showImeiModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50 w-full">
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
      <div v-if="showEditImeiModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50">
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
              <span class="inline-block px-3 py-1 border rounded-full text-sm font-semibold bg-gray-200 text-blue-600">
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
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue';
import ToastNotification from '@/components/ToastNotification.vue';
import Pagination from '@/components/Pagination.vue';
import DynamicTable from '@/components/DynamicTable.vue';
import useSanPhamChiTiet from '@/views/Products/SanPham/composables/sanPhamChiTiet';
import VSelect from 'vue-select';
import 'vue-select/dist/vue-select.css';
import JsBarcode from 'jsbarcode';

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

    const resetAllFilters = () => {
      searchKeyword.value = '';
      searchFilters.value = {
        status: '',
        idMauSac: '',
        idBoNhoTrong: '',
        idRam: '',
      };
      searchProductDetails();
    };

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

    onMounted(() => {
      document.addEventListener('mousedown', handleMouseDown);
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
    };
  },
};
</script>

<style scoped>
/* Container bao quanh form và bảng */
.mx-auto {
  @apply w-full max-w-screen-2xl px-4;
}

/* Container cho form */
.form-container {
  @apply w-full overflow-x-auto;
}

/* Container cho bảng */
.table-container {
  @apply w-full overflow-x-auto;
}

/* Đồng bộ chiều rộng của form và bảng */
.form-container,
.table-container,
.dynamic-table {
  min-width: 1000px; /* Chiều rộng tối thiểu để đảm bảo nội dung không bị co quá nhỏ */
}

.table-container::-webkit-scrollbar {
  width: 6px;
}

.table-container::-webkit-scrollbar-thumb {
  background-color: #9ca3af; /* gray-400 */
  border-radius: 3px;
}

.table-container::-webkit-scrollbar-track {
  background-color: #f3f4f6; /* gray-100 */
}

/* CSS cho input và select trong form */
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

/* Đảm bảo cột Barcode hiển thị hình ảnh đúng kích thước */
:deep(.dynamic-table td:nth-child(4)) {
  @apply p-2 align-middle;
}

:deep(.dynamic-table td:nth-child(4) img) {
  @apply max-h-10 w-auto;
}

/* CSS cho bảng */
.dynamic-table {
  border-top-left-radius: 0px;
  border-top-right-radius: 0px;
  @apply w-full;
}
</style>