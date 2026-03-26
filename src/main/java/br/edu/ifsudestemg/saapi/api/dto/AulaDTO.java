package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.Funcionario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AulaDTO {
    private Long id;

    private String nome;
    private String dataAula;
    private String horaAula;
    private boolean status;
    private Integer capacidade;
    private String descricao;
    private Long idFuncionario;

}
