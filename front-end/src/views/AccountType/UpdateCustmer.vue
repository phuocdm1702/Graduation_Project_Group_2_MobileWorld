<template>
  <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />
  <ToastNotification ref="toastRef" />
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
              v-if="customerImage"
              :src="customerImage"
              alt="Ảnh khách hàng"
              class="w-full h-full object-cover"
              @error="handleImageError"
            >
            <div
              v-else
              class="w-full h-full flex items-center justify-center bg-gray-200 text-gray-500 font-medium"
            >
              Chọn ảnh
            </div>
          </div>
          <button
            v-if="customerImage"
            @click="deleteImage"
            class="bg-red-500 text-white w-8 h-8 rounded-full flex items-center justify-center hover:bg-red-600 transition-all duration-300 shadow-sm"
            title="Xóa ảnh"
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
            <label class="block text-sm font-medium text-gray-700">Tên khách hàng</label>
            <input
              v-model="custmerData.ten"
              type="text"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
              placeholder="Nhập tên khách hàng"
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
          <div class="flex items-center gap-6">
            <label class="flex items-center space-x-2 cursor-pointer">
              <input
                v-model="custmerData.gioiTinh"
                value="false"
                type="radio"
                class="form-radio h-5 w-5 text-orange-500 focus:ring-2 focus:ring-orange-500 focus:ring-opacity-50 transition duration-150 ease-in-out"
              >
              <span class="text-sm text-gray-700">Nam</span>
            </label>
            <label class="flex items-center space-x-2 cursor-pointer">
              <input
                v-model="custmerData.gioiTinh"
                value="true"
                type="radio"
                class="form-radio h-5 w-5 text-orange-500 focus:ring-2 focus:ring-orange-500 focus:ring-opacity-50 transition duration-150 ease-in-out"
              >
              <span class="text-sm text-gray-700">Nữ</span>
            </label>
          </div>
        </div>
        <div class="flex justify-end gap-4 mt-6">
          <router-link to="/khach-hang">
            <button
              class="px-6 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 transition-all duration-300"
            >
              Hủy
            </button>
          </router-link>
          <button
            @click="showConfirmUpdate"
            class="px-6 py-2 bg-orange-500 text-white rounded-lg transition-all duration-300 flex items-center gap-2 hover:bg-orange-600"
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
          <div class="w-full mt-4">
            <button
              @click="toggleAddAddress"
              class="w-full flex items-center justify-center px-4 py-2 border border-green-500 text-green-500 rounded-lg hover:bg-green-100 transition-all duration-300"
            >
              <span class="mr-2 text-xl">+</span>
              Tạo địa chỉ mới
            </button>
          </div>

          <div v-if="showAddAddress" class="border border-gray-300 p-4 rounded-lg mt-4 bg-white shadow-md">
            <h2 class="text-lg font-medium text-gray-700 mb-4">Thêm địa chỉ mới</h2>
            <div class="space-y-4">
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
                    :disabled="!newDistricts.length"
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
                    :disabled="!newWards.length"
                    required
                  >
                    <option value="" disabled>Chọn xã/phường</option>
                    <option v-for="ward in newWards" :key="ward.code" :value="ward.name">{{ ward.name }}</option>
                  </select>
                </div>
              </div>
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

        <hr class="w-full border-t-2 border-gray-200 my-4 rounded-full bg-gray-100">

        <div class="space-y-4">
          <h2 class="text-lg font-medium text-gray-700">Danh sách địa chỉ</h2>
          <div class="space-y-6">
            <div v-for="(address, index) in paginatedAddresses" :key="address.id" class="border border-gray-300 rounded-lg p-4 bg-white shadow-sm">
              <div class="flex items-center mb-4">
                <label class="text-sm font-medium text-gray-700 mr-2">Địa chỉ {{ getAddressNumber(index) }}</label>
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
                    :disabled="!address.isEditing || !address.availableDistricts.length"
                  >
                    <option value="" disabled>Chọn quận/huyện</option>
                    <option v-for="district in address.availableDistricts" :key="district.code" :value="district.name">{{ district.name }}</option>
                    <option v-if="address.quan && !address.availableDistricts.some(d => d.name === address.quan)" :value="address.quan">
                      {{ address.quan }} (từ dữ liệu cũ)
                    </option>
                  </select>
                </div>
                <div class="space-y-1">
                  <label class="block text-sm font-medium text-gray-700">Xã/Phường/Thị trấn</label>
                  <select
                    v-model="address.phuong"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
                    :disabled="!address.isEditing || !address.availableWards.length"
                  >
                    <option value="" disabled>Chọn xã/phường</option>
                    <option v-for="ward in address.availableWards" :key="ward.code" :value="ward.name">{{ ward.name }}</option>
                    <option v-if="address.phuong && !address.availableWards.some(w => w.name === address.phuong)" :value="address.phuong">
                      {{ address.phuong }} (từ dữ liệu cũ)
                    </option>
                  </select>
                </div>
              </div>
              <div class="mt-4 flex items-center">
                <label class="text-sm font-medium text-gray-700 mr-2">Địa chỉ mặc định</label>
                <label class="switch">
                  <input
                    type="checkbox"
                    v-model="address.macDinh"
                    @change="setDefaultAddress(address)"
                  >
                  <span class="slider round"></span>
                </label>
                <div class="flex space-x-2 ml-auto">
                  <button
                    @click="showConfirmDeleteAddress(address.id)"
                    class="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600 transition-all duration-300"
                  >
                    <i class="fas fa-trash"></i>
                  </button>
                  <button
                    v-if="!address.isEditing"
                    @click="editAddress(address)"
                    class="px-4 py-2 bg-yellow-500 text-white rounded-lg hover:bg-yellow-600 transition-all duration-300"
                  >
                    <i class="fas fa-pen-to-square"></i>
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
            </div>
            <div v-if="!addresses.length" class="text-center text-sm text-gray-600">
              Không có địa chỉ nào
            </div>
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

  <ConfirmModal
    :show="showConfirmModal"
    :message="confirmMessage"
    @confirm="handleConfirm"
    @cancel="cancelConfirm"
  />
</template>

<script setup>
import { onMounted, ref, computed, nextTick } from "vue";
import { useRoute, useRouter } from "vue-router";
import axios from "axios";
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue';
import ToastNotification from "@/components/ToastNotification.vue";
import ConfirmModal from "@/components/ConfirmModal.vue";

const route = useRoute();
const router = useRouter();

const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === "function") {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || ["Khách hàng", "Cập nhật khách hàng"];
});

const custmerData = ref({
  id: "",
  email: "",
  soDienThoai: "",
  ngaySinh: "",
  gioiTinh: "",
  ten: "",
  thanhPho: "",
  quan: "",
  phuong: "",
  diaChiCuThe: "",
  anhKhachHang: "",
});

const addresses = ref([]);
const customerImage = ref(null);
const fileInput = ref(null);
const hasNewImage = ref(false);
const provinces = ref([]);
const newDistricts = ref([]);
const newWards = ref([]);
const currentPage = ref(1);
const itemsPerPage = ref(1);
const showConfirmModal = ref(false);
const toastRef = ref(null);
const showAddAddress = ref(false);
const newAddress = ref({
  diaChiCuThe: "",
  thanhPho: "",
  quan: "",
  phuong: "",
});
const confirmAction = ref(null);
const confirmMessage = ref("");

const paginatedAddresses = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage.value;
  const end = start + itemsPerPage.value;
  return addresses.value.slice(start, end);
});

const totalPages = computed(() => Math.ceil(addresses.value.length / itemsPerPage.value));

const getAddressNumber = (index) => {
  const baseIndex = (currentPage.value - 1) * itemsPerPage.value + index + 1;
  return isNaN(baseIndex) ? 1 : baseIndex;
};

const triggerFileInput = () => fileInput.value.click();

const previewImage = (event) => {
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
      custmerData.value.anhKhachHang = e.target.result;
      hasNewImage.value = true;
    };
    reader.readAsDataURL(file);
  }
};

const deleteImage = () => {
  customerImage.value = null;
  custmerData.value.anhKhachHang = "";
  hasNewImage.value = false;
  fileInput.value.value = "";
};

const handleImageError = () => {
  console.error("Không thể tải ảnh khách hàng!");
  customerImage.value = null;
};

const fetchProvinces = async () => {
  try {
    const response = await axios.get("https://provinces.open-api.vn/api/?depth=3", {
      withCredentials: false // Tắt thông tin xác thực để tránh lỗi CORS
    });
    provinces.value = response.data || [];
    if (!provinces.value.length) {
      toastRef.value.kshowToast('error', 'Không tải được danh sách tỉnh/thành phố!');
    }
  } catch (error) {
    console.error("Lỗi khi tải dữ liệu địa chỉ:", error);
    toastRef.value.kshowToast('error', 'Không thể tải danh sách tỉnh/thành phố: ' + (error.response?.data?.error || error.message));
    provinces.value = [];
  }
};

const fetchCustomerData = async () => {
  const id = route.query.id;
  if (!id) {
    console.error("Không tìm thấy ID trong URL");
    toastRef.value.kshowToast('error', 'Không tìm thấy ID khách hàng trong URL!');
    return;
  }

  try {
    const res = await axios.get(`http://localhost:8080/khach-hang/detail/${id}`);
    const data = res.data;

    const addressRes = await axios.get(`http://localhost:8080/dia-chi/getByKhachHang/${id}`);
    addresses.value = addressRes.data.map(address => ({
      ...address,
      isEditing: false,
      availableDistricts: [],
      availableWards: [],
    })) || [];

    const defaultAddress = addresses.value.find(addr => addr.macDinh) || addresses.value[0] || {};

    custmerData.value = {
      id: data.id || "",
      email: data.idTaiKhoan?.email || "",
      soDienThoai: data.idTaiKhoan?.soDienThoai || "",
      ngaySinh: data.ngaySinh ? new Date(data.ngaySinh).toISOString().split("T")[0] : "",
      gioiTinh: data.idTaiKhoan?.deleted !== undefined ? String(data.idTaiKhoan.deleted) : "",
      ten: data.ten || "",
      diaChiCuThe: defaultAddress.diaChiCuThe || "",
      thanhPho: defaultAddress.thanhPho || "",
      quan: defaultAddress.quan || "",
      phuong: defaultAddress.phuong || "",
      anhKhachHang: data.anhKhachHang || "",
    };

    if (data.anhKhachHang) {
      if (data.anhKhachHang.startsWith("data:")) {
        customerImage.value = data.anhKhachHang;
      } else {
        customerImage.value = `http://localhost:8080${data.anhKhachHang}`;
      }
    } else {
      customerImage.value = null;
    }

    // Cập nhật danh sách quận/huyện và xã/phường cho từng địa chỉ
    for (const address of addresses.value) {
      await updateDistricts(address);
      await updateWards(address);
    }
  } catch (error) {
    console.error("Lỗi khi lấy dữ liệu khách hàng:", error);
    toastRef.value.kshowToast('error', 'Không thể tải dữ liệu khách hàng: ' + (error.response?.data?.error || error.message));
  }
};

const showConfirmUpdate = () => {
  if (!CheckUpdate()) {
    return;
  }
  confirmMessage.value = "Bạn có chắc chắn muốn cập nhật thông tin khách hàng này không?";
  confirmAction.value = "updateKhachHang";
  showConfirmModal.value = true;
};

const showConfirmDeleteAddress = (id) => {
  confirmMessage.value = "Bạn có chắc chắn muốn xóa địa chỉ này không?";
  confirmAction.value = () => deleteAddress(id);
  showConfirmModal.value = true;
};

const handleConfirm = async () => {
  if (confirmAction.value === "updateKhachHang") {
    await confirmUpdate();
  } else if (typeof confirmAction.value === "function") {
    await confirmAction.value();
  }
  showConfirmModal.value = false;
  confirmAction.value = null;
};

const cancelConfirm = () => {
  showConfirmModal.value = false;
  confirmAction.value = null;
};

const confirmUpdate = async () => {
  try {
    await updateKhachHang();
    toastRef.value.kshowToast('success', 'Cập nhật thông tin khách hàng thành công!');
  } catch (error) {
    toastRef.value.kshowToast('error', 'Cập nhật thất bại: ' + (error.response?.data?.error || error.message));
  }
};

function isOver15(birthDate) {
  const date = new Date(birthDate);
  const today = new Date();
  const age = today.getFullYear() - date.getFullYear();
  const isBirthdayPassed = today.getMonth() > date.getMonth() || (today.getMonth() === date.getMonth() && today.getDate() >= date.getDate());
  return age > 15 || (age === 15 && isBirthdayPassed);
}

function CheckUpdate() {
  const OnlyNumbers = /^\d+$/;
  const OnlyABC = /^[^\d]+$/;
  const EmailCheck = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  const PhoneCheck = /^(03|05|07|08|09)\d{8}$/;

  if (!custmerData.value.email.trim()) {
    toastRef.value.kshowToast('error', 'Email không được để trống!');
    return false;
  }
  if (!EmailCheck.test(custmerData.value.email.trim())) {
    toastRef.value.kshowToast('error', 'Vui lòng nhập email đúng định dạng!');
    return false;
  }
  if (!custmerData.value.ten.trim()) {
    toastRef.value.kshowToast('error', 'Tên khách hàng không được để trống!');
    return false;
  }
  if (!OnlyABC.test(custmerData.value.ten.trim())) {
    toastRef.value.kshowToast('error', 'Tên khách hàng không được chứa số!');
    return false;
  }
  if (!custmerData.value.soDienThoai.trim()) {
    toastRef.value.kshowToast('error', 'Số điện thoại không được để trống!');
    return false;
  }
  if (!PhoneCheck.test(custmerData.value.soDienThoai.trim())) {
    toastRef.value.kshowToast('error', 'Số điện thoại phải đúng định dạng Việt Nam!');
    return false;
  }
  if (!custmerData.value.ngaySinh.trim()) {
    toastRef.value.kshowToast('error', 'Ngày sinh không được để trống!');
    return false;
  }
  if (!isOver15(custmerData.value.ngaySinh.trim())) {
    toastRef.value.kshowToast('error', 'Khách hàng dưới 15 tuổi không thể tạo tài khoản!');
    return false;
  }
  if (!custmerData.value.gioiTinh.trim()) {
    toastRef.value.kshowToast('error', 'Vui lòng chọn giới tính!');
    return false;
  }
  return true;
}

const updateKhachHang = async () => {
  if (!CheckUpdate()) {
    return;
  }

  const updatedData = {
    email: custmerData.value.email,
    soDienThoai: custmerData.value.soDienThoai,
    ngaySinh: custmerData.value.ngaySinh,
    gioiTinh: custmerData.value.gioiTinh === "true",
    tenKH: custmerData.value.ten,
    anhKH: hasNewImage.value ? custmerData.value.anhKhachHang : custmerData.value.anhKhachHang || "",
  };

  await axios.put(`http://localhost:8080/khach-hang/update/${route.query.id}`, updatedData, {
    headers: { "Content-Type": "application/json" }
  });

  hasNewImage.value = false;
  await fetchCustomerData();
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
  newDistricts.value = [];
  newWards.value = [];
  newAddress.value.quan = "";
  newAddress.value.phuong = "";

  if (!newAddress.value.thanhPho) return;

  const province = provinces.value.find(prov => prov.name === newAddress.value.thanhPho);
  if (province && province.districts) {
    newDistricts.value = province.districts;
  }
};

const handleNewDistrictChange = () => {
  newWards.value = [];
  newAddress.value.phuong = "";

  if (!newAddress.value.quan) return;

  const district = newDistricts.value.find(dist => dist.name === newAddress.value.quan);
  if (district && district.wards) {
    newWards.value = district.wards;
  }
};

const addNewAddress = async () => {
  try {
    if (!route.query.id || isNaN(parseInt(route.query.id))) {
      toastRef.value.kshowToast('error', 'ID khách hàng không hợp lệ!');
      return;
    }
    if (!newAddress.value.diaChiCuThe || !newAddress.value.thanhPho || !newAddress.value.quan || !newAddress.value.phuong) {
      toastRef.value.kshowToast('error', 'Vui lòng nhập đầy đủ thông tin địa chỉ!');
      return;
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

    await axios.post(`http://localhost:8080/dia-chi/addDchi`, newData);
    toastRef.value.kshowToast('success', 'Thêm địa chỉ thành công!');
    toggleAddAddress();
    await fetchCustomerData();
    currentPage.value = totalPages.value;
  } catch (error) {
    console.error("Lỗi khi thêm địa chỉ:", error);
    toastRef.value.kshowToast('error', 'Có lỗi xảy ra khi thêm địa chỉ: ' + (error.response?.data?.error || error.message));
  }
};

const updateDistricts = async (address) => {
  address.availableDistricts = [];
  address.availableWards = [];
  address.quan = address.quan || "";
  address.phuong = address.phuong || "";

  if (!address.thanhPho) return;

  const province = provinces.value.find(prov => prov.name === address.thanhPho);
  if (province && province.districts) {
    address.availableDistricts = province.districts;
    // Nếu quan hiện tại không có trong danh sách mới, giữ nguyên để hiển thị
    if (address.quan && !address.availableDistricts.some(d => d.name === address.quan)) {
      address.availableDistricts.push({ code: address.quan, name: address.quan });
    }
  }
  await nextTick();
};

const updateWards = async (address) => {
  address.availableWards = [];
  address.phuong = address.phuong || "";

  if (!address.quan || !address.availableDistricts.length) return;

  const district = address.availableDistricts.find(dist => dist.name === address.quan);
  if (district && district.wards) {
    address.availableWards = district.wards;
    // Nếu phuong hiện tại không có trong danh sách mới, giữ nguyên để hiển thị
    if (address.phuong && !address.availableWards.some(w => w.name === address.phuong)) {
      address.availableWards.push({ code: address.phuong, name: address.phuong });
    }
  }
  await nextTick();
};

const setDefaultAddress = async (selectedAddress) => {
  try {
    addresses.value.forEach(address => {
      if (address.id !== selectedAddress.id) {
        address.macDinh = false;
      }
    });

    selectedAddress.macDinh = true;

    custmerData.value.diaChiCuThe = selectedAddress.diaChiCuThe;
    custmerData.value.thanhPho = selectedAddress.thanhPho;
    custmerData.value.quan = selectedAddress.quan;
    custmerData.value.phuong = selectedAddress.phuong;

    for (const address of addresses.value) {
      await axios.put(`http://localhost:8080/dia-chi/setDefault/${address.id}`, { macDinh: address.macDinh });
    }

    toastRef.value.kshowToast('success', 'Cập nhật địa chỉ mặc định thành công!');
    await fetchCustomerData();

    document.dispatchEvent(new CustomEvent('defaultAddressChanged', {
      detail: {
        id: custmerData.value.id,
        diaChiCuThe: selectedAddress.diaChiCuThe,
        thanhPho: selectedAddress.thanhPho,
        quan: selectedAddress.quan,
        phuong: selectedAddress.phuong,
      }
    }));
  } catch (error) {
    console.error("Lỗi khi cập nhật địa chỉ mặc định:", error);
    toastRef.value.kshowToast('error', 'Có lỗi xảy ra khi cập nhật địa chỉ mặc định: ' + (error.response?.data?.error || error.message));
    selectedAddress.macDinh = !selectedAddress.macDinh;
  }
};

const editAddress = (address) => {
  address.isEditing = true;
};

const saveAddress = async (address) => {
  try {
    if (!address.diaChiCuThe || !address.thanhPho || !address.quan || !address.phuong) {
      toastRef.value.kshowToast('error', 'Vui lòng nhập đầy đủ thông tin địa chỉ!');
      return;
    }

    const updatedData = {
      diaChiCuThe: address.diaChiCuThe,
      phuong: address.phuong,
      thanhPho: address.thanhPho,
      quan: address.quan,
      macDinh: address.macDinh,
    };

    await axios.put(`http://localhost:8080/dia-chi/updateDchi/${address.id}`, updatedData);
    address.isEditing = false;
    toastRef.value.kshowToast('success', 'Cập nhật địa chỉ thành công!');
    await fetchCustomerData();
  } catch (error) {
    console.error("Lỗi khi cập nhật địa chỉ:", error);
    toastRef.value.kshowToast('error', 'Có lỗi xảy ra khi cập nhật địa chỉ: ' + (error.response?.data?.error || error.message));
  }
};

const deleteAddress = async (id) => {
  try {
    await axios.delete(`http://localhost:8080/dia-chi/delete/${id}`);
    toastRef.value.kshowToast('success', 'Xóa địa chỉ thành công!');
    await fetchCustomerData();
    if (paginatedAddresses.value.length === 0 && currentPage.value > 1) {
      currentPage.value--;
    }
  } catch (error) {
    console.error("Lỗi khi xóa địa chỉ:", error);
    toastRef.value.kshowToast('error', 'Có lỗi xảy ra khi xóa địa chỉ: ' + (error.response?.data?.error || error.message));
  }
};

onMounted(async () => {
  await fetchProvinces();
  await fetchCustomerData();
});
</script>

<style scoped>
.switch {
  position: relative;
  display: inline-block;
  width: 60px;
  height: 30px;
}

.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #d1d5db;
  transition: 0.4s ease;
  border-radius: 30px;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.1);
}

.slider:before {
  position: absolute;
  content: "✕";
  height: 24px;
  width: 24px;
  left: 3px;
  bottom: 3px;
  background-color: #ffffff;
  transition: 0.4s ease;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  color: #6b7280;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

input:checked + .slider {
  background-color: #f97316;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.15);
}

input:checked + .slider:before {
  transform: translateX(30px);
  content: "✔";
  color: #ffffff;
  font-weight: bold;
}

.slider.round {
  border-radius: 30px;
}

.slider.round:before {
  border-radius: 50%;
}

.switch:hover .slider {
  background-color: #e5e7eb;
}

input:checked + .slider:hover {
  background-color: #fb923c;
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
</style>