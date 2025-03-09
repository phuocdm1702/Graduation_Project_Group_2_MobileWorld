<template>
  <div class="bg-white p-6 rounded-lg shadow-lg w-full max-w-screen-xl mx-auto h-screen">
    <h2 class="text-2xl font-bold mb-4">Thông tin nhân viên</h2>

    <div class="grid grid-cols-2 gap-4">
      <div class="flex flex-col items-center gap-2">
        <div
          class="w-32 h-32 rounded-full border-4 border-green-500 flex items-center justify-center cursor-pointer"
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
        <input type="text" v-model="employee.userName" class="w-full px-3 py-2 border rounded-md" placeholder="Nhập UserNames">
      </div>

      <div>
        <label class="block mb-2">SDT</label>
        <input type="text" v-model="employee.sdt" class="w-full px-3 py-2 border rounded-md" placeholder="Nhập SDT">
      </div>

      <div>
        <label class="block mb-2">CCCD</label>
        <input type="text" v-model="employee.cccd" class="w-full px-3 py-2 border rounded-md" placeholder="Nhập CCCD">
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
          <label class="block mb-2">Giới tính</label>
          <div class="flex items-center gap-4">
            <label class="flex items-center gap-2">
              <input type="radio" v-model="employee.gioiTinh" value="True" class="form-radio">
              Nam
            </label>

            <label class="flex items-center gap-2">
              <input type="radio" v-model="employee.gioiTinh" value="False" class="form-radio">
              Nữ
            </label>
          </div>
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

    <div class="flex justify-end space-x-4 mt-4">
      <router-link to="/back">
        <button @click="$emit('cancel')" class="px-4 py-2 bg-gray-300 rounded-md">Hủy</button>
      </router-link>
      <button type="submit" @click="addNhanVien()" class="px-4 py-2 bg-green-600 text-white rounded-md">Lưu</button>
    </div>
  </div>

</template>

<script setup>
import {onMounted, ref} from 'vue';
import axios from "axios";


// /add
async function addNhanVien() {
  const employeeData = {
    ma: employee.value.ma,
    tenNhanVien: employee.value.tenNhanVien,
    ngaySinh: employee.value.ngaySinh,
    anhNhanVien: employeeImage.value,
    thanhPho: selectedProvince.value,
    quan: selectedDistrict.value,
    phuong: selectedWard.value,
    diaChiCuThe: employee.value.diaChicuthe,
    cccd: employee.value.cccd,

    // Thông tin tài khoản mới
    email: employee.value.email,
    sdt: employee.value.sdt,
    userName: employee.value.userName
  };

  try {
    await axios.post('http://localhost:8080/nhan-vien/add', employeeData);
    alert('Thêm nhân viên thành công!');
  } catch (error) {
    console.error('Lỗi khi thêm nhân viên:', error.response ? error.response.data : error);
    alert('Thêm nhân viên thất bại.');
  }
}

const employee = ref({
  tenNhanVien: '',
  cccd:'',
  userName: '',
  sdt: '',
  email: '',
  diaChicuthe: '',
  ngaySinh: '',
  gioiTinh: '',
  
  
});

//anh
function deleteImage() {
  employeeImage.value = null;
  fileInput.value.value = ''; // Reset giá trị input file
}
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
//
function saveEmployee() {
  console.log('Thông tin nhân viên:', employee.value);
}

//diaChi
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
    console.error('Lỗi khi tải dữ liệu:', error);
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



//
</script>
