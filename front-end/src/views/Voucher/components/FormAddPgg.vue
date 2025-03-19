<template>
  <!-- Thay Breadcrumb bằng BreadcrumbWrapper -->
  <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />
  <div class="flex h-screen p-4 bg-gray-100 gap-4">
    <!-- Form Container -->
    <div class="w-2/3 p-6 bg-white rounded-lg shadow-md">
      <form @submit.prevent="submitForm">
        <!-- Header -->
        <h3 class="text-xl font-semibold mb-4 text-gray-800">Thêm Phiếu Giảm Giá</h3>

        <!-- Form Content -->
        <div class="space-y-6">
          <!-- Discount Details -->
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Mã</label>
              <input type="text" v-model="ma" class="form-input" />
              <p v-if="errors.ma" class="error">{{ errors.ma }}</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên Phiếu Giảm Giá</label>
              <input type="text" v-model="tenPhieuGiamGia" class="form-input" />
              <p v-if="errors.tenPhieuGiamGia" class="error">{{ errors.tenPhieuGiamGia }}</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Loại Phiếu</label>
              <div class="flex items-center mt-2 space-x-4">
                <label class="flex items-center">
                  <input type="radio" name="voucherType" value="Phần trăm" v-model="loaiPhieuGiamGia" class="form-radio" />
                  <span class="ml-2">Phần trăm</span>
                </label>
                <label class="flex items-center">
                  <input type="radio" name="voucherType" value="Tiền mặt" v-model="loaiPhieuGiamGia" class="form-radio" />
                  <span class="ml-2">Tiền mặt</span>
                </label>
              </div>
              <p v-if="errors.loaiPhieuGiamGia" class="error">{{ errors.loaiPhieuGiamGia }}</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Phần trăm giảm giá</label>
              <input type="number" v-model="phanTramGiamGia" class="form-input" />
              <p v-if="errors.phanTramGiamGia" class="error">{{ errors.phanTramGiamGia }}</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Số tiền giảm tối đa</label>
              <input type="number" v-model="soTienGiamToiDa" class="form-input" />
              <p v-if="errors.soTienGiamToiDa" class="error">{{ errors.soTienGiamToiDa }}</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Hóa đơn tối thiểu</label>
              <input type="number" v-model="hoaDonToiThieu" class="form-input" />
              <p v-if="errors.hoaDonToiThieu" class="error">{{ errors.hoaDonToiThieu }}</p>
            </div>
          </div>

          <!-- Date Range and Usage -->
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Số lượng sử dụng</label>
              <input type="number" v-model="soLuongDung" class="form-input" />
              <p v-if="errors.soLuongDung" class="error">{{ errors.soLuongDung }}</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Ngày bắt đầu</label>
              <input type="date" v-model="ngayBatDau" class="form-input" />
              <p v-if="errors.ngayBatDau" class="error">{{ errors.ngayBatDau }}</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Ngày kết thúc</label>
              <input type="date" v-model="ngayKetThuc" class="form-input" />
              <p v-if="errors.ngayKetThuc" class="error">{{ errors.ngayKetThuc }}</p>
            </div>
          </div>

          <!-- Privacy and Description -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div class="flex items-center">
              <label class="block text-sm font-medium text-gray-700">Riêng tư</label>
              <input type="checkbox" v-model="riengTu" class="ml-2 form-checkbox" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Mô tả</label>
              <textarea v-model="moTa" class="form-input resize-none" rows="3"></textarea>
            </div>
          </div>
        </div>

        <!-- Buttons -->
        <div class="flex justify-end gap-3 mt-6">
          <router-link to="/phieu-giam-gia">
            <button type="button" class="px-4 py-2 bg-gray-300 text-gray-700 rounded-md hover:bg-gray-400 transition">Hủy</button>
          </router-link>
          <button type="submit" class="flex items-center gap-2 px-4 py-2 bg-orange-500 text-white rounded-md hover:bg-orange-600 transition">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15"/>
            </svg>
            Thêm
          </button>
        </div>
      </form>
    </div>

    <!-- Customer Selection -->
    <div class="w-1/3 p-6 bg-white rounded-lg shadow-md">
      <h4 class="text-xl font-semibold mb-4 text-gray-800">Chọn Khách Hàng</h4>
      <div class="mb-4">
        <input
          type="text"
          v-model="searchQuery"
          placeholder="Tìm kiếm khách hàng..."
          class="form-input w-full"
          @input="debouncedSearchKH"
        />
      </div>

      <div class="table-container" style="max-height: 40vh; overflow-y: auto;">
        <table class="w-full text-sm">
          <thead>
          <tr class="bg-gray-100 sticky top-0">
            <th class="p-3 text-left font-semibold">Chọn</th>
            <th class="p-3 text-left font-semibold">Mã KH</th>
            <th class="p-3 text-left font-semibold">Tên Khách Hàng</th>
            <th class="p-3 text-left font-semibold">Ngày sinh</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="customer in filteredCustomers" :key="customer.id" class="border-b hover:bg-gray-50">
            <td class="p-3">
              <input type="checkbox" v-model="selectedCustomers" :value="customer.id" class="form-checkbox" />
            </td>
            <td class="p-3">{{ customer.ma }}</td>
            <td class="p-3">{{ customer.ten }}</td>
            <td class="p-3">{{ new Date(customer.ngaySinh).toLocaleDateString('vi-VN') }}</td>
          </tr>
          </tbody>
        </table>
      </div>

      <div class="mt-6">
        <h4 class="text-lg font-semibold mb-2 text-gray-800">Khách Hàng Đã Chọn</h4>
        <div class="table-container" style="max-height: 30vh; overflow-y: auto;">
          <table class="w-full text-sm">
            <thead>
            <tr class="bg-gray-100 sticky top-0">
              <th class="p-3 text-left font-semibold">Mã KH</th>
              <th class="p-3 text-left font-semibold">Tên Khách Hàng</th>
              <th class="p-3 text-left font-semibold">Ngày sinh</th>
              <th class="p-3 text-left font-semibold">Hành động</th>
            </tr>
            </thead>
            <tbody>
            <tr v-if="selectedCustomerDetails.length === 0">
              <td colspan="4" class="p-3 text-center text-gray-500">Chưa có khách hàng nào được chọn</td>
            </tr>
            <tr v-else v-for="customer in selectedCustomerDetails" :key="customer.id" class="border-b hover:bg-gray-50">
              <td class="p-3">{{ customer.ma }}</td>
              <td class="p-3">{{ customer.ten }}</td>
              <td class="p-3">{{ new Date(customer.ngaySinh).toLocaleDateString('vi-VN') }}</td>
              <td class="p-3">
                <button
                  @click="removeCustomer(customer.id)"
                  class="px-2 py-1 bg-red-500 text-white rounded-md hover:bg-red-600 transition"
                >
                  Xóa
                </button>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import axios from "axios";
import { debounce } from "lodash";
import BreadcrumbWrapper from "@/components/BreadcrumbWrapper.vue";

const customers = ref([]);
const selectedCustomers = ref([]);
const router = useRouter();
const route = useRoute();


// Tính toán breadcrumb dựa trên meta của route
const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === "function") {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || ["Phiếu Giảm Giá", "Thêm Phiếu Giảm Giá"]; // Mặc định cho trang thêm phiếu giảm giá
});

const searchQuery = ref("");
const filteredCustomers = ref([]);

const ma = ref("");
const tenPhieuGiamGia = ref("");
const loaiPhieuGiamGia = ref("Phần trăm"); // Đặt mặc định là "Phần trăm"
const phanTramGiamGia = ref("");
const soTienGiamToiDa = ref("");
const hoaDonToiThieu = ref("");
const soLuongDung = ref("");
const ngayBatDau = ref("");
const ngayKetThuc = ref("");
const trangThai = ref(true); // Boolean
const riengTu = ref(false); // Boolean
const moTa = ref("");
const deleted = ref(false); // Mặc định là false

const baseURL = "http://localhost:8080/add-phieu-giam-gia";

const fetchDataKH = async () => {
  try {
    const response = await axios.get(`${baseURL}/data-kh`);
    customers.value = response.data;
    filteredCustomers.value = response.data; // Khởi tạo filteredCustomers
  } catch (error) {
    console.log("Error: ", error);
  }
};

const searchKH = async (query) => {
  try {
    let response;
    if (!query || query.trim() === "") {
      response = await axios.get(`${baseURL}/data-kh`);
    } else {
      response = await axios.get(`${baseURL}/search-kh`, {
        params: { keyword: query.trim() },
      });
    }
    filteredCustomers.value = response.data;
  } catch (error) {
    console.error("Lỗi search!", error);
  }
};

// Debounce search
const debouncedSearchKH = debounce((event) => {
  searchKH(searchQuery.value);
}, 300);

watch(searchQuery, (newQuery) => {
  if (newQuery.trim().length > 0) {
    debouncedSearchKH();
  } else {
    filteredCustomers.value = customers.value; // Reset khi xóa search
  }
});

const selectedCustomerDetails = computed(() => {
  return customers.value.filter((customer) =>
    selectedCustomers.value.includes(customer.id)
  );
});

const removeCustomer = (customerId) => {
  selectedCustomers.value = selectedCustomers.value.filter((id) => id !== customerId);
};

const errors = ref({});

const validateForm = () => {
  errors.value = {};

  if (!ma.value) errors.value.ma = "Mã phiếu không được để trống";
  if (!tenPhieuGiamGia.value) errors.value.tenPhieuGiamGia = "Tên phiếu không được để trống";
  if (!loaiPhieuGiamGia.value) errors.value.loaiPhieuGiamGia = "Vui lòng chọn loại phiếu";

  if (phanTramGiamGia.value < 0 || phanTramGiamGia.value > 100)
    errors.value.phanTramGiamGia = "Phần trăm giảm giá phải từ 0 đến 100";
  if (soTienGiamToiDa.value < 0) errors.value.soTienGiamToiDa = "Số tiền giảm không hợp lệ";
  if (soLuongDung.value < 1) errors.value.soLuongDung = "Số lượng phải lớn hơn 0";
  if (hoaDonToiThieu.value < 0) errors.value.hoaDonToiThieu = "Hóa đơn tối thiểu không hợp lệ";

  if (!ngayBatDau.value) errors.value.ngayBatDau = "Vui lòng chọn ngày bắt đầu";
  if (!ngayKetThuc.value) errors.value.ngayKetThuc = "Vui lòng chọn ngày kết thúc";
  if (ngayBatDau.value && ngayKetThuc.value && ngayBatDau.value > ngayKetThuc.value) {
    errors.value.ngayKetThuc = "Ngày kết thúc phải sau ngày bắt đầu";
  }

  return Object.keys(errors.value).length === 0;
};

const submitForm = async () => {
  if (!validateForm()) {
    return;
  }

  const newPgg = {
    ma: ma.value,
    tenPhieuGiamGia: tenPhieuGiamGia.value,
    loaiPhieuGiamGia: loaiPhieuGiamGia.value,
    phanTramGiamGia: phanTramGiamGia.value,
    soTienGiamToiDa: soTienGiamToiDa.value,
    hoaDonToiThieu: hoaDonToiThieu.value,
    soLuongDung: soLuongDung.value,
    ngayBatDau: ngayBatDau.value ? new Date(ngayBatDau.value).toISOString() : null,
    ngayKetThuc: ngayKetThuc.value ? new Date(ngayKetThuc.value).toISOString() : null,
    riengTu: riengTu.value ? 1 : 0,
    moTa: moTa.value,
    deleted: deleted.value,
    customerIds: riengTu.value ? selectedCustomers.value : [],
  };

  console.log("Dữ liệu gửi lên:", newPgg);

  try {
    const response = await axios.post(`${baseURL}/addPhieuGiamGia`, newPgg);
    console.log("Response: ", response);
    alert("Thêm phiếu giảm giá thành công!");
    await router.push("/phieu-giam-gia");
  } catch (error) {
    console.error("Lỗi khi thêm phiếu giảm giá:", error);
    alert("Thêm thất bại, vui lòng kiểm tra lại!");
  }
};

onMounted(fetchDataKH);
</script>

<style scoped>
.form-input {
  @apply w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500 transition;
}

.form-radio {
  @apply h-4 w-4 text-orange-500 focus:ring-orange-500;
}

.form-checkbox {
  @apply h-4 w-4 text-orange-500 focus:ring-orange-500;
}

.error {
  @apply text-red-500 text-xs mt-1;
}

table {
  @apply w-full border-collapse;
}

th, td {
  @apply text-left;
}

th {
  @apply bg-gray-100 font-semibold text-gray-700;
}

tbody tr {
  @apply transition-colors;
}

/* Đảm bảo thead cố định khi scroll */
thead tr {
  @apply sticky top-0 z-10;
}
</style>