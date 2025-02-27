<template>
  <div class="mt-4 flex justify-center gap-2">
    <!-- Nút điều hướng về trang đầu -->
    <button @click="goToPage(0)" :disabled="currentPage === 0"
            class="px-4 py-2 border rounded bg-gray-300 hover:bg-gray-400 disabled:opacity-50">
      Đầu
    </button>

    <!-- Nút điều hướng về trang trước -->
    <button @click="goToPage(currentPage - 1)" :disabled="currentPage === 0"
            class="px-4 py-2 border rounded bg-gray-300 hover:bg-gray-400 disabled:opacity-50">
      Trước
    </button>

    <!-- Hiển thị trang hiện tại và tổng số trang -->
    <span class="px-4 py-2">Trang {{ currentPage + 1 }} / {{ totalPages }}</span>

    <!-- Nút điều hướng đến trang tiếp theo -->
    <button @click="goToPage(currentPage + 1)" :disabled="currentPage === totalPages - 1"
            class="px-4 py-2 border rounded bg-gray-300 hover:bg-gray-400 disabled:opacity-50">
      Tiếp
    </button>

    <!-- Nút điều hướng đến trang cuối -->
    <button @click="goToPage(totalPages - 1)" :disabled="currentPage === totalPages - 1"
            class="px-4 py-2 border rounded bg-gray-300 hover:bg-gray-400 disabled:opacity-50">
      Cuối
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