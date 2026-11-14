package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.AjusteFinanceiro;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AjusteFinanceiroDTOTest {

    @Test
    public void deveConverterEntidadeParaDTOComSucesso() {
        AjusteFinanceiro entidade = new AjusteFinanceiro();
        entidade.setId(1L);
        entidade.setNome("Desconto teste");
        entidade.setTipo("Desconto");
        entidade.setUnidade("Porcentagem");
        entidade.setValor(10.0f);
        entidade.setDescricao("Descrição de teste");

        AjusteFinanceiroDTO dto = AjusteFinanceiroDTO.create(entidade);

        assertNotNull(dto);
        assertEquals(entidade.getId(), dto.getId());
        assertEquals(entidade.getNome(), dto.getNome());
        assertEquals(entidade.getTipo(), dto.getTipo());
        assertEquals(entidade.getUnidade(), dto.getUnidade());
        assertEquals(entidade.getValor(), dto.getValor());
        assertEquals(entidade.getDescricao(), dto.getDescricao());
    }


}