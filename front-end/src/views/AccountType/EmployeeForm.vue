<template>
  <div class="container mx-auto px-4 py-6">
    <ToastNotification ref="toastRef" />
    <!-- BreadcrumbWrapper -->
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />

    <div class="bg-white p-6 rounded-lg shadow-lg">
      <!-- Khu vực quét QR -->
      <div v-if="isScanning" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white p-6 rounded-lg shadow-md">
          <div id="qr-reader" class="w-72 h-72 mx-auto"></div>
          <button
            @click="stopScanning"
            class="mt-4 bg-red-500 text-white px-4 py-2 rounded-md hover:bg-red-600 w-full"
          >
            Dừng quét
          </button>
        </div>
      </div>

      <!-- Phần ảnh được căn giữa -->
      <div class="flex justify-center mb-6">
        <div class="employee-image-container relative">
          <div
            class="w-36 h-36 rounded-full border-4 border-gray-300 flex items-center justify-center cursor-pointer hover:border-gray-400 transition-colors"
            @click="triggerFileInput"
          >
            <img
              v-if="employeeImage"
              :src="employeeImage"
              alt="Ảnh nhân viên"
              class="w-full h-full object-cover rounded-full"
            >
            <span v-else class="text-gray-500 font-medium">Chọn ảnh</span>
          </div>
          <button
            v-if="employeeImage"
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

      <!-- Cột thông tin chính -->
      <div class="md:col-span-2 space-y-4">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label class="block mb-1 text-sm font-medium">Tên nhân viên</label>
            <input
              type="text"
              v-model="employee.tenNhanVien"
              class="w-full px-3 py-2 border rounded-md focus:ring-2 focus:ring-orange-500 focus:border-transparent"
              placeholder="Nhập tên nhân viên"
            >
          </div>
          <div>
            <label class="block mb-1 text-sm font-medium">Số điện thoại</label>
            <input
              type="text"
              v-model="employee.sdt"
              class="w-full px-3 py-2 border rounded-md focus:ring-2 focus:ring-orange-500 focus:border-transparent"
              placeholder="Nhập SDT"
            >
          </div>
          <div>
            <label class="block mb-1 text-sm font-medium">CCCD</label>
            <div class="flex gap-2">
              <input
                type="text"
                v-model="employee.cccd"
                class="flex-1 px-3 py-2 border rounded-md focus:ring-2 focus:ring-orange-500 focus:border-transparent"
                placeholder="Nhập CCCD"
              >
              <button
                @click="startScanning"
                class="bg-orange-500 text-white p-2 rounded-md hover:bg-orange-600 transition-colors"
                title="Quét mã QR"
              >
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M3 3h4v4H3zM17 3h4v4h-4zM3 17h4v4H3zM17 17h4v4h-4zM7 7h4v4H7zM7 17h4M7 13h8v8M17 7h-4v4"/>
                </svg>
              </button>
            </div>
          </div>
          <div>
            <label class="block mb-1 text-sm font-medium">Email</label>
            <input
              type="text"
              v-model="employee.email"
              class="w-full px-3 py-2 border rounded-md focus:ring-2 focus:ring-orange-500 focus:border-transparent"
              placeholder="Nhập Email"
            >
          </div>
        </div>

        <!-- Địa chỉ -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label class="block mb-1 text-sm font-medium">Tỉnh/Thành phố</label>
            <select
              v-model="selectedProvince"
              @change="handleProvinceChange"
              class="w-full px-3 py-2 border rounded-md focus:ring-2 focus:ring-orange-500 focus:border-transparent"
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
            >
              <option value="" disabled>Chọn xã/phường</option>
              <option v-for="ward in wards" :key="ward.code" :value="ward.name">{{ ward.name }}</option>
            </select>
          </div>
        </div>

        <div>
          <label class="block mb-1 text-sm font-medium">Địa chỉ cụ thể</label>
          <input
            type="text"
            v-model="employee.diaChicuthe"
            class="w-full px-3 py-2 border rounded-md focus:ring-2 focus:ring-orange-500 focus:border-transparent"
            placeholder="Nhập địa chỉ cụ thể"
          >
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label class="block mb-1 text-sm font-medium">Ngày sinh</label>
            <input
              type="date"
              v-model="employee.ngaySinh"
              class="w-full px-3 py-2 border rounded-md focus:ring-2 focus:ring-orange-500 focus:border-transparent"
            >
          </div>
          <div>
            <label class="block mb-2 text-sm font-medium text-gray-700">Giới tính</label>
            <div class="flex items-center gap-6">
              <label class="flex items-center space-x-2 cursor-pointer">
                <input
                  v-model="employee.gioiTinh"
                  value="false"
                  type="radio"
                  class="form-radio h-5 w-5 text-orange-500 focus:ring-2 focus:ring-orange-500 focus:ring-opacity-50 transition duration-150 ease-in-out"
                >
                <span class="text-sm text-gray-700">Nam</span>
              </label>
              <label class="flex items-center space-x-2 cursor-pointer">
                <input
                  v-model="employee.gioiTinh"
                  value="true"
                  type="radio"
                  class="form-radio h-5 w-5 text-orange-500 focus:ring-2 focus:ring-orange-500 focus:ring-opacity-50 transition duration-150 ease-in-out"
                >
                <span class="text-sm text-gray-700">Nữ</span>
              </label>
            </div>
          </div>
        </div>
      </div>

      <!-- Nút điều khiển -->
      <div class="flex justify-end gap-4 mt-6">
        <router-link to="/nhan-vien">
          <button
            class="px-6 py-2 bg-gray-300 rounded-md hover:bg-gray-400 transition-colors"
          >
            Hủy
          </button>
        </router-link>
        <button
          @click="addNhanVien"
          class="px-6 py-2 bg-orange-500 text-white rounded-md hover:bg-orange-600 transition-colors flex items-center font-bold"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15"/>
          </svg>
          Thêm nhân viên
        </button>
      </div>
    </div>
  </div>

  <ConfirmModal
    :show="showConfirmModal"
    :message="'Bạn có chắc chắn muốn thêm nhân viên không?'"
    @confirm="confirmAddNhanVien"
    @cancel="showConfirmModal = false"
  />
</template>

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

<script setup>
import { onMounted, ref, nextTick } from 'vue';
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import { Html5Qrcode } from 'html5-qrcode';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue';
import ToastNotification from '@/components/ToastNotification.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';

const showConfirmModal = ref(false);
const router = useRouter();
const route = useRoute();
const qrReader = ref(null);
const isScanning = ref(false);
const toastRef = ref(null);

const employee = ref({
  tenNhanVien: '',
  cccd: '',
  sdt: '',
  email: '',
  diaChicuthe: '',
  ngaySinh: '',
  gioiTinh: '',
  userName: '',
});

const provinces = ref([]);
const districts = ref([]);
const wards = ref([]);
const selectedProvince = ref('');
const selectedDistrict = ref('');
const selectedWard = ref('');

const employeeImage = ref(null);
const fileInput = ref(null);
const uploadedImageUrl = ref(null);

const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === 'function') {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || ['Nhân viên', 'Thêm nhân viên'];
});

const startScanning = async () => {
  isScanning.value = true;
  await nextTick();
  qrReader.value = new Html5Qrcode('qr-reader');
  qrReader.value
    .start(
      { facingMode: 'environment' },
      { fps: 15, qrbox: 250 },
      (decodedText) => {
        handleQRData(decodedText);
        stopScanning();
      },
    )
    .catch((err) => console.error('Lỗi khởi động quét QR:', err));
};

const stopScanning = () => {
  if (qrReader.value) {
    qrReader.value
      .stop()
      .then(() => {
        qrReader.value = null;
        isScanning.value = false;
      })
      .catch((err) => console.error('Lỗi dừng quét:', err));
  }
};

const handleQRData = async (data) => {
  const fields = data.split('|');
  if (fields.length >= 6) {
    employee.value = {
      ...employee.value,
      cccd: fields[0].trim(),
      tenNhanVien: fields[2].trim() || 'Không xác định',
      ngaySinh: isValidDateFormat(fields[3].trim()) ? formatDate(fields[3].trim()) : '',
      gioiTinh: data.idTaiKhoan?.deleted !== undefined ? String(!data.idTaiKhoan.deleted) : "",
      diaChicuthe: fields[5].trim(),
    };
    await parseAddress(fields[5].trim());
  } else {
    employee.value = { ...employee.value, cccd: data.trim() };
  }
};

const isValidDateFormat = (dateStr) => {
  if (dateStr.length !== 8 || !/^\d{8}$/.test(dateStr)) return false;
  const day = parseInt(dateStr.slice(0, 2));
  const month = parseInt(dateStr.slice(2, 4));
  const year = parseInt(dateStr.slice(4, 8));
  const date = new Date(year, month - 1, day);
  return date.getDate() === day && date.getMonth() + 1 === month && date.getFullYear() === year;
};

const formatDate = (dateStr) => {
  if (dateStr.length === 8) {
    const day = dateStr.slice(0, 2);
    const month = dateStr.slice(2, 4);
    const year = dateStr.slice(4, 8);
    return `${year}-${month}-${day}`;
  }
  return '';
};

const provinceMapping = { 'Hà Nội': 'Thành phố Hà Nội', 'TP. Hồ Chí Minh': 'Thành phố Hồ Chí Minh' };
const wardMapping = { 'Trâu Quỳ': 'Thị trấn Trâu Quỳ' };

const normalizeProvinceName = (provinceName) => provinceMapping[provinceName] || provinceName;
const normalizeWardName = (wardName) => wardMapping[wardName] || wardName;
const normalizeName = (name) => name.replace(/^(Huyện|Quận|Phường|Xã|Thị trấn)\s+/i, '').trim();

const parseAddress = async (address) => {
  const parts = address.split(', ').reverse();
  if (parts.length >= 3) {
    let provinceName = normalizeProvinceName(parts[0].trim());
    let districtName = parts[1].trim();
    let wardName = normalizeWardName(parts[2].trim());
    if (parts.length > 3) employee.value.diaChicuthe = parts.slice(3).reverse().join(', ').trim();

    const province = provinces.value.find((prov) => prov.name === provinceName);
    if (!province) {
      alert('Không tìm thấy tỉnh/thành phố trong dữ liệu: ' + provinceName);
      return;
    }
    selectedProvince.value = province.name;
    handleProvinceChange();

    await nextTick();
    const district = districts.value.find((dist) => normalizeName(dist.name) === normalizeName(districtName));
    if (!district) {
      alert('Không tìm thấy quận/huyện trong dữ liệu: ' + districtName);
      return;
    }
    selectedDistrict.value = district.name;
    handleDistrictChange();

    await nextTick();
    const ward = wards.value.find((w) => normalizeName(w.name) === normalizeName(wardName));
    if (!ward) {
      toastRef.value?.kshowToast('error', 'Không tìm thấy phường/xã trong dữ liệu: ' + wardName);
      return;
    }
    selectedWard.value = ward.name;
  } else {
    toastRef.value?.kshowToast('error', 'Địa chỉ từ QR không đủ thông tin: ' + address);
  }
};

async function uploadImage(file) {
  if (!file) return null;
  const maxSize = 5 * 1024 * 1024;
  if (file.size > maxSize) {
    toastRef.value?.kshowToast('error', 'Kích thước ảnh không được vượt quá 5MB!');
    return null;
  }

  const formData = new FormData();
  formData.append('file', file);

  try {
    const response = await axios.post('http://localhost:8080/img/api/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    const imageUrl = response.data.imageUrl;
    if (!imageUrl) throw new Error('Không nhận được URL ảnh từ server.');
    uploadedImageUrl.value = imageUrl;
    return imageUrl;
  } catch (error) {
    toastRef.value?.kshowToast('error', 'Không thể tải ảnh lên: ' + (error.response?.data?.message || error.message));
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
    reader.onload = (e) => (employeeImage.value = e.target.result);
    reader.readAsDataURL(file);
  }
}

function deleteImage() {
  employeeImage.value = null;
  uploadedImageUrl.value = null;
  fileInput.value.value = '';
}

function triggerFileInput() {
  fileInput.value?.click();
}

function isOver18(birthDate) {
  const date = new Date(birthDate);
  const today = new Date();
  const age = today.getFullYear() - date.getFullYear();
  const isBirthdayPassed = today.getMonth() > date.getMonth() || (today.getMonth() === date.getMonth() && today.getDate() >= date.getDate());
  return age > 18 || (age === 18 && isBirthdayPassed);
}

function dieukienADD() {
  const OnlyABC = /^[^\d]+$/;
  const OnlyNumbers = /^\d+$/;
  const EmailCheck = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  const PhoneCheck = /^(03|05|07|08|09)\d{8}$/;

  if (!employee.value.tenNhanVien?.trim()) return toastRef.value?.kshowToast('error', 'Vui lòng điền tên nhân viên!'), false;
  if (!employee.value.diaChicuthe?.trim()) return toastRef.value?.kshowToast('error', 'Vui lòng điền địa chỉ!'), false;
  if (!employee.value.cccd?.trim()) return toastRef.value?.kshowToast('error', 'Vui lòng điền CCCD!'), false;
  if (employee.value.cccd?.trim().length !== 12) return toastRef.value?.kshowToast('error', 'CCCD phải có đúng 12 số!'), false;
  if (!employee.value.sdt?.trim()) return toastRef.value?.kshowToast('error', 'Vui lòng điền số điện thoại!'), false;
  if (!employee.value.email?.trim()) return toastRef.value?.kshowToast('error', 'Vui lòng điền email!'), false;
  if (!employee.value.ngaySinh?.trim()) return toastRef.value?.kshowToast('error', 'Vui lòng chọn ngày sinh!'), false;
  if (!employee.value.gioiTinh?.trim()) return toastRef.value?.kshowToast('error', 'Vui lòng chọn giới tính!'), false;
  if (!selectedProvince.value.trim()) return toastRef.value?.kshowToast('error', 'Vui lòng chọn Tỉnh/Thành phố!'), false;
  if (!selectedDistrict.value.trim()) return toastRef.value?.kshowToast('error', 'Vui lòng chọn Quận/Huyện!'), false;
  if (!selectedWard.value.trim()) return toastRef.value?.kshowToast('error', 'Vui lòng chọn Xã/Phường!'), false;
  if (!OnlyNumbers.test(employee.value.cccd?.trim())) return toastRef.value?.kshowToast('error', 'CCCD chỉ được chứa số!'), false;
  if (!OnlyNumbers.test(employee.value.sdt?.trim())) return toastRef.value?.kshowToast('error', 'Số điện thoại chỉ được chứa số!'), false;
  if (!PhoneCheck.test(employee.value.sdt?.trim())) return toastRef.value?.kshowToast('error', 'Số điện thoại không hợp lệ!'), false;
  if (!EmailCheck.test(employee.value.email?.trim())) return toastRef.value?.kshowToast('error', 'Email không đúng định dạng!'), false;
  if (!isOver18(employee.value.ngaySinh?.trim())) return toastRef.value?.kshowToast('error', 'Nhân viên phải >= 18 tuổi!'), false;
  if (!OnlyABC.test(employee.value.tenNhanVien?.trim())) return toastRef.value?.kshowToast('error', 'Tên nhân viên không điền số!'), false;
  if (new Date(employee.value.ngaySinh?.trim()) > new Date()) return toastRef.value?.kshowToast('error', 'Ngày sinh không được lớn hơn ngày hiện tại!'), false;

  return true;
}

async function addNhanVien() {
  if (!dieukienADD()) return;
  showConfirmModal.value = true;
}

async function confirmAddNhanVien() {
  let imagePath = uploadedImageUrl.value;
  if (fileInput.value?.files[0] && !imagePath) {
    imagePath = await uploadImage(fileInput.value.files[0]);
    if (!imagePath) return;
  }

  const employeeData = {
    ma: employee.value.ma || '',
    tenNhanVien: employee.value.tenNhanVien,
    ngaySinh: employee.value.ngaySinh,
    anhNhanVien: imagePath,
    thanhPho: selectedProvince.value,
    quan: selectedDistrict.value,
    phuong: selectedWard.value,
    diaChiCuThe: employee.value.diaChicuthe,
    cccd: employee.value.cccd,
    email: employee.value.email,
    soDienThoai: employee.value.sdt,
    tenDangNhap: employee.value.userName,
    gioiTinh: employee.value.gioiTinh === 'True',
    createdAt: new Date().toISOString(),
  };

  try {
    const response = await axios.post('http://localhost:8080/nhan-vien/add', employeeData, {
      headers: { 'Content-Type': 'application/json' },
    });
    showConfirmModal.value = false;

    setTimeout(() => {
      router.push({
        path: '/nhan-vien',
        query: { newEmployee: JSON.stringify(response.data), status: 'success' },
      });
    }, 100);
  } catch (error) {
    showConfirmModal.value = false;
    const errorMessage = error.response?.data.split(": ")[1]  || error.response?.data || error.message;
    console.log('Lỗi từ server:', error.response); // Debug
    if (errorMessage.includes('Email đã được sử dụng')) {
      toastRef.value?.kshowToast('error', 'Email đã được sử dụng!');
    }
    else if (errorMessage.includes('SDT đã được sử dụng')) {
      toastRef.value?.kshowToast('error', 'SDT đã được sử dụng!');
    }
    else {
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