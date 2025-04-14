<template>
  <div class="container">
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />
    <div class="mt-2 mx-auto">
      <ToastNotification ref="toast" />

      <!-- Form chỉnh sửa sản phẩm -->
      <div class="bg-white shadow-lg rounded-lg p-6">
        <form @submit.prevent="submitForm">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <!-- Tên sản phẩm -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Tên sản phẩm</label>
              <input
                v-model="form.tenSanPham"
                type="text"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập tên sản phẩm"
                required
              />
            </div>

            <!-- Mã sản phẩm -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Mã sản phẩm</label>
              <input
                v-model="form.ma"
                type="text"
                class="input-field p-2 border border-gray-300"
                placeholder="Nhập mã sản phẩm"
                required
                disabled
              />
            </div>

            <!-- Hãng -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Hãng</label>
              <v-select
                v-model="form.idNhaSanXuat"
                :options="nhaSanXuatOptions"
                label="nhaSanXuat"
                :reduce="option => option.id"
                placeholder="Chọn hoặc tìm kiếm hãng"
                class="input-field"
                required
              />
            </div>

            <!-- Hệ điều hành -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Hệ điều hành</label>
              <v-select
                v-model="form.idHeDieuHanh"
                :options="heDieuHanhOptions"
                :get-option-label="option => `${option.heDieuHanh} ${option.phienBan}`"
                :reduce="option => option.id"
                placeholder="Chọn hoặc tìm kiếm hệ điều hành"
                class="input-field"
                required
              />
            </div>

            <!-- Công nghệ màn hình -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Công nghệ màn hình</label>
              <v-select
                v-model="form.congNgheManHinh"
                :options="congNgheManHinhOptions"
                :get-option-label="option => `${option.chuanManHinh} ${option.congNgheManHinh}`"
                :reduce="option => option.id"
                placeholder="Chọn hoặc tìm kiếm công nghệ màn hình"
                class="input-field"
                required
              />
            </div>

            <!-- Pin -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Pin</label>
              <v-select
                v-model="form.idPin"
                :options="pinOptions"
                :get-option-label="option => `${option.loaiPin} ${option.dungLuongPin}`"
                :reduce="option => option.id"
                placeholder="Tất cả"
                class="input-field"
                @update:model-value="searchProductDetails"
                clearable
              />
            </div>

            <!-- CPU -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">CPU</label>
              <v-select
                v-model="form.idCpu"
                :options="cpuOptions"
                label="tenCpu"
                :reduce="option => option.id"
                placeholder="Chọn hoặc tìm kiếm CPU"
                class="input-field"
                required
              />
            </div>

            <!-- GPU -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">GPU</label>
              <v-select
                v-model="form.idGpu"
                :options="gpuOptions"
                label="tenGpu"
                :reduce="option => option.id"
                placeholder="Chọn hoặc tìm kiếm GPU"
                class="input-field"
                required
              />
            </div>

            <!-- Cụm camera -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Cụm camera</label>
              <v-select
                v-model="form.idCumCamera"
                :options="cumCameraOptions"
                :get-option-label="option => `${option.cameraTruoc} ${option.cameraSau}`"
                :reduce="option => option.id"
                placeholder="Chọn hoặc tìm kiếm cụm camera"
                class="input-field"
                required
              />
            </div>

            <!-- Thiết kế -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Thiết kế</label>
              <v-select
                v-model="form.idThietKe"
                :options="thietKeOptions"
                :get-option-label="option => `${option.chatLieuKhung} ${option.chatLieuMatLung}`"
                :reduce="option => option.id"
                placeholder="Chọn hoặc tìm kiếm thiết kế"
                class="input-field"
                required
              />
            </div>

            <!-- SIM -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">SIM</label>
              <v-select
                v-model="form.idSim"
                :options="simOptions"
                :get-option-label="option => `${option.soLuongSimHoTro} ${option.cacLoaiSimHoTro}`"
                :reduce="option => option.id"
                placeholder="Chọn hoặc tìm kiếm SIM"
                class="input-field"
                required
              />
            </div>

            <!-- Hỗ trợ công nghệ sạc -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Hỗ trợ công nghệ sạc</label>
              <v-select
                v-model="form.hoTroCongNgheSac"
                :options="hoTroCongNgheSacOptions"
                :get-option-label="option => `${option.congSac} ${option.congNgheHoTro}`"
                :reduce="option => option.id"
                placeholder="Chọn hoặc tìm kiếm công nghệ sạc"
                class="input-field"
                required
              />
            </div>

            <!-- Công nghệ mạng -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Công nghệ mạng</label>
              <v-select
                v-model="form.idCongNgheMang"
                :options="congNgheMangOptions"
                label="tenCongNgheMang"
                :reduce="option => option.id"
                placeholder="Chọn hoặc tìm kiếm công nghệ mạng"
                class="input-field"
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
              class="px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition"
            >
              Lưu thay đổi
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
  name: 'EditSanPham',
  components: { BreadcrumbWrapper, ToastNotification, VSelect },
  setup() {
    const route = useRoute();
    const router = useRouter();
    const toast = ref(null);
    const productId = route.params.id;

    const form = ref({
      id: '',
      ma: '',
      tenSanPham: '',
      idNhaSanXuat: null,
      idHeDieuHanh: null,
      congNgheManHinh: null,
      idPin: null,
      idCpu: null,
      idGpu: null,
      idCumCamera: null,
      idThietKe: null,
      idSim: null,
      hoTroCongNgheSac: null,
      idCongNgheMang: null,
      deleted: false,
      createdAt: '',
      createdBy: '',
      updatedAt: '',
      updatedBy: '',
    });

    // Options cho tất cả các dropdown
    const nhaSanXuatOptions = ref([]);
    const heDieuHanhOptions = ref([]);
    const congNgheManHinhOptions = ref([]);
    const pinOptions = ref([]);
    const cpuOptions = ref([]);
    const gpuOptions = ref([]);
    const cumCameraOptions = ref([]);
    const thietKeOptions = ref([]);
    const simOptions = ref([]);
    const hoTroCongNgheSacOptions = ref([]);
    const congNgheMangOptions = ref([]);

    // Breadcrumb
    const breadcrumbItems = computed(() => route.meta?.breadcrumb || ['Sửa Sản Phẩm']);

    // Fetch product details
    const fetchProductDetails = async () => {
      try {
        const { data } = await axios.get(`http://localhost:8080/san-pham/${productId}`);
        console.log('API Response:', data);
        form.value = {
          id: data.id || '',
          ma: data.ma || '',
          tenSanPham: data.tenSanPham || '',
          idNhaSanXuat: data.idNhaSanXuat?.id || null,
          idHeDieuHanh: data.idHeDieuHanh?.id || null,
          congNgheManHinh: data.congNgheManHinh?.id || null,
          idPin: data.idPin?.id || null,
          idCpu: data.idCpu?.id || null,
          idGpu: data.idGpu?.id || null,
          idCumCamera: data.idCumCamera?.id || null,
          idThietKe: data.idThietKe?.id || null,
          idSim: data.idSim?.id || null,
          hoTroCongNgheSac: data.hoTroCongNgheSac?.id || null,
          idCongNgheMang: data.idCongNgheMang?.id || null,
          deleted: data.deleted || false,
          createdAt: data.createdAt || '',
          createdBy: data.createdBy || '',
          updatedAt: data.updatedAt || '',
          updatedBy: data.updatedBy || '',
        };
        console.log('Form value after fetch:', form.value);
      } catch (error) {
        toast.value?.kshowToast('error', 'Không thể tải thông tin sản phẩm!');
        console.error('Fetch error:', error);
      }
    };

    // Fetch options cho tất cả các dropdown
    const fetchOptions = async () => {
      try {
        const [
          nhaSanXuatRes,
          heDieuHanhRes,
          congNgheManHinhRes,
          pinRes,
          cpuRes,
          gpuRes,
          cumCameraRes,
          thietKeRes,
          simRes,
          hoTroCongNgheSacRes,
          congNgheMangRes,
        ] = await Promise.all([
          axios.get('http://localhost:8080/nha-san-xuat'),
          axios.get('http://localhost:8080/he-dieu-hanh'),
          axios.get('http://localhost:8080/cong-nghe-man-hinh'),
          axios.get('http://localhost:8080/pin'),
          axios.get('http://localhost:8080/cpu'),
          axios.get('http://localhost:8080/gpu'),
          axios.get('http://localhost:8080/cum-camera'),
          axios.get('http://localhost:8080/thiet-ke'),
          axios.get('http://localhost:8080/sim'),
          axios.get('http://localhost:8080/ho-tro-cong-nghe-sac'),
          axios.get('http://localhost:8080/cong-nghe-mang'),
        ]);

        nhaSanXuatOptions.value = nhaSanXuatRes.data.content || nhaSanXuatRes.data || [];
        heDieuHanhOptions.value = heDieuHanhRes.data.content || heDieuHanhRes.data || [];
        congNgheManHinhOptions.value = congNgheManHinhRes.data.content || congNgheManHinhRes.data || [];
        pinOptions.value = pinRes.data.content || pinRes.data || [];
        cpuOptions.value = cpuRes.data.content || cpuRes.data || [];
        gpuOptions.value = gpuRes.data.content || gpuRes.data || [];
        cumCameraOptions.value = cumCameraRes.data.content || cumCameraRes.data || [];
        thietKeOptions.value = thietKeRes.data.content || thietKeRes.data || [];
        simOptions.value = simRes.data.content || simRes.data || [];
        hoTroCongNgheSacOptions.value = hoTroCongNgheSacRes.data.content || hoTroCongNgheSacRes.data || [];
        congNgheMangOptions.value = congNgheMangRes.data.content || congNgheMangRes.data || [];

        console.log('nhaSanXuatOptions:', nhaSanXuatOptions.value);
        console.log('heDieuHanhOptions:', heDieuHanhOptions.value);
        console.log('congNgheManHinhOptions:', congNgheManHinhOptions.value);
        console.log('pinOptions:', pinOptions.value);
        console.log('cpuOptions:', cpuOptions.value);
        console.log('gpuOptions:', gpuOptions.value);
        console.log('cumCameraOptions:', cumCameraOptions.value);
        console.log('thietKeOptions:', thietKeOptions.value);
        console.log('simOptions:', simOptions.value);
        console.log('hoTroCongNgheSacOptions:', hoTroCongNgheSacOptions.value);
        console.log('congNgheMangOptions:', congNgheMangOptions.value);
      } catch (error) {
        toast.value?.kshowToast('error', 'Lỗi khi tải danh sách tùy chọn!');
        console.error('Fetch options error:', error);
      }
    };

    // Submit form
    const submitForm = async () => {
      try {
        const payload = {
          id: form.value.id,
          ma: form.value.ma,
          tenSanPham: form.value.tenSanPham,
          idNhaSanXuat: form.value.idNhaSanXuat ? { id: parseInt(form.value.idNhaSanXuat) } : null,
          idHeDieuHanh: form.value.idHeDieuHanh ? { id: parseInt(form.value.idHeDieuHanh) } : null,
          congNgheManHinh: form.value.congNgheManHinh ? { id: parseInt(form.value.congNgheManHinh) } : null,
          idPin: form.value.idPin ? { id: parseInt(form.value.idPin) } : null,
          idCpu: form.value.idCpu ? { id: parseInt(form.value.idCpu) } : null,
          idGpu: form.value.idGpu ? { id: parseInt(form.value.idGpu) } : null,
          idCumCamera: form.value.idCumCamera ? { id: parseInt(form.value.idCumCamera) } : null,
          idThietKe: form.value.idThietKe ? { id: parseInt(form.value.idThietKe) } : null,
          idSim: form.value.idSim ? { id: parseInt(form.value.idSim) } : null,
          hoTroCongNgheSac: form.value.hoTroCongNgheSac ? { id: parseInt(form.value.hoTroCongNgheSac) } : null,
          idCongNgheMang: form.value.idCongNgheMang ? { id: parseInt(form.value.idCongNgheMang) } : null,
          deleted: form.value.deleted,
          createdAt: form.value.createdAt,
          createdBy: form.value.createdBy,
          updatedAt: form.value.updatedAt,
          updatedBy: form.value.updatedBy,
        };

        console.log('Gửi payload:', payload);
        await axios.put(`http://localhost:8080/san-pham/${productId}`, payload);

        toast.value?.kshowToast('success', 'Cập nhật sản phẩm thành công!');
        setTimeout(() => {
          router.push('/products');
        }, 1000);
      } catch (error) {
        toast.value?.kshowToast('error', 'Lỗi khi cập nhật sản phẩm! Vui lòng kiểm tra lại.');
        console.error('Update error:', error);
      }
    };

    // Go back
    const goBack = () => {
      router.push('/products');
    };

    // Lifecycle
    onMounted(async () => {
      await fetchOptions();
      await fetchProductDetails();
    });

    return {
      toast,
      form,
      nhaSanXuatOptions,
      heDieuHanhOptions,
      congNgheManHinhOptions,
      pinOptions,
      cpuOptions,
      gpuOptions,
      cumCameraOptions,
      thietKeOptions,
      simOptions,
      hoTroCongNgheSacOptions,
      congNgheMangOptions,
      breadcrumbItems,
      submitForm,
      goBack,
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