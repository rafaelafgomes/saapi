package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.Plano;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteDTOTest {
    @Test
    public void deveConverterEntidadeParaDTOComSucesso() {
        // Arrange
        Plano plano = new Plano();
        plano.setId(3L);

        Cliente entidade = new Cliente();
        entidade.setId(1L);
        entidade.setNome("Mariana Costa");
        entidade.setCpf("12345678901");
        entidade.setTelefone("32999998888");
        entidade.setStatus(true);
        entidade.setSexo(false);
        entidade.setLogradouro("Avenida Rio Branco");
        entidade.setNumero(1500);
        entidade.setComplemento("Apto 302");
        entidade.setBairro("Centro");
        entidade.setCidade("Juiz de Fora");
        entidade.setEstado("MG");
        entidade.setCep("36000000");
        entidade.setDataNascimento("1998-04-12");
        entidade.setPlano(plano);

        // Act
        ClienteDTO dto = ClienteDTO.create(entidade);

        // Assert
        assertNotNull(dto);
        assertEquals(entidade.getId(), dto.getId());
        assertEquals(entidade.getNome(), dto.getNome());
        assertEquals(entidade.getCpf(), dto.getCpf());
        assertEquals(entidade.getTelefone(), dto.getTelefone());
        assertEquals(entidade.getStatus(), dto.getStatus());
        assertEquals(entidade.getSexo(), dto.getSexo());
        assertEquals(entidade.getLogradouro(), dto.getLogradouro());
        assertEquals(entidade.getNumero(), dto.getNumero());
        assertEquals(entidade.getComplemento(), dto.getComplemento());
        assertEquals(entidade.getBairro(), dto.getBairro());
        assertEquals(entidade.getCidade(), dto.getCidade());
        assertEquals(entidade.getEstado(), dto.getEstado());
        assertEquals(entidade.getCep(), dto.getCep());
        assertEquals(entidade.getDataNascimento(), dto.getDataNascimento());
        assertEquals(plano.getId(), dto.getIdPlano());
    }

}