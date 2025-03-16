<template>
  <header
    class="flex items-center justify-between px-6 py-2 bg-white border-b-4 border-orange-500"
  >
    <!-- Phần bên trái: Menu toggle và Breadcrumb -->
    <div class="flex items-center">
      <button
        @click="isOpen = true"
        class="text-gray-500 focus:outline-none lg:hidden"
      >
        <svg
          class="w-6 h-6"
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            d="M4 6H20M4 12H20M4 18H11"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          />
        </svg>
      </button>

      <!-- Thanh breadcrumb động -->
      <nav class="ml-4 text-gray-600" v-if="breadcrumbItems.length">
        <ol class="flex items-center space-x-2">
          <li v-for="(item, index) in breadcrumbItems" :key="index">
            <router-link
              v-if="index < breadcrumbItems.length - 1"
              :to="getLinkForBreadcrumb(index)"
              class="hover:text-indigo-600 text-lg font-semibold text-gray-800 hover:underline transition-colors duration-200"
            >{{ item }}</router-link
            >
            <span v-else class="text-lg font-semibold text-gray-900">{{ item }}</span>
            <span v-if="index < breadcrumbItems.length - 1" class="before:content-['/'] before:mx-2 text-gray-400"></span>
          </li>
        </ol>
      </nav>
    </div>

    <!-- Phần bên phải: Thông báo và Avatar -->
    <div class="flex items-center">
      <div class="flex items-center">
        <div class="relative">
          <button
            @click="notificationOpen = !notificationOpen"
            class="flex mx-4 text-gray-600 focus:outline-none"
          >
            <svg
              class="h-6 w-6"
              viewBox="0 0 24 24"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="M15 17H20L18.5951 15.5951C18.2141 15.2141 18 14.6973 18 14.1585V11C18 8.38757 16.3304 6.16509 14 5.34142V5C14 3.89543 13.1046 3 12 3C10.8954 3 10 3.89543 10 5V5.34142C7.66962 6.16509 6 8.38757 6 11V14.1585C6 14.6973 5.78595 15.2141 5.40493 15.5951L4 17H9M15 17V18C15 19.6569 13.6569 21 12 21C10.3431 21 9 19.6569 9 18V17M15 17H9"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
            </svg>
          </button>

          <div
            v-show="notificationOpen"
            @click="notificationOpen = false"
            class="fixed inset-0 h-full w-full z-10"
          ></div>

          <div
            v-show="notificationOpen"
            class="absolute right-0 mt-2 w-80 bg-white rounded-lg shadow-xl overflow-hidden z-10"
            style="width: 20rem"
          >
            <a
              href="#"
              class="flex items-center px-4 py-3 text-gray-600 hover:text-white hover:bg-indigo-600 -mx-2"
            >
              <img
                class="h-8 w-8 rounded-full object-cover mx-1"
                src="https://images.unsplash.com/photo-1552774021-9ebbb764f03e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1000&q=80"
                alt="avatar"
              />
              <p class="text-sm mx-2">
                <span class="font-bold" href="#">Evan Josh</span> replied on the
                <span class="font-bold text-indigo-400" href="#"
                >Upload Image</span
                >
                artical . 2m
              </p>
            </a>
          </div>
        </div>
      </div>
      <div class="relative">
        <button
          @click="dropdownOpen = !dropdownOpen"
          class="relative z-10 block w-8 h-8 overflow-hidden rounded-full shadow focus:outline-none"
        >
          <img
            class="object-cover w-full h-full"
            src="https://images.unsplash.com/photo-1528892952291-009c663ce843?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=296&q=80"
            alt="Your avatar"
          />
        </button>

        <div
          v-show="dropdownOpen"
          @click="dropdownOpen = false"
          class="fixed inset-0 z-10 w-full h-full"
        ></div>

        <transition
          enter-active-class="transition duration-150 ease-out transform"
          enter-from-class="scale-95 opacity-0"
          enter-to-class="scale-100 opacity-100"
          leave-active-class="transition duration-150 ease-in transform"
          leave-from-class="scale-100 opacity-100"
          leave-to-class="scale-95 opacity-0"
        >
          <div
            v-show="dropdownOpen"
            class="absolute right-0 z-20 w-48 py-1 mt-2 bg-white rounded-lg shadow-xl"
          >
            <a
              href="#"
              class="px-4 py-2 flex rounded-md text-sm text-gray-700 hover:bg-indigo-600 hover:text-white"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="h-5 w-5 mr-1"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
                stroke-width="2"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"
                />
              </svg>
              Profile</a
            >
            <a
              href="#"
              class="flex rounded-md px-4 py-2 text-sm text-gray-700 hover:bg-indigo-600 hover:text-white"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="h-5 w-5 mr-1"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
                stroke-width="2"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"
                />
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
                />
              </svg>
              Settings</a
            >
            <router-link
              to="/"
              class="flex px-4 py-2 rounded-md text-sm text-gray-700 hover:bg-indigo-600 hover:text-white"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="h-5 w-5 mr-1"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
                stroke-width="2"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"
                />
              </svg>
              Log out</router-link
            >
          </div>
        </transition>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { ref, computed } from "vue";
import { useSidebar } from "../hooks/useSidebar";
import { useRoute } from "vue-router";

const dropdownOpen = ref(false);
const { isOpen } = useSidebar();
const notificationOpen = ref(false);

// Lấy route hiện tại
const route = useRoute();

// Tính toán breadcrumb dựa trên meta của route
const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === "function") {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || [];
});

// Hàm tạo link cho breadcrumb
const getLinkForBreadcrumb = (index: number) => {
  const items = breadcrumbItems.value;
  const item = items[index];

  switch (item) {
    case "Thống kê":
      return "/dashboard";
    case "Phiếu giảm giá":
      return "/phieu-giam-gia";
    case "Đợt giảm giá":
      return "/phieu-giam-gia/dot-giam-gia";
    case "Thêm phiếu giảm giá":
      return "/phieu-giam-gia/add-phieu-giam-gia";
    case "Sửa phiếu giảm giá":
      return "/phieu-giam-gia/update-phieu-giam-gia";
    case "Phiếu bảo hành":
      return "/phieu-bao-hanh";
    case "Lịch sử phiếu bảo hành":
      return "/lich-su-bao-hanh";
    case "Quản lý hóa đơn":
      return "/hoa-don";
    case "Lịch sử hóa đơn":
      return "/lich-su-hoa-don";
    case "Quản lý tài khoản":
      return "/nhan-vien";
    case "Nhân viên":
      return "/nhan-vien";
    case "Khách hàng":
      return "/khach-hang";
    case "Sản phẩm":
      return "/san-pham";
    case "Thương hiệu":
      return "/san-pham/manufacturer";
    case "Nhà sản xuất":
      return "/san-pham/manufacturer";
    case "Dòng sản phẩm":
      return "/san-pham/product-line";
    case "Màn hình":
      return "/san-pham/screen";
    case "Bộ nhớ":
      return "/san-pham/bo-nho-trong";
    case "CPU & GPU":
      return "/san-pham/cpu";
    case "Camera":
      return "/san-pham/camera-truoc";
    case "Sạc":
      return "/san-pham/sac/cong-nghe";
    case "Imel":
      return "/san-pham/imel";
    default:
      return "#";
  }
};
</script>

<style scoped>
.breadcrumb a,
.breadcrumb span {
  /* text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1); /* Hiệu ứng bóng chữ (tùy chọn) */
}
</style>