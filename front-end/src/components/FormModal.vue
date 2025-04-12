<!-- src/components/FormModal.vue -->
<template>
  <div v-if="show" class="fixed inset-0 flex items-center justify-center z-50">
    <div class="fixed inset-0 bg-black bg-opacity-50 z-40" @click="close"></div>
    <div class="bg-white p-6 rounded-xl shadow-lg border border-gray-100 max-w-md w-full z-50">
      <h2 class="text-2xl font-semibold text-gray-800 mb-6 flex items-center">
        <i :class="`fa-solid ${iconClass} ${iconColor} mr-2`"></i>
        Thêm {{ entityName }}
      </h2>
      <form @submit.prevent="submitForm" class="space-y-6">
        <div class="grid grid-cols-1 gap-6">
          <slot :entity-data="localEntityData" />
        </div>
        <div class="flex justify-end gap-4">
          <button
            type="button"
            @click="close"
            class="bg-gray-200 text-gray-700 px-5 py-2 rounded-lg hover:bg-gray-300 transition-all duration-200 font-medium"
          >
            Hủy
          </button>
          <button
            type="submit"
            class="bg-orange-500 hover:bg-orange-600 text-white px-5 py-2 rounded-lg transition-all duration-200 font-medium flex items-center"
          >
            <i class="fa-solid fa-save mr-2"></i> Thêm mới
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';

const props = defineProps({
  show: Boolean,
  entityName: { type: String, required: true },
  entityData: { type: Object, default: () => ({}) },
  iconClass: { type: String, default: 'fa-plus-circle' },
  iconColor: { type: String, default: 'text-orange-500' },
});

const emit = defineEmits(['submit', 'close']);

const localEntityData = ref({ ...props.entityData });

watch(
  () => props.entityData,
  (newValue) => {
    localEntityData.value = { ...newValue };
  },
  { immediate: true }
);

const close = () => {
  emit('close');
};

const submitForm = () => {
  let requiredFields = [];
  switch (props.entityName) {
    case 'khách hàng':
      requiredFields = ['name', 'phone','city','district','ward','address']; 
      break;
    case 'nhaSanXuat':
      requiredFields = ['nhaSanXuat'];
      break;
    case 'heDieuHanh':
      requiredFields = ['heDieuHanh', 'phienBan'];
      break;
    case 'manHinh':
      requiredFields = ['kichThuoc', 'doPhanGiai'];
      break;
    default:
      requiredFields = Object.keys(localEntityData.value);
  }

  const missingFields = requiredFields.filter(field => {
    const value = localEntityData.value[field];
    return value === undefined || value === null || value === '';
  });

  if (missingFields.length > 0) {
    alert(`Vui lòng nhập đầy đủ thông tin: ${missingFields.join(', ')}`);
    return;
  }

  const sanitizedData = {};
  Object.keys(localEntityData.value).forEach(key => {
    if (localEntityData.value[key] !== undefined && localEntityData.value[key] !== null) {
      sanitizedData[key] = localEntityData.value[key];
    }
  });

  emit('submit', sanitizedData);
};
</script>