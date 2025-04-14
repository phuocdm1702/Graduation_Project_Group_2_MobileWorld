<template>
  <div class="overflow-x-auto bg-white shadow-lg rounded-lg p-4">
    <table class="w-full min-w-max table-auto border-collapse">
      <thead>
      <tr class="bg-gray-100 text-gray-700 text-sm font-semibold">
        <th v-for="column in columns" :key="column.key" class="th-cell">
          {{ column.label }}
        </th>
      </tr>
      </thead>
      <tbody>
      <tr v-if="data.length === 0">
        <td :colspan="columns.length" class="td-cell text-center">
          Không có dữ liệu
        </td>
      </tr>
      <tr v-else v-for="(item, index) in data" :key="item.id" class="text-gray-700 border-b hover:bg-gray-50">
        <td v-for="column in columns" :key="column.key" class="td-cell">
          <template v-if="column.cellSlot === 'actionsSlot'">
            <slot name="actionsSlot" :item="item">
              <div class="flex items-center justify-center space-x-2">
                <button @click="editItem(item)" class="bg-blue-500 text-white px-3 py-1 rounded-md hover:bg-blue-600 transition">
                  <i class="fa-solid fa-eye"></i>
                </button>
                <button @click="deleteItem(item)" class="bg-red-500 text-white px-3 py-1 rounded-md hover:bg-red-600 transition">
                  <i class="fa-solid fa-trash"></i>
                </button>
              </div>
            </slot>
          </template>
          <template v-else-if="column.cellSlot === 'trangThaiPGG'">
            <span :class="item.displayStatus?.cssClass">
              {{ item.displayStatus?.text || 'N/A' }}
            </span>
          </template>
          <template v-else-if="column.key === 'trangThai'">
            <span
              :class="{
                'bg-gray-200 text-red-500': item.trangThai,
                'bg-gray-200 text-green-500': !item.trangThai
              }"
              class="inline-block px-3 py-1 border rounded-full text-sm font-semibold"
            >
              {{ item.trangThai ? 'Không hoạt động' : 'Hoạt động' }}
            </span>
          </template>
          <span v-else-if="column.formatter" v-html="column.formatter(getNestedValue(item, column.key), item, index)"></span>
          <span v-else>{{ getNestedValue(item, column.key) || 'N/A' }}</span>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import ToastNotification from '@/components/ToastNotification.vue';

defineProps({
  data: { type: Array, required: true },
  columns: { type: Array, required: true },
  getNestedValue: { type: Function, required: true },
});

const emit = defineEmits(['editItem', 'toggleStatus']);
const router = useRouter();

const editItem = (item) => {
  router.push({ name: 'SanPhamChiTiet', params: { id: item.id } });
};

const deleteItem = async (item) => {
  try {
    await axios.delete(`http://localhost:8080/san-pham/${item.id}`);
    emit('deleteItem', item); // Emit sự kiện để thông báo xóa
    // Giả sử bạn có component ToastNotification
    ToastNotification.value?.showToast('success', 'Xóa sản phẩm thành công!');
  } catch (error) {
    console.error('Delete error:', error);
    ToastNotification.value?.showToast('error', 'Xóa sản phẩm thất bại!');
  }
};
</script>

<style scoped>
.th-cell {
  @apply px-4 py-3 border-b text-center font-semibold; /* Căn giữa chữ và thêm font-semibold */
}

.td-cell {
  @apply px-4 py-2 text-sm text-center; /* Căn giữa chữ */
}

.badge-pending {
  background-color: #ffc107;
  color: white;
  padding: 5px 10px;
  border-radius: 15px;
  font-size: 12px;
  font-weight: 500;
  text-align: center;
}
.badge-active {
  background-color: #28a745;
  color: white;
  padding: 5px 10px;
  border-radius: 15px;
  font-size: 12px;
  font-weight: 500;
  text-align: center;
}
.badge-inactive {
  background-color: #dc3545;
  color: white;
  padding: 5px 10px;
  border-radius: 15px;
  font-size: 12px;
  font-weight: 500;
  text-align: center;
}
</style>