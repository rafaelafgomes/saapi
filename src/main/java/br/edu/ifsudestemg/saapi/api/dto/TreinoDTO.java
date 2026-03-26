package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.Funcionario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Long idFuncionario;
}
