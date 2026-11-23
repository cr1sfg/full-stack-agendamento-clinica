<script setup>
import { useRouter } from 'vue-router'
import RowActions from '@/components/ui/RowActions.vue'
import AgendamentoStatus from './AgendamentoStatus.vue'
import { formatarData, formatarDiaSemana, formatarHora } from '@/utils/formatters'

/**
 * Tabela de agendamentos. Reutilizada na listagem principal, no painel e nas telas de detalhe
 * de médico/paciente (onde a coluna da própria entidade é ocultada).
 */
defineProps({
  agendamentos: { type: Array, required: true },
  ocultarMedico: { type: Boolean, default: false },
  ocultarPaciente: { type: Boolean, default: false },
  ordenavel: { type: Boolean, default: true },
  ordenacaoInicial: { type: Object, default: () => ({ prop: 'data', order: 'ascending' }) },
})
defineEmits(['excluir', 'ordenar'])

const router = useRouter()
const abrir = (a) => router.push({ name: 'agendamento-detalhe', params: { id: a.id } })
</script>

<template>
  <el-table
    :data="agendamentos"
    row-key="id"
    :default-sort="ordenavel ? ordenacaoInicial : undefined"
    row-class-name="linha-clicavel"
    @row-click="abrir"
    @sort-change="$emit('ordenar', $event)"
  >
    <el-table-column prop="data" label="Data" :sortable="ordenavel && 'custom'" width="150" class-name="mono">
      <template #default="{ row }">
        {{ formatarData(row.data) }} <span class="texto-secundario">{{ formatarDiaSemana(row.data) }}</span>
      </template>
    </el-table-column>
    <el-table-column label="Hora" width="80" class-name="mono">
      <template #default="{ row }">{{ formatarHora(row.data) }}</template>
    </el-table-column>
    <el-table-column v-if="!ocultarPaciente" prop="paciente.nome" label="Paciente" :sortable="ordenavel && 'custom'" min-width="180" />
    <el-table-column v-if="!ocultarMedico" prop="medico.nome" label="Médico" :sortable="ordenavel && 'custom'" min-width="180">
      <template #default="{ row }">
        {{ row.medico.nome }}
        <div class="texto-secundario">{{ row.medico.especialidade }}</div>
      </template>
    </el-table-column>
    <el-table-column label="Situação" width="110">
      <template #default="{ row }"><AgendamentoStatus :data="row.data" /></template>
    </el-table-column>
    <el-table-column label="Ações" width="140" align="right">
      <template #default="{ row }">
        <RowActions :editar-para="{ name: 'agendamento-editar', params: { id: row.id } }" @excluir="$emit('excluir', row)" />
      </template>
    </el-table-column>
  </el-table>
</template>
