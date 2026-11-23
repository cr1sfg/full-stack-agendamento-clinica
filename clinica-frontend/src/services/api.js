import axios from 'axios'

/**
 * Cliente HTTP único da aplicação. Toda comunicação com o backend passa por aqui:
 * os componentes nunca chamam axios/fetch diretamente, apenas os services de cada entidade.
 *
 * A URL base vem da variável de ambiente VITE_API_BASE_URL (arquivos .env do Vite).
 */
const baseURL = import.meta.env.VITE_API_BASE_URL

if (!baseURL) {
  console.error(
    '[api] A variável de ambiente VITE_API_BASE_URL não foi definida. ' +
      'Crie o arquivo .env a partir de .env.example.',
  )
}

const api = axios.create({
  baseURL,
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' },
})

/** Erro normalizado da API, com mensagem pronta para exibir ao usuário. */
export class ApiError extends Error {
  constructor(message, { status = null, fieldErrors = {}, cause } = {}) {
    super(message, { cause })
    this.name = 'ApiError'
    this.status = status
    this.fieldErrors = fieldErrors
  }

  get isNotFound() {
    return this.status === 404
  }
}

const MENSAGENS_POR_STATUS = {
  400: 'Os dados enviados são inválidos.',
  404: 'Registro não encontrado.',
  409: 'A operação entrou em conflito com dados existentes.',
  500: 'Erro interno no servidor. Tente novamente mais tarde.',
}

function normalizarErro(error) {
  if (error.response) {
    const { status, data } = error.response
    const message =
      (data && typeof data === 'object' && (data.detail || data.message)) ||
      MENSAGENS_POR_STATUS[status] ||
      `Erro inesperado na comunicação com a API (HTTP ${status}).`
    return new ApiError(message, { status, fieldErrors: data?.errors ?? {}, cause: error })
  }

  if (error.code === 'ECONNABORTED') {
    return new ApiError('O servidor demorou demais para responder. Tente novamente.', { cause: error })
  }

  return new ApiError(
    'Não foi possível conectar ao servidor. Verifique se o backend está em execução.',
    { cause: error },
  )
}

api.interceptors.response.use(
  (response) => response,
  (error) => Promise.reject(normalizarErro(error)),
)

export default api
