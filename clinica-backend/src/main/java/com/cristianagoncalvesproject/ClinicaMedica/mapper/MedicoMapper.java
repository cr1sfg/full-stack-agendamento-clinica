package com.cristianagoncalvesproject.ClinicaMedica.mapper;

import com.cristianagoncalvesproject.ClinicaMedica.dto.MedicoRequest;
import com.cristianagoncalvesproject.ClinicaMedica.dto.MedicoResponse;
import com.cristianagoncalvesproject.ClinicaMedica.dto.MedicoUpdateRequest;
import com.cristianagoncalvesproject.ClinicaMedica.model.Medico;
import org.springframework.stereotype.Component;

@Component
public class MedicoMapper {

    public Medico toDomain(MedicoRequest request) {
        Medico medico = new Medico();
        medico.setCrm(request.crm());
        medico.setNome(request.nome());
        medico.setEspecialidade(request.especialidade());
        return medico;
    }

    public void updateDomain(MedicoUpdateRequest request, Medico medico) {
        medico.setCrm(request.crm());
        medico.setNome(request.nome());
        medico.setEspecialidade(request.especialidade());
    }

    public MedicoResponse toResponse(Medico medico) {
        return new MedicoResponse(
            medico.getId(),
            medico.getCrm(),
            medico.getNome(),
            medico.getEspecialidade()
        );
    }
}