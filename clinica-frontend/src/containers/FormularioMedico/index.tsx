import { useDispatch } from 'react-redux'
import { useNavigate } from 'react-router-dom'

import {ContainerFormulario, Titulo, FormularioPadrao, BotaoFormulario} from "../../styles/GlobalStyles"

import { FormEvent, useState } from 'react'
import { cadastrar } from '../../store/reducers/cadastroMedico'

const FormularioMedico = () => {
  const dispatch = useDispatch()
  const navigate = useNavigate()
  const [nome, setNome] = useState('')
  const [crm, setCrm] = useState('')
  const [especialidade, setEspecialidade] = useState('')
  const crmNumero = Number(crm)

  const cadastrarMedico = (evento: React.FormEvent<HTMLFormElement>) => {
    evento.preventDefault()
    const novoMedico = {nome, crm: crmNumero, especialidade}

    dispatch(cadastrar(novoMedico))
    setNome('')
    setCrm('')
    setEspecialidade('')
    navigate('/')
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