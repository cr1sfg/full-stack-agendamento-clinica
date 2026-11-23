<script setup>
import { useRouter } from 'vue-router'
import RowActions from '@/components/ui/RowActions.vue'

defineProps({
  medicos: { type: Array, required: true },
  ordenacaoInicial: { type: Object, default: () => ({ prop: 'nome', order: 'ascending' }) },
})
defineEmits(['excluir', 'ordenar'])

const router = useRouter()
const abrir = (m) => router.push({ name: 'medico-detalhe', params: { id: m.id } })
</script>

<template>
  <el-table
    :data="medicos"
    row-key="id"
    :default-sort="ordenacaoInicial"
    row-class-name="linha-clicavel"
    @row-click="abrir"
    @sort-change="$emit('ordenar', $event)"
  >
    <el-table-column prop="nome" label="Nome" sortable="custom" min-width="200" />
    <el-table-column prop="crm" label="CRM" sortable="custom" width="140" class-name="mono" />
    <el-table-column prop="especialidade" label="Especialidade" sortable="custom" min-width="160" />
    <el-table-column label="Ações" width="140" align="right">
      <template #default="{ row }">
        <RowActions :editar-para="{ name: 'medico-editar', params: { id: row.id } }" @excluir="$emit('excluir', row)" />
      </template>
    </el-table-column>
  </el-table>
</template>
