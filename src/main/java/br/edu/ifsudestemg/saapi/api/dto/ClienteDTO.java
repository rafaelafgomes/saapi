package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.Plano;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class ClienteDTO {

    private Long id;

    private String nome;
    private String cpf;
    private String telefone;
    private boolean status;
    private String logradouro;
    private Integer numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String dataNascimento;
    private Long idPlano;

}
