<script setup>
/**
 * Centraliza os quatro estados de uma tela que consome a API:
 * carregando (skeleton) → erro (com "Tentar novamente") → lista vazia → sucesso (slot padrão).
 */
defineProps({
  carregando: { type: Boolean, default: false },
  erro: { type: [Object, Error, String], default: null },
  vazio: { type: Boolean, default: false },
  mensagemVazio: { type: String, default: 'Nenhum registro encontrado' },
  linhas: { type: Number, default: 5 },
})
defineEmits(['tentar-novamente'])
</script>

<template>
  <el-skeleton v-if="carregando" :rows="linhas" animated />
  <el-result
    v-else-if="erro"
    icon="error"
    title="Não foi possível carregar os dados"
    :sub-title="typeof erro === 'string' ? erro : erro.message"
  >
    <template #extra>
      <el-button type="primary" @click="$emit('tentar-novamente')">Tentar novamente</el-button>
    </template>
  </el-result>
  <el-empty v-else-if="vazio" :description="mensagemVazio" :image-size="90">
    <slot name="vazio" />
  </el-empty>
  <slot v-else />
</template>
