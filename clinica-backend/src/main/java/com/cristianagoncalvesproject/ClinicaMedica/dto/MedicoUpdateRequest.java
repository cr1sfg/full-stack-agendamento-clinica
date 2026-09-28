package com.cristianagoncalvesproject.ClinicaMedica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record MedicoUpdateRequest(
    @Positive(message = "Id deve ser positivo.")
    Long id,

    @Pattern(regexp = "\\d{4,6}", message = "CRM deve conter de 4 a 6 dígitos.")
    String crm,

    String nome,

    String especialidade
) {}