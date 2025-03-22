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
          <select v-model="searchFilters.idNhaSanXuat" @change="searchProducts" class="input-field">
            <option value="">Tất cả</option>
            <option v-for="option in nhaSanXuatOptions" :key="option.id" :value="option.id">
              {{ option.nhaSanXuat }}
            </option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Hệ Điều Hành</label>
          <select v-model="searchFilters.idHeDieuHanh" @change="searchProducts" class="input-field">
            <option value="">Tất cả</option>
            <option v-for="option in heDieuHanhOptions" :key="option.id" :value="option.id">
              {{ option.heDieuHanh + " " + option.phienBan}}
            </option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Màn Hình</label>
          <select v-model="searchFilters.idManHinh" @change="searchProducts" class="input-field">
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
          <button @click="navigateToAddChiTietPage" class="flex items-center gap-2 px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5 mr-1">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
            </svg>
            Thêm chi tiết sản phẩm
          </button>
        </div>
      </div>

      <!-- Thông báo khi không có dữ liệu -->
      <div v-if="products.length === 0" class="text-center text-gray-500 py-4">
        Không tìm thấy sản phẩm nào.
      </div>

      <!-- Bảng danh sách sản phẩm -->
      <DynamicTable v-else class="dynamic-table" :data="products" :columns="columns" :get-nested-value="getNestedValue" />

      <!-- Phân trang -->
      <footer v-if="products.length > 0" class="bg-white shadow-lg rounded-lg p-4 flex justify-center items-center mt-2">
        <Pagination :current-page="currentPage" :total-pages="totalPages" @page-changed="goToPage" />
      </footer>
    </div>
  </div>
</template>

<script>
import { defineComponent, ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import ToastNotification from '@/components/ToastNotification.vue';
import Pagination from '@/components/Pagination.vue';
import DynamicTable from '@/components/DynamicTable.vue';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue';
import axios from 'axios';
import debounce from 'lodash/debounce';

export default defineComponent({
  name: 'SanPham',
  components: { ToastNotification, Pagination, DynamicTable, BreadcrumbWrapper },
  setup() {
    const toast = ref(null);
    const route = useRoute();
    const router = useRouter();
    const products = ref([]);
    const searchKeyword = ref('');
    const searchFilters = ref({ idNhaSanXuat: '', idHeDieuHanh: '', idManHinh: '' });
    const currentPage = ref(0);
    const totalPages = ref(0);

    const nhaSanXuatOptions = ref([]);
    const heDieuHanhOptions = ref([]);
    const manHinhOptions = ref([]);

    const breadcrumbItems = computed(() => route.meta?.breadcrumb || ['Sản Phẩm']);
    const columns = [
      { key: '#', label: 'STT', formatter: (value, item, index) => currentPage.value * 5 + index + 1 },
      { key: 'tenSanPham', label: 'Tên Sản Phẩm' },
      { key: 'idNhaSanXuat.nhaSanXuat', label: 'Hãng' },
      { key: 'idHeDieuHanh.heDieuHanh', label: 'Hệ Điều Hành' },
      { key: 'idManHinh.kichThuoc', label: 'Màn Hình' },
    ];

    const fetchData = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/san-pham', {
          params: { page: currentPage.value, size: 5 },
        });
        products.value = response.data.content || [];
        totalPages.value = response.data.totalPages || 0;
      } catch (error) {
        toast.value?.kshowToast('error', 'Không thể tải dữ liệu sản phẩm!');
        console.error('Fetch products error:', error);
      }
    };

    const searchProducts = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/san-pham/search', {
          params: {
            keyword: searchKeyword.value || undefined,
            idNhaSanXuat: searchFilters.value.idNhaSanXuat || undefined,
            idHeDieuHanh: searchFilters.value.idHeDieuHanh || undefined,
            idManHinh: searchFilters.value.idManHinh || undefined,
            page: currentPage.value,
            size: 5,
          },
        });
        products.value = response.data.content || [];
        totalPages.value = response.data.totalPages || 0;
      } catch (error) {
        toast.value?.kshowToast('error', 'Lỗi tìm kiếm sản phẩm!');
        console.error('Search products error:', error);
      }
    };

    const fetchOptions = async () => {
      try {
        const [nhaSanXuatRes, heDieuHanhRes, manHinhRes] = await Promise.all([
          axios.get('http://localhost:8080/api/nha-san-xuat'), // Sửa từ /nha-san-xuats thành /nha-san-xuat
          axios.get('http://localhost:8080/api/he-dieu-hanh'), // Cần thêm backend hỗ trợ
          axios.get('http://localhost:8080/api/man-hinh'),     // Cần thêm backend hỗ trợ
        ]);
        // Xử lý dữ liệu trả về từ API
        nhaSanXuatOptions.value = nhaSanXuatRes.data.content || nhaSanXuatRes.data || []; // Hỗ trợ cả phân trang và danh sách
        heDieuHanhOptions.value = heDieuHanhRes.data.content || heDieuHanhRes.data || [];
        manHinhOptions.value = manHinhRes.data.content || manHinhRes.data || [];
      } catch (error) {
        toast.value?.kshowToast('error', 'Lỗi khi tải danh sách tùy chọn!');
        console.error('Fetch options error:', error.response ? error.response.data : error.message);
      }
    };

    const debouncedSearch = debounce(() => {
      currentPage.value = 0; // Reset về trang đầu khi tìm kiếm
      searchProducts();
    }, 500);

    const goToPage = (page) => {
      currentPage.value = page;
      if (searchKeyword.value || Object.values(searchFilters.value).some((val) => val)) {
        searchProducts();
      } else {
        fetchData();
      }
    };

    const resetSearch = () => {
      searchKeyword.value = '';
      searchFilters.value = { idNhaSanXuat: '', idHeDieuHanh: '', idManHinh: '' };
      currentPage.value = 0;
      fetchData();
    };

    const navigateToAddChiTietPage = () => router.push('/products/add');

    const getNestedValue = (obj, path) => path.split('.').reduce((acc, part) => acc && acc[part], obj);

    onMounted(async () => {
      await fetchOptions(); // Load combobox trước
      await fetchData();    // Load table sau khi combobox đã có dữ liệu
    });

    return {
      toast,
      products,
      searchKeyword,
      searchFilters,
      currentPage,
      totalPages,
      fetchData,
      goToPage,
      debouncedSearch,
      searchProducts,
      resetSearch,
      nhaSanXuatOptions,
      heDieuHanhOptions,
      manHinhOptions,
      columns,
      getNestedValue,
      navigateToAddChiTietPage,
      breadcrumbItems,
    };
  },
});
</script>

<style scoped>
.input-field {
  @apply w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-sm;
}
</style>