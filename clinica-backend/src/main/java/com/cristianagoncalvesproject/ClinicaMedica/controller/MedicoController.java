package com.cristianagoncalvesproject.ClinicaMedica.controller;

import com.cristianagoncalvesproject.ClinicaMedica.model.Medico;
import com.cristianagoncalvesproject.ClinicaMedica.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    // Endpoint para cadastrar um novo médico
    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrar(@RequestBody Medico medico) {
        boolean cadastrado = medicoService.cadastrarMedico(medico);

        if (cadastrado) {
            return ResponseEntity.ok("Médico cadastrado com sucesso!");
        } else {
            return ResponseEntity.badRequest().body("CRM já existente, cadastro não realizado.");
        }
    }

}
