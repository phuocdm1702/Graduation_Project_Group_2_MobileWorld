<template>
  <div>
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />
    <div class="mt-2 mx-auto">
      <ToastNotification ref="toast" />

      <!-- Form lọc -->
      <div class="bg-white shadow-lg rounded-lg p-5 mb-2 mt-2 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Tìm kiếm</label>
          <input v-model.trim="searchKeyword" @input="debouncedSearch" type="text" placeholder="Tìm kiếm theo tên sản phẩm..." class="input-field" />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Hãng</label>
          <select v-model="searchFilters.idNhaSanXuat" @change="searchProductDetails" class="input-field">
            <option value="">Tất cả</option>
            <option v-for="option in nhaSanXuatOptions" :key="option.id" :value="option.id">
              {{ option.nhaSanXuat }}
            </option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Hệ Điều Hành</label>
          <select v-model="searchFilters.idHeDieuHanh" @change="searchProductDetails" class="input-field">
            <option value="">Tất cả</option>
            <option v-for="option in heDieuHanhOptions" :key="option.id" :value="option.id">
              {{ option.heDieuHanh + " " + option.phienBan }}
            </option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Màn Hình</label>
          <select v-model="searchFilters.idManHinh" @change="searchProductDetails" class="input-field">
            <option value="">Tất cả</option>
            <option v-for="option in manHinhOptions" :key="option.id" :value="option.id">
              {{ option.kichThuoc + " " + option.doPhanGiai }}
            </option>
          </select>
        </div>
        <div class="flex justify-end w-full col-span-full gap-2">
          <button @click="resetSearch" class="flex items-center gap-2 px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5 mr-1">
              <path stroke-linecap="round" stroke-linejoin="round" d="M16.023 9.348h4.992v-.001M2.985 19.644v-4.992m0 0h4.992m-4.993 0l3.181 3.183a8.25 8.25 0 0013.803-3.7M4.031 9.865a8.25 8.25 0 0113.803-3.7l3.181 3.182m0-4.991v4.992" />
            </svg>
            Đặt lại
          </button>
          <button @click="navigateToAddPage" class="flex items-center gap-2 px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5 mr-1">
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
      <footer v-if="productDetails.length > 0" class="bg-white shadow-lg rounded-lg p-4 flex justify-center items-center mt-2">
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
import { defineComponent, ref, onMounted, computed } from 'vue';
import { useRoute } from 'vue-router';
import ToastNotification from '@/components/ToastNotification.vue';
import Pagination from '@/components/Pagination.vue';
import DynamicTable from '@/components/DynamicTable.vue';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';
import debounce from 'lodash/debounce';
import sanPham from './composables/SanPham.js';

export default defineComponent({
  name: 'SanPham',
  components: { ToastNotification, Pagination, DynamicTable, BreadcrumbWrapper, ConfirmModal },
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
      manHinhOptions,
      fetchData,
      fetchOptions,
      goToPage,
      searchProductDetails,
      resetSearch,
      confirmDelete,
      confirmAction,
      executeConfirmedAction,
      closeConfirmModal,
      navigateToAddPage,
      getNestedValue,
      showConfirmModal,
      confirmMessage,
    } = sanPham(toast);

    const breadcrumbItems = computed(() => route.meta?.breadcrumb || ['Sản Phẩm']);

    const columns = [
      { key: '#', label: 'STT', formatter: (value, item, index) => currentPage.value * 5 + index + 1 },
      { key: 'tenSanPham', label: 'Tên Sản Phẩm' },
      { key: 'idNhaSanXuat.nhaSanXuat', label: 'Hãng' },
      { key: 'idHeDieuHanh.heDieuHanh', label: 'Hệ Điều Hành' },
      { key: 'idManHinh.kichThuoc', label: 'Màn Hình' },
      {
        key: 'stockStatus',
        label: 'Trạng Thái',
        formatter: (value, item) => {
          return item.deleted
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
              <button class="text-blue-600 hover:text-blue-800 transition" data-item='${safeItem}' onclick="document.dispatchEvent(new CustomEvent('viewDetails', { detail: JSON.parse(this.dataset.item) }))">
                <i class="fa-solid fa-eye text-blue-500"></i>
              </button>
              <button class="text-red-600 hover:text-red-800 transition" data-id="${item.id}" onclick="document.dispatchEvent(new CustomEvent('confirmDelete', { detail: this.dataset.id }))">
                <i class="fa-solid fa-trash"></i>
              </button>
            </div>
          `;
        },
      },
    ];

    const debouncedSearch = debounce(() => {
      currentPage.value = 0; // Reset về trang đầu khi tìm kiếm
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
      manHinhOptions,
      fetchData,
      goToPage,
      debouncedSearch,
      searchProductDetails,
      resetSearch,
      confirmDelete,
      confirmAction,
      executeConfirmedAction,
      closeConfirmModal,
      navigateToAddPage,
      getNestedValue,
      breadcrumbItems,
      columns,
      showConfirmModal,
      confirmMessage,
    };
  },
});
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