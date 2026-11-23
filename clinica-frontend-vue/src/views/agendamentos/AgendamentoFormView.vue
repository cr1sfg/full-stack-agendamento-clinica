<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import PageHeader from '@/components/ui/PageHeader.vue'
import AsyncContent from '@/components/ui/AsyncContent.vue'
import AgendamentoForm from '@/components/agendamentos/AgendamentoForm.vue'
import { agendamentoService } from '@/services/agendamentoService'
import { medicoService } from '@/services/medicoService'
import { pacienteService } from '@/services/pacienteService'
import { useAsyncData } from '@/composables/useAsyncData'
import { useSalvar } from '@/composables/useSalvar'

/**
 * Mesma tela para cadastro (/agendamentos/novo) e edição (/agendamentos/:id/editar).
 * No cadastro, aceita ?medicoId= e ?pacienteId= para pré-selecionar (atalho das telas de detalhe).
 */
const props = defineProps({
  id: { type: String, default: null },
})

const route = useRoute()
const router = useRouter()
const edicao = computed(() => !!props.id)

const voltarPara = computed(() => {
  if (edicao.value) return { name: 'agendamento-detalhe', params: { id: props.id } }
  if (route.query.medicoId) return { name: 'medico-detalhe', params: { id: route.query.medicoId } }
  if (route.query.pacienteId) return { name: 'paciente-detalhe', params: { id: route.query.pacienteId } }
  return { name: 'agendamento-lista' }
})

/** Carrega em paralelo as entidades relacionadas necessárias para o formulário. */
const { data, carregando, erro, recarregar } = useAsyncData(async () => {
  const [medicos, pacientes, agendamento] = await Promise.all([
    medicoService.listar(),
    pacienteService.listar(),
    edicao.value ? agendamentoService.buscarPorId(props.id) : Promise.resolve(null),
  ])
  return { medicos, pacientes, agendamento }
})

const valorInicial = computed(() => {
  const ag = data.value?.agendamento
  if (ag) return { medicoId: ag.medico.id, pacienteId: ag.paciente.id, data: ag.data }
  return {
    medicoId: route.query.medicoId ? Number(route.query.medicoId) : '',
    pacienteId: route.query.pacienteId ? Number(route.query.pacienteId) : '',
  }
})

const faltaCadastro = computed(() => data.value && (!data.value.medicos.length || !data.value.pacientes.length))

const { salvar, salvando, errosServidor } = useSalvar({
  criar: agendamentoService.cadastrar,
  atualizar: agendamentoService.atualizar,
  rotaDetalhe: 'agendamento-detalhe',
  mensagens: { criado: 'Consulta agendada.', atualizado: 'Agendamento atualizado.' },
})
</script>

<template>
  <div>
    <PageHeader :titulo="edicao ? 'Editar agendamento' : 'Novo agendamento'" :voltar-para="voltarPara" />

    <el-card shadow="never">
      <AsyncContent :carregando="carregando" :erro="erro" @tentar-novamente="recarregar">
        <el-empty
          v-if="faltaCadastro"
          description="Para agendar uma consulta é preciso ter ao menos um médico e um paciente cadastrados."
          :image-size="90"
        >
          <el-button v-if="!data.medicos.length" type="primary" @click="router.push({ name: 'medico-novo' })">
            Cadastrar médico
          </el-button>
          <el-button v-if="!data.pacientes.length" type="primary" @click="router.push({ name: 'paciente-novo' })">
            Cadastrar paciente
          </el-button>
        </el-empty>

        <AgendamentoForm
          v-else-if="data"
          :valor-inicial="valorInicial"
          :medicos="data.medicos"
          :pacientes="data.pacientes"
          :erros-servidor="errosServidor"
          :salvando="salvando"
          :texto-botao="edicao ? 'Salvar alterações' : 'Agendar'"
          @enviar="(dados) => salvar(id, dados)"
          @cancelar="router.push(voltarPara)"
        />
      </AsyncContent>
    </el-card>
  </div>
</template>
