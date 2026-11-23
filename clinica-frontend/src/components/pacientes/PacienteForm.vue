<script setup>
import { reactive, ref } from 'vue'
import { criarRegras } from '@/utils/regras'
import { validarPaciente } from '@/utils/validators'
import { mascararCpf, somenteDigitos } from '@/utils/formatters'
import { useErrosApi } from '@/composables/useErrosApi'

/** Formulário de paciente, reaproveitado no cadastro e na edição. */
const props = defineProps({
  valorInicial: { type: Object, default: () => ({}) },
  errosServidor: { type: Object, default: () => ({}) },
  salvando: { type: Boolean, default: false },
  textoBotao: { type: String, default: 'Salvar' },
})
const emit = defineEmits(['enviar', 'cancelar'])

const formRef = ref()
const modelo = reactive({ nome: '', cpf: '', dataNascimento: '', ...props.valorInicial })
modelo.cpf = mascararCpf(modelo.cpf)

const regras = criarRegras(modelo, validarPaciente, ['nome', 'cpf', 'dataNascimento'])
const errosApi = useErrosApi(modelo, () => props.errosServidor)

const dataFutura = (data) => data > new Date()

async function enviar() {
  const valido = await formRef.value.validate().catch(() => false)
  if (!valido) return
  emit('enviar', {
    nome: modelo.nome.trim(),
    // CPF sempre enviado só com dígitos, evitando duplicidades por formatação diferente
    cpf: somenteDigitos(modelo.cpf),
    dataNascimento: modelo.dataNascimento,
  })
}
</script>

<template>
  <el-form ref="formRef" :model="modelo" :rules="regras" label-position="top" class="formulario" @submit.prevent="enviar">
    <el-form-item label="Nome completo" prop="nome" :error="errosApi.nome">
      <el-input v-model="modelo.nome" placeholder="Ex.: João da Silva" maxlength="255" />
    </el-form-item>

    <el-row :gutter="16">
      <el-col :xs="24" :sm="12">
        <el-form-item label="CPF" prop="cpf" :error="errosApi.cpf">
          <el-input
            :model-value="modelo.cpf"
            placeholder="000.000.000-00"
            inputmode="numeric"
            maxlength="14"
            @input="(v) => (modelo.cpf = mascararCpf(v))"
          />
        </el-form-item>
      </el-col>
      <el-col :xs="24" :sm="12">
        <el-form-item label="Data de nascimento" prop="dataNascimento" :error="errosApi.dataNascimento">
          <el-date-picker
            v-model="modelo.dataNascimento"
            type="date"
            format="DD/MM/YYYY"
            value-format="YYYY-MM-DD"
            placeholder="dd/mm/aaaa"
            :disabled-date="dataFutura"
            style="width: 100%"
          />
        </el-form-item>
      </el-col>
    </el-row>

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
.botoes {
  margin-top: 8px;
  margin-bottom: 0;
}
</style>
