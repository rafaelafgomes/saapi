package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.AjusteFinanceiro;
import br.edu.ifsudestemg.saapi.model.entity.Fatura;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ListaAjusteFinanceiroFaturaDTO {

    private Long id;
    private Long idAjusteFinanceiro;
    private Long idFatura;
}
