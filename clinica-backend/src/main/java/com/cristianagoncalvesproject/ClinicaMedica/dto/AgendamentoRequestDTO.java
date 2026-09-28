package com.cristianagoncalvesproject.ClinicaMedica.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record AgendamentoRequestDTO(
    @NotNull(message = "O id do médico é obrigatório.")
    Long medicoId,

    @NotNull(message = "O id do paciente é obrigatório.")
    Long pacienteId,

    @NotNull(message = "A data e hora do agendamento são obrigatórias.")
    LocalDateTime data
) {}