<script setup>
/** Ações padrão de uma linha de tabela: editar e excluir. */
defineProps({
  editarPara: { type: [String, Object], required: true },
})
defineEmits(['excluir'])
</script>

<template>
  <span @click.stop>
    <el-button link type="primary" @click="$router.push(editarPara)">Editar</el-button>
    <el-button link type="danger" @click="$emit('excluir')">Excluir</el-button>
  </span>
</template>
