/** Funções puras de formatação usadas pelas telas. */

export function somenteDigitos(valor = '') {
  return String(valor ?? '').replace(/\D/g, '')
}

export function formatarCpf(cpf) {
  const d = somenteDigitos(cpf)
  if (d.length !== 11) return cpf ?? ''
  return d.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4')
}

/** Máscara progressiva para o campo de CPF enquanto o usuário digita. */
export function mascararCpf(valor) {
  const d = somenteDigitos(valor).slice(0, 11)
  return d
    .replace(/^(\d{3})(\d)/, '$1.$2')
    .replace(/^(\d{3})\.(\d{3})(\d)/, '$1.$2.$3')
    .replace(/\.(\d{3})(\d{1,2})$/, '.$1-$2')
}

/**
 * Converte "AAAA-MM-DD" ou "AAAA-MM-DDTHH:mm[:ss]" (sem fuso, como o backend envia)
 * em Date no horário local.
 */
export function parseDataLocal(valor) {
  if (!valor) return null
  const [data, hora = '00:00:00'] = String(valor).split('T')
  const [ano, mes, dia] = data.split('-').map(Number)
  const [h = 0, m = 0, s = 0] = hora.split(':').map((p) => Number.parseInt(p, 10))
  return new Date(ano, mes - 1, dia, h, m, s)
}

const fmtData = new Intl.DateTimeFormat('pt-BR', { day: '2-digit', month: '2-digit', year: 'numeric' })
const fmtHora = new Intl.DateTimeFormat('pt-BR', { hour: '2-digit', minute: '2-digit' })
const fmtDiaSemana = new Intl.DateTimeFormat('pt-BR', { weekday: 'short' })

export function formatarData(valor) {
  const d = parseDataLocal(valor)
  return d ? fmtData.format(d) : '-'
}

export function formatarDataHora(valor) {
  const d = parseDataLocal(valor)
  return d ? `${fmtData.format(d)} às ${fmtHora.format(d)}` : '-'
}

export function formatarHora(valor) {
  const d = parseDataLocal(valor)
  return d ? fmtHora.format(d) : '-'
}

export function formatarDiaSemana(valor) {
  const d = parseDataLocal(valor)
  return d ? fmtDiaSemana.format(d).replace('.', '') : ''
}

export function calcularIdade(dataNascimento) {
  const nasc = parseDataLocal(dataNascimento)
  if (!nasc) return null
  const hoje = new Date()
  let idade = hoje.getFullYear() - nasc.getFullYear()
  const fezAniversario =
    hoje.getMonth() > nasc.getMonth() ||
    (hoje.getMonth() === nasc.getMonth() && hoje.getDate() >= nasc.getDate())
  if (!fezAniversario) idade -= 1
  return idade
}

/** Data de hoje no formato aceito por <input type="date">. */
export function hojeIso() {
  const d = new Date()
  const pad = (n) => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}`
}

/** "AAAA-MM-DDTHH:mm:ss" → "AAAA-MM-DDTHH:mm" para <input type="datetime-local">. */
export function paraInputDataHora(valor) {
  return valor ? String(valor).slice(0, 16) : ''
}

/** Normaliza texto para buscas sem diferenciar maiúsculas e acentos. */
export function normalizarTexto(valor) {
  return String(valor ?? '')
    .normalize('NFD')
    .replace(/[̀-ͯ]/g, '')
    .toLowerCase()
    .trim()
}
