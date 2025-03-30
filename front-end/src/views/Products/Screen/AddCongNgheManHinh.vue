<template>
  <div>
    <!-- Thêm BreadcrumbWrapper -->
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />

    <div class="mt-2 mx-auto">
      <h2 class="bg-white shadow-lg rounded-lg p-5 mb-2 mt-2 text-2xl font-semibold text-gray-700">
        Thêm Mới Công Nghệ Màn Hình
      </h2>
      <ToastNotification ref="toast" />

      <!-- Form thêm mới -->
      <div class="bg-white shadow-lg rounded-lg p-5 mb-2 mt-2">
        <form @submit.prevent="handleFormSubmit" class="grid grid-cols-1 gap-6">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Mã Công Nghệ</label>
            <input
              v-model.trim="congNgheManHinh.ma"
              type="text"
              placeholder="Nhập mã công nghệ"
              class="input-field"
              required
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Tên Công Nghệ</label>
            <select
              v-model="congNgheManHinh.congNgheManHinh"
              class="input-field"
              required
            >
              <option value="">Chọn tên công nghệ</option>
              <option v-for="tech in uniqueCongNgheManHinhList" :key="tech" :value="tech">{{ tech }}</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Chuẩn Màn Hình</label>
            <select
              v-model="congNgheManHinh.chuanManHinh"
              class="input-field"
              required
            >
              <option value="">Chọn chuẩn màn hình</option>
              <option v-for="chuan in uniqueChuanManHinhList" :key="chuan" :value="chuan">{{ chuan }}</option>
            </select>
          </div>
          <div class="flex justify-end gap-2 mt-6">
            <button
              @click="goBack"
              class="px-4 py-2 bg-gray-500 text-white font-semibold rounded-lg shadow-md hover:bg-gray-600 transition"
            >
              Hủy
            </button>
            <button
              type="submit"
              class="px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition"
            >
              Thêm
            </button>
          </div>
        </form>
      </div>

      <!-- Modal xác nhận -->
      <ConfirmModal
        :show="showConfirmModal"
        :message="confirmMessage"
        @confirm="executeConfirmedAction"
        @cancel="closeConfirmModal"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import ToastNotification from '@/components/ToastNotification.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue'; // Import BreadcrumbWrapper
import useCongNgheManHinh from './CongNgheManHinh.js';

const router = useRouter();
const route = useRoute(); // Thêm useRoute để lấy thông tin route hiện tại
const toast = ref(null);
const {
  congNgheManHinh,
  showConfirmModal,
  confirmMessage,
  saveCongNgheManHinh,
  confirmAction,
  executeConfirmedAction,
  closeConfirmModal,
  uniqueCongNgheManHinhList,
  uniqueChuanManHinhList,
} = useCongNgheManHinh(toast);

// Tính toán breadcrumb dựa trên meta của route
const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === 'function') {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || ['Thêm Mới Công Nghệ Màn Hình']; // Mặc định nếu không có breadcrumb
});

// Xử lý form
const handleFormSubmit = () => {
  confirmAction('Bạn có chắc chắn muốn thêm công nghệ màn hình này?', async () => {
    await saveCongNgheManHinh();
    router.push('/screens/technology'); // Quay lại danh sách sau khi thêm
  });
};

const goBack = () => {
  router.push('/screens/technology');
};

onMounted(() => {
  congNgheManHinh.value = { id: null, ma: '', congNgheManHinh: '', chuanManHinh: '' }; // Reset form khi vào trang
});
</script>

<style scoped>
.input-field {
  @apply w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-sm;
}
</style>