package br.edu.ifsudestemg.saapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Funcionario extends Pessoa{

    private String email;
    private String dataAdmissao;
    private String observacao;

    @ManyToOne
    private Cargo cargo;
}
