<script setup>
import { ref } from 'vue'
import AppSidebar from '@/components/layout/AppSidebar.vue'
import AppHeader from '@/components/layout/AppHeader.vue'

// Em telas estreitas o menu lateral começa recolhido (só ícones)
const menuRecolhido = ref(window.innerWidth < 768)
</script>

<template>
  <el-container class="app">
    <AppSidebar :recolhido="menuRecolhido" />
    <el-container direction="vertical" class="app-conteudo">
      <AppHeader v-model:recolhido="menuRecolhido" />
      <el-main class="app-main">
        <RouterView v-slot="{ Component, route }">
          <component :is="Component" :key="route.fullPath" />
        </RouterView>
      </el-main>
    </el-container>
  </el-container>
</template>
