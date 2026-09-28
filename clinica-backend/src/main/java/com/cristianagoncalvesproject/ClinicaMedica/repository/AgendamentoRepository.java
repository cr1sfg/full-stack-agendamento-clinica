package com.cristianagoncalvesproject.ClinicaMedica.repository;

import com.cristianagoncalvesproject.ClinicaMedica.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    boolean existsByMedico_IdAndData(Long medicoId, java.time.LocalDateTime data);
}