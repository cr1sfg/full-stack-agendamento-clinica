package com.cristianagoncalvesproject.ClinicaMedica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record MedicoUpdateRequest(
    @Positive(message = "Id deve ser positivo.")
    Long id,

    @Pattern(regexp = "(EME|300)*\\d{1,6}P*/\\w{2}", message = "CRM deve estar no formato correto (ex: 12345/RS).")
    String crm,

    String nome,

    String especialidade
) {}