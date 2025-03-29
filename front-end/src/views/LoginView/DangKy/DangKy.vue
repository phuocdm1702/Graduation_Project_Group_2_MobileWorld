<template>
  <ToastNotification ref="toastRef" />

  <div class="flex items-center justify-center h-screen px-6 bg-gradient-to-r from-gray-100 to-gray-300">
    <div class="w-full max-w-md p-8 bg-white rounded-xl shadow-lg">
      <div class="flex items-center justify-center mb-6">
        <img class="logo" src="../../../assets/Logo_Mobile_World_vector.png" alt="Logo">
      </div>

      <h2 class="text-2xl font-semibold text-center text-gray-800 mb-6">Đăng Ký Tài Khoản</h2>

      <form @submit.prevent>
        <label class="block mb-4">
          <span class="text-sm font-medium text-gray-700">Tên đăng nhập</span>
          <input v-model="taikhoan.tenDangNhap"
                 type="text" 
          class="
          block
          w-full
          mt-1
          px-4
          py-2
          bg-gray-50
          border
          border-gray-300
          rounded-md
          focus:border-orange-500
          focus:ring
          focus:ring-orange-200
          focus:ring-opacity-50
          transition
          duration-200
          "
          placeholder="Nhập UserName của bạn"
          />
        </label>

        <!-- Trường Mật khẩu -->
        <label class="block mb-6 relative">
          <span class="text-sm font-medium text-gray-700">Mật khẩu</span>
          <input v-model="taikhoan.matKhau"
                 :type="showPassword ? 'text' : 'password'"
                 class="
      block
      w-full
      mt-1
      px-4
      py-2
      bg-gray-50
      border
      border-gray-300
      rounded-md
      focus:border-orange-500
      focus:ring
      focus:ring-orange-200
      focus:ring-opacity-50
      transition
      duration-200
    "
                 placeholder="Nhập mật khẩu"
          />
          <!-- Biểu tượng mắt -->
          <span class="absolute right-3 top-10 cursor-pointer eye-icon" @click="togglePassword">
    <i :class="showPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
  </span>
        </label>

        <!-- Trường Xác nhận mật khẩu -->
        <label class="block mb-6 relative">
          <span class="text-sm font-medium text-gray-700">Xác nhận mật khẩu</span>
          <input
            v-model="confirmPassword"
            :type="showConfirmPassword ? 'text' : 'password'"
            class="
      block
      w-full
      mt-1
      px-4
      py-2
      bg-gray-50
      border
      border-gray-300
      rounded-md
      focus:border-orange-500
      focus:ring
      focus:ring-orange-200
      focus:ring-opacity-50
      transition
      duration-200
    "
            placeholder="Xác nhận mật khẩu"
          />
          <!-- Biểu tượng mắt -->
          <span class="absolute right-3 top-10 cursor-pointer eye-icon" @click="toggleConfirmPassword">
    <i :class="showConfirmPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
  </span>
        </label>

        <div>
          <button
            type="submit"
            @click="AddTK"
            class="
              w-full
              px-4
              py-3
              text-sm
              font-medium
              text-white
              bg-orange-500
              rounded-md
              hover:bg-orange-600
              focus:outline-none
              focus:ring
              focus:ring-orange-300
              focus:ring-opacity-50
              transition
              duration-200
            "
          >
            Đăng Ký
          </button>
        </div>
      </form>

      <p class="mt-4 text-center text-sm text-gray-600">
        Đã có tài khoản?
        <router-link to="/" class="text-orange-500 hover:underline">Đăng nhập</router-link>
      </p>
    </div>
  </div>

  <ConfirmModal
    :show="showConfirmModal"
    :message="'Bạn có muốn chuyển sang trang đăng nhập không?'"
    @confirm="chuyentrang"
    @cancel="showConfirmModal = false"
  />
  
</template>

<script setup lang="ts">
import axios from "axios";
import { ref } from "vue";
import { useRouter } from "vue-router";
import ToastNotification from "@/components/ToastNotification.vue";
import ConfirmModal from "@/components/ConfirmModal.vue";
import {tr} from "@faker-js/faker";

const showConfirmModal = ref(false);
const router = useRouter();
const toastRef = ref(null);

// Biến trạng thái để điều khiển hiển thị mật khẩu
const showPassword = ref(false);
const showConfirmPassword = ref(false);

// Biến để lưu giá trị xác nhận mật khẩu
const confirmPassword = ref('');

// Dữ liệu tài khoản
const taikhoan = ref({
  tenDangNhap: '',
  email: '',
  soDienThoai: '',
  matKhau: '',
});

// Hàm chuyển đổi trạng thái hiển thị mật khẩu
const togglePassword = () => {
  showPassword.value = !showPassword.value;
};

// Hàm chuyển đổi trạng thái hiển thị xác nhận mật khẩu
const toggleConfirmPassword = () => {
  showConfirmPassword.value = !showConfirmPassword.value;
};

//chuyentrang
const chuyentrang = () => {
  router.push({
    path: '/',
  });
}

function checktk() {
  if (!taikhoan.value.tenDangNhap.trim()) {
    toastRef.value.kshowToast('error', 'Tên đăng nhập không được để trống!');
    return false;
  }
  if (!taikhoan.value.matKhau.trim()) {
    toastRef.value.kshowToast('error', 'Mật khẩu không được để trống!');
    return false;
  }
  if (!confirmPassword.value.trim()) {
    toastRef.value.kshowToast('error', 'Xác nhận mật khẩu không được để trống!');
    return false;
  }
  if (taikhoan.value.matKhau !== confirmPassword.value) {
    toastRef.value.kshowToast('error', 'Mật khẩu và xác nhận mật khẩu không khớp!');
    return false;
  }
  return true;
}

async function AddTK() {
  if (!checktk()) {
    return;
  }
  const taikhoanData = {
    tenDangNhap: taikhoan.value.tenDangNhap,
    email: taikhoan.value.email,
    soDienThoai: taikhoan.value.soDienThoai,
    matKhau: taikhoan.value.matKhau,
  };

  try {
    const response = await axios.post('http://localhost:8080/tai-khoan/addTk', taikhoanData, {
      headers: { 'Content-Type': 'application/json' },
    });
    toastRef.value.kshowToast('success','Chúc mừng bạn đã đăng ký tài khoản thành công!');
    setTimeout(() => {
      showConfirmModal.value = true;
    }, 1000);
  } catch (error) {
    console.error('Lỗi khi thêm tài khoản:', error);
    toastRef.value?.kshowToast('error', 'Đăng ký thất bại: ' + (error.response?.data?.message || error.message));
  }
}
</script>

<style scoped>
.logo {
  max-width: 100%;
  width: 60%;
  height: auto;
}
/* Làm mờ biểu tượng mắt */
.eye-icon {
  opacity: 0.5; /* Làm mờ đi, giá trị từ 0 (hoàn toàn mờ) đến 1 (hoàn toàn rõ) */
  transition: opacity 0.2s ease; /* Thêm hiệu ứng chuyển đổi mượt mà */
}

/* Khi hover, làm biểu tượng rõ hơn */
.eye-icon:hover {
  opacity: 1; /* Trở nên rõ hoàn toàn khi hover */
}
</style>