import { computed, ref, unref, watch } from 'vue'
import { normalizarTexto } from '@/utils/formatters'

/**
 * Busca textual + filtros extras + ordenação + paginação aplicados no cliente sobre uma lista.
 */
export function useListaFiltrada(
  itens,
  { camposBusca = [], ordenacoes = {}, ordenacaoInicial = null, filtros, tamanhoPagina = 10 } = {},
) {
  const busca = ref('')
  const ordenacao = ref(ordenacaoInicial)
  const pagina = ref(1)

  const resultado = computed(() => {
    const termo = normalizarTexto(busca.value)
    const predicados = unref(filtros) ?? []

    const filtrados = (unref(itens) ?? []).filter((item) => {
      const casaBusca = !termo || camposBusca.some((campo) => normalizarTexto(campo(item)).includes(termo))
      return casaBusca && predicados.every((p) => p(item))
    })

    const chave = ordenacao.value?.order && ordenacoes[ordenacao.value.prop]
    if (!chave) return filtrados

    const fator = ordenacao.value.order === 'ascending' ? 1 : -1
    return [...filtrados].sort((a, b) => {
      const va = chave(a)
      const vb = chave(b)
      if (typeof va === 'string' || typeof vb === 'string') {
        return String(va ?? '').localeCompare(String(vb ?? ''), 'pt-BR', { sensitivity: 'base' }) * fator
      }
      return ((va ?? 0) - (vb ?? 0)) * fator
    })
  })

  const paginados = computed(() => {
    if (!tamanhoPagina) return resultado.value
    const inicio = (pagina.value - 1) * tamanhoPagina
    return resultado.value.slice(inicio, inicio + tamanhoPagina)
  })

  // Ao mudar busca/filtros, volta para a primeira página
  watch([busca, () => unref(filtros)], () => {
    pagina.value = 1
  })

  /** Handler do evento `sort-change` do el-table. */
  function ordenar({ prop, order }) {
    ordenacao.value = order ? { prop, order } : ordenacaoInicial
  }

  function limpar() {
    busca.value = ''
  }

  return { busca, ordenacao, pagina, tamanhoPagina, resultado, paginados, ordenar, limpar }
}
