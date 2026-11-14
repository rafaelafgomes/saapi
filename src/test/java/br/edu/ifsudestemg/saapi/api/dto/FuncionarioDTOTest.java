package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.Cargo;
import br.edu.ifsudestemg.saapi.model.entity.Funcionario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FuncionarioDTOTest {
    @Test
    public void deveConverterEntidadeParaDTOComSucesso() {

        Cargo cargo = new Cargo();
        cargo.setId(5L);
        cargo.setNome("Personal Trainer");

        Funcionario entidade = new Funcionario();
        entidade.setId(1L);
        entidade.setNome("Roberto Alves");
        entidade.setCpf("12345678901");
        entidade.setTelefone("32988887777");
        entidade.setStatus(true);
        entidade.setLogradouro("Rua Halfeld");
        entidade.setNumero(800);
        entidade.setComplemento("Sala 10");
        entidade.setBairro("Centro");
        entidade.setCidade("Juiz de Fora");
        entidade.setEstado("MG");
        entidade.setCep("36000000");
        entidade.setDataNascimento("1988-03-10");
        entidade.setEmail("roberto@saapi.com");
        entidade.setDataAdmissao("2025-02-01");
        entidade.setObservacao("Especialista em musculação");
        entidade.setCargo(cargo);

        FuncionarioDTO dto = FuncionarioDTO.create(entidade);

        assertNotNull(dto);
        assertEquals(entidade.getId(), dto.getId());
        assertEquals(entidade.getNome(), dto.getNome());
        assertEquals(entidade.getCpf(), dto.getCpf());
        assertEquals(entidade.getTelefone(), dto.getTelefone());
        assertEquals(entidade.getStatus(), dto.getStatus());
        assertEquals(entidade.getLogradouro(), dto.getLogradouro());
        assertEquals(entidade.getNumero(), dto.getNumero());
        assertEquals(entidade.getComplemento(), dto.getComplemento());
        assertEquals(entidade.getBairro(), dto.getBairro());
        assertEquals(entidade.getCidade(), dto.getCidade());
        assertEquals(entidade.getEstado(), dto.getEstado());
        assertEquals(entidade.getCep(), dto.getCep());
        assertEquals(entidade.getDataNascimento(), dto.getDataNascimento());
        assertEquals(entidade.getEmail(), dto.getEmail());
        assertEquals(entidade.getDataAdmissao(), dto.getDataAdmissao());
        assertEquals(entidade.getObservacao(), dto.getObservacao());

        assertEquals(cargo.getId(), dto.getIdCargo());
        assertEquals(cargo.getNome(), dto.getNomeCargo());
    }

}