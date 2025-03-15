<template>
  <div class="bg-white p-4 rounded-lg">
    <h2 class="text-2xl font-bold mb-4">Thông tin khách hàng</h2>

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
        >
      </div>

      <div class="w-full mt-4">
        <label class="block mb-2">Tên Khách Hàng</label>
        <input
          type="text"
          v-model="custmer.ten"
          class="w-full px-3 py-2 border rounded-md"
          placeholder="Nhập tên khách hàng"
          required
        >
      </div>

      <div>
        <label class="block mb-2">UserName</label>
        <input
          v-model="custmer.userName"
          type="text"
          class="w-full px-3 py-2 border rounded-md"
          placeholder="Nhập UserName"
          required
        >
      </div>

      <div>
        <label class="block mb-2">SDT</label>
        <input
          v-model="custmer.sdt"
          type="text"
          class="w-full px-3 py-2 border rounded-md"
          placeholder="Nhập SDT"
          required
        >
      </div>

      <div>
        <label class="block mb-2">CCCD</label>
        <div class="flex items-center gap-2">
          <input
            v-model="custmer.cccd"
            type="text"
            class="flex-1 px-3 py-2 border rounded-md"
            placeholder="Nhập CCCD (12 số)"
            maxlength="12"
            required
          >
          <button
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
        <input
          v-model="custmer.email"
          type="email"
          class="w-full px-3 py-2 border rounded-md"
          placeholder="Nhập Email"
          required
        >
      </div>

      <div class="col-span-2 grid grid-cols-3 gap-4">
        <div>
          <label class="block mb-2">Địa chỉ cụ thể</label>
          <input
            v-model="custmer.diaChicuthe"
            type="text"
            class="w-full px-3 py-2 border rounded-md"
            placeholder="Nhập địa chỉ cụ thể"
            required
          >
        </div>

        <div>
          <label class="block mb-2">Ngày sinh</label>
          <input
            v-model="custmer.ngaySinh"
            type="date"
            class="w-full px-3 py-2 border rounded-md"
            required
          >
        </div>

        <div>
          <label class="block mb-2">Giới Tính</label>
          <select
            v-model="custmer.gioiTinh"
            class="w-full px-3 py-2 border rounded-md"
            required
          >
            <option value="" disabled>Chọn giới tính</option>
            <option value="false">Nam</option>
            <option value="true">Nữ</option>
          </select>
        </div>
      </div>

      <div class="flex gap-4 col-span-2">
        <div class="w-1/3">
          <label class="block mb-2">Tỉnh/Thành phố</label>
          <select
            v-model="selectedProvince"
            @change="handleProvinceChange"
            class="w-full px-3 py-2 border rounded-md"
            required
          >
            <option value="" disabled>Chọn tỉnh/thành phố</option>
            <option v-for="province in provinces" :key="province.code" :value="province.name">{{ province.name }}</option>
          </select>
        </div>

        <div class="w-1/3">
          <label class="block mb-2">Quận/Huyện</label>
          <select
            v-model="selectedDistrict"
            @change="handleDistrictChange"
            class="w-full px-3 py-2 border rounded-md"
            required
          >
            <option value="" disabled>Chọn quận/huyện</option>
            <option v-for="district in districts" :key="district.code" :value="district.name">{{ district.name }}</option>
          </select>
        </div>

        <div class="w-1/3">
          <label class="block mb-2">Xã/Phường</label>
          <select
            v-model="selectedWard"
            class="w-full px-3 py-2 border rounded-md"
            required
          >
            <option value="" disabled>Chọn xã/phường</option>
            <option v-for="ward in wards" :key="ward.code" :value="ward.name">{{ ward.name }}</option>
          </select>
        </div>
      </div>
    </div>

    <div class="flex justify-end space-x-4 mt-4">
      <router-link to="/backKH">
        <button @click="$emit('cancel')" class="px-4 py-2 bg-gray-300 rounded-md">Hủy</button>
      </router-link>
      <button @click="addKhachHang" class="px-4 py-2 bg-orange-500 text-white rounded-md">Lưu</button>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();

async function addKhachHang() {
  // Kiểm tra các trường bắt buộc
  if (!custmer.value.ten || !custmer.value.userName || !custmer.value.sdt ||
    !custmer.value.cccd || !custmer.value.email || !custmer.value.diaChicuthe ||
    !custmer.value.ngaySinh || custmer.value.gioiTinh === '' ||
    !selectedProvince.value || !selectedDistrict.value || !selectedWard.value) {
    alert('Vui lòng điền đầy đủ tất cả các trường bắt buộc!');
    return;
  }

  // Kiểm tra CCCD phải đủ 12 chữ số
  if (custmer.value.cccd.length !== 12) {
    alert('CCCD phải có đúng 12 chữ số!');
    return;
  }

  // Kiểm tra ngày sinh không được trong tương lai
  const today = new Date();
  const ngaySinhDate = new Date(custmer.value.ngaySinh);
  if (ngaySinhDate > today) {
    alert('Ngày sinh không được trong tương lai!');
    return;
  }

  const employeeData = {
    tenKH: custmer.value.ten,
    email: custmer.value.email,
    soDienThoai: custmer.value.sdt,
    userName: custmer.value.userName,
    cccd: custmer.value.cccd,
    ngaySinh: custmer.value.ngaySinh,
    diaChiCuThe: custmer.value.diaChicuthe,
    thanhPho: selectedProvince.value,
    quan: selectedDistrict.value,
    phuong: selectedWard.value,
    gioiTinh: custmer.value.gioiTinh === 'true' // Boolean true/false
  };

  console.log('Dữ liệu gửi lên:', employeeData);

  try {
    const response = await axios.post('http://localhost:8080/khach-hang/add', employeeData);
    console.log('Thêm khách hàng thành công:', response.data);
    router.push({ path: '/khach-hang' });
  } catch (error) {
    console.error('Lỗi khi thêm khách hàng:', error.message);
    console.log('Chi tiết lỗi từ server:', error.response?.data);
    alert('Lỗi khi thêm khách hàng: ' + (error.response?.data?.message || error.message));
  }
}

const custmer = ref({
  ten: '',
  cccd: '',
  userName: '',
  sdt: '',
  email: '',
  diaChicuthe: '',
  ngaySinh: '',
  gioiTinh: ''
});

const employeeImage = ref(null);
const fileInput = ref(null);

function triggerFileInput() {
  fileInput.value.click();
}

function previewImage(event) {
  const file = event.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      employeeImage.value = e.target.result;
    };
    reader.readAsDataURL(file);
  }
}

function deleteImage() {
  employeeImage.value = null;
  fileInput.value.value = '';
}

const provinces = ref([]);
const districts = ref([]);
const wards = ref([]);
const selectedProvince = ref('');
const selectedDistrict = ref('');
const selectedWard = ref('');

onMounted(async () => {
  try {
    const response = await axios.get('https://provinces.open-api.vn/api/?depth=3');
    provinces.value = response.data;
  } catch (error) {
    console.error('Lỗi khi tải dữ liệu địa chỉ:', error);
  }
});

const handleProvinceChange = () => {
  const province = provinces.value.find(prov => prov.name === selectedProvince.value);
  districts.value = province ? province.districts : [];
  selectedDistrict.value = '';
  selectedWard.value = '';
};

const handleDistrictChange = () => {
  const district = districts.value.find(dist => dist.name === selectedDistrict.value);
  wards.value = district ? district.wards : [];
  selectedWard.value = '';
};
</script>