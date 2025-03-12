<template>
  <div>
    <!-- Breadcrumb -->
    <Breadcrumb breadcrumb="Đợt Giảm Giá" />

    <div class="bg-white p-4 rounded-md shadow-md">
      <h4 class="text-gray-600 text-lg font-semibold mb-4">Quản lý Đợt Giảm Giá</h4>

      <!-- Container for form and product list -->
      <div class="flex gap-6 w-full">
        <!-- Form -->
        <div class="w-3/5 bg-white border rounded-md shadow-md p-6">
          <form @submit.prevent="confirmAction" class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Mã</label>
              <input
                type="text"
                v-model="dotGiamGia.ma"
                class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-400 outline-none"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Tên đợt giảm giá</label>
              <input
                type="text"
                v-model="dotGiamGia.tenDotGiamGia"
                class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-400 outline-none"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Loại giảm giá</label>
              <select
                v-model="dotGiamGia.loaiGiamGiaApDung"
                class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-400 outline-none"
              >
                <option>Phần trăm</option>
                <option>Tiền mặt</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Giá trị giảm giá</label>
              <input
                type="number"
                v-model="dotGiamGia.giaTriGiamGia"
                :disabled="isTienMat"
                class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-400 outline-none"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Số tiền giảm tối đa</label>
              <input
                type="number"
                v-model="dotGiamGia.soTienGiamToiDa"
                class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-400 outline-none"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Ngày bắt đầu</label>
              <input
                type="date"
                v-model="dotGiamGia.ngayBatDau"
                class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-400 outline-none"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Ngày kết thúc</label>
              <input
                type="date"
                v-model="dotGiamGia.ngayKetThuc"
                class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-400 outline-none"
              />
            </div>
            <div class="flex gap-2 mt-4">
              <button
                class="flex items-center gap-2 px-4 py-2 bg-[#f97316] text-white font-semibold rounded-lg shadow-md hover:bg-orange-600 transition"
              >
                {{ edit ? 'Cập nhật' : 'Thêm' }}
              </button>
              <router-link to="/dot-giam-gia">
                <button
                  class="flex items-center gap-2 px-4 py-2 bg-gray-300 rounded-md hover:bg-gray-400 font-semibold "
                >
                  Quay Về
                </button>
              </router-link>
            </div>
          </form>
        </div>

        <!-- Bảng Dòng Sản Phẩm -->
        <div class="w-2/5 bg-white border rounded-md shadow-md p-6">
          <h5 class="text-gray-700 font-semibold mb-4">Dòng Sản Phẩm</h5>
          <input
            v-model="searchKeyword"
            placeholder="Tìm kiếm theo tên, mã"
            class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-400 outline-none mb-4"
          />
          <div class="max-h-[700px] overflow-y-auto">
            <DynamicTable
              :data="dspList"
              :columns="columns"
              :getNestedValue="getNestedValue"
            />
          </div>
        </div>
      </div>

      <!-- Chi Tiết Sản Phẩm -->
      <div class="w-full bg-white border rounded-md shadow-md p-6 mt-6">
        <h5 class="text-gray-700 font-semibold mb-4">Chi Tiết Sản Phẩm</h5>
        <div class="grid grid-cols-2 gap-4 mb-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Dòng sản phẩm:</label>
            <select
              v-model="selectedDongSanPham"
              @change="updateBoNhoTrong"
              class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-400 outline-none"
            >
              <option></option>
              <option v-for="dong in uniqueDongSanPhams" :key="dong" :value="dong">
                {{ dong }}
              </option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Bộ nhớ trong:</label>
            <select
              v-model="selectedBoNhoTrong"
              class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-400 outline-none"
            >
              <option></option>
              <option v-for="boNho in filteredBoNhoTrong" :key="boNho" :value="boNho">
                {{ boNho }}
              </option>
            </select>
          </div>
        </div>
        <div class="max-h-[700px] overflow-y-auto">
          <DynamicTable
            :data="filteredCTSPList"
            :columns="columns2"
            :getNestedValue="getNestedValue2"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useDotGiamGia } from './ViewAddDotGiamGia.js';
import { computed } from "vue";
import DynamicTable from "@/components/DynamicTable.vue";

const {
  dspList,
  ctspList,
  searchKeyword,
  idDSPs,
  selectedDongSanPham,
  selectedBoNhoTrong,
  dotGiamGia,
  edit,
  uniqueDongSanPhams,
  filteredBoNhoTrong,
  filteredCTSPList,
  addData,
  resetForm,
  confirmAction,
  columns,
  getNestedValue,
  columns2,
  getNestedValue2,
  fetchCTSPData
} = useDotGiamGia();

const isTienMat = computed(() => {
  return dotGiamGia.value.loaiGiamGiaApDung === "Tiền mặt";
});
</script>

<style scoped>
.input-field {
  @apply w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-400 outline-none;
}
</style>