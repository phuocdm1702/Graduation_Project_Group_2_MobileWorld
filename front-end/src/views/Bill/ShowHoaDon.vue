<!--<template>-->
<!--  <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />-->
<!--  <div class="show-hoa-don bg-white shadow-lg rounded-lg p-5 mt-2 mx-auto">-->
<!--    &lt;!&ndash; Thanh trạng thái &ndash;&gt;-->
<!--    <div class="timeline mb-6">-->
<!--      <div class="timeline-container flex items-center justify-between relative">-->
<!--        <div v-for="(status, index) in timelineStatuses" :key="index" class="timeline-item flex-1 text-center">-->
<!--          <div class="relative">-->
<!--            <div-->
<!--              :class="[-->
<!--                'w-10 h-10 rounded-full mx-auto flex items-center justify-center',-->
<!--                status.completed ? 'bg-green-500 text-white' : 'bg-gray-300 text-gray-700',-->
<!--              ]"-->
<!--            >-->
<!--              <span class="text-sm">{{ index + 1 }}</span>-->
<!--            </div>-->
<!--            <div-->
<!--              v-if="index < timelineStatuses.length - 1"-->
<!--              class="absolute top-5 left-1/2 w-full h-1 bg-gray-300"-->
<!--              :class="{ 'bg-green-500': status.completed }"-->
<!--            ></div>-->
<!--          </div>-->
<!--          <p class="mt-2 text-sm font-medium text-gray-700">{{ status.name }}</p>-->
<!--          <p class="text-xs text-gray-500">{{ status.time || "N/A" }}</p>-->
<!--        </div>-->
<!--      </div>-->
<!--      <div class="text-right mt-4">-->
<!--        <button-->
<!--          @click="fetchInvoiceHistory(hoaDon?.id)"-->
<!--          class="px-4 py-2 bg-green-500 text-white font-semibold rounded-lg shadow-md hover:bg-green-600 transition"-->
<!--        >-->
<!--          Chi tiết-->
<!--        </button>-->
<!--      </div>-->
<!--    </div>-->

<!--    &lt;!&ndash; Modal Lịch sử hóa đơn &ndash;&gt;-->
<!--    <div v-if="isHistoryModalOpen" class="fixed inset-0 z-50 overflow-y-auto" role="dialog" aria-modal="true">-->
<!--      <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" @click="closeHistoryModal"></div>-->
<!--      <div class="flex min-h-full items-center justify-center p-4">-->
<!--        <div class="relative bg-white rounded-lg shadow-xl w-full max-w-5xl transform transition-all">-->
<!--          <div class="px-6 py-4 border-b border-gray-200 flex justify-between items-center">-->
<!--            <h3 class="text-lg font-medium text-gray-700">Lịch sử hóa đơn - {{ hoaDon?.id }}</h3>-->
<!--            <button @click="closeHistoryModal" class="text-gray-400 hover:text-gray-500">-->
<!--              <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">-->
<!--                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />-->
<!--              </svg>-->
<!--            </button>-->
<!--          </div>-->
<!--          <div class="px-6 py-4 space-y-4">-->
<!--            <DynamicTable :data="invoiceHistory" :columns="historyColumns" :get-nested-value="getNestedValue" />-->
<!--          </div>-->
<!--          <div class="px-6 py-4 border-t border-gray-200 flex justify-end">-->
<!--            <button-->
<!--              @click="closeHistoryModal"-->
<!--              class="px-4 py-2 bg-gray-100 text-gray-700 rounded-md hover:bg-gray-200"-->
<!--            >-->
<!--              Đóng-->
<!--            </button>-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->
<!--    </div>-->

<!--    &lt;!&ndash; Thông tin đơn hàng &ndash;&gt;-->
<!--    <div class="bg-gray-50 p-6 rounded-xl mb-6 shadow-sm">-->
<!--      <h3 class="text-xl font-semibold text-gray-900 mb-5">Thông Tin Đơn Hàng</h3>-->
<!--      <div class="grid grid-cols-1 md:grid-cols-2 gap-x-8 gap-y-4">-->
<!--        <div class="space-y-4">-->
<!--          <div class="flex items-start">-->
<!--            <span class="w-28 font-medium text-gray-700">Mã đơn:</span>-->
<!--            <span class="flex-1 text-gray-900">{{ hoaDon?.ma || "Chưa có" }}</span>-->
<!--          </div>-->
<!--          <div class="flex items-start">-->
<!--            <span class="w-28 font-medium text-gray-700">Loại đơn:</span>-->
<!--            <span class="flex-1 text-gray-900">{{ hoaDon?.loaiDon || "Chưa xác định" }}</span>-->
<!--          </div>-->
<!--          <div class="flex items-start">-->
<!--            <span class="w-28 font-medium text-gray-700">Trạng thái:</span>-->
<!--            <span class="flex-1 text-gray-900">{{ getStatusText(hoaDon?.trangThai) }}</span>-->
<!--          </div>-->
<!--          <div class="flex items-start">-->
<!--            <span class="w-28 font-medium text-gray-700">Giảm giá:</span>-->
<!--            <span class="flex-1 text-gray-900">{{ hoaDon?.idPhieuGiamGia?.ma || "Không áp dụng" }}</span>-->
<!--          </div>-->
<!--        </div>-->
<!--        <div class="space-y-4">-->
<!--          <div class="flex items-start">-->
<!--            <span class="w-28 font-medium text-gray-700">Ngày đặt hàng:</span>-->
<!--            <span class="flex-1 text-gray-900">-->
<!--              {{ hoaDon?.ngayTao ? format(new Date(hoaDon.ngayTao), "dd/MM/yyyy HH:mm:ss", { locale: vi }) : "" }}-->
<!--            </span>-->
<!--          </div>-->
<!--          <div class="flex items-start">-->
<!--            <span class="w-28 font-medium text-gray-700">Khách hàng:</span>-->
<!--            <span class="flex-1 text-gray-900">{{ hoaDon?.idKhachHang?.ten || "Khách lẻ" }}</span>-->
<!--          </div>-->
<!--          <div class="flex items-start">-->
<!--            <span class="w-28 font-medium text-gray-700">SĐT:</span>-->
<!--            <span class="flex-1 text-gray-900">{{ hoaDon?.soDienThoaiKhachHang || "Chưa có" }}</span>-->
<!--          </div>-->
<!--          <div class="flex items-start">-->
<!--            <span class="w-28 font-medium text-gray-700">Địa chỉ:</span>-->
<!--            <span class="flex-1 text-gray-900 break-words">{{ hoaDon?.diaChiKhachHang || "Chưa có" }}</span>-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->
<!--    </div>-->

<!--    &lt;!&ndash; Lịch sử thanh toán &ndash;&gt;-->
<!--    <div class="bg-gray-100 p-4 rounded-lg mb-4">-->
<!--      <h3 class="text-lg font-medium text-gray-700 mb-2">Lịch sử thanh toán</h3>-->
<!--      <DynamicTable-->
<!--        :data="hoaDon?.hinhThucThanhToan || []"-->
<!--        :columns="paymentMethodColumns"-->
<!--        :get-nested-value="getNestedValue"-->
<!--      />-->
<!--    </div>-->

<!--    &lt;!&ndash; Danh sách sản phẩm &ndash;&gt;-->
<!--    <div class="bg-gray-100 p-4 rounded-lg mb-4">-->
<!--      <h3 class="text-lg font-medium text-gray-700 mb-2">Danh sách sản phẩm đã mua</h3>-->
<!--      <DynamicTable :data="hoaDon?.chiTietHoaDon || []" :columns="productColumns" :get-nested-value="getNestedValue" />-->
<!--    </div>-->

<!--    &lt;!&ndash; Tổng tiền &ndash;&gt;-->
<!--    <div class="bg-gray-100 p-4 rounded-lg">-->
<!--      <h3 class="text-lg font-medium text-gray-700 mb-2">Tổng tiền</h3>-->
<!--      <div class="grid grid-cols-2 gap-4">-->
<!--        <div>-->
<!--          <p><strong>Tổng tiền hàng:</strong> {{ hoaDon?.tongTien?.toLocaleString() || "0" }} VND</p>-->
<!--          <p>-->
<!--            <strong>Giảm giá:</strong>-->
<!--            {{-->
<!--              hoaDon?.idPhieuGiamGia?.phanTramGiamGia-->
<!--                ? (hoaDon.tongTien * (hoaDon.idPhieuGiamGia.phanTramGiamGia / 100)).toLocaleString()-->
<!--                : "0"-->
<!--            }}-->
<!--            VND-->
<!--          </p>-->
<!--        </div>-->
<!--        <div>-->
<!--          <p><strong>Tiền phải thanh toán:</strong> {{ hoaDon?.tongTienSauGiam?.toLocaleString() || "0" }} VND</p>-->
<!--        </div>-->
<!--      </div>-->
<!--    </div>-->

<!--    &lt;!&ndash; Modal Thêm sản phẩm &ndash;&gt;-->
<!--    <div v-if="isModalOpen" class="fixed inset-0 z-50 overflow-y-auto" role="dialog" aria-modal="true">-->
<!--      <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" @click="closeModal"></div>-->
<!--      <div class="flex min-h-full items-center justify-center p-4">-->
<!--        <div class="relative bg-white rounded-lg shadow-xl w-full max-w-5xl transform transition-all">-->
<!--          <div class="px-6 py-4 border-b border-gray-200 flex justify-between items-center">-->
<!--            <h3 class="text-lg font-medium text-gray-700">Thêm Sản Phẩm - {{ hoaDon?.id }}</h3>-->
<!--            <button @click="closeModal" class="text-gray-400 hover:text-gray-500">-->
<!--              <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">-->
<!--                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />-->
<!--              </svg>-->
<!--            </button>-->
<!--          </div>-->
<!--          <div class="px-6 py-4 space-y-4">-->
<!--            <DynamicTable :data="hardcodedProducts" :columns="productModalColumns" :get-nested-value="getNestedValue" />-->
<!--          </div>-->
<!--          <div class="px-6 py-4 border-t border-gray-200 flex justify-end gap-2">-->
<!--            <button-->
<!--              @click="closeModal"-->
<!--              class="px-4 py-2 bg-gray-100 text-gray-700 rounded-md hover:bg-gray-200"-->
<!--            >-->
<!--              Đóng-->
<!--            </button>-->
<!--            <button-->
<!--              @click="closeModal"-->
<!--              class="px-4 py-2 bg-gray-100 text-gray-700 rounded-md hover:bg-gray-200"-->
<!--            >-->
<!--              Thêm-->
<!--            </button>-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->
<!--    </div>-->

<!--    &lt;!&ndash; Nút Quay lại &ndash;&gt;-->
<!--    <div class="text-right mt-4">-->
<!--      <button-->
<!--        @click="$router.push('/hoa-don')"-->
<!--        class="px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition"-->
<!--      >-->
<!--        Quay lại-->
<!--      </button>-->
<!--    </div>-->
<!--  </div>-->
<!--</template>-->

<!--<script setup>-->
<!--import { defineProps, computed, onMounted } from "vue";-->
<!--import { useRoute } from "vue-router";-->
<!--import useShowHoaDon from "@/views/Bill/JS/ShowHoaDon";-->
<!--import DynamicTable from "@/components/DynamicTable.vue";-->
<!--import { format } from "date-fns";-->
<!--import { vi } from "date-fns/locale";-->
<!--import BreadcrumbWrapper from "@/components/BreadcrumbWrapper.vue";-->

<!--const props = defineProps({-->
<!--  id: { type: String, required: true },-->
<!--});-->

<!--const {-->
<!--  hoaDon,-->
<!--  getNestedValue,-->
<!--  paymentMethodColumns,-->
<!--  productColumns,-->
<!--  historyColumns,-->
<!--  productModalColumns,-->
<!--  hardcodedProducts,-->
<!--  isModalOpen,-->
<!--  openModal,-->
<!--  closeModal,-->
<!--  isHistoryModalOpen,-->
<!--  invoiceHistory,-->
<!--  fetchInvoiceHistory,-->
<!--  closeHistoryModal,-->
<!--  getStatusText-->
<!--} = useShowHoaDon(props.id);-->

<!--const timelineStatuses = computed(() => [-->
<!--  { name: "Đặt Hàng Thành Công", time: "15:45:11 - 22/01/2024", completed: true },-->
<!--  { name: "Chờ Xác Nhận", time: "15:45:11 - 22/01/2024", completed: true },-->
<!--  { name: "Đang Giao", time: null, completed: false },-->
<!--  { name: "Hoàn Thành", time: null, completed: false },-->
<!--]);-->

<!--const route = useRoute();-->
<!--const breadcrumbItems = computed(() => {-->
<!--  return typeof route.meta.breadcrumb === "function"-->
<!--    ? route.meta.breadcrumb(route)-->
<!--    : route.meta?.breadcrumb || ["Quản lý hóa đơn"];-->
<!--});-->

<!--// // Hàm ánh xạ trạng thái-->
<!--// const getStatusText = (status) => {-->
<!--//   const statusMap = {-->
<!--//     0: "Chờ xác nhận",-->
<!--//     1: "Chờ giao hàng",-->
<!--//     2: "Đang giao",-->
<!--//     3: "Hoàn thành",-->
<!--//     4: "Đã hủy",-->
<!--//   };-->
<!--//   return statusMap[status] || "Không xác định";-->
<!--// };-->

<!--// Gắn sự kiện cho các nút trong cột "Thao tác"-->
<!--onMounted(() => {-->
<!--  const handleButtonClick = (event) => {-->
<!--    const action = event.currentTarget.dataset.action;-->
<!--    const itemId = event.currentTarget.dataset.id;-->
<!--    if (action === "add") openModal();-->
<!--    else if (action === "scan") console.log("Quét QR cho sản phẩm với ID:", itemId);-->
<!--    else if (action === "delete") console.log("Xóa sản phẩm với ID:", itemId);-->
<!--  };-->

<!--  const attachEventListeners = () => {-->
<!--    document.querySelectorAll("button[data-action]").forEach((button) => {-->
<!--      button.removeEventListener("click", handleButtonClick);-->
<!--      button.addEventListener("click", handleButtonClick);-->
<!--    });-->
<!--  };-->

<!--  attachEventListeners();-->

<!--  const observer = new MutationObserver(attachEventListeners);-->
<!--  const table = document.querySelector(".show-hoa-don table");-->
<!--  if (table) observer.observe(table, { childList: true, subtree: true });-->
<!--});-->
<!--</script>-->

<!--<style scoped>-->
<!--.timeline {-->
<!--  position: relative;-->
<!--}-->

<!--.timeline-container {-->
<!--  position: relative;-->
<!--}-->

<!--.timeline-item {-->
<!--  position: relative;-->
<!--  z-index: 1;-->
<!--}-->

<!--.timeline-item .w-10 {-->
<!--  position: relative;-->
<!--  z-index: 2;-->
<!--}-->

<!--.timeline-item .absolute {-->
<!--  transform: translateX(-50%);-->
<!--  z-index: 1;-->
<!--}-->

<!--table {-->
<!--  border: 1px solid #ddd;-->
<!--}-->

<!--th,-->
<!--td {-->
<!--  border: 1px solid #ddd;-->
<!--  padding: 8px;-->
<!--}-->

<!--button {-->
<!--  padding: 5px 10px;-->
<!--  border: none;-->
<!--  cursor: pointer;-->
<!--}-->
<!--</style>-->


<template>
  <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />
  <div class="show-hoa-don bg-white shadow-lg rounded-lg p-5 mt-2 mx-auto">
    <!-- Thanh trạng thái -->
    <div class="timeline mb-6">
      <div class="timeline-container flex items-center justify-between relative">
        <div v-for="(status, index) in computedTimelineStatuses" :key="index" class="timeline-item flex-1 text-center">
          <div class="relative">
            <div
              :class="[
                'w-10 h-10 rounded-full mx-auto flex items-center justify-center',
                status.completed ? 'bg-green-500 text-white' : 'bg-gray-300 text-gray-700',
              ]"
            >
              <span class="text-sm">{{ index + 1 }}</span>
            </div>
            <div
              v-if="index < computedTimelineStatuses.length - 1"
              class="absolute top-5 left-1/2 w-full h-1 bg-gray-300"
              :class="{ 'bg-green-500': status.completed }"
            ></div>
          </div>
          <p class="mt-2 text-sm font-medium text-gray-700">{{ status.name }}</p>
          <p class="text-xs text-gray-500">{{ status.time || "N/A" }}</p>
        </div>
      </div>
      <!-- Nút điều khiển trạng thái cho hóa đơn online -->
      <div v-if="hoaDon?.loaiDon === 'Online'" class="text-right mt-4 space-x-2">
        <button
          v-if="hoaDon?.trangThai === 0"
          @click="confirmOrder"
          class="px-4 py-2 bg-blue-500 text-white font-semibold rounded-lg shadow-md hover:bg-blue-600 transition"
        >
          Xác nhận đơn hàng
        </button>
        <button
          v-if="hoaDon?.trangThai === 1"
          @click="startShipping"
          class="px-4 py-2 bg-orange-500 text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition"
        >
          Bắt đầu giao hàng
        </button>
        <button
          v-if="hoaDon?.trangThai === 2"
          @click="completeOrder"
          class="px-4 py-2 bg-green-500 text-white font-semibold rounded-lg shadow-md hover:bg-green-600 transition"
        >
          Hoàn thành
        </button>
      </div>
      <div class="text-right mt-4">
        <button
          @click="fetchInvoiceHistory(hoaDon?.id)"
          class="px-4 py-2 bg-green-500 text-white font-semibold rounded-lg shadow-md hover:bg-green-600 transition"
        >
          Chi tiết
        </button>
      </div>
    </div>

    <!-- Modal Lịch sử hóa đơn -->
    <div v-if="isHistoryModalOpen" class="fixed inset-0 z-50 overflow-y-auto" role="dialog" aria-modal="true">
      <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" @click="closeHistoryModal"></div>
      <div class="flex min-h-full items-center justify-center p-4">
        <div class="relative bg-white rounded-lg shadow-xl w-full max-w-5xl transform transition-all">
          <div class="px-6 py-4 border-b border-gray-200 flex justify-between items-center">
            <h3 class="text-lg font-medium text-gray-700">Lịch sử hóa đơn - {{ hoaDon?.id }}</h3>
            <button @click="closeHistoryModal" class="text-gray-400 hover:text-gray-500">
              <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
          <div class="px-6 py-4 space-y-4">
            <DynamicTable :data="invoiceHistory" :columns="historyColumns" :get-nested-value="getNestedValue" />
          </div>
          <div class="px-6 py-4 border-t border-gray-200 flex justify-end">
            <button
              @click="closeHistoryModal"
              class="px-4 py-2 bg-gray-100 text-gray-700 rounded-md hover:bg-gray-200"
            >
              Đóng
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Thông tin đơn hàng -->
    <div class="bg-gray-50 p-6 rounded-xl mb-6 shadow-sm">
      <h3 class="text-xl font-semibold text-gray-900 mb-5">Thông Tin Đơn Hàng</h3>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-x-8 gap-y-4">
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
            <span class="flex-1 text-gray-900">{{ getStatusText(hoaDon?.trangThai) }}</span>
          </div>
          <div class="flex items-start">
            <span class="w-28 font-medium text-gray-700">Giảm giá:</span>
            <span class="flex-1 text-gray-900">{{ hoaDon?.idPhieuGiamGia?.ma || "Không áp dụng" }}</span>
          </div>
        </div>
        <div class="space-y-4">
          <div class="flex items-start">
            <span class="w-28 font-medium text-gray-700">Ngày đặt hàng:</span>
            <span class="flex-1 text-gray-900">
              {{ hoaDon?.ngayTao ? format(new Date(hoaDon.ngayTao), "dd/MM/yyyy HH:mm:ss", { locale: vi }) : "" }}
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

    <!-- Lịch sử thanh toán -->
    <div class="bg-gray-100 p-4 rounded-lg mb-4">
      <h3 class="text-lg font-medium text-gray-700 mb-2">Lịch sử thanh toán</h3>
      <DynamicTable
        :data="hoaDon?.hinhThucThanhToan || []"
        :columns="paymentMethodColumns"
        :get-nested-value="getNestedValue"
      />
    </div>

    <!-- Danh sách sản phẩm -->
    <div class="bg-gray-100 p-4 rounded-lg mb-4">
      <h3 class="text-lg font-medium text-gray-700 mb-2">Danh sách sản phẩm đã mua</h3>
      <DynamicTable :data="hoaDon?.chiTietHoaDon || []" :columns="productColumns" :get-nested-value="getNestedValue" />
    </div>

    <!-- Tổng tiền -->
    <div class="bg-gray-100 p-4 rounded-lg">
      <h3 class="text-lg font-medium text-gray-700 mb-2">Tổng tiền</h3>
      <div class="grid grid-cols-2 gap-4">
        <div>
          <p><strong>Tổng tiền hàng:</strong> {{ hoaDon?.tongTien?.toLocaleString() || "0" }} VND</p>
          <p>
            <strong>Giảm giá:</strong>
            {{
              hoaDon?.idPhieuGiamGia?.phanTramGiamGia
                ? (hoaDon.tongTien * (hoaDon.idPhieuGiamGia.phanTramGiamGia / 100)).toLocaleString()
                : "0"
            }}
            VND
          </p>
        </div>
        <div>
          <p><strong>Tiền phải thanh toán:</strong> {{ hoaDon?.tongTienSauGiam?.toLocaleString() || "0" }} VND</p>
        </div>
      </div>
    </div>

    <!-- Modal Thêm sản phẩm -->
    <div v-if="isModalOpen" class BAB="fixed inset-0 z-50 overflow-y-auto" role="dialog" aria-modal="true">
      <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" @click="closeModal"></div>
      <div class="flex min-h-full items Talvez-center justify-center p-4">
        <div class="relative bg-white rounded-lg shadow-xl w-full max-w-5xl transform transition-all">
          <div class="px-6 py-4 border-b border-gray-200 flex justify-between items-center">
            <h3 class="text-lg font-medium text-gray-700">Thêm Sản Phẩm - {{ hoaDon?.id }}</h3>
            <button @click="closeModal" class="text-gray-400 hover:text-gray-500">
              <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
          <div class="px-6 py-4 space-y-4">
            <DynamicTable :data="hardcodedProducts" :columns="productModalColumns" :get-nested-value="getNestedValue" />
          </div>
          <div class="px-6 py-4 border-t border-gray-200 flex justify-end gap-2">
            <button
              @click="closeModal"
              class="px-4 py-2 bg-gray-100 text-gray-700 rounded-md hover:bg-gray-200"
            >
              Đóng
            </button>
            <button
              @click="closeModal"
              class="px-4 py-2 bg-gray-100 text-gray-700 rounded-md hover:bg-gray-200"
            >
              Thêm
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Nút Quay lại -->
    <div class="text-right mt-4">
      <button
        @click="$router.push('/hoa-don')"
        class="px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition"
      >
        Quay lại
      </button>
    </div>
  </div>
</template>

<script setup>
import { defineProps, computed, onMounted } from "vue";
import { useRoute } from "vue-router";
import useShowHoaDon from "@/views/Bill/JS/ShowHoaDon";
import DynamicTable from "@/components/DynamicTable.vue";
import { format } from "date-fns";
import { vi } from "date-fns/locale";
import BreadcrumbWrapper from "@/components/BreadcrumbWrapper.vue";

const props = defineProps({
  id: { type: String, required: true },
});

const {
  hoaDon,
  getNestedValue,
  paymentMethodColumns,
  productColumns,
  historyColumns,
  productModalColumns,
  hardcodedProducts,
  isModalOpen,
  openModal,
  closeModal,
  isHistoryModalOpen,
  invoiceHistory,
  fetchInvoiceHistory,
  closeHistoryModal,
  getStatusText,
  confirmOrder,
  startShipping,
  completeOrder,
} = useShowHoaDon(props.id);

// Xây dựng timeline dựa trên trạng thái thực tế từ DB
const computedTimelineStatuses = computed(() => {
  const statuses = [
    { name: "Đặt Hàng Thành Công", completed: false, time: null },
    { name: "Chờ Xác Nhận", completed: false, time: null },
    { name: "Đang Giao", completed: false, time: null },
    { name: "Hoàn Thành", completed: false, time: null },
    { name: "Đã hủy", completed: false, time: null },
  ];

  // Nếu là hóa đơn trực tiếp và đã thanh toán, hiển thị trạng thái "Hoàn Thành"
  if (hoaDon.value?.loaiDon === "Trực tiếp" && hoaDon.value?.hinhThucThanhToan?.length > 0) {
    const payment = hoaDon.value.hinhThucThanhToan[0];
    if (payment.tienMat > 0 || payment.tienChuyenKhoan > 0) {
      statuses.forEach((status) => {
        status.completed = true;
        status.time = hoaDon.value?.ngayTao
          ? format(new Date(hoaDon.value.ngayTao), "HH:mm:ss - dd/MM/yyyy", { locale: vi })
          : "N/A";
      });
      return statuses;
    }
  }

  // Xử lý hóa đơn online dựa trên trạng thái và lịch sử
  if (hoaDon.value?.lichSuHoaDon?.length) {
    hoaDon.value.lichSuHoaDon.forEach((history) => {
      const time = history.thoiGian
        ? format(new Date(history.thoiGian), "HH:mm:ss - dd/MM/yyyy", { locale: vi })
        : "N/A";
      if (history.hanhDong === "Đặt hàng") {
        statuses[0].completed = true;
        statuses[0].time = time;
      } else if (history.hanhDong === "Đã xác nhận") {
        statuses[1].completed = true;
        statuses[1].time = time;
      } else if (history.hanhDong === "Đang giao") {
        statuses[2].completed = true; 
        statuses[2].time = time;
      } else if (history.hanhDong === "Hoàn thành") {
        statuses[3].completed = true;
        statuses[3].time = time;
      }else if (history.hanhDong === "Đã hủy") {
        statuses[4].completed = true;
        statuses[4].time = time;
      }
    });
  }

  // Đảm bảo trạng thái hiện tại được phản ánh
  if (hoaDon.value?.trangThai >= 0) {
    for (let i = 0; i <= hoaDon.value.trangThai; i++) {
      if (!statuses[i].time) {
        statuses[i].completed = true;
        statuses[i].time = hoaDon.value?.ngayTao
          ? format(new Date(hoaDon.value.ngayTao), "HH:mm:ss - dd/MM/yyyy", { locale: vi })
          : "N/A";
      }
    }
  }

  return statuses;
});

const route = useRoute();
const breadcrumbItems = computed(() => {
  return typeof route.meta.breadcrumb === "function"
    ? route.meta.breadcrumb(route)
    : route.meta?.breadcrumb || ["Quản lý hóa đơn"];
});

onMounted(() => {
  const handleButtonClick = (event) => {
    const action = event.currentTarget.dataset.action;
    const itemId = event.currentTarget.dataset.id;
    if (action === "add") openModal();
    else if (action === "scan") console.log("Quét QR cho sản phẩm với ID:", itemId);
    else if (action === "delete") console.log("Xóa sản phẩm với ID:", itemId);
  };

  const attachEventListeners = () => {
    document.querySelectorAll("button[data-action]").forEach((button) => {
      button.removeEventListener("click", handleButtonClick);
      button.addEventListener("click", handleButtonClick);
    });
  };

  attachEventListeners();

  const observer = new MutationObserver(attachEventListeners);
  const table = document.querySelector(".show-hoa-don table");
  if (table) observer.observe(table, { childList: true, subtree: true });
});
</script>

<style scoped>
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

table {
  border: 1px solid #ddd;
}

th,
td {
  border: 1px solid #ddd;
  padding: 8px;
}

button {
  padding: 5px 10px;
  border: none;
  cursor: pointer;
}
</style>