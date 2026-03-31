package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.AjusteFinanceiro;
import br.edu.ifsudestemg.saapi.model.entity.Fatura;
import br.edu.ifsudestemg.saapi.model.entity.ListaAjusteFinanceiroFatura;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.persistence.ManyToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ListaAjusteFinanceiroFaturaDTO {

    private Long id;
    private Long idAjusteFinanceiro;
    private String nomeAjusteFinanceiro;
    private Long idFatura;
    private String nomeFatura;

    public static ListaAjusteFinanceiroFaturaDTO create(ListaAjusteFinanceiroFatura listaAjusteFinanceiroFatura) {
        ModelMapper modelMapper = new ModelMapper();
        ListaAjusteFinanceiroFaturaDTO dto = modelMapper.map(listaAjusteFinanceiroFatura, ListaAjusteFinanceiroFaturaDTO.class);
        dto.nomeAjusteFinanceiro = listaAjusteFinanceiroFatura.getAjusteFinanceiro().getNome();
        dto.nomeFatura = listaAjusteFinanceiroFatura.getFatura().getCliente().getNome();
        return dto;
    }
}
