<template>
  <nav class="ml-4 text-gray-600" v-if="breadcrumbItems.length">
    <ol class="flex items-center space-x-2">
      <li v-for="(item, index) in breadcrumbItems" :key="index" class="flex items-center">
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
          class="mx-2 text-gray-400"
        >/</span>
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

const route = useRoute();

// Tính toán breadcrumb dựa trên meta của route
const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === "function") {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || [];
});

// Hàm tạo link cho breadcrumb dựa trên danh sách route
const getLinkForBreadcrumb = (index: number) => {
  const items = breadcrumbItems.value.slice(0, index + 1); // Lấy các phần tử từ đầu đến index hiện tại
  const lastItem = items[index];

  // Map các tên breadcrumb với đường dẫn tương ứng
  const breadcrumbMap: { [key: string]: string } = {
    "Thống kê": "/dashboard",
    "Quản Lý Phiếu Giảm Giá": "/phieu-giam-gia",
    "Phiếu giảm giá": "/phieu-giam-gia",
    "Đợt giảm giá": "/dot-giam-gia",
    "Thêm phiếu giảm giá": "/add-phieu-giam-gia",
    "Cập nhật phiếu giảm giá": `/update-phieu-giam-gia/:id`,
    "Thêm đợt giảm giá": "/ViewAddDotGiamGia",
    "Cập nhật đợt giảm giá": `/update-dot-giam-gia/${route.params.id || ":id"}`, // Thêm đường dẫn mới
    "Bảo hành": "/phieu-bao-hanh",
    "Quản lý Phiếu bảo hành": "/phieu-bao-hanh",
    "Quản lý Lịch sử phiếu bảo hành": "/lich-su-bao-hanh",
    "Quản lý hóa đơn": "/hoa-don",
    "Lịch sử hóa đơn": "/lich-su-hoa-don",
    "Hóa đơn chi tiết": "/hoa-don-chi-tiet",
    "Chi tiết hóa đơn": `/show-hoa-don/${route.params.id || ":id"}`,
    "Quản lý tài khoản": "/nhan-vien",
    "Nhân viên": "/nhan-vien",
    "Thêm nhân viên": "/them-nhan-vien",
    "Cập nhật nhân viên": "/update-nhan-vien",
    "Khách hàng": "/khach-hang",
    "Thêm khách hàng": "/them-khach-hang",
    "Cập nhật khách hàng": "/update-khach-hang",
    "Sản phẩm": "/san-pham-chi-tiet",
    "Chi tiết sản phẩm": "/san-pham-chi-tiet",
    "Quản lý Chi tiết sản phẩm": "/san-pham-chi-tiet",
    "Thêm chi tiết sản phẩm": "/product-detail/add",
    "Thêm Sản Phẩm": "/add-product",
    "Quản Lý Nhà Sản Xuất": "/manufacturer",
    "Thêm nhà sản xuất": "/manufacturer/add",
    "Cập nhật Nhà Sản Xuất": `/manufacturer/edit/${route.params.id || ":id"}`,
    "Quản lý Dòng sản phẩm": "/product-line",
    "Thêm dòng sản phẩm": "/product-line/add",
    "Cập nhật dòng sản phẩm": `/product-line/edit/${route.params.id || ":id"}`,
    "Quản lý Màn hình": "/screen",
    "Thêm màn hình": "/screen/add",
    "Cập nhật màn hình": `/screen/edit/${route.params.id || ":id"}`,
    "Quản lý Công nghệ màn hình": "/man-hinh/cong-nghe",
    "Thêm công nghệ màn hình": "/man-hinh/cong-nghe/add",
    "Cập nhật công nghệ màn hình": `/man-hinh/cong-nghe/edit/${route.params.id || ":id"}`,
    "Quản lý RAM": "/ram",
    "Quản lý Bộ nhớ trong": "/bo-nho-trong",
    "Quản lý Bộ nhớ ngoài": "/bo-nho-ngoai",
    "Quản lý CPU": "/cpu",
    "Quản lý GPU": "/gpu",
    "Quản lý Thông số camera trước": "/camera-truoc",
    "Quản lý Thông số camera sau": "/camera-sau",
    "Quản lý Chi tiết camera trước": "/chi-tiet-camera-truoc",
    "Quản lý Chi tiết camera sau": "/chi-tiet-camera-sau",
    "Quản lý Cụm camera": "/cum-camera",
    "Quản lý Công nghệ sạc": "/sac/cong-nghe",
    "Quản lý Hỗ trợ công nghệ sạc": "/sac/ho-tro-cong-nghe",
    "Quản lý Hỗ trợ sạc": "/sac/ho-tro",
    "Quản lý Cổng sạc": "/sac/cong-sac",
    "Quản lý Imel": "/imel",
    "Thêm Imel": "/imel/add",
    "Cập nhật Imel": `/imel/edit/${route.params.id || ":id"}`,
    "Quản lý Imel đã bán": "/imel-da-ban",
    "Không tìm thấy": "/:pathMatch(.*)*"
  };

  // Tạo đường dẫn dựa trên các phần tử trước đó
  for (let i = items.length - 1; i >= 0; i--) {
    const key = items[i];
    if (breadcrumbMap[key]) {
      return breadcrumbMap[key];
    }
  }

  return "#"; // Mặc định nếu không tìm thấy
};
</script>

<style scoped>
/* Tối ưu hóa CSS */
nav {
  display: flex;
  align-items: center;
}
</style>