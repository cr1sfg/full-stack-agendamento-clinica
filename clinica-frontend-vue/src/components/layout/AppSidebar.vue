<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { Calendar, FirstAidKit, HomeFilled, User } from '@element-plus/icons-vue'

defineProps({
  recolhido: { type: Boolean, default: false },
})

const route = useRoute()

const itens = [
  { index: '/', rotulo: 'Início', icone: HomeFilled },
  { index: '/agendamentos', rotulo: 'Agendamentos', icone: Calendar },
  { index: '/medicos', rotulo: 'Médicos', icone: FirstAidKit },
  { index: '/pacientes', rotulo: 'Pacientes', icone: User },
]

/** Mantém o item do menu ativo também nas sub-rotas (ex.: /medicos/3/editar). */
const ativo = computed(() => '/' + (route.path.split('/')[1] ?? ''))
</script>

<template>
  <el-aside :width="recolhido ? '64px' : '210px'" class="sidebar">
    <RouterLink to="/" class="marca">
      <img src="@/assets/logo.svg" alt="" width="24" height="24" />
      <span v-show="!recolhido">Clínica Médica</span>
    </RouterLink>
    <el-menu :default-active="ativo" :collapse="recolhido" :collapse-transition="false" router class="menu">
      <el-menu-item v-for="item in itens" :key="item.index" :index="item.index">
        <el-icon><component :is="item.icone" /></el-icon>
        <template #title>{{ item.rotulo }}</template>
      </el-menu-item>
    </el-menu>
  </el-aside>
</template>

<style scoped>
.sidebar {
  background: #fff;
  border-right: 1px solid var(--el-border-color-light);
  transition: width 0.2s;
  overflow-x: hidden;
}
.marca {
  display: flex;
  align-items: center;
  gap: 10px;
  height: 56px;
  padding: 0 20px;
  border-bottom: 1px solid var(--el-border-color-light);
  color: var(--el-text-color-primary);
  font-weight: 600;
  font-size: 15px;
  white-space: nowrap;
  text-decoration: none;
}
.menu {
  border-right: none;
}
</style>
