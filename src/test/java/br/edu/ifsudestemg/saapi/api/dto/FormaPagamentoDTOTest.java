package br.edu.ifsudestemg.saapi.api.dto;


import br.edu.ifsudestemg.saapi.model.entity.FormaPagamento;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FormaPagamentoDTOTest {
    @Test
    public void deveConverterEntidadeParaDTOComSucesso() {
        FormaPagamento entidade = new FormaPagamento();
        entidade.setId(1L);
        entidade.setNome("Forma de pagamento teste");
        entidade.setDescricao("Descrição teste");

        FormaPagamentoDTO dto = FormaPagamentoDTO.create(entidade);

        assertNotNull(dto);
        assertEquals(entidade.getId(), dto.getId());
        assertEquals(entidade.getNome(), dto.getNome());
        assertEquals(entidade.getDescricao(), dto.getDescricao());
    }

}