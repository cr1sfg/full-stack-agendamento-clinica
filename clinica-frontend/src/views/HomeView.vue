<script setup>
import { computed } from 'vue'
import AsyncContent from '@/components/ui/AsyncContent.vue'
import AgendamentoTable from '@/components/agendamentos/AgendamentoTable.vue'
import { medicoService } from '@/services/medicoService'
import { pacienteService } from '@/services/pacienteService'
import { agendamentoService } from '@/services/agendamentoService'
import { useAsyncData } from '@/composables/useAsyncData'
import { useExclusao } from '@/composables/useExclusao'
import { parseDataLocal } from '@/utils/formatters'

const { data, carregando, erro, recarregar } = useAsyncData(async () => {
  const [medicos, pacientes, agendamentos] = await Promise.all([
    medicoService.listar(),
    pacienteService.listar(),
    agendamentoService.listar(),
  ])
  return { medicos, pacientes, agendamentos }
})

const proximos = computed(() => {
  if (!data.value) return []
  const agora = new Date()
  return data.value.agendamentos
    .filter((a) => parseDataLocal(a.data) >= agora)
    .sort((a, b) => parseDataLocal(a.data) - parseDataLocal(b.data))
})

const consultasHoje = computed(() => {
  const hoje = new Date().toDateString()
  return proximos.value.filter((a) => parseDataLocal(a.data).toDateString() === hoje).length
})

const { excluir } = useExclusao(agendamentoService.excluir)

function excluirAgendamento(agendamento) {
  excluir(agendamento, {
    titulo: 'Excluir agendamento',
    mensagem: `Excluir a consulta de ${agendamento.paciente.nome}? Esta ação não pode ser desfeita.`,
    sucesso: 'Agendamento excluído.',
    aoExcluir: () => {
      data.value = { ...data.value, agendamentos: data.value.agendamentos.filter((a) => a.id !== agendamento.id) }
    },
  })
}
</script>

<template>
  <div>
    <AsyncContent :carregando="carregando" :erro="erro" @tentar-novamente="recarregar">
      <template v-if="data">
        <el-row :gutter="16">
          <el-col :xs="12" :sm="6">
            <el-card shadow="never"><el-statistic title="Consultas hoje" :value="consultasHoje" /></el-card>
          </el-col>
          <el-col :xs="12" :sm="6">
            <el-card shadow="never"><el-statistic title="Próximas consultas" :value="proximos.length" /></el-card>
          </el-col>
          <el-col :xs="12" :sm="6">
            <el-card shadow="never"><el-statistic title="Médicos" :value="data.medicos.length" /></el-card>
          </el-col>
          <el-col :xs="12" :sm="6">
            <el-card shadow="never"><el-statistic title="Pacientes" :value="data.pacientes.length" /></el-card>
          </el-col>
        </el-row>

        <el-card shadow="never" class="mt-16">
          <template #header>
            <div class="card-titulo">
              <span>Próximas consultas</span>
              <el-button link type="primary" @click="$router.push({ name: 'agendamento-lista' })">Ver todas</el-button>
            </div>
          </template>
          <el-empty v-if="proximos.length === 0" description="Nenhuma consulta agendada" :image-size="80">
            <el-button type="primary" @click="$router.push({ name: 'agendamento-novo' })">Agendar consulta</el-button>
          </el-empty>
          <AgendamentoTable v-else :agendamentos="proximos.slice(0, 8)" :ordenavel="false" @excluir="excluirAgendamento" />
        </el-card>
      </template>
    </AsyncContent>
  </div>
</template>

<style scoped>
.el-col {
  margin-bottom: 16px;
}
.el-row {
  margin-bottom: -16px;
}
.card-titulo {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>
