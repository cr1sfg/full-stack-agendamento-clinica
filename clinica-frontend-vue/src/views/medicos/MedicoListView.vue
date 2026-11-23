<script setup>
import { computed, ref } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import PageHeader from '@/components/ui/PageHeader.vue'
import ListToolbar from '@/components/ui/ListToolbar.vue'
import AsyncContent from '@/components/ui/AsyncContent.vue'
import MedicoTable from '@/components/medicos/MedicoTable.vue'
import { medicoService } from '@/services/medicoService'
import { useAsyncData } from '@/composables/useAsyncData'
import { useListaFiltrada } from '@/composables/useListaFiltrada'
import { useExclusao } from '@/composables/useExclusao'

const { data: medicos, carregando, erro, recarregar } = useAsyncData(medicoService.listar, { valorInicial: [] })

const especialidade = ref('')
const especialidades = computed(() =>
  [...new Set(medicos.value.map((m) => m.especialidade))].sort((a, b) => a.localeCompare(b, 'pt-BR')),
)

const ordenacaoInicial = { prop: 'nome', order: 'ascending' }
const { busca, pagina, tamanhoPagina, resultado, paginados, ordenar, limpar } = useListaFiltrada(medicos, {
  camposBusca: [(m) => m.nome, (m) => m.crm],
  ordenacoes: { nome: (m) => m.nome, crm: (m) => m.crm, especialidade: (m) => m.especialidade },
  ordenacaoInicial,
  filtros: computed(() => (especialidade.value ? [(m) => m.especialidade === especialidade.value] : [])),
})

function limparFiltros() {
  limpar()
  especialidade.value = ''
}

const { excluir } = useExclusao(medicoService.excluir)

function confirmarExclusao(medico) {
  excluir(medico, {
    titulo: 'Excluir médico',
    mensagem: `Deseja excluir ${medico.nome} (CRM ${medico.crm})? Esta ação não pode ser desfeita.`,
    sucesso: 'Médico excluído.',
    aoExcluir: () => {
      medicos.value = medicos.value.filter((m) => m.id !== medico.id)
    },
  })
}
</script>

<template>
  <div>
    <PageHeader titulo="Médicos" descricao="Profissionais cadastrados na clínica">
      <template #acoes>
        <el-button type="primary" :icon="Plus" @click="$router.push({ name: 'medico-novo' })">Novo médico</el-button>
      </template>
    </PageHeader>

    <el-card shadow="never">
      <ListToolbar
        v-if="!carregando && !erro && medicos.length"
        v-model:busca="busca"
        placeholder="Buscar por nome ou CRM"
        :total="medicos.length"
        :total-filtrado="resultado.length"
      >
        <template #filtros>
          <el-select v-model="especialidade" placeholder="Especialidade" clearable>
            <el-option v-for="e in especialidades" :key="e" :label="e" :value="e" />
          </el-select>
        </template>
      </ListToolbar>

      <AsyncContent
        :carregando="carregando"
        :erro="erro"
        :vazio="medicos.length === 0"
        mensagem-vazio="Nenhum médico cadastrado"
        @tentar-novamente="recarregar"
      >
        <template #vazio>
          <el-button type="primary" @click="$router.push({ name: 'medico-novo' })">Cadastrar médico</el-button>
        </template>

        <el-empty v-if="resultado.length === 0" description="Nenhum médico encontrado com esses filtros" :image-size="90">
          <el-button @click="limparFiltros">Limpar filtros</el-button>
        </el-empty>
        <template v-else>
          <MedicoTable
            :medicos="paginados"
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
