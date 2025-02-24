<template>
  <div>
    <!-- Breadcrumb -->
    <Breadcrumb breadcrumb="Imel Đã Bán" />

    <div class="mt-4">
      <h4 class="text-gray-600">Imel Đã Bán</h4>

      <div class="mt-4 flex">
        <div class="w-full max-w-4xl overflow-hidden bg-white border rounded-md shadow-md">
          <form>
            <div class="flex items-center justify-between px-5 py-3 text-gray-700 border-b">
              <h3 class="text-sm">Thêm Imel Đã Bán</h3>
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
                <label class="text-xs">Mã</label>
                <input type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Imel</label>
                <input type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Ngày bán</label>
                <input type="date" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div class="col-span-3">
                <label class="text-xs">Ghi chú</label>
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
      <h4 class="text-gray-600">Danh sách Imel Đã Bán</h4>
      <div class="mt-4">
        <table class="w-full bg-white rounded-md shadow-md">
          <thead>
            <tr class="bg-gray-200 text-gray-700">
              <th class="px-4 py-2">ID</th>
              <th class="px-4 py-2">Mã</th>
              <th class="px-4 py-2">Imel</th>
              <th class="px-4 py-2">Ngày bán</th>
              <th class="px-4 py-2">Ghi chú</th>
              <th class="px-4 py-2">Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr class="border-t text-center" v-for="item in imelSoldList" :key="item.id">
              <td class="px-4 py-2">{{ item.id }}</td>
              <td class="px-4 py-2">{{ item.code }}</td>
              <td class="px-4 py-2">{{ item.imel }}</td>
              <td class="px-4 py-2">{{ item.soldDate }}</td>
              <td class="px-4 py-2">{{ item.note }}</td>
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

const imelSoldList = ref([
  { id: "IM001", code: "A123", imel: "123456789012345", soldDate: "2024-02-10", note: "Khách hàng A" },
  { id: "IM002", code: "B456", imel: "987654321098765", soldDate: "2024-02-15", note: "Khách hàng B" },
  { id: "IM003", code: "C789", imel: "567890123456789", soldDate: "2024-02-20", note: "Khách hàng C" }
]);
</script>
