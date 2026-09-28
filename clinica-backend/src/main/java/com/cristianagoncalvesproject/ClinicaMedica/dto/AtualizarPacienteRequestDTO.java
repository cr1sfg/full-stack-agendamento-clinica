package com.cristianagoncalvesproject.ClinicaMedica.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarPacienteRequestDTO {
    @CPF(message = "CPF informado é inválido.")
    private String cpf;

    private String nome;

    @PastOrPresent(message = "A data de nascimento não pode ser no futuro.")
    private LocalDate dataNascimento;
}
