package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.entity.Equipamento;
import br.edu.ifsudestemg.saapi.model.entity.ListaEquipamentoAula;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.persistence.ManyToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ListaEquipamentoAulaDTO{

private Long id;
private Integer quantidade;
private Long idEquipamento;
private String nomeEquipamento;
private Long idAula;
private String nomeAula;

    public static ListaEquipamentoAulaDTO create(ListaEquipamentoAula listaEquipamentoAula) {
        ModelMapper modelMapper = new ModelMapper();
        ListaEquipamentoAulaDTO dto = modelMapper.map(listaEquipamentoAula, ListaEquipamentoAulaDTO.class);
        dto.nomeEquipamento = listaEquipamentoAula.getEquipamento().getNome();
        dto.nomeAula = listaEquipamentoAula.getAula().getNome();
        return dto;
    }
}
