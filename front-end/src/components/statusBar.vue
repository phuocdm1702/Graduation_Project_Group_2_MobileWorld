<template>
  <div class="relative bg-white custom-div">
    <div class="flex justify-start px-2.5 relative pt-2">
      <!-- Các bước trạng thái -->
      <button
        v-for="(stepInfo, index) in steps"
        :key="index"
        @click="selectedStep = index"
        class="relative flex flex-col items-center w-30 px-3 pb-2 m-2 rounded-lg transition duration-300"
        :class="{
          'bg-orange-500 text-white shadow-md': selectedStep === index, // Chỉ button được chọn đổi màu
          'bg-gray-200 text-gray-500 hover:bg-orange-200': selectedStep !== index // Các button khác giữ nguyên
        }"
      >
        <!-- Badge (tách riêng khỏi chữ, nằm trong button) -->
        <span
          class="absolute -top-2 -right-2 w-6 h-6 flex items-center justify-center bg-white text-orange-500 text-xs font-bold rounded-full shadow-md border-2"
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

// Biến lưu trạng thái button nào đang được chọn
const selectedStep = ref(null);

// Dữ liệu các bước
const steps = [
  { badge: 11, label: 'Chờ xác nhận' },
  { badge: 1, label: 'Chờ giao hàng' },
  { badge: 1, label: 'Đang giao' },
  { badge: 12, label: 'Hoàn thành' },
  { badge: 8, label: 'Đã hủy' },
];
</script>

<style scoped>
.custom-div {
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
}
</style>
