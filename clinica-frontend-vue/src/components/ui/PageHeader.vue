<script setup>
/**
 * Cabeçalho das telas. Com `voltarPara`, usa o el-page-header (botão Voltar);
 * sem ele, exibe apenas título, descrição e ações (telas de listagem).
 */
import { useRouter } from 'vue-router'

const props = defineProps({
  titulo: { type: String, required: true },
  descricao: { type: String, default: '' },
  voltarPara: { type: [String, Object], default: null },
})

const router = useRouter()
const voltar = () => router.push(props.voltarPara)
</script>

<template>
  <div class="page-header">
    <el-page-header v-if="voltarPara" title="Voltar" @back="voltar">
      <template #content>
        <span class="titulo">{{ titulo }}</span>
        <span v-if="descricao || $slots.descricao" class="descricao">
          <slot name="descricao">{{ descricao }}</slot>
        </span>
      </template>
      <template v-if="$slots.acoes" #extra>
        <div class="acoes"><slot name="acoes" /></div>
      </template>
    </el-page-header>

    <div v-else class="simples">
      <div>
        <h2 class="titulo">{{ titulo }}</h2>
        <p v-if="descricao" class="descricao">{{ descricao }}</p>
      </div>
      <div v-if="$slots.acoes" class="acoes"><slot name="acoes" /></div>
    </div>
  </div>
</template>

<style scoped>
.page-header {
  margin-bottom: 16px;
}
.simples {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  flex-wrap: wrap;
}
h2.titulo {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
}
.titulo {
  font-weight: 600;
}
.descricao {
  margin: 4px 0 0;
  color: var(--el-text-color-secondary);
  font-size: 13px;
}
.el-page-header .descricao {
  margin: 0 0 0 10px;
}
.acoes {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}
:deep(.el-page-header__header) {
  flex-wrap: wrap;
  gap: 8px;
}
</style>
