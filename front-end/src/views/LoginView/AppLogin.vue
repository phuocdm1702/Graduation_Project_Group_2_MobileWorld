<template>
  <div class="flex items-center justify-center h-screen px-6 bg-gray-200">
    <div class="w-full max-w-sm p-6 bg-white rounded-md shadow-md">
      <div class="flex items-center justify-center">
        <span class="text-2xl font-semibold text-orange-500">Mobile World</span>
      </div>

      <form class="mt-4" @submit.prevent="login">
        <label class="block">
          <span class="text-sm text-gray-700">Tên đăng nhập</span>
          <input
            type="text"
            class="
              block
              w-full
              mt-1
              border-gray-200
              rounded-md
              focus:border-indigo-600
              focus:ring
              focus:ring-opacity-40
              focus:ring-indigo-500
            "
            v-model="tenDangNhap"
          />
        </label>

        <label class="block mt-3">
          <span class="text-sm text-gray-700">Mật khẩu</span>
          <input
            type="password"
            class="
              block
              w-full
              mt-1
              border-gray-200
              rounded-md
              focus:border-indigo-600
              focus:ring
              focus:ring-opacity-40
              focus:ring-indigo-500
            "
            v-model="matKhau"
          />
        </label>

        <div class="flex items-center justify-between mt-4">
          <div>
            <router-link to="/dang-ky">
              <a class="block text-sm text-orange-500 fontme hover:underline">Đăng ký?</a>
            </router-link>
          </div>

          <div>
            <a class="block text-sm text-orange-500 fontme hover:underline" href="#">Forgot your password?</a>
          </div>
        </div>

        <div class="mt-6">
          <button
            type="submit"
            class="
              w-full
              px-4
              py-2
              text-sm text-center text-white
              bg-orange-500
              rounded-md
              focus:outline-none
              hover:bg-orange-600
            "
          >
            Sign in
          </button>
        </div>

        <!-- Hiển thị thông báo lỗi nếu có -->
        <p v-if="error" class="mt-2 text-sm text-red-500">{{ error }}</p>
      </form>

      <!-- Component ConfirmModal -->
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
import useLogin from "@/views/LoginView/Login.js"; // Điều chỉnh đường dẫn nếu cần
import ConfirmModal from "@/components/ConfirmModal.vue"; // Điều chỉnh đường dẫn tới ConfirmModal

const {
  tenDangNhap,
  matKhau,
  error,
  showConfirmModal,
  confirmMessage,
  login,
  executeConfirmedAction,
  closeConfirmModal,
} = useLogin();
</script>