<template>
  <div>
    <!-- Breadcrumb -->
    <Breadcrumb breadcrumb="Phiếu Giảm Giá" />

    <div class="mt-8">
      <h4 class="text-gray-600 text-lg">Danh sách Phiếu Giảm Giá</h4>

      <div class="bg-white p-4 rounded-md shadow-md flex flex-wrap gap-3 items-end mx-2">
        <!-- Các bộ lọc tìm kiếm -->
        <div class="relative w-1/4">
          <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-4.35-4.35m0 0A7.5 7.5 0 1011 3.5a7.5 7.5 0 005.65 13.15z"></path>
          </svg>
          <input type="text" v-model="searchQuery" placeholder="Tìm theo mã hoặc tên phiếu..."
                 class="border px-10 py-2 rounded-md w-full transition-all duration-300 ease-in-out focus:ring-2 focus:ring-blue-400 text-base" />
        </div>

        <div class="relative w-1/6">
          <select v-model="filterType" @change="filterPGG" class="border px-10 py-2 rounded-md w-full text-base">
            <option value="Tất cả loại phiếu" selected>Tất cả loại phiếu</option>
            <option value="Phần trăm">Phần trăm</option>
            <option value="Tiền mặt">Tiền mặt</option>
          </select>
        </div>

        <div class="relative w-1/6">
          <select v-model="filterStatus" @change="filterPGG" class="border px-10 py-2 rounded-md w-full text-base">
            <option value="Tất cả trạng thái" selected>Tất cả trạng thái</option>
            <option value="Hoạt động">Còn hạn</option>
            <option value="Hết hạn">Hết hạn</option>
          </select>
        </div>

        <div class="relative">
          <input type="date" v-model="startDate" class="border px-10 py-2 rounded-md text-base" />
        </div>

        <div class="relative">
          <input type="date" v-model="endDate" class="border px-10 py-2 rounded-md text-base" />
        </div>

        <div class="relative w-1/6">
          <input type="number" v-model="minOrder" placeholder="Hóa đơn tối thiểu" class="border px-10 py-2 rounded-md w-full text-base" />
        </div>

        <div class="relative w-1/6">
          <input type="number" v-model="valueFilter" placeholder="Giá trị phiếu" class="border px-10 py-2 rounded-md w-full text-base" />
        </div>

        <!-- Nút tìm kiếm -->
        <button @click="searchPGG" class="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition-all text-base">
          Tìm kiếm
        </button>
      </div>

      <div class="mt-4 flex justify-between items-center mx-2">
        <router-link to="/add-phieu-giam-gia" class="bg-blue-600 text-white px-4 py-2 rounded-md flex items-center hover:bg-blue-700 btn-modal text-base">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/>
          </svg>
          Thêm Phiếu
        </router-link>
      </div>

      <DynamicTable 
        :data="vouchers" 
        :columns="columns" 
        :getNestedValue="getNestedValue" 
      />
      
     

      <!-- Modal chỉnh sửa -->
      <div v-if="isEditing" class="fixed inset-0 flex items-center justify-center bg-gray-800 bg-opacity-50">
        <div class="bg-white p-4 rounded-lg shadow-lg w-[80%] max-w-md mx-2">
          <h3 class="text-lg font-semibold mb-3 text-base">Chỉnh sửa Phiếu Giảm Giá</h3>

          <div class="grid grid-cols-1 gap-3">
            <div>
              <label class="block text-gray-700 text-base">Mã phiếu:</label>
              <input v-model="editingVoucher.ma" type="text" class="border px-2 py-1 rounded-md w-full text-base" />
            </div>

            <div>
              <label class="block text-gray-700 text-base">Tên phiếu:</label>
              <input v-model="editingVoucher.tenPhieuGiamGia" type="text" class="border px-2 py-1 rounded-md w-full text-base" />
            </div>

            <div>
              <label class="block text-gray-700 text-base">Loại phiếu:</label>
              <select v-model="editingVoucher.loaiPhieuGiamGia" class="border px-2 py-1 rounded-md w-full text-base">
                <option value="Phần trăm">Phần trăm</option>
                <option value="Tiền mặt">Tiền mặt</option>
              </select>
            </div>

            <div>
              <label class="block text-gray-700 text-base">Phần trăm giảm giá (%):</label>
              <input v-model="editingVoucher.phanTramGiamGia" type="number" class="border px-2 py-1 rounded-md w-full text-base" />
            </div>

            <div>
              <label class="block text-gray-700 text-base">Ngày bắt đầu:</label>
              <input v-model="editingVoucher.ngayBatDau" type="date" class="border px-2 py-1 rounded-md w-full text-base" />
            </div>

            <div>
              <label class="block text-gray-700 text-base">Ngày kết thúc:</label>
              <input v-model="editingVoucher.ngayKetThuc" type="date" class="border px-2 py-1 rounded-md w-full text-base" />
            </div>

            
          </div>

          <div class="flex justify-end gap-2 mt-4">
            <button @click="isEditing = false" class="px-3 py-1 bg-gray-400 text-white rounded-md text-base">Hủy</button>
            <button @click="updatePGG" class="px-3 py-1 bg-blue-600 text-white rounded-md text-base">Cập nhật</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>



<script setup>
import { onMounted,watch, ref } from "vue";
import AppVoucher from "@/views/Voucher/JS/AppVoucher"
import DynamicTable from "@/components/DynamicTable.vue";
import ToggleSwitch from "@/components/ToggleSwitch.vue";
import axios from "axios";

const baseURL = "http://localhost:8080/phieu-giam-gia";

const { vouchers, searchQuery, filteredVouchers, searchPGG, deletePGG, fetchDataPGG } = AppVoucher();
const columns = ref([
  { key: "ma", label: "Mã" },
  { key: "tenPhieuGiamGia", label: "Tên Phiếu" },
  { key: "loaiPhieuGiamGia", label: "Loại Phiếu" },
  { key: "phanTramGiamGia", label: "Phần trăm giảm giá", formatter: (value) => `${value * 100}%` },
  { key: "soTienGiamToiDa", label: "Số tiền giảm tối đa" },
  { key: "soLuongDung", label: "Số lượng" },
  { key: "hoaDonToiThieu", label: "Hóa đơn tối thiểu" },
  { key: "ngayBatDau", label: "Ngày bắt đầu", formatter: (value) => new Date(value).toLocaleDateString("vi-VN") },
  { key: "ngayKetThuc", label: "Ngày kết thúc", formatter: (value) => new Date(value).toLocaleDateString("vi-VN") },
  { key: "moTa", label: "Mô tả" },
  { key: "trangThai", label: "Trạng thái", cellSlot: "trangThaiSlot"},
  
]);



const getNestedValue = (obj, key) => {
  return key.split(".").reduce((o, k) => (o && o[k] !== undefined ? o[k] : null), obj);
};
watch(searchQuery, (newQuery) =>  {
  if(newQuery.trim().length > 0) {
    searchPGG();
  }
});

onMounted(fetchDataPGG);

const isEditing = ref(false);
const editingVoucher = ref({});
const errors = ref({});

const validateForm = () => {
  errors.value = {}; // Reset lỗi

  const { ma, tenPhieuGiamGia, loaiPhieuGiamGia, phanTramGiamGia, soTienGiamToiDa, soLuongDung, hoaDonToiThieu, ngayBatDau, ngayKetThuc } = editingVoucher.value;
  
  if (!ma) errors.value.ma = "Mã phiếu không được để trống";
  if (!tenPhieuGiamGia) errors.value.tenPhieuGiamGia = "Tên phiếu không được để trống";
  if (!loaiPhieuGiamGia) errors.value.loaiPhieuGiamGia = "Vui lòng chọn loại phiếu";
  
  if (phanTramGiamGia < 0 || phanTramGiamGia > 100) errors.value.phanTramGiamGia = "Phần trăm giảm giá phải từ 0 đến 100";
  if (soTienGiamToiDa < 0) errors.value.soTienGiamToiDa = "Số tiền giảm không hợp lệ";
  if (soLuongDung < 1) errors.value.soLuongDung = "Số lượng phải lớn hơn 0";
  if (hoaDonToiThieu < 0) errors.value.hoaDonToiThieu = "Hóa đơn tối thiểu không hợp lệ";
  
  if (!ngayBatDau) errors.value.ngayBatDau = "Vui lòng chọn ngày bắt đầu";
  if (!ngayKetThuc) errors.value.ngayKetThuc = "Vui lòng chọn ngày kết thúc";
  if (ngayBatDau && ngayKetThuc && ngayBatDau > ngayKetThuc) {
    errors.value.ngayKetThuc = "Ngày kết thúc phải sau ngày bắt đầu";
  }

  return Object.keys(errors.value).length === 0; 
};



const editPGG = (voucher) => {
  editingVoucher.value = { ...voucher };
  editingVoucher.value.ngayBatDau = new Date(editingVoucher.value.ngayBatDau).toISOString().split("T")[0];
  editingVoucher.value.ngayKetThuc = new Date(editingVoucher.value.ngayKetThuc).toISOString().split("T")[0];
  isEditing.value = true;
};

const updatePGG = async () => {
  if(!validateForm()) {
    return;
  }
  try {
    await axios.put(`${baseURL}/update/${editingVoucher.value.id}`, editingVoucher.value);
    isEditing.value = false;
    await fetchDataPGG();
  } catch (error) {
    console.log("Cập nhật thất bại!", error);
  }
};

</script>





