package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.FormaPagamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class FormaPagamentoDTO {

    private Long id;
    private String nome;
    private String descricao;

    public static FormaPagamentoDTO create(FormaPagamento formaPagamento) {
        ModelMapper modelMapper = new ModelMapper();
        FormaPagamentoDTO dto = modelMapper.map(formaPagamento, FormaPagamentoDTO.class);
        return dto;
    }
}
