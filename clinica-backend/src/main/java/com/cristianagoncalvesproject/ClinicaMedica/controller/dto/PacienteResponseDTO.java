package com.cristianagoncalvesproject.ClinicaMedica.controller.dto;

import java.time.LocalDate;

import com.cristianagoncalvesproject.ClinicaMedica.model.Paciente;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacienteResponseDTO {
    private Long id;
    private String cpf;
    private String nome;
    private LocalDate dataNascimento;

    public static PacienteResponseDTO fromPaciente(Paciente paciente) {
        return PacienteResponseDTO.builder()
            .id(paciente.getId())
            .cpf(paciente.getCpf())
            .nome(paciente.getNome())
            .dataNascimento(paciente.getDataNascimento())
            .build();
    }
}
