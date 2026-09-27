package com.cristianagoncalvesproject.ClinicaMedica.model;

import static jakarta.persistence.GenerationType.IDENTITY;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "pacientes")
@Entity
@Data
public class Paciente {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    private String cpf;

    private String nome;

    private LocalDate dataNascimento;
}
