import { useNavigate } from 'react-router-dom'

import { Principal, Titulo, Botao } from "./style"


const ContainerPrincipal = () => {
    const navigate = useNavigate()

    return (
        <Principal>
            <Titulo>Bem vindo!</Titulo>
            <Botao type="button" onClick={() => navigate('/FormularioMedico')}>
                Cadastrar médico
            </Botao>
            <Botao onClick={() => navigate('/FormularioPaciente')}>
                Cadastrar paciente
            </Botao>
            <Botao>
                Agendar nova consulta
            </Botao>
            <Botao>
                Alterar agendamento
            </Botao>
            <Botao>
                Excluir agendamento
            </Botao>
            <Botao>
                Consultar agendamentos
            </Botao>
        </Principal>
    )
}

export default ContainerPrincipal