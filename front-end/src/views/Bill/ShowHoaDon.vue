<template>
  <!-- Sử dụng BreadcrumbWrapper thay vì PathRouter trực tiếp -->
  <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />
  <div class="show-hoa-don bg-white shadow-lg rounded-lg p-5 mt-2 mx-auto">
    <!-- Thanh trạng thái -->
    <div class="timeline mb-6">
      <div class="timeline-container flex items-center justify-between relative">
        <div v-for="(status, index) in timelineStatuses" :key="index" class="timeline-item flex-1 text-center">
          <div class="relative">
            <div
              :class="['w-10 h-10 rounded-full mx-auto flex items-center justify-center', status.completed ? 'bg-green-500 text-white' : 'bg-gray-300 text-gray-700']">
              <span class="text-sm">{{ index + 1 }}</span>
            </div>
            <div v-if="index < timelineStatuses.length - 1" class="absolute top-5 left-1/2 w-full h-1 bg-gray-300"
                 :class="{ 'bg-green-500': status.completed }"></div>
          </div>
          <p class="mt-2 text-sm font-medium text-gray-700">{{ status.name }}</p>
          <p class="text-xs text-gray-500">{{ status.time || "N/A" }}</p>
        </div>
      </div>
    </div>

    <!-- Thông tin đơn hàng -->
    <div class="bg-gray-50 p-6 rounded-xl mb-6 shadow-sm">
      <h3 class="text-xl font-semibold text-gray-900 mb-5">Thông Tin Đơn Hàng</h3>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-x-8 gap-y-4">
        <!-- Cột trái -->
        <div class="space-y-4">
          <div class="flex items-start">
            <span class="w-28 font-medium text-gray-700">Mã đơn:</span>
            <span class="flex-1 text-gray-900">{{ hoaDon?.ma || "Chưa có" }}</span>
          </div>
          <div class="flex items-start">
            <span class="w-28 font-medium text-gray-700">Loại đơn:</span>
            <span class="flex-1 text-gray-900">{{ hoaDon?.loaiDon || "Chưa xác định" }}</span>
          </div>
          <div class="flex items-start">
            <span class="w-28 font-medium text-gray-700">Trạng thái:</span>
            <span class="flex-1 text-gray-900">{{ hoaDon?.trangThai === 1 ? "Hoàn thành" : "Chờ xác nhận" }}</span>
          </div>
          <div class="flex items-start">
            <span class="w-28 font-medium text-gray-700">Giảm giá:</span>
            <span class="flex-1 text-gray-900">{{ hoaDon?.idPhieuGiamGia?.ma || "Không áp dụng" }}</span>
          </div>
        </div>
        <!-- Cột phải -->
        <div class="space-y-4">
          <div class="flex items-start">
            <span class="w-28 font-medium text-gray-700">Ngày đặt hàng:</span>
            <span class="flex-1 text-gray-900">
    {{ hoaDon?.ngayTao ? format(new Date(hoaDon.ngayTao), 'dd/MM/yyyy HH:mm:ss', {locale: vi}) : '' }}
  </span>
          </div>
          <div class="flex items-start">
            <span class="w-28 font-medium text-gray-700">Khách hàng:</span>
            <span class="flex-1 text-gray-900">{{ hoaDon?.idKhachHang?.ten || "Khách lẻ" }}</span>
          </div>
          <div class="flex items-start">
            <span class="w-28 font-medium text-gray-700">SĐT:</span>
            <span class="flex-1 text-gray-900">{{ hoaDon?.soDienThoaiKhachHang || "Chưa có" }}</span>
          </div>
          <div class="flex items-start">
            <span class="w-28 font-medium text-gray-700">Địa chỉ:</span>
            <span class="flex-1 text-gray-900 break-words">{{ hoaDon?.diaChiKhachHang || "Chưa có" }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Lịch sử thanh toán với DynamicTable -->
    <div class="bg-gray-100 p-4 rounded-lg mb-4">
      <h3 class="text-lg font-medium text-gray-700 mb-2">Lịch sử thanh toán</h3>
      <DynamicTable
        :data="hoaDon?.lichSuHoaDon || []"
        :columns="paymentHistoryColumns"
        :get-nested-value="getNestedValue"
      />
    </div>

    <!-- Danh sách sản phẩm với DynamicTable -->
    <div class="bg-gray-100 p-4 rounded-lg mb-4">
      <h3 class="text-lg font-medium text-gray-700 mb-2">Danh sách sản phẩm</h3>
      <DynamicTable
        :data="hoaDon?.chiTietHoaDon || []"
        :columns="productColumns"
        :get-nested-value="getNestedValue"
      />
    </div>

    <!-- Tổng tiền -->
    <div class="bg-gray-100 p-4 rounded-lg">
      <h3 class="text-lg font-medium text-gray-700 mb-2">Tổng tiền</h3>
      <div class="grid grid-cols-2 gap-4">
        <div>
          <p><strong>Tổng tiền hàng:</strong> {{ hoaDon?.tongTien?.toLocaleString() || "0" }} VND</p>
          <p><strong>Giảm giá:</strong> {{
              hoaDon?.idPhieuGiamGia?.phanTramGiamGia ? (hoaDon.tongTien * (hoaDon.idPhieuGiamGia.phanTramGiamGia / 100)).toLocaleString() : "0"
            }} VND</p>
        </div>
        <div>
          <p><strong>Tiền phải thanh toán:</strong> {{ hoaDon?.tongTienSauGiam?.toLocaleString() || "0" }} VND</p>
        </div>
      </div>
    </div>

    <button @click="$router.push('/hoa-don')"
            class="mt-4 px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition">
      Quay lại
    </button>
  </div>
</template>

<script setup>
import {defineProps, computed} from "vue";
import useShowHoaDon from "@/views/Bill/ShowHoaDon";
import DynamicTable from "@/components/DynamicTable.vue";
import { format } from 'date-fns';
import { vi } from 'date-fns/locale'; // Thêm locale tiếng Việt
import {useRoute} from "vue-router"; // Import PathRouter
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue'; // Import BreadcrumbWrapper

const props = defineProps({
  id: {
    type: String,
    required: true,
  },
});

const {hoaDon, getNestedValue, paymentHistoryColumns, productColumns} = useShowHoaDon(props.id);

// Dữ liệu giả lập cho timeline
const timelineStatuses = computed(() => [
  {name: "Đặt Hàng Thành Công", time: "15:45:11 - 22/01/2024", completed: true},
  {name: "Chờ Xác Nhận", time: "15:45:11 - 22/01/2024", completed: true},
  {name: "Đang Giao", time: null, completed: false},
  {name: "Hoàn Thành", time: null, completed: false},
]);

// Lấy route hiện tại
const route = useRoute();

// Tính toán breadcrumb dựa trên meta của route
const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === "function") {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || ["Quản lý hóa đơn"]; // Mặc định nếu không có breadcrumb
});
</script>

<style scoped>
/* Thanh trạng thái */
.timeline {
  position: relative;
}

.timeline-container {
  position: relative;
}

.timeline-item {
  position: relative;
  z-index: 1;
}

.timeline-item .w-10 {
  position: relative;
  z-index: 2;
}

.timeline-item .absolute {
  transform: translateX(-50%);
  z-index: 1;
}

/* Căn chỉnh bảng */
table {
  border: 1px solid #ddd;
}

th, td {
  border: 1px solid #ddd;
  padding: 8px;
}

button {
  padding: 5px 10px;
  border: none;
  cursor: pointer;
}
</style>