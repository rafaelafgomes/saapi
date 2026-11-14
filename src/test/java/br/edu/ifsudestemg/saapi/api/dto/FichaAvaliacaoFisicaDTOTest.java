package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FichaAvaliacaoFisicaDTOTest {
    @Test
    public void deveConverterEntidadeParaDTOComSucesso() {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(3L);
        funcionario.setNome("Carlos Instrutor");

        Cliente cliente = new Cliente();
        cliente.setId(2L);
        cliente.setNome("Carlos Instrutor");
        cliente.setSexo(true);

        FichaAvaliacaoFisica entidade = new FichaAvaliacaoFisica();
        entidade.setId(1L);
        entidade.setDataFicha("12/12/2020");
        entidade.setPeso(50.0f);
        entidade.setAltura(1.70f);
        entidade.setDescricao("Descrição teste");

        entidade.setCliente(cliente);
        entidade.setFuncionario(funcionario);

        FichaAvaliacaoFisicaDTO dto = FichaAvaliacaoFisicaDTO.create(entidade);

        assertNotNull(dto);
        assertEquals(entidade.getId(), dto.getId());
        assertEquals(entidade.getDataFicha(), dto.getDataFicha());
        assertEquals(entidade.getPeso(), dto.getPeso());
        assertEquals(entidade.getAltura(), dto.getAltura());
        assertEquals(entidade.getDescricao(), dto.getDescricao());

        assertEquals(cliente.getId(), dto.getIdCliente());
        assertEquals(cliente.getNome(), dto.getNomeCliente());
        assertEquals(cliente.getSexo(), dto.getSexoCliente());
        assertEquals(funcionario.getId(), dto.getIdFuncionario());
        assertEquals(funcionario.getNome(), dto.getNomeFuncionario());
    }

}