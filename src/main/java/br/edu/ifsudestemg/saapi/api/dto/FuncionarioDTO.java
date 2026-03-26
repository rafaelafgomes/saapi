package br.edu.ifsudestemg.saapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class FuncionarioDTO {

    private String email;
    private String dataAdmissao;
    private String observacao;
    private Long idCargo;
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
}
