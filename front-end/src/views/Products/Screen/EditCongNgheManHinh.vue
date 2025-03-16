<template>
  <div class="mt-2 mx-auto">
    <h2 class="bg-white shadow-lg rounded-lg p-5 mb-2 mt-2 text-2xl font-semibold text-gray-700">
      Chỉnh Sửa Công Nghệ Màn Hình
    </h2>
    <ToastNotification ref="toast" />

    <!-- Form chỉnh sửa -->
    <div class="bg-white shadow-lg rounded-lg p-5 mb-2 mt-2">
      <form v-if="congNgheManHinh.id" @submit.prevent="handleFormSubmit" class="grid grid-cols-1 gap-6">
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
            Lưu
          </button>
        </div>
      </form>
      <div v-else class="text-center text-gray-500">Đang tải dữ liệu...</div>
    </div>

    <!-- Modal xác nhận -->
    <ConfirmModal
      :show="showConfirmModal"
      :message="confirmMessage"
      @confirm="executeConfirmedAction"
      @cancel="closeConfirmModal"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import ToastNotification from '@/components/ToastNotification.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';
import useCongNgheManHinh from './useCongNgheManHinh.js';
import axios from 'axios';

const router = useRouter();
const route = useRoute();
const toast = ref(null);
const {
  congNgheManHinh,
  showConfirmModal,
  confirmMessage,
  updateCongNgheManHinh,
  confirmAction,
  executeConfirmedAction,
  closeConfirmModal,
  uniqueCongNgheManHinhList,
  uniqueChuanManHinhList,
} = useCongNgheManHinh(toast);

// Lấy dữ liệu công nghệ màn hình theo ID
const fetchCongNgheManHinh = async (id) => {
  try {
    const { data } = await axios.get(`http://localhost:8080/api/cong-nghe-man-hinh/${id}`);
    congNgheManHinh.value = data;
  } catch (error) {
    if (toast.value) toast.value?.kshowToast('error', 'Không thể tải dữ liệu!');
    console.error('Fetch error:', error);
    router.push('/cong-nghe-man-hinh'); // Quay lại nếu không tìm thấy
  }
};

// Xử lý form
const handleFormSubmit = () => {
  confirmAction('Bạn có chắc chắn muốn cập nhật công nghệ màn hình này?', async () => {
    await updateCongNgheManHinh();
    router.push('/cong-nghe-man-hinh'); // Quay lại danh sách sau khi lưu
  });
};

const goBack = () => {
  router.push('/cong-nghe-man-hinh');
};

onMounted(() => {
  const id = route.params.id;
  if (id) fetchCongNgheManHinh(id);
});
</script>

<style scoped>
.input-field {
  @apply w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-sm;
}
</style>