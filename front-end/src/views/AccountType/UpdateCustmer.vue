<template>
  <div class="bg-gray-100 min-h-screen w-full flex items-start justify-center p-0">
    <div class="bg-white w-full h-full grid grid-cols-1 md:grid-cols-2 gap-6 p-6">
      <!-- Bên trái -->
      <div class="space-y-6">
        <div class="flex flex-col items-center gap-4">
          <div
            class="w-40 h-40 rounded-full border-4 border-gray-400 overflow-hidden cursor-pointer transition-all duration-300 hover:shadow-lg"
            @click="triggerFileInput"
          >
            <img
              v-if="employeeImage"
              :src="employeeImage"
              alt="Ảnh nhân viên"
              class="w-full h-full object-cover"
            >
            <span v-else class="flex items-center justify-center h-full text-gray-500 text-sm font-medium">Chọn ảnh</span>
          </div>

          <button
            v-if="employeeImage"
            @click="deleteImage"
            class="bg-red-500 text-white w-8 h-8 rounded-full flex items-center justify-center hover:bg-red-600 transition-all duration-300 shadow-sm"
            title="Xóa ảnh"
          >
            ✕
          </button>

          <input type="file" ref="fileInput" @change="previewImage" class="hidden">
        </div>

        <div class="space-y-4">
          <div class="space-y-1">
            <label class="block text-sm font-medium text-gray-700">Email</label>
            <input
              v-model="custmerData.email"
              type="text"
              ref="emailInput"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
              placeholder="Nhập Email"
            >
          </div>

          <div class="space-y-1">
            <label class="block text-sm font-medium text-gray-700">CCCD</label>
            <input
              v-model="custmerData.cccd"
              type="text"
              ref="cccdInput"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
              placeholder="Nhập CCCD"
            >
          </div>

          <div class="space-y-1">
            <label class="block text-sm font-medium text-gray-700">Số điện thoại</label>
            <input
              v-model="custmerData.soDienThoai"
              type="text"
              ref="sdtInput"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
              placeholder="Nhập SDT"
            >
          </div>

          <div class="space-y-1">
            <label class="block text-sm font-medium text-gray-700">Ngày sinh</label>
            <input
              v-model="custmerData.ngaySinh"
              type="date"
              ref="ngaySinhInput"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
            >
          </div>

          <div class="space-y-1">
            <label class="block text-sm font-medium text-gray-700">Giới tính</label>
            <select
              v-model="custmerData.gioiTinh"
              ref="gioiTinhSelect"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
            >
              <option value="" disabled selected>--- Chọn giới tính ---</option>
              <option value="false">Nam</option>
              <option value="true">Nữ</option>
            </select>
          </div>
        </div>

        <div class="flex justify-end gap-4 mt-6">
          <router-link to="/backKH">
            <button
              @click="$emit('cancel')"
              class="px-6 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 transition-all duration-300"
            >
              Hủy
            </button>
          </router-link>
          <button
            @click="updateNhanVien"
            class="px-6 py-2 bg-orange-500 text-white rounded-lg transition-all duration-300 flex items-center gap-2"
          >
            <i class="fas fa-pen-to-square"></i> Cập nhật
          </button>
        </div>
      </div>

      <!-- Bên phải -->
      <div class="bg-gray-50 rounded-xl p-6 shadow-sm">
        <h1 class="text-xl font-semibold text-orange-600 mb-4">Thông tin địa chỉ</h1>
        <hr class="w-full border-t-2 border-gray-200 my-4 rounded-full bg-gray-100">
        <div class="space-y-4">
          <!-- Tên và Số điện thoại trên cùng hàng -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div class="space-y-1">
              <label class="block text-sm font-medium text-gray-700">Tên nhân viên</label>
              <input
                v-model="custmerData.ten"
                type="text"
                ref="tenNhanVienInput"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
                placeholder="Nhập tên nhân viên"
              >
            </div>
            <div class="space-y-1">
              <label class="block text-sm font-medium text-gray-700">Số điện thoại</label>
              <input
                v-model="custmerData.soDienThoai"
                type="text"
                ref="sdt2Input"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
                placeholder="Nhập SDT"
              >
            </div>
          </div>

          <!-- Địa chỉ cụ thể -->
          <div class="space-y-1">
            <label class="block text-sm font-medium text-gray-700">Địa chỉ cụ thể</label>
            <input
              v-model="custmerData.diaChiCuThe"
              type="text"
              ref="diaChiCuTheInput"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
              placeholder="Nhập địa chỉ cụ thể"
            >
          </div>

          <!-- Tỉnh/Thành phố, Quận/Huyện, Xã/Phường trên cùng hàng -->
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div class="space-y-1">
              <label class="block text-sm font-medium text-gray-700">Tỉnh/Thành phố</label>
              <select
                v-model="selectedProvince"
                @change="handleProvinceChange"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
              >
                <option value="" disabled>Chọn tỉnh/thành phố</option>
                <option v-for="province in provinces" :key="province.code">{{ province.name }}</option>
              </select>
            </div>
            <div class="space-y-1">
              <label class="block text-sm font-medium text-gray-700">Quận/Huyện</label>
              <select
                v-model="selectedDistrict"
                @change="handleDistrictChange"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
              >
                <option value="" disabled>Chọn quận/huyện</option>
                <option v-for="district in districts" :key="district.code">{{ district.name }}</option>
              </select>
            </div>
            <div class="space-y-1">
              <label class="block text-sm font-medium text-gray-700">Xã/Phường</label>
              <select
                v-model="selectedWard"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
              >
                <option value="" disabled>Chọn xã/phường</option>
                <option v-for="ward in wards" :key="ward.code">{{ ward.name }}</option>
              </select>
            </div>
          </div>
        </div>

        <div class="flex justify-end mt-8">
          <button
            @click="updateDchi"
            class="px-6 py-2 bg-orange-500 text-white rounded-lg transition-all duration-300 flex items-center gap-2"
          >
            <i class="fas fa-pen-to-square"></i> Cập nhật địa chỉ
          </button>
        </div>
      </div>
    </div>
  </div>
</template>



<script setup>
import { onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import axios from "axios";

const route = useRoute();
const router = useRouter();

const custmerData = ref({
  id: "",
  email: "",
  cccd: "",
  soDienThoai: "",
  ngaySinh: "",
  gioiTinh: "",
  ten: "",
  thanhPho: "",
  quan: "",
  phuong: "",
  diaChiCuThe: "",
});

const employeeImage = ref(null);
const fileInput = ref(null);
const provinces = ref([]);
const districts = ref([]);
const wards = ref([]);
const selectedProvince = ref("");
const selectedDistrict = ref("");
const selectedWard = ref("");

const triggerFileInput = () => fileInput.value.click();

const previewImage = (event) => {
  const file = event.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      employeeImage.value = e.target.result;
      custmerData.value.anhNhanVien = e.target.result;
    };
    reader.readAsDataURL(file);
  }
};

const deleteImage = () => {
  employeeImage.value = null;
  custmerData.value.anhNhanVien = "";
};

const fetchEmployeeData = async () => {
  const id = route.query.id;
  if (!id) {
    console.error("Không tìm thấy ID trong URL");
    return;
  }

  try {
    const res = await axios.get(`http://localhost:8080/khach-hang/detail/${id}`);
    const data = res.data;
    console.log("Dữ liệu thô từ API:", data);

    custmerData.value = {
      id: data.id || "",
      email: data.idTaiKhoan?.email || "",
      cccd: data.cccd || "",
      soDienThoai: data.idTaiKhoan?.soDienThoai || "",
      ngaySinh: data.ngaySinh ? new Date(data.ngaySinh).toISOString().split("T")[0] : "",
      gioiTinh: data.idTaiKhoan?.deleted !== undefined ? (data.idTaiKhoan.deleted ? "true" : "false") : "",
      ten: data.ten || "",
      diaChiCuThe: data.idDiaChiKH?.diaChiCuThe || "",
      thanhPho: data.idDiaChiKH?.thanhPho || "",
      quan: data.idDiaChiKH?.quan || "",
      phuong: data.idDiaChiKH?.phuong || "",
      anhNhanVien: data.anhNhanVien || "",
    };

    if (data.anhNhanVien) employeeImage.value = data.anhNhanVien;

    // Đồng bộ dropdown
    selectedProvince.value = custmerData.value.thanhPho;
    if (selectedProvince.value) {
      await handleProvinceChange();
      selectedDistrict.value = custmerData.value.quan;
      if (selectedDistrict.value) {
        await handleDistrictChange();
        selectedWard.value = custmerData.value.phuong;
      }
    }
  } catch (error) {
    console.error("Lỗi khi lấy dữ liệu nhân viên:", error);
  }
};

const updateNhanVien = async () => {
  try {
    const updatedData = {
      ...custmerData.value,
    };
    await axios.put(`http://localhost:8080/khach-hang/update/${route.query.id}`, updatedData);
    alert("Cập nhật thành công!");
  } catch (error) {
    console.error("Lỗi khi cập nhật nhân viên:", error);
    alert("Có lỗi xảy ra khi cập nhật.");
  }
};
const updateDchi = async () => {
  try {
    // Đồng bộ dữ liệu từ dropdown vào custmerData
    custmerData.value.thanhPho = selectedProvince.value;
    custmerData.value.quan = selectedDistrict.value;
    custmerData.value.phuong = selectedWard.value;

   
    const updatedData = {
      tenKH: custmerData.value.ten, 
      diaChiCuThe: custmerData.value.diaChiCuThe,
      phuong: custmerData.value.phuong,
      thanhPho: custmerData.value.thanhPho,
      quan: custmerData.value.quan,
    };

    // Gửi yêu cầu tới endpoint chính xác
    await axios.put(`http://localhost:8080/khach-hang/updateDchi/${route.query.id}`, updatedData);
    alert("Cập nhật địa chỉ thành công!");
  } catch (error) {
    console.error("Lỗi khi cập nhật địa chỉ:", error);
    if (error.code === "ERR_NETWORK") {
      alert("Không thể kết nối đến server. Vui lòng kiểm tra server!");
    } else {
      alert(`Có lỗi xảy ra khi cập nhật địa chỉ: ${error.message}`);
    }
  }
};



onMounted(async () => {
  try {
    const response = await axios.get("https://provinces.open-api.vn/api/?depth=3");
    provinces.value = response.data;
    await fetchEmployeeData();
  } catch (error) {
    console.error("Lỗi khi tải dữ liệu địa chỉ:", error);
  }
});

const handleProvinceChange = async () => {
  const province = provinces.value.find((prov) => prov.name === selectedProvince.value);
  if (province) {
    districts.value = province.districts;
  } else {
    districts.value = [];
  }
  selectedDistrict.value = "";
  wards.value = [];
  selectedWard.value = "";
};

const handleDistrictChange = async () => {
  const district = districts.value.find((dist) => dist.name === selectedDistrict.value);
  if (district) {
    wards.value = district.wards;
  } else {
    wards.value = [];
  }
  selectedWard.value = "";
};
</script>
<style>

</style>