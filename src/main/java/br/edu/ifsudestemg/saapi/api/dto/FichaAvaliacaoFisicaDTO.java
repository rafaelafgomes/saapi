package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.Funcionario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class FichaAvaliacaoFisicaDTO {
    private Long id;

    private String dataFicha;
    private float peso;
    private float altura;
    private String descricao;

    private Long idCliente;
    private Long idFuncionario;

}
