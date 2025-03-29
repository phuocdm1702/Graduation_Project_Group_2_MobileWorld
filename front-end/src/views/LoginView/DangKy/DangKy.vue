<template>
  <div class="flex items-center justify-center h-screen px-6 bg-gradient-to-r from-gray-100 to-gray-300">
    <div class="w-full max-w-md p-8 bg-white rounded-xl shadow-lg">
      <div class="flex items-center justify-center mb-6">
        <img class="logo" src="../../../assets/Logo_Mobile_World_vector.png" alt="Logo">
      </div>

      <h2 class="text-2xl font-semibold text-center text-gray-800 mb-6">Đăng Ký Tài Khoản</h2>

      <form>
        <label class="block mb-4">
          <span class="text-sm font-medium text-gray-700">Email</span>
          <input
            type="email"
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
            placeholder="Nhập email của bạn"
          />
        </label>

        <label class="block mb-6">
          <span class="text-sm font-medium text-gray-700">Mật khẩu</span>
          <input
            type="password"
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
        </label>

        <label class="block mb-6">
          <span class="text-sm font-medium text-gray-700">Mật khẩu</span>
          <input
            type="password"
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
        </label>

        <div>
          <button
            type="submit"
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
</template>

<script setup lang="ts">
</script>

<style scoped>
.logo {
  max-width: 100%;
  width: 60%; 
  height: auto;
}
</style>