<template>
  <component :is="layout">
    <router-view />
    <footer-page/>
  </component>
  
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
