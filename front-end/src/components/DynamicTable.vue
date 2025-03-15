<template>
  <div class="overflow-x-auto bg-white shadow-lg rounded-lg p-4">
    <table class="w-full min-w-max table-auto border-collapse">
      <thead>
      <tr class="bg-gray-100 text-gray-700 text-sm font-semibold">
        <th v-for="column in columns" :key="column.key" class="th-cell">
          <slot :name="`header-${column.key}`" :column="column">
            <span>{{ column.label }}</span>
          </slot>
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
          <template v-if="column.key === 'select'">
            <slot :name="`cell-${column.key}`" :item="item" :index="index">
              <input
                type="checkbox"
                class="w-4 h-4 rounded"
                :checked="isSelected(item.id)"
                @change="toggleSelect(item.id)"
              >
            </slot>
          </template>
          <span v-else-if="column.key === 'trangThai'">
            <span
              :class="{
                'bg-gray-200 text-red-500': item.trangThai,
                'bg-gray-200 text-green-500': !item.trangThai
              }"
              class="inline-block px-3 py-1 border rounded-full text-sm font-semibold"
            >
              {{ item.trangThai ? 'Không hoạt động' : ' Hoạt động' }}
            </span>
          </span>
          <span v-else-if="column.formatter"
                v-html="column.formatter(getNestedValue(item, column.key), item, index)"></span>
          <span v-else>{{ getNestedValue(item, column.key) || 'N/A' }}</span>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';

const props = defineProps({
  data: {
    type: Array,
    required: true,
  },
  columns: {
    type: Array,
    required: true,
  },
  getNestedValue: {
    type: Function,
    required: true,
  },
  selectedProducts: {
    type: Array,
    required: false, // Không bắt buộc
    default: () => [], // Giá trị mặc định là mảng rỗng
  },
});

const emit = defineEmits(['toggle-select']);

const isSelected = (id) => {
  return props.selectedProducts.includes(id);
};

const toggleSelect = (id) => {
  emit('toggle-select', id);
};
</script>

<style scoped>
.th-cell {
  @apply px-4 py-3 text-left border-b;
}

.td-cell {
  @apply px-4 py-2 text-sm;
}
</style>