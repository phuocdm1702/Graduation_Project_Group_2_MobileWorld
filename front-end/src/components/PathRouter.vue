<template>
  <nav class="ml-4 text-gray-600" v-if="breadcrumbItems.length">
    <ol class="flex items-center space-x-2">
      <li v-for="(item, index) in breadcrumbItems" :key="index">
        <router-link
          v-if="index < breadcrumbItems.length - 1"
          :to="getLinkForBreadcrumb(index)"
          class="hover:text-indigo-600 text-lg font-semibold text-gray-800 hover:underline transition-colors duration-200"
        >
          {{ item }}
        </router-link>
        <span v-else class="text-lg font-semibold text-gray-900">{{ item }}</span>
        <span
          v-if="index < breadcrumbItems.length - 1"
          class="before:content-['/'] before:mx-2 text-gray-400"
        ></span>
      </li>
    </ol>
  </nav>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { useRoute } from "vue-router";

defineProps<{
  breadcrumbItems: string[];
}>();

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