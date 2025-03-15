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
          <!-- Nút thêm địa chỉ -->
          <div class="w-full mt-4">
            <button
              @click="toggleAddAddress"
              class="w-full flex items-center justify-center px-4 py-2 border border-green-500 text-green-500 rounded-lg hover:bg-green-100 transition-all duration-300"
            >
              <span class="mr-2 text-xl">+</span>
              Tạo địa chỉ mới
            </button>
          </div>

          <!-- Form thêm địa chỉ mới -->
          <div v-if="showAddAddress" class="border border-gray-300 p-4 rounded-lg mt-4 bg-white shadow-md">
            <h2 class="text-lg font-medium text-gray-700 mb-4">Thêm địa chỉ mới</h2>
            <div class="space-y-4">
              <!-- Địa chỉ cụ thể -->
              <div class="space-y-1">
                <label class="block text-sm font-medium text-gray-700">Địa chỉ cụ thể</label>
                <input
                  v-model="newAddress.diaChiCuThe"
                  type="text"
                  class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
                  placeholder="Nhập địa chỉ cụ thể"
                  required
                >
              </div>

              <!-- Tỉnh/Thành phố, Quận/Huyện, Xã/Phường -->
              <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div class="space-y-1">
                  <label class="block text-sm font-medium text-gray-700">Tỉnh/Thành phố</label>
                  <select
                    v-model="newAddress.thanhPho"
                    @change="handleNewProvinceChange"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
                    required
                  >
                    <option value="" disabled>Chọn tỉnh/thành phố</option>
                    <option v-for="province in provinces" :key="province.code" :value="province.name">{{ province.name }}</option>
                  </select>
                </div>
                <div class="space-y-1">
                  <label class="block text-sm font-medium text-gray-700">Quận/Huyện</label>
                  <select
                    v-model="newAddress.quan"
                    @change="handleNewDistrictChange"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
                    required
                  >
                    <option value="" disabled>Chọn quận/huyện</option>
                    <option v-for="district in newDistricts" :key="district.code" :value="district.name">{{ district.name }}</option>
                  </select>
                </div>
                <div class="space-y-1">
                  <label class="block text-sm font-medium text-gray-700">Xã/Phường/Thị trấn</label>
                  <select
                    v-model="newAddress.phuong"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
                    required
                  >
                    <option value="" disabled>Chọn xã/phường</option>
                    <option v-for="ward in newWards" :key="ward.code" :value="ward.name">{{ ward.name }}</option>
                  </select>
                </div>
              </div>

              <!-- Nút thêm và hủy trong form -->
              <div class="flex justify-end gap-4">
                <button
                  @click="addNewAddress"
                  class="px-4 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600 transition-all duration-300"
                >
                  Thêm
                </button>
                <button
                  @click="toggleAddAddress"
                  class="px-4 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 transition-all duration-300"
                >
                  Hủy
                </button>
              </div>
            </div>
          </div>
        </div>

        <div class="flex justify-end mt-8 gap-4">
          <button
            @click="updateDchi"
            class="px-6 py-2 bg-orange-500 text-white rounded-lg transition-all duration-300 flex items-center gap-2"
          >
            <i class="fas fa-pen-to-square"></i> Cập nhật địa chỉ
          </button>
        </div>

        <hr class="w-full border-t-2 border-gray-200 my-4 rounded-full bg-gray-100">

        <!-- Danh sách địa chỉ -->
        <div class="space-y-4">
          <h2 class="text-lg font-medium text-gray-700">Danh sách địa chỉ</h2>
          <div class="space-y-6">
            <div v-for="(address, index) in paginatedAddresses" :key="address.id" class="border border-gray-300 rounded-lg p-4 bg-white shadow-sm">
              <div class="flex items-center mb-4">
                <label class="text-sm font-medium text-gray-700 mr-2">Địa chỉ {{ index + 1 + (currentPage.value - 1) * itemsPerPage.value }}</label>
                <div class="flex items-center ml-auto space-x-2">
                  <label class="text-sm font-medium text-gray-700">Địa chỉ mặc định</label>
                  <input
                    type="checkbox"
                    v-model="address.macDinh"
                    @change="setDefaultAddress(address)"
                    class="form-checkbox h-5 w-5 text-blue-600"
                  >
                </div>
              </div>
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div class="space-y-1">
                  <label class="block text-sm font-medium text-gray-700">Tên</label>
                  <input
                    v-model="address.ten"
                    type="text"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
                    readonly
                  >
                </div>
                <div class="space-y-1">
                  <label class="block text-sm font-medium text-gray-700">Số điện thoại</label>
                  <input
                    v-model="address.soDienThoai"
                    type="text"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
                    readonly
                  >
                </div>
              </div>
              <div class="space-y-1 mt-4">
                <label class="block text-sm font-medium text-gray-700">Địa chỉ cụ thể</label>
                <input
                  v-model="address.diaChiCuThe"
                  type="text"
                  class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
                  :readonly="!address.isEditing"
                >
              </div>
              <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mt-4">
                <div class="space-y-1">
                  <label class="block text-sm font-medium text-gray-700">Tỉnh/Thành phố</label>
                  <select
                    v-model="address.thanhPho"
                    @change="updateDistricts(address)"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
                    :disabled="!address.isEditing"
                  >
                    <option value="" disabled>Chọn tỉnh/thành phố</option>
                    <option v-for="province in provinces" :key="province.code" :value="province.name">{{ province.name }}</option>
                  </select>
                </div>
                <div class="space-y-1">
                  <label class="block text-sm font-medium text-gray-700">Quận/Huyện</label>
                  <select
                    v-model="address.quan"
                    @change="updateWards(address)"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
                    :disabled="!address.isEditing"
                  >
                    <option value="" disabled>Chọn quận/huyện</option>
                    <option v-for="district in address.availableDistricts" :key="district.code" :value="district.name">{{ district.name }}</option>
                  </select>
                </div>
                <div class="space-y-1">
                  <label class="block text-sm font-medium text-gray-700">Xã/Phường/Thị trấn</label>
                  <select
                    v-model="address.phuong"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
                    :disabled="!address.isEditing"
                  >
                    <option value="" disabled>Chọn xã/phường</option>
                    <option v-for="ward in address.availableWards" :key="ward.code" :value="ward.name">{{ ward.name }}</option>
                  </select>
                </div>
              </div>
              <div class="flex justify-end mt-4 space-x-2">
                <button
                  @click="deleteAddress(address.id)"
                  class="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600 transition-all duration-300"
                >
                  Xóa
                </button>
                <button
                  v-if="!address.isEditing"
                  @click="editAddress(address)"
                  class="px-4 py-2 bg-yellow-500 text-white rounded-lg hover:bg-yellow-600 transition-all duration-300"
                >
                  Sửa
                </button>
                <button
                  v-else
                  @click="saveAddress(address)"
                  class="px-4 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600 transition-all duration-300"
                >
                  Lưu
                </button>
              </div>
            </div>
            <div v-if="!addresses.length" class="text-center text-sm text-gray-600">
              Không có địa chỉ nào
            </div>
            <!-- Phân trang -->
            <div class="flex justify-center mt-4" v-if="addresses.length > itemsPerPage">
              <button
                @click="currentPage--"
                :disabled="currentPage === 1"
                class="px-3 py-1 bg-gray-200 text-gray-700 rounded-l-lg hover:bg-gray-300 transition-all duration-300 disabled:opacity-50"
              >
                Trước
              </button>
              <span class="px-4 py-1 bg-gray-100">{{ currentPage }} / {{ totalPages }}</span>
              <button
                @click="currentPage++"
                :disabled="currentPage === totalPages"
                class="px-3 py-1 bg-gray-200 text-gray-700 rounded-r-lg hover:bg-gray-300 transition-all duration-300 disabled:opacity-50"
              >
                Sau
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, computed } from "vue";
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
  anhNhanVien: "",
});

const addresses = ref([]);
const employeeImage = ref(null);
const fileInput = ref(null);
const provinces = ref([]);
const districts = ref([]);
const wards = ref([]);
const selectedProvince = ref("");
const selectedDistrict = ref("");
const selectedWard = ref("");

// Phân trang
const currentPage = ref(1);
const itemsPerPage = ref(1); // Chỉ hiển thị 1 địa chỉ trên mỗi trang

// Dữ liệu cho địa chỉ mới
const showAddAddress = ref(false);
const newAddress = ref({
  diaChiCuThe: "",
  thanhPho: "",
  quan: "",
  phuong: "",
});
const newDistricts = ref([]);
const newWards = ref([]);

// Computed property cho danh sách địa chỉ phân trang
const paginatedAddresses = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage.value;
  const end = start + itemsPerPage.value;
  return addresses.value.slice(start, end);
});

const totalPages = computed(() => Math.ceil(addresses.value.length / itemsPerPage.value));

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
    console.log("Dữ liệu thô từ API sau fetch:", data);

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

    const addressRes = await axios.get(`http://localhost:8080/dia-chi/getByKhachHang/${id}`);
    addresses.value = addressRes.data.map(address => ({
      ...address,
      ten: data.ten || "",
      soDienThoai: data.idTaiKhoan?.soDienThoai || "",
      isEditing: false,
      availableDistricts: [],
      availableWards: [],
    })) || [];

    // Đồng bộ selectedProvince, selectedDistrict, selectedWard với custmerData
    selectedProvince.value = custmerData.value.thanhPho;
    await handleProvinceChange();
    selectedDistrict.value = custmerData.value.quan;
    await handleDistrictChange();
    selectedWard.value = custmerData.value.phuong;

    // Cập nhật lại availableDistricts và availableWards cho các địa chỉ
    addresses.value.forEach(address => {
      updateDistricts(address);
      updateWards(address);
    });
  } catch (error) {
    console.error("Lỗi khi lấy dữ liệu nhân viên:", error);
    alert("Không thể tải dữ liệu khách hàng: " + (error.response?.data?.error || error.message));
  }
};

const updateNhanVien = async () => {
  try {
    if (!custmerData.value.email || !custmerData.value.soDienThoai) {
      throw new Error("Email và số điện thoại không được để trống!");
    }

    const updatedData = { ...custmerData.value };
    const response = await axios.put(`http://localhost:8080/khach-hang/update/${route.query.id}`, updatedData);

    alert("Cập nhật thông tin khách hàng thành công!");
    await fetchEmployeeData(); // Làm mới dữ liệu
    router.push({ path: "/khach-hang" });
  } catch (error) {
    console.error("Lỗi khi cập nhật thông tin khách hàng:", error);
    const errorMessage = error.response?.data?.error || error.message;
    alert("Có lỗi xảy ra khi cập nhật thông tin khách hàng: " + errorMessage);
  }
};

const updateDchi = async () => {
  try {
    if (!selectedProvince.value || !selectedDistrict.value || !selectedWard.value) {
      throw new Error("Vui lòng chọn đầy đủ Tỉnh/Thành phố, Quận/Huyện và Xã/Phường!");
    }
    if (!custmerData.value.diaChiCuThe || !custmerData.value.ten) {
      throw new Error("Tên và địa chỉ cụ thể không được để trống!");
    }

    const updatedData = {
      tenKH: custmerData.value.ten,
      diaChiCuThe: custmerData.value.diaChiCuThe,
      phuong: selectedWard.value,
      thanhPho: selectedProvince.value,
      quan: selectedDistrict.value,
    };

    console.log("Dữ liệu gửi đi cho updateDchi:", updatedData); // Debug
    const response = await axios.put(`http://localhost:8080/khach-hang/updateDchi/${route.query.id}`, updatedData);
    console.log("Phản hồi từ server sau updateDchi:", response.data);

    alert("Cập nhật địa chỉ mặc định thành công!");
    await fetchEmployeeData(); // Làm mới dữ liệu
  } catch (error) {
    console.error("Lỗi khi cập nhật địa chỉ mặc định:", error);
    const errorMessage = error.response?.data?.error || error.message;
    alert("Có lỗi xảy ra khi cập nhật địa chỉ mặc định: " + errorMessage);
  }
};

const toggleAddAddress = () => {
  showAddAddress.value = !showAddAddress.value;
  if (!showAddAddress.value) {
    newAddress.value = { diaChiCuThe: "", thanhPho: "", quan: "", phuong: "" };
    newDistricts.value = [];
    newWards.value = [];
  }
};

const handleNewProvinceChange = () => {
  const province = provinces.value.find((prov) => prov.name === newAddress.value.thanhPho);
  if (province) {
    newDistricts.value = province.districts || [];
    console.log("New Districts for", newAddress.value.thanhPho, ":", newDistricts.value);
  } else {
    newDistricts.value = [];
    console.warn("Province not found:", newAddress.value.thanhPho);
  }
  newAddress.value.quan = "";
  newWards.value = [];
  newAddress.value.phuong = "";
};

const handleNewDistrictChange = () => {
  const district = newDistricts.value.find((dist) => dist.name === newAddress.value.quan);
  if (district) {
    newWards.value = district.wards || [];
    console.log("New Wards for", newAddress.value.quan, ":", newWards.value);
    if (!newWards.value.length) {
      console.warn("No wards available for district:", newAddress.value.quan);
    }
  } else {
    newWards.value = [];
    console.warn("District not found or no wards for:", newAddress.value.quan, "in", newDistricts.value);
  }
  newAddress.value.phuong = "";
};

const addNewAddress = async () => {
  try {
    if (!route.query.id || isNaN(parseInt(route.query.id))) {
      throw new Error("ID khách hàng không hợp lệ");
    }
    if (!newAddress.value.diaChiCuThe || !newAddress.value.phuong || !newAddress.value.thanhPho || !newAddress.value.quan) {
      throw new Error("Vui lòng nhập đầy đủ thông tin địa chỉ");
    }

    const newData = {
      idKhachHang: parseInt(route.query.id),
      ma: "DC" + new Date().getTime(),
      diaChiCuThe: newAddress.value.diaChiCuThe,
      phuong: newAddress.value.phuong,
      thanhPho: newAddress.value.thanhPho,
      quan: newAddress.value.quan,
      macDinh: false,
      deleted: false,
    };

    const response = await axios.post(`http://localhost:8080/dia-chi/addDchi`, newData);
    console.log("Phản hồi từ server sau addNewAddress:", response.data);

    alert("Thêm địa chỉ thành công!");
    toggleAddAddress();
    await fetchEmployeeData();
    currentPage.value = totalPages.value;
  } catch (error) {
    console.error("Lỗi khi thêm địa chỉ:", error);
    const errorMessage = error.response?.data?.error || error.message;
    alert("Có lỗi xảy ra khi thêm địa chỉ: " + errorMessage);
  }
};

const handleProvinceChange = async () => {
  const province = provinces.value.find((prov) => prov.name === selectedProvince.value);
  if (province) {
    districts.value = province.districts || [];
    console.log("Districts for", selectedProvince.value, ":", districts.value);
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
    wards.value = district.wards || [];
    console.log("Wards for", selectedDistrict.value, ":", wards.value);
    if (!wards.value.length) {
      console.warn("No wards available for district:", selectedDistrict.value);
    }
  } else {
    wards.value = [];
    console.warn("District not found or no wards for:", selectedDistrict.value, "in", districts.value);
  }
  selectedWard.value = "";
};

const updateDistricts = (address) => {
  const province = provinces.value.find((prov) => prov.name === address.thanhPho);
  if (province) {
    address.availableDistricts = province.districts || [];
    console.log("Available Districts for", address.thanhPho, ":", address.availableDistricts);
  } else {
    address.availableDistricts = [];
    console.warn("Province not found for address:", address.thanhPho);
  }
  address.quan = address.availableDistricts.some(d => d.name === address.quan) ? address.quan : "";
  address.availableWards = [];
  address.phuong = "";
};

const updateWards = (address) => {
  const district = address.availableDistricts.find((dist) => dist.name === address.quan);
  if (district) {
    address.availableWards = district.wards || [];
    console.log("Available Wards for", address.quan, ":", address.availableWards);
    if (!address.availableWards.length) {
      console.warn("No wards available for district:", address.quan, "in", address.availableDistricts);
    }
  } else {
    address.availableWards = [];
    console.warn("District not found or no wards for:", address.quan, "in", address.availableDistricts);
  }
  address.phuong = address.availableWards.some(w => w.name === address.phuong) ? address.phuong : "";
};

const setDefaultAddress = async (selectedAddress) => {
  try {
    addresses.value.forEach(address => {
      if (address.id !== selectedAddress.id) {
        address.macDinh = false;
      }
    });
    const response = await axios.put(`http://localhost:8080/dia-chi/setDefault/${selectedAddress.id}`, { macDinh: selectedAddress.macDinh });
    console.log("Phản hồi từ server sau setDefault:", response.data);

    alert("Cập nhật địa chỉ mặc định thành công!");
    await fetchEmployeeData();
  } catch (error) {
    console.error("Lỗi khi cập nhật địa chỉ mặc định:", error);
    const errorMessage = error.response?.data?.error || error.message;
    alert("Có lỗi xảy ra khi cập nhật địa chỉ mặc định: " + errorMessage);
  }
};

const editAddress = (address) => {
  address.isEditing = true;
};

const saveAddress = async (address) => {
  try {
    if (!address.diaChiCuThe || !address.thanhPho || !address.quan || !address.phuong) {
      throw new Error("Vui lòng nhập đầy đủ thông tin địa chỉ!");
    }

    const updatedData = {
      diaChiCuThe: address.diaChiCuThe,
      phuong: address.phuong,
      thanhPho: address.thanhPho,
      quan: address.quan,
      macDinh: address.macDinh,
    };

    console.log("Dữ liệu gửi đi cho saveAddress:", updatedData); // Debug
    const response = await axios.put(`http://localhost:8080/dia-chi/updateDchi/${address.id}`, updatedData);
    console.log("Phản hồi từ server sau saveAddress:", response.data);

    address.isEditing = false;
    alert("Cập nhật địa chỉ thành công!");
    await fetchEmployeeData(); // Làm mới dữ liệu
  } catch (error) {
    console.error("Lỗi khi cập nhật địa chỉ:", error);
    const errorMessage = error.response?.data?.error || error.message;
    alert("Có lỗi xảy ra khi cập nhật địa chỉ: " + errorMessage);
  }
};

const deleteAddress = async (id) => {
  if (confirm("Bạn có chắc chắn muốn xóa địa chỉ này?")) {
    try {
      const response = await axios.delete(`http://localhost:8080/dia-chi/delete/${id}`);
      console.log("Phản hồi từ server sau deleteAddress:", response.data);

      alert("Xóa địa chỉ thành công!");
      await fetchEmployeeData();
      if (paginatedAddresses.value.length === 0 && currentPage.value > 1) {
        currentPage.value--;
      }
    } catch (error) {
      console.error("Lỗi khi xóa địa chỉ:", error);
      const errorMessage = error.response?.data?.error || error.message;
      alert("Có lỗi xảy ra khi xóa địa chỉ: " + errorMessage);
    }
  }
};

onMounted(async () => {
  try {
    const response = await axios.get("https://provinces.open-api.vn/api/?depth=3");
    provinces.value = response.data;
    console.log("Provinces data:", provinces.value);
    await fetchEmployeeData();
  } catch (error) {
    console.error("Lỗi khi tải dữ liệu địa chỉ:", error);
    alert("Không thể tải dữ liệu địa chỉ: " + (error.response?.data?.error || error.message));
  }
});
</script>

<style>
/* Tùy chỉnh style nếu cần */
</style>