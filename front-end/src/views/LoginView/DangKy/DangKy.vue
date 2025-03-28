<template>
  <div class="flex items-center justify-center h-screen px-6 bg-gray-200">
    <div class="w-full max-w-sm p-6 bg-white rounded-md shadow-md">
      <div class="flex items-center justify-center">
        <span class="text-2xl font-semibold text-orange-500">Mobile World</span>
      </div>

      <form class="mt-4">
        <label class="block">
          <span class="text-sm text-gray-700">Email</span>
          <input
            type="email"
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
          />
        </label>

        <label class="block mt-3">
          <span class="text-sm text-gray-700">Password</span>
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
           
          />
        </label>

        <div class="flex items-center justify-between mt-4">
          <div>
            <router-link to="/dang-ky">
              <a
                class="block text-sm text-orange-500 fontme hover:underline"
              >Đăng ký?</a
              >
            </router-link>
          </div>

          <div>
            <a
              class="block text-sm text-orange-500 fontme hover:underline"
              href="#"
            >Forgot your password?</a
            >
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
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">

</script>
