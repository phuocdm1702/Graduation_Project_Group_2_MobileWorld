<template>
  <div>
    <!-- Thêm BreadcrumbWrapper -->
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />

    <div class="mt-2 mx-auto">
      <ToastNotification ref="toast" />

      <!-- Form chỉnh sửa -->
      <div class="bg-white shadow-lg rounded-lg p-5 mb-4 mt-4">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">ID Công nghệ màn hình</label>
            <select
              v-model="manHinh.idCongNgheManHinh"
              class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
              required
            >
              <option v-for="tech in congNgheManHinhs" :key="tech.id" :value="tech.id">{{ tech.congNgheManHinh }}</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Mã màn hình</label>
            <input
              v-model.trim="manHinh.ma"
              type="text"
              placeholder="Nhập mã màn hình"
              class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
              required
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Kích thước</label>
            <input
              v-model.trim="manHinh.kichThuoc"
              type="text"
              placeholder="Nhập kích thước (inch)"
              class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
              required
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Độ phân giải</label>
            <input
              v-model.trim="manHinh.doPhanGiai"
              type="text"
              placeholder="Nhập độ phân giải"
              class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
              required
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Độ sáng tối đa</label>
            <input
              v-model.trim="manHinh.doSangToiDa"
              type="text"
              placeholder="Nhập độ sáng tối đa (nits)"
              class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
              required
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Tần số quét</label>
            <input
              v-model.trim="manHinh.tanSoQuet"
              type="text"
              placeholder="Nhập tần số quét (Hz)"
              class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
              required
            />
          </div>
          <div class="md:col-span-2">
            <label class="block text-sm font-medium text-gray-700 mb-1">Kiểu màn hình</label>
            <input
              v-model.trim="manHinh.kieuManHinh"
              type="text"
              placeholder="Nhập kiểu màn hình"
              class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
              required
            />
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
            Lưu
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
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import ToastNotification from '@/components/ToastNotification.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue'; // Import BreadcrumbWrapper
import useManHinh from './ManHinh.js';
import axios from 'axios';

const router = useRouter();
const route = useRoute();
const toast = ref(null);
const originalMa = ref('');
const originalKieuManHinh = ref('');

const {
  manHinh,
  congNgheManHinhs,
  checkDuplicate,
  updateManHinh,
  showConfirmModal,
  confirmMessage,
  confirmAction,
  executeConfirmedAction,
  closeConfirmModal,
} = useManHinh(toast);

// Tính toán breadcrumb dựa trên meta của route
const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === "function") {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || ["Quản Lý Màn Hình", "Chỉnh Sửa Màn Hình"]; // Mặc định nếu không có breadcrumb
});

const fetchManHinh = async (id) => {
  try {
    const { data } = await axios.get(`http://localhost:8080/api/man-hinh/${id}`);
    manHinh.value = data;
    originalMa.value = data.ma;
    originalKieuManHinh.value = data.kieuManHinh;
  } catch (error) {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Không thể tải dữ liệu màn hình!');
    }
    console.error('Fetch error:', error);
  }
};

const fetchCongNgheManHinhs = async () => {
  try {
    const { data } = await axios.get('http://localhost:8080/api/cong-nghe-man-hinh', {
      params: { page: 0, size: 100 },
    });
    congNgheManHinhs.value = data.content;
  } catch (error) {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Không thể tải danh sách công nghệ màn hình!');
    }
    console.error('Fetch cong nghe man hinh error:', error);
  }
};

onMounted(() => {
  const id = route.params.id;
  if (id) {
    fetchManHinh(id);
    fetchCongNgheManHinhs();
  }
});

const handleSubmit = async () => {
  const { id, ma, kieuManHinh, idCongNgheManHinh } = manHinh.value;
  if (!id || !ma || !kieuManHinh || !idCongNgheManHinh) {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Dữ liệu không hợp lệ! Vui lòng kiểm tra lại.');
    }
    return;
  }

  if (ma !== originalMa.value && (await checkDuplicate('ma', ma, id))) {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Mã màn hình đã tồn tại!');
    }
    return;
  }

  if (kieuManHinh !== originalKieuManHinh.value && (await checkDuplicate('kieuManHinh', kieuManHinh, id))) {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Kiểu màn hình đã tồn tại!');
    }
    return;
  }

  confirmAction('Bạn có chắc chắn muốn cập nhật màn hình này?', async () => {
    await updateManHinh();
    router.push('/screens');
  });
};

const goBack = () => {
  router.push('/screens');
};
</script>

<style scoped>
.input-field {
  @apply w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-sm;
}
</style>