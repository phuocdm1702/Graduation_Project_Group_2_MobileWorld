<template>
  <div class="relative bg-white custom-div">
    <div class="flex justify-start px-2.5 relative pt-2">
      <!-- Nút Tất cả -->
      <button
        @click="selectedStep = null; filterByStatus(null)"
        class="relative flex flex-col items-center w-30 px-3 pb-2 m-2 rounded-lg transition duration-300"
        :class="[
          selectedStep === null ? 'bg-gray-500 text-white shadow-md' : 'bg-gray-100 text-gray-700',
          'hover:opacity-80'
        ]"
      >
        <!-- Nhãn bước -->
        <span class="mt-2 text-sm font-semibold text-center whitespace-nowrap">
          Tất cả
        </span>
      </button>

      <!-- Các bước trạng thái -->
      <button
        v-for="(stepInfo, index) in steps"
        :key="index"
        @click="selectedStep = index; filterByStatus(index)"
        class="relative flex flex-col items-center w-30 px-3 pb-2 m-2 rounded-lg transition duration-300"
        :class="[
          selectedStep === index ? getSelectedColor(index) : getDefaultColor(index),
          'hover:opacity-80'
        ]"
      >
        <!-- Badge -->
        <span
          class="absolute -top-2 -right-2 w-6 h-6 flex items-center justify-center bg-white text-xs font-bold rounded-full shadow-md border-2"
          :class="getBadgeColor(index)"
        >
          {{ stepInfo.badge }}
        </span>

        <!-- Nhãn bước -->
        <span class="mt-2 text-sm font-semibold text-center whitespace-nowrap">
          {{ stepInfo.label }}
        </span>
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

// Khai báo emit
const emit = defineEmits(['filter-by-status']);

const selectedStep = ref(null);
const steps = ref([
  { badge: 0, label: 'Chờ xác nhận' },  // 0
  { badge: 0, label: 'Chờ giao hàng' }, // 1
  { badge: 0, label: 'Đang giao' },     // 2
  { badge: 0, label: 'Hoàn thành' },    // 3
  { badge: 0, label: 'Đã hủy' },       // 4
]);

// Hàm lấy dữ liệu từ API
const fetchStatusCount = async () => {
  try {
    const response = await axios.get('http://localhost:8080/hoa-don/status-count');
    const statusCount = response.data;

    // Cập nhật badge cho từng trạng thái
    steps.value[0].badge = statusCount[0] || 0; // Chờ xác nhận
    steps.value[1].badge = statusCount[1] || 0; // Chờ giao hàng
    steps.value[2].badge = statusCount[2] || 0; // Đang giao
    steps.value[3].badge = statusCount[3] || 0; // Hoàn thành
    steps.value[4].badge = statusCount[4] || 0; // Đã hủy
  } catch (error) {
    console.error('Lỗi khi lấy số lượng trạng thái:', error);
  }
};

// Hàm trả về màu khi được chọn
const getSelectedColor = (index) => {
  const colors = [
    'bg-blue-500 text-white shadow-md',    // Chờ xác nhận
    'bg-yellow-500 text-white shadow-md',  // Chờ giao hàng
    'bg-orange-500 text-white shadow-md',  // Đang giao
    'bg-green-500 text-white shadow-md',   // Hoàn thành
    'bg-red-500 text-white shadow-md',     // Đã hủy
  ];
  return colors[index];
};

// Hàm trả về màu khi không được chọn
const getDefaultColor = (index) => {
  const colors = [
    'bg-blue-100 text-blue-700',    // Chờ xác nhận
    'bg-yellow-100 text-yellow-700', // Chờ giao hàng
    'bg-orange-100 text-orange-700', // Đang giao
    'bg-green-100 text-green-700',   // Hoàn thành
    'bg-red-100 text-red-700',      // Đã hủy
  ];
  return colors[index];
};

// Hàm trả về màu cho badge
const getBadgeColor = (index) => {
  const colors = [
    'text-blue-500 border-blue-500',    // Chờ xác nhận
    'text-yellow-500 border-yellow-500', // Chờ giao hàng
    'text-orange-500 border-orange-500', // Đang giao
    'text-green-500 border-green-500',   // Hoàn thành
    'text-red-500 border-red-500',      // Đã hủy
  ];
  return colors[index];
};

// Hàm lọc theo trạng thái (gửi sự kiện lên parent component)
const filterByStatus = (status) => {
  emit('filter-by-status', status); // Gửi status lên parent, có thể là null hoặc số
};

// Gọi API khi component được mount
onMounted(() => {
  fetchStatusCount();
});
</script>

<style scoped>
.custom-div {
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
}
</style>