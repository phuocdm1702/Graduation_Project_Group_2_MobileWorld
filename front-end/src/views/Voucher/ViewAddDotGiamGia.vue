<template>
  <div class="min-h-screen bg-gray-100">
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" class="mb-6" />

    <div class="container mx-auto px-4">
      <ToastNotification ref="toast" />

      <!-- Container chính -->
      <div class="grid grid-cols-1 gap-6 lg:grid-cols-2">
        <!-- Form Đợt Giảm Giá -->
        <div class="bg-white border rounded-lg shadow-md p-6">
          <h4 class="text-lg font-semibold text-gray-800 mb-4">Thông Tin Đợt Giảm Giá</h4>
          <form @submit.prevent="confirmAction" class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Tên đợt giảm giá</label>
              <input
                type="text"
                v-model="dotGiamGia.tenDotGiamGia"
                class="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-400 focus:border-blue-400 outline-none transition"
                placeholder="Nhập tên đợt giảm giá"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Loại giảm giá</label>
              <select
                v-model="dotGiamGia.loaiGiamGiaApDung"
                class="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-400 focus:border-blue-400 outline-none transition"
              >
                <option value="">Chọn loại giảm giá</option>
                <option value="Phần trăm">Phần trăm</option>
                <option value="Tiền mặt">Tiền mặt</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Giá trị giảm giá</label>
              <input
                type="number"
                v-model="dotGiamGia.giaTriGiamGia"
                :disabled="isTienMat"
                class="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-400 focus:border-blue-400 outline-none transition disabled:bg-gray-100"
                placeholder="Nhập giá trị giảm"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Số tiền giảm tối đa</label>
              <input
                type="text"
                v-model="formattedSoTienGiamToiDa"
                class="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-400 focus:border-blue-400 outline-none transition"
                placeholder="Nhập số tiền tối đa"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Ngày bắt đầu</label>
              <input
                type="date"
                v-model="dotGiamGia.ngayBatDau"
                class="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-400 focus:border-blue-400 outline-none transition"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Ngày kết thúc</label>
              <input
                type="date"
                v-model="dotGiamGia.ngayKetThuc"
                class="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-400 focus:border-blue-400 outline-none transition"
              />
            </div>
            <div class="flex gap-3 mt-6">
              <button
                type="submit"
                class="flex-1 px-4 py-2 bg-orange-500 text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 focus:ring-2 focus:ring-orange-400 transition"
              >
                {{ isEditMode ? 'Cập nhật' : 'Thêm' }}
              </button>
              <router-link to="/dot-giam-gia" class="flex-1">
                <button
                  type="button"
                  class="w-full px-4 py-2 bg-gray-300 text-gray-700 font-semibold rounded-lg hover:bg-gray-400 focus:ring-2 focus:ring-gray-400 transition"
                >
                  Quay Về
                </button>
              </router-link>
            </div>
          </form>
        </div>

        <!-- Bảng Sản Phẩm -->
        <div class="bg-white border rounded-lg shadow-md p-6">
          <h4 class="text-lg font-semibold text-gray-800 mb-4">Sản Phẩm</h4>
          <input
            v-model="searchKeyword"
            placeholder="Tìm kiếm theo tên, mã..."
            class="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-400 focus:border-blue-400 outline-none transition mb-4"
          />
          <div class="grid grid-cols-2 gap-4 mb-6 sm:grid-cols-2 lg:grid-cols-2">
            <div >
              <label class="block text-sm font-medium text-gray-700 mb-1">Hệ điều hành</label>
              <select
                v-model="selectedHeDieuHanh"
                class="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-400 focus:border-blue-400 outline-none transition"
              >
                <option value="">Tất cả</option>
                <option v-for="hdh in uniqueHeDieuHanh" :key="hdh.id" :value="hdh.id">
                  {{ hdh.heDieuHanh }} {{ hdh.phienBan }}
                </option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Nhà sản xuất</label>
              <select
                v-model="selectedNhaSanXuat"
                class="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-400 focus:border-blue-400 outline-none transition"
              >
                <option value="">Tất cả</option>
                <option v-for="nsx in uniqueNhaSanXuat" :key="nsx.id" :value="nsx.id">
                  {{ nsx.nhaSanXuat }}
                </option>
              </select>
            </div>
          </div>
          <div class="max-h-[500px] overflow-y-auto rounded-md border border-gray-200">
            <DynamicTable
              :data="dspList"
              :columns="columns"
              :getNestedValue="getNestedValue"
              class="w-full"
            />
          </div>
          <footer class="mt-4 flex justify-center">
            <Pagination
              :current-page="currentPageDSP"
              :total-pages="totalPagesDSP"
              @page-changed="changePageDSP"
              class="inline-flex items-center gap-2"
            />
          </footer>
        </div>
      </div>

      <!-- Chi Tiết Sản Phẩm -->
      <div class="bg-white border rounded-lg shadow-md p-6 mt-6">
        <div class="flex justify-between items-center mb-4">
          <h4 class="text-lg font-semibold text-gray-800">Chi Tiết Sản Phẩm</h4>
          <div class="flex gap-2">
            <button
              @click="selectAllCTSP"
              class="px-4 py-2 bg-blue-500 text-white font-semibold rounded-lg hover:bg-blue-600 focus:ring-2 focus:ring-blue-400 transition disabled:bg-blue-300 disabled:cursor-not-allowed"
              :disabled="idDSPs.length === 0"
            >
              Chọn tất cả
            </button>
            <button
              @click="deselectAllCTSP"
              class="px-4 py-2 bg-red-500 text-white font-semibold rounded-lg hover:bg-red-600 focus:ring-2 focus:ring-red-400 transition disabled:bg-red-300 disabled:cursor-not-allowed"
              :disabled="idDSPs.length === 0"
            >
              Bỏ chọn tất cả
            </button>
          </div>
        </div>
        <div class="grid grid-cols-1 gap-4 mb-6 sm:grid-cols-2 lg:grid-cols-3">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Dòng sản phẩm</label>
            <select
              v-model="selectedDongSanPham"
              @change="updateBoNhoTrong"
              class="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-400 focus:border-blue-400 outline-none transition"
            >
              <option value="">Tất cả</option>
              <option v-for="dong in uniqueDongSanPhams" :key="dong" :value="dong">
                {{ dong }}
              </option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Bộ nhớ trong</label>
            <select
              v-model="selectedBoNhoTrong"
              class="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-400 focus:border-blue-400 outline-none transition"
            >
              <option value="">Tất cả</option>
              <option v-for="boNho in filteredBoNhoTrong" :key="boNho" :value="boNho">
                {{ boNho }}
              </option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Màu sắc</label>
            <select
              v-model="selectedMauSac"
              class="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-400 focus:border-blue-400 outline-none transition"
            >
              <option value="">Tất cả</option>
              <option v-for="mau in filteredMauSac" :key="mau" :value="mau">
                {{ mau }}
              </option>
            </select>
          </div>
        </div>
        <div class="max-h-[500px] overflow-y-auto rounded-md border border-gray-200">
          <div v-if="filteredCTSPList.length === 0" class="text-center py-6 text-gray-500">
            {{ isEditMode ? 'Không có dữ liệu chi tiết đợt giảm giá' : 'Không có dữ liệu' }}
          </div>
          <DynamicTable
            v-else
            :data="filteredCTSPList"
            :columns="columns2"
            :getNestedValue="getNestedValue2"
            class="w-full"
          />
        </div>
        <footer class="mt-4 flex justify-center">
          <Pagination
            :current-page="currentPageCTSP"
            :total-pages="totalPagesCTSP"
            @page-changed="changePageCTSP"
            class="inline-flex items-center gap-2"
          />
        </footer>
      </div>
    </div>

    <ConfirmModal
      :show="showConfirmModal"
      :message="confirmMessage"
      @confirm="executeConfirmedAction"
      @cancel="closeConfirmModal"
    />
  </div>
</template>

<script setup>
import { useDotGiamGia } from './ViewAddDotGiamGia.js';
import { computed } from "vue";
import { useRoute, useRouter } from 'vue-router';
import DynamicTable from "@/components/DynamicTable.vue";
import Pagination from '@/components/Pagination.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';
import ToastNotification from '@/components/ToastNotification.vue';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue';

const route = useRoute();
const router = useRouter();

const {
  toast,
  currentPageDSP,
  changePageDSP,
  totalPagesDSP,
  currentPageCTSP,
  changePageCTSP,
  totalPagesCTSP,
  dspList,
  searchKeyword,
  selectedDongSanPham,
  selectedBoNhoTrong,
  selectedHeDieuHanh, 
  selectedNhaSanXuat, 
  selectedMauSac,
  dotGiamGia,
  edit,
  uniqueDongSanPhams,
  filteredBoNhoTrong,
  filteredMauSac,
  filteredCTSPList,
  uniqueHeDieuHanh,
  uniqueNhaSanXuat,
  confirmAction,
  columns,
  getNestedValue,
  columns2,
  getNestedValue2,
  displayedPagesDSP,
  displayedPagesCTSP,
  showConfirmModal,
  confirmMessage,
  executeConfirmedAction,
  closeConfirmModal,
  fetchCTSPData,
  idDSPs,
  selectAllCTSP,
  deselectAllCTSP,
} = useDotGiamGia();

const isEditMode = computed(() => edit.value);

const breadcrumbItems = computed(() => {
  if (edit.value) {
    return ["Quản Lý Phiếu Giảm Giá", "Đợt giảm giá", `Cập nhật đợt giảm giá`];
  }
  return ["Quản Lý Phiếu Giảm Giá", "Đợt giảm giá", "Thêm đợt giảm giá"];
});

const isTienMat = computed(() => {
  return dotGiamGia.value.loaiGiamGiaApDung === "Tiền mặt";
});

const updateBoNhoTrong = () => {
  currentPageCTSP.value = 0;
};

const formattedSoTienGiamToiDa = computed({
  get() {
    const value = dotGiamGia.value.soTienGiamToiDa;
    return value ? Number(value).toLocaleString('vi-VN') : '';
  },
  set(newValue) {
    // Loại bỏ các dấu chấm hoặc ký tự không phải số
    const rawValue = newValue.replace(/[^0-9]/g, '');
    dotGiamGia.value.soTienGiamToiDa = rawValue ? Number(rawValue) : 0;
  }
});
</script>

<style scoped>
</style>