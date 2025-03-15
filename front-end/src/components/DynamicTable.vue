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
              <!-- Nút Sửa -->
              <button
                @click="editItem(item)"
                class="px-2 py-1 bg-blue-500 text-white rounded hover:bg-blue-600"
              >
                Sửa
              </button>
              <!-- ToggleSwitch -->
              <label class="switch ml-2">
                <ToggleSwitch
                  class="ml-2"
                  :checked="!item.trangThai"
                  @change="toggleStatus(item)"
                />
                <div class="slider"></div>
                <div class="slider-card">
                  <div class="slider-card-face slider-card-front"></div>
                  <div class="slider-card-face slider-card-back"></div>
                </div>
              </label>
            </slot>
          </template>
          <template v-else-if="column.key === 'trangThai'">
            <!-- Thay checkbox bằng chữ có viền và màu -->
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
import {useRouter} from "vue-router";
import ToggleSwitch from "@/components/ToggleSwitch.vue";

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

const emit = defineEmits(['editItem', 'toggleStatus']);

const router = useRouter();

const editItem = (item) => {
  router.push({ name: 'FormUpdatePgg', params: { id: item.id } });
};

const toggleStatus = (item) => {
  emit('toggleStatus', item);
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