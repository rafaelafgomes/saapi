package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.Exercicio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExercicioDTO {
    private Long id;

    private String nome;
    private String descricao;

    public static ExercicioDTO create (Exercicio exercicio) {
        ModelMapper modelMapper = new ModelMapper();
        ExercicioDTO dto = modelMapper.map(exercicio, ExercicioDTO.class);
        return dto;
    }
}
