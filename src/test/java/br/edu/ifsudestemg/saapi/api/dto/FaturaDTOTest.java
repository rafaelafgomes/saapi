package br.edu.ifsudestemg.saapi.api.dto;
import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.Fatura;
import br.edu.ifsudestemg.saapi.model.entity.FormaPagamento;
import br.edu.ifsudestemg.saapi.model.entity.Plano;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FaturaDTOTest {
    @Test
    public void deveConverterEntidadeParaDTOComSucesso() {
        Plano plano = new Plano();
        plano.setId(10L);
        plano.setNome("Plano teste");

        Cliente cliente = new Cliente();
        cliente.setId(2L);
        cliente.setNome("Carlos Instrutor");
        cliente.setPlano(plano);

        FormaPagamento formaPagamento = new FormaPagamento();
        formaPagamento.setId(3L);
        formaPagamento.setNome("Pix");

        Fatura entidade = new Fatura();
        entidade.setId(1L);
        entidade.setDataVencimento("12/12/2020");
        entidade.setDataPagamento("12/12/2020");
        entidade.setValor(100.0f);
        entidade.setCliente(cliente);
        entidade.setFormaPagamento(formaPagamento);

        FaturaDTO dto = FaturaDTO.create(entidade);
        dto.setIdsAjustesFinanceiros(List.of(100L, 200L));

        assertNotNull(dto);
        assertEquals(entidade.getId(), dto.getId());
        assertEquals(entidade.getDataVencimento(), dto.getDataVencimento());
        assertEquals(entidade.getDataPagamento(), dto.getDataPagamento());
        assertEquals(entidade.getValor(), dto.getValor());

        assertEquals(cliente.getId(), dto.getIdCliente());
        assertEquals(cliente.getNome(), dto.getNomeCliente());
        assertEquals(plano.getNome(), dto.getPlanoCliente());
        assertEquals(formaPagamento.getId(), dto.getIdFormaPagamento());
        assertEquals(formaPagamento.getNome(), dto.getNomeFormaPagamento());

        assertNotNull(dto.getIdsAjustesFinanceiros());
        assertEquals(2, dto.getIdsAjustesFinanceiros().size());
        assertEquals(List.of(100L, 200L), dto.getIdsAjustesFinanceiros());
    }

}