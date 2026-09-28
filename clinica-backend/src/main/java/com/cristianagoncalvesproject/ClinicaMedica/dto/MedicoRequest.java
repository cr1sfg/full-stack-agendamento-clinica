package com.cristianagoncalvesproject.ClinicaMedica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MedicoRequest(
    @NotBlank(message = "CRM é obrigatório.")
    @Pattern(regexp = "(EME|300)*\\d{1,6}P*/\\w{2}", message = "CRM deve estar no formato correto (ex: 12345/RS).")
    String crm,

    @NotBlank(message = "Nome é obrigatório.")
    String nome,

    @NotBlank(message = "Especialidade é obrigatória.")
    String especialidade
) {}