<template>
  <div>
    <!-- Breadcrumb -->
    <Breadcrumb breadcrumb="Phiếu Giảm Giá" />

    


    <div class="mt-8">
      <h4 class="text-gray-600">Danh sách Phiếu Giảm Giá</h4>
      
        <div class="bg-white p-4 rounded-md shadow-md flex flex-wrap gap-3">
          <!-- Tìm kiếm -->
          <div class="relative w-1/4">
            <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-4.35-4.35m0 0A7.5 7.5 0 1011 3.5a7.5 7.5 0 005.65 13.15z"></path>
            </svg>
            <input
              type="text"
              v-model="searchQuery"
              placeholder="Tìm theo mã hoặc tên phiếu..."
              class="border px-10 py-2 rounded-md w-full"
            />
          </div>

          <!-- Loại Phiếu -->
          <div class="relative w-1/6">
            <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 10l7-7m0 0l7 7m-7-7v18"></path>
            </svg>
            <select v-model="filterType" class="border px-10 py-2 rounded-md w-full">
              <option value="">Tất cả loại phiếu</option>
              <option value="Phần trăm">Phần trăm</option>
              <option value="Tiền mặt">Tiền mặt</option>
            </select>
          </div>

          <!-- Trạng thái -->
          <div class="relative w-1/6">
            <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m0 0l-4 4m4-4V3m0 9V3"></path>
            </svg>
            <select v-model="filterStatus" class="border px-10 py-2 rounded-md w-full">
              <option value="">Tất cả trạng thái</option>
              <option value="Hoạt động">Còn hạn</option>
              <option value="Hết hạn">Hết hạn</option>
            </select>
          </div>

          <!-- Ngày bắt đầu -->
          <div class="relative">
            <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10m-10 4h7m-7 4h4"></path>
            </svg>
            <input type="date" v-model="startDate" class="border px-10 py-2 rounded-md" />
          </div>

          <!-- Ngày kết thúc -->
          <div class="relative">
            <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10m-10 4h7m-7 4h4"></path>
            </svg>
            <input type="date" v-model="endDate" class="border px-10 py-2 rounded-md" />
          </div>

          <!-- Hóa đơn tối thiểu -->
          <div class="relative w-1/6">
            <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 10l7-7m0 0l7 7m-7-7v18"></path>
            </svg>
            <input type="number" v-model="minOrder" placeholder="Hóa đơn tối thiểu" class="border px-10 py-2 rounded-md w-full" />
          </div>

          <!-- Giá trị phiếu -->
          <div class="relative w-1/6">
            <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8V4m0 0l-4 4m4-4l4 4m0 0V20"></path>
            </svg>
            <input type="number" v-model="valueFilter" placeholder="Giá trị phiếu" class="border px-10 py-2 rounded-md w-full" />
          </div>
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
            <td class="px-4 py-2">{{ voucher.tenPhieuGiamGia }}</td>
            <td class="px-4 py-2">{{ voucher.loaiPhieuGiamGia }}</td>
            <td class="px-4 py-2">{{ voucher.phanTramGiamGia }}%</td>
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

          <div class="mb-3">
            <label class="block text-gray-700">Tên phiếu:</label>
            <input v-model="editingVoucher.tenPhieuGiamGia" type="text" class="border px-3 py-2 rounded-md w-full" />
          </div>

          <div class="mb-3">
            <label class="block text-gray-700">Loại phiếu:</label>
            <select v-model="editingVoucher.loaiPhieuGiamGia" class="border px-3 py-2 rounded-md w-full">
              <option value="Phần trăm">Phần trăm</option>
              <option value="Tiền mặt">Tiền mặt</option>
            </select>
          </div>

          <div class="mb-3">
            <label class="block text-gray-700">Số tiền giảm tối đa:</label>
            <input v-model="editingVoucher.soTienGiamToiDa" type="number" class="border px-3 py-2 rounded-md w-full" />
          </div>

          <div class="mb-3">
            <label class="block text-gray-700">Hóa đơn tối thiểu:</label>
            <input v-model="editingVoucher.hoaDonToiThieu" type="number" class="border px-3 py-2 rounded-md w-full" />
          </div>

          <div class="mb-3">
            <label class="block text-gray-700">Ngày bắt đầu:</label>
            <input v-model="editingVoucher.ngayBatDau" type="date" class="border px-3 py-2 rounded-md w-full" />
          </div>

          <div class="mb-3">
            <label class="block text-gray-700">Ngày kết thúc:</label>
            <input v-model="editingVoucher.ngayKetThuc" type="date" class="border px-3 py-2 rounded-md w-full" />
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
import { ref, onMounted } from "vue";
import axios from "axios";
const vouchers = ref([]);
const isEditing = ref(false);
const editingVoucher = ref({});

const visible = ref(false);
const message = ref("");
const type = ref("success");

const showToast = (toastType, msg) => {
  message.value = msg;
  type.value = toastType;
  visible.value = true;
  setTimeout(() => {
    visible.value = false;
  }, 3000);
};

// Load data
const fetchDataPGG = async () => {
  try {
    const response = await axios.get("http://localhost:8080/phieu-giam-gia/data");
    vouchers.value = response.data;
  } catch (error) {
    console.log("Error data");
  }
};

const deletePGG = async (id) => {
  try {
    await axios.delete(`http://localhost:8080/phieu-giam-gia/delete/${id}`);
    showToast("success", "Xóa phiếu giảm giá thành công!");
    await fetchDataPGG();
  } catch(error) {
    console.log("Lỗi khi xóa mềm:", error);
    showToast("error", "Xóa thất bại, vui lòng kiểm tra lại!");
  }
}

const editPGG = (voucher) => {
  editingVoucher.value = { ...voucher }; 
  isEditing.value = true;
};

const updatePGG = async () => {
  try {
    await axios.put(`http://localhost:8080/phieu-giam-gia/update/${editingVoucher.value.id}`, editingVoucher.value);
    showToast("success", "Cập nhật thành công!");
    isEditing.value = false;
    await fetchDataPGG(); 
  } catch (error) {
    showToast("error", "Cập nhật thất bại!");
  }
};

onMounted(fetchDataPGG);

</script>

<style>
table {
  width: 100%;
  border-collapse: collapse;
  font-size: 12px;
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
</style>



