package com.cristianagoncalvesproject.ClinicaMedica.dto;

import java.time.LocalDateTime;

public record AgendamentoResponseDTO(
    Long id,
    LocalDateTime data,
    MedicoResponse medico,
    PacienteResponseDTO paciente
) {}