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
import useNhaSanXuat from '@/views/Products/Manufacturer/Manufacturer.js';
import axios from 'axios';

const router = useRouter();
const route = useRoute();
const toast = ref(null);
const originalMa = ref(''); // Lưu trữ mã gốc

// Tính toán breadcrumb dựa trên meta của route
const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === "function") {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || ["Quản Lý Nhà Sản Xuất", "Chỉnh Sửa Nhà Sản Xuất"]; // Mặc định cho trang chỉnh sửa
});

const {
  manufacturer,
  checkDuplicate,
  updateManufacturer,
  showConfirmModal,
  confirmMessage,
  confirmAction,
  executeConfirmedAction,
  closeConfirmModal,
} = useNhaSanXuat();

const fetchManufacturer = async (id) => {
  try {
    const { data } = await axios.get(`http://localhost:8080/nha-san-xuat/${id}`);
    manufacturer.value = data;
    originalMa.value = data.ma; // Lưu mã gốc
  } catch (error) {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Không thể tải dữ liệu nhà sản xuất!');
    }
    console.error('Fetch error:', error);
  }
};

onMounted(() => {
  const id = route.params.id;
  if (id) {
    fetchManufacturer(id);
  }
});

const handleSubmit = async () => {
  const { id, ma, nhaSanXuat } = manufacturer.value;
  if (!id || !ma || !nhaSanXuat) {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Dữ liệu không hợp lệ! Vui lòng kiểm tra lại.');
    }
    return;
  }

  // Chỉ kiểm tra trùng lặp mã nếu mã đã thay đổi
  if (ma !== originalMa.value && (await checkDuplicate('ma', ma, id))) {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Mã nhà sản xuất đã tồn tại!');
    }
    return;
  }

  // Kiểm tra trùng lặp tên nhà sản xuất
  if (await checkDuplicate('nhaSanXuat', nhaSanXuat, id)) {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Tên nhà sản xuất đã tồn tại!');
    }
    return;
  }

  confirmAction('Bạn có chắc chắn muốn cập nhật nhà sản xuất này?', async () => {
    await updateManufacturer();
    router.push('/manufacturer');
  });
};

const goBack = () => {
  router.push('/manufacturer');
};
</script>

<style scoped>
.input-field {
  @apply w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-sm;
}
</style>