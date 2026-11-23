<script setup>
import { computed } from 'vue'
import { parseDataLocal } from '@/utils/formatters'

const props = defineProps({
  data: { type: String, required: true },
})

const status = computed(() => {
  const d = parseDataLocal(props.data)
  const agora = new Date()
  if (d < agora) return { rotulo: 'Realizado', tipo: 'info' }
  if (d.toDateString() === agora.toDateString()) return { rotulo: 'Hoje', tipo: 'warning' }
  return { rotulo: 'Agendado', tipo: 'success' }
})
</script>

<template>
  <el-tag :type="status.tipo" size="small" disable-transitions>{{ status.rotulo }}</el-tag>
</template>
