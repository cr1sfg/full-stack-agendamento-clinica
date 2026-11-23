import { useDispatch } from 'react-redux'
import { useNavigate } from 'react-router-dom'

import {ContainerFormulario, Titulo, FormularioPadrao, BotaoFormulario} from "../../styles/GlobalStyles"

import { FormEvent, useState } from 'react'
import { cadastrar } from '../../store/reducers/cadastroPaciente'

const FormularioPaciente = () => {
  const dispatch = useDispatch()
  const navigate = useNavigate()
  const [nome, setNome] = useState('')
  const [cpf, setCpf] = useState('')
  const [data_nascimento, setDataNascimento] = useState('')
  const cpfNumero = Number(cpf)

  const cadastrarPaciente = (evento: React.FormEvent<HTMLFormElement>) => {
    evento.preventDefault()
    const novoPaciente = {nome, cpf: cpfNumero, data_nascimento}
    
    

    dispatch(cadastrar(novoPaciente))
    setNome('')
    setCpf('')
    setDataNascimento('')
    navigate('/')
  }

  return (
    <ContainerFormulario>
        <FormularioPadrao onSubmit={cadastrarPaciente}>
            <Titulo>Cadastramento novo médico</Titulo>
            <input value={nome} onChange={(evento) => setNome(evento.target.value)} type="text" placeholder="Nome completo" required />
            <input value={cpf} onChange={(evento) => setCpf(evento.target.value)} type="number" placeholder="CPF" required/>
            <input value={data_nascimento} onChange={(evento) => setDataNascimento(evento.target.value)} type="date" placeholder="Data de nascimento"/>
        <BotaoFormulario type="submit">Cadastrar</BotaoFormulario>
        <BotaoFormulario type="submit" onClick={() => navigate('/')}>Cancelar</BotaoFormulario>
        </FormularioPadrao>
        
    </ContainerFormulario>
    )
}

export default FormularioPaciente