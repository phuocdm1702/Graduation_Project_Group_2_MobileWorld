<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100 px-4 py-12">
    <div class="w-full max-w-md bg-white rounded-lg shadow-lg p-8">
      <!-- Header -->
      <div class="mb-8 text-center">
        <h1 class="text-2xl font-semibold text-gray-800">Đăng nhập</h1>
      </div>

      <!-- Form -->
      <form @submit.prevent="login" class="space-y-6">
        <!-- Username Field -->
        <div class="space-y-2">
          <label class="block text-sm font-medium text-gray-700">
            Tên đăng nhập
          </label>
          <input
            v-model="tenDangNhap"
            type="text"
            placeholder="Nhập tên đăng nhập"
            class="w-full px-4 py-2 border border-gray-300 rounded-md 
                  focus:ring-2 focus:ring-orange-500 focus:border-orange-500 
                  transition duration-200 ease-in-out"
          />
        </div>

        <!-- Password Field -->
        <div class="space-y-2">
          <label class="block text-sm font-medium text-gray-700">
            Mật khẩu
          </label>
          <input
            v-model="matKhau"
            type="password"
            placeholder="Nhập mật khẩu"
            class="w-full px-4 py-2 border border-gray-300 rounded-md 
                  focus:ring-2 focus:ring-orange-500 focus:border-orange-500 
                  transition duration-200 ease-in-out"
          />
        </div>

        <!-- Navigation Links -->
        <div class="flex items-center justify-between text-sm">
          <router-link
            to="/dang-ky"
            class="text-orange-500 hover:text-orange-600 font-medium 
                  transition-colors duration-200"
          >
            Đăng ký
          </router-link>
          <router-link
            to="/quen-mk"
            class="text-orange-500 hover:text-orange-600 font-medium 
                  transition-colors duration-200"
          >
            Quên mật khẩu
          </router-link>
        </div>

        <!-- Submit Button -->
        <button
          type="submit"
          class="w-full py-3 px-4 bg-orange-500 text-white rounded-md
                hover:bg-orange-600 focus:ring-2 focus:ring-orange-500 
                focus:ring-offset-2 transition duration-200 ease-in-out
                font-medium"
        >
          Đăng nhập
        </button>

        <!-- Error Message -->
        <transition name="fade">
          <p
            v-if="error"
            class="text-sm text-red-500 text-center bg-red-50 p-2 rounded-md"
          >
            {{ error }}
          </p>
        </transition>
      </form>
    </div>
  </div>
</template>

<script setup>
import useLogin from "@/views/LoginView/Login.js";

const { tenDangNhap, matKhau, error, login } = useLogin();
</script>

<style scoped>
input:focus {
  outline: none;
}

/* Animation cho error message */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>