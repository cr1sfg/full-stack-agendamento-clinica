<script setup>
import { useRouter } from 'vue-router'
import { Plus } from '@element-plus/icons-vue'
import PageHeader from '@/components/ui/PageHeader.vue'
import AsyncContent from '@/components/ui/AsyncContent.vue'
import AgendamentoTable from '@/components/agendamentos/AgendamentoTable.vue'
import { pacienteService } from '@/services/pacienteService'
import { agendamentoService } from '@/services/agendamentoService'
import { useAsyncData } from '@/composables/useAsyncData'
import { useListaFiltrada } from '@/composables/useListaFiltrada'
import { useExclusao } from '@/composables/useExclusao'
import { calcularIdade, formatarCpf, formatarData, parseDataLocal } from '@/utils/formatters'

const props = defineProps({
  id: { type: String, required: true },
})
const router = useRouter()

const { data: paciente, carregando, erro, recarregar } = useAsyncData(() => pacienteService.buscarPorId(props.id))
const consultas = useAsyncData(() => agendamentoService.listarPorPaciente(props.id), { valorInicial: [] })

const ordenacaoInicial = { prop: 'data', order: 'descending' }
const { resultado: consultasOrdenadas, ordenar } = useListaFiltrada(consultas.data, {
  ordenacoes: { data: (a) => parseDataLocal(a.data).getTime(), 'medico.nome': (a) => a.medico.nome },
  ordenacaoInicial,
  tamanhoPagina: 0,
})

const { excluir, excluindo } = useExclusao(pacienteService.excluir)
const exclusaoAgendamento = useExclusao(agendamentoService.excluir)

function excluirPaciente() {
  excluir(paciente.value, {
    titulo: 'Excluir paciente',
    mensagem: `Deseja excluir ${paciente.value.nome}? Esta ação não pode ser desfeita.`,
    sucesso: 'Paciente excluído.',
    aoExcluir: () => router.push({ name: 'paciente-lista' }),
  })
}

function excluirAgendamento(agendamento) {
  exclusaoAgendamento.excluir(agendamento, {
    titulo: 'Excluir agendamento',
    mensagem: `Excluir a consulta com ${agendamento.medico.nome}? Esta ação não pode ser desfeita.`,
    sucesso: 'Agendamento excluído.',
    aoExcluir: () => {
      consultas.data.value = consultas.data.value.filter((a) => a.id !== agendamento.id)
    },
  })
}
</script>

<template>
  <div>
    <PageHeader :titulo="paciente?.nome ?? 'Paciente'" :voltar-para="{ name: 'paciente-lista' }">
      <template v-if="paciente" #acoes>
        <el-button @click="router.push({ name: 'paciente-editar', params: { id } })">Editar</el-button>
        <el-button type="danger" plain :loading="excluindo" @click="excluirPaciente">Excluir</el-button>
      </template>
    </PageHeader>

    <el-card shadow="never">
      <AsyncContent :carregando="carregando" :erro="erro" :linhas="3" @tentar-novamente="recarregar">
        <el-descriptions v-if="paciente" :column="3" border>
          <el-descriptions-item label="Nome">{{ paciente.nome }}</el-descriptions-item>
          <el-descriptions-item label="CPF">{{ formatarCpf(paciente.cpf) }}</el-descriptions-item>
          <el-descriptions-item label="Nascimento">
            {{ formatarData(paciente.dataNascimento) }} ({{ calcularIdade(paciente.dataNascimento) }} anos)
          </el-descriptions-item>
        </el-descriptions>
      </AsyncContent>
    </el-card>

    <el-card v-if="paciente" shadow="never" class="mt-16">
      <template #header>
        <div class="card-titulo">
          <span>Consultas</span>
          <el-button
            type="primary"
            size="small"
            :icon="Plus"
            @click="router.push({ name: 'agendamento-novo', query: { pacienteId: id } })"
          >
            Agendar consulta
          </el-button>
        </div>
      </template>
      <AsyncContent
        :carregando="consultas.carregando.value"
        :erro="consultas.erro.value"
        :vazio="consultas.data.value.length === 0"
        mensagem-vazio="Nenhuma consulta para este paciente"
        @tentar-novamente="consultas.recarregar"
      >
        <AgendamentoTable
          :agendamentos="consultasOrdenadas"
          ocultar-paciente
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
