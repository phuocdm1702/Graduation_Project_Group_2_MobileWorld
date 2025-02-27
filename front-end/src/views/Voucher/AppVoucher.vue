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
              <option value="Hoạt động">Hoạt động</option>
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
        <button @click="openModal" class="bg-blue-600 text-white px-4 py-2 rounded-md flex items-center hover:bg-blue-700 btn-modal">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/>
          </svg>
          Thêm Phiếu
        </button>
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
            <td class="px-4 py-2">{{ voucher.trangThai }}</td>
            <td class="px-4 py-2">{{ voucher.riengTu }}</td>
            <td class="px-4 py-2">{{ voucher.moTa }}</td>
            <td class="px-4 py-2">
              <button class="text-blue-600 hover:underline mr-2">Sửa</button>
              <button class="text-red-600 hover:underline">Xóa</button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    
    <div v-if="isModalOpen" class="mt-4 modal">
      <div class="mt-4 flex">
        <div
          class="w-full max-w-4xl overflow-hidden bg-white border rounded-md shadow-md"
        >
          <form>
            <div
              class="flex items-center justify-between px-5 py-3 text-gray-700 border-b"
            >
              <h3 class="text-sm">Thêm Phiếu Giảm Giá</h3>
              <button @click="closeModal">&times;</button>
            </div>

            <div class="p-5 text-gray-700 bg-gray-200 border-b grid grid-cols-3 gap-4">
              <div>
                <label class="text-xs">Mã</label>
                <input type="text" class="w-full px-4 py-2 mt-2 border rounded-md" v-model="ma" />
              </div>
              <div>
                <label class="text-xs">Tên Phiếu Giảm Giá</label>
                <input type="text" class="w-full px-4 py-2 mt-2 border rounded-md" v-model="tenPhieuGiamGia" />
              </div>
              <div>
                <label class="text-xs">Loại Phiếu</label>
                <div class="mt-2 flex items-center space-x-4">
                  <label class="flex items-center">
                    <input type="radio" name="voucherType" value="Phần trăm" v-model="loaiPhieuGiamGia" class="mr-2" />
                    Phần trăm
                  </label>
                  <label class="flex items-center">
                    <input type="radio" name="voucherType" value="Tiền mặt" v-model="loaiPhieuGiamGia" class="mr-2" />
                    Tiền mặt
                  </label>
                </div>
              </div>
              <div>
                <label class="text-xs">Phần trăm giảm giá</label>
                <input type="number" class="w-full px-4 py-2 mt-2 border rounded-md" v-model="phanTramGiamGia" />
              </div>
              <div>
                <label class="text-xs">Số tiền giảm tối đa</label>
                <input type="number" class="w-full px-4 py-2 mt-2 border rounded-md" v-model="soTienGiamToiDa" />
              </div>
              <div>
                <label class="text-xs">Hóa đơn tối thiểu</label>
                <input type="number" class="w-full px-4 py-2 mt-2 border rounded-md" v-model="hoaDonToiThieu" />
              </div>
              <div>
                <label class="text-xs">Số lượng sử dụng</label>
                <input type="number" class="w-full px-4 py-2 mt-2 border rounded-md" v-model="soLuongDung" />
              </div>
              <div>
                <label class="text-xs">Ngày bắt đầu</label>
                <input type="date" class="w-full px-4 py-2 mt-2 border rounded-md" v-model="ngayBatDau" />
              </div>
              <div>
                <label class="text-xs">Ngày kết thúc</label>
                <input type="date" class="w-full px-4 py-2 mt-2 border rounded-md" v-model="ngayKetThuc" />
              </div>
              <div>
                <label class="text-xs">Trạng thái</label>
                <input type="checkbox" v-model="trangThai" />
              </div>
              <div>
                <label class="text-xs">Riêng tư</label>
                <input type="checkbox" v-model="riengTu" />
              </div>
              <div>
                <label class="text-xs">Mô tả</label>
                <textarea class="w-full px-4 py-2 mt-2 border rounded-md" v-model="moTa"></textarea>
              </div>
            </div>
            <div class="px-5 py-3 flex justify-between">
              <button
                @click="closeModal"
                class="px-3 py-1 text-sm text-gray-700 bg-gray-200 rounded-md hover:bg-gray-300"
              >
                Hủy
              </button>
              <button
                @click="submitForm"
                class="px-3 py-1 text-sm text-white bg-indigo-600 rounded-md hover:bg-indigo-500"
              >
                Thêm
              </button>
            </div>


            <div
              v-if="riengTu"
              class="ml-4 bg-white border rounded-md shadow-md p-4 customer-table-container"
            >
              <h4 class="text-gray-600 mb-2">Chọn Khách Hàng</h4>
              <table class="w-full bg-white rounded-md shadow-md">
                <thead>
                <tr class="bg-gray-200 text-gray-700">
                  <th class="px-4 py-2">Chọn</th>
                  <th class="px-4 py-2">Mã KH</th>
                  <th class="px-4 py-2">Tên Khách Hàng</th>
                  <th class="px-4 py-2">Ngày sinh</th>
                </tr>
                </thead>
                <tbody>
                <tr
                  v-for="customer in customers"
                  :key="customer.id"
                  class="border-t text-center"
                >
                  <td class="px-4 py-2">
                    <input
                      type="checkbox"
                      v-model="selectedCustomers"
                      :value="customer.id"
                    />
                  </td>
                  <td class="px-4 py-2">{{ customer.ma }}</td>
                  <td class="px-4 py-2">{{ customer.ten }}</td>
                  <td class="px-4 py-2">{{ customer.ngaySinh }}</td>
                </tr>
                </tbody>
              </table>
            </div>
          </form>
        </div>
      </div>
    </div>
    
    <div v-if="isModalOpen" class="overlay"></div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import axios from "axios";
import PhieuGGService from "@/MobieWorldService/PhieuGiamGiaService/PhieuGGService.js"
import KhachHangService from "@/MobieWorldService/KhachHangService/KhachHangService.js"
const vouchers = ref([]);
const customers = ref([]);

const selectedCustomers = ref([]);

const isModalOpen = ref(false);

const openModal = () => {
  isModalOpen.value = true;
};

const closeModal = () => {
  isModalOpen.value = false;
};

// Load data
const fetchDataPGG = async () => {
  try {
    const response = await PhieuGGService.getData();
    const responseKH = await KhachHangService.getData();
    vouchers.value = response.data;
    customers.value = responseKH.data;
  } catch (error) {
    console.log("Error data");
  }
};
onMounted(fetchDataPGG);

// Add data
const ma = ref("");
const tenPhieuGiamGia = ref("");
const loaiPhieuGiamGia = ref("Chung");
const phanTramGiamGia = ref("");
const soTienGiamToiDa = ref("");
const hoaDonToiThieu = ref("");
const soLuongDung = ref("");
const ngayBatDau = ref("");
const ngayKetThuc = ref("");
const trangThai = ref(false); // Boolean
const riengTu = ref(false); // Boolean
const moTa = ref("");
const deleted = ref(false); // Mặc định là false

const submitForm = async () => {
  const newPgg = {
    ma: ma.value,
    tenPhieuGiamGia: tenPhieuGiamGia.value,
    loaiPhieuGiamGia: loaiPhieuGiamGia.value,
    phanTramGiamGia: phanTramGiamGia.value,
    soTienGiamToiDa: soTienGiamToiDa.value,
    hoaDonToiThieu: hoaDonToiThieu.value,
    soLuongDung: soLuongDung.value,
    ngayBatDau: ngayBatDau.value ? new Date(ngayBatDau.value).toISOString() : null,
    ngayKetThuc: ngayKetThuc.value ? new Date(ngayKetThuc.value).toISOString() : null,
    trangThai: trangThai.value ? 1 : 0, // Convert Boolean to Int
    riengTu: riengTu.value ? 1 : 0, // Convert Boolean to Int
    moTa: moTa.value,
    deleted: deleted.value,
  };

  console.log("Dữ liệu gửi lên:", newPgg);

  try {
    const response = await PhieuGGService.createPgg(newPgg);
    console.log("Response: ", response);
    alert("Thêm phiếu giảm giá thành công!");
    closeModal();
    fetchDataPGG();
  } catch (error) {
    console.error("Lỗi khi thêm phiếu giảm giá:", error);
    alert("Thêm thất bại, vui lòng kiểm tra lại!");
  }
};


</script>

<style src="@/assets/VoucherCss/voucher.css"></style>



