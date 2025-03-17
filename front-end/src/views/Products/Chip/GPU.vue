<template>
  <div>
    <!-- Thay Breadcrumb bằng BreadcrumbWrapper -->
    <BreadcrumbWrapper :breadcrumb-items="breadcrumbItems" />

    <div class="mt-4">
      <div class="mt-4 flex">
        <div class="w-full max-w-4xl overflow-hidden bg-white border rounded-md shadow-md">
          <form>
            <div class="flex items-center justify-between px-5 py-3 text-gray-700 border-b">
              <h3 class="text-sm">Thêm GPU</h3>
              <button>
                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>

            <div class="p-5 text-gray-700 bg-gray-200 border-b grid grid-cols-2 gap-4">
              <div>
                <label class="text-xs">ID</label>
                <input type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Mã</label>
                <input type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Tên GPU</label>
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
      <h4 class="text-gray-600">Danh sách GPU</h4>
      <div class="mt-4">
        <table class="w-full bg-white rounded-md shadow-md">
          <thead>
          <tr class="bg-gray-200 text-gray-700">
            <th class="px-4 py-2">ID</th>
            <th class="px-4 py-2">Mã</th>
            <th class="px-4 py-2">Tên GPU</th>
            <th class="px-4 py-2">Hành động</th>
          </tr>
          </thead>
          <tbody>
          <tr class="border-t text-center" v-for="gpu in gpus" :key="gpu.id">
            <td class="px-4 py-2">{{ gpu.id }}</td>
            <td class="px-4 py-2">{{ gpu.code }}</td>
            <td class="px-4 py-2">{{ gpu.name }}</td>
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
import { ref, computed } from "vue";
import { useRoute } from 'vue-router';
import BreadcrumbWrapper from '@/components/BreadcrumbWrapper.vue'; // Import BreadcrumbWrapper

const route = useRoute();

// Tính toán breadcrumb dựa trên meta của route
const breadcrumbItems = computed(() => {
  if (typeof route.meta.breadcrumb === "function") {
    return route.meta.breadcrumb(route);
  }
  return route.meta?.breadcrumb || ["GPU"]; // Mặc định nếu không có breadcrumb
});

const gpus = ref([
  { id: "GPU001", code: "GPU001", name: "Adreno 740" },
  { id: "GPU002", code: "GPU002", name: "Adreno 730" },
  { id: "GPU003", code: "GPU003", name: "Mali-G710 MP10" },
  { id: "GPU004", code: "GPU004", name: "Mali-G78 MP22" },
  { id: "GPU005", code: "GPU005", name: "Apple GPU (5-core)" }
]);
</script>