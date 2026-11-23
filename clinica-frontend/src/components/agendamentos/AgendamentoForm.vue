<script setup>
import { computed, reactive, ref } from 'vue'
import { criarRegras } from '@/utils/regras'
import { validarAgendamento } from '@/utils/validators'
import { formatarCpf } from '@/utils/formatters'
import { useErrosApi } from '@/composables/useErrosApi'

/**
 * Formulário de agendamento, reaproveitado no cadastro e na edição.
 * Recebe as listas de médicos e pacientes já carregadas pela view.
 */
const props = defineProps({
  valorInicial: { type: Object, default: () => ({}) },
  medicos: { type: Array, required: true },
  pacientes: { type: Array, required: true },
  errosServidor: { type: Object, default: () => ({}) },
  salvando: { type: Boolean, default: false },
  textoBotao: { type: String, default: 'Salvar' },
})
const emit = defineEmits(['enviar', 'cancelar'])

const formRef = ref()
const modelo = reactive({
  pacienteId: props.valorInicial.pacienteId ?? '',
  medicoId: props.valorInicial.medicoId ?? '',
  data: props.valorInicial.data ?? '',
})
const regras = criarRegras(modelo, validarAgendamento, ['pacienteId', 'medicoId', 'data'])
const errosApi = useErrosApi(modelo, () => props.errosServidor)

const porNome = (a, b) => a.nome.localeCompare(b.nome, 'pt-BR')
const medicosOrdenados = computed(() => [...props.medicos].sort(porNome))
const pacientesOrdenados = computed(() => [...props.pacientes].sort(porNome))

/** Restrições do seletor, iguais às regras do backend: dias úteis a partir de hoje, das 08h às 17h. */
function diaIndisponivel(data) {
  const hoje = new Date()
  hoje.setHours(0, 0, 0, 0)
  return data < hoje || data.getDay() === 0 || data.getDay() === 6
}
const horasIndisponiveis = () => [...Array(24).keys()].filter((h) => h < 8 || h > 17)
const horaPadrao = new Date(2000, 0, 1, 8, 0, 0)

async function enviar() {
  const valido = await formRef.value.validate().catch(() => false)
  if (!valido) return
  emit('enviar', {
    medicoId: Number(modelo.medicoId),
    pacienteId: Number(modelo.pacienteId),
    data: modelo.data,
  })
}
</script>

<template>
  <el-form ref="formRef" :model="modelo" :rules="regras" label-position="top" class="formulario" @submit.prevent="enviar">
    <el-row :gutter="16">
      <el-col :xs="24" :sm="12">
        <el-form-item label="Paciente" prop="pacienteId" :error="errosApi.pacienteId">
          <el-select v-model="modelo.pacienteId" filterable placeholder="Selecione o paciente" style="width: 100%">
            <el-option v-for="p in pacientesOrdenados" :key="p.id" :label="p.nome" :value="p.id">
              <span>{{ p.nome }}</span>
              <span class="opcao-extra">{{ formatarCpf(p.cpf) }}</span>
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :xs="24" :sm="12">
        <el-form-item label="Médico" prop="medicoId" :error="errosApi.medicoId">
          <el-select v-model="modelo.medicoId" filterable placeholder="Selecione o médico" style="width: 100%">
            <el-option v-for="m in medicosOrdenados" :key="m.id" :label="m.nome" :value="m.id">
              <span>{{ m.nome }}</span>
              <span class="opcao-extra">{{ m.especialidade }}</span>
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>

    <el-form-item label="Data e hora" prop="data" :error="errosApi.data">
      <el-date-picker
        v-model="modelo.data"
        type="datetime"
        format="DD/MM/YYYY HH:mm"
        value-format="YYYY-MM-DD[T]HH:mm:ss"
        placeholder="Selecione data e hora"
        :disabled-date="diaIndisponivel"
        :disabled-hours="horasIndisponiveis"
        :default-time="horaPadrao"
        :show-now="false"
      />
      <div class="texto-secundario">Atendimento de segunda a sexta, das 08h às 17h.</div>
    </el-form-item>

    <el-form-item class="botoes">
      <el-button type="primary" native-type="submit" :loading="salvando">{{ textoBotao }}</el-button>
      <el-button :disabled="salvando" @click="emit('cancelar')">Cancelar</el-button>
    </el-form-item>
  </el-form>
</template>

<style scoped>
.formulario {
  max-width: 720px;
}
.opcao-extra {
  float: right;
  margin-left: 16px;
  color: var(--el-text-color-secondary);
  font-size: 12px;
}
.botoes {
  margin-top: 8px;
  margin-bottom: 0;
}
</style>
