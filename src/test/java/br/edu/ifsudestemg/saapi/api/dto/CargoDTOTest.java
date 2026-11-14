package br.edu.ifsudestemg.saapi.api.dto;


import br.edu.ifsudestemg.saapi.model.entity.Cargo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CargoDTOTest {
    @Test
    public void deveConverterEntidadeParaDTOComSucesso() {
        Cargo entidade = new Cargo();
        entidade.setId(1L);
        entidade.setNome("Cargo teste");
        entidade.setDescricao("Descrição teste");

        CargoDTO dto = CargoDTO.create(entidade);

        assertNotNull(dto);
        assertEquals(entidade.getId(), dto.getId());
        assertEquals(entidade.getNome(), dto.getNome());
        assertEquals(entidade.getDescricao(), dto.getDescricao());
    }

}