package com.cristianagoncalvesproject.ClinicaMedica.repository;

import com.cristianagoncalvesproject.ClinicaMedica.model.Medico;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Optional<Medico> findByCrmIgnoreCase(String crm);

    boolean existsByCrmIgnoreCase(String crm);

    boolean existsByCrmIgnoreCaseAndIdNot(String crm, Long id);
}