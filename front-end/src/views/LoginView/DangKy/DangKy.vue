<template>
  <ToastNotification ref="toastRef" />

  <div class="flex items-center justify-center h-screen px-6 bg-gradient-to-r from-gray-100 to-gray-300">
    <div class="w-full max-w-md p-8 bg-white rounded-xl shadow-lg">
      <div class="flex items-center justify-center mb-6">
        <img class="logo" src="../../../assets/Logo_Mobile_World_vector.png" alt="Logo">
      </div>

      <h2 class="text-2xl font-semibold text-center text-gray-800 mb-6">Đăng Ký Tài Khoản</h2>

      <form @submit.prevent>
        <!-- Tên đăng nhập -->
        <label class="block mb-4">
          <span class="text-sm font-medium text-gray-700">Tên đăng nhập</span>
          <input v-model="taikhoan.tenDangNhap" type="text"
                 class="block w-full mt-1 px-4 py-2 bg-gray-50 border border-gray-300 rounded-md focus:border-orange-500 focus:ring focus:ring-orange-200 focus:ring-opacity-50 transition duration-200"
                 :class="{ 'border-red-500': errors.tenDangNhap }"
                 placeholder="Nhập Tên đăng nhập của bạn" />
          <span v-if="errors.tenDangNhap" class="text-xs text-red-500 mt-1">{{ errors.tenDangNhap }}</span>
        </label>

        <!-- Email -->
        <label class="block mb-4 relative">
          <span class="text-sm font-medium text-gray-700">Email</span>
          <div class="relative">
            <input v-model="taikhoan.email" type="email"
                   class="block w-full mt-1 px-4 py-2 bg-gray-50 border border-gray-300 rounded-md focus:border-orange-500 focus:ring focus:ring-orange-200 focus:ring-opacity-50 transition duration-200 pr-24"
                   :class="{ 'border-red-500': errors.email }"
                   placeholder="Nhập email của bạn" />
            <button type="button" @click="sendOTP"
                    class="absolute right-2 top-1/2 transform -translate-y-1/2 px-3 py-1 text-sm font-medium text-white bg-orange-600 rounded-md hover:bg-orange-700 transition duration-200">
              Gửi mã
            </button>
          </div>
          <span v-if="errors.email" class="text-xs text-red-500 mt-1">{{ errors.email }}</span>
        </label>

        <!-- Mã xác nhận -->
        <label class="block mb-4">
          <span class="text-sm font-medium text-gray-700">Mã xác nhận</span>
          <input v-model="otp" type="text"
                 class="block w-full mt-1 px-4 py-2 bg-gray-50 border border-gray-300 rounded-md focus:border-orange-500 focus:ring focus:ring-orange-200 focus:ring-opacity-50 transition duration-200"
                 :class="{ 'border-red-500': errors.otp }"
                 placeholder="Nhập mã xác nhận từ email" />
          <span v-if="errors.otp" class="text-xs text-red-500 mt-1">{{ errors.otp }}</span>
        </label>

        <!-- Mật khẩu -->
        <label class="block mb-6 relative">
          <span class="text-sm font-medium text-gray-700">Mật khẩu</span>
          <input v-model="taikhoan.matKhau" :type="showPassword ? 'text' : 'password'"
                 class="block w-full mt-1 px-4 py-2 bg-gray-50 border border-gray-300 rounded-md focus:border-orange-500 focus:ring focus:ring-orange-200 focus:ring-opacity-50 transition duration-200"
                 :class="{ 'border-red-500': errors.matKhau }"
                 placeholder="Nhập mật khẩu" />
          <span class="absolute right-3 top-10 cursor-pointer eye-icon" @click="togglePassword">
            <i :class="showPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
          </span>
          <span v-if="errors.matKhau" class="text-xs text-red-500 mt-1">{{ errors.matKhau }}</span>
        </label>

        <!-- Xác nhận mật khẩu -->
        <label class="block mb-6 relative">
          <span class="text-sm font-medium text-gray-700">Xác nhận mật khẩu</span>
          <input v-model="confirmPassword" :type="showConfirmPassword ? 'text' : 'password'"
                 class="block w-full mt-1 px-4 py-2 bg-gray-50 border border-gray-300 rounded-md focus:border-orange-500 focus:ring focus:ring-orange-200 focus:ring-opacity-50 transition duration-200"
                 :class="{ 'border-red-500': errors.confirmPassword }"
                 placeholder="Xác nhận mật khẩu" />
          <span class="absolute right-3 top-10 cursor-pointer eye-icon" @click="toggleConfirmPassword">
            <i :class="showConfirmPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
          </span>
          <span v-if="errors.confirmPassword" class="text-xs text-red-500 mt-1">{{ errors.confirmPassword }}</span>
        </label>

        <div>
          <button type="submit" @click="AddTK"
                  class="w-full px-4 py-3 text-sm font-medium text-white bg-orange-500 rounded-md hover:bg-orange-600 focus:outline-none focus:ring focus:ring-orange-300 focus:ring-opacity-50 transition duration-200">
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

  <ConfirmModal :show="showConfirmModal" :message="'Bạn có muốn chuyển sang trang đăng nhập không?'"
                @confirm="chuyentrang" @cancel="showConfirmModal = false" />
</template>

<script setup lang="ts">
import axios from "axios";
import { ref } from "vue";
import { useRouter } from "vue-router";
import ToastNotification  from "@/components/ToastNotification.vue";
import ConfirmModal from "@/components/ConfirmModal.vue";

const showConfirmModal = ref(false);
const router = useRouter();
const toastRef = ref(null);

const showPassword = ref(false);
const showConfirmPassword = ref(false);
const confirmPassword = ref('');
const otp = ref('');

// Biến lưu lỗi
const errors = ref({
  tenDangNhap: '',
  email: '',
  otp: '',
  matKhau: '',
  confirmPassword: '',
});

const taikhoan = ref({
  tenDangNhap: '',
  email: '',
  soDienThoai: '',
  matKhau: '',
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
  errors.value = { tenDangNhap: '', email: '', otp: '', matKhau: '', confirmPassword: '' }; // Reset lỗi

  if (!taikhoan.value.tenDangNhap.trim()) {
    errors.value.tenDangNhap = 'Tên đăng nhập không được để trống!';
    isValid = false;
  }
  if (!taikhoan.value.email.trim()) {
    errors.value.email = 'Email không được để trống!';
    isValid = false;
  } else if (!/\S+@\S+\.\S+/.test(taikhoan.value.email)) {
    errors.value.email = 'Email không hợp lệ!';
    isValid = false;
  }
  if (!taikhoan.value.matKhau.trim()) {
    errors.value.matKhau = 'Mật khẩu không được để trống!';
    isValid = false;
  }
  if (!confirmPassword.value.trim()) {
    errors.value.confirmPassword = 'Xác nhận mật khẩu không được để trống!';
    isValid = false;
  }
  if (taikhoan.value.matKhau && confirmPassword.value && taikhoan.value.matKhau !== confirmPassword.value) {
    errors.value.confirmPassword = 'Mật khẩu và xác nhận mật khẩu không khớp!';
    isValid = false;
  }

  return isValid;
}

// Hàm gửi OTP
async function sendOTP() {
  errors.value.tenDangNhap = ''; 
  errors.value.email = ''; 
  if (!taikhoan.value.tenDangNhap.trim()) {
    errors.value.tenDangNhap = 'Tên đăng nhập không được để trống!';
    return;
  }
  if (!taikhoan.value.email.trim()) {
    errors.value.email = 'Email không được để trống!';
    return;
  }
  if (!/\S+@\S+\.\S+/.test(taikhoan.value.email)) {
    errors.value.email = 'Email không hợp lệ!';
    return;
  }

  try {
    const response = await axios.post('http://localhost:8080/tai-khoan/requestOtp', {
      email: taikhoan.value.email,
      tenDangNhap: taikhoan.value.tenDangNhap,
      matKhau: taikhoan.value.matKhau
    }, {
      headers: { 'Content-Type': 'application/json' },
    });
    toastRef.value.kshowToast('success', response.data);
  } catch (error) {
    const errorMsg = error.response?.data || 'Lỗi khi gửi OTP!';
    if (errorMsg.includes("Tên đăng nhập")) {
      errors.value.tenDangNhap = errorMsg;
    } else if (errorMsg.includes("Email")) {
      errors.value.email = errorMsg;
    } else {
      errors.value.email = errorMsg;
    }
  }
}

// Hàm đăng ký
async function AddTK() {
  if (!checktk()) {
    return;
  }
  if (!otp.value.trim()) {
    errors.value.otp = 'Vui lòng nhập mã OTP!';
    return;
  }

  const requestData = {
    tenDangNhap: taikhoan.value.tenDangNhap,
    email: taikhoan.value.email,
    matKhau: taikhoan.value.matKhau,
    otp: otp.value,
  };

  try {
    const response = await axios.post('http://localhost:8080/tai-khoan/addTk', requestData, {
      headers: { 'Content-Type': 'application/json' },
    });
    toastRef.value.kshowToast('success', 'Chúc mừng bạn đã đăng ký tài khoản thành công!');
    setTimeout(() => {
      showConfirmModal.value = true;
    }, 1000);
  } catch (error) {
    const errorMsg = error.response?.data.split(": ")[1] || 'Mã OTP không hợp lệ!';
    if (errorMsg.includes("Tên đăng nhập")) {
      errors.value.tenDangNhap = errorMsg;
    } else if (errorMsg.includes("Email")) {
      errors.value.email = errorMsg;
    } else {
      errors.value.otp = errorMsg;
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