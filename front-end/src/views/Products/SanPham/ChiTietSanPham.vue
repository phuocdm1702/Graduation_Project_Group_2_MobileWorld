<template>
  <div>
    <!-- Breadcrumb -->
    <Breadcrumb breadcrumb="Chi Tiết Sản Phẩm" />

    <div class="mt-4">
      <h4 class="text-gray-600">Chi Tiết Sản Phẩm</h4>

      <div class="mt-4 flex justify-center">
        <div class="w-[95%] max-w-[95%] bg-white border rounded-md shadow-md">
          <form>
            <div class="flex items-center justify-between px-5 py-3 text-gray-700 border-b">
              <h3 class="text-sm">Thêm Chi Tiết Sản Phẩm</h3>
              <button>
                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>

            <div class="p-5 text-gray-700 bg-gray-200 border-b grid grid-cols-2 gap-8 px-8">
              <div v-for="field in productFields" :key="field.id">
                <label class="text-xs">{{ field.label }}</label>
                <template v-if="field.type === 'select'">
                  <select class="w-full px-4 py-2 mt-2 border rounded-md">
                    <option v-for="option in field.options" :key="option" :value="option">{{ option }}</option>
                  </select>
                </template>
                <template v-else>
                  <input type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
                </template>
              </div>
            </div>

            <div class="px-5 py-3 flex justify-between">
              <button class="px-3 py-1 text-sm text-gray-700 bg-gray-200 rounded-md hover:bg-gray-300">Hủy</button>
              <button class="px-3 py-1 text-sm text-white bg-indigo-600 rounded-md hover:bg-indigo-500">Lưu</button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <div class="mt-8">
      <h4 class="text-gray-600">Danh sách Chi Tiết Sản Phẩm</h4>
      <div class="mt-4 table-container" >
        <table class="w-full bg-white rounded-md shadow-md table">
          <thead>
            <tr class="bg-gray-200 text-gray-700">
              <th v-for="field in productFields" :key="field.id" class="px-4 py-2">{{ field.label }}</th>
              <th class="px-4 py-2">Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr class="border-t text-center" v-for="product in productDetails" :key="product.id">
              <td v-for="field in productFields" :key="field.id" class="px-4 py-2">{{ product[field.id] }}</td>
              <td class="px-4 py-2">
                <button class="text-blue-600 hover:underline mr-2">Sửa</button>
                <button class="text-red-600 hover:underline">Xóa</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";

// Danh sách các trường dữ liệu sản phẩm
const productFields = ref([
  { id: "id", label: "ID", type: "text" },
  { id: "id_imel", label: "ID Imel", type: "text" },
  { id: "id_anh_sp", label: "ID Ảnh Sản Phẩm", type: "select", options: ["Ảnh 1", "Ảnh 2", "Ảnh 3"] },
  { id: "id_nha_sx", label: "ID Nhà Sản Xuất", type: "select", options: ["Apple", "Samsung", "Xiaomi"] },
  { id: "id_dong_sp", label: "ID Dòng Sản Phẩm", type: "select", options: ["iPhone 15", "Galaxy S24", "Xiaomi 14"] },
  { id: "id_mau_sac", label: "ID Màu Sắc", type: "select", options: ["Đen", "Trắng", "Xanh"] },
  { id: "id_pin", label: "ID Pin", type: "select", options: ["4500mAh", "5000mAh", "6000mAh"] },
  { id: "id_man_hinh", label: "ID Màn Hình", type: "select", options: ["AMOLED", "OLED", "LCD"] },
  { id: "id_ram", label: "ID RAM", type: "select", options: ["8GB", "12GB", "16GB"] },
  { id: "id_bo_nho", label: "ID Bộ Nhớ Trong", type: "select", options: ["128GB", "256GB", "512GB"] },
  { id: "id_ho_tro_bn", label: "ID Hỗ Trợ Bộ Nhớ Ngoài", type: "select", options: ["Có", "Không"] },
  { id: "id_cpu", label: "ID CPU", type: "text" },
  { id: "id_gpu", label: "ID GPU", type: "text" },
  { id: "id_camera", label: "ID Cụm Camera", type: "select", options: ["Camera kép", "Camera đơn", "Camera 3 ống kính"] },
  { id: "id_he_dieu_hanh", label: "ID Hệ Điều Hành", type: "select", options: ["iOS", "Android"] },
  { id: "id_khang_bui", label: "ID Chỉ Số Kháng Bụi Kháng Nước", type: "select", options: ["IP68", "IP67", "IPX5"] },
  { id: "id_thiet_ke", label: "ID Thiết Kế", type: "select", options: ["Nhôm", "Kính", "Nhựa"] },
  { id: "id_sim", label: "ID Sim", type: "select", options: ["1 Sim", "2 Sim"] },
  { id: "id_sac", label: "ID Công Nghệ Sạc", type: "select", options: ["Fast Charge", "Wireless Charging"] },
  { id: "id_ho_tro_sac", label: "ID Hỗ Trợ Công Nghệ Sạc", type: "select", options: ["Có", "Không"] },
  { id: "id_cong_nghe_mang", label: "ID Công Nghệ Mạng", type: "select", options: ["5G", "4G"] },
  { id: "id_loai_tinh_trang", label: "ID Loại Tình Trạng", type: "select", options: ["Mới", "Đã sử dụng"] },
  { id: "tien_ich_dac_biet", label: "Tiện Ích Đặc Biệt", type: "text" },
  { id: "gia_ban", label: "Giá Bán", type: "text" }
]);

// Dữ liệu mẫu cho danh sách sản phẩm
const productDetails = ref([
  {
    id: "P001",
    id_imel: "123456789",
    id_anh_sp: "Ảnh 1",
    id_nha_sx: "Apple",
    id_dong_sp: "iPhone 15",
    id_mau_sac: "Đen",
    id_pin: "4500mAh",
    id_man_hinh: "OLED",
    id_ram: "8GB",
    id_bo_nho: "256GB",
    id_ho_tro_bn: "Không",
    id_cpu: "Apple A17",
    id_gpu: "Apple GPU",
    id_camera: "Camera kép",
    id_he_dieu_hanh: "iOS",
    id_khang_bui: "IP68",
    id_thiet_ke: "Nhôm",
    id_sim: "2 Sim",
    id_sac: "Fast Charge",
    id_ho_tro_sac: "Có",
    id_cong_nghe_mang: "5G",
    id_loai_tinh_trang: "Mới",
    tien_ich_dac_biet: "Face ID",
    gia_ban: "25,000,000 VND"
  }
]);
</script>

<style>
.table-container {
    display: flex;
    justify-content: center; 
    padding: 20px; 
}

.table {
    width: 80%;
    border-collapse: collapse;
    font-size: 15px;
}

th, td {
  white-space: nowrap;
  padding: 3px;
}
</style>
