<script setup>
import { computed, ref } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import PageHeader from '@/components/ui/PageHeader.vue'
import ListToolbar from '@/components/ui/ListToolbar.vue'
import AsyncContent from '@/components/ui/AsyncContent.vue'
import AgendamentoTable from '@/components/agendamentos/AgendamentoTable.vue'
import { agendamentoService } from '@/services/agendamentoService'
import { useAsyncData } from '@/composables/useAsyncData'
import { useListaFiltrada } from '@/composables/useListaFiltrada'
import { useExclusao } from '@/composables/useExclusao'
import { formatarCpf, parseDataLocal, somenteDigitos } from '@/utils/formatters'

const { data: agendamentos, carregando, erro, recarregar } = useAsyncData(agendamentoService.listar, {
  valorInicial: [],
})

/** Filtros adicionais: médico e período (futuras / realizadas). */
const medicoId = ref('')
const periodo = ref('')

const medicos = computed(() => {
  const mapa = new Map(agendamentos.value.map((a) => [a.medico.id, a.medico]))
  return [...mapa.values()].sort((a, b) => a.nome.localeCompare(b.nome, 'pt-BR'))
})

const filtros = computed(() => {
  const agora = new Date()
  const lista = []
  if (medicoId.value) lista.push((a) => a.medico.id === medicoId.value)
  if (periodo.value === 'futuros') lista.push((a) => parseDataLocal(a.data) >= agora)
  if (periodo.value === 'realizados') lista.push((a) => parseDataLocal(a.data) < agora)
  return lista
})

const ordenacaoInicial = { prop: 'data', order: 'ascending' }
const { busca, pagina, tamanhoPagina, resultado, paginados, ordenar, limpar } = useListaFiltrada(agendamentos, {
  camposBusca: [
    (a) => a.paciente.nome,
    (a) => somenteDigitos(a.paciente.cpf),
    (a) => formatarCpf(a.paciente.cpf),
    (a) => a.medico.nome,
    (a) => a.medico.especialidade,
  ],
  ordenacoes: {
    data: (a) => parseDataLocal(a.data).getTime(),
    'paciente.nome': (a) => a.paciente.nome,
    'medico.nome': (a) => a.medico.nome,
  },
  ordenacaoInicial,
  filtros,
})

function limparFiltros() {
  limpar()
  medicoId.value = ''
  periodo.value = ''
}

const { excluir } = useExclusao(agendamentoService.excluir)

function confirmarExclusao(agendamento) {
  excluir(agendamento, {
    titulo: 'Excluir agendamento',
    mensagem: `Excluir a consulta de ${agendamento.paciente.nome} com ${agendamento.medico.nome}? Esta ação não pode ser desfeita.`,
    sucesso: 'Agendamento excluído.',
    aoExcluir: () => {
      agendamentos.value = agendamentos.value.filter((a) => a.id !== agendamento.id)
    },
  })
}
</script>

<template>
  <div>
    <PageHeader titulo="Agendamentos" descricao="Consultas marcadas entre pacientes e médicos">
      <template #acoes>
        <el-button type="primary" :icon="Plus" @click="$router.push({ name: 'agendamento-novo' })">
          Novo agendamento
        </el-button>
      </template>
    </PageHeader>

    <el-card shadow="never">
      <ListToolbar
        v-if="!carregando && !erro && agendamentos.length"
        v-model:busca="busca"
        placeholder="Paciente, CPF ou médico"
        :total="agendamentos.length"
        :total-filtrado="resultado.length"
      >
        <template #filtros>
          <el-select v-model="medicoId" placeholder="Médico" clearable filterable>
            <el-option v-for="m in medicos" :key="m.id" :label="m.nome" :value="m.id" />
          </el-select>
          <el-select v-model="periodo" placeholder="Período" clearable>
            <el-option label="Próximas consultas" value="futuros" />
            <el-option label="Consultas realizadas" value="realizados" />
          </el-select>
        </template>
      </ListToolbar>

      <AsyncContent
        :carregando="carregando"
        :erro="erro"
        :vazio="agendamentos.length === 0"
        mensagem-vazio="Nenhum agendamento registrado"
        @tentar-novamente="recarregar"
      >
        <template #vazio>
          <el-button type="primary" @click="$router.push({ name: 'agendamento-novo' })">Agendar consulta</el-button>
        </template>

        <el-empty v-if="resultado.length === 0" description="Nenhum agendamento encontrado com esses filtros" :image-size="90">
          <el-button @click="limparFiltros">Limpar filtros</el-button>
        </el-empty>
        <template v-else>
          <AgendamentoTable
            :agendamentos="paginados"
            :ordenacao-inicial="ordenacaoInicial"
            @ordenar="ordenar"
            @excluir="confirmarExclusao"
          />
          <div v-if="resultado.length > tamanhoPagina" class="tabela-rodape">
            <el-pagination v-model:current-page="pagina" :page-size="tamanhoPagina" :total="resultado.length" layout="total, prev, pager, next" background />
          </div>
        </template>
      </AsyncContent>
    </el-card>
  </div>
</template>
