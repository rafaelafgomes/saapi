package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.AjusteFinanceiro;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

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

    public static AjusteFinanceiroDTO create(AjusteFinanceiro ajusteFinanceiro) {
        ModelMapper modelMapper = new ModelMapper();
        AjusteFinanceiroDTO dto = modelMapper.map(ajusteFinanceiro, AjusteFinanceiroDTO.class);
        return dto;
    }
}
