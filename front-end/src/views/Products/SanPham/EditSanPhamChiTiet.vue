<template>
  <div class="container">
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />
    <div class="mt-2 mx-auto">
      <ToastNotification ref="toast" />

      <!-- Form chỉnh sửa sản phẩm chi tiết -->
      <div class="bg-white shadow-lg rounded-lg p-6">
        <form @submit.prevent="submitForm">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <!-- Mã sản phẩm -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Mã sản phẩm</label>
              <input
                v-model="form.ma"
                type="text"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập mã sản phẩm"
                disabled
              />
            </div>

            <!-- Màu sắc -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Màu sắc</label>
              <v-select
                v-model="form.idMauSac"
                :options="mauSacOptions"
                label="tenMau"
                :reduce="option => option.id"
                placeholder="Chọn màu sắc"
                class="input-field"
                required
              />
            </div>

            <!-- RAM -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">RAM</label>
              <v-select
                v-model="form.idRam"
                :options="ramOptions"
                :get-option-label="option => `${option.dungLuong}`"
                :reduce="option => option.id"
                placeholder="Chọn RAM"
                class="input-field"
                required
              />
            </div>

            <!-- ROM -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">ROM</label>
              <v-select
                v-model="form.idBoNhoTrong"
                :options="boNhoTrongOptions"
                :get-option-label="option => `${option.dungLuong}`"
                :reduce="option => option.id"
                placeholder="Chọn ROM"
                class="input-field"
                required
              />
            </div>

            <!-- Giá bán -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Giá bán</label>
              <input
                v-model="form.giaBan"
                type="number"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập giá bán"
                required
              />
            </div>
          </div>

          <!-- Nút hành động -->
          <div class="flex justify-end gap-4 mt-6">
            <button
              type="button"
              @click="goBack"
              class="px-4 py-2 bg-gray-500 text-white font-semibold rounded-lg shadow-md hover:bg-gray-600 transition"
            >
              Quay lại
            </button>
            <button
              type="submit"
              :disabled="isLoading"
              class="px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition"
            >
              {{ isLoading ? 'Đang lưu...' : 'Lưu thay đổi' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue';
import ToastNotification from '@/components/ToastNotification.vue';
import VSelect from 'vue-select';
import 'vue-select/dist/vue-select.css';

export default {
  name: 'EditSanPhamChiTiet',
  components: { BreadcrumbWrapper, ToastNotification, VSelect },
  setup() {
    const route = useRoute();
    const router = useRouter();
    const toast = ref(null);
    const productId = route.params.id; // ID của sản phẩm
    const detailId = route.params.detailId; // ID của chi tiết sản phẩm
    const isLoading = ref(false);

    const form = ref({
      id: '',
      ma: '',
      idMauSac: null,
      idRam: null,
      idBoNhoTrong: null,
      giaBan: null,
    });

    const mauSacOptions = ref([]);
    const ramOptions = ref([]);
    const boNhoTrongOptions = ref([]);

    const breadcrumbItems = computed(() => route.meta?.breadcrumb || ['Chỉnh Sửa Sản Phẩm Chi Tiết']);

    // Fetch chi tiết sản phẩm
    const fetchProductDetail = async () => {
      try {
        const { data } = await axios.get(`http://localhost:8080/chi-tiet-san-pham/${productId}/details`, {
          params: { id: detailId }
        });
        console.log('API Response:', data);
        if (data.content && data.content.length > 0) {
          const detail = data.content.find(item => item.id === parseInt(detailId));
          if (detail) {
            form.value = {
              id: detail.id,
              ma: detail.ma,
              idMauSac: detail.variants[0]?.idMauSac,
              idRam: detail.variants[0]?.idRam,
              idBoNhoTrong: detail.variants[0]?.idBoNhoTrong,
              giaBan: detail.giaBan,
            };
            console.log('Form after fetch:', form.value);
          } else {
            toast.value?.kshowToast('error', 'Không tìm thấy chi tiết sản phẩm với ID: ' + detailId);
          }
        } else {
          toast.value?.kshowToast('error', 'Không tìm thấy chi tiết sản phẩm!');
        }
      } catch (error) {
        toast.value?.kshowToast('error', 'Lỗi khi tải chi tiết sản phẩm!');
        console.error('Fetch error:', error);
      }
    };

    // Fetch options
    const fetchOptions = async () => {
      try {
        const [mauSacRes, boNhoTrongRes, ramRes] = await Promise.all([
          axios.get('http://localhost:8080/mau-sac/all'),
          axios.get('http://localhost:8080/bo-nho-trong/all'),
          axios.get('http://localhost:8080/ram/all'),
        ]);
        mauSacOptions.value = mauSacRes.data.content || mauSacRes.data || [];
        boNhoTrongOptions.value = boNhoTrongRes.data.content || boNhoTrongRes.data || [];
        ramOptions.value = ramRes.data.content || ramRes.data || [];
      } catch (error) {
        toast.value?.kshowToast('error', 'Lỗi khi tải danh sách tùy chọn!');
        console.error('Lỗi khi tải tùy chọn:', error);
      }
    };

    // Submit form
    const submitForm = async () => {
      if (isLoading.value) return;
      isLoading.value = true;
      try {
        if (!form.value.idMauSac || !form.value.idRam || !form.value.idBoNhoTrong) {
          toast.value?.kshowToast('error', 'Vui lòng chọn đầy đủ màu sắc, RAM và ROM!');
          return;
        }
        if (!form.value.giaBan || isNaN(form.value.giaBan) || form.value.giaBan <= 0) {
          toast.value?.kshowToast('error', 'Giá bán phải là một số hợp lệ và lớn hơn 0!');
          return;
        }

        const payload = {
          id: form.value.id,
          idMauSac: parseInt(form.value.idMauSac),
          idRam: parseInt(form.value.idRam),
          idBoNhoTrong: parseInt(form.value.idBoNhoTrong),
          giaBan: parseFloat(form.value.giaBan),
        };
        console.log('Payload:', payload);

        const response = await axios.put(`http://localhost:8080/chi-tiet-san-pham/${detailId}`, payload);
        toast.value?.kshowToast('success', response.data.message || 'Cập nhật sản phẩm chi tiết thành công!');
        setTimeout(() => {
          router.push(`/products/${productId}`);
        }, 1000);
      } catch (error) {
        const errorMessage = error.response?.data?.message || 'Lỗi khi cập nhật sản phẩm chi tiết!';
        toast.value?.kshowToast('error', errorMessage);
        console.error('Update error:', error.response?.data || error);
      } finally {
        isLoading.value = false;
      }
    };

    // Go back
    const goBack = () => {
      router.push(`/products/${productId}`);
    };

    // Lifecycle
    onMounted(async () => {
      await fetchOptions();
      await fetchProductDetail();
    });

    return {
      toast,
      form,
      mauSacOptions,
      ramOptions,
      boNhoTrongOptions,
      breadcrumbItems,
      submitForm,
      goBack,
      isLoading,
    };
  },
};
</script>

<style scoped>
.input-field {
  @apply w-full h-12 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-sm;
}

.input-field :deep(.v-select) {
  @apply w-full border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-sm;
}

.input-field :deep(.vs__dropdown-toggle) {
  @apply border border-gray-300 rounded-lg;
}

.input-field :deep(.vs__search) {
  @apply p-2;
}
</style>