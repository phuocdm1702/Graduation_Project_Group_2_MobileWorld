<template>
  <div class="mt-2 mx-auto max-w-4xl">
    <h2 class="bg-white shadow-lg rounded-lg p-5 mb-2 mt-2 text-2xl font-semibold text-gray-700">
      Thêm Mới Chi Tiết Sản Phẩm
    </h2>
    <ToastNotification ref="toast" />

    <!-- Form thêm mới -->
    <div class="bg-white shadow-lg rounded-lg p-5 mb-4 mt-4">
      <div class="grid grid-cols-1 gap-6">
        <div v-for="field in productFields" :key="field.id">
          <label class="block text-sm font-medium text-gray-700 mb-1">{{ field.label }}</label>
          <template v-if="field.type === 'select'">
            <select
              v-model="productDetail[field.id]"
              class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">Chọn {{ field.label }}</option>
              <option v-for="option in field.options" :key="option" :value="option">{{ option }}</option>
            </select>
          </template>
          <template v-else>
            <input
              v-model="productDetail[field.id]"
              type="text"
              :placeholder="'Nhập ' + field.label"
              class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </template>
        </div>
      </div>

      <!-- Nút chức năng -->
      <div class="flex justify-end gap-2 mt-6">
        <button
          @click="goBack"
          class="px-4 py-2 bg-gray-500 text-white font-semibold rounded-lg shadow-md hover:bg-gray-600 transition"
        >
          Hủy
        </button>
        <button
          @click="handleSubmit"
          class="px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition"
        >
          Thêm
        </button>
      </div>
    </div>

    <!-- Modal xác nhận -->
    <ConfirmModal
      :show="showConfirmModal"
      :message="confirmMessage"
      @confirm="executeConfirmedAction"
      @cancel="closeConfirmModal"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import ToastNotification from '@/components/ToastNotification.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';
import axios from 'axios';

const router = useRouter();
const toast = ref(null);
const showConfirmModal = ref(false);
const confirmMessage = ref('');
const confirmedAction = ref(null);

const productDetail = ref({
  id: '',
  id_imel: '',
  id_anh_sp: '',
  id_nha_sx: '',
  id_dong_sp: '',
  id_mau_sac: '',
  id_pin: '',
  id_man_hinh: '',
  id_ram: '',
  id_bo_nho: '',
  id_ho_tro_bn: '',
  id_cpu: '',
  id_gpu: '',
  id_camera: '',
  id_he_dieu_hanh: '',
  id_khang_bui: '',
  id_thiet_ke: '',
  id_sim: '',
  id_sac: '',
  id_ho_tro_sac: '',
  id_cong_nghe_mang: '',
  id_loai_tinh_trang: '',
  tien_ich_dac_biet: '',
  gia_ban: '',
});

const productFields = ref([
  { id: "id", label: "ID", type: "text" },
  { id: "id_imel", label: "ID Imel", type: "text" },
  { id: "id_anh_sp", label: "ID Ảnh Sản Phẩm", type: "select", options: ["Ảnh 1", "Ảnh 2", "Ảnh 3"] },
  { id: "id_nha_sx", label: "ID Nhà Sản Xuất", type: "select", options: ["Apple", "Samsung", "Xiaomi"] },
  { id: "id_dong_sp", label: "ID Dòng Sản Phẩm", type: "select", options: ["iPhone 15", "Galaxy S24", "Xiaomi 14"] },
  { id: "id_mau_sac", label: "ID Màu Sắc", type: "select", options: ["Đen", "Trắng", "Xanh"] },
  { id: "id_pin", label: "ID Pin", type: "select", options: ["4500mAh", "5000mAh", "6000mAh"] },
  { id: "id_man_hinh", label: "ID Màn Hình", type: "select", options: ["AMOLED", "OLED", "LCD"] },
  { id: "id_ram", label: "ID RAM", type: "select", options: ["8GB", "12GB", "16GB"] },
  { id: "id_bo_nho", label: "ID Bộ Nhớ Trong", type: "select", options: ["128GB", "256GB", "512GB"] },
  { id: "id_ho_tro_bn", label: "ID Hỗ Trợ Bộ Nhớ Ngoài", type: "select", options: ["Có", "Không"] },
  { id: "id_cpu", label: "ID CPU", type: "text" },
  { id: "id_gpu", label: "ID GPU", type: "text" },
  { id: "id_camera", label: "ID Cụm Camera", type: "select", options: ["Camera kép", "Camera đơn", "Camera 3 ống kính"] },
  { id: "id_he_dieu_hanh", label: "ID Hệ Điều Hành", type: "select", options: ["iOS", "Android"] },
  { id: "id_khang_bui", label: "ID Chỉ Số Kháng Bụi Kháng Nước", type: "select", options: ["IP68", "IP67", "IPX5"] },
  { id: "id_thiet_ke", label: "ID Thiết Kế", type: "select", options: ["Nhôm", "Kính", "Nhựa"] },
  { id: "id_sim", label: "ID Sim", type: "select", options: ["1 Sim", "2 Sim"] },
  { id: "id_sac", label: "ID Công Nghệ Sạc", type: "select", options: ["Fast Charge", "Wireless Charging"] },
  { id: "id_ho_tro_sac", label: "ID Hỗ Trợ Công Nghệ Sạc", type: "select", options: ["Có", "Không"] },
  { id: "id_cong_nghe_mang", label: "ID Công Nghệ Mạng", type: "select", options: ["5G", "4G"] },
  { id: "id_loai_tinh_trang", label: "ID Loại Tình Trạng", type: "select", options: ["Mới", "Đã sử dụng"] },
  { id: "tien_ich_dac_biet", label: "Tiện Ích Đặc Biệt", type: "text" },
  { id: "gia_ban", label: "Giá Bán", type: "text" },
]);

const handleSubmit = async () => {
  // Kiểm tra xem tất cả các trường có được điền đầy đủ hay không
  const requiredFields = productFields.value.filter(field => field.type === 'text');
  const hasEmptyRequiredField = requiredFields.some(field => !productDetail.value[field.id]);
  const hasEmptySelectField = productFields.value
    .filter(field => field.type === 'select')
    .some(field => !productDetail.value[field.id]);

  if (hasEmptyRequiredField || hasEmptySelectField) {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Vui lòng nhập đầy đủ thông tin!');
    }
    return;
  }

  confirmAction('Bạn có chắc chắn muốn thêm chi tiết sản phẩm này?', async () => {
    try {
      await axios.post('http://localhost:8080/api/chi-tiet-san-pham', productDetail.value);
      if (toast.value) {
        toast.value?.kshowToast('success', 'Thêm mới thành công!');
      }
      await router.push('/product-detail');
    } catch (error) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Lỗi khi lưu dữ liệu: ' + (error.response?.data?.error || error.message));
      }
      console.error('Save error:', error);
    }
  });
};

const goBack = () => {
  router.push('/product-detail');
};

const confirmAction = (message, action) => {
  confirmMessage.value = message;
  confirmedAction.value = action;
  showConfirmModal.value = true;
};

const executeConfirmedAction = () => {
  if (confirmedAction.value) {
    confirmedAction.value();
  }
  closeConfirmModal();
};

const closeConfirmModal = () => {
  showConfirmModal.value = false;
  confirmedAction.value = null;
};
</script>

<style scoped>
.input-field {
  @apply w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-sm;
}
</style>