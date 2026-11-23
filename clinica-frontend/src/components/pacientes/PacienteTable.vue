<script setup>
import { useRouter } from 'vue-router'
import RowActions from '@/components/ui/RowActions.vue'
import { calcularIdade, formatarCpf, formatarData } from '@/utils/formatters'

defineProps({
  pacientes: { type: Array, required: true },
  ordenacaoInicial: { type: Object, default: () => ({ prop: 'nome', order: 'ascending' }) },
})
defineEmits(['excluir', 'ordenar'])

const router = useRouter()
const abrir = (p) => router.push({ name: 'paciente-detalhe', params: { id: p.id } })
</script>

<template>
  <el-table
    :data="pacientes"
    row-key="id"
    :default-sort="ordenacaoInicial"
    row-class-name="linha-clicavel"
    @row-click="abrir"
    @sort-change="$emit('ordenar', $event)"
  >
    <el-table-column prop="nome" label="Nome" sortable="custom" min-width="200" />
    <el-table-column label="CPF" width="150" class-name="mono">
      <template #default="{ row }">{{ formatarCpf(row.cpf) }}</template>
    </el-table-column>
    <el-table-column prop="dataNascimento" label="Nascimento" sortable="custom" width="140" class-name="mono">
      <template #default="{ row }">{{ formatarData(row.dataNascimento) }}</template>
    </el-table-column>
    <el-table-column label="Idade" width="90">
      <template #default="{ row }">{{ calcularIdade(row.dataNascimento) ?? '-' }}</template>
    </el-table-column>
    <el-table-column label="Ações" width="140" align="right">
      <template #default="{ row }">
        <RowActions :editar-para="{ name: 'paciente-editar', params: { id: row.id } }" @excluir="$emit('excluir', row)" />
      </template>
    </el-table-column>
  </el-table>
</template>
