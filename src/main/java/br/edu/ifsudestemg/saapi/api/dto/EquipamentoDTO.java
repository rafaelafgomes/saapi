package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.Equipamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

public class EquipamentoDTO {
    private Long id;

    private String nome;
    private String modelo;
    private String ultManutencao;
    private Integer quantidade;
    private boolean disponivel;
    private String local;
    private String descricao;

    public static EquipamentoDTO create (Equipamento equipamento) {
        ModelMapper modelMapper = new ModelMapper();
        EquipamentoDTO dto = modelMapper.map(equipamento, EquipamentoDTO.class);
        return dto;
    }
}
