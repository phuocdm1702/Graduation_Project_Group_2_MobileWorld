<template>
  <div class="flex justify-center gap-2">
    <!-- Nút điều hướng về trang đầu -->
    <button @click="goToPage(0)" :disabled="currentPage === 0"
            class="px-2 py-1 border rounded bg-gray-300 hover:bg-orange-500 text-black hover:text-white shadow-md disabled:opacity-50">
      <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
        <path stroke-linecap="round" stroke-linejoin="round" d="M19.5 4.5L10.5 12l9 7.5M4.5 4.5v15"/>
      </svg>
    </button>

    <!-- Nút điều hướng về trang trước -->
    <button @click="goToPage(currentPage - 1)" :disabled="currentPage === 0"
            class="px-2 py-1 border rounded bg-gray-300 hover:bg-orange-500 text-black hover:text-white shadow-md disabled:opacity-50">
      <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
        <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 19.5 8.25 12l7.5-7.5"/>
      </svg>
    </button>

    <!-- Hiển thị trang hiện tại và tổng số trang -->
    <span class="px-2 py-1">Trang {{ currentPage + 1 }} / {{ totalPages }}</span>

    <!-- Nút điều hướng đến trang tiếp theo -->
    <button @click="goToPage(currentPage + 1)" :disabled="currentPage === totalPages - 1"
            class="px-2 py-1 border rounded bg-gray-300 hover:bg-orange-500 text-black hover:text-white shadow-md disabled:opacity-50">
      <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
        <path stroke-linecap="round" stroke-linejoin="round" d="M8.25 4.5l7.5 7.5-7.5 7.5"/>
      </svg>
    </button>

    <!-- Nút điều hướng đến trang cuối -->
    <button @click="goToPage(totalPages - 1)" :disabled="currentPage === totalPages - 1"
            class="px-2 py-1 border rounded bg-gray-300 hover:bg-orange-500 text-black hover:text-white shadow-md disabled:opacity-50">
      <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
        <path stroke-linecap="round" stroke-linejoin="round" d="M4.5 4.5l9 7.5-9 7.5M19.5 4.5v15"/>
      </svg>
    </button>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';

// Props nhận từ component cha
const props = defineProps({
  currentPage: Number,
  totalPages: Number
});

// Emit sự kiện thay đổi trang
const emit = defineEmits(['page-changed']);

// Hàm chuyển trang
const goToPage = (page) => {
  if (page >= 0 && page < props.totalPages) {
    emit('page-changed', page);
  }
};
</script>
