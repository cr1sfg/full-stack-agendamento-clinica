import api from './api'

const RECURSO = '/agendamentos'

export const agendamentoService = {
  async listar() {
    const { data } = await api.get(RECURSO)
    return data
  },

  async buscarPorId(id) {
    const { data } = await api.get(`${RECURSO}/${id}`)
    return data
  },

  /** Agendamentos de um médico (o backend não possui endpoint específico; filtra a listagem). */
  async listarPorMedico(medicoId) {
    const todos = await this.listar()
    return todos.filter((a) => a.medico?.id === Number(medicoId))
  },

  async listarPorPaciente(pacienteId) {
    const todos = await this.listar()
    return todos.filter((a) => a.paciente?.id === Number(pacienteId))
  },

  async cadastrar(agendamento) {
    const { data } = await api.post(RECURSO, agendamento)
    return data
  },

  async atualizar(id, agendamento) {
    const { data } = await api.put(`${RECURSO}/${id}`, agendamento)
    return data
  },

  async excluir(id) {
    await api.delete(`${RECURSO}/${id}`)
  },
}
