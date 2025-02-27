<template>
  <div v-if="show" class="fixed inset-0 flex items-center justify-center z-50">
    <!-- Overlay tối màu -->
    <div class="fixed inset-0 bg-black bg-opacity-50" @click="cancel"></div>

    <!-- Modal xác nhận -->
    <div class="bg-white p-6 rounded-xl shadow-lg border border-gray-100 max-w-sm w-full z-50">
      <h3 class="text-lg font-semibold text-gray-800 mb-4 flex items-center">
        <i class="fa-solid fa-question-circle text-yellow-500 mr-2"></i>
        Xác nhận
      </h3>
      <p class="text-gray-600 mb-6">{{ message }}</p>
      <div class="flex justify-end gap-4">
        <button
          @click="cancel"
          class="bg-gray-200 text-gray-700 px-4 py-2 rounded-lg hover:bg-gray-300 transition-all duration-200 font-medium"
        >
          Hủy
        </button>
        <button
          @click="confirm"
          class="bg-red-600 text-white px-4 py-2 rounded-lg hover:bg-red-700 transition-all duration-200 font-medium flex items-center"
        >
          <i class="fa-solid fa-check mr-2"></i> Đồng ý
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

defineProps({
  show: Boolean,
  message: String,
});

const emit = defineEmits(['confirm', 'cancel']);

const confirm = () => emit('confirm');
const cancel = () => emit('cancel');
</script>

<style scoped>
/* Tùy chỉnh nếu cần */
</style>