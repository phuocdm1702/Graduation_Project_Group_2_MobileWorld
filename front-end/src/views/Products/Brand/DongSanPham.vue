<template>
  <div>
    <h4 class="text-gray-600">Quản lý Dòng Sản Phẩm</h4>
    <div class="mt-4">
      <form @submit.prevent="saveProductLine">
        <div class="grid grid-cols-2 gap-4">
          <input v-model="productLine.ma" type="text" placeholder="Mã" class="border p-2 rounded" />
          <input v-model="productLine.dongSanPham" type="text" placeholder="Tên Dòng Sản Phẩm" class="border p-2 rounded" />
        </div>
        <div class="mt-2">
          <button type="submit" class="bg-indigo-600 text-white px-4 py-2 rounded">{{ editing ? 'Cập nhật' : 'Thêm' }}</button>
          <button v-if="editing" @click="cancelEdit" class="ml-2 bg-gray-400 text-white px-4 py-2 rounded">Hủy</button>
        </div>
      </form>
    </div>
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

<script setup lang="ts">
import { ref, onMounted } from "vue";
import axios from "axios";

const productLines = ref([]);
const productLine = ref({ id: null, ma: "", dongSanPham: "" });
const editing = ref(false);

const fetchData = async () => {
  try {
    const response = await axios.get("http://localhost:8080/api/dong-san-pham");
    productLines.value = response.data;
  } catch (error) {
    console.error("Lỗi khi tải dữ liệu!", error);
  }
};

const saveProductLine = async () => {
  try {
    if (editing.value) {
      await axios.put(`http://localhost:8080/api/dong-san-pham/${productLine.value.id}`, productLine.value);
    } else {
      await axios.post("http://localhost:8080/api/dong-san-pham", productLine.value);
    }
    await fetchData();
    resetForm();
  } catch (error) {
    console.error("Lỗi khi lưu dữ liệu!", error);
  }
};

const editProductLine = (product) => {
  productLine.value = { ...product };
  editing.value = true;
};

const cancelEdit = () => {
  resetForm();
};

const deleteProductLine = async (id) => {
  if (confirm("Bạn có chắc muốn xóa?")) {
    try {
      await axios.delete(`http://localhost:8080/api/dong-san-pham/${id}`);
      await fetchData();
    } catch (error) {
      console.error("Lỗi khi xóa dữ liệu!", error);
    }
  }
};

const resetForm = () => {
  productLine.value = { id: null, ma: "", dongSanPham: "" };
  editing.value = false;
};

onMounted(fetchData);
</script>
