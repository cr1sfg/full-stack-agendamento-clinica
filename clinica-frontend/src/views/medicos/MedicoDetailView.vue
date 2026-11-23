<script setup>
import { useRouter } from 'vue-router'
import { Plus } from '@element-plus/icons-vue'
import PageHeader from '@/components/ui/PageHeader.vue'
import AsyncContent from '@/components/ui/AsyncContent.vue'
import AgendamentoTable from '@/components/agendamentos/AgendamentoTable.vue'
import { medicoService } from '@/services/medicoService'
import { agendamentoService } from '@/services/agendamentoService'
import { useAsyncData } from '@/composables/useAsyncData'
import { useListaFiltrada } from '@/composables/useListaFiltrada'
import { useExclusao } from '@/composables/useExclusao'
import { parseDataLocal } from '@/utils/formatters'

const props = defineProps({
  id: { type: String, required: true },
})
const router = useRouter()

const { data: medico, carregando, erro, recarregar } = useAsyncData(() => medicoService.buscarPorId(props.id))
const agenda = useAsyncData(() => agendamentoService.listarPorMedico(props.id), { valorInicial: [] })

const ordenacaoInicial = { prop: 'data', order: 'ascending' }
const { resultado: agendaOrdenada, ordenar } = useListaFiltrada(agenda.data, {
  ordenacoes: { data: (a) => parseDataLocal(a.data).getTime(), 'paciente.nome': (a) => a.paciente.nome },
  ordenacaoInicial,
  tamanhoPagina: 0,
})

const { excluir, excluindo } = useExclusao(medicoService.excluir)
const exclusaoAgendamento = useExclusao(agendamentoService.excluir)

function excluirMedico() {
  excluir(medico.value, {
    titulo: 'Excluir médico',
    mensagem: `Deseja excluir ${medico.value.nome}? Esta ação não pode ser desfeita.`,
    sucesso: 'Médico excluído.',
    aoExcluir: () => router.push({ name: 'medico-lista' }),
  })
}

function excluirAgendamento(agendamento) {
  exclusaoAgendamento.excluir(agendamento, {
    titulo: 'Excluir agendamento',
    mensagem: `Excluir a consulta de ${agendamento.paciente.nome}? Esta ação não pode ser desfeita.`,
    sucesso: 'Agendamento excluído.',
    aoExcluir: () => {
      agenda.data.value = agenda.data.value.filter((a) => a.id !== agendamento.id)
    },
  })
}
</script>

<template>
  <div>
    <PageHeader :titulo="medico?.nome ?? 'Médico'" :voltar-para="{ name: 'medico-lista' }">
      <template v-if="medico" #acoes>
        <el-button @click="router.push({ name: 'medico-editar', params: { id } })">Editar</el-button>
        <el-button type="danger" plain :loading="excluindo" @click="excluirMedico">Excluir</el-button>
      </template>
    </PageHeader>

    <el-card shadow="never">
      <AsyncContent :carregando="carregando" :erro="erro" :linhas="3" @tentar-novamente="recarregar">
        <el-descriptions v-if="medico" :column="3" border>
          <el-descriptions-item label="Nome">{{ medico.nome }}</el-descriptions-item>
          <el-descriptions-item label="CRM">{{ medico.crm }}</el-descriptions-item>
          <el-descriptions-item label="Especialidade">{{ medico.especialidade }}</el-descriptions-item>
        </el-descriptions>
      </AsyncContent>
    </el-card>

    <el-card v-if="medico" shadow="never" class="mt-16">
      <template #header>
        <div class="card-titulo">
          <span>Agenda</span>
          <el-button
            type="primary"
            size="small"
            :icon="Plus"
            @click="router.push({ name: 'agendamento-novo', query: { medicoId: id } })"
          >
            Agendar consulta
          </el-button>
        </div>
      </template>
      <AsyncContent
        :carregando="agenda.carregando.value"
        :erro="agenda.erro.value"
        :vazio="agenda.data.value.length === 0"
        mensagem-vazio="Nenhuma consulta agendada para este médico"
        @tentar-novamente="agenda.recarregar"
      >
        <AgendamentoTable
          :agendamentos="agendaOrdenada"
          ocultar-medico
          :ordenacao-inicial="ordenacaoInicial"
          @ordenar="ordenar"
          @excluir="excluirAgendamento"
        />
      </AsyncContent>
    </el-card>
  </div>
</template>

<style scoped>
.card-titulo {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>
