<template>
  <div class="container mx-auto p-6 max-w-2xl">
    <!-- Toast thông báo -->
    <ToastNotification ref="toast" />

    <!-- Tiêu đề -->
    <h2 class="text-2xl font-semibold text-gray-800 mb-6 flex items-center">
      <i class="fa-solid fa-plus-circle text-green-500 mr-2"></i>
      Thêm Dòng Sản Phẩm
    </h2>

    <!-- Form thêm mới -->
    <div class="bg-white p-6 rounded-xl shadow-lg border border-gray-100">
      <form @submit.prevent="saveProductLine" class="space-y-6">
        <div class="grid grid-cols-1 gap-6">
          <!-- Nhập mã dòng sản phẩm -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Mã dòng sản phẩm</label>
            <input
              v-model.trim="productLine.ma"
              type="text"
              placeholder="Nhập mã dòng sản phẩm"
              class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200"
              required
            />
          </div>

          <!-- Nhập tên dòng sản phẩm -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Tên dòng sản phẩm</label>
            <input
              v-model.trim="productLine.dongSanPham"
              type="text"
              placeholder="Nhập tên dòng sản phẩm"
              class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent transition-all duration-200"
              required
            />
          </div>
        </div>

        <!-- Nút thêm mới và hủy -->
        <div class="flex justify-end gap-4">
          <button
            type="button"
            @click="goBack"
            class="bg-gray-200 text-gray-700 px-5 py-2 rounded-lg hover:bg-gray-300 transition-all duration-200 font-medium"
          >
            Hủy
          </button>
          <button
            type="submit"
            class="bg-green-600 text-white px-5 py-2 rounded-lg hover:bg-green-700 transition-all duration-200 font-medium flex items-center"
          >
            <i class="fa-solid fa-save mr-2"></i> Thêm mới
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import ToastNotification from '@/components/ToastNotification.vue';
import { useRouter } from 'vue-router';

// Khởi tạo các biến
const toast = ref(null);
const productLine = ref({ ma: '', dongSanPham: '' });
const router = useRouter();

// Hàm kiểm tra trùng lặp
const checkDuplicate = async (field, value) => {
  try {
    const { data } = await axios.get(`http://localhost:8080/api/dong-san-pham/exists/${field}`, {
      params: { [field]: value },
    });
    return data;
  } catch (error) {
    toast.value?.showToast('error', `Lỗi kiểm tra ${field}!`);
    return false;
  }
};

// Hàm lưu dòng sản phẩm
const saveProductLine = async () => {
  const { ma, dongSanPham } = productLine.value;

  // Kiểm tra dữ liệu nhập
  if (!ma || !dongSanPham) {
    toast.value?.showToast('error', 'Vui lòng nhập đầy đủ thông tin!');
    return;
  }

  // Kiểm tra trùng mã
  if (await checkDuplicate('ma', ma)) {
    toast.value?.showToast('error', 'Mã dòng sản phẩm đã tồn tại!');
    return;
  }

  // Kiểm tra trùng tên
  if (await checkDuplicate('dongSanPham', dongSanPham)) {
    toast.value?.showToast('error', 'Tên dòng sản phẩm đã tồn tại!');
    return;
  }

  try {
    // Gọi API thêm mới
    await axios.post('http://localhost:8080/api/dong-san-pham', productLine.value);
    toast.value?.showToast('success', 'Thêm mới thành công!');

    // Chuyển hướng về trang danh sách
    router.push('/dong-san-pham');
  } catch (error) {
    toast.value?.showToast('error', 'Lỗi khi lưu dữ liệu!');
  }
};

// Hàm quay lại trang danh sách
const goBack = () => {
  router.push('/dong-san-pham');
};
</script>

<style scoped>
/* Tùy chỉnh thêm nếu cần */
.container {
  display: flex;
  flex-direction: column;
  justify-content: center;
}
</style>