package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.FichaAvaliacaoFisica;
import br.edu.ifsudestemg.saapi.model.entity.Funcionario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;


public class FichaAvaliacaoFisicaDTO {
    private Long id;

    private String dataFicha;
    private float peso;
    private float altura;
    private String descricao;

    private Long idCliente;
    private String nomeCliente;
    private Long idFuncionario;
    private String nomeFuncionario;

    public static FichaAvaliacaoFisicaDTO create(FichaAvaliacaoFisica fichaAvaliacaoFisica) {
        ModelMapper modelMapper = new ModelMapper();
       FichaAvaliacaoFisicaDTO dto = modelMapper.map(fichaAvaliacaoFisica, FichaAvaliacaoFisicaDTO.class);
        dto.nomeFuncionario = fichaAvaliacaoFisica.getFuncionario().getNome();
        dto.nomeCliente = fichaAvaliacaoFisica.getCliente().getNome();
        return dto;
    }


}
