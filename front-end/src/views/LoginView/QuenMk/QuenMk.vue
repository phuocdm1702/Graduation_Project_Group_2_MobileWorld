<template>
  <ToastNotification ref="toastRef" />

  <div class="flex items-center justify-center min-h-screen px-6 bg-gradient-to-r from-gray-100 to-gray-300">
    <div class="w-full max-w-md p-8 bg-white rounded-xl shadow-lg">
      <div class="flex items-center justify-center mb-6">
        <img class="logo w-32" src="../../../assets/Logo_Mobile_World_vector.png" alt="Logo">
      </div>

      <form @submit.prevent="submitForm">
        <!-- Email -->
        <label class="block mb-4 relative">
          <span class="text-sm font-medium text-gray-700">Email</span>
          <div class="relative">
            <input
              v-model="taikhoan.email"
              type="email"
              class="block w-full mt-1 px-4 py-2 bg-gray-50 border border-gray-300 rounded-md focus:border-orange-500 focus:ring focus:ring-orange-200 focus:ring-opacity-50 transition duration-200 pr-24"
              :class="{ 'border-red-500': errors.email }"
              placeholder="Nhập email của bạn"
              @input="clearError('email')"
            />
            <button
              type="button"
              @click="sendOTP"
              :disabled="isSendingOTP"
              class="absolute right-2 top-1/2 transform -translate-y-1/2 px-3 py-1 text-sm font-medium text-white bg-orange-600 rounded-md hover:bg-orange-700 transition duration-200 disabled:bg-gray-400"
            >
              {{ isSendingOTP ? 'Đang gửi...' : 'Gửi mã' }}
            </button>
          </div>
          <span v-if="errors.email" class="text-xs text-red-500 mt-1">{{ errors.email }}</span>
        </label>

        <!-- Mã xác nhận -->
        <label class="block mb-6">
          <span class="text-sm font-medium text-gray-700">Mã xác nhận</span>
          <input
            v-model="otp"
            type="text"
            class="block w-full mt-1 px-4 py-2 bg-gray-50 border border-gray-300 rounded-md focus:border-orange-500 focus:ring focus:ring-orange-200 focus:ring-opacity-50 transition duration-200"
            :class="{ 'border-red-500': errors.otp }"
            placeholder="Nhập mã xác nhận từ email"
            @input="clearError('otp')"
          />
          <span v-if="errors.otp" class="text-xs text-red-500 mt-1">{{ errors.otp }}</span>
        </label>

        <div>
          <button
            type="submit"
            :disabled="isSubmitting"
            class="w-full px-4 py-3 text-sm font-medium text-white bg-orange-500 rounded-md hover:bg-orange-600 focus:outline-none focus:ring focus:ring-orange-300 focus:ring-opacity-50 transition duration-200 disabled:bg-gray-400"
          >
            {{ isSubmitting ? 'Đang xử lý...' : 'Xác nhận' }}
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

const router = useRouter();
const toastRef = ref<InstanceType<typeof ToastNotification> | null>(null);
const showConfirmModal = ref(false);
const otp = ref('');
const isSendingOTP = ref(false);
const isSubmitting = ref(false);

// Biến lưu lỗi
const errors = ref({
  email: '',
  otp: '',
});

const taikhoan = ref({
  email: '',
});

// Chuyển trang
const chuyentrang = () => {
  router.push({ path: '/' });
};

// Xóa lỗi khi người dùng nhập
const clearError = (field: keyof typeof errors.value) => {
  errors.value[field] = '';
};

// Validate form
const validateForm = () => {
  let isValid = true;
  errors.value = { email: '', otp: '' };

  if (!taikhoan.value.email.trim()) {
    errors.value.email = 'Email không được để trống!';
    isValid = false;
  } else if (!/\S+@\S+\.\S+/.test(taikhoan.value.email)) {
    errors.value.email = 'Email không hợp lệ!';
    isValid = false;
  }

  if (!otp.value.trim()) {
    errors.value.otp = 'Mã xác nhận không được để trống!';
    isValid = false;
  } else if (otp.value.length < 6) {
    errors.value.otp = 'Mã xác nhận phải có ít nhất 6 ký tự!';
    isValid = false;
  }

  return isValid;
};

// Gửi OTP
const sendOTP = async () => {
  if (isSendingOTP.value) return;

  clearError('email');
  if (!taikhoan.value.email.trim()) {
    errors.value.email = 'Email không được để trống!';
    return;
  }
  if (!/\S+@\S+\.\S+/.test(taikhoan.value.email)) {
    errors.value.email = 'Email không hợp lệ!';
    return;
  }

  isSendingOTP.value = true;
  try {
    const response = await axios.post('http://localhost:8080/tai-khoan/sendOTPQuenMk', {
      email: taikhoan.value.email,
    }, {
      headers: { 'Content-Type': 'application/json' },
    });
    toastRef.value?.kshowToast('success', response.data || 'Mã OTP đã được gửi!');
  } catch (error) {
    const errorMsg = error.response?.data || 'Lỗi khi gửi OTP!';
    errors.value.email = errorMsg;
  } finally {
    isSendingOTP.value = false;
  }
};

// Submit form
const submitForm = async () => {
  if (!validateForm() || isSubmitting.value) return;

  isSubmitting.value = true;
  try {
    const response = await axios.post('http://localhost:8080/tai-khoan/requestOtp', {
      email: taikhoan.value.email,
      otp: otp.value,
    }, {
      headers: { 'Content-Type': 'application/json' },
    });

    toastRef.value?.kshowToast('success', 'Xác nhận thành công!');
    // Chuyển hướng sau khi xác nhận thành công
    setTimeout(() => router.push('/reset-password'), 1000);
  } catch (error) {
    const errorMsg = error.response?.data || 'Mã OTP không hợp lệ!';
    errors.value.otp = errorMsg;
  } finally {
    isSubmitting.value = false;
  }
};
</script>

<style scoped>
.logo {
  max-width: 100%;
  height: auto;
}
</style>