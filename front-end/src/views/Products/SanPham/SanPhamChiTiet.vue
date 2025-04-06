<template>
  <div>
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />
    <div class="mt-2 mx-auto">
      <ToastNotification ref="toast" />

      <!-- Form lọc -->
      <div class="bg-white shadow-lg rounded-lg p-5 mb-2 mt-2 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Tìm kiếm</label>
          <input v-model.trim="searchKeyword" @input="debouncedSearch" type="text" placeholder="Tìm kiếm theo mã sản phẩm..." class="input-field" />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Màu Sắc</label>
          <select v-model="searchFilters.idMauSac" @change="searchProductDetails" class="input-field">
            <option value="">Tất cả</option>
            <option v-for="option in mauSacOptions" :key="option.id" :value="option.id">
              {{ option.tenMau || 'N/A' }}
            </option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">ROM</label>
          <select v-model="searchFilters.idBoNhoTrong" @change="searchProductDetails" class="input-field">
            <option value="">Tất cả</option>
            <option v-for="option in boNhoTrongOptions" :key="option.id" :value="option.id">
              {{ option.dungLuong ? `${option.dungLuong}` : 'N/A' }}
            </option>
          </select>
        </div>
        <div class="flex justify-end w-full col-span-full gap-2">
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
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue';
import ToastNotification from '@/components/ToastNotification.vue';
import Pagination from '@/components/Pagination.vue';
import DynamicTable from '@/components/DynamicTable.vue';
import debounce from 'lodash/debounce';

export default {
  name: 'SanPhamChiTiet',
  components: { BreadcrumbWrapper, ToastNotification, Pagination, DynamicTable },
  setup() {
    const route = useRoute();
    const router = useRouter();
    const toast = ref(null);
    const productDetails = ref([]);
    const searchKeyword = ref('');
    const searchFilters = ref({
      status: '',
      idMauSac: '',
      idBoNhoTrong: '',
      idRam: '',
    });
    const currentPage = ref(0);
    const pageSize = ref(5);
    const totalItems = ref(0);
    const productId = computed(() => route.params.id);

    const mauSacOptions = ref([]);
    const boNhoTrongOptions = ref([]);
    const ramOptions = ref([]);

    const breadcrumbItems = computed(() => route.meta?.breadcrumb || ['Sản Phẩm Chi Tiết']);

    const columns = [
      { key: '#', label: 'STT', formatter: (value, item, index) => currentPage.value * 5 + index + 1 },
      { key: 'ma', label: 'Mã Sản Phẩm' },
      {
        key: 'variants[0].mauSac',
        label: 'Màu Sắc',
        formatter: (value) => value || 'N/A'
      },
      {
        key: 'variants[0].dungLuongRam',
        label: 'RAM',
        formatter: (value) => value ? `${value}` : 'N/A'
      },
      {
        key: 'variants[0].dungLuongBoNhoTrong',
        label: 'ROM',
        formatter: (value) => value ? `${value}` : 'N/A'
      },
      {
        key: 'variants[0].idImel.imel',
        label: 'IMEI',
        formatter: (value) => value || 'N/A'
      },
      {
        key: 'giaBan',
        label: 'Đơn Giá',
        formatter: (value, item) => {
          const id = item.id || '';
          if (!id) {
            return `
              <input
                type="number"
                value="${value || 0}"
                class="w-full p-1 border border-gray-300 rounded text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                disabled
                title="Không thể cập nhật giá do thiếu ID"
              />
            `;
          }
          return `
            <input
              type="number"
              value="${value || 0}"
              class="w-full p-1 border border-gray-300 rounded text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              onblur="updatePrice('${id}', this.value)"
            />
          `;
        }
      },
      {
        key: 'deleted',
        label: 'Trạng Thái',
        formatter: (value) => {
          return value
            ? '<span class="inline-block px-3 py-1 border rounded-full text-sm font-semibold bg-gray-200 text-red-600">Đã đóng</span>'
            : '<span class="inline-block px-3 py-1 border rounded-full text-sm font-semibold bg-gray-200 text-green-600">Hoạt động</span>';
        },
      },
    ];

    const fetchProductDetails = async () => {
      try {
        const { data } = await axios.get(`http://localhost:8080/chi-tiet-san-pham/${productId.value}/details`, {
          params: {
            page: currentPage.value,
            size: pageSize.value,
            keyword: searchKeyword.value || undefined,
            status: searchFilters.value.status || undefined,
            idMauSac: searchFilters.value.idMauSac || undefined,
            idBoNhoTrong: searchFilters.value.idBoNhoTrong || undefined,
            idRam: searchFilters.value.idRam || undefined,
          },
        });
        productDetails.value = data.content || data;
        totalItems.value = data.totalElements || productDetails.value.length;
      } catch (error) {
        toast.value?.kshowToast('error', 'Không thể tải dữ liệu!');
        console.error('Fetch error:', error);
        productDetails.value = [];
        totalItems.value = 0;
      }
    };

    const updatePrice = async (id, newPrice) => {
      try {
        // Kiểm tra id có hợp lệ không
        const parsedId = parseInt(id);
        if (!id || isNaN(parsedId) || parsedId <= 0) {
          toast.value?.kshowToast('error', 'ID sản phẩm không hợp lệ!');
          return;
        }

        const price = parseFloat(newPrice);
        if (isNaN(price) || price < 0) {
          toast.value?.kshowToast('error', 'Giá không hợp lệ!');
          return;
        }

        const response = await axios.put(`http://localhost:8080/chi-tiet-san-pham/${id}/update-price`, null, {
          params: { newPrice: price }
        });

        // Cập nhật giá trong productDetails mà không cần tải lại toàn bộ dữ liệu
        const item = productDetails.value.find(item => item.id === id);
        if (item) {
          item.giaBan = price;
        }

        toast.value?.kshowToast('success', response.data.message || 'Cập nhật giá thành công!');
      } catch (error) {
        const errorMessage = error.response?.data?.message || 'Không thể cập nhật giá!';
        toast.value?.kshowToast('error', errorMessage);
        console.error('Update price error:', error);
      }
    };

    const fetchOptions = async () => {
      try {
        const [mauSacRes, boNhoTrongRes, ramRes] = await Promise.all([
          axios.get('http://localhost:8080/mau-sac'),
          axios.get('http://localhost:8080/bo-nho-trong'),
          axios.get('http://localhost:8080/ram'),
        ]);
        mauSacOptions.value = mauSacRes.data.content || mauSacRes.data || [];
        boNhoTrongOptions.value = boNhoTrongRes.data.content || boNhoTrongRes.data || [];
        ramOptions.value = ramRes.data.content || ramRes.data || [];
      } catch (error) {
        toast.value?.kshowToast('error', 'Lỗi khi tải danh sách tùy chọn!');
        console.error('Fetch options error:', error);
      }
    };

    const searchProductDetails = () => {
      currentPage.value = 0;
      fetchProductDetails();
    };

    const goToPage = async (page) => {
      currentPage.value = page;
      await fetchProductDetails();
    };

    const debouncedSearch = debounce(() => {
      currentPage.value = 0;
      fetchProductDetails();
    }, 500);

    const goBack = () => {
      router.push('/products');
    };

    const totalPages = computed(() => Math.ceil(totalItems.value / pageSize.value));

    const getNestedValue = (obj, path) => {
      try {
        return path.split('.').reduce((acc, part) => {
          if (part.includes('[')) {
            const [key, index] = part.split('[');
            const idx = parseInt(index.replace(']', ''), 10);
            return acc[key][idx];
          }
          return acc && acc[part];
        }, obj);
      } catch (e) {
        console.error(`Error accessing path ${path}:`, e);
        return null;
      }
    };

    onMounted(() => {
      fetchProductDetails();
      fetchOptions();
      // Đăng ký hàm updatePrice toàn cục để có thể gọi từ HTML
      window.updatePrice = updatePrice;
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
      goToPage,
      debouncedSearch,
      searchProductDetails,
      goBack,
      getNestedValue,
    };
  },
};
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