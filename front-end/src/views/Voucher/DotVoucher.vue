<template>
  <div>
    <!-- Breadcrumb -->
    <Breadcrumb breadcrumb="Đợt Giảm Giá" />

    <div class="mt-4">
      <h4 class="text-gray-600">Quản lý Đợt Giảm Giá</h4>

      <div class="mt-4 flex">
        <div
          class="w-full max-w-4xl overflow-hidden bg-white border rounded-md shadow-md"
        >
          <form>
            <div
              class="flex items-center justify-between px-5 py-3 text-gray-700 border-b"
            >
              <h3 class="text-sm">Thêm Đợt Giảm Giá</h3>
              <button>
                <svg
                  class="w-4 h-4"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M6 18L18 6M6 6l12 12"
                  />
                </svg>
              </button>
            </div>

            <div class="p-5 text-gray-700 bg-gray-200 border-b grid grid-cols-2 gap-4">
              <div>
                <label class="text-xs">ID Đợt Giảm Giá</label>
                <input type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">ID Dòng Sản Phẩm</label>
                <input type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Giá Ban Đầu</label>
                <input type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Giá Sau Khi Giảm</label>
                <input type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
            </div>

            <div class="px-5 py-3 flex justify-between">
              <button class="px-3 py-1 text-sm text-gray-700 bg-gray-200 rounded-md hover:bg-gray-300">Hủy</button>
              <button class="px-3 py-1 text-sm text-white bg-indigo-600 rounded-md hover:bg-indigo-500">Lưu</button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <div class="mt-8">
      <h4 class="text-gray-600">Danh sách Đợt Giảm Giá</h4>
      <div class="mt-4">
        <table class="w-full bg-white rounded-md shadow-md">
          <thead>
            <tr class="bg-gray-200 text-gray-700">
              <th class="px-4 py-2">ID</th>
              <th class="px-4 py-2">ID Đợt Giảm Giá</th>
              <th class="px-4 py-2">ID Dòng Sản Phẩm</th>
              <th class="px-4 py-2">Giá Ban Đầu</th>
              <th class="px-4 py-2">Giá Sau Khi Giảm</th>
              <th class="px-4 py-2">Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr class="border-t text-center" v-for="discount in discounts" :key="discount.id">
              <td class="px-4 py-2">{{ discount.id }}</td>
              <td class="px-4 py-2">{{ discount.batchId }}</td>
              <td class="px-4 py-2">{{ discount.productLineId }}</td>
              <td class="px-4 py-2">{{ discount.originalPrice }}</td>
              <td class="px-4 py-2">{{ discount.discountedPrice }}</td>
              <td class="px-4 py-2">
                <button class="text-blue-600 hover:underline mr-2">Sửa</button>
                <button class="text-red-600 hover:underline">Xóa</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";

const discounts = ref([
  { id: "D001", batchId: "BG001", productLineId: "PL001", originalPrice: "500,000đ", discountedPrice: "450,000đ" },
  { id: "D002", batchId: "BG002", productLineId: "PL002", originalPrice: "700,000đ", discountedPrice: "630,000đ" },
  { id: "D003", batchId: "BG003", productLineId: "PL003", originalPrice: "1,200,000đ", discountedPrice: "1,080,000đ" },
  { id: "D004", batchId: "BG004", productLineId: "PL004", originalPrice: "900,000đ", discountedPrice: "810,000đ" },
  { id: "D005", batchId: "BG005", productLineId: "PL005", originalPrice: "2,000,000đ", discountedPrice: "1,800,000đ" },
  { id: "D006", batchId: "BG006", productLineId: "PL006", originalPrice: "350,000đ", discountedPrice: "315,000đ" },
  { id: "D007", batchId: "BG007", productLineId: "PL007", originalPrice: "1,500,000đ", discountedPrice: "1,350,000đ" },
  { id: "D008", batchId: "BG008", productLineId: "PL008", originalPrice: "800,000đ", discountedPrice: "720,000đ" },
  { id: "D009", batchId: "BG009", productLineId: "PL009", originalPrice: "650,000đ", discountedPrice: "585,000đ" },
  { id: "D010", batchId: "BG010", productLineId: "PL010", originalPrice: "1,000,000đ", discountedPrice: "900,000đ" },
]);
</script>
