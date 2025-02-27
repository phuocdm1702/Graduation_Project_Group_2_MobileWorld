<template>
  <div v-if="show" class="fixed inset-0 flex items-center justify-center z-50">
    <!-- Overlay tối màu -->
    <div class="fixed inset-0 bg-black bg-opacity-50 z-40" @click="close"></div>

    <!-- Modal -->
    <div class="bg-white p-6 rounded-xl shadow-lg border border-gray-100 max-w-md w-full z-50">
      <h2 class="text-2xl font-semibold text-gray-800 mb-6 flex items-center">
        <i :class="isEdit ? 'fa-solid fa-edit text-blue-500 mr-2' : 'fa-solid fa-plus-circle text-green-500 mr-2'"></i>
        {{ isEdit ? 'Cập Nhật Dòng Sản Phẩm' : 'Thêm Dòng Sản Phẩm' }}
      </h2>
      <form @submit.prevent="submitForm" class="space-y-6">
        <div class="grid grid-cols-1 gap-6">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Mã dòng sản phẩm</label>
            <input
              v-model.trim="localProductLine.ma"
              type="text"
              placeholder="Nhập mã dòng sản phẩm"
              class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
              required
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Tên dòng sản phẩm</label>
            <input
              v-model.trim="localProductLine.dongSanPham"
              type="text"
              placeholder="Nhập tên dòng sản phẩm"
              class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
              required
            />
          </div>
        </div>
        <div class="flex justify-end gap-4">
          <button
            type="button"
            @click="close"
            class="bg-gray-200 text-gray-700 px-5 py-2 rounded-lg hover:bg-gray-300 transition-all duration-200 font-medium"
          >
            Hủy
          </button>
          <button
            type="submit"
            :class="isEdit ? 'bg-blue-600 hover:bg-blue-700' : 'bg-green-600 hover:bg-green-700'"
            class="text-white px-5 py-2 rounded-lg transition-all duration-200 font-medium flex items-center"
          >
            <i class="fa-solid fa-save mr-2"></i> {{ isEdit ? 'Cập nhật' : 'Thêm mới' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';

const props = defineProps({
  show: Boolean,
  isEdit: Boolean,
  productLine: Object,
});

const emit = defineEmits(['submit', 'close']);

const localProductLine = ref({ id: null, ma: '', dongSanPham: '' });

// Đồng bộ dữ liệu từ prop productLine khi thay đổi
watch(
  () => props.productLine,
  (newValue) => {
    localProductLine.value = { ...newValue };
  },
  { immediate: true }
);

const submitForm = () => {
  emit('submit', localProductLine.value);
};

const close = () => {
  emit('close');
};
</script>

<style scoped>
:root {
  --ring-color: #10b981; /* Màu xanh lá cho Thêm mới */
}
div[z-50] {
  --ring-color: #3b82f6; /* Màu xanh dương cho Cập nhật */
}
</style>