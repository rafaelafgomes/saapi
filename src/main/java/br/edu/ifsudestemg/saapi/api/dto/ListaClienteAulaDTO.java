package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.ListaClienteAula;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.persistence.ManyToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ListaClienteAulaDTO {

    private Long id;
    private Long idCliente;
    private String nomeCliente;
    private Long idAula;
    private String nomeAula;

    public static ListaClienteAulaDTO create(ListaClienteAula listaClienteAula) {
        ModelMapper modelMapper = new ModelMapper();
        ListaClienteAulaDTO dto = modelMapper.map(listaClienteAula, ListaClienteAulaDTO.class);
        dto.nomeCliente = listaClienteAula.getCliente().getNome();
        dto.nomeAula = listaClienteAula.getAula().getNome();
        return dto;
    }
}
