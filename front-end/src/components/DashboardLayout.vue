<template>
  <div class="flex h-screen bg-gray-200 font-roboto">
    <Sidebar />

    <div class="flex-1 flex flex-col overflow-hidden">
      <Header />

      <main class="flex-1 overflow-x-hidden overflow-y-auto bg-gray-200">
        <div class="container mx-auto px-2 py-2">
          <slot />
        </div>
      </main>
      
    </div>
  </div>
</template>

<script setup lang="ts">
import Sidebar from './AppSidebar.vue'
import Header from './AppHeader.vue'

</script>
