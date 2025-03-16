<template>
  <div class="mt-2 mx-auto">
    <h2 class="bg-white shadow-lg rounded-lg p-5 mb-2 mt-2 text-2xl font-semibold text-gray-700">
      Thêm Mới Màn Hình
    </h2>
    <ToastNotification ref="toast" />

    <!-- Form thêm mới -->
    <div class="bg-white shadow-lg rounded-lg p-5 mb-4 mt-4">
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">ID Công nghệ màn hình</label>
          <select
            v-model="manHinh.idCongNgheManHinh"
            class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
            required
          >
            <option value="">Chọn công nghệ</option>
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
          <select
            v-model="manHinh.kichThuoc"
            class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
            required
          >
            <option value="">Chọn kích thước</option>
            <option v-for="kichThuoc in uniqueKichThuocList" :key="kichThuoc" :value="kichThuoc">{{ kichThuoc }}</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Độ phân giải</label>
          <select
            v-model="manHinh.doPhanGiai"
            class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
            required
          >
            <option value="">Chọn độ phân giải</option>
            <option v-for="doPhanGiai in uniqueDoPhanGiaiList" :key="doPhanGiai" :value="doPhanGiai">{{ doPhanGiai }}</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Độ sáng tối đa</label>
          <select
            v-model="manHinh.doSangToiDa"
            class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
            required
          >
            <option value="">Chọn độ sáng</option>
            <option v-for="doSang in uniqueDoSangToiDaList" :key="doSang" :value="doSang">{{ doSang }}</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Tần số quét</label>
          <select
            v-model="manHinh.tanSoQuet"
            class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
            required
          >
            <option value="">Chọn tần số quét</option>
            <option v-for="tanSo in uniqueTanSoQuetList" :key="tanSo" :value="tanSo">{{ tanSo }}</option>
          </select>
        </div>
        <div class="md:col-span-2">
          <label class="block text-sm font-medium text-gray-700 mb-1">Kiểu màn hình</label>
          <select
            v-model="manHinh.kieuManHinh"
            class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-[var(--ring-color)] focus:border-transparent transition-all duration-200"
            required
          >
            <option value="">Chọn kiểu màn hình</option>
            <option v-for="kieu in uniqueKieuManHinhList" :key="kieu" :value="kieu">{{ kieu }}</option>
          </select>
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
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import ToastNotification from '@/components/ToastNotification.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';
import useManHinh from './useManHinh.js';
import axios from 'axios';

const router = useRouter();
const toast = ref(null);

const {
  manHinh,
  congNgheManHinhs,
  checkDuplicate,
  saveManHinh,
  showConfirmModal,
  confirmMessage,
  confirmAction,
  executeConfirmedAction,
  closeConfirmModal,
} = useManHinh(toast);

// Khởi tạo giá trị mặc định cho manHinh
manHinh.value = { id: null, idCongNgheManHinh: '', ma: '', kichThuoc: '', doPhanGiai: '', doSangToiDa: '', tanSoQuet: '', kieuManHinh: '', deleted: false };

// Dữ liệu cứng cho các combobox
const kichThuocData = [
  "6.1 inches", "6.1 inches", "6.1 inches", "6.9 inches", "6.2 inches", "6.7 inches", "6.8 inches",
  "6.1 inches", "6.8 inches", "6.7 inches", "6.7 inches", "6.6 inches", "7.6 inches", "6.3 inches",
  "6.74 inches", "6.67 inches", "6.67 inches", "6.67 inches", "6.67 inches", "6.43 inches", "6.56 inches",
  "6.7 inches", "6.7 inches", "6.7 inches", "6.67 inches", "6.59 inches", "7.82 inches", "6.31 inches",
  "6.31 inches", "6.1 inches", "6.3 inches", "7.6 inches", "6.79 inches", "6.67 inches"
];
const doPhanGiaiData = [
  "1170x2532px", "1179x2556px", "1290x2796px", "1320x2868px", "1080x2340px", "1080x2340px",
  "1440x3120px", "1080x2340px", "1440x3088px", "1080x2640px", "720x1600px", "1080x2340px",
  "1856x2160px", "968x2376px", "720x1600px", "1080x2400px", "1220x2712px", "1220x2712px",
  "1220x2712px", "1080x2400px", "720x1612px", "1080x2412px", "1080x2040px", "1080x2412px",
  "1080x2040px", "1256x2760px", "2268x2440px", "1116x2484px", "1284x2778px", "1179x2556px",
  "1206x2622px", "1812x2176px", "1080x2460px", "1080x2400px"
];
const doSangToiDaData = [
  "1200 nits", "2000 nits", "2000 nits", "2000 nits", "2600 nits", "1900 nits", "2600 nits",
  "1750 nits", "1750 nits", "1750 nits", "1750 nits", "1000 nits", "2600 nits", "2600 nits",
  "600 nits", "1000 nits", "1200 nits", "4000 nits", "3000 nits", "800 nits", "480 nits",
  "500 nits", "1200 nits", "1200 nits", "1200 nits", "4500 nits", "2800 nits", "2800 nits",
  "1200 nits", "2000 nits", "2000 nits", "1200 nits", "550 nits", "1300 nits"
];
const tanSoQuetData = [
  "60Hz", "120Hz", "120Hz", "120Hz", "120Hz", "120Hz", "120Hz", "120Hz", "120Hz", "120Hz",
  "60Hz", "120Hz", "120Hz", "120Hz", "90Hz", "120Hz", "120Hz", "144Hz", "120Hz", "90Hz",
  "90Hz", "120Hz", "120Hz", "120Hz", "120Hz", "120Hz", "120Hz", "120Hz", "60Hz", "60Hz",
  "120Hz", "120Hz", "90Hz", "120Hz"
];
const kieuManHinhData = [
  "tai thỏ", "dynamic island", "dynamic island", "dynamic island", "đục lỗ", "đục lỗ", "đục lỗ", "đục lỗ",
  "đục lỗ", "đục lỗ", "đục lỗ", "đục lỗ", "đục lỗ", "đục lỗ", "đục lỗ", "đục lỗ", "đục lỗ", "đục lỗ",
  "đục lỗ", "đục lỗ", "đục lỗ", "đục lỗ", "đục lỗ", "đục lỗ", "đục lỗ", "đục lỗ", "đục lỗ", "tai thỏ",
  "dynamic island", "dynamic island", "đục lỗ", "đục lỗ", "đục lỗ"
];

// Loại bỏ giá trị trùng lặp và sắp xếp
const uniqueKichThuocList = ref([...new Set(kichThuocData)].sort());
const uniqueDoPhanGiaiList = ref([...new Set(doPhanGiaiData)].sort());
const uniqueDoSangToiDaList = ref([...new Set(doSangToiDaData)].sort((a, b) => parseInt(a) - parseInt(b)));
const uniqueTanSoQuetList = ref([...new Set(tanSoQuetData)].sort((a, b) => parseInt(a) - parseInt(b)));
const uniqueKieuManHinhList = ref([...new Set(kieuManHinhData)].sort());

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
  fetchCongNgheManHinhs();
});

const handleSubmit = async () => {
  const { ma, kieuManHinh, idCongNgheManHinh, kichThuoc, doPhanGiai, doSangToiDa, tanSoQuet } = manHinh.value;
  if (!ma || !kieuManHinh || !idCongNgheManHinh || !kichThuoc || !doPhanGiai || !doSangToiDa || !tanSoQuet) {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Vui lòng chọn/nhập đầy đủ thông tin!');
    }
    return;
  }

  // Chỉ kiểm tra trùng lặp mã
  if (await checkDuplicate('ma', ma)) {
    if (toast.value) {
      toast.value?.kshowToast('error', 'Mã màn hình đã tồn tại!');
    }
    return;
  }

  confirmAction('Bạn có chắc chắn muốn thêm màn hình này?', async () => {
    try {
      await saveManHinh();
      if (toast.value) {
        toast.value?.kshowToast('success', 'Thêm màn hình thành công!');
      }
      router.push('/screen');
    } catch (error) {
      if (toast.value) {
        toast.value?.kshowToast('error', 'Không thể thêm màn hình: ' + error.message);
      }
      console.error('Save man hinh error:', error);
    }
  });
};

const goBack = () => {
  router.push('/screen');
};
</script>

<style scoped>
.input-field {
  @apply w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-sm;
}
</style>