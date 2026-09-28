package com.cristianagoncalvesproject.ClinicaMedica.controller;

import com.cristianagoncalvesproject.ClinicaMedica.dto.AgendamentoRequestDTO;
import com.cristianagoncalvesproject.ClinicaMedica.dto.AgendamentoResponseDTO;
import com.cristianagoncalvesproject.ClinicaMedica.service.AgendamentoService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @GetMapping("/{id}")
    public AgendamentoResponseDTO buscarPorId(@PathVariable Long id) {
        return agendamentoService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<AgendamentoResponseDTO> cadastrar(@Valid @RequestBody AgendamentoRequestDTO request) {
        AgendamentoResponseDTO agendamento = agendamentoService.cadastrar(request);
        return ResponseEntity.created(URI.create("/agendamentos/" + agendamento.id())).body(agendamento);
    }
}