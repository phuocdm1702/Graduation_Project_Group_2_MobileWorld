<template>
  <div>
    <!-- Breadcrumb -->
    <Breadcrumb breadcrumb="Đợt Giảm Giá"/>
    <h4 class="text-gray-600">Quản lý Đợt Giảm Giá</h4>
    <div class="bg-white p-4 rounded-md shadow-md flex flex-wrap gap-3">

      <div class="relative w-1/4">
        <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"
             xmlns="http://www.w3.org/2000/svg">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M21 21l-4.35-4.35m0 0A7.5 7.5 0 1011 3.5a7.5 7.5 0 005.65 13.15z"></path>
        </svg>
        <input
          type="text"
          v-model="searchQuery"
          placeholder="Tìm theo mã hoặc tên đợt giảm giá..."
          class="border px-10 py-2 rounded-md w-full"
        />
      </div>

      <!-- Loại Phiếu -->
      <div class="relative w-1/6">
        <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"
             xmlns="http://www.w3.org/2000/svg">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 10l7-7m0 0l7 7m-7-7v18"></path>
        </svg>
        <select v-model="filterType" class="border px-10 py-2 rounded-md w-full">
          <option value="">Tất cả loại phiếu</option>
          <option value="Phần trăm">Phần trăm</option>
          <option value="Tiền mặt">Tiền mặt</option>
        </select>
      </div>

      <!-- Trạng thái -->
      <div class="relative w-1/6">
        <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"
             xmlns="http://www.w3.org/2000/svg">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M9 12l2 2 4-4m0 0l-4 4m4-4V3m0 9V3"></path>
        </svg>
        <select v-model="filterStatus" class="border px-10 py-2 rounded-md w-full">
          <option value="">Tất cả trạng thái</option>
          <option value="Hoạt động">Hoạt động</option>
          <option value="Hết hạn">Hết hạn</option>
        </select>
      </div>

      <!-- Ngày bắt đầu -->
      <div class="relative">
        <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"
             xmlns="http://www.w3.org/2000/svg">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M8 7V3m8 4V3m-9 8h10m-10 4h7m-7 4h4"></path>
        </svg>
        <input type="date" v-model="startDate" class="border px-10 py-2 rounded-md"/>
      </div>

      <!-- Ngày kết thúc -->
      <div class="relative">
        <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"
             xmlns="http://www.w3.org/2000/svg">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M8 7V3m8 4V3m-9 8h10m-10 4h7m-7 4h4"></path>
        </svg>
        <input type="date" v-model="endDate" class="border px-10 py-2 rounded-md"/>
      </div>

      <!-- Hóa đơn tối thiểu -->
      <div class="relative w-1/6">
        <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"
             xmlns="http://www.w3.org/2000/svg">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 10l7-7m0 0l7 7m-7-7v18"></path>
        </svg>
        <input type="number" v-model="minOrder" placeholder="Hóa đơn tối thiểu"
               class="border px-10 py-2 rounded-md w-full"/>
      </div>

      <!-- Giá trị phiếu -->
    </div>

    <div class="mt-4 flex justify-between items-center">
      <RouterLink
        to="/ViewAddDotGiamGia"
        class="bg-blue-600 text-white px-4 py-2 rounded-md flex items-center hover:bg-blue-700 btn-modal"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          class="h-5 w-5 mr-2"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
        >
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/>
        </svg>
        Thêm Đợt Giảm Giá
      </RouterLink>
    </div>

    <div class="mt-8">
      <h4 class="text-gray-600">Danh sách Đợt Giảm Giá</h4>
      <div class="mt-4">
        <table class="w-full bg-white rounded-md shadow-md">
          <thead>
          <tr class="bg-gray-200 text-gray-700">
            <th class="px-4 py-2">Id</th>
            <th class="px-4 py-2">Tên đợt giảm giá</th>
            <th class="px-4 py-2">Loại giảm giá</th>
            <th class="px-4 py-2">Giá trị</th>
            <th class="px-4 py-2">Số tiền giảm tối đa</th>
            <th class="px-4 py-2">Ngày bắt đầu</th>
            <th class="px-4 py-2">Ngày kết thúc</th>
            <th class="px-4 py-2">Trạng thái</th>
            <th class="px-4 py-2">Action</th>
          </tr>
          </thead>
          <tbody>
          <tr class="border-t text-center" v-for="discount in dataTable" :key="discount.id">
            <td class="px-4 py-2">{{ discount.id }}</td>
            <td class="px-4 py-2">{{ discount.tenDotGiamGia }}</td>
            <td class="px-4 py-2">{{ discount.loaiGiamGiaApDung }}</td>
            <td class="px-4 py-2">{{ discount.giaTriGiamGia }}</td>
            <td class="px-4 py-2">{{ discount.soTienGiamToiDa }}</td>
            <td class="px-4 py-2">
              {{ new Date(discount.ngayBatDau).toLocaleDateString("vi-VN") }}
            </td>
            <td class="px-4 py-2">
              {{ new Date(discount.ngayKetThuc).toLocaleDateString("vi-VN") }}
            </td>
            <td class="px-4 py-2">{{ discount.trangThai==true? 'Sắp tới':'Đang diễn ra' }}</td>
            <td class="px-4 py-2 flex justify-center gap-2">
              <button
                @click="deleteDotGiamGia(discount.id)"
                class="bg-red-500 text-white px-3 py-1 rounded-md hover:bg-red-600 transition">
                Xóa
              </button>
              <button
                @click="viewUpdate(discount)"
                class="bg-blue-500 text-white px-3 py-1 rounded-md hover:bg-blue-600 transition">
                Cập nhật
              </button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
    <!--    <div v-if="isModalOpen" class="overlay"></div>-->
  </div>
</template>


<script setup lang="ts">
import axios from "axios";
import {onMounted, ref, watch} from "vue";

import {useRouter} from "vue-router";

const router = useRouter();

const goToAddView = () => {
  router.push("/ViewAddDotGiamGia");
};

const isModalOpen = ref(false);

const openModal = () => {
  isModalOpen.value = true;
}

const dataTable = ref([]);

const dotGiamGia = ref({
  id: null,
  ma: "",
  tenDotGiamGia: "",
  loaiGiamGiaApDung: "",
  giaTriGiamGia: "",
  soTienGiamToiDa: "",
  ngayBatDau: "",
  ngayKetThuc: "",
  trangThai: "",
});

const fetchData = async () => {
  try {
    const res = await axios.get("http://localhost:8080/dot_giam_gia/home");
    console.log("Dữ liệu từ API:", res.data);
    dataTable.value = res.data;
  } catch (error) {
    console.error("Lỗi:", error);
  }
};

const deleteDotGiamGia = async (id: number) => {
  try {
    await axios.delete(`http://localhost:8080/dot_giam_gia/${id}`);
    console.log("Đã xóa đợt giảm giá với ID:", id);
    await fetchData();
  } catch (error) {
    console.error("Lỗi khi xóa đợt giảm giá:", error);
  }
};

const viewUpdate = (discount) => {
  router.push({
    path: "/ViewAddDotGiamGia",
    query: {
      id: discount.id,
      ma: discount.ma,
      tenDotGiamGia: discount.tenDotGiamGia,
      loaiGiamGiaApDung: discount.loaiGiamGiaApDung,
      giaTriGiamGia: discount.giaTriGiamGia,
      soTienGiamToiDa: discount.soTienGiamToiDa,
      ngayBatDau: discount.ngayBatDau,
      ngayKetThuc: discount.ngayKetThuc,
      trangThai: discount.trangThai,
    },
  });
};


onMounted(() => {
  fetchData();
});
</script>

<style src="@/assets/VoucherCss/dotVoucher.css"></style>
