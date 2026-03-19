package br.edu.ifsudestemg.saapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public abstract class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cpf;
    private String telefone;
    private boolean status;
    private String logradouro;
    private Integer numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String dataNascimento;
}
