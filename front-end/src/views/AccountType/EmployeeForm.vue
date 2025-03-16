<template>
  <div class="bg-white p-6 rounded-lg shadow-lg">
    <!-- Khu vực quét QR -->
    <div v-if="isScanning" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white p-4 rounded-lg">
        <div id="qr-reader" class="w-64 h-64"></div>
        <button
          @click="stopScanning"
          class="mt-4 bg-red-500 text-white px-4 py-2 rounded-md hover:bg-red-600"
        >
          Dừng quét
        </button>
      </div>
    </div>

    <div class="grid grid-cols-2 gap-4">
      <div class="flex flex-col items-center gap-2">
        <div
          class="w-32 h-32 rounded-full border-4 border-gray-400 flex items-center justify-center cursor-pointer"
          @click="triggerFileInput"
        >
          <img
            v-if="employeeImage"
            :src="employeeImage"
            alt="Ảnh nhân viên"
            class="w-full h-full object-cover rounded-full"
          >
          <span v-else class="text-gray-400">Chọn ảnh</span>
        </div>
        <button
          v-if="employeeImage"
          @click="deleteImage"
          class="bg-red-500 text-white w-8 h-8 flex items-center justify-center rounded-full hover:bg-red-600 shadow-md"
          title="Xóa ảnh"
        >
          ✖
        </button>
        <input
          type="file"
          ref="fileInput"
          @change="previewImage"
          class="hidden"
          accept="image/*"
        >
      </div>

      <div class="w-full mt-4">
        <label class="block mb-2">Tên Nhân Viên</label>
        <input
          type="text"
          v-model="employee.tenNhanVien"
          class="w-full px-3 py-2 border rounded-md"
          placeholder="Nhập tên nhân viên"
        >
      </div>

      <div>
        <label class="block mb-2">UserName</label>
        <input type="text" v-model="employee.userName" class="w-full px-3 py-2 border rounded-md" placeholder="Nhập UserName">
      </div>

      <div>
        <label class="block mb-2">SDT</label>
        <input type="text" v-model="employee.sdt" class="w-full px-3 py-2 border rounded-md" placeholder="Nhập SDT">
      </div>

      <div>
        <label class="block mb-2">CCCD</label>
        <div class="flex items-center gap-2">
          <input
            type="text"
            v-model="employee.cccd"
            class="flex-1 px-3 py-2 border rounded-md"
            placeholder="Nhập CCCD"
          >
          <button
            @click="startScanning"
            class="bg-orange-500 text-white px-3 py-2 rounded-md hover:bg-orange-600 flex items-center justify-center"
            title="Quét mã QR"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M3 3h4v4H3z"></path>
              <path d="M17 3h4v4h-4z"></path>
              <path d="M3 17h4v4H3z"></path>
              <path d="M17 17h4v4h-4z"></path>
              <path d="M7 7h4v4H7z"></path>
              <path d="M7 17h4"></path>
              <path d="M7 13h8v8"></path>
              <path d="M17 7h-4v4"></path>
            </svg>
          </button>
        </div>
      </div>

      <div>
        <label class="block mb-2">Email</label>
        <input type="text" v-model="employee.email" class="w-full px-3 py-2 border rounded-md" placeholder="Nhập Email">
      </div>

      <div class="col-span-2 grid grid-cols-3 gap-4">
        <div>
          <label class="block mb-2">Địa chỉ cụ thể</label>
          <input type="text" v-model="employee.diaChicuthe" class="w-full px-3 py-2 border rounded-md" placeholder="Nhập địa chỉ cụ thể">
        </div>

        <div>
          <label class="block mb-2">Ngày sinh</label>
          <input type="date" v-model="employee.ngaySinh" class="w-full px-3 py-2 border rounded-md">
        </div>

        <div>
          <label class="block mb-2">Giới Tính</label>
          <select v-model="employee.gioiTinh" class="w-full px-3 py-2 border rounded-md">
            <option value="">---Chọn Giới Tính---</option>
            <option value="False">Nam</option>
            <option value="True">Nữ</option>
          </select>
        </div>
      </div>

      <div class="flex gap-4 col-span-2">
        <div class="w-1/3">
          <label class="block mb-2">Tỉnh/Thành phố</label>
          <select v-model="selectedProvince" @change="handleProvinceChange" class="w-full px-3 py-2 border rounded-md">
            <option value="" disabled>Chọn tỉnh/thành phố</option>
            <option v-for="province in provinces" :key="province.code">{{ province.name }}</option>
          </select>
        </div>

        <div class="w-1/3">
          <label class="block mb-2">Quận/Huyện</label>
          <select v-model="selectedDistrict" @change="handleDistrictChange" class="w-full px-3 py-2 border rounded-md">
            <option value="" disabled>Chọn quận/huyện</option>
            <option v-for="district in districts" :key="district.code">{{ district.name }}</option>
          </select>
        </div>

        <div class="w-1/3">
          <label class="block mb-2">Xã/Phường</label>
          <select v-model="selectedWard" class="w-full px-3 py-2 border rounded-md">
            <option value="" disabled>Chọn xã/phường</option>
            <option v-for="ward in wards" :key="ward.code">{{ ward.name }}</option>
          </select>
        </div>
      </div>
    </div>

    <div class="flex justify-end space-x-4 mt-6">
      <router-link to="/nhan-vien">
        <button @click="$emit('cancel')" class="px-4 py-2 bg-gray-300 rounded-md hover:bg-gray-400 transition-colors">
          Hủy
        </button>
      </router-link>

      <button
        type="submit"
        @click="addNhanVien"
        class="px-4 py-2 bg-orange-500 text-white rounded-md hover:bg-orange-600 transition-colors flex items-center"
      >
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5 mr-2">
          <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15"/>
        </svg>
        <span class="font-bold">Thêm nhân viên</span>
      </button>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, nextTick } from 'vue';
import axios from 'axios';
import { Html5Qrcode } from 'html5-qrcode';
import { useRouter } from 'vue-router';

const router = useRouter();
const qrReader = ref(null);
const isScanning = ref(false);

// Dữ liệu nhân viên
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

// Dữ liệu ảnh
const employeeImage = ref(null);
const fileInput = ref(null);
const uploadedImageUrl = ref(null);

// Quét mã QR
const startScanning = async () => {
  isScanning.value = true;
  await nextTick();
  qrReader.value = new Html5Qrcode('qr-reader');

  qrReader.value
    .start(
      { facingMode: 'environment' },
      {
        fps: 15,
        qrbox: 250,
      },
      (decodedText) => {
        handleQRData(decodedText);
        stopScanning();
      },
    )
    .catch((err) => {
      console.error('Lỗi khởi động quét QR:', err);
    });
};

const stopScanning = () => {
  if (qrReader.value) {
    qrReader.value
      .stop()
      .then(() => {
        qrReader.value = null;
        isScanning.value = false;
      })
      .catch((err) => {
        console.error('Lỗi dừng quét:', err);
      });
  }
};

const handleQRData = (data) => {
  console.log('Dữ liệu QR thô:', data);
  const fields = data.split('|');
  console.log('Các trường phân tích:', fields);

  if (fields.length >= 6) {
    employee.value = {
      ...employee.value,
      cccd: fields[0].trim(),
      tenNhanVien: fields[2].trim() || 'Không xác định',
      ngaySinh: isValidDateFormat(fields[3].trim()) ? formatDate(fields[3].trim()) : '',
      gioiTinh: fields[4].trim() === 'Nam' ? 'False' : 'True',
      diaChicuthe: fields[5].trim(),
    };
    parseAddress(fields[5].trim());
  } else {
    employee.value = {
      ...employee.value,
      cccd: data.trim(),
    };
  }
};

const isValidDateFormat = (dateStr) => {
  if (dateStr.length !== 8 || !/^\d{8}$/.test(dateStr)) {
    return false;
  }
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

const parseAddress = (address) => {
  const parts = address.split(', ').reverse();
  if (parts.length >= 3) {
    selectedProvince.value = parts[0].trim();
    handleProvinceChange();
    selectedDistrict.value = parts[1].trim();
    handleDistrictChange();
    selectedWard.value = parts[2].trim();
  }
};

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

    console.log('Response từ server:', response.data);

    const imageUrl = response.data.imageUrl;
    if (!imageUrl) {
      throw new Error('Không nhận được URL ảnh từ server. Response: ' + JSON.stringify(response.data));
    }

    uploadedImageUrl.value = imageUrl;
    return imageUrl;
  } catch (error) {
    console.error('Lỗi upload ảnh:', error);
    const errorMessage = error.response
      ? `Server lỗi: ${error.response.status} - ${error.response.data.message || error.message}`
      : error.message;
    alert('Không thể tải ảnh lên: ' + errorMessage);
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
      employeeImage.value = e.target.result;
    };
    reader.readAsDataURL(file);
  }
}

function deleteImage() {
  employeeImage.value = null;
  uploadedImageUrl.value = null;
  fileInput.value.value = '';
}

function triggerFileInput() {
  fileInput.value.click();
}

async function addNhanVien() {
  // Validate cơ bản
  if (!employee.value.tenNhanVien || !employee.value.userName || !employee.value.sdt ||
    !employee.value.cccd || !employee.value.email || !employee.value.diaChicuthe ||
    !employee.value.ngaySinh || !employee.value.gioiTinh) {
    alert('Vui lòng điền đầy đủ thông tin bắt buộc!');
    return;
  }

  let imagePath = uploadedImageUrl.value;
  if (fileInput.value?.files[0] && !imagePath) {
    imagePath = await uploadImage(fileInput.value.files[0]);
    if (!imagePath) {
      return;
    }
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
    createdAt: new Date().toISOString()
  };

  console.log('Dữ liệu gửi lên server:', employeeData);

  try {
    const response = await axios.post('http://localhost:8080/nhan-vien/add', employeeData, {
      headers: { 'Content-Type': 'application/json' },
    });
    console.log('Phản hồi từ server:', response.data);
    alert('Thêm nhân viên thành công!');

    router.push({
      path: '/nhan-vien',
      query: { newEmployee: JSON.stringify(response.data) }
    });
  } catch (error) {
    console.error('Lỗi khi thêm nhân viên:', error.response ? error.response.data : error.message);
    alert('Thêm nhân viên thất bại: ' + (error.response?.data?.message || error.message));
  }
}

// Tải dữ liệu tỉnh/thành phố
onMounted(async () => {
  try {
    const response = await axios.get('https://provinces.open-api.vn/api/?depth=3');
    provinces.value = response.data;
  } catch (error) {
    console.error('Lỗi khi tải dữ liệu:', error);
  }
});

const handleProvinceChange = () => {
  const province = provinces.value.find((prov) => prov.name === selectedProvince.value);
  districts.value = province ? province.districts : [];
  selectedDistrict.value = '';
  selectedWard.value = '';
};

const handleDistrictChange = () => {
  const district = districts.value.find((dist) => dist.name === selectedDistrict.value);
  wards.value = district ? district.wards : [];
  selectedWard.value = '';
};
</script>