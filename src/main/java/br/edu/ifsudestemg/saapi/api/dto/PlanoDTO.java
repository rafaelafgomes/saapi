package br.edu.ifsudestemg.saapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PlanoDTO {

    private Long id;
    private String nome;
    private Integer duracao;
    private float valor;
    private String descricao;
}
