<template>
  <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />
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
              @error="handleImageError"
            >
          </div>
          <input type="file" ref="fileInput" @change="previewImage" class="hidden" accept="image/*">
        </div>

        <div class="space-y-4">
          <div class="space-y-1">
            <label class="block text-sm font-medium text-gray-700">Email</label>
            <input
              v-model="employeeData.email"
              type="text"
              ref="emailInput"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
              placeholder="Nhập Email"
            >
          </div>

          <div class="space-y-1">
            <label class="block text-sm font-medium text-gray-700">CCCD</label>
            <input
              v-model="employeeData.cccd"
              type="text"
              ref="cccdInput"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
              placeholder="Nhập CCCD"
            >
          </div>

          <div class="space-y-1">
            <label class="block text-sm font-medium text-gray-700">Số điện thoại</label>
            <input
              v-model="employeeData.soDienThoai"
              type="text"
              ref="sdtInput"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
              placeholder="Nhập SDT"
            >
          </div>

          <div class="space-y-1">
            <label class="block text-sm font-medium text-gray-700">Ngày sinh</label>
            <input
              v-model="employeeData.ngaySinh"
              type="date"
              ref="ngaySinhInput"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
            >
          </div>

          <div>
            <label class="block mb-2 text-sm font-medium text-gray-700">Giới tính</label>
            <div class="flex items-center gap-6">
              <label class="flex items-center space-x-2 cursor-pointer">
                <input
                  v-model="employeeData.gioiTinh"
                  value="false"
                  type="radio"
                  name="gender"
                  id="male"
                  class="form-radio h-5 w-5 text-orange-500 focus:ring-2 focus:ring-orange-500 focus:ring-opacity-50 transition duration-150 ease-in-out"
                >
                <span class="text-sm text-gray-700">Nam</span>
              </label>
              <label class="flex items-center space-x-2 cursor-pointer">
                <input
                  v-model="employeeData.gioiTinh"
                  value="true"
                  type="radio"
                  name="gender"
                  id="female"
                  class="form-radio h-5 w-5 text-orange-500 focus:ring-2 focus:ring-orange-500 focus:ring-opacity-50 transition duration-150 ease-in-out"
                >
                <span class="text-sm text-gray-700">Nữ</span>
              </label>
            </div>
          </div>
        </div>

        <div class="flex justify-end gap-4 mt-6">
          <router-link to="/nhan-vien">
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
          <div class="space-y-1">
            <label class="block text-sm font-medium text-gray-700">Tên nhân viên</label>
            <input
              v-model="employeeData.tenNhanVien"
              type="text"
              ref="tenNhanVienInput"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
              placeholder="Nhập tên nhân viên"
            >
          </div>

          <div class="space-y-1">
            <label class="block text-sm font-medium text-gray-700">Địa chỉ cụ thể</label>
            <input
              v-model="employeeData.diaChiCuThe"
              type="text"
              ref="diaChiCuTheInput"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
              placeholder="Nhập địa chỉ cụ thể"
            >
          </div>

          <div class="grid grid-cols-3 gap-4">
            <div class="space-y-1">
              <label class="block text-sm font-medium text-gray-700">Tỉnh/Thành phố</label>
              <select
                v-model="selectedProvince"
                @change="handleProvinceChange"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
              >
                <option value="" disabled>Chọn tỉnh/thành phố</option>
                <option v-for="province in provinces" :key="province.code" :value="province.name">{{ province.name }}</option>
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
                <option v-for="district in districts" :key="district.code" :value="district.name">{{ district.name }}</option>
              </select>
            </div>

            <div class="space-y-1">
              <label class="block text-sm font-medium text-gray-700">Xã/Phường</label>
              <select
                v-model="selectedWard"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
              >
                <option value="" disabled>Chọn xã/phường</option>
                <option v-for="ward in wards" :key="ward.code" :value="ward.name">{{ ward.name }}</option>
              </select>
            </div>
          </div>
        </div>

        <div class="flex justify-end mt-8">
          <button
            @click="updatediaChi"
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
import { onMounted, ref, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import axios from "axios";
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue';

const route = useRoute();
const router = useRouter();

const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === "function") {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || ["Quản lý Nhân Viên", "Cập nhật Nhân Viên"];
});

const employeeData = ref({
  id: "",
  email: "",
  cccd: "",
  soDienThoai: "",
  ngaySinh: "",
  gioiTinh: "",
  tenNhanVien: "",
  diaChiCuThe: "",
  thanhPho: "",
  quan: "",
  phuong: "",
  anhNhanVien: "",
});

const employeeImage = ref(null);
const fileInput = ref(null);
const hasNewImage = ref(false);

const triggerFileInput = () => {
  fileInput.value.click();
};

const previewImage = (event) => {
  const file = event.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      employeeImage.value = e.target.result; // Chuỗi Base64
      employeeData.value.anhNhanVien = e.target.result;
      hasNewImage.value = true;
    };
    reader.readAsDataURL(file);
  }
};

const deleteImage = () => {
  employeeImage.value = null;
  employeeData.value.anhNhanVien = "";
  hasNewImage.value = false;
  fileInput.value.value = "";
};

// Xử lý lỗi khi ảnh không tải được
const handleImageError = () => {
  console.error("Không thể tải ảnh nhân viên!");
  employeeImage.value = null; // Reset ảnh nếu lỗi
};

// Dữ liệu địa chỉ
const provinces = ref([]);
const districts = ref([]);
const wards = ref([]);
const selectedProvince = ref("");
const selectedDistrict = ref("");
const selectedWard = ref("");

// Lấy dữ liệu nhân viên theo ID
const fetchEmployeeData = async () => {
  const id = route.query.id;
  if (!id) {
    console.error("Không tìm thấy ID trong URL");
    return;
  }

  try {
    const res = await axios.get(`http://localhost:8080/nhan-vien/detail/${id}`);
    const data = res.data;
    employeeData.value = {
      id: data.id || "",
      email: data.idTaiKhoan?.email || "",
      cccd: data.cccd || "",
      soDienThoai: data.idTaiKhoan?.soDienThoai || "",
      ngaySinh: data.ngaySinh ? new Date(data.ngaySinh).toISOString().split("T")[0] : "",
      gioiTinh: data.idTaiKhoan?.deleted !== undefined ? String(!data.idTaiKhoan.deleted) : "",
      tenNhanVien: data.tenNhanVien || "",
      diaChiCuThe: data.diaChiCuThe || "",
      thanhPho: data.thanhPho || "",
      quan: data.quan || "",
      phuong: data.phuong || "",
      anhNhanVien: data.anhNhanVien || "",
    };

    // Xử lý ảnh từ API
    if (data.anhNhanVien) {
      if (data.anhNhanVien.startsWith("data:")) {
        // Nếu là chuỗi Base64
        employeeImage.value = data.anhNhanVien;
      } else {
        // Nếu là đường dẫn tương đối, nối với base URL
        employeeImage.value = `http://localhost:8080${data.anhNhanVien}`;
      }
    } else {
      employeeImage.value = null;
    }

    console.log("Ảnh nhân viên từ API:", employeeImage.value); // Debug giá trị ảnh
  } catch (error) {
    console.error("Lỗi khi lấy dữ liệu nhân viên:", error);
  }

  // Đồng bộ dữ liệu địa chỉ
  selectedProvince.value = employeeData.value.thanhPho || "";
  if (selectedProvince.value) {
    await handleProvinceChange();
    selectedDistrict.value = employeeData.value.quan || "";
    if (selectedDistrict.value) {
      await handleDistrictChange();
      selectedWard.value = employeeData.value.phuong || "";
    }
  }
};

// Cập nhật nhân viên
const updateNhanVien = async () => {
  try {
    const updatedData = {
      email: employeeData.value.email,
      cccd: employeeData.value.cccd,
      soDienThoai: employeeData.value.soDienThoai,
      ngaySinh: employeeData.value.ngaySinh,
      gioiTinh: employeeData.value.gioiTinh === "true",
      tenNhanVien: employeeData.value.tenNhanVien,
      diaChiCuThe: employeeData.value.diaChiCuThe,
      thanhPho: selectedProvince.value,
      quan: selectedDistrict.value,
      phuong: selectedWard.value,
      anhNhanVien: hasNewImage.value ? employeeData.value.anhNhanVien : employeeData.value.anhNhanVien || "",
    };

    const response = await axios.put(
      `http://localhost:8080/nhan-vien/update/${route.query.id}`,
      updatedData,
      { headers: { "Content-Type": "application/json" } }
    );
    alert("Cập nhật thành công!");
    router.push({ path: "/nhan-vien" });
  } catch (error) {
    console.error("Lỗi khi cập nhật nhân viên:", error.response ? error.response.data : error.message);
    alert("Có lỗi xảy ra khi cập nhật: " + (error.response ? error.response.data : error.message));
  }
};

// Cập nhật địa chỉ
const updatediaChi = async () => {
  try {
    const updatedData = {
      email: employeeData.value.email,
      cccd: employeeData.value.cccd,
      soDienThoai: employeeData.value.soDienThoai,
      ngaySinh: employeeData.value.ngaySinh,
      gioiTinh: employeeData.value.gioiTinh === "true",
      tenNhanVien: employeeData.value.tenNhanVien,
      diaChiCuThe: employeeData.value.diaChiCuThe,
      thanhPho: selectedProvince.value,
      quan: selectedDistrict.value,
      phuong: selectedWard.value,
      anhNhanVien: hasNewImage.value ? employeeData.value.anhNhanVien : employeeData.value.anhNhanVien || "",
    };

    const response = await axios.put(
      `http://localhost:8080/nhan-vien/update/${route.query.id}`,
      updatedData,
      { headers: { "Content-Type": "application/json" } }
    );
    alert("Cập nhật địa chỉ thành công!");
    router.push({ path: "/nhan-vien" });
  } catch (error) {
    console.error("Lỗi khi cập nhật địa chỉ:", error.response ? error.response.data : error.message);
    alert("Có lỗi xảy ra khi cập nhật địa chỉ: " + (error.response ? error.response.data : error.message));
  }
};

// Lấy danh sách tỉnh/thành phố
onMounted(async () => {
  try {
    const response = await axios.get("https://provinces.open-api.vn/api/?depth=3");
    provinces.value = response.data;

    const imageFromQuery = route.query.image;
    if (imageFromQuery) {
      employeeImage.value = imageFromQuery;
      employeeData.value.anhNhanVien = imageFromQuery;
    } else {
      await fetchEmployeeData();
    }
  } catch (error) {
    console.error("Lỗi khi tải dữ liệu địa chỉ:", error);
    await fetchEmployeeData();
  }
});

// Xử lý thay đổi tỉnh/thành phố
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

// Xử lý thay đổi quận/huyện
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

<style scoped>
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