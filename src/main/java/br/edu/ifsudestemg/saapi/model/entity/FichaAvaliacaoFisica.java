package br.edu.ifsudestemg.saapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class FichaAvaliacaoFisica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dataFicha;
    private Float peso;
    private Float altura;
    private String descricao;

    @ManyToOne
    private Cliente cliente;
    @ManyToOne
    private Funcionario funcionario;
}

