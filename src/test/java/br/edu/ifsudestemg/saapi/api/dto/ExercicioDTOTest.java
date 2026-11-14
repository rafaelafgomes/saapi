package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.Exercicio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExercicioDTOTest {
    @Test
    public void deveConverterEntidadeParaDTOComSucesso() {
        Exercicio entidade = new Exercicio();
        entidade.setId(1L);
        entidade.setNome("Exercicio teste");
        entidade.setDescricao("Exercicio teste");

        ExercicioDTO dto = ExercicioDTO.create(entidade);

        assertNotNull(dto);
        assertEquals(entidade.getId(), dto.getId());
        assertEquals(entidade.getNome(), dto.getNome());
        assertEquals(entidade.getDescricao(), dto.getDescricao());
    }

}