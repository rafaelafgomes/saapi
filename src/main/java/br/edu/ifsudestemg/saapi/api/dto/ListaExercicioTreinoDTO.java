package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.Exercicio;
import br.edu.ifsudestemg.saapi.model.entity.Treino;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ListaExercicioTreinoDTO {

    private Long id;
    private Integer serie;
    private Integer carga;
    private Integer repeticao;
    private Long idxercicio;
    private Long idTreino;

}
