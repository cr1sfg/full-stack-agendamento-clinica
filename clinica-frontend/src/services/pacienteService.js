import api from './api'

const RECURSO = '/pacientes'

export const pacienteService = {
  async listar() {
    const { data } = await api.get(RECURSO)
    return data
  },

  async buscarPorId(id) {
    const { data } = await api.get(`${RECURSO}/${id}`)
    return data
  },

  async cadastrar(paciente) {
    const { data } = await api.post(RECURSO, paciente)
    return data
  },

  async atualizar(id, paciente) {
    const { data } = await api.put(`${RECURSO}/${id}`, paciente)
    return data
  },

  async excluir(id) {
    await api.delete(`${RECURSO}/${id}`)
  },
}
