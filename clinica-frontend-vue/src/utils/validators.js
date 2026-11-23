/**
 * Regras de validação dos formulários. Espelham as validações do backend (Bean Validation
 * e AgendamentoRegraService) para que o usuário receba o retorno antes de enviar.
 * Cada função recebe os dados do formulário e devolve um objeto { campo: mensagem }.
 */
import { parseDataLocal, somenteDigitos } from './formatters'

/** Mesmo padrão aceito por MedicoRequest no backend. */
export const CRM_REGEX = /^(EME|300)*\d{1,6}P*\/\w{2}$/

export function cpfValido(cpf) {
  const d = somenteDigitos(cpf)
  if (d.length !== 11 || /^(\d)\1{10}$/.test(d)) return false

  const digito = (base) => {
    const soma = base
      .split('')
      .reduce((acc, n, i) => acc + Number(n) * (base.length + 1 - i), 0)
    const resto = (soma * 10) % 11
    return resto === 10 ? 0 : resto
  }

  return digito(d.slice(0, 9)) === Number(d[9]) && digito(d.slice(0, 10)) === Number(d[10])
}

const vazio = (v) => v === null || v === undefined || String(v).trim() === ''

export function validarMedico(medico) {
  const erros = {}
  if (vazio(medico.nome)) erros.nome = 'Nome é obrigatório.'
  else if (medico.nome.trim().length < 3) erros.nome = 'Informe o nome completo.'

  if (vazio(medico.crm)) erros.crm = 'CRM é obrigatório.'
  else if (!CRM_REGEX.test(medico.crm.trim()))
    erros.crm = 'CRM deve estar no formato correto (ex: 12345/RS).'

  if (vazio(medico.especialidade)) erros.especialidade = 'Especialidade é obrigatória.'
  return erros
}

export function validarPaciente(paciente) {
  const erros = {}
  if (vazio(paciente.nome)) erros.nome = 'O nome é obrigatório.'
  else if (paciente.nome.trim().length < 3) erros.nome = 'Informe o nome completo.'

  if (vazio(paciente.cpf)) erros.cpf = 'O CPF é obrigatório.'
  else if (!cpfValido(paciente.cpf)) erros.cpf = 'CPF informado é inválido.'

  if (vazio(paciente.dataNascimento)) erros.dataNascimento = 'A data de nascimento é obrigatória.'
  else if (parseDataLocal(paciente.dataNascimento) > new Date())
    erros.dataNascimento = 'A data de nascimento não pode ser no futuro.'
  return erros
}

export function validarAgendamento(agendamento) {
  const erros = {}
  if (vazio(agendamento.medicoId)) erros.medicoId = 'Selecione o médico.'
  if (vazio(agendamento.pacienteId)) erros.pacienteId = 'Selecione o paciente.'

  if (vazio(agendamento.data)) {
    erros.data = 'A data e hora do agendamento são obrigatórias.'
  } else {
    const data = parseDataLocal(agendamento.data)
    const diaSemana = data.getDay()
    if (data <= new Date()) erros.data = 'O agendamento deve ser para uma data futura.'
    else if (diaSemana === 0 || diaSemana === 6)
      erros.data = 'Agendamentos não são permitidos aos finais de semana.'
    else if (data.getHours() < 8 || data.getHours() > 17)
      erros.data = 'O horário de atendimento é das 08h às 17h.'
  }
  return erros
}
