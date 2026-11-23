<template>
  <el-result icon="warning" title="Página não encontrada" sub-title="O endereço acessado não existe.">
    <template #extra>
      <el-button type="primary" @click="$router.push('/')">Voltar ao início</el-button>
    </template>
  </el-result>
</template>
