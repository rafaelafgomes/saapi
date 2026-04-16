package br.edu.ifsudestemg.saapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor


public class Fatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dataVencimento;
    private String dataPagamento;
    private Float valor;

    @ManyToOne
    private Cliente cliente;
    @ManyToOne
    private FormaPagamento formaPagamento;

}
