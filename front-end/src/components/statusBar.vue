<template>
  <div class="relative bg-white custom-div">
    <div class="flex justify-start px-2.5 relative pt-2">
      <!-- Các bước trạng thái -->
      <button
        v-for="(stepInfo, index) in steps"
        :key="index"
        @click="selectedStep = index"
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
import { ref } from 'vue';

const selectedStep = ref(null);

const steps = [
  { badge: 11, label: 'Chờ xác nhận' },  // 0
  { badge: 1, label: 'Chờ giao hàng' },  // 1
  { badge: 1, label: 'Đang giao' },      // 2
  { badge: 12, label: 'Hoàn thành' },    // 3
  { badge: 8, label: 'Đã hủy' },        // 4
];

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
</script>

<style scoped>
.custom-div {
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
}
</style>