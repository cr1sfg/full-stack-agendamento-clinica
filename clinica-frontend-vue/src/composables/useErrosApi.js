import { ref, watch } from 'vue'

/**
 * Mantém os erros de campo devolvidos pela API (exibidos via `:error` no el-form-item)
 * e os limpa assim que o usuário altera o formulário.
 */
export function useErrosApi(modelo, getErrosServidor) {
  const errosApi = ref({})
  watch(getErrosServidor, (erros) => {
    errosApi.value = { ...(erros ?? {}) }
  })
  watch(modelo, () => {
    errosApi.value = {}
  }, { deep: true })
  return errosApi
}
