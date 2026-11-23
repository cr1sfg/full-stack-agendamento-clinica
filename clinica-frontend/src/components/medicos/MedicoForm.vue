<script setup>
import { reactive, ref } from 'vue'
import { criarRegras } from '@/utils/regras'
import { validarMedico } from '@/utils/validators'
import { useErrosApi } from '@/composables/useErrosApi'

/**
 * Formulário de médico, reaproveitado no cadastro e na edição.
 * Não conversa com a API: apenas valida e emite `enviar` com os dados prontos.
 */
const props = defineProps({
  valorInicial: { type: Object, default: () => ({}) },
  errosServidor: { type: Object, default: () => ({}) },
  salvando: { type: Boolean, default: false },
  textoBotao: { type: String, default: 'Salvar' },
})
const emit = defineEmits(['enviar', 'cancelar'])

const ESPECIALIDADES = [
  'Cardiologia', 'Clínica Geral', 'Dermatologia', 'Endocrinologia', 'Gastroenterologia', 'Ginecologia',
  'Neurologia', 'Oftalmologia', 'Ortopedia', 'Pediatria', 'Psiquiatria', 'Urologia',
]

const formRef = ref()
const modelo = reactive({ nome: '', crm: '', especialidade: '', ...props.valorInicial })
const regras = criarRegras(modelo, validarMedico, ['nome', 'crm', 'especialidade'])
const errosApi = useErrosApi(modelo, () => props.errosServidor)

async function enviar() {
  const valido = await formRef.value.validate().catch(() => false)
  if (!valido) return
  emit('enviar', {
    nome: modelo.nome.trim(),
    crm: modelo.crm.trim().toUpperCase(),
    especialidade: modelo.especialidade.trim(),
  })
}
</script>

<template>
  <el-form ref="formRef" :model="modelo" :rules="regras" label-position="top" class="formulario" @submit.prevent="enviar">
    <el-form-item label="Nome completo" prop="nome" :error="errosApi.nome">
      <el-input v-model="modelo.nome" placeholder="Ex.: Ana Souza" maxlength="255" />
    </el-form-item>

    <el-row :gutter="16">
      <el-col :xs="24" :sm="12">
        <el-form-item label="CRM" prop="crm" :error="errosApi.crm">
          <el-input v-model="modelo.crm" placeholder="12345/RS" @input="(v) => (modelo.crm = v.toUpperCase())" />
          <div class="texto-secundario">Número/UF, ex.: 12345/RS</div>
        </el-form-item>
      </el-col>
      <el-col :xs="24" :sm="12">
        <el-form-item label="Especialidade" prop="especialidade" :error="errosApi.especialidade">
          <el-select
            v-model="modelo.especialidade"
            filterable
            allow-create
            default-first-option
            placeholder="Selecione ou digite"
            style="width: 100%"
          >
            <el-option v-for="e in ESPECIALIDADES" :key="e" :label="e" :value="e" />
          </el-select>
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
