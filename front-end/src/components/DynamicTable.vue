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
    <label class="switch">
      <input type="checkbox" v-model="item.trangThai">
      <div class="slider"></div>
      <div class="slider-card">
        <div class="slider-card-face slider-card-front"></div>
        <div class="slider-card-face slider-card-back"></div>
      </div>
    </label>
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
import {defineProps} from 'vue';

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

.switch {
  --circle-dim: 1.4em;
  font-size: 17px;
  position: relative;
  display: inline-block;
  width: 3.5em;
  height: 2em;
}

/* Hide default HTML checkbox */
.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

/* The slider */
.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #f5aeae;
  transition: .4s;
  border-radius: 30px;
}

.slider-card {
  position: absolute;
  content: "";
  height: var(--circle-dim);
  width: var(--circle-dim);
  border-radius: 20px;
  left: 0.3em;
  bottom: 0.3em;
  transition: .4s;
  pointer-events: none;
}

.slider-card-face {
  position: absolute;
  inset: 0;
  backface-visibility: hidden;
  perspective: 1000px;
  border-radius: 50%;
  transition: .4s transform;
}

.slider-card-front {
  background-color: #DC3535;
}

.slider-card-back {
  background-color: #379237;
  transform: rotateY(180deg);
}

input:checked ~ .slider-card .slider-card-back {
  transform: rotateY(0);
}

input:checked ~ .slider-card .slider-card-front {
  transform: rotateY(-180deg);
}

input:checked ~ .slider-card {
  transform: translateX(1.5em);
}

input:checked ~ .slider {
  background-color: #9ed99c;
}
</style>