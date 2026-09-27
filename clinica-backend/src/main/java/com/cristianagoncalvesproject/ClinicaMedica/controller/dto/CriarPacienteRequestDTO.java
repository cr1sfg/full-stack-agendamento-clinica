package com.cristianagoncalvesproject.ClinicaMedica.controller.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import com.cristianagoncalvesproject.ClinicaMedica.model.Paciente;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriarPacienteRequestDTO {
    @NotBlank(message = "O CPF é obrigatório.")
    @CPF(message = "CPF informado é inválido.")
    private String cpf;

    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    @NotNull(message = "A data de nascimento é obrigatória.")
    @PastOrPresent(message = "A data de nascimento não pode ser no futuro.")
    private LocalDate dataNascimento;

    @JsonIgnore
    public Paciente toModel() {
        Paciente paciente = new Paciente();
        paciente.setCpf(this.getCpf());
        paciente.setNome(this.getNome());
        paciente.setDataNascimento(this.getDataNascimento());
        return paciente;
    }
}
