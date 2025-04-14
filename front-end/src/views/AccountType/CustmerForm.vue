<template>
  <div class="container mx-auto px-4 py-6">
    <ToastNotification ref="toastRef" />
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />

    <div class="bg-white p-6 rounded-lg shadow-lg">
      <h2 class="text-2xl font-bold mb-6">Thông tin khách hàng</h2>

      <!-- Phần ảnh được căn giữa -->
      <div class="flex justify-center mb-6">
        <div class="employee-image-container relative">
          <div
            class="w-36 h-36 rounded-full border-4 border-gray-300 flex items-center justify-center cursor-pointer hover:border-gray-400 transition-colors"
            @click="triggerFileInput"
          >
            <img
              v-if="customerImage"
              :src="customerImage"
              alt="Ảnh khách hàng"
              class="w-full h-full object-cover rounded-full"
            >
            <span v-else class="text-gray-500 font-medium">Chọn ảnh</span>
          </div>
          <button
            v-if="customerImage"
            @click="deleteImage"
            class="absolute top-0 right-0 bg-red-500 text-white w-6 h-6 flex items-center justify-center rounded-full hover:bg-red-600 transition-colors transform translate-x-2 -translate-y-2"
          >
            ✕
          </button>
          <input
            type="file"
            ref="fileInput"
            @change="previewImage"
            class="hidden"
            accept="image/*"
          >
        </div>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label class="block mb-1 text-sm font-medium">Tên khách hàng</label>
          <input
            type="text"
            v-model="custmer.ten"
            class="w-full px-3 py-2 border rounded-md focus:ring-2 focus:ring-orange-500 focus:border-transparent"
            placeholder="Nhập tên khách hàng"
            required
          >
        </div>
        <div>
          <label class="block mb-1 text-sm font-medium">Số điện thoại</label>
          <input
            v-model="custmer.sdt"
            type="text"
            class="w-full px-3 py-2 border rounded-md focus:ring-2 focus:ring-orange-500 focus:border-transparent"
            placeholder="Nhập SDT"
            required
          >
        </div>
      </div>
      <div>
        <label class="block mb-1 text-sm font-medium">Email</label>
        <input
          v-model="custmer.email"
          type="email"
          class="w-full px-3 py-2 border rounded-md focus:ring-2 focus:ring-orange-500 focus:border-transparent"
          placeholder="Nhập Email"
          required
        >
      </div>
      <!-- Địa chỉ -->
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mt-4">
        <div>
          <label class="block mb-1 text-sm font-medium">Tỉnh/Thành phố</label>
          <select
            v-model="selectedProvince"
            @change="handleProvinceChange"
            class="w-full px-3 py-2 border rounded-md focus:ring-2 focus:ring-orange-500 focus:border-transparent"
            required
          >
            <option value="" disabled>Chọn tỉnh/thành phố</option>
            <option v-for="province in provinces" :key="province.code" :value="province.name">{{ province.name }}</option>
          </select>
        </div>
        <div>
          <label class="block mb-1 text-sm font-medium">Quận/Huyện</label>
          <select
            v-model="selectedDistrict"
            @change="handleDistrictChange"
            class="w-full px-3 py-2 border rounded-md focus:ring-2 focus:ring-orange-500 focus:border-transparent"
            required
          >
            <option value="" disabled>Chọn quận/huyện</option>
            <option v-for="district in districts" :key="district.code" :value="district.name">{{ district.name }}</option>
          </select>
        </div>
        <div>
          <label class="block mb-1 text-sm font-medium">Xã/Phường</label>
          <select
            v-model="selectedWard"
            class="w-full px-3 py-2 border rounded-md focus:ring-2 focus:ring-orange-500 focus:border-transparent"
            required
          >
            <option value="" disabled>Chọn xã/phường</option>
            <option v-for="ward in wards" :key="ward.code" :value="ward.name">{{ ward.name }}</option>
          </select>
        </div>
      </div>

      <div class="mt-4">
        <label class="block mb-1 text-sm font-medium">Địa chỉ cụ thể</label>
        <input
          v-model="custmer.diaChicuthe"
          type="text"
          class="w-full px-3 py-2 border rounded-md focus:ring-2 focus:ring-orange-500 focus:border-transparent"
          placeholder="Nhập địa chỉ cụ thể"
          required
        >
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4">
        <div>
          <label class="block mb-1 text-sm font-medium">Ngày sinh</label>
          <input
            v-model="custmer.ngaySinh"
            type="date"
            class="w-full px-3 py-2 border rounded-md focus:ring-2 focus:ring-orange-500 focus:border-transparent"
            required
          >
        </div>
        <div>
          <label class="block mb-2 text-sm font-medium text-gray-700">Giới tính</label>
          <div class="flex items-center gap-6">
            <label class="flex items-center space-x-2 cursor-pointer">
              <input
                v-model="custmer.gioiTinh"
                value="false"
                type="radio"
                class="form-radio h-5 w-5 text-orange-500 focus:ring-2 focus:ring-orange-500 focus:ring-opacity-50 transition duration-150 ease-in-out"
              >
              <span class="text-sm text-gray-700">Nam</span>
            </label>
            <label class="flex items-center space-x-2 cursor-pointer">
              <input
                v-model="custmer.gioiTinh"
                value="true"
                type="radio"
                class="form-radio h-5 w-5 text-orange-500 focus:ring-2 focus:ring-orange-500 focus:ring-opacity-50 transition duration-150 ease-in-out"
              >
              <span class="text-sm text-gray-700">Nữ</span>
            </label>
          </div>
        </div>
      </div>

      <div class="flex justify-end space-x-4 mt-6">
        <router-link to="/khach-hang">
          <button class="px-6 py-2 bg-gray-300 rounded-md hover:bg-gray-400 transition-colors">
            Hủy
          </button>
        </router-link>
        <button
          @click="addKhachHang"
          class="px-6 py-2 bg-orange-500 text-white rounded-md hover:bg-orange-600 transition-colors flex items-center font-bold"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15"/>
          </svg>
          Thêm khách hàng
        </button>
      </div>
    </div>
  </div>

  <ConfirmModal
    :show="showConfirmModal"
    :message="'Bạn có chắc chắn muốn thêm khách hàng không?'"
    @confirm="confirmAddKhachHang"
    @cancel="showConfirmModal = false"
  />
</template>

<script setup>
import { onMounted, ref, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import axios from 'axios';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue';
import ToastNotification from "@/components/ToastNotification.vue";
import ConfirmModal from "@/components/ConfirmModal.vue";

const showConfirmModal = ref(false);
const router = useRouter();
const route = useRoute();

// Dữ liệu khách hàng
const custmer = ref({
  ten: '',
  userName: '',
  sdt: '',
  email: '',
  diaChicuthe: '',
  ngaySinh: '',
  gioiTinh: '',
  anhKhachHang: '',
});

const provinces = ref([]);
const districts = ref([]);
const wards = ref([]);
const selectedProvince = ref('');
const selectedDistrict = ref('');
const selectedWard = ref('');
const toastRef = ref(null);

// Dữ liệu ảnh
const customerImage = ref(null);
const fileInput = ref(null);
const uploadedImageUrl = ref(null);

const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === 'function') {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || ['Khách hàng', 'Thêm khách hàng'];
});

// Xử lý ảnh
async function uploadImage(file) {
  if (!file) return null;

  const maxSize = 5 * 1024 * 1024; // 5MB
  if (file.size > maxSize) {
    toastRef.value.kshowToast('error', 'Kích thước ảnh không được vượt quá 5MB!');
    return null;
  }

  const formData = new FormData();
  formData.append('file', file);

  try {
    const response = await axios.post('http://localhost:8080/img/api/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    const imageUrl = response.data.imageUrl;
    if (!imageUrl) throw new Error('Không nhận được URL ảnh từ server');
    uploadedImageUrl.value = imageUrl;
    return imageUrl;
  } catch (error) {
    console.error('Lỗi upload ảnh:', error);
    toastRef.value.kshowToast('error', 'Không thể tải ảnh lên: ' + (error.response?.data?.message || error.message));
    return null;
  }
}

function previewImage(event) {
  const file = event.target.files[0];
  if (file) {
    if (!file.type.startsWith('image/')) {
      toastRef.value.kshowToast('error', 'Vui lòng chọn file ảnh hợp lệ!');
      fileInput.value.value = '';
      return;
    }
    const reader = new FileReader();
    reader.onload = (e) => {
      customerImage.value = e.target.result;
    };
    reader.readAsDataURL(file);
  }
}

function deleteImage() {
  customerImage.value = null;
  uploadedImageUrl.value = null;
  fileInput.value.value = '';
}

function triggerFileInput() {
  fileInput.value.click();
}

function isOver15(birthDate) {
  const date = new Date(birthDate);
  const today = new Date();
  const age = today.getFullYear() - date.getFullYear();
  const isBirthdayPassed = today.getMonth() > date.getMonth() || (today.getMonth() === date.getMonth() && today.getDate() >= date.getDate());
  return age > 15 || (age === 15 && isBirthdayPassed);
}

function CheckAdd() {
  const OnlyNumbers = /^\d+$/;
  const OnlyABC = /^[^\d]+$/;
  const EmailCheck = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  const PhoneCheck = /^(03|05|07|08|09)\d{8}$/;

  if (!custmer.value.ten.trim()) {
    toastRef.value.kshowToast('error', 'Vui lòng điền tên khách hàng!');
    return false;
  }
  if (!OnlyABC.test(custmer.value.ten.trim())) {
    toastRef.value.kshowToast('error', 'Tên khách hàng không được chứa số!');
    return false;
  }
  if (!custmer.value.sdt.trim()) {
    toastRef.value.kshowToast('error', 'Vui lòng điền số điện thoại!');
    return false;
  }
  if (!OnlyNumbers.test(custmer.value.sdt.trim())) {
    toastRef.value.kshowToast('error', 'Số điện thoại chỉ được chứa số!');
    return false;
  }
  if (!PhoneCheck.test(custmer.value.sdt.trim())) {
    toastRef.value.kshowToast('error', 'Số điện thoại phải đúng định dạng Việt Nam!');
    return false;
  }
  if (!custmer.value.email.trim()) {
    toastRef.value.kshowToast('error', 'Vui lòng điền email!');
    return false;
  }
  if (!EmailCheck.test(custmer.value.email.trim())) {
    toastRef.value.kshowToast('error', 'Vui lòng điền email đúng định dạng');
    return false;
  }
  if (!custmer.value.gioiTinh) {
    toastRef.value.kshowToast('error', 'Vui lòng chọn giới tính (Nam/Nữ)!');
    return false;
  }
  if (!custmer.value.ngaySinh.trim()) {
    toastRef.value.kshowToast('error', 'Vui lòng chọn ngày sinh!');
    return false;
  }
  if (!isOver15(custmer.value.ngaySinh.trim())) {
    toastRef.value.kshowToast('error', 'Khách hàng dưới 15 tuổi không thể tạo tài khoản!');
    return false;
  }
  if (!selectedProvince.value.trim()) {
    toastRef.value.kshowToast('error', 'Vui lòng chọn Tỉnh/Thành phố!');
    return false;
  }
  if (!selectedDistrict.value.trim()) {
    toastRef.value.kshowToast('error', 'Vui lòng chọn Quận/Huyện!');
    return false;
  }
  if (!selectedWard.value.trim()) {
    toastRef.value.kshowToast('error', 'Vui lòng chọn Xã/Phường!');
    return false;
  }
  if (!custmer.value.diaChicuthe.trim()) {
    toastRef.value.kshowToast('error', 'Vui lòng điền địa chỉ cụ thể!');
    return false;
  }

  return true;
}

async function addKhachHang() {
  if (!CheckAdd()) return;
  showConfirmModal.value = true; // Hiển thị modal xác nhận
}

async function confirmAddKhachHang() {
  let imagePath = uploadedImageUrl.value;
  if (fileInput.value?.files[0] && !imagePath) {
    imagePath = await uploadImage(fileInput.value.files[0]);
    if (!imagePath) return;
  }

  const customerData = {
    tenKH: custmer.value.ten,
    email: custmer.value.email,
    soDienThoai: custmer.value.sdt,
    userName: custmer.value.userName,
    ngaySinh: custmer.value.ngaySinh,
    diaChiCuThe: custmer.value.diaChicuthe,
    thanhPho: selectedProvince.value,
    quan: selectedDistrict.value,
    phuong: selectedWard.value,
    gioiTinh: custmer.value.gioiTinh === 'true',
    anhKhachHang: imagePath,
  };

  try {
    const response = await axios.post('http://localhost:8080/khach-hang/add', customerData, {
      headers: { 'Content-Type': 'application/json' },
    });
    showConfirmModal.value = false;
    router.push({
      path: '/khach-hang',
      query: { newCustomer: JSON.stringify(response.data), status: 'success' },
    });
  } catch (error) {
    showConfirmModal.value = false;
    const errorMessage = error.response?.data.split(": ")[1] || error.response?.data || error.message;
    console.log('Lỗi từ server:', error.response); // Debug
    if (errorMessage.includes('Email đã được sử dụng')) {
      toastRef.value?.kshowToast('error', 'Email đã được sử dụng!');
    } else if (errorMessage.includes('SDT đã được sử dụng')) {
      toastRef.value?.kshowToast('error', 'SDT đã được sử dụng!');
    } else {
      toastRef.value?.kshowToast('error', 'Thêm nhân viên thất bại: ' + errorMessage);
    }
  }
}

onMounted(async () => {
  try {
    const response = await axios.get('https://provinces.open-api.vn/api/?depth=3', {
      withCredentials: false // Thêm dòng này để tránh lỗi CORS
    });
    provinces.value = response.data;
  } catch (error) {
    console.error('Lỗi khi tải dữ liệu địa chỉ:', error);
    toastRef.value.kshowToast('error', 'Không thể tải danh sách tỉnh/thành phố: ' + (error.response?.data?.error || error.message));
  }
});

const handleProvinceChange = () => {
  const province = provinces.value.find((prov) => prov.name === selectedProvince.value);
  districts.value = province ? province.districts : [];
  selectedDistrict.value = '';
  selectedWard.value = '';
  wards.value = [];
};

const handleDistrictChange = () => {
  const district = districts.value.find((dist) => dist.name === selectedDistrict.value);
  wards.value = district ? district.wards : [];
  selectedWard.value = '';
};
</script>

<style scoped>
.employee-image-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.form-radio {
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  border: 2px solid #d1d5db;
  border-radius: 50%;
  outline: none;
  cursor: pointer;
  transition: all 0.2s;
}

.form-radio:checked {
  background-color: #f97316;
  border-color: #f97316;
}

.form-radio:hover {
  border-color: #f59e0b;
}

.form-radio:focus {
  box-shadow: 0 0 0 3px rgba(249, 115, 22, 0.3);
}

label {
  display: flex;
  align-items: center;
}
</style>