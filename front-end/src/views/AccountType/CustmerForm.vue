<template>
  <div class="container mx-auto px-4 py-6">
    <!-- Thêm BreadcrumbWrapper -->
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
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import axios from 'axios';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue';

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
  anhKhachHang:'',
});

const provinces = ref([]);
const districts = ref([]);
const wards = ref([]);
const selectedProvince = ref('');
const selectedDistrict = ref('');
const selectedWard = ref('');

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
    alert('Kích thước ảnh không được vượt quá 5MB!');
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
    alert('Không thể tải ảnh lên: ' + (error.response?.data?.message || error.message));
    return null;
  }
}

function previewImage(event) {
  const file = event.target.files[0];
  if (file) {
    if (!file.type.startsWith('image/')) {
      alert('Vui lòng chọn file ảnh hợp lệ!');
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

async function addKhachHang() {
  if (
    !custmer.value.ten ||
    !custmer.value.sdt ||
    !custmer.value.email ||
    !custmer.value.diaChicuthe ||
    !custmer.value.ngaySinh ||
    custmer.value.gioiTinh === '' ||
    !selectedProvince.value ||
    !selectedDistrict.value ||
    !selectedWard.value
  ) {
    alert('Vui lòng điền đầy đủ tất cả các trường bắt buộc!');
    return;
  }

  const today = new Date();
  const ngaySinhDate = new Date(custmer.value.ngaySinh);
  if (ngaySinhDate > today) {
    alert('Ngày sinh không được trong tương lai!');
    return;
  }

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
    alert('Thêm khách hàng thành công!');
    router.push({
      path: '/khach-hang',
      query: { newCustomer: JSON.stringify(response.data) },
    });
  } catch (error) {
    console.error('Lỗi khi thêm khách hàng:', error);
    alert('Thêm khách hàng thất bại: ' + (error.response?.data?.message || error.message));
  }
}

onMounted(async () => {
  try {
    const response = await axios.get('https://provinces.open-api.vn/api/?depth=3');
    provinces.value = response.data;
  } catch (error) {
    console.error('Lỗi khi tải dữ liệu địa chỉ:', error);
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