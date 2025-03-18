<template>
  <div>
    <!-- Thêm BreadcrumbWrapper -->
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />

    <div class="mt-2 mx-auto">
      <ToastNotification ref="toast"/>

      <!-- Form lọc -->
      <div
        class="bg-white shadow-lg rounded-lg p-5 mb-2 mt-2 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4"
      >
        <!-- Ô tìm kiếm -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Tìm kiếm</label>
          <input
            v-model.trim="searchKeyword"
            type="text"
            placeholder="Tìm kiếm theo các trường..."
            class="input-field"
          />
        </div>

        <!-- Bộ lọc Hãng -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Hãng</label>
          <select v-model="searchFilters.idNhaSanXuat" class="input-field">
            <option value="">Tất cả</option>
            <option v-for="option in nhaSanXuatOptions" :key="option.id" :value="option.id">
              {{ option.nhaSanXuat }}
            </option>
          </select>
        </div>

        <!-- Bộ lọc Hệ Điều Hành -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Hệ Điều Hành</label>
          <select v-model="searchFilters.idHeDieuHanh" class="input-field">
            <option value="">Tất cả</option>
            <option v-for="option in heDieuHanhOptions" :key="option.id" :value="option.id">
              {{ option.heDieuHanh }}
            </option>
          </select>
        </div>

        <!-- Bộ lọc Màn Hình -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Màn Hình</label>
          <select v-model="searchFilters.idManHinh" class="input-field">
            <option value="">Tất cả</option>
            <option v-for="option in manHinhOptions" :key="option.id" :value="option.id">
              {{ option.kichThuoc }}
            </option>
          </select>
        </div>

        <!-- Nút chức năng -->
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

          <!-- Button Thêm mới -->
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
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15"/>
            </svg>
            Thêm mới
          </button>
        </div>
      </div>

      <!-- Nút xóa các sản phẩm đã chọn -->
      <div v-if="selectedProducts.length" class="bg-white shadow-lg rounded-lg p-5 mb-4 mt-4 flex justify-end">
        <button
          @click="confirmDeleteSelected"
          class="flex items-center gap-2 px-4 py-2 bg-red-500 text-white font-semibold rounded-lg shadow-md hover:bg-red-600 transition"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            stroke-width="1.5"
            stroke="currentColor"
            class="w-5 h-5 mr-1"
          >
            <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12"/>
          </svg>
          Xóa {{ selectedProducts.length }} sản phẩm đã chọn
        </button>
      </div>

      <!-- Bảng danh sách chi tiết sản phẩm -->
      <DynamicTable
        class="dynamic-table"
        :data="productDetails"
        :columns="columns"
        :get-nested-value="getNestedValue"
        :selected-products="selectedProducts"
        @toggle-select="toggleSelect"
      >
        <!-- Slot để render checkbox "Chọn tất cả" trong tiêu đề cột select -->
        <template #header-select>
          <input
            type="checkbox"
            class="w-4 h-4 rounded"
            :checked="isAllSelected"
            @change="toggleSelectAll"
          >
        </template>
        <!-- Slot để render checkbox trong các dòng -->
        <template #cell-select="{ item }">
          <input
            type="checkbox"
            class="w-4 h-4 rounded"
            :checked="selectedProducts.includes(item.id)"
            @change="toggleSelect(item.id)"
          >
        </template>
        <!-- Slot để render trạng thái -->
        <template #cell-idLoaiTinhTrang="{ item }">
          <span
            :class="{
              'bg-green-100 text-green-800 px-2 py-1 rounded': item.idLoaiTinhTrang === 'Hoạt động',
              'bg-red-100 text-red-800 px-2 py-1 rounded': item.idLoaiTinhTrang === 'Đã dừng'
            }"
          >
            {{ item.idLoaiTinhTrang }}
          </span>
        </template>
      </DynamicTable>

      <!-- Phân trang -->
      <footer class="bg-white shadow-lg rounded-lg p-4 flex justify-center items-center mt-2">
        <Pagination
          :current-page="currentPage"
          :total-pages="totalPages"
          @page-changed="goToPage"
        />
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
import productDetailList from './productDetailList.js'; // Import file JS riêng
import ToastNotification from '@/components/ToastNotification.vue'; // Đảm bảo đường dẫn đúng
import Pagination from '@/components/Pagination.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';
import DynamicTable from '@/components/DynamicTable.vue';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue'; // Import BreadcrumbWrapper

export default defineComponent({
  name: 'SanPham',
  components: {
    ToastNotification,
    Pagination,
    ConfirmModal,
    DynamicTable,
    BreadcrumbWrapper // Đăng ký component BreadcrumbWrapper
  },
  setup() {
    const toast = ref(null); // Khởi tạo toast ref
    const route = useRoute();

    // Tính toán breadcrumb dựa trên meta của route
    const breadcrumbItems = computed(() => {
      if (typeof route.meta.breadcrumb === "function") {
        return route.meta.breadcrumb(route);
      }
      return route.meta?.breadcrumb || ["Sản Phẩm"]; // Mặc định nếu không có breadcrumb
    });

    // Truyền toast vào productDetailList
    const {
      productDetails,
      searchKeyword,
      searchFilters,
      currentPage,
      totalPages,
      selectedProducts,
      isSearching,
      showConfirmModal,
      confirmMessage,
      fetchData,
      goToPage,
      searchProductDetails,
      resetSearch,
      confirmDelete,
      confirmDeleteSelected,
      confirmAction,
      executeConfirmedAction,
      closeConfirmModal,
      toggleSelectAll,
      isAllSelected,
      nhaSanXuatOptions,
      heDieuHanhOptions,
      manHinhOptions,
      getNestedValue,
      toggleSelect,
      navigateToAddPage,
    } = productDetailList(toast);

    // Định nghĩa các cột chính hiển thị trong bảng
    const columns = [
      { key: 'select', label: '' },
      {
        key: '#',
        label: 'STT',
        formatter: (value, item, index) => (currentPage.value * 5) + index + 1,
      },
      { key: 'dongSanPham', label: 'Tên Sản Phẩm' },
      { key: 'tenNhaSanXuat', label: 'Hãng' },
      { key: 'tenHeDieuHanh', label: 'Hệ Điều Hành' },
      { key: 'kichThuocManHinh', label: 'Màn Hình' },
      { key: 'dungLuongPin', label: 'Pin' },
      { key: 'tenTinhTrang', label: 'Tình Trạng' },
      {
        key: 'actions',
        label: 'Thao Tác',
        formatter: (value, item) => {
          const safeItem = JSON.stringify(item);
          return `
            <div class="space-x-4">
              <button class="text-orange-600 hover:text-orange-800 transition" data-item='${safeItem}' onclick="document.dispatchEvent(new CustomEvent('openEditModal', { detail: JSON.parse(this.dataset.item) }))">
                <i class="fa-solid fa-eye text-blue-500"></i>
              </button>
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

    onMounted(async () => {
      await fetchData(); // Sử dụng await để đợi kết quả trả về
    });

    return {
      toast,
      productDetails,
      searchKeyword,
      searchFilters,
      currentPage,
      totalPages,
      selectedProducts,
      isSearching,
      showConfirmModal,
      confirmMessage,
      fetchData,
      goToPage,
      searchProductDetails,
      resetSearch,
      confirmDelete,
      confirmDeleteSelected,
      confirmAction,
      executeConfirmedAction,
      closeConfirmModal,
      toggleSelectAll,
      isAllSelected,
      nhaSanXuatOptions,
      heDieuHanhOptions,
      manHinhOptions,
      columns,
      getNestedValue,
      toggleSelect,
      navigateToAddPage,
      breadcrumbItems, // Trả về breadcrumbItems để sử dụng trong template
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