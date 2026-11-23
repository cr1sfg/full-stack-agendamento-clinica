<script setup>
import { Plus } from '@element-plus/icons-vue'
import PageHeader from '@/components/ui/PageHeader.vue'
import ListToolbar from '@/components/ui/ListToolbar.vue'
import AsyncContent from '@/components/ui/AsyncContent.vue'
import PacienteTable from '@/components/pacientes/PacienteTable.vue'
import { pacienteService } from '@/services/pacienteService'
import { useAsyncData } from '@/composables/useAsyncData'
import { useListaFiltrada } from '@/composables/useListaFiltrada'
import { useExclusao } from '@/composables/useExclusao'
import { formatarCpf, somenteDigitos } from '@/utils/formatters'

const { data: pacientes, carregando, erro, recarregar } = useAsyncData(pacienteService.listar, { valorInicial: [] })

const ordenacaoInicial = { prop: 'nome', order: 'ascending' }
const { busca, pagina, tamanhoPagina, resultado, paginados, ordenar, limpar } = useListaFiltrada(pacientes, {
  camposBusca: [(p) => p.nome, (p) => somenteDigitos(p.cpf), (p) => formatarCpf(p.cpf)],
  ordenacoes: { nome: (p) => p.nome, dataNascimento: (p) => p.dataNascimento },
  ordenacaoInicial,
})

const { excluir } = useExclusao(pacienteService.excluir)

function confirmarExclusao(paciente) {
  excluir(paciente, {
    titulo: 'Excluir paciente',
    mensagem: `Deseja excluir ${paciente.nome}? Esta ação não pode ser desfeita.`,
    sucesso: 'Paciente excluído.',
    aoExcluir: () => {
      pacientes.value = pacientes.value.filter((p) => p.id !== paciente.id)
    },
  })
}
</script>

<template>
  <div>
    <PageHeader titulo="Pacientes" descricao="Pacientes atendidos pela clínica">
      <template #acoes>
        <el-button type="primary" :icon="Plus" @click="$router.push({ name: 'paciente-novo' })">Novo paciente</el-button>
      </template>
    </PageHeader>

    <el-card shadow="never">
      <ListToolbar
        v-if="!carregando && !erro && pacientes.length"
        v-model:busca="busca"
        placeholder="Buscar por nome ou CPF"
        :total="pacientes.length"
        :total-filtrado="resultado.length"
      />

      <AsyncContent
        :carregando="carregando"
        :erro="erro"
        :vazio="pacientes.length === 0"
        mensagem-vazio="Nenhum paciente cadastrado"
        @tentar-novamente="recarregar"
      >
        <template #vazio>
          <el-button type="primary" @click="$router.push({ name: 'paciente-novo' })">Cadastrar paciente</el-button>
        </template>

        <el-empty v-if="resultado.length === 0" :description="`Nenhum paciente encontrado para “${busca}”`" :image-size="90">
          <el-button @click="limpar">Limpar busca</el-button>
        </el-empty>
        <template v-else>
          <PacienteTable
            :pacientes="paginados"
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
