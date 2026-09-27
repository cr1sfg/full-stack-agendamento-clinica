package com.cristianagoncalvesproject.ClinicaMedica.controller;

import com.cristianagoncalvesproject.ClinicaMedica.dto.MedicoRequest;
import com.cristianagoncalvesproject.ClinicaMedica.dto.MedicoResponse;
import com.cristianagoncalvesproject.ClinicaMedica.dto.MedicoUpdateRequest;
import com.cristianagoncalvesproject.ClinicaMedica.service.MedicoService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @GetMapping("/{id}")
    public MedicoResponse buscarPorId(@PathVariable Long id) {
        return medicoService.buscarPorId(id);
    }

    @GetMapping("/crm/{crm}")
    public MedicoResponse buscarPorCrm(@PathVariable String crm) {
        return medicoService.buscarPorCrm(crm);
    }

    @PostMapping
    public ResponseEntity<MedicoResponse> cadastrar(@Valid @RequestBody MedicoRequest request) {
        MedicoResponse medico = medicoService.cadastrar(request);
        return ResponseEntity.created(URI.create("/medicos/crm/" + medico.crm())).body(medico);
    }

    @PutMapping
    public MedicoResponse atualizar(@Valid @RequestBody MedicoUpdateRequest request) {
        return medicoService.atualizar(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        medicoService.excluir(id);
    }

}
