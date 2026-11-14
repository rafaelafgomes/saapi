package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.Plano;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlanoDTOTest {
    @Test
    public void deveConverterEntidadeParaDTOComSucesso() {
        Plano entidade = new Plano();
        entidade.setId(1L);
        entidade.setNome("Plano teste");
        entidade.setDuracao(10);
        entidade.setValor(10.0f);
        entidade.setDescricao("Descrição de teste");

        PlanoDTO dto = PlanoDTO.create(entidade);

        assertNotNull(dto);
        assertEquals(entidade.getId(), dto.getId());
        assertEquals(entidade.getNome(), dto.getNome());
        assertEquals(entidade.getDuracao(), dto.getDuracao());
        assertEquals(entidade.getValor(), dto.getValor());
        assertEquals(entidade.getDescricao(), dto.getDescricao());
    }

}