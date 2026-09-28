package com.cristianagoncalvesproject.ClinicaMedica.service.agendamento;

import com.cristianagoncalvesproject.ClinicaMedica.dto.AgendamentoRequestDTO;
import com.cristianagoncalvesproject.ClinicaMedica.dto.AgendamentoResponseDTO;
import com.cristianagoncalvesproject.ClinicaMedica.mapper.AgendamentoMapper;
import com.cristianagoncalvesproject.ClinicaMedica.model.Agendamento;
import com.cristianagoncalvesproject.ClinicaMedica.model.Medico;
import com.cristianagoncalvesproject.ClinicaMedica.model.Paciente;
import com.cristianagoncalvesproject.ClinicaMedica.repository.AgendamentoRepository;
import com.cristianagoncalvesproject.ClinicaMedica.repository.MedicoRepository;
import com.cristianagoncalvesproject.ClinicaMedica.repository.PacienteRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final AgendamentoMapper agendamentoMapper;
    private final AgendamentoRegraService agendamentoRegraService;

    public AgendamentoService(
        AgendamentoRepository agendamentoRepository,
        MedicoRepository medicoRepository,
        PacienteRepository pacienteRepository,
        AgendamentoMapper agendamentoMapper,
        AgendamentoRegraService agendamentoRegraService
    ) {
        this.agendamentoRepository = agendamentoRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.agendamentoMapper = agendamentoMapper;
        this.agendamentoRegraService = agendamentoRegraService;
    }

    public AgendamentoResponseDTO cadastrar(AgendamentoRequestDTO request) {
        Medico medico = medicoRepository.findById(request.medicoId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médico não encontrado."));
        Paciente paciente = pacienteRepository.findById(request.pacienteId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado."));

        Agendamento agendamento = agendamentoMapper.toDomain(request, medico, paciente);
        agendamentoRegraService.validar(agendamento);
        return agendamentoMapper.toResponseDTO(agendamentoRepository.save(agendamento));
    }

    public AgendamentoResponseDTO buscarPorId(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agendamento não encontrado."));
        return agendamentoMapper.toResponseDTO(agendamento);
    }
}