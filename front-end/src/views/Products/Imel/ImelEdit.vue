<template>
  <div>
    <!-- Thêm BreadcrumbWrapper -->
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />

    <div class="mt-2 mx-auto">
      <ToastNotification ref="toast" />

      <!-- Form chỉnh sửa -->
      <div class="bg-white shadow-lg rounded-lg p-5 mb-4 mt-4">
        <div class="grid grid-cols-1 gap-6">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Mã Imel</label>
            <input
              v-model.trim="imel.ma"
              type="text"
              placeholder="Nhập mã Imel"
              class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
              required
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Tên Imel</label>
            <input
              v-model.trim="imel.imel"
              type="text"
              placeholder="Nhập tên Imel"
              class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
              required
            />
          </div>
        </div>

        <!-- Nút chức năng -->
        <div class="flex justify-end gap-2 mt-6">
          <button
            @click="goBack"
            class="px-4 py-2 bg-gray-500 text-white font-semibold rounded-lg shadow-md hover:bg-gray-600 transition"
          >
            Hủy
          </button>
          <button
            @click="handleSubmit"
            class="px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition"
          >
            Lưu
          </button>
        </div>
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
import useImel from '@/views/Products/Imel/Imel.js';
import axios from 'axios';

const router = useRouter();
const route = useRoute();
const toast = ref(null);
const originalMa = ref('');

// Tính toán breadcrumb dựa trên meta của route
const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === "function") {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || ["Quản Lý Imel", "Chỉnh Sửa Imel"]; // Mặc định cho trang chỉnh sửa
});

const {
  imel,
  checkDuplicate,
  updateImel,
  showConfirmModal,
  confirmMessage,
  confirmAction,
  executeConfirmedAction,
  closeConfirmModal,
} = useImel();

const fetchImel = async (id) => {
  try {
    const { data } = await axios.get(`http://localhost:8080/api/imel/${id}`);
    imel.value = data;
    originalMa.value = data.ma;
  } catch (error) {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Không thể tải dữ liệu Imel!');
    }
    console.error('Fetch error:', error);
  }
};

onMounted(() => {
  const id = route.params.id;
  if (id) {
    fetchImel(id);
  }
});

const handleSubmit = async () => {
  const { id, ma, imel: imelValue } = imel.value;
  if (!id || !ma || !imelValue) {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Dữ liệu không hợp lệ! Vui lòng kiểm tra lại.');
    }
    return;
  }

  if (ma !== originalMa.value && (await checkDuplicate('ma', ma, id))) {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Mã Imel đã tồn tại!');
    }
    return;
  }

  if (await checkDuplicate('imel', imelValue, id)) {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Tên Imel đã tồn tại!');
    }
    return;
  }

  confirmAction('Bạn có chắc chắn muốn cập nhật Imel này?', async () => {
    await updateImel();
    router.push('/imel');
  });
};

const goBack = () => {
  router.push('/imel');
};
</script>

<style scoped>
.input-field {
  @apply w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-sm;
}
</style>