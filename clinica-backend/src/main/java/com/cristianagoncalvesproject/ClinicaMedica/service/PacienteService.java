package com.cristianagoncalvesproject.ClinicaMedica.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.cristianagoncalvesproject.ClinicaMedica.dto.AtualizarPacienteRequestDTO;
import com.cristianagoncalvesproject.ClinicaMedica.dto.CriarPacienteRequestDTO;
import com.cristianagoncalvesproject.ClinicaMedica.dto.PacienteResponseDTO;
import com.cristianagoncalvesproject.ClinicaMedica.exception.CpfJaCadastradoException;
import com.cristianagoncalvesproject.ClinicaMedica.exception.PacienteNaoEncontradoException;
import com.cristianagoncalvesproject.ClinicaMedica.mapper.PacienteMapper;
import com.cristianagoncalvesproject.ClinicaMedica.model.Paciente;
import com.cristianagoncalvesproject.ClinicaMedica.repository.PacienteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository repository;
    private final PacienteMapper pacienteMapper;

    public PacienteResponseDTO cadastrar(CriarPacienteRequestDTO request) {
        if (repository.existsByCpf(request.getCpf())) {
            throw new CpfJaCadastradoException();
        }

        Paciente paciente = pacienteMapper.fromRequestDTO(request);
        Paciente salvo = repository.save(paciente);
        return pacienteMapper.toResponseDTO(salvo);
    }

    public List<PacienteResponseDTO> listarTodos() {
        return repository.findAll().stream()
                .map(pacienteMapper::toResponseDTO)
                .toList();
    }

    public PacienteResponseDTO buscarPorId(Long id) {
        Paciente paciente = repository.findById(id)
                .orElseThrow(PacienteNaoEncontradoException::new);
        return pacienteMapper.toResponseDTO(paciente);
    }

    public PacienteResponseDTO atualizar(Long id, AtualizarPacienteRequestDTO request) {
        Paciente paciente = repository.findById(id)
                .orElseThrow(PacienteNaoEncontradoException::new);

        if (!request.getCpf().equalsIgnoreCase(paciente.getCpf())
            && repository.existsByCpf(request.getCpf())) {
            throw new CpfJaCadastradoException();
        }

        if (request.getNome() != null) {
            paciente.setNome(request.getNome());
        }
        if (request.getCpf() != null) {
            paciente.setCpf(request.getCpf());
        }
        if (request.getDataNascimento() != null) {
            paciente.setDataNascimento(request.getDataNascimento());
        }

        Paciente atualizado = repository.save(paciente);
        return pacienteMapper.toResponseDTO(atualizado);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new PacienteNaoEncontradoException();
        }
        repository.deleteById(id);
    }
}
