<template>
  <div>
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems"/>

<!--    <div v-if="product" class="bg-white shadow-lg rounded-lg p-6">-->
<!--      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">-->
<!--        &lt;!&ndash; Product Info &ndash;&gt;-->
<!--        <div>-->
<!--          <h2 class="text-xl font-semibold">{{ product.tenSanPham }}</h2>-->
<!--          <p class="text-gray-600 mt-2">-->
<!--            <strong>Hãng:</strong> {{ product.idNhaSanXuat?.nhaSanXuat || 'Không xác định' }}-->
<!--          </p>-->
<!--          <p class="text-gray-600">-->
<!--            <strong>Hệ Điều Hành:</strong> {{ product.idHeDieuHanh?.heDieuHanh || 'Không xác định' }} {{ product.idHeDieuHanh?.phienBan || '' }}-->
<!--          </p>-->
<!--          <p class="text-gray-600">-->
<!--            <strong>Màn Hình:</strong> {{ product.idManHinh?.kichThuoc || 'Không xác định' }} {{ product.idManHinh?.doPhanGiai || '' }}-->
<!--          </p>-->
<!--          <p class="text-gray-600">-->
<!--            <strong>Trạng Thái:</strong>-->
<!--            <span :class="product.deleted ? 'text-red-600' : 'text-green-600'">{{ product.deleted ? 'Hết hàng' : 'Còn hàng' }}</span>-->
<!--          </p>-->
<!--        </div>-->

<!--        &lt;!&ndash; Actions &ndash;&gt;-->
<!--        <div class="flex justify-end items-start">-->
<!--          <button @click="goBack" class="px-4 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition">-->
<!--            Quay Lại-->
<!--          </button>-->
<!--        </div>-->
<!--      </div>-->
<!--    </div>-->

<!--    <div v-else class="text-center text-gray-500 py-4">-->
<!--      Đang tải dữ liệu hoặc không tìm thấy sản phẩm...-->
<!--    </div>-->
  </div>
</template>

<script>
import {ref, onMounted, computed} from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue';

export default {
  name: 'SanPhamChiTiet',
  components: { BreadcrumbWrapper },
  setup() {
    const route = useRoute();
    const router = useRouter();
    const product = ref(null);

    const breadcrumbItems = computed(() => {
      if (typeof route.meta.breadcrumb === 'function') {
        return route.meta.breadcrumb(route);
      }
      return route.meta?.breadcrumb || ['Chi Tiết Sản Phẩm'];
    });

    // const fetchProductDetails = async () => {
    //   const productId = route.params.id;
    //   try {
    //     const { data } = await axios.get(`http://localhost:8080/san-pham/${productId}`);
    //     product.value = data;
    //   } catch (error) {
    //     console.error('Error fetching product details:', error);
    //   }
    // };

    const goBack = () => {
      router.push('/products');
    };

    // onMounted(() => {
    //   fetchProductDetails();
    // });

    return {
      product,
      breadcrumbItems,
      goBack,
    };
  },
};
</script>

<style scoped>
.container {
  max-width: 800px;
}
</style>