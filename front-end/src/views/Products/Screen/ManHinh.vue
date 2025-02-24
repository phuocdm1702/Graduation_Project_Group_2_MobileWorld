<template>
  <div>
    <!-- Breadcrumb -->
    <Breadcrumb breadcrumb="Màn hình" />

    <div class="mt-4">
      <h4 class="text-gray-600">Màn hình</h4>

      <div class="mt-4 flex">
        <div class="w-full max-w-4xl overflow-hidden bg-white border rounded-md shadow-md">
          <form>
            <div class="flex items-center justify-between px-5 py-3 text-gray-700 border-b">
              <h3 class="text-sm">Thêm Màn hình</h3>
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
                <label class="text-xs">ID Công nghệ màn hình</label>
                <select class="w-full px-4 py-2 mt-2 border rounded-md">
                  <option v-for="tech in screenTechnologies" :key="tech.id" :value="tech.id">{{ tech.technology }}</option>
                </select>
              </div>
              <div>
                <label class="text-xs">Mã</label>
                <input type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Kích thước</label>
                <input type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Độ phân giải</label>
                <select class="w-full px-4 py-2 mt-2 border rounded-md">
                  <option>HD</option>
                  <option>Full HD</option>
                  <option>Quad HD</option>
                  <option>4K</option>
                </select>
              </div>
              <div>
                <label class="text-xs">Độ sáng tối đa</label>
                <select class="w-full px-4 py-2 mt-2 border rounded-md">
                  <option>500 nits</option>
                  <option>800 nits</option>
                  <option>1000 nits</option>
                  <option>1500 nits</option>
                </select>
              </div>
              <div>
                <label class="text-xs">Tần số quét</label>
                <select class="w-full px-4 py-2 mt-2 border rounded-md">
                  <option>60Hz</option>
                  <option>90Hz</option>
                  <option>120Hz</option>
                  <option>144Hz</option>
                </select>
              </div>
              <div>
                <label class="text-xs">Kiểu màn hình</label>
                <select class="w-full px-4 py-2 mt-2 border rounded-md">
                  <option>Phẳng</option>
                  <option>Cong</option>
                </select>
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
      <h4 class="text-gray-600">Danh sách Màn hình</h4>
      <div class="mt-4">
        <table class="w-full bg-white rounded-md shadow-md">
          <thead>
            <tr class="bg-gray-200 text-gray-700">
              <th class="px-4 py-2">ID</th>
              <th class="px-4 py-2">ID Công nghệ màn hình</th>
              <th class="px-4 py-2">Mã</th>
              <th class="px-4 py-2">Kích thước</th>
              <th class="px-4 py-2">Độ phân giải</th>
              <th class="px-4 py-2">Độ sáng tối đa</th>
              <th class="px-4 py-2">Tần số quét</th>
              <th class="px-4 py-2">Kiểu màn hình</th>
              <th class="px-4 py-2">Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr class="border-t text-center" v-for="screen in screens" :key="screen.id">
              <td class="px-4 py-2">{{ screen.id }}</td>
              <td class="px-4 py-2">{{ screen.technologyId }}</td>
              <td class="px-4 py-2">{{ screen.code }}</td>
              <td class="px-4 py-2">{{ screen.size }}</td>
              <td class="px-4 py-2">{{ screen.resolution }}</td>
              <td class="px-4 py-2">{{ screen.brightness }}</td>
              <td class="px-4 py-2">{{ screen.refreshRate }}</td>
              <td class="px-4 py-2">{{ screen.screenType }}</td>
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

const screenTechnologies = ref([
  { id: "ST001", technology: "OLED" },
  { id: "ST002", technology: "AMOLED" }
]);

const screens = ref([
  { id: "SCR001", technologyId: "ST001", code: "SCR-OLED-1", size: "6.5 inch", resolution: "Full HD", brightness: "1000 nits", refreshRate: "120Hz", screenType: "Phẳng" }
]);
</script>
