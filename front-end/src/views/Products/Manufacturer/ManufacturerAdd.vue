<template>
  <div>
    <!-- Thêm BreadcrumbWrapper -->
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />

    <div class="mt-2 mx-auto">
      <h2 class="bg-white shadow-lg rounded-lg p-5 mb-2 mt-2 text-2xl font-semibold text-gray-700">
        Thêm Mới Nhà Sản Xuất
      </h2>
      <ToastNotification ref="toast" />

      <!-- Form thêm mới -->
      <div class="bg-white shadow-lg rounded-lg p-5 mb-4 mt-4">
        <div class="grid grid-cols-1 gap-6">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Mã nhà sản xuất</label>
            <input
              v-model.trim="manufacturer.ma"
              type="text"
              placeholder="Nhập mã nhà sản xuất"
              class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
              required
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Tên nhà sản xuất</label>
            <input
              v-model.trim="manufacturer.nhaSanXuat"
              type="text"
              placeholder="Nhập tên nhà sản xuất"
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
            Thêm
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
import { ref, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import ToastNotification from '@/components/ToastNotification.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue'; // Import BreadcrumbWrapper
import useNhaSanXuat from '@/views/Products/Manufacturer/Manufacturer.js';

const router = useRouter();
const route = useRoute();
const toast = ref(null);

// Tính toán breadcrumb dựa trên meta của route
const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === "function") {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || ["Quản Lý Nhà Sản Xuất", "Thêm Mới Nhà Sản Xuất"]; // Mặc định cho trang thêm mới
});

const {
  manufacturer,
  checkDuplicate,
  saveManufacturer,
  showConfirmModal,
  confirmMessage,
  confirmAction,
  executeConfirmedAction,
  closeConfirmModal,
} = useNhaSanXuat();

manufacturer.value = { id: null, ma: '', nhaSanXuat: '' }; // Đặt lại dữ liệu mặc định

const handleSubmit = async () => {
  const { ma, nhaSanXuat } = manufacturer.value;
  if (!ma || !nhaSanXuat) {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Vui lòng nhập đầy đủ thông tin!');
    }
    return;
  }

  if (await checkDuplicate('ma', ma)) {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Mã nhà sản xuất đã tồn tại!');
    }
    return;
  }
  if (await checkDuplicate('nhaSanXuat', nhaSanXuat)) {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Tên nhà sản xuất đã tồn tại!');
    }
    return;
  }

  confirmAction('Bạn có chắc chắn muốn thêm nhà sản xuất này?', async () => {
    await saveManufacturer();
    router.push('/manufacturer'); // Quay lại danh sách sau khi lưu thành công
  });
};

const goBack = () => {
  router.push('/manufacturer'); // Quay lại danh sách
};
</script>

<style scoped>
.input-field {
  @apply w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-sm;
}
</style>