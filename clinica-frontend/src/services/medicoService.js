import api from './api'

const RECURSO = '/medicos'

export const medicoService = {
  async listar() {
    const { data } = await api.get(RECURSO)
    return data
  },

  async buscarPorId(id) {
    const { data } = await api.get(`${RECURSO}/${id}`)
    return data
  },

  async cadastrar(medico) {
    const { data } = await api.post(RECURSO, medico)
    return data
  },

  /** O backend recebe o id no corpo da requisição (PUT /medicos). */
  async atualizar(id, medico) {
    const { data } = await api.put(RECURSO, { ...medico, id: Number(id) })
    return data
  },

  async excluir(id) {
    await api.delete(`${RECURSO}/${id}`)
  },
}
