import { ref, shallowRef } from 'vue'

/**
 * Executa uma função assíncrona (geralmente um método de service) e expõe
 * os estados de carregamento, sucesso (data) e erro, além de `recarregar`.
 */
export function useAsyncData(fn, { imediato = true, valorInicial = null } = {}) {
  const data = shallowRef(valorInicial)
  const carregando = ref(false)
  const erro = ref(null)

  async function recarregar(...args) {
    carregando.value = true
    erro.value = null
    try {
      data.value = await fn(...args)
    } catch (e) {
      erro.value = e
    } finally {
      carregando.value = false
    }
    return data.value
  }

  if (imediato) recarregar()

  return { data, carregando, erro, recarregar }
}
