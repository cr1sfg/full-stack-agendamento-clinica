import { ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

/**
 * Exclusão de itens com confirmação do usuário.
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
