<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import PageHeader from '@/components/ui/PageHeader.vue'
import AsyncContent from '@/components/ui/AsyncContent.vue'
import PacienteForm from '@/components/pacientes/PacienteForm.vue'
import { pacienteService } from '@/services/pacienteService'
import { useAsyncData } from '@/composables/useAsyncData'
import { useSalvar } from '@/composables/useSalvar'

/** Mesma tela para cadastro (/pacientes/novo) e edição (/pacientes/:id/editar). */
const props = defineProps({
  id: { type: String, default: null },
})

const router = useRouter()
const edicao = computed(() => !!props.id)
const voltarPara = computed(() =>
  edicao.value ? { name: 'paciente-detalhe', params: { id: props.id } } : { name: 'paciente-lista' },
)

const { data: paciente, carregando, erro, recarregar } = useAsyncData(() => pacienteService.buscarPorId(props.id), {
  imediato: edicao.value,
})

const { salvar, salvando, errosServidor } = useSalvar({
  criar: pacienteService.cadastrar,
  atualizar: pacienteService.atualizar,
  rotaDetalhe: 'paciente-detalhe',
  mensagens: { criado: 'Paciente cadastrado.', atualizado: 'Dados do paciente atualizados.' },
})
</script>

<template>
  <div>
    <PageHeader :titulo="edicao ? 'Editar paciente' : 'Novo paciente'" :voltar-para="voltarPara" />

    <el-card shadow="never">
      <AsyncContent v-if="edicao" :carregando="carregando" :erro="erro" @tentar-novamente="recarregar">
        <PacienteForm
          v-if="paciente"
          :valor-inicial="paciente"
          :erros-servidor="errosServidor"
          :salvando="salvando"
          texto-botao="Salvar alterações"
          @enviar="(dados) => salvar(id, dados)"
          @cancelar="router.push(voltarPara)"
        />
      </AsyncContent>
      <PacienteForm
        v-else
        :erros-servidor="errosServidor"
        :salvando="salvando"
        texto-botao="Cadastrar"
        @enviar="(dados) => salvar(null, dados)"
        @cancelar="router.push(voltarPara)"
      />
    </el-card>
  </div>
</template>
