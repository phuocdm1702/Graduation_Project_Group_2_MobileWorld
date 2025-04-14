<!-- Template -->
<template>
  <table class="w-full">
    <thead>
    <tr>
      <th v-for="column in columns" :key="column.key" class="th-cell">
        {{ column.title }}
      </th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="(item, index) in data" :key="item.id">
      <td v-for="column in columns" :key="column.key" class="td-cell">
        <template v-if="column.cellSlot === 'actionsSlot'">
          <slot name="actionsSlot" :item="item">
            <div class="flex items-center justify-centesspace-x-2">
              <button @click="editItem(item)" class="bg-blue-500 text-white px-3 py-1 rounded-md hover:bg-blue-600 transition">
                <i class="fa-solid fa-edit"></i>
              </button>
              <button @click="deleteItem(item)" class="bg-red-500 text-white px-3 py-1 rounded-md hover:bg-red-600 transition">
                <i class="fa-solid fa-trash"></i>
              </button>
              <ToggleSwitch
                v-if="!item.displayStatus.isHiddenToggle"
                :checked="!item.trangThai"
                :value="!item.trangThai"
                @change="toggleStatus(item)"
                :id="item.id"
              />
            </div>
          </slot>
        </template>
        <template v-else-if="column.key === 'trangThai'">
            <span class="inline-block px-2 py-1 text-xs font-semibold rounded-full"
                  :class="{
                    'bg-green-100 text-green-800': item.trangThai,
                    'bg-red-100 text-red-800': !item.trangThai
                  }">
              {{ item.trangThai ? 'Hoạt động' : 'Không hoạt động' }}
            </span>
        </template>
        <span v-else-if="column.formatter"
              v-html="column.formatter(getNestedValue(item, column.key), item, index)"></span>
        <span v-else>{{ getNestedValue(item, column.key) || 'N/A' }}</span>
      </td>
    </tr>
    </tbody>
  </table>
</template>

<!-- Script -->
<script setup>
import { defineProps, defineEmits } from 'vue';
import { useRouter } from 'vue-router';
import ToggleSwitch from '@/components/ToggleSwitch.vue';

defineProps({
  data: { type: Array, required: true },
  columns: { type: Array, required: true }
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

<!-- Style -->
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
}
</style>