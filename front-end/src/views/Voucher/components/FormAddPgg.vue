<template>
  <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />
  <div class="flex h-screen p-4 bg-gray-100 gap-4">
    <div class="w-1/2 p-6 bg-white rounded-lg shadow-md">
      <ToastNotification ref="toast"/>
      <form @submit.prevent="handleSubmit">
        <h3 class="text-xl font-semibold mb-4 text-gray-800">Thêm Phiếu Giảm Giá</h3>
        <div class="space-y-4">
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Mã</label>
              <input
                type="text"
                v-model="ma"
                class="form-input"
                :disabled="true"
              />
              <p v-if="errors.ma" class="error">{{ errors.ma }}</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên</label>
              <input type="text" v-model="tenPhieuGiamGia" class="form-input" />
              <p v-if="errors.tenPhieuGiamGia" class="error">{{ errors.tenPhieuGiamGia }}</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Loại</label>
              <select v-model="loaiPhieuGiamGia" class="form-input">
                <option value="Phần trăm">Phần trăm</option>
                <option value="Tiền mặt">Tiền mặt</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Phần trăm</label>
              <input
                type="number"
                v-model="phanTramGiamGia"
                :disabled="loaiPhieuGiamGia === 'Tiền mặt'"
                class="form-input"
              />
              <p v-if="errors.phanTramGiamGia" class="error">{{ errors.phanTramGiamGia }}</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Số tiền tối đa</label>
              <input
                type="text"
                v-model="formattedSoTienGiamToiDa"
                :disabled="loaiPhieuGiamGia === 'Phần trăm'"
                class="form-input"
                @input="updateSoTienGiamToiDa"
                placeholder="0 VND"
              />
              <p v-if="errors.soTienGiamToiDa" class="error">{{ errors.soTienGiamToiDa }}</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Hóa đơn tối thiểu</label>
              <input
                type="text"
                v-model="formattedHoaDonToiThieu"
                class="form-input"
                @input="updateHoaDonToiThieu"
                placeholder="0 VND"
              />
              <p v-if="errors.hoaDonToiThieu" class="error">{{ errors.hoaDonToiThieu }}</p>
            </div>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Số lượng</label>
              <div class="flex items-center gap-2">
                <select v-model="quantityType" class="form-input w-1/2">
                  <option value="Vô hạn">Vô hạn</option>
                  <option value="Nhập số lượng">Nhập số</option>
                  <option value="Dựa theo khách hàng">Theo KH</option>
                </select>
                <input
                  type="number"
                  v-model="soLuongDung"
                  :disabled="quantityType !== 'Nhập số lượng'"
                  class="form-input w-1/2"
                />
              </div>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Ngày bắt đầu</label>
              <input type="date" v-model="ngayBatDau" class="form-input" :min="minDate" />
              <p v-if="errors.ngayBatDau" class="error">{{ errors.ngayBatDau }}</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Ngày kết thúc</label>
              <input type="date" v-model="ngayKetThuc" class="form-input" :min="minEndDate" />
              <p v-if="errors.ngayKetThuc" class="error">{{ errors.ngayKetThuc }}</p>
            </div>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div class="flex items-center">
              <label class="block text-sm font-medium text-gray-700">Riêng tư</label>
              <ToggleSwitch :checked="riengTu" @change="riengTu = $event" class="ml-2" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Mô tả</label>
              <textarea v-model="moTa" class="form-input resize-none" rows="2"></textarea>
            </div>
          </div>
        </div>

        <div class="flex justify-end gap-3 mt-4">
          <router-link to="/phieu-giam-gia">
            <button type="button" class="px-4 py-2 bg-gray-300 text-gray-700 rounded-md hover:bg-gray-400">Hủy</button>
          </router-link>
          <button type="submit" class="flex items-center gap-2 px-4 py-2 bg-orange-500 text-white rounded-md hover:bg-orange-600">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15"/>
            </svg>
            Thêm
          </button>
        </div>
      </form>
    </div>

    <div class="w-1/2 p-6 bg-white rounded-lg shadow-md">
      <h4 class="text-xl font-semibold mb-4 text-gray-800">Chọn Khách Hàng</h4>
      <div class="mb-4">
        <input
          type="text"
          v-model="searchQuery"
          placeholder="Tìm kiếm khách hàng..."
          class="form-input w-full"
          @input="debouncedSearchKH"
          :disabled="!riengTu"
        />
      </div>

      <div class="table-container" style="max-height: 50vh; overflow-y: auto;">
        <table class="w-full text-sm">
          <thead>
          <tr class="bg-gray-100 sticky top-0">
            <th class="p-3 text-left font-semibold">Chọn</th>
            <th class="p-3 text-left font-semibold">Mã KH</th>
            <th class="p-3 text-left font-semibold">Tên Khách Hàng</th>
            <th class="p-3 text-left font-semibold">Giới tính</th>
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
                :disabled="!riengTu" />
            </td>
            <td class="p-3">{{ customer.ma }}</td>
            <td class="p-3">{{ customer.ten }}</td>
            <td class="p-3">{{ customer.gioiTinh == "0" ? "Nam" : "Nữ" }}</td>
            <td class="p-3">{{ new Date(customer.ngaySinh).toLocaleDateString('vi-VN') }}</td>
            
          </tr>
          </tbody>
        </table>
      </div>

      <div class="mt-6">
        <h4 class="text-lg font-semibold mb-2 text-gray-800">Khách Hàng Đã Chọn</h4>
        <div class="table-container" style="max-height: 20vh; overflow-y: auto;">
          <table class="w-full text-sm">
            <thead>
            <tr class="bg-gray-100 sticky top-0">
              <th class="p-3 text-left font-semibold">Mã KH</th>
              <th class="p-3 text-left font-semibold">Tên Khách Hàng</th>
              <th class="p-3 text-left font-semibold">Giới tính</th>
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
              <td class="p-3">{{ customer.gioiTinh == "1" ? "Nam" : "Nữ" }}</td>
              <td class="p-3">{{ new Date(customer.ngaySinh).toLocaleDateString('vi-VN') }}</td>
              <td class="p-3">
                <button
                  @click="removeCustomer(customer.id)"
                  class="px-2 py-1 bg-red-500 text-white rounded-md hover:bg-red-600"
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

  <ConfirmModal
    :show="showConfirmModal"
    :message="confirmMessage"
    @confirm="executeConfirmedAction"
    @cancel="closeConfirmModal"
  />
</template>

<script setup>
import { ref, onMounted, computed, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import axios from "axios";
import { debounce } from "lodash";
import BreadcrumbWrapper from "@/components/BreadcrumbWrapper.vue";
import ToggleSwitch from "@/components/ToggleSwitch.vue";
import ToastNotification from "@/components/ToastNotification.vue";
import ConfirmModal from "@/components/ConfirmModal.vue";

const toast = ref(null);
const customers = ref([]);
const selectedCustomers = ref([]);
const router = useRouter();
const route = useRoute();
const quantityType = ref("Nhập số lượng");

const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === "function") {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || ["Phiếu Giảm Giá", "Thêm Phiếu Giảm Giá"];
});

const searchQuery = ref("");
const filteredCustomers = ref([]);

const ma = ref("");
const tenPhieuGiamGia = ref("");
const loaiPhieuGiamGia = ref("Phần trăm");
const phanTramGiamGia = ref("");
const soTienGiamToiDa = ref(""); // Giá trị gốc (number)
const hoaDonToiThieu = ref(""); // Giá trị gốc (number)
const formattedSoTienGiamToiDa = ref(""); // Giá trị format (string)
const formattedHoaDonToiThieu = ref(""); // Giá trị format (string)
const soLuongDung = ref("");
const ngayBatDau = ref("");
const ngayKetThuc = ref("");
const trangThai = ref(true);
const riengTu = ref(false);
const moTa = ref("");
const deleted = ref(false);

const showConfirmModal = ref(false);
const confirmMessage = ref('');
const confirmedAction = ref(null);

const confirmAction = (message, action) => {
  confirmMessage.value = message;
  confirmedAction.value = action;
  showConfirmModal.value = true;
};

const executeConfirmedAction = () => {
  if (confirmedAction.value) {
    confirmedAction.value();
  }
  closeConfirmModal();
};

const closeConfirmModal = () => {
  showConfirmModal.value = false;
  confirmedAction.value = null;
};

// Hàm tạo mã ngẫu nhiên dạng PGG_ + 8 ký tự (chữ và số)
const generateRandomCode = () => {
  const characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  let randomCode = "PGG_";
  for (let i = 0; i < 8; i++) {
    const randomIndex = Math.floor(Math.random() * characters.length);
    randomCode += characters[randomIndex];
  }
  return randomCode;
};

const baseURL = "http://localhost:8080/add-phieu-giam-gia";

const today = new Date();
const minDate = today.toISOString().split("T")[0];

const minEndDate = computed(() => {
  if (!ngayBatDau.value) return minDate;
  const startDate = new Date(ngayBatDau.value);
  startDate.setDate(startDate.getDate() + 1);
  return startDate.toISOString().split("T")[0];
});

// Format số tiền sang định dạng VND
const formatVND = (value) => {
  if (!value || isNaN(value)) return "";
  return Number(value).toLocaleString("vi-VN") + " VND";
};

// Cập nhật giá trị gốc từ input đã format
const updateSoTienGiamToiDa = (event) => {
  const rawValue = event.target.value.replace(/[^0-9]/g, ""); // Loại bỏ ký tự không phải số
  soTienGiamToiDa.value = rawValue ? Number(rawValue) : "";
  formattedSoTienGiamToiDa.value = formatVND(soTienGiamToiDa.value);
};

const updateHoaDonToiThieu = (event) => {
  const rawValue = event.target.value.replace(/[^0-9]/g, ""); // Loại bỏ ký tự không phải số
  hoaDonToiThieu.value = rawValue ? Number(rawValue) : "";
  formattedHoaDonToiThieu.value = formatVND(hoaDonToiThieu.value);
};

watch(soTienGiamToiDa, (newValue) => {
  formattedSoTienGiamToiDa.value = formatVND(newValue);
});
watch(hoaDonToiThieu, (newValue) => {
  formattedHoaDonToiThieu.value = formatVND(newValue);
});

const fetchDataKH = async () => {
  try {
    const response = await axios.get(`${baseURL}/data-kh`);
    customers.value = response.data;
    filteredCustomers.value = response.data;
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

const debouncedSearchKH = debounce((event) => {
  searchKH(searchQuery.value);
}, 300);

watch(searchQuery, (newQuery) => {
  if (newQuery.trim().length > 0) {
    debouncedSearchKH();
  } else {
    filteredCustomers.value = customers.value;
  }
});

watch([quantityType, selectedCustomers], ([newType, newSelectedCustomers]) => {
  if (newType === "Vô hạn") {
    soLuongDung.value = -1;
  } else if (newType === "Dựa theo khách hàng") {
    soLuongDung.value = newSelectedCustomers.length;
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

  if (loaiPhieuGiamGia.value === "Phần trăm" && (phanTramGiamGia.value < 0 || phanTramGiamGia.value > 100)) {
    errors.value.phanTramGiamGia = "Phần trăm giảm giá phải từ 0 đến 100";
  }
  if (loaiPhieuGiamGia.value === "Tiền mặt" && (soTienGiamToiDa.value < 0 || soTienGiamToiDa.value === "")) {
    errors.value.soTienGiamToiDa = "Số tiền giảm không hợp lệ";
  }
  if (hoaDonToiThieu.value < 0 || hoaDonToiThieu.value === "") {
    errors.value.hoaDonToiThieu = "Hóa đơn tối thiểu không hợp lệ";
  }

  if (!ngayBatDau.value) {
    errors.value.ngayBatDau = "Vui lòng chọn ngày bắt đầu";
  } else {
    const startDate = new Date(ngayBatDau.value);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    if (startDate < today) {
      errors.value.ngayBatDau = "Ngày bắt đầu không được là ngày đã qua";
    }
  }

  if (!ngayKetThuc.value) {
    errors.value.ngayKetThuc = "Vui lòng chọn ngày kết thúc";
  } else {
    const startDate = new Date(ngayBatDau.value);
    const endDate = new Date(ngayKetThuc.value);
    if (endDate <= startDate) {
      errors.value.ngayKetThuc = "Ngày kết thúc phải sau ngày bắt đầu";
    }
    if (ngayBatDau.value && endDate < new Date(ngayBatDau.value)) {
      errors.value.ngayKetThuc = "Ngày kết thúc không được trước ngày bắt đầu";
    }
  }

  return Object.keys(errors.value).length === 0;
};

const submitForm = async () => {
  if (!validateForm()) {
    toast.value.kshowToast('warning', 'Vui lòng kiểm tra lại thông tin');
    return;
  }

  const newPgg = {
    ma: ma.value,
    tenPhieuGiamGia: tenPhieuGiamGia.value,
    loaiPhieuGiamGia: loaiPhieuGiamGia.value,
    phanTramGiamGia: phanTramGiamGia.value || null,
    soTienGiamToiDa: soTienGiamToiDa.value || null, // Gửi giá trị gốc (number)
    hoaDonToiThieu: hoaDonToiThieu.value, // Gửi giá trị gốc (number)
    soLuongDung: soLuongDung.value,
    ngayBatDau: ngayBatDau.value ? new Date(ngayBatDau.value).toISOString() : null,
    ngayKetThuc: ngayKetThuc.value ? new Date(ngayKetThuc.value).toISOString() : null,
    riengTu: riengTu.value ? 1 : 0,
    moTa: moTa.value,
    deleted: deleted.value,
    customerIds: riengTu.value ? selectedCustomers.value : [],
  };

  console.log("Dữ liệu gửi lên:", JSON.stringify(newPgg, null, 2));

  try {
    const response = await axios.post(`${baseURL}/addPhieuGiamGia`, newPgg);
    console.log("Response: ", response.data);
    toast.value.kshowToast('success', 'Add phiếu thành công');
    setTimeout(() => {
      router.push("/phieu-giam-gia");
    }, 1500);
  } catch (error) {
    console.error("Lỗi khi thêm phiếu giảm giá:", error.response?.data || error.message);
    const errorMsg = error.response?.data?.message || 'Thêm thất bại, vui lòng thử lại!';
    toast.value.kshowToast('error', errorMsg);
  }
};

const handleSubmit = () => {
  confirmAction('Bạn có chắc chắn muốn thêm phiếu giảm giá này không?', submitForm);
};

onMounted(() => {
  fetchDataKH();
  ma.value = generateRandomCode();
  if (toast.value) {
    console.log("Toast component is mounted:", toast.value);
    console.log("kshowToast method available:", !!toast.value.kshowToast);
  } else {
    console.warn("Toast component is not mounted yet");
  }
});
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

thead tr {
  @apply sticky top-0 z-10;
}
</style>