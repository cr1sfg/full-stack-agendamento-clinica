import { createRouter, createWebHistory } from 'vue-router'

import HomeView from '@/views/HomeView.vue'
import NotFoundView from '@/views/NotFoundView.vue'

/**
 * Cada entidade possui quatro rotas: listagem, cadastro, detalhe e edição.
 * Cadastro e edição apontam para a MESMA view de formulário — o parâmetro `id`
 * define se a tela está em modo de criação ou de edição.
 */
function rotasCrud({ caminho, nome, titulo, lista, formulario, detalhe }) {
  // `secao`/`acao` alimentam o breadcrumb do cabeçalho
  const secao = { secao: titulo, secaoRota: `${nome}-lista` }
  return [
    { path: `/${caminho}`, name: `${nome}-lista`, component: lista, meta: { titulo, ...secao } },
    {
      path: `/${caminho}/novo`,
      name: `${nome}-novo`,
      component: formulario,
      meta: { titulo: `Novo · ${titulo}`, ...secao, acao: 'Cadastro' },
    },
    {
      path: `/${caminho}/:id(\\d+)`,
      name: `${nome}-detalhe`,
      component: detalhe,
      props: true,
      meta: { titulo, ...secao, acao: 'Detalhes' },
    },
    {
      path: `/${caminho}/:id(\\d+)/editar`,
      name: `${nome}-editar`,
      component: formulario,
      props: true,
      meta: { titulo: `Editar · ${titulo}`, ...secao, acao: 'Edição' },
    },
  ]
}

const routes = [
  { path: '/', name: 'home', component: HomeView, meta: { titulo: 'Início' } },
  ...rotasCrud({
    caminho: 'agendamentos',
    nome: 'agendamento',
    titulo: 'Agendamentos',
    lista: () => import('@/views/agendamentos/AgendamentoListView.vue'),
    formulario: () => import('@/views/agendamentos/AgendamentoFormView.vue'),
    detalhe: () => import('@/views/agendamentos/AgendamentoDetailView.vue'),
  }),
  ...rotasCrud({
    caminho: 'medicos',
    nome: 'medico',
    titulo: 'Médicos',
    lista: () => import('@/views/medicos/MedicoListView.vue'),
    formulario: () => import('@/views/medicos/MedicoFormView.vue'),
    detalhe: () => import('@/views/medicos/MedicoDetailView.vue'),
  }),
  ...rotasCrud({
    caminho: 'pacientes',
    nome: 'paciente',
    titulo: 'Pacientes',
    lista: () => import('@/views/pacientes/PacienteListView.vue'),
    formulario: () => import('@/views/pacientes/PacienteFormView.vue'),
    detalhe: () => import('@/views/pacientes/PacienteDetailView.vue'),
  }),
  { path: '/:pathMatch(.*)*', name: 'nao-encontrado', component: NotFoundView, meta: { titulo: 'Página não encontrada' } },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
  scrollBehavior: () => ({ top: 0 }),
})

router.afterEach((to) => {
  document.title = to.meta.titulo ? `${to.meta.titulo} · Clínica Médica` : 'Clínica Médica'
})

export default router
