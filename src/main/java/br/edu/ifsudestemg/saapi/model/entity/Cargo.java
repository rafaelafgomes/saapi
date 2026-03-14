package br.edu.ifsudestemg.saapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GeneratedType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Cargo {

    @Id
    @GeneratedValue(strategy = Generation.Tyoe.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;
}
