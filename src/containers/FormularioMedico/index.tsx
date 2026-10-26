import { useState, FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'

import {ContainerFormulario, Titulo, FormularioPadrao, BotaoFormulario} from "../../styles/GlobalStyles"
import api from '../../services/api'

const FormularioMedico = () => {
  const navigate = useNavigate()
  const [nome, setNome] = useState('')
  const [crm, setCrm] = useState('')
  const [especialidade, setEspecialidade] = useState('')
  const crmNumero = Number(crm)

  const cadastrarMedico = async (evento: React.FormEvent<HTMLFormElement>) => {
    evento.preventDefault()
    const novoMedico = {nome, crm: crmNumero, especialidade}

    try {
      await api.post("/medicos/cadastro", novoMedico) // chamada ao back-end
      alert("Médico cadastrado com sucesso!")
      setNome('')
      setCrm('')
      setEspecialidade('')
      navigate('/')
    } catch (error: any) {
      if (error.response) {
        // O servidor respondeu com um status fora da faixa 2xx
        console.log("Status:", error.response.status);
        console.log("Dados:", error.response.data);
        alert(error.response.data)
      } else if (error.request) {
        // A requisição foi feita, mas não houve resposta
        console.log("Sem resposta do servidor:", error.request);
        alert(error.response.request)
      } else {
        // Algo deu errado na configuração da requisição
        console.log("Erro:", error.message);
        alert("Erro para cadastrar novo médico")
      }
    }
  }

  return (
    <ContainerFormulario>
        <FormularioPadrao onSubmit={cadastrarMedico}>
            <Titulo>Cadastramento novo médico</Titulo>
            <input value={nome} onChange={(evento) => setNome(evento.target.value)} type="text" placeholder="Nome completo" required />
            <input value={crm} onChange={(evento) => setCrm(evento.target.value)} type="number" placeholder="CRM" required/>
            <input value={especialidade} onChange={(evento) => setEspecialidade(evento.target.value)} type="text" placeholder="Especialidade"/>
        <BotaoFormulario type="submit">Cadastrar</BotaoFormulario>
        <BotaoFormulario type="submit" onClick={() => navigate('/')}>Cancelar</BotaoFormulario>
        </FormularioPadrao>
        
    </ContainerFormulario>
    )
}

export default FormularioMedico

/*
editar: (state, action: PayloadAction<Medico>) => {
      const medicoJaExiste = state.itens.find(
        (medico) =>
          medico.crm === action.payload.crm &&
          medico.crm !== action.payload.crm
      )
      if (medicoJaExiste) {
        alert('Médico já cadastrado')
        return
      }
      const idx = state.itens.findIndex((c) => c.crm === action.payload.crm)
      if (idx !== -1) {
        state.itens[idx] = action.payload
      }
*/