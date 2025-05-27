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
                <button @click="editItem(item)" class="text-orange-500 hover:text-orange-500 transition">
                  <i class="fa-solid fa-edit"></i>
                </button>
                <ToggleSwitch
                  v-if="!item.displayStatus.isHiddenToggle"
                  :checked="item.trangThai"
                  :value="item.trangThai"
                  @change="toggleStatus(item)"
                  :id="item.id"
                />
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
import { useRouter } from 'vue-router';
import ToggleSwitch from '@/components/ToggleSwitch.vue';

defineProps({
  data: { type: Array, required: true },
  columns: { type: Array, required: true },
  getNestedValue: { type: Function, required: true },
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
  @apply px-4 py-3 border-b text-center font-semibold;
}

.td-cell {
  @apply px-4 py-2 text-sm text-center;
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