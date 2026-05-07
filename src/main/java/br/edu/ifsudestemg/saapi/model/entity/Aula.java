package br.edu.ifsudestemg.saapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String dataAula;
    private String horaAula;
    private Boolean status;
    private Integer capacidade;
    private String descricao;

    @ManyToOne
    private Funcionario funcionario;
}
