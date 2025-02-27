<template>
  <div>
    <!-- Breadcrumb -->
    <Breadcrumb breadcrumb="Quản lý Nhân Viên" />

    <div class="mt-4">
      <h4 class="text-gray-600">Quản lý Nhân Viên</h4>
      <div class="mt-4">
        <div class="w-full max-w-4xl overflow-hidden bg-white border rounded-md shadow-md">
          <form>
            <div class="flex items-center justify-between px-5 py-3 text-gray-700 border-b">
              <h3 class="text-sm">Thêm Nhân Viên</h3>
              <button>
                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>
            <div class="p-5 text-gray-700 bg-gray-200 border-b grid grid-cols-3 gap-4">
              <div>
                <label class="text-xs">ID Tài khoản</label>
                <input type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Mã</label>
                <input type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Tên Nhân viên</label>
                <input type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Ngày sinh</label>
                <input type="date" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Ảnh nhân viên</label>
                <input type="file" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Ghi chú</label>
                <input type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Email</label>
                <input type="email" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Thành phố</label>
                <input type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Quận</label>
                <input type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Phường</label>
                <input type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">Địa chỉ cụ thể</label>
                <input type="text" class="w-full px-4 py-2 mt-2 border rounded-md" />
              </div>
              <div>
                <label class="text-xs">CCCD</label>
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
      <h4 class="text-gray-600">Danh sách Nhân Viên</h4>
      <div class="mt-4">
        <table class="w-full bg-white rounded-md shadow-md">
          <thead>
            <tr class="bg-gray-200 text-gray-700">
              <th class="px-4 py-2">ID</th>
              <th class="px-4 py-2">ID Tài khoản</th>
              <th class="px-4 py-2">Mã</th>
              <th class="px-4 py-2">Tên Nhân viên</th>
              <th class="px-4 py-2">Ngày sinh</th>
              <th class="px-4 py-2">Ảnh nhân viên</th>
              <th class="px-4 py-2">Ghi chú</th>
              <th class="px-4 py-2">Thành phố</th>
              <th class="px-4 py-2">Quận</th>
              <th class="px-4 py-2">Phường</th>
              <th class="px-4 py-2">Địa chỉ cụ thể</th>
              <th class="px-4 py-2">CCCD</th>
              <th class="px-4 py-2">Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr class="border-t text-center" v-for="nv in dataTable" :key="nv.id">
              <td class="px-4 py-2">{{ nv.id }}</td>
              <td class="px-4 py-2">{{ nv.idTaiKhoan.id }}</td>
              <td class="px-4 py-2">{{ nv.ma }}</td>
              <td class="px-4 py-2">{{ nv.tenNhanVien }}</td>
              <td class="px-4 py-2">{{ nv.ngaySinh }}</td>
              <td class="px-4 py-2"><img :src="nv.anhNhanVien" class="w-10 h-10 rounded-md" /></td>
              <td class="px-4 py-2">{{ nv.ghiChu }}</td>
              <td class="px-4 py-2">{{ nv.thanhPho }}</td>
              <td class="px-4 py-2">{{ nv.quan }}</td>
              <td class="px-4 py-2">{{ nv.phuong }}</td>
              <td class="px-4 py-2">{{ nv.diaChiCuThe }}</td>
              <td class="px-4 py-2">{{ nv.cccd }}</td>
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
import { ref, onMounted } from "vue";
import axios from "axios";

const dataTable = ref([]);

onMounted(async () => {
  try {
    const res = await axios.get("http://localhost:8080/nhan-vien/home");
    console.log("Dữ liệu từ API:", res.data);
    dataTable.value = res.data;
  } catch (error) {
    console.error("Lỗi:", error);
  }
});
</script>
