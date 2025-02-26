<template>
  <div>
    <!-- Breadcrumb -->
    <Breadcrumb breadcrumb="Phiếu Giảm Giá" />
    
    <div class="mt-8">
      <h4 class="text-gray-600">Danh sách Phiếu Giảm Giá</h4>
      
        <div class="bg-white p-4 rounded-md shadow-md flex flex-wrap gap-3">
          <!-- Tìm kiếm -->
          <div class="relative w-1/4">
            <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-4.35-4.35m0 0A7.5 7.5 0 1011 3.5a7.5 7.5 0 005.65 13.15z"></path>
            </svg>
            <input
              type="text"
              v-model="searchQuery"
              placeholder="Tìm theo mã hoặc tên phiếu..."
              class="border px-10 py-2 rounded-md w-full"
            />
          </div>

          <!-- Loại Phiếu -->
          <div class="relative w-1/6">
            <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
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
            <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m0 0l-4 4m4-4V3m0 9V3"></path>
            </svg>
            <select v-model="filterStatus" class="border px-10 py-2 rounded-md w-full">
              <option value="">Tất cả trạng thái</option>
              <option value="Hoạt động">Hoạt động</option>
              <option value="Hết hạn">Hết hạn</option>
            </select>
          </div>

          <!-- Ngày bắt đầu -->
          <div class="relative">
            <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10m-10 4h7m-7 4h4"></path>
            </svg>
            <input type="date" v-model="startDate" class="border px-10 py-2 rounded-md" />
          </div>

          <!-- Ngày kết thúc -->
          <div class="relative">
            <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10m-10 4h7m-7 4h4"></path>
            </svg>
            <input type="date" v-model="endDate" class="border px-10 py-2 rounded-md" />
          </div>

          <!-- Hóa đơn tối thiểu -->
          <div class="relative w-1/6">
            <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 10l7-7m0 0l7 7m-7-7v18"></path>
            </svg>
            <input type="number" v-model="minOrder" placeholder="Hóa đơn tối thiểu" class="border px-10 py-2 rounded-md w-full" />
          </div>

          <!-- Giá trị phiếu -->
          <div class="relative w-1/6">
            <svg class="absolute left-3 top-3 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8V4m0 0l-4 4m4-4l4 4m0 0V20"></path>
            </svg>
            <input type="number" v-model="valueFilter" placeholder="Giá trị phiếu" class="border px-10 py-2 rounded-md w-full" />
          </div>
        </div>


      <div class="mt-4 flex justify-between items-center">
        <button @click="openModal" class="bg-blue-600 text-white px-4 py-2 rounded-md flex items-center hover:bg-blue-700 btn-modal">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/>
          </svg>
          Thêm Phiếu
        </button>
      </div>
      
      <div class="mt-4">
        <table class="w-full bg-white rounded-md shadow-md">
          <thead>
          <tr class="bg-gray-200 text-gray-700">
            <th class="px-4 py-2">Mã</th>
            <th class="px-4 py-2">Tên Phiếu</th>
            <th class="px-4 py-2">Loại Phiếu</th>
            <th class="px-4 py-2">Giá trị</th>
            <th class="px-4 py-2">Số tiền giảm tối đa</th>
            <th class="px-4 py-2">Hóa đơn tối thiểu</th>
            <th class="px-4 py-2">Ngày bắt đầu</th>
            <th class="px-4 py-2">Ngày kết thúc</th>
            <th class="px-4 py-2">Trạng thái</th>
            <th class="px-4 py-2">Hành động</th>
          </tr>
          </thead>
          <tbody>
          <tr class="border-t text-center" v-for="voucher in vouchers" :key="voucher.id">
            <td class="px-4 py-2">{{ voucher.id }}</td>
            <td class="px-4 py-2">{{ voucher.name }}</td>
            <td class="px-4 py-2">{{ voucher.type }}</td>
            <td class="px-4 py-2">{{ voucher.value }}</td>
            <td class="px-4 py-2">{{ voucher.maxDiscount }}</td>
            <td class="px-4 py-2">{{ voucher.minOrder }}</td>
            <td class="px-4 py-2">{{ voucher.startDate }}</td>
            <td class="px-4 py-2">{{ voucher.endDate }}</td>
            <td class="px-4 py-2">{{ voucher.status }}</td>
            <td class="px-4 py-2">
              <button class="text-blue-600 hover:underline mr-2">Sửa</button>
              <button class="text-red-600 hover:underline">Xóa</button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    
    <div v-if="isModalOpen" class="mt-4 modal">
      <div class="mt-4 flex">
        <div
          class="w-full max-w-4xl overflow-hidden bg-white border rounded-md shadow-md"
        >
          <form>
            <div
              class="flex items-center justify-between px-5 py-3 text-gray-700 border-b"
            >
              <h3 class="text-sm">Thêm Phiếu Giảm Giá</h3>
              <button @click="closeModal">&times;</button>
            </div>

            <div
              class="p-5 text-gray-700 bg-gray-200 border-b grid grid-cols-3 gap-4"
            >
              <div>
                <label class="text-xs">Mã</label>
                <input
                  type="text"
                  class="w-full px-4 py-2 mt-2 border rounded-md"
                />
              </div>
              <div>
                <label class="text-xs">Tên Phiếu</label>
                <input
                  type="text"
                  class="w-full px-4 py-2 mt-2 border rounded-md"
                />
              </div>
              <div>
                <label class="text-xs">Loại Phiếu</label>
                <div class="mt-2 flex items-center space-x-4">
                  <label class="flex items-center">
                    <input
                      type="radio"
                      name="voucherType"
                      value="Chung"
                      v-model="voucherType"
                      class="mr-2"
                    />
                    Chung
                  </label>
                  <label class="flex items-center">
                    <input
                      type="radio"
                      name="voucherType"
                      value="Cá nhân"
                      v-model="voucherType"
                      class="mr-2"
                    />
                    Cá nhân
                  </label>
                </div>
              </div>
              <div>
                <label class="text-xs">Giá trị</label>
                <input
                  type="text"
                  class="w-full px-4 py-2 mt-2 border rounded-md"
                />
              </div>
              <div>
                <label class="text-xs">Số tiền giảm tối đa</label>
                <input
                  type="text"
                  class="w-full px-4 py-2 mt-2 border rounded-md"
                />
              </div>
              <div>
                <label class="text-xs">Hóa đơn tối thiểu</label>
                <input
                  type="text"
                  class="w-full px-4 py-2 mt-2 border rounded-md"
                />
              </div>
              <div>
                <label class="text-xs">Ngày bắt đầu</label>
                <input
                  type="date"
                  class="w-full px-4 py-2 mt-2 border rounded-md"
                />
              </div>
              <div>
                <label class="text-xs">Ngày kết thúc</label>
                <input
                  type="date"
                  class="w-full px-4 py-2 mt-2 border rounded-md"
                />
              </div>
              <div>
                <label class="text-xs">Trạng thái</label>
                <select class="w-full px-4 py-2 mt-2 border rounded-md">
                  <option>Hoạt động</option>
                  <option>Hết hạn</option>
                </select>
              </div>
            </div>

            <div class="px-5 py-3 flex justify-between">
              <button
                class="px-3 py-1 text-sm text-gray-700 bg-gray-200 rounded-md hover:bg-gray-300"
              >
                Hủy
              </button>
              <button
                class="px-3 py-1 text-sm text-white bg-indigo-600 rounded-md hover:bg-indigo-500"
              >
                Lưu
              </button>
            </div>
          </form>
        </div>

        <div
          v-if="voucherType === 'Cá nhân'"
          class="ml-4 bg-white border rounded-md shadow-md p-4 customer-table-container"
        >
        <h4 class="text-gray-600 mb-2">Chọn Khách Hàng</h4>
          <table class="w-full bg-white rounded-md shadow-md">
            <thead>
            <tr class="bg-gray-200 text-gray-700">
              <th class="px-4 py-2">Chọn</th>
              <th class="px-4 py-2">Mã KH</th>
              <th class="px-4 py-2">Tên Khách Hàng</th>
            </tr>
            </thead>
            <tbody>
            <tr
              v-for="customer in customers"
              :key="customer.id"
              class="border-t text-center"
            >
              <td class="px-4 py-2">
                <input
                  type="checkbox"
                  v-model="selectedCustomers"
                  :value="customer.id"
                />
              </td>
              <td class="px-4 py-2">{{ customer.id }}</td>
              <td class="px-4 py-2">{{ customer.name }}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    
    <div v-if="isModalOpen" class="overlay"></div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";

const isModalOpen = ref(false);

const openModal = () => {
  isModalOpen.value = true;
};

const closeModal = () => {
  isModalOpen.value = false;
};

const vouchers = ref([
  {
    id: "VC001",
    name: "Giảm 10%",
    type: "Phần trăm",
    value: "10%",
    maxDiscount: "50,000đ",
    minOrder: "200,000đ",
    startDate: "2024-02-20",
    endDate: "2024-03-20",
    status: "Hoạt động",
  },
  {
    id: "VC002",
    name: "Giảm 50k",
    type: "Tiền mặt",
    value: "50,000đ",
    maxDiscount: "50,000đ",
    minOrder: "500,000đ",
    startDate: "2024-02-25",
    endDate: "2024-03-25",
    status: "Hết hạn",
  },
]);

const voucherType = ref("Chung");
const selectedCustomers = ref([]);
const customers = ref([
  { id: "KH001", name: "Nguyễn Văn A", email: "a@example.com" },
  { id: "KH002", name: "Trần Thị B", email: "b@example.com" },
  { id: "KH003", name: "Lê Văn C", email: "c@example.com" },
]);


</script>

<style src="@/assets/VoucherCss/voucher.css"></style>



