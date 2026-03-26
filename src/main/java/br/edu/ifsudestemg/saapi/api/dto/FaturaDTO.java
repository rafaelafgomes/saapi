package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.FormaPagamento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class FaturaDTO {
    private Long id;

    private String dataVencimento;
    private String dataPagamento;
    private float valor;

    private Long idCliente;
    private Long idFormaPagamento;
}
