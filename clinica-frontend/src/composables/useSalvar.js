import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

/**
 * Fluxo padrão de envio de formulário (cadastro ou edição): controla o estado `salvando`,
 * mostra o feedback ao usuário, repassa ao formulário os erros de campo vindos da API
 * e navega para a tela de detalhe do registro salvo.
 */
export function useSalvar({ criar, atualizar, rotaDetalhe, mensagens }) {
  const router = useRouter()
  const salvando = ref(false)
  const errosServidor = ref({})

  async function salvar(id, dados) {
    salvando.value = true
    errosServidor.value = {}
    try {
      const salvo = id ? await atualizar(id, dados) : await criar(dados)
      ElMessage.success(id ? mensagens.atualizado : mensagens.criado)
      router.push({ name: rotaDetalhe, params: { id: salvo.id } })
    } catch (e) {
      errosServidor.value = e.fieldErrors ?? {}
      ElMessage.error({ message: e.message, showClose: true, duration: 6000 })
    } finally {
      salvando.value = false
    }
  }

  return { salvar, salvando, errosServidor }
}
