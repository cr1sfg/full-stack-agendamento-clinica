package com.cristianagoncalvesproject.ClinicaMedica.service;

import java.util.List;

import com.cristianagoncalvesproject.ClinicaMedica.dto.MedicoRequest;
import com.cristianagoncalvesproject.ClinicaMedica.dto.MedicoResponse;
import com.cristianagoncalvesproject.ClinicaMedica.dto.MedicoUpdateRequest;
import com.cristianagoncalvesproject.ClinicaMedica.mapper.MedicoMapper;
import com.cristianagoncalvesproject.ClinicaMedica.model.Medico;
import com.cristianagoncalvesproject.ClinicaMedica.repository.MedicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MedicoService {
    private final MedicoRepository medicoRepository;
    private final MedicoMapper medicoMapper;

    public List<MedicoResponse> listarTodos() {
        return medicoRepository.findAll().stream()
            .map(medicoMapper::toResponse)
            .toList();
    }

    public MedicoResponse buscarPorId(Long id) {
        Medico medico = medicoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médico não encontrado."));
        return medicoMapper.toResponse(medico);
    }

    public MedicoResponse buscarPorCrm(String crm) {
        Medico medico = medicoRepository.findByCrmIgnoreCase(crm)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médico não encontrado."));
        return medicoMapper.toResponse(medico);
    }

    public MedicoResponse cadastrar(MedicoRequest request) {
        if (medicoRepository.existsByCrmIgnoreCase(request.crm())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um médico com este CRM.");
        }

        Medico medico = medicoRepository.save(medicoMapper.toDomain(request));
        return medicoMapper.toResponse(medico);
    }

    public MedicoResponse atualizar(MedicoUpdateRequest request) {
        Medico medico = medicoRepository.findById(request.id())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médico não encontrado."));

        if (medicoRepository.existsByCrmIgnoreCaseAndIdNot(request.crm(), request.id())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um médico com este CRM.");
        }

        medicoMapper.updateDomain(request, medico);
        return medicoMapper.toResponse(medicoRepository.save(medico));
    }

    public void excluir(Long id) {
        if (!medicoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Médico não encontrado.");
        }
        medicoRepository.deleteById(id);
    }
}

