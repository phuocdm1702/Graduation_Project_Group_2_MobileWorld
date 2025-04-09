<template>
  <ToastNotification ref="toastRef" />

  <div class="flex items-center justify-center h-screen px-6 bg-gradient-to-r from-gray-100 to-gray-300">
    <div class="w-full max-w-md p-8 bg-white rounded-xl shadow-lg">
      <div class="flex items-center justify-center mb-6">
        <img class="logo" src="../../../assets/Logo_Mobile_World_vector.png" alt="Logo" />
      </div>
      
      <form @submit.prevent="AddTK">
        <!-- Email (hiển thị từ URL, không cho chỉnh sửa) -->
        <label class="block mb-6 text-center">
          <i class="text-sm italic text-gray-600">{{ taikhoan.email }}</i>
        </label>


        <!-- Mật khẩu mới -->
        <label class="block mb-6 relative">
          <span class="text-sm font-medium text-gray-700">Mật khẩu mới</span>
          <input v-model="taikhoan.matKhau" :type="showPassword ? 'text' : 'password'"
                 class="block w-full mt-1 px-4 py-2 bg-gray-50 border border-gray-300 rounded-md focus:border-orange-500 focus:ring focus:ring-orange-200 focus:ring-opacity-50 transition duration-200"
                 :class="{ 'border-red-500': errors.matKhau }"
                 placeholder="Nhập mật khẩu mới" />
          <span class="absolute right-3 top-10 cursor-pointer eye-icon" @click="togglePassword">
            <i :class="showPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
          </span>
          <span v-if="errors.matKhau" class="text-xs text-red-500 mt-1">{{ errors.matKhau }}</span>
        </label>
        
        <div>
          <button type="submit"
                  class="w-full px-4 py-3 text-sm font-medium text-white bg-orange-500 rounded-md hover:bg-orange-600 focus:outline-none focus:ring focus:ring-orange-300 focus:ring-opacity-50 transition duration-200">
            Xác nhận
          </button>
        </div>
      </form>

      <p class="mt-4 text-center text-sm text-gray-600">
        Đã có tài khoản?
        <router-link to="/" class="text-orange-500 hover:underline">Đăng nhập</router-link>
      </p>
    </div>
  </div>

  <ConfirmModal :show="showConfirmModal" :message="'Bạn có muốn chuyển sang trang đăng nhập không?'"
                @confirm="chuyentrang" @cancel="showConfirmModal = false" />
</template>

<script setup lang="ts">
import axios from "axios";
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import ToastNotification from "@/components/ToastNotification.vue";
import ConfirmModal from "@/components/ConfirmModal.vue";

const showConfirmModal = ref(false);
const router = useRouter();
const route = useRoute();
const toastRef = ref(null);

const showPassword = ref(false);
const showConfirmPassword = ref(false);

// Biến lưu lỗi
const errors = ref({
  matKhau: '',
  confirmPassword: '',
});

const taikhoan = ref({
  email: '',
  matKhau: '',
});

// Lấy email từ query parameter khi component được mount
onMounted(() => {
  taikhoan.value.email = route.query.email || '';
  if (!taikhoan.value.email) {
    router.push('/'); // Nếu không có email thì chuyển về trang chính
  }
});

const togglePassword = () => {
  showPassword.value = !showPassword.value;
};

const toggleConfirmPassword = () => {
  showConfirmPassword.value = !showConfirmPassword.value;
};

const chuyentrang = () => {
  router.push({ path: '/' });
};

// Hàm kiểm tra và gán lỗi
function checktk() {
  let isValid = true;
  errors.value = { matKhau: '', confirmPassword: '' }; // Reset lỗi

  if (!taikhoan.value.matKhau.trim()) {
    errors.value.matKhau = 'Mật khẩu không được để trống!';
    isValid = false;
  }
  
  return isValid;
}

// Hàm cập nhật mật khẩu
async function AddTK() {
  if (!checktk()) {
    return;
  }

  const requestData = {
    email: taikhoan.value.email,
    matKhau: taikhoan.value.matKhau,
  };

  try {
    const response = await axios.post(
      'http://localhost:8080/tai-khoan/update-tk',
      requestData,
      {
        headers: { 'Content-Type': 'application/json' },
      }
    );
    toastRef.value.kshowToast('success', 'Chúc mừng bạn đã cập nhật mật khẩu thành công!');
    setTimeout(() => {
      showConfirmModal.value = true;
    }, 1000);
  } catch (error) {
    let errorMsg = 'Có lỗi xảy ra khi cập nhật mật khẩu!';

    // Kiểm tra và xử lý error.response.data
    if (error.response?.data) {
      if (typeof error.response.data === 'string') {
        errorMsg = error.response.data;
      } else if (typeof error.response.data === 'object' && error.response.data.message) {
        errorMsg = error.response.data.message; // Lấy message từ object nếu có
      }
    }

    // Kiểm tra nội dung lỗi để gán vào field phù hợp
    if (typeof errorMsg === 'string' && errorMsg.includes("Mật khẩu")) {
      errors.value.matKhau = errorMsg;
    } else {
      errors.value.confirmPassword = errorMsg;
    }
  }
}
</script>

<style scoped>
.logo {
  max-width: 100%;
  width: 60%;
  height: auto;
}

.eye-icon {
  opacity: 0.5;
  transition: opacity 0.2s ease;
}

.eye-icon:hover {
  opacity: 1;
}
</style>