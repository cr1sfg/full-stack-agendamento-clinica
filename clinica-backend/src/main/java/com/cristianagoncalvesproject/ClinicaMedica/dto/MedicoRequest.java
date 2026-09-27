package com.cristianagoncalvesproject.ClinicaMedica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MedicoRequest(
    @NotBlank(message = "CRM é obrigatório.")
    @Pattern(regexp = "\\d{4,6}", message = "CRM deve conter de 4 a 6 dígitos.")
    String crm,

    @NotBlank(message = "Nome é obrigatório.")
    String nome,

    @NotBlank(message = "Especialidade é obrigatória.")
    String especialidade
) {}