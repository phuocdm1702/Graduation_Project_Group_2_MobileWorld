<template>
  <div>
    <ToastNotification ref="toastRef" />
    <h4 class="text-gray-600">Quản lý Dòng Sản Phẩm</h4>

    <!-- Ô tìm kiếm -->
    <div class="mt-4 flex gap-2">
      <input v-model="searchKeyword" type="text" placeholder="Tìm kiếm theo mã hoặc tên..." class="border p-2 rounded flex-1" />
      <button @click="searchProductLine" class="bg-blue-500 text-white px-4 py-2 rounded">Tìm kiếm</button>
      <button @click="resetSearch" class="bg-gray-400 text-white px-4 py-2 rounded">Xóa</button>
    </div>

    <!-- Khung nhập liệu -->
    <div class="mt-6 p-4 border rounded-md bg-gray-100 shadow-md">
      <h5 class="text-lg font-semibold mb-3">Thêm Mới Dòng Sản Phẩm</h5>
      <form @submit.prevent="saveProductLine">
        <div class="grid grid-cols-2 gap-4">
          <input v-model="productLine.ma" type="text" placeholder="Mã" class="border p-2 rounded" />
          <input v-model="productLine.dongSanPham" type="text" placeholder="Tên Dòng Sản Phẩm" class="border p-2 rounded" />
        </div>
        <div class="mt-2">
          <button type="submit" class="bg-indigo-600 text-white px-4 py-2 rounded">
            {{ editing ? 'Cập nhật' : 'Thêm' }}
          </button>
          <button v-if="editing" @click="cancelEdit" class="ml-2 bg-gray-400 text-white px-4 py-2 rounded">Hủy</button>
        </div>
      </form>
    </div>

    <!-- Danh sách dòng sản phẩm -->
    <div class="mt-8">
      <table class="w-full bg-white rounded-md shadow-md">
        <thead>
        <tr class="bg-gray-200 text-gray-700">
          <th>ID</th>
          <th>Mã</th>
          <th>Tên Dòng Sản Phẩm</th>
          <th>Hành động</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="product in productLines" :key="product.id" class="border-t text-center">
          <td>{{ product.id }}</td>
          <td>{{ product.ma }}</td>
          <td>{{ product.dongSanPham }}</td>
          <td>
            <button @click="editProductLine(product)" class="text-blue-600 hover:underline mr-2">Sửa</button>
            <button @click="deleteProductLine(product.id)" class="text-red-600 hover:underline">Xóa</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import ToastNotification from "@/components/ToastNotification.vue";

const toastRef = ref();
const productLines = ref([]);
const productLine = ref({ id: null, ma: "", dongSanPham: "" });
const searchKeyword = ref("");
const editing = ref(false);

const fetchData = async () => {
  try {
    const response = await axios.get("http://localhost:8080/api/dong-san-pham");
    productLines.value = response.data;
  } catch {
    toastRef.value?.showToast("error", "Lỗi khi tải dữ liệu!");
  }
};

const resetForm = () => {
  productLine.value = { id: null, ma: "", dongSanPham: "" };
  editing.value = false;
};

const saveProductLine = async () => {
  if (!productLine.value.ma.trim()) {
    toastRef.value?.showToast("error", "Vui lòng nhập mã dòng sản phẩm!");
    return;
  }
  if (!productLine.value.dongSanPham.trim()) {
    toastRef.value?.showToast("error", "Vui lòng nhập tên dòng sản phẩm!");
    return;
  }

  try {
    if (editing.value) {
      await axios.put(`http://localhost:8080/api/dong-san-pham/${productLine.value.id}`, productLine.value);
      toastRef.value?.showToast("success", "Cập nhật dòng sản phẩm thành công!");
    } else {
      await axios.post("http://localhost:8080/api/dong-san-pham", productLine.value);
      toastRef.value?.showToast("success", "Thêm dòng sản phẩm thành công!");
    }
    await fetchData();
    resetForm();
  } catch {
    toastRef.value?.showToast("error", "Lỗi khi lưu dữ liệu!");
  }
};

const editProductLine = (product) => {
  productLine.value = { ...product };
  editing.value = true;
};

const cancelEdit = () => {
  resetForm();
  toastRef.value?.showToast("info", "Hủy chỉnh sửa!");
};

const deleteProductLine = async (id) => {
  if (confirm("Bạn có chắc muốn xóa?")) {
    try {
      await axios.delete(`http://localhost:8080/api/dong-san-pham/${id}`);
      toastRef.value?.showToast("success", "Xóa dòng sản phẩm thành công!");
      await fetchData();
    } catch {
      toastRef.value?.showToast("error", "Lỗi khi xóa dữ liệu!");
    }
  }
};

onMounted(fetchData);
</script>
