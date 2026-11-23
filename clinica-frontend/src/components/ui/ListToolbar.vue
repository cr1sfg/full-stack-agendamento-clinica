<script setup>
/**
 * Barra de filtros das listagens: busca textual, filtros extras (slot) e total de registros.
 * A ordenação é feita clicando no cabeçalho das colunas da tabela.
 */
import { Search } from '@element-plus/icons-vue'

const busca = defineModel('busca', { type: String, default: '' })

defineProps({
  placeholder: { type: String, default: 'Buscar' },
  total: { type: Number, required: true },
  totalFiltrado: { type: Number, required: true },
})
</script>

<template>
  <div class="toolbar">
    <el-input v-model="busca" :prefix-icon="Search" :placeholder="placeholder" clearable class="busca" />
    <slot name="filtros" />
    <span class="contagem">
      <template v-if="totalFiltrado !== total">{{ totalFiltrado }} de </template>{{ total }}
      {{ total === 1 ? 'registro' : 'registros' }}
    </span>
  </div>
</template>

<style scoped>
.toolbar {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 10px;
  margin-bottom: 16px;
}
.busca {
  width: 280px;
  max-width: 100%;
}
.toolbar :slotted(.el-select) {
  width: 200px;
}
.contagem {
  margin-left: auto;
  color: var(--el-text-color-secondary);
  font-size: 13px;
}
</style>
