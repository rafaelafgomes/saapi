package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.Funcionario;
import br.edu.ifsudestemg.saapi.model.entity.Treino;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TreinoDTOTest {
    @Test
    public void deveConverterEntidadeParaDTOComSucesso() {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(3L);
        funcionario.setNome("Carlos Instrutor");

        Cliente cliente = new Cliente();
        cliente.setId(2L);
        cliente.setNome("Carlos Instrutor");
        cliente.setSexo(true);

        Treino entidade = new Treino();
        entidade.setId(1L);
        entidade.setNome("Treino teste");
        entidade.setData("12/12/2020");
        entidade.setDescricao("Descrição teste");
        entidade.setCliente(cliente);
        entidade.setFuncionario(funcionario);

        TreinoDTO dto = TreinoDTO.create(entidade);

        assertNotNull(dto);
        assertEquals(entidade.getId(), dto.getId());
        assertEquals(entidade.getNome(), dto.getNome());
        assertEquals(entidade.getData(), dto.getData());
        assertEquals(entidade.getDescricao(), dto.getDescricao());

        assertEquals(cliente.getId(), dto.getIdCliente());
        assertEquals(cliente.getNome(), dto.getNomeCliente());
        assertEquals(funcionario.getId(), dto.getIdFuncionario());
        assertEquals(funcionario.getNome(), dto.getNomeFuncionario());
    }

}