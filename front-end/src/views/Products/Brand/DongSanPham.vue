<template>
  <div class="container mx-auto p-4">
    <ToastNotification ref="toast" />

    <h4 class="text-xl text-gray-600 mb-6">Quản lý Dòng Sản Phẩm</h4>

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

    <!-- Form Section -->
    <div class="bg-gray-50 p-6 rounded-lg shadow-md mb-6">
      <h5 class="text-lg font-semibold mb-4">
        {{ editing ? 'Cập nhật Dòng Sản Phẩm' : 'Thêm Dòng Sản Phẩm' }}
      </h5>
      <form @submit.prevent="saveProductLine" class="space-y-4">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <input
            v-model.trim="productLine.ma"
            type="text"
            placeholder="Mã dòng sản phẩm"
            class="border rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-green-500"
            required
          />
          <input
            v-model.trim="productLine.dongSanPham"
            type="text"
            placeholder="Tên dòng sản phẩm"
            class="border rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-green-500"
            required
          />
        </div>
        <div class="flex justify-end gap-3">
          <button
            type="submit"
            class="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition"
          >
            {{ editing ? 'Cập nhật' : 'Thêm mới' }}
          </button>
          <button
            v-if="editing"
            @click="cancelEdit"
            type="button"
            class="bg-gray-400 text-white px-4 py-2 rounded-lg hover:bg-gray-500 transition"
          >
            Hủy
          </button>
        </div>
      </form>
    </div>

    <!-- Bulk Delete Button -->
    <div v-if="selectedProducts.length" class="mb-6 flex justify-end">
      <button
        @click="deleteSelectedProducts"
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
              @click="editProductLine(product)"
              class="text-blue-600 hover:text-blue-800 transition"
            >
              <i class="fa-solid fa-edit"></i>
            </button>
            <button
              @click="deleteProductLine(product.id)"
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

    <Pagination
      :current-page="currentPage"
      :total-pages="totalPages"
      @page-changed="goToPage"
      class="mt-6"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import axios from 'axios'
import ToastNotification from '@/components/ToastNotification.vue'
import Pagination from '@/components/Pagination.vue'

const toast = ref(null)
const productLines = ref([])
const productLine = ref({ id: null, ma: '', dongSanPham: '' })
const searchKeyword = ref('')
const editing = ref(false)
const currentPage = ref(0)
const pageSize = ref(5)
const totalItems = ref(0)
const selectedProducts = ref([])
const selectAll = ref(false)
const isSearching = ref(false)

const totalPages = computed(() => Math.ceil(totalItems.value / pageSize.value))

watch(searchKeyword, (newValue) => {
  isSearching.value = !!newValue.trim()
})

const fetchData = async () => {
  try {
    const { data } = await axios.get('http://localhost:8080/api/dong-san-pham', {
      params: { page: currentPage.value, size: pageSize.value }
    })
    productLines.value = data.content
    totalItems.value = data.totalElements
  } catch (error) {
    toast.value?.showToast('error', 'Không thể tải dữ liệu!')
    console.error('Fetch error:', error)
  }
}

const goToPage = async (page) => {
  currentPage.value = page
  if (isSearching.value && searchKeyword.value.trim()) {
    await searchProductLine()
  } else {
    await fetchData()
  }
}

const searchProductLine = async () => {
  const keyword = searchKeyword.value.replace(/\s+/g, '').trim()
  if (!keyword) {
    isSearching.value = false
    currentPage.value = 0 // Reset về trang 1 khi không có từ khóa
    await fetchData()
    return
  }
  isSearching.value = true
  currentPage.value = 0 // Reset về trang 1 khi tìm kiếm mới
  try {
    const { data } = await axios.get('http://localhost:8080/api/dong-san-pham/search', {
      params: { keyword, page: currentPage.value, size: pageSize.value }
    })
    productLines.value = data.content
    totalItems.value = data.totalElements
  } catch (error) {
    toast.value?.showToast('error', 'Lỗi tìm kiếm!')
  }
}

const resetSearch = () => {
  searchKeyword.value = ''
  isSearching.value = false
  currentPage.value = 0
  fetchData()
}

const checkDuplicate = async (field, value) => {
  try {
    const { data } = await axios.get(`http://localhost:8080/api/dong-san-pham/exists/${field}`, {
      params: { [field]: value }
    })
    return data
  } catch (error) {
    toast.value?.showToast('error', `Lỗi kiểm tra ${field}!`)
    return false
  }
}

const saveProductLine = async () => {
  const { ma, dongSanPham } = productLine.value
  if (!ma || !dongSanPham) {
    toast.value?.showToast('error', 'Vui lòng nhập đầy đủ thông tin!')
    return
  }

  if (!editing.value) {
    if (await checkDuplicate('ma', ma)) {
      toast.value?.showToast('error', 'Mã dòng sản phẩm đã tồn tại!')
      return
    }
    if (await checkDuplicate('dongSanPham', dongSanPham)) {
      toast.value?.showToast('error', 'Tên dòng sản phẩm đã tồn tại!')
      return
    }
  }

  try {
    let response
    if (editing.value) {
      response = await axios.put(`http://localhost:8080/api/dong-san-pham/${productLine.value.id}`, productLine.value)
      const index = productLines.value.findIndex(p => p.id === productLine.value.id)
      if (index !== -1) {
        productLines.value[index] = response.data
      }
    } else {
      response = await axios.post('http://localhost:8080/api/dong-san-pham', productLine.value)
      productLines.value.unshift(response.data)
      totalItems.value += 1
      if (productLines.value.length > pageSize.value) {
        productLines.value.pop()
      }
    }

    toast.value?.showToast('success', editing.value ? 'Cập nhật thành công!' : 'Thêm mới thành công!')
    resetForm()
  } catch (error) {
    toast.value?.showToast('error', 'Lỗi khi lưu dữ liệu!')
  }
}

const editProductLine = (product) => {
  productLine.value = { ...product }
  editing.value = true
}

const cancelEdit = () => {
  resetForm()
  toast.value?.showToast('info', 'Đã hủy chỉnh sửa')
}

const deleteProductLine = async (id) => {
  if (!confirm('Bạn có chắc chắn muốn xóa dòng sản phẩm này?')) return
  try {
    await axios.delete(`http://localhost:8080/api/dong-san-pham/${id}`)
    toast.value?.showToast('success', 'Xóa thành công!')
    await fetchData()
  } catch (error) {
    toast.value?.showToast('error', 'Lỗi khi xóa!')
  }
}

const deleteSelectedProducts = async () => {
  if (!selectedProducts.value.length) {
    toast.value?.showToast('error', 'Vui lòng chọn ít nhất một dòng sản phẩm!')
    return
  }
  if (!confirm(`Xác nhận xóa ${selectedProducts.value.length} dòng sản phẩm?`)) return

  try {
    await axios.delete('http://localhost:8080/api/dong-san-pham/bulk', {
      data: { ids: selectedProducts.value }
    })
    toast.value?.showToast('success', 'Xóa thành công!')
    selectedProducts.value = []
    selectAll.value = false
    await fetchData()
  } catch (error) {
    toast.value?.showToast('error', 'Lỗi khi xóa nhiều sản phẩm!')
  }
}

const toggleSelectAll = () => {
  selectedProducts.value = selectAll.value
    ? productLines.value.map(p => p.id)
    : []
}

const resetForm = () => {
  productLine.value = { id: null, ma: '', dongSanPham: '' }
  editing.value = false
}

onMounted(fetchData)
</script>