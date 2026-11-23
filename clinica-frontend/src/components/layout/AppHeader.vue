<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { Expand, Fold } from '@element-plus/icons-vue'

const recolhido = defineModel('recolhido', { type: Boolean, default: false })
const route = useRoute()

/** Breadcrumb montado a partir do `meta` das rotas (ver router/index.js). */
const trilha = computed(() => {
  const { secao, secaoRota, acao } = route.meta
  const itens = [{ rotulo: 'Início', para: '/' }]
  if (secao) itens.push({ rotulo: secao, para: acao ? { name: secaoRota } : null })
  if (acao) itens.push({ rotulo: acao })
  return itens
})
</script>

<template>
  <el-header height="56px" class="cabecalho">
    <el-button
      text
      :icon="recolhido ? Expand : Fold"
      :aria-label="recolhido ? 'Expandir menu' : 'Recolher menu'"
      @click="recolhido = !recolhido"
    />
    <el-breadcrumb separator="/">
      <el-breadcrumb-item v-for="(item, i) in trilha" :key="i" :to="item.para ?? undefined">
        {{ item.rotulo }}
      </el-breadcrumb-item>
    </el-breadcrumb>
  </el-header>
</template>

<style scoped>
.cabecalho {
  display: flex;
  align-items: center;
  gap: 12px;
  background: #fff;
  border-bottom: 1px solid var(--el-border-color-light);
  padding: 0 16px 0 8px;
}
</style>
