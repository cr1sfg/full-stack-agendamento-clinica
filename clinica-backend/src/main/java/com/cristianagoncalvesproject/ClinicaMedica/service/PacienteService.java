package com.cristianagoncalvesproject.ClinicaMedica.service;

import static com.cristianagoncalvesproject.ClinicaMedica.controller.dto.PacienteResponseDTO.fromPaciente;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cristianagoncalvesproject.ClinicaMedica.controller.dto.CriarPacienteRequestDTO;
import com.cristianagoncalvesproject.ClinicaMedica.controller.dto.PacienteResponseDTO;
import com.cristianagoncalvesproject.ClinicaMedica.exception.CpfJaCadastradoException;
import com.cristianagoncalvesproject.ClinicaMedica.exception.PacienteNaoEncontradoException;
import com.cristianagoncalvesproject.ClinicaMedica.model.Paciente;
import com.cristianagoncalvesproject.ClinicaMedica.repository.PacienteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository repository;

    public PacienteResponseDTO cadastrar(CriarPacienteRequestDTO request) {
        if (repository.existsByCpfIgnoreCase(request.getCpf())) {
            throw new CpfJaCadastradoException();
        }

        Paciente paciente = request.toModel();
        Paciente salvo = repository.save(paciente);
        return fromPaciente(salvo);
    }

    public List<PacienteResponseDTO> listarTodos() {
        return repository.findAll().stream()
                .map(PacienteResponseDTO::fromPaciente)
                .toList();
    }

    public PacienteResponseDTO buscarPorId(Long id) {
        Paciente paciente = repository.findById(id)
                .orElseThrow(PacienteNaoEncontradoException::new);
        return fromPaciente(paciente);
    }

    public PacienteResponseDTO atualizar(Long id, CriarPacienteRequestDTO request) {
        Paciente paciente = repository.findById(id)
                .orElseThrow(PacienteNaoEncontradoException::new);

        if (!request.getCpf().equalsIgnoreCase(paciente.getCpf())
            && repository.existsByCpfIgnoreCase(request.getCpf())) {
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
        return fromPaciente(atualizado);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new PacienteNaoEncontradoException();
        }
        repository.deleteById(id);
    }
}
