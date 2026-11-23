import { ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

/**
 * Fluxo padrão de exclusão: pede confirmação explícita (ElMessageBox), chama o service,
 * exibe o feedback (ElMessage) e executa `aoExcluir` em caso de sucesso.
 */
export function useExclusao(funcaoExcluir) {
  const excluindo = ref(false)

  async function excluir(item, { titulo, mensagem, sucesso, aoExcluir }) {
    try {
      await ElMessageBox.confirm(mensagem, titulo, {
        type: 'warning',
        confirmButtonText: 'Excluir',
        cancelButtonText: 'Cancelar',
        confirmButtonClass: 'el-button--danger',
      })
    } catch {
      return false // usuário cancelou
    }

    excluindo.value = true
    try {
      await funcaoExcluir(item.id)
      ElMessage.success(sucesso)
      await aoExcluir?.(item)
      return true
    } catch (e) {
      ElMessage.error({ message: e.message, showClose: true, duration: 6000 })
      return false
    } finally {
      excluindo.value = false
    }
  }

  return { excluir, excluindo }
}
