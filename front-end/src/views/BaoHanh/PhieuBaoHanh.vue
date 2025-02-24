<template>
  <div>
    <!-- Breadcrumb -->
    <Breadcrumb breadcrumb="Phiếu Bảo Hành" />

    <div class="mt-4">
      <h4 class="text-gray-600">Phiếu Bảo Hành</h4>

      <div class="mt-4 flex">
        <div class="w-full max-w-4xl overflow-hidden bg-white border rounded-md shadow-md">
          <form>
            <div class="flex items-center justify-between px-5 py-3 text-gray-700 border-b">
              <h3 class="text-sm">Thêm Phiếu Bảo Hành</h3>
              <button>
                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>

            <div class="p-5 text-gray-700 bg-gray-200 border-b grid grid-cols-3 gap-4">
              <div>
                <label class="text-xs">ID</label>
                <input type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">ID Imel Đã Bán</label>
                <select class="w-full px-4 py-2 mt-2 border rounded-md">
                  <option value="">Chọn Imel</option>
                  <option v-for="imel in imelsSold" :key="imel.id" :value="imel.id">{{ imel.name }}</option>
                </select>
              </div>
              <div>
                <label class="text-xs">Ngày Bắt Đầu</label>
                <input type="date" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Ngày Kết Thúc</label>
                <input type="date" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div class="col-span-3">
                <label class="text-xs">Ghi Chú</label>
                <textarea class="w-full px-4 py-2 mt-2 border rounded-md"></textarea>
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
      <h4 class="text-gray-600">Danh sách Phiếu Bảo Hành</h4>
      <div class="mt-4">
        <table class="w-full bg-white rounded-md shadow-md">
          <thead>
            <tr class="bg-gray-200 text-gray-700">
              <th class="px-4 py-2">ID</th>
              <th class="px-4 py-2">ID Imel Đã Bán</th>
              <th class="px-4 py-2">Ngày Bắt Đầu</th>
              <th class="px-4 py-2">Ngày Kết Thúc</th>
              <th class="px-4 py-2">Ghi Chú</th>
              <th class="px-4 py-2">Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr class="border-t text-center" v-for="warranty in warranties" :key="warranty.id">
              <td class="px-4 py-2">{{ warranty.id }}</td>
              <td class="px-4 py-2">{{ warranty.imelSold }}</td>
              <td class="px-4 py-2">{{ warranty.startDate }}</td>
              <td class="px-4 py-2">{{ warranty.endDate }}</td>
              <td class="px-4 py-2">{{ warranty.note }}</td>
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

<script setup>
import { ref } from "vue";

const imelsSold = ref([
  { id: "IM001", name: "Imel 1" },
  { id: "IM002", name: "Imel 2" },
  { id: "IM003", name: "Imel 3" }
]);

const warranties = ref([
  { id: "WB001", imelSold: "Imel 1", startDate: "2025-01-01", endDate: "2026-01-01", note: "Bảo hành 1 năm" },
  { id: "WB002", imelSold: "Imel 2", startDate: "2025-02-01", endDate: "2026-02-01", note: "Bảo hành 1 năm" }
]);
</script>
