package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.ListaClienteAula;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListaClienteAulaDTOTest {
    @Test
    public void deveConverterEntidadeParaDTOComSucesso() {
        Aula aula = new Aula();
        aula.setId(10L);
        aula.setNome("Aula teste");

        Cliente cliente = new Cliente();
        cliente.setId(2L);
        cliente.setNome("Carlos Instrutor");

        ListaClienteAula entidade = new ListaClienteAula();
        entidade.setId(1L);
        entidade.setAula(aula);
        entidade.setCliente(cliente);

        ListaClienteAulaDTO dto = ListaClienteAulaDTO.create(entidade);

        assertNotNull(dto);
        assertEquals(entidade.getId(), dto.getId());
        assertEquals(cliente.getId(), dto.getIdCliente());
        assertEquals(cliente.getNome(), dto.getNomeCliente());
        assertEquals(aula.getId(), dto.getIdAula());
        assertEquals(aula.getNome(), dto.getNomeAula());
    }

}