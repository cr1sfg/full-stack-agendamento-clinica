package com.cristianagoncalvesproject.ClinicaMedica.mapper;

import org.springframework.stereotype.Component;

import com.cristianagoncalvesproject.ClinicaMedica.dto.CriarPacienteRequestDTO;
import com.cristianagoncalvesproject.ClinicaMedica.dto.PacienteResponseDTO;
import com.cristianagoncalvesproject.ClinicaMedica.model.Paciente;

@Component
public class PacienteMapper {
    public Paciente fromRequestDTO(CriarPacienteRequestDTO dto) {
        Paciente paciente = new Paciente();
        paciente.setCpf(dto.getCpf());
        paciente.setNome(dto.getNome());
        paciente.setDataNascimento(dto.getDataNascimento());
        return paciente;
    }

    public PacienteResponseDTO toResponseDTO(Paciente paciente) {
        return PacienteResponseDTO.builder()
            .id(paciente.getId())
            .cpf(paciente.getCpf())
            .nome(paciente.getNome())
            .dataNascimento(paciente.getDataNascimento())
            .build();
    }
}
