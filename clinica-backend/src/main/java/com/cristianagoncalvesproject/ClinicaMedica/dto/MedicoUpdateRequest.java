package com.cristianagoncalvesproject.ClinicaMedica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record MedicoUpdateRequest(
    @NotNull(message = "Id é obrigatório.")
    @Positive(message = "Id deve ser positivo.")
    Long id,

    @NotBlank(message = "CRM é obrigatório.")
    @Pattern(regexp = "\\d{4,6}", message = "CRM deve conter de 4 a 6 dígitos.")
    String crm,

    @NotBlank(message = "Nome é obrigatório.")
    String nome,

    @NotBlank(message = "Especialidade é obrigatória.")
    String especialidade
) {}