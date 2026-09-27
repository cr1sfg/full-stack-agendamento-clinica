package com.cristianagoncalvesproject.ClinicaMedica.dto;

import java.time.LocalDate;

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
}
