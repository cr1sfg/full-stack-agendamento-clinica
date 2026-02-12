package com.cristianagoncalvesproject.ClinicaMedica.service;

import com.cristianagoncalvesproject.ClinicaMedica.model.Medico;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class MedicoService {

    private List<Medico> medicos = new ArrayList<>();

    public boolean cadastrarMedico(Medico novoMedico) {
        for (Medico medico : medicos) {
            if (medico.getCrm().equalsIgnoreCase(novoMedico.getCrm())) {
                return false; 
            }
        }

        medicos.add(novoMedico);
        return true; 
    }

    public List<Medico> listarTodos() {
        return medicos;
    }
}

