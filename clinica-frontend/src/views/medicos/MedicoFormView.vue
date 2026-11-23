<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import PageHeader from '@/components/ui/PageHeader.vue'
import AsyncContent from '@/components/ui/AsyncContent.vue'
import MedicoForm from '@/components/medicos/MedicoForm.vue'
import { medicoService } from '@/services/medicoService'
import { useAsyncData } from '@/composables/useAsyncData'
import { useSalvar } from '@/composables/useSalvar'

/** Mesma tela para cadastro (/medicos/novo) e edição (/medicos/:id/editar). */
const props = defineProps({
  id: { type: String, default: null },
})

const router = useRouter()
const edicao = computed(() => !!props.id)
const voltarPara = computed(() =>
  edicao.value ? { name: 'medico-detalhe', params: { id: props.id } } : { name: 'medico-lista' },
)

const { data: medico, carregando, erro, recarregar } = useAsyncData(() => medicoService.buscarPorId(props.id), {
  imediato: edicao.value,
})

const { salvar, salvando, errosServidor } = useSalvar({
  criar: medicoService.cadastrar,
  atualizar: medicoService.atualizar,
  rotaDetalhe: 'medico-detalhe',
  mensagens: { criado: 'Médico cadastrado.', atualizado: 'Dados do médico atualizados.' },
})
</script>

<template>
  <div>
    <PageHeader :titulo="edicao ? 'Editar médico' : 'Novo médico'" :voltar-para="voltarPara" />

    <el-card shadow="never">
      <AsyncContent v-if="edicao" :carregando="carregando" :erro="erro" @tentar-novamente="recarregar">
        <MedicoForm
          v-if="medico"
          :valor-inicial="medico"
          :erros-servidor="errosServidor"
          :salvando="salvando"
          texto-botao="Salvar alterações"
          @enviar="(dados) => salvar(id, dados)"
          @cancelar="router.push(voltarPara)"
        />
      </AsyncContent>
      <MedicoForm
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
