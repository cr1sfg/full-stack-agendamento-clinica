package com.cristianagoncalvesproject.ClinicaMedica.model;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Table(
    name = "medicos",
    uniqueConstraints = @UniqueConstraint(columnNames = "crm", name = "uk_medicos_numero_crm"),
    indexes = @Index(columnList = "crm", name = "idx_medicos_numero_crm")
)
@Data // (Lombok) Anotação para gerar getters, setters, toString, equals, hashCode e construtores
public class Medico {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    private String crm;

    private String nome;
    private String especialidade;
}
