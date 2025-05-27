<template>
  <div class="toggle-switch">
    <input class="toggle-input" :id="uniqueId" type="checkbox"
           :checked="checked"
           @change="$emit('change', $event.target.checked)">
    <label class="toggle-label" :for="uniqueId"></label>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, computed } from 'vue';

const props = defineProps({
  checked: { type: Boolean, default: false }, // true = Riêng tư/Hoạt động (xanh), false = Công khai/Không hoạt động (đỏ)
  id: { type: [String, Number], default: '' }
});

defineEmits(['change']);

const uniqueId = computed(() => `toggle-${props.id}`);
</script>

<style scoped>
.toggle-switch {
  position: relative;
  display: inline-block;
  width: 40px;
  height: 24px;
  margin: 10px;
}

.toggle-switch .toggle-input {
  display: none;
}

.toggle-switch .toggle-label {
  position: absolute;
  top: 0;
  left: 0;
  width: 40px;
  height: 24px;
  border-radius: 34px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.toggle-switch .toggle-label::before {
  content: "";
  position: absolute;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  top: 2px;
  left: 2px;
  background-color: #fff;
  box-shadow: 0px 2px 5px 0px rgba(0, 0, 0, 0.3);
  transition: transform 0.3s;
}

.toggle-switch .toggle-input:checked + .toggle-label {
  background-color: #f97316; /* Màu xanh khi Riêng tư/Hoạt động (true) */
}

.toggle-switch .toggle-input:checked + .toggle-label::before {
  transform: translateX(16px);
}

.toggle-switch.light .toggle-label {
  background-color: #BEBEBE;
}

.toggle-switch.light .toggle-input:checked + .toggle-label {
  background-color: #9B9B9B;
}

.toggle-switch.light .toggle-input:checked + .toggle-label::before {
  transform: translateX(6px);
}

.toggle-switch.dark .toggle-label {
  background-color: #4B4B4B;
}

.toggle-switch.dark .toggle-input:checked + .toggle-label {
  background-color: #717171;
}

.toggle-switch.dark .toggle-input:checked + .toggle-label::before {
  transform: translateX(16px);
}
</style>