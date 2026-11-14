package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.entity.Equipamento;
import br.edu.ifsudestemg.saapi.model.entity.ListaEquipamentoAula;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListaEquipamentoAulaDTOTest {
    @Test
    public void deveConverterEntidadeParaDTOComSucesso() {
        Aula aula = new Aula();
        aula.setId(10L);
        aula.setNome("Aula teste");

        Equipamento equipamento = new Equipamento();
        equipamento.setId(2L);
        equipamento.setNome("Equipamento teste");

        ListaEquipamentoAula entidade = new ListaEquipamentoAula();
        entidade.setId(1L);
        entidade.setQuantidade(10);
        entidade.setAula(aula);
        entidade.setEquipamento(equipamento);

        ListaEquipamentoAulaDTO dto = ListaEquipamentoAulaDTO.create(entidade);

        assertNotNull(dto);
        assertEquals(entidade.getId(), dto.getId());
        assertEquals(entidade.getQuantidade(), dto.getQuantidade());
        assertEquals(equipamento.getId(), dto.getIdEquipamento());
        assertEquals(equipamento.getNome(), dto.getNomeEquipamento());
        assertEquals(aula.getId(), dto.getIdAula());
        assertEquals(aula.getNome(), dto.getNomeAula());

    }

}