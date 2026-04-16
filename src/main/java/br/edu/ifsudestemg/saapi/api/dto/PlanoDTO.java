package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.Plano;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PlanoDTO {

    private Long id;
    private String nome;
    private Integer duracao;
    private Float valor;
    private String descricao;

    public static PlanoDTO create(Plano plano) {
        ModelMapper modelMapper = new ModelMapper();
        PlanoDTO dto = modelMapper.map(plano, PlanoDTO.class);
        return dto;
    }
}
