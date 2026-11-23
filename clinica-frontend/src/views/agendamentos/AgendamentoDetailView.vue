<script setup>
import { useRouter } from 'vue-router'
import PageHeader from '@/components/ui/PageHeader.vue'
import AsyncContent from '@/components/ui/AsyncContent.vue'
import AgendamentoStatus from '@/components/agendamentos/AgendamentoStatus.vue'
import { agendamentoService } from '@/services/agendamentoService'
import { useAsyncData } from '@/composables/useAsyncData'
import { useExclusao } from '@/composables/useExclusao'
import { calcularIdade, formatarCpf, formatarData, formatarDataHora, formatarDiaSemana } from '@/utils/formatters'

const props = defineProps({
  id: { type: String, required: true },
})
const router = useRouter()

const { data: agendamento, carregando, erro, recarregar } = useAsyncData(() => agendamentoService.buscarPorId(props.id))
const { excluir, excluindo } = useExclusao(agendamentoService.excluir)

function excluirAgendamento() {
  const a = agendamento.value
  excluir(a, {
    titulo: 'Excluir agendamento',
    mensagem: `Excluir a consulta de ${a.paciente.nome} em ${formatarDataHora(a.data)}? Esta ação não pode ser desfeita.`,
    sucesso: 'Agendamento excluído.',
    aoExcluir: () => router.push({ name: 'agendamento-lista' }),
  })
}
</script>

<template>
  <div>
    <PageHeader :titulo="`Agendamento nº ${id}`" :voltar-para="{ name: 'agendamento-lista' }">
      <template v-if="agendamento" #acoes>
        <el-button @click="router.push({ name: 'agendamento-editar', params: { id } })">Editar</el-button>
        <el-button type="danger" plain :loading="excluindo" @click="excluirAgendamento">Excluir</el-button>
      </template>
    </PageHeader>

    <el-card shadow="never">
      <AsyncContent :carregando="carregando" :erro="erro" @tentar-novamente="recarregar">
        <template v-if="agendamento">
          <el-descriptions title="Consulta" :column="2" border>
            <el-descriptions-item label="Data e hora">
              {{ formatarDiaSemana(agendamento.data) }}, {{ formatarDataHora(agendamento.data) }}
            </el-descriptions-item>
            <el-descriptions-item label="Situação"><AgendamentoStatus :data="agendamento.data" /></el-descriptions-item>
          </el-descriptions>

          <el-descriptions title="Paciente" :column="3" border class="mt-16">
            <template #extra>
              <el-button link type="primary" @click="router.push({ name: 'paciente-detalhe', params: { id: agendamento.paciente.id } })">
                Ver cadastro
              </el-button>
            </template>
            <el-descriptions-item label="Nome">{{ agendamento.paciente.nome }}</el-descriptions-item>
            <el-descriptions-item label="CPF">{{ formatarCpf(agendamento.paciente.cpf) }}</el-descriptions-item>
            <el-descriptions-item label="Nascimento">
              {{ formatarData(agendamento.paciente.dataNascimento) }}
              ({{ calcularIdade(agendamento.paciente.dataNascimento) }} anos)
            </el-descriptions-item>
          </el-descriptions>

          <el-descriptions title="Médico" :column="3" border class="mt-16">
            <template #extra>
              <el-button link type="primary" @click="router.push({ name: 'medico-detalhe', params: { id: agendamento.medico.id } })">
                Ver cadastro
              </el-button>
            </template>
            <el-descriptions-item label="Nome">{{ agendamento.medico.nome }}</el-descriptions-item>
            <el-descriptions-item label="CRM">{{ agendamento.medico.crm }}</el-descriptions-item>
            <el-descriptions-item label="Especialidade">{{ agendamento.medico.especialidade }}</el-descriptions-item>
          </el-descriptions>
        </template>
      </AsyncContent>
    </el-card>
  </div>
</template>
