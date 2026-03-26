package br.edu.ifsudestemg.saapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AjusteFinanceiroDTO {

    private Long id;
    private String nome;
    private String tipo;
    private String unidade;
    private float valor;
    private String descricao;

}
