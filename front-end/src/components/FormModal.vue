<template>
  <div v-if="show" class="fixed inset-0 flex items-center justify-center z-50">
    <!-- Overlay tối màu -->
    <div class="fixed inset-0 bg-black bg-opacity-50 z-40" @click="close"></div>

    <!-- Modal -->
    <div class="bg-white p-6 rounded-xl shadow-lg border border-gray-100 max-w-md w-full z-50">
      <h2 class="text-2xl font-semibold text-gray-800 mb-6 flex items-center">
        <i :class="`fa-solid ${iconClass} ${iconColor} mr-2`"></i>
        {{ isEdit ? `Cập Nhật ${entityName}` : `Thêm ${entityName}` }}
      </h2>
      <form @submit.prevent="submitForm" class="space-y-6">
        <div class="grid grid-cols-1 gap-6">
          <!-- Dùng slots để linh hoạt với các trường input -->
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
            :class="isEdit ? 'bg-blue-600 hover:bg-blue-700' : 'bg-green-600 hover:bg-green-700'"
            class="text-white px-5 py-2 rounded-lg transition-all duration-200 font-medium flex items-center"
          >
            <i class="fa-solid fa-save mr-2"></i> {{ isEdit ? 'Cập nhật' : 'Thêm mới' }}
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
  isEdit: Boolean,
  entityName: {
    type: String,
    required: true, // Tên thuộc tính (ví dụ: "Dòng Sản Phẩm", "Nhà Sản Xuất")
  },
  entityData: {
    type: Object,
    default: () => ({ id: null }), // Mặc định chỉ có id, các trường khác sẽ được định nghĩa qua slot
  },
  iconClass: {
    type: String,
    default: 'fa-plus-circle', // Icon mặc định cho thêm mới
  },
  iconColor: {
    type: String,
    default: 'text-green-500', // Màu mặc định cho thêm mới
  },
});

const emit = defineEmits(['submit', 'close']);

const localEntityData = ref({ ...props.entityData });

// Đồng bộ dữ liệu từ prop entityData khi thay đổi
watch(
  () => props.entityData,
  (newValue) => {
    console.log('Syncing entityData:', newValue);
    localEntityData.value = { ...newValue };
  },
  { immediate: true }
);

const submitForm = () => {
  console.log('Submitting entity data:', localEntityData.value);

  // Kiểm tra dữ liệu dựa trên entityName
  let requiredFields = [];
  switch (props.entityName) {
    case 'Dòng Sản Phẩm':
      requiredFields = ['ma', 'dongSanPham'];
      break;
    case 'Nhà Sản Xuất':
      requiredFields = ['ma', 'nhaSanXuat'];
      break;
    case 'Imel':
      requiredFields = ['ma', 'imel'];
      break;
    // Có thể mở rộng cho các thực thể khác
    default:
      requiredFields = Object.keys(localEntityData.value).filter(key => key !== 'id');
  }

  // Kiểm tra các trường bắt buộc
  const missingFields = requiredFields.filter(field => !localEntityData.value[field]);
  if (missingFields.length > 0) {
    console.error('Dữ liệu không đầy đủ:', missingFields, localEntityData.value);
    return; // Ngăn emit nếu dữ liệu thiếu
  }

  emit('submit', localEntityData.value);
};

const close = () => {
  emit('close');
};
</script>

<style scoped>
:root {
  --ring-color: #10b981; /* Màu xanh lá cho Thêm mới */
}
div[z-50] {
  --ring-color: #3b82f6; /* Màu xanh dương cho Cập nhật */
}
</style>