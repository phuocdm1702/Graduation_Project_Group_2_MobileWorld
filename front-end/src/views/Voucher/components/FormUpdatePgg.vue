<template>
  <!-- Thêm BreadcrumbWrapper -->
  <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />
  <div class="flex h-screen p-4 bg-gray-100 gap-4">
    <!-- Form Container -->
    <div class="w-2/3 p-6 bg-white rounded-lg shadow-md">
      <h3 class="text-xl font-semibold mb-4 text-gray-800">Chỉnh sửa Phiếu Giảm Giá</h3>

      <!-- Hiển thị trạng thái loading -->
      <div v-if="loading" class="text-center text-gray-500">
        Đang tải dữ liệu...
      </div>

      <!-- Hiển thị thông báo lỗi -->
      <div v-if="errorMessage" class="text-red-500 text-center mb-4">
        {{ errorMessage }}
      </div>

      <!-- Chỉ hiển thị form nếu không có lỗi và không đang tải -->
      <div v-if="!loading && !errorMessage">
        <div class="space-y-6">
          <!-- Discount Details -->
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Mã</label>
              <input type="text" v-model="editingVoucher.ma" class="form-input" />
              <p v-if="errors.ma" class="error">{{ errors.ma }}</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên Phiếu Giảm Giá</label>
              <input type="text" v-model="editingVoucher.tenPhieuGiamGia" class="form-input" />
              <p v-if="errors.tenPhieuGiamGia" class="error">{{ errors.tenPhieuGiamGia }}</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Loại Phiếu</label>
              <select v-model="editingVoucher.loaiPhieuGiamGia" class="form-input">
                <option value="Phần trăm">Phần trăm</option>
                <option value="Tiền mặt">Tiền mặt</option>
              </select>
              <p v-if="errors.loaiPhieuGiamGia" class="error">{{ errors.loaiPhieuGiamGia }}</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Phần trăm giảm giá</label>
              <input
                type="number"
                v-model.number="editingVoucher.phanTramGiamGia"
                :disabled="editingVoucher.loaiPhieuGiamGia === 'Tiền mặt'"
                class="form-input"
              />
              <p v-if="errors.phanTramGiamGia" class="error">{{ errors.phanTramGiamGia }}</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Số tiền giảm tối đa</label>
              <input
                type="number"
                v-model.number="editingVoucher.soTienGiamToiDa"
                :disabled="editingVoucher.loaiPhieuGiamGia === 'Phần trăm'"
                class="form-input"
              />
              <p v-if="errors.soTienGiamToiDa" class="error">{{ errors.soTienGiamToiDa }}</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Hóa đơn tối thiểu</label>
              <input type="number" v-model.number="editingVoucher.hoaDonToiThieu" class="form-input" />
              <p v-if="errors.hoaDonToiThieu" class="error">{{ errors.hoaDonToiThieu }}</p>
            </div>
          </div>

          <!-- Date Range and Usage -->
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Số lượng sử dụng</label>
              <div class="flex items-center gap-2">
                <select v-model="quantityType" class="form-input w-1/2">
                  <option value="Vô hạn">Vô hạn</option>
                  <option value="Nhập số lượng">Nhập số lượng</option>
                  <option value="Dựa theo khách hàng">Dựa theo khách hàng</option>
                </select>
                <input
                  type="number"
                  v-model.number="editingVoucher.soLuongDung"
                  :disabled="quantityType !== 'Nhập số lượng'"
                  class="form-input w-1/2"
                />
              </div>
              <p v-if="errors.soLuongDung" class="error">{{ errors.soLuongDung }}</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Ngày bắt đầu</label>
              <input type="date" v-model="editingVoucher.ngayBatDau" class="form-input" :min="minDate" />
              <p v-if="errors.ngayBatDau" class="error">{{ errors.ngayBatDau }}</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Ngày kết thúc</label>
              <input type="date" v-model="editingVoucher.ngayKetThuc" class="form-input" :min="minEndDate" />
              <p v-if="errors.ngayKetThuc" class="error">{{ errors.ngayKetThuc }}</p>
            </div>
          </div>

          <!-- Privacy and Description -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div class="flex items-center">
                <label class="block text-sm font-medium text-gray-700">Riêng tư</label>
                <ToggleSwitch
                  :checked="editingVoucher.riengTu"
                  @change="editingVoucher.riengTu = $event"
                  :id="'riengTu-toggle'"
                  class="ml-2"
                />
              </div>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Mô tả</label>
              <textarea v-model="editingVoucher.moTa" class="form-input resize-none" rows="3"></textarea>
            </div>
          </div>
        </div>

        <!-- Buttons -->
        <div class="flex justify-end gap-3 mt-6">
          <router-link to="/phieu-giam-gia">
            <button type="button" class="px-4 py-2 bg-gray-300 text-gray-700 rounded-md hover:bg-gray-400 transition">
              Hủy
            </button>
          </router-link>
          <button @click="updatePGG" class="flex items-center gap-2 px-4 py-2 bg-orange-500 text-white rounded-md hover:bg-orange-600 transition">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
              <path stroke-linecap="round" stroke-linejoin="round" d="M16.862 4.487l1.687-1.688a1.875 1.875 0 112.652 2.652L10.582 16.07a4.5 4.5 0 01-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 011.13-1.897l8.932-8.931zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0115.75 21H5.25A2.25 2.25 0 013 18.75V8.25A2.25 2.25 0 015.25 6H10" />
            </svg>
            Cập nhật
          </button>
        </div>
      </div>
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
          :disabled="!editingVoucher.riengTu"
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
              <input
                type="checkbox"
                v-model="selectedCustomers"
                :value="customer.id"
                class="form-checkbox"
                :disabled="!editingVoucher.riengTu"
              />
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
import { onMounted, ref, computed, watch } from "vue";
import axios from "axios";
import { useRoute, useRouter } from "vue-router";
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue';

const route = useRoute();
const router = useRouter();

const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === "function") {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || ["Phiếu Giảm Giá", "Chỉnh sửa Phiếu Giảm Giá"];
});

const baseURL = "http://localhost:8080/phieu-giam-gia";

const editingVoucher = ref({});
const errors = ref({});
const loading = ref(false);
const errorMessage = ref(null);
const allCustomers = ref([]);
const selectedCustomers = ref([]);
const searchQuery = ref("");
const quantityType = ref("Nhập số lượng");

const minDate = computed(() => {
  const today = new Date();
  return today.toISOString().split("T")[0];
});

const minEndDate = computed(() => {
  return editingVoucher.value.ngayBatDau || minDate.value;
});

const formatDateForInput = (dateString) => {
  const date = new Date(dateString);
  if (isNaN(date.getTime())) {
    console.error("Invalid date:", dateString);
    return "";
  }
  return date.toISOString().split("T")[0];
};

const getDataUpdateId = async (id) => {
  if (!id || isNaN(parseInt(id))) {
    console.error("ID không hợp lệ:", id);
    errorMessage.value = "ID không hợp lệ. Vui lòng kiểm tra lại.";
    loading.value = false;
    return;
  }

  loading.value = true;
  errorMessage.value = null;

  try {
    const response = await axios.get(`${baseURL}/${id}`);
    const voucher = response.data;

    if (voucher && voucher.id) {
      console.log("Voucher found:", voucher);
      editingVoucher.value = {
        ...voucher,
        ngayBatDau: formatDateForInput(voucher.ngayBatDau),
        ngayKetThuc: formatDateForInput(voucher.ngayKetThuc),
      };
      allCustomers.value = voucher.allCustomers || [];
      selectedCustomers.value = voucher.selectedCustomers.map(c => c.id) || [];
    } else {
      console.error("Không tìm thấy phiếu giảm giá với id:", id);
      errorMessage.value = "Không tìm thấy phiếu giảm giá.";
    }
  } catch (error) {
    console.error("Lỗi khi lấy dữ liệu phiếu:", error);
    if (error.response) {
      if (error.response.status === 400) errorMessage.value = "ID không hợp lệ!";
      else if (error.response.status === 404) errorMessage.value = "Phiếu giảm giá không tồn tại!";
      else errorMessage.value = "Lỗi server. Vui lòng thử lại sau!";
    } else {
      errorMessage.value = "Lỗi kết nối. Vui lòng kiểm tra mạng!";
    }
  } finally {
    loading.value = false;
  }
};

const validateForm = () => {
  errors.value = {};
  const { ma, tenPhieuGiamGia, loaiPhieuGiamGia, phanTramGiamGia, soTienGiamToiDa, soLuongDung, hoaDonToiThieu, ngayBatDau, ngayKetThuc } = editingVoucher.value;

  if (!ma) errors.value.ma = "Mã phiếu không được để trống";
  if (!tenPhieuGiamGia) errors.value.tenPhieuGiamGia = "Tên phiếu không được để trống";
  if (!loaiPhieuGiamGia) errors.value.loaiPhieuGiamGia = "Vui lòng chọn loại phiếu";
  if (phanTramGiamGia < 0 || phanTramGiamGia > 100) errors.value.phanTramGiamGia = "Phần trăm giảm giá phải từ 0 đến 100";
  if (soTienGiamToiDa < 0) errors.value.soTienGiamToiDa = "Số tiền giảm không hợp lệ";
  if (soLuongDung < -1) errors.value.soLuongDung = "Số lượng phải >= -1";
  if (hoaDonToiThieu < 0) errors.value.hoaDonToiThieu = "Hóa đơn tối thiểu không hợp lệ";
  if (!ngayBatDau) errors.value.ngayBatDau = "Vui lòng chọn ngày bắt đầu";
  if (!ngayKetThuc) errors.value.ngayKetThuc = "Vui lòng chọn ngày kết thúc";
  if (ngayBatDau && ngayKetThuc && ngayBatDau > ngayKetThuc) {
    errors.value.ngayKetThuc = "Ngày kết thúc phải sau ngày bắt đầu";
  }

  return Object.keys(errors.value).length === 0;
};

const updatePGG = async () => {
  if (!validateForm()) {
    return;
  }
  const formatDateForBackend = (dateStr) => {
    const date = new Date(dateStr);
    return date.toISOString().split('T')[0];
  };

  const payload = {
    ...editingVoucher.value,
    ngayBatDau: formatDateForBackend(editingVoucher.value.ngayBatDau),
    ngayKetThuc: formatDateForBackend(editingVoucher.value.ngayKetThuc),
    customerIds: selectedCustomers.value
  };
  try {
    await axios.put(`${baseURL}/update-phieu-giam-gia/${route.params.id}`, payload);
    setTimeout(() => {
      router.push("/phieu-giam-gia");
    }, 1500);
  } catch (error) {
    console.log("Cập nhật thất bại!", error);
    if (error.response) {
      if (error.response.status === 400) errorMessage.value = "Dữ liệu không hợp lệ!";
      else if (error.response.status === 404) errorMessage.value = "Phiếu giảm giá không tồn tại!";
      else errorMessage.value = "Lỗi server. Vui lòng thử lại sau!";
    } else {
      errorMessage.value = "Lỗi kết nối. Vui lòng kiểm tra mạng!";
    }
  }
};

const filteredCustomers = computed(() => {
  if (!searchQuery.value) return allCustomers.value;
  return allCustomers.value.filter(customer =>
    customer.ma.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
    customer.ten.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

const selectedCustomerDetails = computed(() => {
  return allCustomers.value.filter(customer => selectedCustomers.value.includes(customer.id));
});

const removeCustomer = (customerId) => {
  selectedCustomers.value = selectedCustomers.value.filter((id) => id !== customerId);
};

watch(quantityType, (newVal) => {
  if (newVal === "Vô hạn") {
    editingVoucher.value.soLuongDung = -1;
  } else if (newVal === "Dựa theo khách hàng") {
    editingVoucher.value.soLuongDung = selectedCustomers.value.length;
  }
});

onMounted(() => {
  const id = route.params.id;
  console.log("Route params id:", id);
  if (id) {
    getDataUpdateId(id);
  } else {
    errorMessage.value = "Không tìm thấy ID phiếu giảm giá.";
    loading.value = false;
  }
});
</script>

<style scoped>
.form-input {
  @apply w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500 transition text-sm;
}

.error {
  @apply text-red-500 text-xs mt-1;
}

button {
  @apply transition-colors duration-200;
}
</style>