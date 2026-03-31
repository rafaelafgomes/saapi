package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.Funcionario;
import br.edu.ifsudestemg.saapi.model.entity.Treino;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.persistence.ManyToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class TreinoDTO {

    private Long id;
    private String nome;
    private String data;
    private String descricao;
    private Long idCliente;
    private String nomeCliente;
    private Long idFuncionario;
    private String nomeFuncionario;

    public static TreinoDTO create(Treino treino) {
        ModelMapper modelMapper = new ModelMapper();
        TreinoDTO dto = modelMapper.map(treino, TreinoDTO.class);
        dto.nomeCliente = treino.getCliente().getNome();
        dto.nomeFuncionario = treino.getFuncionario().getNome();
        return dto;
    }
}
