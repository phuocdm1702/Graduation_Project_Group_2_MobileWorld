<template>
  <div>
    <!-- Breadcrumb -->
    <Breadcrumb breadcrumb="Phiếu Giảm Giá" />

    <div class="mt-8">
      <h4 class="text-gray-600">Danh sách Phiếu Giảm Giá</h4>

      <div class="bg-white p-4 rounded-md shadow-md flex flex-wrap gap-3 items-end">
        <!-- Các bộ lọc tìm kiếm -->
        <div class="relative w-1/4">
          <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-4.35-4.35m0 0A7.5 7.5 0 1011 3.5a7.5 7.5 0 005.65 13.15z"></path>
          </svg>
          <input type="text" v-model="searchQuery" placeholder="Tìm theo mã hoặc tên phiếu..." class="border px-10 py-2 rounded-md w-full transition-all duration-300 ease-in-out focus:ring-2 focus:ring-blue-400" />
        </div>

        <div class="relative w-1/6">
          <select v-model="filterType" class="border px-10 py-2 rounded-md w-full">
            <option value="Tất cả loại phiếu" selected>Tất cả loại phiếu</option>
            <option value="Phần trăm">Phần trăm</option>
            <option value="Tiền mặt">Tiền mặt</option>
          </select>
        </div>

        <div class="relative w-1/6">
          <select v-model="filterStatus" class="border px-10 py-2 rounded-md w-full">
            <option value="Tất cả trạng thái" selected>Tất cả trạng thái</option>
            <option value="Hoạt động">Còn hạn</option>
            <option value="Hết hạn">Hết hạn</option>
          </select>
        </div>

        <div class="relative">
          <input type="date" v-model="startDate" class="border px-10 py-2 rounded-md" />
        </div>

        <div class="relative">
          <input type="date" v-model="endDate" class="border px-10 py-2 rounded-md" />
        </div>

        <div class="relative w-1/6">
          <input type="number" v-model="minOrder" placeholder="Hóa đơn tối thiểu" class="border px-10 py-2 rounded-md w-full" />
        </div>

        <div class="relative w-1/6">
          <input type="number" v-model="valueFilter" placeholder="Giá trị phiếu" class="border px-10 py-2 rounded-md w-full" />
        </div>

        <!-- Nút tìm kiếm -->
        <button @click="searchPGG" class="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition-all">
          Tìm kiếm
        </button>
      </div>

      <div class="mt-4 flex justify-between items-center">
        <router-link to="/add-phieu-giam-gia" class="bg-blue-600 text-white px-4 py-2 rounded-md flex items-center hover:bg-blue-700 btn-modal">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/>
          </svg>
          Thêm Phiếu
        </router-link>
      </div>
      
      <div class="mt-4">
        <table class="w-full bg-white rounded-md shadow-md">
          <thead>
          <tr class="bg-gray-200 text-gray-700">
            <th class="px-4 py-2">Mã</th>
            <th class="px-4 py-2">Tên Phiếu</th>
            <th class="px-4 py-2">Loại Phiếu</th>
            <th class="px-4 py-2">Phần trăm giảm giá</th>
            <th class="px-4 py-2">Số tiền giảm tối đa</th>
            <th class="px-4 py-2">Số lượng</th>
            <th class="px-4 py-2">Hóa đơn tối thiểu</th>
            <th class="px-4 py-2">Ngày bắt đầu</th>
            <th class="px-4 py-2">Ngày kết thúc</th>
            <th class="px-4 py-2">Trạng thái</th>
            <th class="px-4 py-2">Riêng tư</th>
            <th class="px-4 py-2">Mô tả</th>
            <th class="px-4 py-2">Hành động</th>
          </tr>
          </thead>
          <tbody>
          <tr class="border-t text-center" v-for="voucher in vouchers" :key="voucher.id">
            <td class="px-4 py-2">{{ voucher.ma }}</td>
            <td class="px-4 py-2">{{ voucher?.tenPhieuGiamGia || "Không có dữ liệu" }}</td>
            <td class="px-4 py-2">{{ voucher.loaiPhieuGiamGia }}</td>
            <td class="px-4 py-2">{{ voucher.phanTramGiamGia * 100 }}%</td>
            <td class="px-4 py-2">{{ voucher.soTienGiamToiDa }}</td>
            <td class="px-4 py-2">{{ voucher.soLuongDung }}</td>
            <td class="px-4 py-2">{{ voucher.hoaDonToiThieu }}</td>
            <td class="px-4 py-2">{{ new Date(voucher.ngayBatDau).toLocaleDateString('vi-VN') }}</td>
            <td class="px-4 py-2">{{ new Date(voucher.ngayKetThuc).toLocaleDateString('vi-VN') }}</td>
            <td class="px-4 py-2">{{ voucher.trangThai == "1" ? "Còn hạn" : "Hết hạn" }}</td>
            <td class="px-4 py-2">{{ voucher.riengTu == "1" ? "Có" : "Không" }}</td>
            <td class="px-4 py-2">{{ voucher.moTa }}</td>
            <td class="px-4 py-2">
              <button @click="editPGG(voucher)" class="text-blue-600 hover:underline mr-2">Sửa</button>
              <button @click="deletePGG(voucher.id)" class="text-red-600 hover:underline">Xóa</button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- Modal chỉnh sửa -->
      <div v-if="isEditing" class="fixed inset-0 flex items-center justify-center bg-gray-800 bg-opacity-50">
        <div class="bg-white p-6 rounded-lg shadow-lg w-1/2">
          <h3 class="text-lg font-semibold mb-4">Chỉnh sửa Phiếu Giảm Giá</h3>

          <!-- Chia layout thành 2 cột -->
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-gray-700">Mã phiếu:</label>
              <input v-model="editingVoucher.ma" type="text" class="border px-3 py-2 rounded-md w-full" />
              <p v-if="errors.ma" class="text-red-500 text-sm">{{ errors.ma }}</p>
            </div>

            <div>
              <label class="block text-gray-700">Tên phiếu:</label>
              <input v-model="editingVoucher.tenPhieuGiamGia" type="text" class="border px-3 py-2 rounded-md w-full" />
              <p v-if="errors.tenPhieuGiamGia" class="text-red-500 text-sm">{{ errors.tenPhieuGiamGia }}</p>
            </div>

            <div>
              <label class="block text-gray-700">Loại phiếu:</label>
              <select v-model="editingVoucher.loaiPhieuGiamGia" class="border px-3 py-2 rounded-md w-full">
                <option value="Phần trăm">Phần trăm</option>
                <option value="Tiền mặt">Tiền mặt</option>
              </select>
              <p v-if="errors.loaiPhieuGiamGia" class="text-red-500 text-sm">{{ errors.loaiPhieuGiamGia }}</p>
            </div>

            <div>
              <label class="block text-gray-700">Phần trăm giảm giá:</label>
              <input v-model="editingVoucher.phanTramGiamGia" type="number" class="border px-3 py-2 rounded-md w-full" />
              <p v-if="errors.phanTramGiamGia" class="text-red-500 text-sm">{{ errors.phanTramGiamGia }}</p>
            </div>

            <div>
              <label class="block text-gray-700">Số tiền giảm tối đa:</label>
              <input v-model="editingVoucher.soTienGiamToiDa" type="number" class="border px-3 py-2 rounded-md w-full" />
              <p v-if="errors.soTienGiamToiDa" class="text-red-500 text-sm">{{ errors.soTienGiamToiDa }}</p>
            </div>

            <div>
              <label class="block text-gray-700">Số lượng:</label>
              <input v-model="editingVoucher.soLuongDung" type="number" class="border px-3 py-2 rounded-md w-full" />
              <p v-if="errors.soLuongDung" class="text-red-500 text-sm">{{ errors.soLuongDung }}</p>
            </div>

            <div>
              <label class="block text-gray-700">Hóa đơn tối thiểu:</label>
              <input v-model="editingVoucher.hoaDonToiThieu" type="number" class="border px-3 py-2 rounded-md w-full" />
              <p v-if="errors.hoaDonToiThieu" class="text-red-500 text-sm">{{ errors.hoaDonToiThieu }}</p>
            </div>

            <div>
              <label class="block text-gray-700">Ngày bắt đầu:</label>
              <input v-model="editingVoucher.ngayBatDau" type="date" class="border px-3 py-2 rounded-md w-full" />
              <p v-if="errors.ngayBatDau" class="text-red-500 text-sm">{{ errors.ngayBatDau }}</p>
            </div>

            <div>
              <label class="block text-gray-700">Ngày kết thúc:</label>
              <input v-model="editingVoucher.ngayKetThuc" type="date" class="border px-3 py-2 rounded-md w-full" />
              <p v-if="errors.ngayKetThuc" class="text-red-500 text-sm">{{ errors.ngayKetThuc }}</p>
            </div>
            
            <div>
              <label class="block text-gray-700">Trạng thái:</label>
              <div class="flex items-center gap-2">
                <input type="checkbox" id="trangThai" v-model="editingVoucher.trangThai" true-value="Còn hạn" false-value="Hết hạn" checked/>
                <label for="trangThai">Còn hạn</label>
              </div>
            </div>
            
          </div>
          
          <div class="mt-3">
            <label class="block text-gray-700">Mô tả:</label>
            <textarea v-model="editingVoucher.moTa" class="border px-3 py-2 rounded-md w-full"></textarea>
          </div>
          
          <div class="flex justify-end gap-3 mt-4">
            <button @click="isEditing = false" class="px-4 py-2 bg-gray-400 text-white rounded-md">Hủy</button>
            <button @click="updatePGG" class="px-4 py-2 bg-blue-600 text-white rounded-md">Cập nhật</button>
          </div>
        </div>
      </div>


    </div>
    
  </div>
</template>

<script setup>
import { onMounted,watch, ref } from "vue";
import AppVoucher from "@/views/Voucher/JS/AppVoucher"
import axios from "axios";

const baseURL = "http://localhost:8080/phieu-giam-gia";

const { vouchers, searchQuery, filterType, filterStatus, searchPGG, deletePGG, fetchDataPGG } = AppVoucher();
vouchers.value = [];
watch(searchQuery, (newQuery) => {
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

<style scoped>
table {
  width: 100%;
  border-collapse: collapse;
  font-size: 13px;
}

th, td {
  padding: 5px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

th {
  background-color: #f2f2f2;
}

tr:hover {
  background-color: #f5f5f5;
}


/*Thong bao*/
.toast {
  position: fixed;
  top: 5%;
  right: 20px;
  padding: 10px 20px;
  border-radius: 5px;
  color: white;
}
.success { background: green; }
.error { background: red; }
.info { background: blue; }
</style>



