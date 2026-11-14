package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.Equipamento;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EquipamentoDTOTest {
    @Test
    public void deveConverterEntidadeParaDTOComSucesso() {
        Equipamento entidade = new Equipamento();
        entidade.setId(1L);
        entidade.setNome("Nome teste");
        entidade.setModelo("modelo teste");
        entidade.setUltManutencao("2026-11-15");
        entidade.setQuantidade(20);
        entidade.setDisponivel(true);
        entidade.setLocal("Local teste");
        entidade.setDescricao("Descrição teste");

        EquipamentoDTO dto = EquipamentoDTO.create(entidade);

        assertNotNull(dto);
        assertEquals(entidade.getId(), dto.getId());
        assertEquals(entidade.getNome(), dto.getNome());
        assertEquals(entidade.getModelo(), dto.getModelo());
        assertEquals(entidade.getUltManutencao(), dto.getUltManutencao());
        assertEquals(entidade.getQuantidade(), dto.getQuantidade());
        assertEquals(entidade.getDisponivel(), dto.getDisponivel());
        assertEquals(entidade.getLocal(), dto.getLocal());
        assertEquals(entidade.getDescricao(), dto.getDescricao());
    }

}