<template>
  <div class="container mx-auto p-4">
    <ToastNotification ref="toast" />

    <h2 class="text-xl text-gray-600 mb-6">Quản lý Imel</h2>

    <!-- Search Section -->
    <div class="flex gap-3 mb-6">
      <input
        v-model.trim="searchKeyword"
        type="text"
        placeholder="Tìm kiếm theo mã hoặc tên..."
        class="flex-1 border rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
      />
      <button
        @click="searchImel"
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
        {{ editing ? 'Cập nhật Imel' : 'Thêm Imel' }}
      </h5>
      <form @submit.prevent="saveImel" class="space-y-4">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <input
            v-model.trim="imel.ma"
            type="text"
            placeholder="Mã Imel"
            class="border rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-green-500"
            required
          />
          <input
            v-model.trim="imel.imel"
            type="text"
            placeholder="Tên Imel"
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
    <div v-if="selectedImels.length" class="mb-6 flex justify-end">
      <button
        @click="deleteSelectedImels"
        class="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 transition"
      >
        Xóa {{ selectedImels.length }} Imel đã chọn
      </button>
    </div>

    <!-- Imel Table -->
    <div class="overflow-x-auto shadow-md rounded-lg">
      <table class="w-full text-sm text-gray-500">
        <thead class="bg-blue-100 text-blue-700 uppercase">
        <tr>
          <th class="px-6 py-3 text-center">ID</th>
          <th class="px-6 py-3 text-center">Mã</th>
          <th class="px-6 py-3 text-center">Imel</th>
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
          v-for="imel in imels"
          :key="imel.id"
          class="bg-white border-b hover:bg-gray-50 transition"
        >
          <td class="px-6 py-4 text-center">{{ imel.id }}</td>
          <td class="px-6 py-4 text-center">{{ imel.ma }}</td>
          <td class="px-6 py-4 text-center">{{ imel.imel }}</td>
          <td class="px-6 py-4 text-center space-x-4">
            <button
              @click="editImel(imel)"
              class="text-blue-600 hover:text-blue-800 transition"
            >
              <i class="fa-solid fa-edit"></i>
            </button>
            <button
              @click="deleteImel(imel.id)"
              class="text-red-600 hover:text-red-800 transition"
            >
              <i class="fa-solid fa-trash"></i>
            </button>
          </td>
          <td class="px-6 py-4 text-center">
            <input
              type="checkbox"
              v-model="selectedImels"
              :value="imel.id"
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
const imels = ref([])
const imel = ref({ id: null, ma: '', imel: '' })
const searchKeyword = ref('')
const editing = ref(false)
const currentPage = ref(0)
const pageSize = ref(5)
const totalItems = ref(0)
const selectedImels = ref([])
const selectAll = ref(false)
const isSearching = ref(false)

const totalPages = computed(() => Math.ceil(totalItems.value / pageSize.value))

watch(searchKeyword, (newValue) => {
  isSearching.value = !!newValue.trim()
})

const fetchData = async () => {
  try {
    const { data } = await axios.get('http://localhost:8080/api/imel', {
      params: { page: currentPage.value, size: pageSize.value }
    })
    imels.value = data.content
    totalItems.value = data.totalElements
  } catch (error) {
    toast.value?.showToast('error', 'Không thể tải dữ liệu!')
    console.error('Fetch error:', error)
  }
}

const goToPage = async (page) => {
  currentPage.value = page
  if (isSearching.value && searchKeyword.value.trim()) {
    await searchImel()
  } else {
    await fetchData()
  }
}

const searchImel = async () => {
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
    const { data } = await axios.get('http://localhost:8080/api/imel/search', {
      params: { keyword, page: currentPage.value, size: pageSize.value }
    })
    imels.value = data.content
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
    const { data } = await axios.get(`http://localhost:8080/api/imel/exists/${field}`, {
      params: { [field]: value }
    })
    return data
  } catch (error) {
    toast.value?.showToast('error', `Lỗi kiểm tra ${field}!`)
    return false
  }
}

const saveImel = async () => {
  const { ma, imel } = imel.value
  if (!ma || !imel) {
    toast.value?.showToast('error', 'Vui lòng nhập đầy đủ thông tin!')
    return
  }

  if (!editing.value) {
    if (await checkDuplicate('ma', ma)) {
      toast.value?.showToast('error', 'Mã Imel đã tồn tại!')
      return
    }
    if (await checkDuplicate('imel', imel)) {
      toast.value?.showToast('error', 'Tên Imel đã tồn tại!')
      return
    }
  }

  try {
    let response
    if (editing.value) {
      response = await axios.put(`http://localhost:8080/api/imel/${imel.value.id}`, imel.value)
      const index = imels.value.findIndex(p => p.id === imel.value.id)
      if (index !== -1) {
        imels.value[index] = response.data
      }
    } else {
      response = await axios.post('http://localhost:8080/api/imel', imel.value)
      imels.value.unshift(response.data)
      totalItems.value += 1
      if (imels.value.length > pageSize.value) {
        imels.value.pop()
      }
    }

    toast.value?.showToast('success', editing.value ? 'Cập nhật thành công!' : 'Thêm mới thành công!')
    resetForm()
  } catch (error) {
    toast.value?.showToast('error', 'Lỗi khi lưu dữ liệu!')
  }
}

const editImel = (imel) => {
  imel.value = { ...imel }
  editing.value = true
}

const cancelEdit = () => {
  resetForm()
  toast.value?.showToast('info', 'Đã hủy chỉnh sửa')
}

const deleteImel = async (id) => {
  if (!confirm('Bạn có chắc chắn muốn xóa Imel này?')) return
  try {
    await axios.delete(`http://localhost:8080/api/imel/${id}`)
    toast.value?.showToast('success', 'Xóa thành công!')
    await fetchData()
  } catch (error) {
    toast.value?.showToast('error', 'Lỗi khi xóa!')
  }
}

const deleteSelectedImels = async () => {
  if (!selectedImels.value.length) {
    toast.value?.showToast('error', 'Vui lòng chọn ít nhất một Imel!')
    return
  }
  if (!confirm(`Xác nhận xóa ${selectedImels.value.length} Imel?`)) return

  try {
    await axios.delete('http://localhost:8080/api/imel/bulk', {
      data: { ids: selectedImels.value }
    })
    toast.value?.showToast('success', 'Xóa thành công!')
    selectedImels.value = []
    selectAll.value = false
    await fetchData()
  } catch (error) {
    toast.value?.showToast('error', 'Lỗi khi xóa nhiều Imel!')
  }
}

const toggleSelectAll = () => {
  selectedImels.value = selectAll.value
    ? imels.value.map(p => p.id)
    : []
}

const resetForm = () => {
  imel.value = { id: null, ma: '', imel: '' }
  editing.value = false
}

onMounted(fetchData)
</script>