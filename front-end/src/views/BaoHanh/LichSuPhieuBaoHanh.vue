<template>
  <div>
    <!-- Thay Breadcrumb bằng BreadcrumbWrapper -->
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />

    <div class="mt-4">
      <h4 class="text-gray-600">Lịch sử bảo hành</h4>

      <div class="mt-4 flex">
        <div class="w-full max-w-4xl overflow-hidden bg-white border rounded-md shadow-md">
          <form>
            <div class="flex items-center justify-between px-5 py-3 text-gray-700 border-b">
              <h3 class="text-sm">Thêm Phiếu Bảo Hành</h3>
              <button>
                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                </svg>
              </button>
            </div>

            <div class="p-5 text-gray-700 bg-gray-200 border-b grid grid-cols-3 gap-4">
              <div>
                <label class="text-xs">Phiếu Bảo Hành</label>
                <input type="text" class="w-full px-4 py-2 mt-2 border rounded-md"/>
              </div>
              <div>
                <label class="text-xs">Mã Loại Phương Thức Sửa Chữa</label>
                <select class="w-full px-4 py-2 mt-2 border rounded-md">
                  <option value="">Chọn phương thức</option>
                  <option v-for="method in repairMethods" :key="method.id" :value="method.id">{{ method.name }}</option>
                </select>
              </div>
              <div>
                <label class="text-xs">Chi Phí</label>
                <input type="number" class="w-full px-4 py-2 mt-2 border rounded-md"/>
              </div>
              <div>
                <label class="text-xs">Trạng Thái</label>
                <select class="w-full px-4 py-2 mt-2 border rounded-md">
                  <option value="">Chọn trạng thái</option>
                  <option value="pending">Đang chờ</option>
                  <option value="processing">Đang xử lý</option>
                  <option value="completed">Hoàn thành</option>
                </select>
              </div>
              <div class="col-span-3">
                <label class="text-xs">Ghi Chú</label>
                <textarea class="w-full px-4 py-2 mt-2 border rounded-md"></textarea>
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
    <LichSuBaoHanhTable/>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import LichSuBaoHanhTable from "@/views/BaoHanh/LichSuBaoHanhTable.vue";
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue'; // Import BreadcrumbWrapper

// Dữ liệu giả lập cho phương thức sửa chữa (có thể thay bằng API thực tế)
const repairMethods = [
  { id: 1, name: "Sửa tại chỗ" },
  { id: 2, name: "Gửi bảo hành" },
  { id: 3, name: "Thay thế linh kiện" },
];

// Lấy route hiện tại
const route = useRoute();

// Tính toán breadcrumb dựa trên meta của route
const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === "function") {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || ["Lịch sử bảo hành"]; // Mặc định nếu không có breadcrumb
});
</script>