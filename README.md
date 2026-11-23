# Sistema de Agendamento de Clínica Médica

Trabalho do **Grau B — Desenvolvimento do Frontend**, disciplina de **Implementação de Software** (Unisinos, 2026/2).

## Integrantes

- Bruno Schmidt, RA 1833303
- Cristiana Gonçalves, RA 1967675


## Vídeo de apresentação

<!-- TODO: substituir pelo link público do vídeo (ou incluir o arquivo .mp4 na raiz do .zip) -->
🎬 **Link do vídeo:** _a definir_

---

## O problema e o domínio

Clínicas médicas precisam organizar a agenda dos profissionais e evitar conflitos de horário. O sistema
modela esse domínio com três entidades, expostas como API REST pelo backend do Grau A:

| Entidade        | Atributos                                    | Regras principais                                                                                   |
| --------------- | -------------------------------------------- | --------------------------------------------------------------------------------------------------- |
| **Médico**      | nome, CRM, especialidade                     | CRM obrigatório, único e no formato `12345/UF`                                                       |
| **Paciente**    | nome, CPF, data de nascimento                | CPF válido e único; nascimento não pode ser no futuro                                                |
| **Agendamento** | data/hora, médico (N:1), paciente (N:1)      | data futura, dias úteis, das 08h às 17h, sem dois agendamentos do mesmo médico no mesmo horário |

Médicos e pacientes com agendamentos vinculados não podem ser excluídos (a API responde `409 Conflict`).

## Estrutura do repositório

```
.
├── clinica-backend/       # API REST — Spring Boot 4 + JPA + Flyway + PostgreSQL (Grau A, atualizado)
├── clinica-frontend/  # Interface web — Vue 3 + Vite + Vue Router + Element Plus (entrega do Grau B)
└── collection/            # Coleção de requisições (Bruno) para testar a API manualmente
```

---

## Executando o backend

**Pré-requisitos:** JDK 25 e um PostgreSQL acessível (Docker é opcional, para subir o banco localmente).

1. **Banco de dados.** O `application.properties` aponta para o banco hospedado do grupo. Para usar um banco local:
   ```bash
   cd clinica-backend
   docker compose up -d
   ```
   e, em `src/main/resources/application.properties`, comente o bloco "Banco hospedado" e descomente o bloco "Banco local".
   As tabelas são criadas automaticamente pelo Flyway (`src/main/resources/db/migration`).

2. **Subir a API** (porta `8001`):
   ```bash
   cd clinica-backend
   ./mvnw spring-boot:run
   ```
   No Windows: `mvnw.cmd spring-boot:run`.

3. A documentação interativa (Swagger UI) fica em <http://localhost:8001/swagger-ui.html>.

### Endpoints

| Método | Rota                  | Descrição                         |
| ------ | --------------------- | --------------------------------- |
| GET    | `/medicos`            | Lista médicos                     |
| GET    | `/medicos/{id}`       | Busca médico por id               |
| GET    | `/medicos/crm/{crm}`  | Busca médico por CRM (`12345-RS`) |
| POST   | `/medicos`            | Cadastra médico                   |
| PUT    | `/medicos`            | Atualiza médico (id no corpo)     |
| DELETE | `/medicos/{id}`       | Exclui médico                     |
| GET    | `/pacientes`          | Lista pacientes                   |
| GET    | `/pacientes/{id}`     | Busca paciente por id             |
| POST   | `/pacientes`          | Cadastra paciente                 |
| PUT    | `/pacientes/{id}`     | Atualiza paciente                 |
| DELETE | `/pacientes/{id}`     | Exclui paciente                   |
| GET    | `/agendamentos`       | Lista agendamentos                |
| GET    | `/agendamentos/{id}`  | Busca agendamento por id          |
| POST   | `/agendamentos`       | Cadastra agendamento              |
| PUT    | `/agendamentos/{id}`  | Atualiza agendamento              |
| DELETE | `/agendamentos/{id}`  | Exclui agendamento                |

### Alterações no backend para o Grau B

- **CORS** liberado para o servidor de desenvolvimento do frontend (`config/CorsConfig.java`). As origens
  permitidas vêm da propriedade `app.cors.allowed-origins` (padrão: `http://localhost:5173` e
  `http://127.0.0.1:5173`) e podem ser trocadas pela variável de ambiente `CORS_ALLOWED_ORIGINS`.
- **Erros padronizados** em `ProblemDetail` com mensagem legível em `detail` e, nos erros de
  validação, um mapa `errors` campo → mensagem (`exception/GlobalExceptionHandler.java`). O frontend exibe
  essas mensagens ao usuário.
- **Edição e exclusão de agendamentos** (`PUT` e `DELETE /agendamentos/{id}`), para completar o CRUD da
  entidade. A regra de conflito de horário ignora o próprio agendamento durante a edição.
- **Exclusão protegida:** médico ou paciente com agendamentos retorna `409` com mensagem explicativa, em vez de erro 500.

---

## Executando o frontend

**Pré-requisitos:** Node.js 18.18 ou superior (com npm). O backend precisa estar rodando.

```bash
cd clinica-frontend-vue
cp .env.example .env      # opcional: .env.development já aponta para http://localhost:8001
npm install
npm run dev
```

Acesse <http://localhost:5173>. Para gerar a versão de produção: `npm run build` (saída em `dist/`).

### Variável de ambiente

| Variável            | Exemplo                 | Uso                                              |
| ------------------- | ----------------------- | ------------------------------------------------ |
| `VITE_API_BASE_URL` | `http://localhost:8001` | URL base da API REST, lida em `src/services/api.js` |

Nenhuma URL do backend fica fixa no código: o cliente HTTP lê `import.meta.env.VITE_API_BASE_URL`.

### Rotas do frontend

| Rota                         | Tela                     | Finalidade                                                                                       |
| ---------------------------- | ------------------------ | ------------------------------------------------------------------------------------------------ |
| `/`                          | Painel                   | Totais (consultas hoje, próximas consultas, médicos, pacientes) e lista das próximas consultas     |
| `/agendamentos`              | Lista de agendamentos    | Busca (paciente, CPF, médico), filtros por médico e período, ordenação por data/paciente/médico |
| `/agendamentos/novo`         | Formulário de agendamento | Cadastro; aceita `?medicoId=` e `?pacienteId=` para pré-selecionar                                |
| `/agendamentos/:id`          | Detalhe do agendamento   | Dados da consulta com paciente e médico relacionados; editar/excluir                             |
| `/agendamentos/:id/editar`   | Formulário de agendamento | Edição (mesmo formulário do cadastro)                                                            |
| `/medicos`                   | Lista de médicos         | Busca por nome/CRM, filtro por especialidade, ordenação por nome/CRM/especialidade               |
| `/medicos/novo`              | Formulário de médico     | Cadastro                                                                                         |
| `/medicos/:id`               | Detalhe do médico        | Dados do médico e sua agenda (agendamentos relacionados)                                         |
| `/medicos/:id/editar`        | Formulário de médico     | Edição (mesmo formulário do cadastro)                                                            |
| `/pacientes`                 | Lista de pacientes       | Busca por nome/CPF, ordenação por nome ou data de nascimento                                     |
| `/pacientes/novo`            | Formulário de paciente   | Cadastro                                                                                         |
| `/pacientes/:id`             | Detalhe do paciente      | Dados do paciente e suas consultas (agendamentos relacionados)                                   |
| `/pacientes/:id/editar`      | Formulário de paciente   | Edição (mesmo formulário do cadastro)                                                            |
| qualquer outra               | Página não encontrada    | Página 404 com link para o início                                                                 |

### Arquitetura do frontend

A interface usa a biblioteca de componentes **Element Plus** (layout com menu lateral, tabelas, formulários,
seletores de data, mensagens e diálogos), com o idioma configurado para português (`pt-br`).

```
clinica-frontend/src
├── assets/            # logo e estilos globais (styles/main.css: layout e utilitários)
├── components/
│   ├── layout/        # AppSidebar (menu lateral) e AppHeader (breadcrumb + recolher menu)
│   ├── ui/            # componentes reutilizáveis: AsyncContent (carregando/erro/vazio/sucesso),
│   │                  #   ListToolbar (busca e filtros), PageHeader, RowActions (editar/excluir)
│   ├── medicos/       # MedicoForm, MedicoTable
│   ├── pacientes/     # PacienteForm, PacienteTable
│   └── agendamentos/  # AgendamentoForm, AgendamentoTable, AgendamentoStatus
├── composables/       # lógica reaproveitável: useAsyncData, useListaFiltrada (busca, filtros,
│                      #   ordenação e paginação), useSalvar, useExclusao, useErrosApi
├── router/            # definição das rotas (Vue Router)
├── services/          # camada HTTP: api.js (axios + tratamento de erros) e um service por entidade
├── utils/             # formatadores (datas, CPF), validadores (CPF, CRM, regras de agendamento)
│                      #   e regras.js (converte os validadores em regras do el-form)
├── views/             # telas ligadas às rotas (lista, formulário e detalhe de cada entidade)
├── App.vue            # layout base: menu lateral + cabeçalho + área de conteúdo
└── main.js            # registra Element Plus (locale pt-br) e o router
```

- **Views** (telas) orquestram: chamam os **services**, controlam os estados e repassam dados aos componentes.
- **Componentes de entidade** (formulários e tabelas) não conhecem a API: recebem props e emitem eventos.
- **Services** concentram toda a comunicação HTTP. `api.js` cria a instância do axios com a URL base da
  variável de ambiente e converte qualquer falha em um `ApiError` com mensagem amigável (inclusive
  "servidor fora do ar" e timeout).
- **Estados de tela:** toda listagem passa pelo `AsyncContent`, que exibe carregando (`el-skeleton`), erro
  (`el-result` com botão "Tentar novamente"), lista vazia (`el-empty` com atalho para cadastrar) ou o
  conteúdo. Há também um estado específico de "nenhum resultado para os filtros".
- **Listagens:** busca e filtros na barra acima da tabela; ordenação clicando no cabeçalho das colunas;
  paginação de 10 em 10.
- **Formulários:** `el-form` com regras geradas a partir de `utils/validators.js`, que espelham as regras do
  backend (campos obrigatórios, CPF com dígito verificador, formato do CRM, data futura em dia útil das 08h
  às 17h; o seletor de data já bloqueia fins de semana, datas passadas e horários fora do expediente).
  Erros de campo devolvidos pela API aparecem sob o campo correspondente.
- **Feedback:** `ElMessage` para sucesso e erro em todas as operações e `ElMessageBox` para a confirmação
  explícita antes de qualquer exclusão.
- **Estilização:** tema padrão do Element Plus e um único `main.css` com o layout geral, além de
  `<style scoped>` nos componentes. O menu lateral recolhe em telas pequenas.

### Bibliotecas utilizadas no frontend

| Biblioteca                    | Justificativa                                                                                     |
| ----------------------------- | ------------------------------------------------------------------------------------------------- |
| `vue` 3                       | Framework da interface (Composition API com `<script setup>`)                                   |
| `vue-router` 4                | Navegação entre telas (requisito do trabalho)                                                     |
| `element-plus`                | Biblioteca de componentes UI para ter uma base sólida de design.                                 |
| `@element-plus/icons-vue`     | Ícones do Element Plus                                     |
| `axios`                       | Maior costume com a biblioteca em contrapartida com o Fetch API nativo.                          |
| `vite` + `@vitejs/plugin-vue` | Servidor de desenvolvimento, build e leitura das variáveis de ambiente (`.env`)                   |
