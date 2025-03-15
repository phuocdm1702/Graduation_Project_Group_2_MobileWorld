<template>
  <div class="mt-2 mx-auto">
    <h2 class="bg-white shadow-lg rounded-lg p-5 mb-2 mt-2 text-2xl font-semibold text-gray-700">
      Thêm Mới Dòng Sản Phẩm
    </h2>
    <ToastNotification ref="toast" />

    <!-- Form thêm mới -->
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
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import ToastNotification from '@/components/ToastNotification.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';
import useProductLineList from '@/views/Products/Brand/ProductLine/ProductLine.js';

const router = useRouter();
const toast = ref(null);

const {
  productLine,
  checkDuplicate,
  saveProductLine,
  showConfirmModal,
  confirmMessage,
  confirmAction,
  executeConfirmedAction,
  closeConfirmModal,
} = useProductLineList();

productLine.value = { id: null, ma: '', dongSanPham: '' }; // Đặt lại dữ liệu mặc định

const handleSubmit = async () => {
  const { ma, dongSanPham } = productLine.value;
  if (!ma || !dongSanPham) {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Vui lòng nhập đầy đủ thông tin!');
    }
    return;
  }

  if (await checkDuplicate('ma', ma)) {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Mã dòng sản phẩm đã tồn tại!');
    }
    return;
  }
  if (await checkDuplicate('dongSanPham', dongSanPham)) {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Tên dòng sản phẩm đã tồn tại!');
    }
    return;
  }

  confirmAction('Bạn có chắc chắn muốn thêm dòng sản phẩm này?', async () => {
    await saveProductLine();
    router.push('/product-line'); // Quay lại danh sách sau khi lưu thành công
  });
};

const goBack = () => {
  router.push('/product-line'); // Quay lại danh sách
};
</script>

<style scoped>
.input-field {
  @apply w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-sm;
}
</style>