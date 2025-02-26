<template>
  <div>
    <!-- Toast thông báo -->
    <ToastNotification ref="toastRef" />

    <!-- Tiêu đề -->
    <h4 class="text-gray-600">Quản lý Dòng Sản Phẩm</h4>

    <!-- Ô tìm kiếm -->
    <div class="mt-4 flex gap-2">
      <input v-model="searchKeyword" type="text" placeholder="Tìm kiếm theo mã hoặc tên..."
             class="border p-2 rounded flex-1" />
      <button @click="searchProductLine" class="bg-blue-500 text-white px-4 py-2 rounded">Tìm kiếm</button>
      <button @click="resetSearch" class="bg-gray-400 text-white px-4 py-2 rounded">Xóa</button>
    </div>

    <!-- Khung nhập liệu -->
    <div class="mt-6 p-4 border rounded-md bg-gray-100 shadow-md">
      <h5 class="text-lg font-semibold mb-3">Thêm Mới Dòng Sản Phẩm</h5>
      <form @submit.prevent="saveProductLine">
        <div class="grid grid-cols-2 gap-4">
          <input v-model="productLine.ma" type="text" placeholder="Mã" class="border p-2 rounded" />
          <input v-model="productLine.dongSanPham" type="text" placeholder="Tên Dòng Sản Phẩm"
                 class="border p-2 rounded" />
        </div>
        <div class="mt-2 flex justify-end">
          <button type="submit" class="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 transition">
            {{ editing ? 'Cập nhật' : 'Thêm' }}
          </button>
          <button v-if="editing" @click="cancelEdit" class="ml-2 bg-gray-400 text-white px-4 py-2 rounded">Hủy</button>
        </div>
      </form>
    </div>

    <!-- Danh sách dòng sản phẩm -->
    <div class="mt-8 relative overflow-x-auto shadow-md sm:rounded-lg">
      <table class="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
        <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
        <tr class="bg-blue-200 text-blue-700 text-center items-center">
          <th scope="col" class="px-6 py-3">ID</th>
          <th scope="col" class="px-6 py-3">Mã</th>
          <th scope="col" class="px-6 py-3">Tên Dòng Sản Phẩm</th>
          <th scope="col" class="px-6 py-3">Hành động</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="product in productLines" :key="product.id"
            class="bg-white border-b text-center hover:bg-gray-100 transition">
          <td class="px-6 py-4">{{ product.id }}</td>
          <td class="px-6 py-4">{{ product.ma }}</td>
          <td class="px-6 py-4">{{ product.dongSanPham }}</td>
          <td class="px-6 py-4">
            <button @click="editProductLine(product)" class="text-blue-600 hover:scale-110 transition">
              <i class="fa-solid fa-edit"></i>
            </button>
            <button @click="deleteProductLine(product.id)" class="text-red-600 hover:scale-110 transition ml-4">
              <i class="fa-solid fa-trash"></i>
            </button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- Phân trang -->
    <Pagination :currentPage="currentPage" :totalPages="totalPages" @page-changed="goToPage" />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import axios from "axios";
import ToastNotification from "@/components/ToastNotification.vue";
import Pagination from "@/components/Pagination.vue"; // Import component phân trang

// Refs
const toastRef = ref();
const productLines = ref([]);
const productLine = ref({ id: null, ma: "", dongSanPham: "" });
const searchKeyword = ref("");
const editing = ref(false);
const currentPage = ref(0);
const pageSize = ref(5);
const totalItems = ref(0);

// Computed properties
const totalPages = computed(() => Math.ceil(totalItems.value / pageSize.value));

// Hàm fetch dữ liệu
const fetchData = async () => {
  try {
    const response = await axios.get("http://localhost:8080/api/dong-san-pham", {
      params: { page: currentPage.value, size: pageSize.value }
    });
    productLines.value = response.data.content;
    totalItems.value = response.data.totalElements;
  } catch {
    toastRef.value?.showToast("error", "Lỗi khi tải dữ liệu!");
  }
};

// Hàm chuyển trang
const goToPage = (page) => {
  currentPage.value = page;
  fetchData();
};

// Hàm tìm kiếm
const searchProductLine = async () => {
  if (!searchKeyword.value.trim()) {
    fetchData();
    return;
  }

  try {
    const response = await axios.get("http://localhost:8080/api/dong-san-pham/search", {
      params: { keyword: searchKeyword.value, page: currentPage.value, size: pageSize.value }
    });
    productLines.value = response.data.content;
    totalItems.value = response.data.totalElements;
  } catch {
    toastRef.value?.showToast("error", "Lỗi khi tìm kiếm!");
  }
};

// Hàm reset tìm kiếm
const resetSearch = () => {
  searchKeyword.value = "";
  currentPage.value = 0;
  fetchData();
};

// Hàm kiểm tra mã dòng sản phẩm đã tồn tại chưa
const checkMaExists = async (ma) => {
  try {
    const response = await axios.get(`http://localhost:8080/api/dong-san-pham/exists/ma`, {
      params: { ma }
    });
    return response.data; // Trả về true nếu mã đã tồn tại, ngược lại trả về false
  } catch {
    toastRef.value?.showToast("error", "Lỗi khi kiểm tra mã!");
    return false;
  }
};

// Hàm kiểm tra tên dòng sản phẩm đã tồn tại chưa
const checkDongSanPhamExists = async (dongSanPham) => {
  try {
    const response = await axios.get(`http://localhost:8080/api/dong-san-pham/exists/dongSanPham`, {
      params: { dongSanPham }
    });
    return response.data; // Trả về true nếu tên đã tồn tại, ngược lại trả về false
  } catch {
    toastRef.value?.showToast("error", "Lỗi khi kiểm tra tên!");
    return false;
  }
};

// Hàm lưu dòng sản phẩm
const saveProductLine = async () => {
  if (!productLine.value.ma.trim() || !productLine.value.dongSanPham.trim()) {
    toastRef.value?.showToast("error", "Vui lòng nhập đầy đủ thông tin!");
    return;
  }

  // Kiểm tra mã dòng sản phẩm đã tồn tại chưa (nếu không phải đang chỉnh sửa)
  if (!editing.value && (await checkMaExists(productLine.value.ma))) {
    toastRef.value?.showToast("error", "Mã dòng sản phẩm đã tồn tại!");
    return;
  }

  // Kiểm tra tên dòng sản phẩm đã tồn tại chưa (nếu không phải đang chỉnh sửa)
  if (!editing.value && (await checkDongSanPhamExists(productLine.value.dongSanPham))) {
    toastRef.value?.showToast("error", "Tên dòng sản phẩm đã tồn tại!");
    return;
  }

  try {
    if (editing.value) {
      await axios.put(`http://localhost:8080/api/dong-san-pham/${productLine.value.id}`, productLine.value);
      toastRef.value?.showToast("success", "Cập nhật thành công!");
    } else {
      await axios.post("http://localhost:8080/api/dong-san-pham", productLine.value);
      toastRef.value?.showToast("success", "Thêm thành công!");
    }

    // Reset form sau khi lưu thành công
    productLine.value = { id: null, ma: "", dongSanPham: "" };
    editing.value = false;

    // Fetch lại dữ liệu và reset tìm kiếm
    await fetchData();
    resetSearch();
  } catch {
    toastRef.value?.showToast("error", "Lỗi khi lưu dữ liệu!");
  }
};

// Hàm chỉnh sửa dòng sản phẩm
const editProductLine = (product) => {
  productLine.value = { ...product };
  editing.value = true;
};

// Hàm hủy chỉnh sửa
const cancelEdit = () => {
  productLine.value = { id: null, ma: "", dongSanPham: "" };
  editing.value = false;
  toastRef.value?.showToast("info", "Hủy chỉnh sửa!");
};

// Hàm xóa dòng sản phẩm
const deleteProductLine = async (id) => {
  if (confirm("Bạn có chắc muốn xóa?")) {
    try {
      await axios.delete(`http://localhost:8080/api/dong-san-pham/${id}`);
      toastRef.value?.showToast("success", "Xóa thành công!");
      await fetchData();
    } catch {
      toastRef.value?.showToast("error", "Lỗi khi xóa!");
    }
  }
};

// Gọi dữ liệu khi load trang
onMounted(fetchData);
</script>