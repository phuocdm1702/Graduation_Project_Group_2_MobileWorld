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
          <span v-if="column.key === 'trangThai'">
            <!-- Thay checkbox bằng chữ có viền và màu -->
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
import { defineProps } from 'vue';

defineProps({
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
});
</script>

<style scoped>
.th-cell {
  @apply px-4 py-3 text-left border-b;
}

.td-cell {
  @apply px-4 py-2 text-sm;
}
</style>