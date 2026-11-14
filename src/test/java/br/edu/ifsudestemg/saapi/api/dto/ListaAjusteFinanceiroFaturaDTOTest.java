package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.AjusteFinanceiro;
import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.Fatura;
import br.edu.ifsudestemg.saapi.model.entity.ListaAjusteFinanceiroFatura;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ListaAjusteFinanceiroFaturaDTOTest {
    @Test
    public void deveConverterEntidadeParaDTOComSucesso() {
        AjusteFinanceiro ajusteFinanceiro = new AjusteFinanceiro();
        ajusteFinanceiro.setId(10L);
        ajusteFinanceiro.setNome("Ajuste teste");

        Cliente cliente = new Cliente();
        cliente.setId(2L);
        cliente.setNome("Carlos Instrutor");

        Fatura fatura = new Fatura();
        fatura.setId(20L);
        fatura.setCliente(cliente);

        ListaAjusteFinanceiroFatura entidade = new ListaAjusteFinanceiroFatura();
        entidade.setId(1L);
        entidade.setAjusteFinanceiro(ajusteFinanceiro);
        entidade.setFatura(fatura);

        ListaAjusteFinanceiroFaturaDTO dto = ListaAjusteFinanceiroFaturaDTO.create(entidade);

        assertNotNull(dto);
        assertEquals(entidade.getId(), dto.getId());
        assertEquals(ajusteFinanceiro.getId(), dto.getIdAjusteFinanceiro());
        assertEquals(ajusteFinanceiro.getNome(), dto.getNomeAjusteFinanceiro());
        assertEquals(fatura.getId(), dto.getIdFatura());
    }

}