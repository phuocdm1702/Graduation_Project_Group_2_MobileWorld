<!-- BreadcrumbWrapper.vue -->
<template>
  <div class="bg-white shadow-lg rounded-lg p-5 mb-4 mt-4">
    <PathRouter :breadcrumb-items="breadcrumbItems" />
  </div>
</template>

<script setup lang="ts">
import PathRouter from "./PathRouter.vue"; // Đảm bảo đường dẫn đúng

defineProps<{
  breadcrumbItems: string[];
}>();
</script>

<style scoped>
/* Không cần thêm style vì class đã xử lý mọi thứ */
</style>