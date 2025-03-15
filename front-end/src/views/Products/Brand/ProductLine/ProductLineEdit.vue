<template>
  <div class="mt-2 mx-auto">
    <h2 class="bg-white shadow-lg rounded-lg p-5 mb-2 mt-2 text-2xl font-semibold text-gray-700">
      Chỉnh Sửa Dòng Sản Phẩm
    </h2>
    <ToastNotification ref="toast" />

    <!-- Form chỉnh sửa -->
    <div class="bg-white shadow-lg rounded-lg p-5 mb-4 mt-4">
      <div class="grid grid-cols-1 gap-6">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Mã dòng sản phẩm</label>
          <input
            v-model.trim="productLine.ma"
            type="text"
            placeholder="Nhập mã dòng sản phẩm"
            class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
            required
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Tên dòng sản phẩm</label>
          <input
            v-model.trim="productLine.dongSanPham"
            type="text"
            placeholder="Nhập tên dòng sản phẩm"
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
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import ToastNotification from '@/components/ToastNotification.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';
import useProductLineList from '@/views/Products/Brand/ProductLine/ProductLine.js';
import axios from 'axios';

const router = useRouter();
const route = useRoute();
const toast = ref(null);
const originalMa = ref(''); // Lưu trữ mã gốc

const {
  productLine,
  checkDuplicate,
  updateProductLine,
  showConfirmModal,
  confirmMessage,
  confirmAction,
  executeConfirmedAction,
  closeConfirmModal,
} = useProductLineList();

const fetchProductLine = async (id) => {
  try {
    const { data } = await axios.get(`http://localhost:8080/api/dong-san-pham/${id}`);
    productLine.value = data;
    originalMa.value = data.ma; // Lưu mã gốc
  } catch (error) {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Không thể tải dữ liệu dòng sản phẩm!');
    }
    console.error('Fetch error:', error);
  }
};

onMounted(() => {
  const id = route.params.id;
  if (id) {
    fetchProductLine(id);
  }
});

const handleSubmit = async () => {
  const { id, ma, dongSanPham } = productLine.value;
  if (!id || !ma || !dongSanPham) {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Dữ liệu không hợp lệ! Vui lòng kiểm tra lại.');
    }
    return;
  }

  // Chỉ kiểm tra trùng lặp mã nếu mã đã thay đổi
  if (ma !== originalMa.value && (await checkDuplicate('ma', ma, id))) {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Mã dòng sản phẩm đã tồn tại!');
    }
    return;
  }

  // Kiểm tra trùng lặp tên dòng sản phẩm
  if (await checkDuplicate('dongSanPham', dongSanPham, id)) {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Tên dòng sản phẩm đã tồn tại!');
    }
    return;
  }

  confirmAction('Bạn có chắc chắn muốn cập nhật dòng sản phẩm này?', async () => {
    await updateProductLine();
    router.push('/product-line');
  });
};

const goBack = () => {
  router.push('/product-line');
};
</script>

<style scoped>
.input-field {
  @apply w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-sm;
}
</style>