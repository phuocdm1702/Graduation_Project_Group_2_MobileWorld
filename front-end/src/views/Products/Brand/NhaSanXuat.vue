<template>
  <div class="container mx-auto p-4">
    <ToastNotification ref="toast" />

    <h2 class="text-xl text-gray-600 mb-6">Quản lý Nhà Sản Xuất</h2>

    <!-- Search Section -->
    <div class="flex gap-3 mb-6">
      <input
        v-model.trim="searchKeyword"
        type="text"
        placeholder="Tìm kiếm theo mã hoặc tên..."
        class="flex-1 border rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
      />
      <button
        @click="searchManufacturer"
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
        {{ editing ? 'Cập nhật Nhà Sản Xuất' : 'Thêm Nhà Sản Xuất' }}
      </h5>
      <form @submit.prevent="saveManufacturer" class="space-y-4">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <input
            v-model.trim="manufacturer.ma"
            type="text"
            placeholder="Mã nhà sản xuất"
            class="border rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-green-500"
            required
          />
          <input
            v-model.trim="manufacturer.nhaSanXuat"
            type="text"
            placeholder="Tên nhà sản xuất"
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
    <div v-if="selectedManufacturers.length" class="mb-6 flex justify-end">
      <button
        @click="deleteSelectedManufacturers"
        class="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 transition"
      >
        Xóa {{ selectedManufacturers.length }} nhà sản xuất đã chọn
      </button>
    </div>

    <!-- Manufacturer Table -->
    <div class="overflow-x-auto shadow-md rounded-lg">
      <table class="w-full text-sm text-gray-500">
        <thead class="bg-blue-100 text-blue-700 uppercase">
        <tr>
          <th class="px-6 py-3 text-center">ID</th>
          <th class="px-6 py-3 text-center">Mã</th>
          <th class="px-6 py-3 text-center">Tên Nhà Sản Xuất</th>
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
          v-for="manufacturer in manufacturers"
          :key="manufacturer.id"
          class="bg-white border-b hover:bg-gray-50 transition"
        >
          <td class="px-6 py-4 text-center">{{ manufacturer.id }}</td>
          <td class="px-6 py-4 text-center">{{ manufacturer.ma }}</td>
          <td class="px-6 py-4 text-center">{{ manufacturer.nhaSanXuat }}</td>
          <td class="px-6 py-4 text-center space-x-4">
            <button
              @click="editManufacturer(manufacturer)"
              class="text-blue-600 hover:text-blue-800 transition"
            >
              <i class="fa-solid fa-edit"></i>
            </button>
            <button
              @click="deleteManufacturer(manufacturer.id)"
              class="text-red-600 hover:text-red-800 transition"
            >
              <i class="fa-solid fa-trash"></i>
            </button>
          </td>
          <td class="px-6 py-4 text-center">
            <input
              type="checkbox"
              v-model="selectedManufacturers"
              :value="manufacturer.id"
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
const manufacturers = ref([])
const manufacturer = ref({ id: null, ma: '', nhaSanXuat: '' })
const searchKeyword = ref('')
const editing = ref(false)
const currentPage = ref(0)
const pageSize = ref(5)
const totalItems = ref(0)
const selectedManufacturers = ref([])
const selectAll = ref(false)
const isSearching = ref(false)

const totalPages = computed(() => Math.ceil(totalItems.value / pageSize.value))

watch(searchKeyword, (newValue) => {
  isSearching.value = !!newValue.trim()
})

const fetchData = async () => {
  try {
    const { data } = await axios.get('http://localhost:8080/api/nha-san-xuat', {
      params: { page: currentPage.value, size: pageSize.value }
    })
    manufacturers.value = data.content
    totalItems.value = data.totalElements
  } catch (error) {
    toast.value?.showToast('error', 'Không thể tải dữ liệu!')
    console.error('Fetch error:', error)
  }
}

const goToPage = async (page) => {
  currentPage.value = page
  if (isSearching.value && searchKeyword.value.trim()) {
    await searchManufacturer()
  } else {
    await fetchData()
  }
}

const searchManufacturer = async () => {
  const keyword = searchKeyword.value.replace(/\s+/g, '').trim()
  if (!keyword) {
    isSearching.value = false
    currentPage.value = 0
    await fetchData()
    return
  }
  isSearching.value = true
  currentPage.value = 0
  try {
    const { data } = await axios.get('http://localhost:8080/api/nha-san-xuat/search', {
      params: { keyword, page: currentPage.value, size: pageSize.value }
    })
    manufacturers.value = data.content
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
    const { data } = await axios.get(`http://localhost:8080/api/nha-san-xuat/exists/${field}`, {
      params: { [field]: value }
    })
    return data
  } catch (error) {
    toast.value?.showToast('error', `Lỗi kiểm tra ${field}!`)
    return false
  }
}

const saveManufacturer = async () => {
  const { ma, nhaSanXuat } = manufacturer.value
  if (!ma || !nhaSanXuat) {
    toast.value?.showToast('error', 'Vui lòng nhập đầy đủ thông tin!')
    return
  }

  if (!editing.value) {
    if (await checkDuplicate('ma', ma)) {
      toast.value?.showToast('error', 'Mã nhà sản xuất đã tồn tại!')
      return
    }
    if (await checkDuplicate('nhaSanXuat', nhaSanXuat)) {
      toast.value?.showToast('error', 'Tên nhà sản xuất đã tồn tại!')
      return
    }
  }

  try {
    let response
    if (editing.value) {
      response = await axios.put(`http://localhost:8080/api/nha-san-xuat/${manufacturer.value.id}`, manufacturer.value)
      const index = manufacturers.value.findIndex(p => p.id === manufacturer.value.id)
      if (index !== -1) {
        manufacturers.value[index] = response.data
      }
    } else {
      response = await axios.post('http://localhost:8080/api/nha-san-xuat', manufacturer.value)
      manufacturers.value.unshift(response.data)
      totalItems.value += 1
      if (manufacturers.value.length > pageSize.value) {
        manufacturers.value.pop()
      }
    }

    toast.value?.showToast('success', editing.value ? 'Cập nhật thành công!' : 'Thêm mới thành công!')
    resetForm()
  } catch (error) {
    toast.value?.showToast('error', 'Lỗi khi lưu dữ liệu!')
  }
}

const editManufacturer = (manufacturer) => {
  manufacturer.value = { ...manufacturer }
  editing.value = true
}

const cancelEdit = () => {
  resetForm()
  toast.value?.showToast('info', 'Đã hủy chỉnh sửa')
}

const deleteManufacturer = async (id) => {
  if (!confirm('Bạn có chắc chắn muốn xóa nhà sản xuất này?')) return
  try {
    await axios.delete(`http://localhost:8080/api/nha-san-xuat/${id}`)
    toast.value?.showToast('success', 'Xóa thành công!')
    await fetchData()
  } catch (error) {
    toast.value?.showToast('error', 'Lỗi khi xóa!')
  }
}

const deleteSelectedManufacturers = async () => {
  if (!selectedManufacturers.value.length) {
    toast.value?.showToast('error', 'Vui lòng chọn ít nhất một nhà sản xuất!')
    return
  }
  if (!confirm(`Xác nhận xóa ${selectedManufacturers.value.length} nhà sản xuất?`)) return

  try {
    await axios.delete('http://localhost:8080/api/nha-san-xuat/bulk', {
      data: { ids: selectedManufacturers.value }
    })
    toast.value?.showToast('success', 'Xóa thành công!')
    selectedManufacturers.value = []
    selectAll.value = false
    await fetchData()
  } catch (error) {
    toast.value?.showToast('error', 'Lỗi khi xóa nhiều nhà sản xuất!')
  }
}

const toggleSelectAll = () => {
  selectedManufacturers.value = selectAll.value
    ? manufacturers.value.map(p => p.id)
    : []
}

const resetForm = () => {
  manufacturer.value = { id: null, ma: '', nhaSanXuat: '' }
  editing.value = false
}

onMounted(fetchData)
</script>