package com.cristianagoncalvesproject.ClinicaMedica.mapper;

import com.cristianagoncalvesproject.ClinicaMedica.dto.AgendamentoRequestDTO;
import com.cristianagoncalvesproject.ClinicaMedica.dto.AgendamentoResponseDTO;
import com.cristianagoncalvesproject.ClinicaMedica.dto.MedicoResponse;
import com.cristianagoncalvesproject.ClinicaMedica.dto.PacienteResponseDTO;
import com.cristianagoncalvesproject.ClinicaMedica.model.Agendamento;
import com.cristianagoncalvesproject.ClinicaMedica.model.Medico;
import com.cristianagoncalvesproject.ClinicaMedica.model.Paciente;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AgendamentoMapper {
    private final PacienteMapper pacienteMapper;
    private final MedicoMapper medicoMapper;

    public Agendamento toDomain(AgendamentoRequestDTO request, Medico medico, Paciente paciente) {
        Agendamento agendamento = new Agendamento();
        agendamento.setMedico(medico);
        agendamento.setPaciente(paciente);
        agendamento.setData(request.data());
        return agendamento;
    }

    public AgendamentoResponseDTO toResponseDTO(Agendamento agendamento) {
        Medico medico = agendamento.getMedico();
        Paciente paciente = agendamento.getPaciente();

        return new AgendamentoResponseDTO(
            agendamento.getId(),
            agendamento.getData(),
            medicoMapper.toResponse(medico),
            pacienteMapper.toResponseDTO(paciente)
        );
    }
}