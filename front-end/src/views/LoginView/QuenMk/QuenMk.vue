<template>
  <ToastNotification ref="toastRef" />

  <div class="flex items-center justify-center h-screen px-6 bg-gradient-to-r from-gray-100 to-gray-300">
    <div class="w-full max-w-md p-8 bg-white rounded-xl shadow-lg">
      <div class="flex items-center justify-center mb-6">
        <img class="logo" src="../../../assets/Logo_Mobile_World_vector.png" alt="Logo">
      </div>
      
      <form @submit.prevent="AddTK">
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

        <!-- Nút gửi -->
        <div>
          <button type="submit"
                  class="w-full px-4 py-3 text-sm font-medium text-white bg-orange-500 rounded-md hover:bg-orange-600 focus:outline-none focus:ring focus:ring-orange-300 focus:ring-opacity-50 transition duration-200">
            Xác nhận OTP
          </button>
        </div>
      </form>

      <p class="mt-4 text-center text-sm text-gray-600">
        Đã có tài khoản?
        <router-link to="/" class="text-orange-500 hover:underline">Đăng nhập</router-link>
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import axios from "axios";
import { ref } from "vue";
import { useRouter } from "vue-router";
import ToastNotification from "@/components/ToastNotification.vue";

const router = useRouter();
const toastRef = ref(null);

const otp = ref('');

// Biến lưu lỗi
const errors = ref({
  email: '',
  otp: '',
});

const taikhoan = ref({
  tenDangNhap: '',
  email: '',
  soDienThoai: '',
  matKhau: '',
});

// Hàm kiểm tra và gán lỗi
function checktk() {
  let isValid = true;
  errors.value = { email: '', otp: '' }; // Reset lỗi

  if (!taikhoan.value.email.trim()) {
    errors.value.email = 'Email không được để trống!';
    isValid = false;
  } else if (!/\S+@\S+\.\S+/.test(taikhoan.value.email)) {
    errors.value.email = 'Email không hợp lệ!';
    isValid = false;
  }
  return isValid;
}

// Hàm gửi OTP
async function sendOTP() {
  if (!taikhoan.value.email.trim()) {
    errors.value.email = 'Email không được để trống!';
    return;
  }
  if (!/\S+@\S+\.\S+/.test(taikhoan.value.email)) {
    errors.value.email = 'Email không hợp lệ!';
    return;
  }

  try {
    const response = await axios.post('http://localhost:8080/tai-khoan/requestOtpMk', {
      email: taikhoan.value.email,
    }, {
      headers: { 'Content-Type': 'application/json' },
    });

    // Kiểm tra phản hồi từ backend
    if (response.data === 'OTP đã được gửi đến email của bạn!') {
      toastRef.value.kshowToast('success', response.data);
    } else {
      errors.value.email = response.data; // Hiển thị "Email không tồn tại trong hệ thống!"
    }
  } catch (error) {
    const errorMsg = error.response?.data || 'Lỗi khi gửi OTP!';
    errors.value.email = errorMsg;
  }
}

// Hàm xác nhận OTP và chuyển trang
async function AddTK() {
  if (!checktk()) {
    return;
  }
  if (!otp.value.trim()) {
    errors.value.otp = 'Vui lòng nhập mã OTP!';
    return;
  }

  const requestData = {
    email: taikhoan.value.email,
  };

  try {
    const response = await axios.post(
      `http://localhost:8080/tai-khoan/verifyOtp?otp=${encodeURIComponent(otp.value)}`,
      requestData,
      {
        headers: { 'Content-Type': 'application/json' },
      }
    );
    toastRef.value.kshowToast('success', 'Xác nhận OTP thành công!');
    setTimeout(() => {
      router.push({path: '/update-mk',
        query: { email: taikhoan.value.email }});// Chuyển sang trang /update-mk
    }, 1000);
  } catch (error) {
    const errorMsg = error.response?.data || 'Mã OTP không hợp lệ!';
    errors.value.otp = errorMsg;
  }
}
</script>

<style scoped>
.logo {
  max-width: 100%;
  width: 60%;
  height: auto;
}
</style>