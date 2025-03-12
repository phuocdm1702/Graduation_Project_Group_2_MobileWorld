<template>
  <div>
    <!-- Breadcrumb -->
    <Breadcrumb breadcrumb="Phiếu Giảm Giá" />

    <div class="mt-2 mx-auto">
      <h2 class="bg-white shadow-lg rounded-lg p-5 mb-2 mt-2 text-2xl font-semibold text-gray-700">
        Quản Lý Phiếu Giảm Giá
      </h2>

      <!-- Form lọc -->
      <div
        class="bg-white shadow-lg rounded-lg p-5 mb-4 mt-4 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">

        <!-- Ô tìm kiếm -->
        <div>
          <label for="searchQuery" class="block text-sm font-medium text-gray-700 mb-1">Tìm kiếm phiếu</label>
          <input
            v-model="searchQuery"
            id="searchQuery"
            type="text"
            placeholder="Tìm theo mã hoặc tên phiếu..."
            class="input-field"
          />
        </div>

        <!-- Dropdown chọn loại phiếu -->
        <div>
          <label for="filterType" class="block text-sm font-medium text-gray-700 mb-1">Loại phiếu</label>
          <select
            v-model="filterType"
            id="filterType"
            @change="filterPGG"
            class="input-field"
          >
            <option value="">Tất cả loại phiếu</option>
            <option value="Phần trăm">Phần trăm</option>
            <option value="Tiền mặt">Tiền mặt</option>
          </select>
        </div>

        <!-- Dropdown chọn trạng thái -->
        <div>
          <label for="filterStatus" class="block text-sm font-medium text-gray-700 mb-1">Trạng thái</label>
          <select
            v-model="filterStatus"
            id="filterStatus"
            @change="filterPGG"
            class="input-field"
          >
            <option value="">Tất cả trạng thái</option>
            <option value="Hoạt động">Còn hạn</option>
            <option value="Hết hạn">Hết hạn</option>
          </select>
        </div>

        <!-- Chọn ngày bắt đầu -->
        <div>
          <label for="startDate" class="block text-sm font-medium text-gray-700 mb-1">Ngày bắt đầu</label>
          <input
            v-model="startDate"
            id="startDate"
            type="date"
            class="input-field"
          />
        </div>

        <!-- Chọn ngày kết thúc -->
        <div>
          <label for="endDate" class="block text-sm font-medium text-gray-700 mb-1">Ngày kết thúc</label>
          <input
            v-model="endDate"
            id="endDate"
            type="date"
            class="input-field"
          />
        </div>

        <!-- Ô nhập hóa đơn tối thiểu -->
        <div>
          <label for="minOrder" class="block text-sm font-medium text-gray-700 mb-1">Hóa đơn tối thiểu</label>
          <input
            v-model="minOrder"
            id="minOrder"
            type="number"
            placeholder="Hóa đơn tối thiểu"
            class="input-field"
          />
        </div>

        <!-- Ô nhập giá trị phiếu -->
        <div>
          <label for="valueFilter" class="block text-sm font-medium text-gray-700 mb-1">Giá trị phiếu</label>
          <input
            v-model="valueFilter"
            id="valueFilter"
            type="number"
            placeholder="Giá trị phiếu"
            class="input-field"
          />
        </div>

        <!-- Nút tìm kiếm và thêm phiếu -->
        <div class="flex justify-end w-full col-span-full gap-2">
          <!-- Button Thêm Phiếu -->
          <router-link to="/add-phieu-giam-gia">
            <button
              class="flex items-center gap-2 px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition"
            >
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5 mr-1">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15"/>
              </svg>
              Thêm Phiếu
            </button>
          </router-link>
        </div>
      </div>

      <!-- Bảng danh sách phiếu giảm giá -->
      <DynamicTable
        class="dynamic-table"
        :data="vouchers"
        :columns="columns"
        :get-nested-value="getNestedValue"
      />

      <!-- Phân trang -->
      <footer class="bg-white shadow-lg rounded-lg p-4 flex justify-center items-center mt-2">
        <Pagination
          :current-page="currentPage"
          :total-pages="totalPages"
          @page-changed="goToPage"
        />
      </footer>
    </div>

    <!-- Modal chỉnh sửa -->
    <div v-if="isEditing" class="fixed inset-0 flex items-center justify-center bg-gray-800 bg-opacity-50">
      <div class="bg-white p-4 rounded-lg shadow-lg w-[100%] max-w-md mx-2">
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
          <button @click="updatePGG" class="px-3 py-1 bg-[#f97316] text-white rounded-md text-base hover:bg-orange-600 transition">Cập nhật</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.input-field {
  @apply w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-400 outline-none;
}
</style>

<script setup>
import { onMounted,watch, ref } from "vue";
import AppVoucher from "@/views/Voucher/JS/AppVoucher"
import DynamicTable from "@/components/DynamicTable.vue";
import ToggleSwitch from "@/components/ToggleSwitch.vue";
import Pagination from '@/components/Pagination.vue';
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





