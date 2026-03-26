package br.edu.ifsudestemg.saapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class EquipamentoDTO {
    private Long id;

    private String nome;
    private String modelo;
    private String ultManutencao;
    private Integer quantidade;
    private boolean disponivel;
    private String local;
    private String descricao;
}
