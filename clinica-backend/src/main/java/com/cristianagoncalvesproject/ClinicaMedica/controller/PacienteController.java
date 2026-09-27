package com.cristianagoncalvesproject.ClinicaMedica.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cristianagoncalvesproject.ClinicaMedica.controller.dto.CriarPacienteRequestDTO;
import com.cristianagoncalvesproject.ClinicaMedica.controller.dto.PacienteResponseDTO;
import com.cristianagoncalvesproject.ClinicaMedica.service.PacienteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PacienteController {
    private final PacienteService service;

    @PostMapping
    @ResponseStatus(CREATED)
    public PacienteResponseDTO cadastrar(@RequestBody @Valid CriarPacienteRequestDTO request) {
        return service.cadastrar(request);
    }

    @GetMapping
    public List<PacienteResponseDTO> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public PacienteResponseDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public PacienteResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid CriarPacienteRequestDTO request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
