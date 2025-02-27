<template>
  <div class="container mx-auto p-4 relative">
    <!-- Toast thông báo -->
    <ToastNotification ref="toast" />

    <!-- Tiêu đề và nút mở modal -->
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-xl text-gray-600">Quản lý Dòng Sản Phẩm</h2>
      <button
        @click="openAddModal"
        class="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition"
      >
        Thêm mới
      </button>
    </div>

    <!-- Search Section -->
    <div class="flex gap-3 mb-6">
      <input
        v-model.trim="searchKeyword"
        type="text"
        placeholder="Tìm kiếm theo mã hoặc tên..."
        class="flex-1 border rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
      />
      <button
        @click="searchProductLine"
        class="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition"
      >
        Tìm kiếm
      </button>
      <button
        @click="resetSearch"
        class="bg-gray-400 text-white px-4 py-2 rounded-lg hover:bg-gray-500 transition"
      >
        Đặt lại
      </button>
    </div>

    <!-- Bulk Delete Button -->
    <div v-if="selectedProducts.length" class="mb-6 flex justify-end">
      <button
        @click="confirmDeleteSelected"
        class="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 transition"
      >
        Xóa {{ selectedProducts.length }} dòng sản phẩm đã chọn
      </button>
    </div>

    <!-- Product Lines Table -->
    <div class="overflow-x-auto shadow-md rounded-lg">
      <table class="w-full text-sm text-gray-500">
        <thead class="bg-blue-100 text-blue-700 uppercase">
        <tr>
          <th class="px-6 py-3 text-center">ID</th>
          <th class="px-6 py-3 text-center">Mã</th>
          <th class="px-6 py-3 text-center">Tên Dòng Sản Phẩm</th>
          <th class="px-6 py-3 text-center">Hành động</th>
          <th class="px-6 py-3 text-center">
            <input
              type="checkbox"
              v-model="selectAll"
              @change="toggleSelectAll"
              class="w-4 h-4 rounded"
            />
          </th>
        </tr>
        </thead>
        <tbody>
        <tr
          v-for="product in productLines"
          :key="product.id"
          class="bg-white border-b hover:bg-gray-50 transition"
        >
          <td class="px-6 py-4 text-center">{{ product.id }}</td>
          <td class="px-6 py-4 text-center">{{ product.ma }}</td>
          <td class="px-6 py-4 text-center">{{ product.dongSanPham }}</td>
          <td class="px-6 py-4 text-center space-x-4">
            <button
              @click="openEditModal(product)"
              class="text-blue-600 hover:text-blue-800 transition"
            >
              <i class="fa-solid fa-edit"></i>
            </button>
            <button
              @click="confirmDelete(product.id)"
              class="text-red-600 hover:text-red-800 transition"
            >
              <i class="fa-solid fa-trash"></i>
            </button>
          </td>
          <td class="px-6 py-4 text-center">
            <input
              type="checkbox"
              v-model="selectedProducts"
              :value="product.id"
              class="w-4 h-4 rounded"
            />
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <Pagination
      :current-page="currentPage"
      :total-pages="totalPages"
      @page-changed="goToPage"
      class="mt-6"
    />

    <!-- Modal Form (Thêm mới và Cập nhật) -->
    <ProductLineFormModal
      :show="showAddModal || showEditModal"
      :is-edit="showEditModal"
      :product-line="productLine"
      @submit="handleFormSubmit"
      @close="closeModal"
    />

    <!-- Modal Confirm -->
    <ConfirmModal
      :show="showConfirmModal"
      :message="confirmMessage"
      @confirm="executeConfirmedAction"
      @cancel="closeConfirmModal"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue';
import axios from 'axios';
import ToastNotification from '@/components/ToastNotification.vue';
import Pagination from '@/components/Pagination.vue';
import ProductLineFormModal from '@/components/ProductLineFormModal.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';

const toast = ref(null);
const productLines = ref([]);
const productLine = ref({ id: null, ma: '', dongSanPham: '' });
const searchKeyword = ref('');
const currentPage = ref(0);
const pageSize = ref(6);
const totalItems = ref(0);
const selectedProducts = ref([]);
const selectAll = ref(false);
const isSearching = ref(false);
const showAddModal = ref(false);
const showEditModal = ref(false);
const showConfirmModal = ref(false);
const confirmMessage = ref('');
const confirmedAction = ref(null);

const totalPages = computed(() => Math.ceil(totalItems.value / pageSize.value));

watch(searchKeyword, (newValue) => {
  isSearching.value = !!newValue.trim();
});

const fetchData = async () => {
  try {
    const { data } = await axios.get('http://localhost:8080/api/dong-san-pham', {
      params: { page: currentPage.value, size: pageSize.value },
    });
    productLines.value = data.content;
    totalItems.value = data.totalElements;
  } catch (error) {
    toast.value?.showToast('error', 'Không thể tải dữ liệu!');
    console.error('Fetch error:', error);
  }
};

const goToPage = async (page) => {
  currentPage.value = page;
  if (isSearching.value && searchKeyword.value.trim()) {
    await searchProductLine();
  } else {
    await fetchData();
  }
};

const searchProductLine = async () => {
  const keyword = searchKeyword.value.replace(/\s+/g, '').trim();
  if (!keyword) {
    isSearching.value = false;
    currentPage.value = 0;
    await fetchData();
    return;
  }
  isSearching.value = true;
  currentPage.value = 0;
  try {
    const { data } = await axios.get('http://localhost:8080/api/dong-san-pham/search', {
      params: { keyword, page: currentPage.value, size: pageSize.value },
    });
    productLines.value = data.content;
    totalItems.value = data.totalElements;
  } catch (error) {
    toast.value?.showToast('error', 'Lỗi tìm kiếm!');
  }
};

const resetSearch = () => {
  searchKeyword.value = '';
  isSearching.value = false;
  currentPage.value = 0;
  fetchData();
};

const checkDuplicate = async (field, value, excludeId = null) => {
  try {
    const { data } = await axios.get(`http://localhost:8080/api/dong-san-pham/exists/${field}`, {
      params: { [field]: value, excludeId },
    });
    return data;
  } catch (error) {
    toast.value?.showToast('error', `Lỗi kiểm tra ${field}!`);
    return false;
  }
};

const saveProductLine = async () => {
  const { ma, dongSanPham } = productLine.value;
  if (!ma || !dongSanPham) {
    toast.value?.showToast('error', 'Vui lòng nhập đầy đủ thông tin!');
    return;
  }
  if (await checkDuplicate('ma', ma)) {
    toast.value?.showToast('error', 'Mã dòng sản phẩm đã tồn tại!');
    return;
  }
  if (await checkDuplicate('dongSanPham', dongSanPham)) {
    toast.value?.showToast('error', 'Tên dòng sản phẩm đã tồn tại!');
    return;
  }
  try {
    const response = await axios.post('http://localhost:8080/api/dong-san-pham', productLine.value);
    toast.value?.showToast('success', 'Thêm mới thành công!');
    productLines.value.unshift(response.data);
    totalItems.value += 1;
    if (productLines.value.length > pageSize.value) {
      productLines.value.pop();
    }
    closeModal();
  } catch (error) {
    toast.value?.showToast('error', 'Lỗi khi lưu dữ liệu!');
  }
};

const updateProductLine = async () => {
  const { id, ma, dongSanPham } = productLine.value;

  // Kiểm tra dữ liệu đầu vào
  if (!ma || !dongSanPham) {
    toast.value?.showToast('error', 'Vui lòng nhập đầy đủ thông tin!');
    return;
  }

  // Lấy bản ghi gốc từ danh sách để so sánh
  const originalProduct = productLines.value.find((p) => p.id === id);
  const originalMa = originalProduct?.ma;
  const originalDongSanPham = originalProduct?.dongSanPham;

  // Kiểm tra trùng mã (chỉ khi mã thay đổi)
  if (ma !== originalMa && (await checkDuplicate('ma', ma, id))) {
    toast.value?.showToast('error', 'Mã dòng sản phẩm đã tồn tại!');
    return;
  }

  // Kiểm tra trùng tên (chỉ khi tên thay đổi)
  if (dongSanPham !== originalDongSanPham && (await checkDuplicate('dongSanPham', dongSanPham, id))) {
    toast.value?.showToast('error', 'Tên dòng sản phẩm đã tồn tại!');
    return;
  }

  try {
    const response = await axios.put(`http://localhost:8080/api/dong-san-pham/${id}`, productLine.value);
    toast.value?.showToast('success', 'Cập nhật thành công!');
    const index = productLines.value.findIndex((p) => p.id === id);
    if (index !== -1) {
      productLines.value[index] = response.data;
    }
    closeModal();
  } catch (error) {
    toast.value?.showToast('error', 'Lỗi khi cập nhật dữ liệu!');
  }
};

const deleteProductLine = async (id) => {
  try {
    await axios.delete(`http://localhost:8080/api/dong-san-pham/${id}`);
    toast.value?.showToast('success', 'Xóa thành công!');
    await fetchData();
  } catch (error) {
    toast.value?.showToast('error', 'Lỗi khi xóa!');
  }
};

const deleteSelectedProducts = async () => {
  try {
    await axios.delete('http://localhost:8080/api/dong-san-pham/bulk', {
      data: { ids: selectedProducts.value },
    });
    toast.value?.showToast('success', 'Xóa thành công!');
    selectedProducts.value = [];
    selectAll.value = false;
    await fetchData();
  } catch (error) {
    toast.value?.showToast('error', 'Lỗi khi xóa nhiều sản phẩm!');
  }
};

const openAddModal = () => {
  productLine.value = { id: null, ma: '', dongSanPham: '' };
  showAddModal.value = true;
};

const openEditModal = (product) => {
  productLine.value = { ...product };
  showEditModal.value = true;
};

const closeModal = () => {
  showAddModal.value = false;
  showEditModal.value = false;
};

const handleFormSubmit = (data) => {
  productLine.value = data;
  if (showAddModal.value) {
    confirmAction('Bạn có chắc chắn muốn thêm dòng sản phẩm này?', saveProductLine);
  } else if (showEditModal.value) {
    confirmAction('Bạn có chắc chắn muốn cập nhật dòng sản phẩm này?', updateProductLine);
  }
};

const confirmDelete = (id) => {
  confirmAction('Bạn có chắc chắn muốn xóa dòng sản phẩm này?', () => deleteProductLine(id));
};

const confirmDeleteSelected = () => {
  confirmAction(`Bạn có chắc chắn muốn xóa ${selectedProducts.value.length} dòng sản phẩm đã chọn?`, deleteSelectedProducts);
};

const confirmAction = (message, action) => {
  confirmMessage.value = message;
  confirmedAction.value = action;
  showConfirmModal.value = true;
};

const executeConfirmedAction = () => {
  if (confirmedAction.value) {
    confirmedAction.value();
  }
  closeConfirmModal();
};

const closeConfirmModal = () => {
  showConfirmModal.value = false;
  confirmedAction.value = null;
};

const toggleSelectAll = () => {
  selectedProducts.value = selectAll.value ? productLines.value.map((p) => p.id) : [];
};

onMounted(fetchData);
</script>

<style scoped>
.fixed.inset-0 {
  overflow-y: auto;
}
</style>