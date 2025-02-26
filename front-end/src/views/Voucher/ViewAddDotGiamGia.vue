<template>
  <div>
    <!-- Breadcrumb -->
    <Breadcrumb breadcrumb="Đợt Giảm Giá" />
    <h4 class="text-gray-600">Quản lý Đợt Giảm Giá</h4>
    <div class="bg-white p-4 rounded-md shadow-md flex flex-wrap gap-3">
   

      <h4 class="text-gray-600 text-lg font-semibold">Quản lý Đợt Giảm Giá</h4>

      <!-- Container for form and product list -->
      <div class="flex gap-4 w-full">
        <!-- Form -->
        <div class="w-3/5 overflow-hidden bg-white border rounded-md shadow-md p-4">
          <form class="space-y-4">
            <div>
              <label class="block text-gray-700">Mã</label>
              <input type="text" class="w-full border rounded p-2">
            </div>
            <div>
              <label class="block text-gray-700">Tên đợt giảm giá</label>
              <input type="text" class="w-full border rounded p-2">
            </div>
            <div>
              <label class="block text-gray-700">Loại giảm giá</label>
              <select class="w-full border rounded p-2">
                <option>Phần trăm</option>
                <option>Tiền mặt</option>
              </select>
            </div>
            <div>
              <label class="block text-gray-700">Giá trị giảm giá</label>
              <input type="number" class="w-full border rounded p-2">
            </div>
            <div>
              <label class="block text-gray-700">Số tiền giảm tối đa</label>
              <input type="number" class="w-full border rounded p-2">
            </div>
            <div>
              <label class="block text-gray-700">Ngày bắt đầu</label>
              <input type="date" class="w-full border rounded p-2">
            </div>
            <div>
              <label class="block text-gray-700">Ngày kết thúc</label>
              <input type="date" class="w-full border rounded p-2">
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
            <table class="w-full mt-3 border-collapse border border-gray-300">
              <thead class="bg-gray-200">
              <tr>
                <th class="border border-gray-300 px-2 py-1"> </th>
                <th class="border border-gray-300 px-2 py-1">STT</th>
                <th class="border border-gray-300 px-2 py-1">Mã</th>
                <th class="border border-gray-300 px-2 py-1">Dòng sản phẩm</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(product, index) in dataTable" :key="index" class="border border-gray-300">
                <td class="px-2 py-1 border border-gray-300">
                  <input type="checkbox" v-model="selectedProducts" :value="product.id">
                </td>
                <td class="px-2 py-1 border border-gray-300">{{ product.id }}</td>
                <td class="px-2 py-1 border border-gray-300">{{ product.ma }}</td>
                <td class="px-2 py-1 border border-gray-300">{{ product.dongSanPham }}</td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- Bảng Chi Tiết Sản Phẩm -->
      <div class="w-full overflow-hidden bg-white border rounded-md shadow-md p-4 mt-4">
        <h5 class="text-gray-700 font-semibold">Chi Tiết Sản Phẩm</h5>
        <div class="max-h-[700px] overflow-y-auto">
          <table class="w-full mt-3 border-collapse border border-gray-300">
            <thead class="bg-gray-200">
            <tr>
              <th class="border border-gray-300 px-2 py-1"> </th>
              <th class="border border-gray-300 px-2 py-1">STT</th>
              <th class="border border-gray-300 px-2 py-1">Ảnh</th>
              <th class="border border-gray-300 px-2 py-1">Tên sản phẩm</th>
              <th class="border border-gray-300 px-2 py-1">Đơn giá</th>
              <th class="border border-gray-300 px-2 py-1">Giảm giá</th>
              <th class="border border-gray-300 px-2 py-1">Đơn giá sau giảm giá</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(detail, index) in productDetails" :key="index" class="border border-gray-300">
              <td class="px-2 py-1 border border-gray-300">{{ detail.id }}</td>
              <td class="px-2 py-1 border border-gray-300">
                <img :src="detail.image" alt="Ảnh" class="w-10 h-10 object-cover">
              </td>
              <td class="px-2 py-1 border border-gray-300">
                <input type="checkbox" v-model="selectedProducts" :value="product.id">
              </td>
              <td class="px-2 py-1 border border-gray-300">{{ detail.brand }}</td>
              <td class="px-2 py-1 border border-gray-300">{{ detail.name }}</td>
              <td class="px-2 py-1 border border-gray-300">{{ detail.chip }}</td>
              <td class="px-2 py-1 border border-gray-300">{{ detail.ssd }}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>


<script setup lang="ts">
import axios from "axios";
import { onMounted, ref, watch } from "vue";

const dataTable = ref([]);
const searchKeyword = ref(""); // Biến lưu từ khóa tìm kiếm

// Hàm fetch dữ liệu từ API
const fetchData = async () => {
  try {
    const res = await axios.get("http://localhost:8080/dot_giam_gia/ViewAddDotGiamGia", {
      params: { keyword: searchKeyword.value } // Gửi tham số keyword
    });
    console.log("Dữ liệu từ API:", res.data);
    dataTable.value = res.data;
  } catch (error) {
    console.error("Lỗi:", error);
  }
};

// Gọi API khi component được mounted
onMounted(fetchData);

// Theo dõi searchKeyword, gọi API khi thay đổi
watch(searchKeyword, fetchData);
</script>


<style src="@/assets/VoucherCss/dotVoucher.css"></style>
