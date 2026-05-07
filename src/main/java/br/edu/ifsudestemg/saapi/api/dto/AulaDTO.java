package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.entity.Funcionario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AulaDTO {
    private Long id;

    private String nome;
    private String dataAula;
    private String horaAula;
    private Boolean status;
    private Integer capacidade;
    private String descricao;
    private Long idFuncionario;
    private String nomeFuncionario;

    public static AulaDTO create(Aula aula) {
        ModelMapper modelMapper = new ModelMapper();
        AulaDTO dto = modelMapper.map(aula, AulaDTO.class);
        dto.nomeFuncionario = aula.getFuncionario().getNome();
        return dto;
    }
}

