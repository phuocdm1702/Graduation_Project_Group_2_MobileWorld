<template>
  <div>
    <!-- Breadcrumb -->
    <Breadcrumb breadcrumb="Đợt Giảm Giá"/>
    <h4 class="text-gray-600">Quản lý Đợt Giảm Giá</h4>
    <div class="bg-white p-4 rounded-md shadow-md flex flex-wrap gap-3">
      <h4 class="text-gray-600 text-lg font-semibold">Quản lý Đợt Giảm Giá</h4>

      <!-- Container for form and product list -->
      <div class="flex gap-4 w-full">
        <!-- Form -->
        <div class="w-3/5 overflow-hidden bg-white border rounded-md shadow-md p-4">
          <form @submit.prevent="confirmAction" class="space-y-4">
            <div>
              <label class="block text-gray-700">Mã</label>
              <input type="text" v-model="dotGiamGia.ma" class="w-full border rounded p-2">
            </div>
            <div>
              <label class="block text-gray-700">Tên đợt giảm giá</label>
              <input type="text" v-model="dotGiamGia.tenDotGiamGia" class="w-full border rounded p-2">
            </div>
            <div>
              <label class="block text-gray-700">Loại giảm giá</label>
              <select v-model="dotGiamGia.loaiGiamGiaApDung" class="w-full border rounded p-2">
                <option>Phần trăm</option>
                <option>Tiền mặt</option>
              </select>
            </div>
            <div>
              <label class="block text-gray-700">Giá trị giảm giá</label>
              <input type="number" v-model="dotGiamGia.giaTriGiamGia" :disabled="isTienMat" class="w-full border rounded p-2">
            </div>
            <div>
              <label class="block text-gray-700">Số tiền giảm tối đa</label>
              <input type="number" v-model="dotGiamGia.soTienGiamToiDa" class="w-full border rounded p-2">
            </div>
            <div>
              <label class="block text-gray-700">Ngày bắt đầu</label>
              <input type="date" v-model="dotGiamGia.ngayBatDau" class="w-full border rounded p-2">
            </div>
            <div>
              <label class="block text-gray-700">Ngày kết thúc</label>
              <input type="date" v-model="dotGiamGia.ngayKetThuc" class="w-full border rounded p-2">
            </div>
            <div class="mt-2">
              <button type="submit" class="bg-indigo-600 text-white px-4 py-2 rounded">
                {{ edit ? 'Cập nhật' : 'Thêm' }}
              </button>
            </div>
            <div class="mt-2">
              <router-link to="/dot-giam-gia">
                <button type="button" class="bg-green-500 text-white px-3 py-1 rounded-md hover:bg-blue-600 transition">Quay về</button>
              </router-link>
            </div>
          </form>
        </div>

        <!-- Bảng Dòng Sản Phẩm -->
        <div class="w-2/5 overflow-hidden bg-white border rounded-md shadow-md p-4">
          <h5 class="text-gray-700 font-semibold">Dòng Sản Phẩm</h5>
          <input
            v-model="searchKeyword"
            placeholder="Tìm kiếm theo tên, mã"
            class="w-full p-2 border border-gray-300 rounded-md mt-2"
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

      <div class="w-full overflow-hidden bg-white border rounded-md shadow-md p-4 mt-4">
        <h5 class="text-gray-700 font-semibold">Chi Tiết Sản Phẩm</h5>
        <label for="dongSanPham">Dòng sản phẩm:</label>
        <select v-model="selectedDongSanPham" @change="updateBoNhoTrong">
          <option></option>
          <option v-for="dong in uniqueDongSanPhams" :key="dong" :value="dong">
            {{ dong }}
          </option>
        </select>
        <label for="boNhoTrong">Bộ nhớ trong:</label>
        <select v-model="selectedBoNhoTrong">
          <option></option>
          <option v-for="boNho in filteredBoNhoTrong" :key="boNho" :value="boNho">
            {{ boNho }}
          </option>
        </select>
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

<style src="@/assets/VoucherCss/dotVoucher.css"></style>