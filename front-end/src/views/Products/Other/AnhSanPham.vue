<template>
  <div>
    <!-- Breadcrumb -->
    <Breadcrumb breadcrumb="Ảnh Sản Phẩm" />

    <div class="mt-4">
      <h4 class="text-gray-600">Ảnh Sản Phẩm</h4>

      <div class="mt-4 flex">
        <div class="w-full max-w-4xl overflow-hidden bg-white border rounded-md shadow-md">
          <form>
            <div class="flex items-center justify-between px-5 py-3 text-gray-700 border-b">
              <h3 class="text-sm">Thêm Ảnh Sản Phẩm</h3>
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
                <label class="text-xs">Tên ảnh</label>
                <input type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Đường dẫn</label>
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
      <h4 class="text-gray-600">Danh sách Ảnh Sản Phẩm</h4>
      <div class="mt-4">
        <table class="w-full bg-white rounded-md shadow-md">
          <thead>
            <tr class="bg-gray-200 text-gray-700">
              <th class="px-4 py-2">ID</th>
              <th class="px-4 py-2">Mã</th>
              <th class="px-4 py-2">Tên ảnh</th>
              <th class="px-4 py-2">Đường dẫn</th>
              <th class="px-4 py-2">Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr class="border-t text-center" v-for="image in productImages" :key="image.id">
              <td class="px-4 py-2">{{ image.id }}</td>
              <td class="px-4 py-2">{{ image.code }}</td>
              <td class="px-4 py-2">{{ image.name }}</td>
              <td class="px-4 py-2">{{ image.path }}</td>
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

const productImages = ref([
  { id: "IMG001", code: "IMG001", name: "Ảnh mặt trước", path: "/images/front.jpg" },
  { id: "IMG002", code: "IMG002", name: "Ảnh mặt sau", path: "/images/back.jpg" },
  { id: "IMG003", code: "IMG003", name: "Ảnh cạnh bên", path: "/images/side.jpg" }
]);
</script>
