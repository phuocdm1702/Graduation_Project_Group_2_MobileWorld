<!-- Template chính -->
<template>
  <div class="app-container">
    <component :is="layout">
      <router-view />
      <footer-page />
    </component>
  </div>
</template>

<script lang="ts" setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import FooterPage from "@/components/FooterPage.vue";

const defaultLayout = 'default'
const { currentRoute } = useRouter()

const layout = computed(
  () => `${currentRoute.value.meta.layout || defaultLayout}-layout`
)
</script>

<style scoped>
.app-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh; /* Chiều cao tối thiểu bằng toàn bộ viewport */
}

router-view {
  flex: 1 0 auto; /* Nội dung chính sẽ chiếm không gian còn lại */
}

footer-page {
  flex-shrink: 0; /* Footer không bị co lại */
}
</style>