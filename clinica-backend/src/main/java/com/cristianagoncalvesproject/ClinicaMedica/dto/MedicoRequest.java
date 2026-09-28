package com.cristianagoncalvesproject.ClinicaMedica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MedicoRequest(
    @NotBlank(message = "CRM é obrigatório.")
    @Pattern(regexp = "(EME|300)*\\d{1,6}P*/\\w{2}", message = "CRM deve conter de 4 a 6 dígitos.")
    String crm,

    @NotBlank(message = "Nome é obrigatório.")
    String nome,

    @NotBlank(message = "Especialidade é obrigatória.")
    String especialidade
) {}