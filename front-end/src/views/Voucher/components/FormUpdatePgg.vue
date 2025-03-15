<template>
  <div class="min-h-screen bg-gray-100 p-6">
    <div class="bg-white p-8 rounded-lg shadow-lg w-full max-w-2xl mx-auto">
      <h3 class="text-2xl font-semibold mb-6 text-gray-800">Chỉnh sửa Phiếu Giảm Giá</h3>

      <div class="space-y-6">
        <!-- Voucher Code -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Mã phiếu</label>
          <input v-model="editingVoucher.ma" type="text" class="form-input w-full" />
          <p v-if="errors.ma" class="error">{{ errors.ma }}</p>
        </div>

        <!-- Voucher Name -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Tên phiếu</label>
          <input v-model="editingVoucher.tenPhieuGiamGia" type="text" class="form-input w-full" />
          <p v-if="errors.tenPhieuGiamGia" class="error">{{ errors.tenPhieuGiamGia }}</p>
        </div>

        <!-- Voucher Type -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Loại phiếu</label>
          <select v-model="editingVoucher.loaiPhieuGiamGia" class="form-input w-full">
            <option value="Phần trăm">Phần trăm</option>
            <option value="Tiền mặt">Tiền mặt</option>
          </select>
          <p v-if="errors.loaiPhieuGiamGia" class="error">{{ errors.loaiPhieuGiamGia }}</p>
        </div>

        <!-- Discount Cash -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Số tiền giảm tối đa</label>
          <input v-model.number="editingVoucher.soTienGiamToiDa" type="number" class="form-input w-full" />
          <p v-if="errors.soTienGiamToiDa" class="error">{{ errors.soTienGiamToiDa }}</p>
        </div>

        <!-- Discount Percentage -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Phần trăm giảm giá (%)</label>
          <input v-model.number="editingVoucher.phanTramGiamGia" type="number" class="form-input w-full" />
          <p v-if="errors.phanTramGiamGia" class="error">{{ errors.phanTramGiamGia }}</p>
        </div>

        <!-- Start Date -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Ngày bắt đầu</label>
          <input v-model="editingVoucher.ngayBatDau" type="date" class="form-input w-full" />
          <p v-if="errors.ngayBatDau" class="error">{{ errors.ngayBatDau }}</p>
        </div>

        <!-- End Date -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Ngày kết thúc</label>
          <input v-model="editingVoucher.ngayKetThuc" type="date" class="form-input w-full" />
          <p v-if="errors.ngayKetThuc" class="error">{{ errors.ngayKetThuc }}</p>
        </div>
      </div>

      <div class="flex justify-end gap-3 mt-6">
        <router-link to="/phieu-giam-gia">
          <button class="px-4 py-2 bg-gray-300 text-gray-700 rounded-md hover:bg-gray-400 transition text-sm">
            Hủy
          </button>
        </router-link>
        <button @click="updatePGG" class="px-4 py-2 bg-orange-500 text-white rounded-md hover:bg-orange-600 transition text-sm flex items-center gap-2">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
            <path stroke-linecap="round" stroke-linejoin="round" d="M16.862 4.487l1.687-1.688a1.875 1.875 0 112.652 2.652L10.582 16.07a4.5 4.5 0 01-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 011.13-1.897l8.932-8.931zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0115.75 21H5.25A2.25 2.25 0 013 18.75V8.25A2.25 2.25 0 015.25 6H10" />
          </svg>
          Cập nhật
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import {useRoute, useRouter} from "vue-router";

const route = useRoute();
const router = useRouter();

const baseURL = "http://localhost:8080/phieu-giam-gia";

const editingVoucher = ref({});
const errors = ref({});

const formatDateForInput = (dateString) => {
  const date = new Date(dateString);
  if (isNaN(date.getTime())) {
    console.error("Invalid date:", dateString);
    return "";
  }
  return date.toISOString().split("T")[0]; // Trả về YYYY-MM-DD
};

const getDataUpdateId = async (id) => {
  try {
    const response = await axios.get(`${baseURL}/data`);
    const voucher = response.data.find(v => v.id === parseInt(id));
    if(voucher) {
      editingVoucher.value = {
        ...voucher,
        ngayBatDau: formatDateForInput(voucher.ngayBatDau),
        ngayKetThuc: formatDateForInput(voucher.ngayKetThuc),
      };
    } else {
      console.error("Không tìm thấy phiếu giảm giá!");
      router.push("/phieu-giam-gia");
    }
  } catch(error) {
    console.error("Lỗi khi lấy dữ liệu phiếu:", error);
    router.push('/phieu-giam-gia');
  }
}

const emit = defineEmits(['close']);

const validateForm = () => {
  errors.value = {}; // Reset lỗi

  const { ma, tenPhieuGiamGia, loaiPhieuGiamGia, phanTramGiamGia, soTienGiamToiDa, soLuongDung, hoaDonToiThieu, ngayBatDau, ngayKetThuc } = editingVoucher.value;

  if (!ma) errors.value.ma = "Mã phiếu không được để trống";
  if (!tenPhieuGiamGia) errors.value.tenPhieuGiamGia = "Tên phiếu không được để trống";
  if (!loaiPhieuGiamGia) errors.value.loaiPhieuGiamGia = "Vui lòng chọn loại phiếu";

  if (phanTramGiamGia < 0 || phanTramGiamGia > 100) errors.value.phanTramGiamGia = "Phần trăm giảm giá phải từ 0 đến 100";
  if (soTienGiamToiDa < 0) errors.value.soTienGiamToiDa = "Số tiền giảm không hợp lệ";
  if (soLuongDung < 1) errors.value.soLuongDung = "Số lượng phải lớn hơn 0";
  if (hoaDonToiThieu < 0) errors.value.hoaDonToiThieu = "Hóa đơn tối thiểu không hợp lệ";

  if (!ngayBatDau) errors.value.ngayBatDau = "Vui lòng chọn ngày bắt đầu";
  if (!ngayKetThuc) errors.value.ngayKetThuc = "Vui lòng chọn ngày kết thúc";
  if (ngayBatDau && ngayKetThuc && ngayBatDau > ngayKetThuc) {
    errors.value.ngayKetThuc = "Ngày kết thúc phải sau ngày bắt đầu";
  }

  return Object.keys(errors.value).length === 0;
};


const updatePGG = async () => {
  if(!validateForm()) {
    return;
  }
  try {
    await axios.put(`${baseURL}/update-phieu-giam-gia/${editingVoucher.value.id}`, editingVoucher.value);
    router.push("/phieu-giam-gia");
  } catch (error) {
    console.log("Cập nhật thất bại!", error);
  }
};


onMounted(() => {
  const id = route.params.id;
  if(id) {
    getDataUpdateId(id);
  }
})
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