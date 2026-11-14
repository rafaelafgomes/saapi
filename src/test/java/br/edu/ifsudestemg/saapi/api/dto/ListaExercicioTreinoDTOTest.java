package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.Exercicio;
import br.edu.ifsudestemg.saapi.model.entity.ListaExercicioTreino;
import br.edu.ifsudestemg.saapi.model.entity.Treino;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListaExercicioTreinoDTOTest {
    @Test
    public void deveConverterEntidadeParaDTOComSucesso() {
        Exercicio exercicio = new Exercicio();
        exercicio.setId(10L);
        exercicio.setNome("Exercicio teste");

        Treino treino = new Treino();
        treino.setId(2L);
        treino.setNome("Treino teste");

        ListaExercicioTreino entidade = new ListaExercicioTreino();
        entidade.setId(1L);
        entidade.setSerie(10);
        entidade.setCarga(20);
        entidade.setRepeticao(30);
        entidade.setExercicio(exercicio);
        entidade.setTreino(treino);

        ListaExercicioTreinoDTO dto = ListaExercicioTreinoDTO.create(entidade);

        assertNotNull(dto);
        assertEquals(entidade.getId(), dto.getId());
        assertEquals(entidade.getSerie(), dto.getSerie());
        assertEquals(entidade.getCarga(), dto.getCarga());
        assertEquals(entidade.getRepeticao(), dto.getRepeticao());
        assertEquals(exercicio.getId(), dto.getIdExercicio());
        assertEquals(exercicio.getNome(), dto.getNomeExercicio());
        assertEquals(treino.getId(), dto.getIdTreino());
        assertEquals(treino.getNome(), dto.getNomeTreino());

    }

}