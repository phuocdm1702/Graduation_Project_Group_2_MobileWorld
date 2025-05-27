<template>
  <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />
  <div class="flex h-screen p-4 bg-gray-100 gap-4">
    <div class="w-1/2 p-6 bg-white rounded-lg shadow-md">
      <ToastNotification ref="toast"/>
      <form @submit.prevent="updatePGG">
        <h3 class="text-xl font-semibold mb-4 text-gray-800">Chỉnh Sửa Phiếu Giảm Giá</h3>
        <div class="space-y-4">
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700">Mã</label>
              <input type="text" v-model="editingVoucher.ma" class="form-input" disabled/>
              <p v-if="errors.ma" class="error">{{ errors.ma }}</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Tên</label>
              <input type="text" v-model="editingVoucher.tenPhieuGiamGia" class="form-input" />
              <p v-if="errors.tenPhieuGiamGia" class="error">{{ errors.tenPhieuGiamGia }}</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Loại</label>
              <select v-model="editingVoucher.loaiPhieuGiamGia" class="form-input">
                <option value="Phần trăm">Phần trăm</option>
                <option value="Tiền mặt">Tiền mặt</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Phần trăm</label>
              <input
                type="number"
                v-model="editingVoucher.phanTramGiamGia"
                :disabled="editingVoucher.loaiPhieuGiamGia === 'Tiền mặt'"
                class="form-input"
              />
              <p v-if="errors.phanTramGiamGia" class="error">{{ errors.phanTramGiamGia }}</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Số tiền tối đa</label>
              <input
                type="text"
                v-model="formattedSoTienGiamToiDa"
                :disabled="editingVoucher.loaiPhieuGiamGia === 'Phần trăm'"
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
                  v-model="editingVoucher.soLuongDung"
                  :disabled="quantityType !== 'Nhập số lượng'"
                  class="form-input w-1/2"
                />
              </div>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Ngày bắt đầu</label>
              <input type="date" v-model="editingVoucher.ngayBatDau" class="form-input"  />
              <p v-if="errors.ngayBatDau" class="error">{{ errors.ngayBatDau }}</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Ngày kết thúc</label>
              <input type="date" v-model="editingVoucher.ngayKetThuc" class="form-input" :min="minEndDate" />
              <p v-if="errors.ngayKetThuc" class="error">{{ errors.ngayKetThuc }}</p>
            </div>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div class="flex items-center">
              <label class="block text-sm font-medium text-gray-700">Riêng tư</label>
              <ToggleSwitch :checked="editingVoucher.riengTu" @change="editingVoucher.riengTu = $event" class="ml-2" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Mô tả</label>
              <textarea v-model="editingVoucher.moTa" class="form-input resize-none" rows="2"></textarea>
            </div>
          </div>
        </div>

        <div class="flex justify-end gap-3 mt-4">
          <router-link to="/phieu-giam-gia">
            <button type="button" class="px-4 py-2 bg-gray-300 text-gray-700 rounded-md hover:bg-gray-400">Hủy</button>
          </router-link>
          <button type="submit" class="flex items-center gap-2 px-4 py-2 bg-orange-500 text-white rounded-md hover:bg-orange-600">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
              <path stroke-linecap="round" stroke-linejoin="round" d="M16.862 4.487l1.687-1.688a1.875 1.875 0 112.652 2.652L10.582 16.07a4.5 4.5 0 01-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 011.13-1.897l8.932-8.931zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0115.75 21H5.25A2.25 2.25 0 013 18.75V8.25A2.25 2.25 0 015.25 6H10" />
            </svg>
            Cập nhật
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
          :disabled="editingVoucher.riengTu"
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
                :disabled="!editingVoucher.riengTu" />
            </td>
            <td class="p-3">{{ customer.ma }}</td>
            <td class="p-3">{{ customer.ten }}</td>
            <td class="p-3">{{ customer.gioiTinh == "1" ? "Nam" : "Nữ" }}</td>
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
              <td colspan="5" class="p-3 text-center text-gray-500">Chưa có khách hàng nào được chọn</td>
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
</template>

<script setup>
import { onMounted, ref, computed, watch } from "vue";
import axios from "axios";

defineProps({
  id: {
    type: [String, Number],
    required: false
  }
});

import { useRoute, useRouter } from "vue-router";
import { debounce } from "lodash";
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue';
import ToggleSwitch from "@/components/ToggleSwitch.vue";
import ToastNotification from "@/components/ToastNotification.vue";

const route = useRoute();
const router = useRouter();
const toast = ref(null);

const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === "function") {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || ["Phiếu Giảm Giá", "Chỉnh sửa Phiếu Giảm Giá"];
});

const baseURL = "http://localhost:8080/phieu-giam-gia";

const editingVoucher = ref({
  ma: "",
  tenPhieuGiamGia: "",
  loaiPhieuGiamGia: "Phần trăm",
  phanTramGiamGia: "",
  soTienGiamToiDa: "",
  hoaDonToiThieu: "",
  soLuongDung: "",
  ngayBatDau: "",
  ngayKetThuc: "",
  riengTu: true,
  moTa: "",
});

const formattedSoTienGiamToiDa = ref("");
const formattedHoaDonToiThieu = ref("");
const quantityType = ref("Nhập số lượng");
const errors = ref({});
const loading = ref(false);
const errorMessage = ref(null);
const allCustomers = ref([]);
const selectedCustomers = ref([]);
const searchQuery = ref("");

const today = new Date();
const minDate = today.toISOString().split("T")[0];

const minEndDate = computed(() => {
  if (!editingVoucher.value.ngayBatDau) return minDate;
  const startDate = new Date(editingVoucher.value.ngayBatDau);
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
  const rawValue = event.target.value.replace(/[^0-9]/g, "");
  editingVoucher.value.soTienGiamToiDa = rawValue ? Number(rawValue) : "";
  formattedSoTienGiamToiDa.value = formatVND(editingVoucher.value.soTienGiamToiDa);
};

const updateHoaDonToiThieu = (event) => {
  const rawValue = event.target.value.replace(/[^0-9]/g, "");
  editingVoucher.value.hoaDonToiThieu = rawValue ? Number(rawValue) : "";
  formattedHoaDonToiThieu.value = formatVND(editingVoucher.value.hoaDonToiThieu);
};

watch(() => editingVoucher.value.soTienGiamToiDa, (newValue) => {
  formattedSoTienGiamToiDa.value = formatVND(newValue);
});

watch(() => editingVoucher.value.hoaDonToiThieu, (newValue) => {
  formattedHoaDonToiThieu.value = formatVND(newValue);
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
      editingVoucher.value = {
        ...voucher,
        ngayBatDau: formatDateForInput(voucher.ngayBatDau),
        ngayKetThuc: formatDateForInput(voucher.ngayKetThuc),
      };

      // Format số tiền khi tải dữ liệu
      formattedSoTienGiamToiDa.value = formatVND(voucher.soTienGiamToiDa);
      formattedHoaDonToiThieu.value = formatVND(voucher.hoaDonToiThieu);

      allCustomers.value = voucher.allCustomers || [];
      selectedCustomers.value = voucher.selectedCustomers.map(c => c.id) || [];

      // Xác định loại số lượng
      if (voucher.soLuongDung === -1) {
        quantityType.value = "Vô hạn";
      } else if (voucher.soLuongDung === selectedCustomers.value.length) {
        quantityType.value = "Dựa theo khách hàng";
      } else {
        quantityType.value = "Nhập số lượng";
      }
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
  const { ma, tenPhieuGiamGia, loaiPhieuGiamGia, phanTramGiamGia, soTienGiamToiDa, soLuongDung, hoaDonToiThieu, ngayBatDau, ngayKetThuc, riengTu } = editingVoucher.value;

  if (!ma || ma.trim() === "") errors.value.ma = "Mã phiếu không được để trống";
  if (!tenPhieuGiamGia || tenPhieuGiamGia.trim() === "") errors.value.tenPhieuGiamGia = "Tên phiếu không được để trống";
  if (!loaiPhieuGiamGia) errors.value.loaiPhieuGiamGia = "Vui lòng chọn loại phiếu";

  if (loaiPhieuGiamGia === "Phần trăm") {
    if (!phanTramGiamGia || isNaN(phanTramGiamGia) || phanTramGiamGia <= 0 || phanTramGiamGia > 100) {
      errors.value.phanTramGiamGia = "Phần trăm giảm giá phải từ 1 đến 100";
    }
  } else if (loaiPhieuGiamGia === "Tiền mặt") {
    if (!soTienGiamToiDa || isNaN(soTienGiamToiDa) || soTienGiamToiDa <= 0) {
      errors.value.soTienGiamToiDa = "Số tiền giảm phải lớn hơn 0";
    }
  }
  if (!hoaDonToiThieu || isNaN(hoaDonToiThieu) || hoaDonToiThieu < 0) {
    errors.value.hoaDonToiThieu = "Hóa đơn tối thiểu không được âm hoặc trống";
  }
  if (quantityType.value === "Nhập số lượng" && (!soLuongDung || isNaN(soLuongDung) || soLuongDung <= 0)) {
    errors.value.soLuongDung = "Số lượng phải lớn hơn 0";
  }
  if (!ngayBatDau) errors.value.ngayBatDau = "Ngày bắt đầu không được để trống";
  if (!ngayKetThuc) errors.value.ngayKetThuc = "Ngày kết thúc không được để trống";
  if (new Date(ngayBatDau) >= new Date(ngayKetThuc)) {
    errors.value.ngayKetThuc = "Ngày kết thúc phải sau ngày bắt đầu";
  }
  if (riengTu) {
    if (!selectedCustomers.value || selectedCustomers.value.length === 0) {
      errors.value.customerIds = "Vui lòng chọn ít nhất 1 khách hàng cho phiếu riêng tư";
    } else if (quantityType.value === "Nhập số lượng" && soLuongDung < selectedCustomers.value.length) {
      errors.value.soLuongDung = "Số lượng phải lớn hơn hoặc bằng số khách hàng đã chọn";
    }
  }

  return Object.keys(errors.value).length === 0;
};

const updatePGG = async () => {
  if (!validateForm()) {
    toast.value.kshowToast('warning', 'Vui lòng kiểm tra lại thông tin');
    return;
  }

  const formatDateForBackend = (dateStr) => {
    const date = new Date(dateStr);
    if (isNaN(date.getTime())) {
      console.error("Invalid date:", dateStr);
      return new Date().toISOString().split('T')[0]; // Fallback to today if invalid
    }
    return date.toISOString().split('T')[0];
  };

  const payload = {
    ma: editingVoucher.value.ma,
    tenPhieuGiamGia: editingVoucher.value.tenPhieuGiamGia,
    loaiPhieuGiamGia: editingVoucher.value.loaiPhieuGiamGia,
    phanTramGiamGia: editingVoucher.value.loaiPhieuGiamGia === "Phần trăm"
      ? Number(editingVoucher.value.phanTramGiamGia) : 0,
    soTienGiamToiDa: editingVoucher.value.loaiPhieuGiamGia === "Tiền mặt"
      ? Number(editingVoucher.value.soTienGiamToiDa) : 0,
    hoaDonToiThieu: Number(editingVoucher.value.hoaDonToiThieu),
    soLuongDung: quantityType.value === "Vô hạn"
      ? -1
      : quantityType.value === "Dựa theo khách hàng"
        ? selectedCustomers.value.length
        : Number(editingVoucher.value.soLuongDung),
    riengTu: editingVoucher.value.riengTu ? 1 : 0,
    ngayBatDau: formatDateForBackend(editingVoucher.value.ngayBatDau),
    ngayKetThuc: formatDateForBackend(editingVoucher.value.ngayKetThuc),
    moTa: editingVoucher.value.moTa || "", // Default to empty string if null
    customerIds: editingVoucher.value.riengTu ? selectedCustomers.value : []
  };

  console.log("Payload sent to backend:", JSON.stringify(payload, null, 2));

  try {
    const response = await axios.put(`${baseURL}/update-phieu-giam-gia/${route.params.id}`, payload);
    toast.value.kshowToast('success', 'Cập nhật phiếu thành công');
    setTimeout(() => {
      router.push("/phieu-giam-gia");
    }, 1500);
  } catch (error) {
    console.error("Lỗi khi cập nhật phiếu giảm giá:", error);
    console.error("Server response:", error.response?.data);
    const errorMsg = error.response?.data || 'Cập nhật thất bại, vui lòng thử lại!';
    toast.value.kshowToast('error', errorMsg);
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

const debouncedSearchKH = debounce(() => {
  searchKH(searchQuery.value);
}, 300);

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
  @apply w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500 transition;
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