package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.Exercicio;
import br.edu.ifsudestemg.saapi.model.entity.ListaExercicioTreino;
import br.edu.ifsudestemg.saapi.model.entity.Treino;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.persistence.ManyToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ListaExercicioTreinoDTO {

    private Long id;
    private Integer serie;
    private Integer carga;
    private Integer repeticao;
    private Long idExercicio;
    private String nomeExercicio;
    private Long idTreino;
    private String nomeTreino;

    public static ListaExercicioTreinoDTO create(ListaExercicioTreino listaExercicioTreino) {
        ModelMapper modelMapper = new ModelMapper();
        ListaExercicioTreinoDTO dto = modelMapper.map(listaExercicioTreino, ListaExercicioTreinoDTO.class);
        dto.nomeExercicio = listaExercicioTreino.getExercicio().getNome();
        dto.nomeTreino = listaExercicioTreino.getTreino().getNome();

        return dto;
    }
}
