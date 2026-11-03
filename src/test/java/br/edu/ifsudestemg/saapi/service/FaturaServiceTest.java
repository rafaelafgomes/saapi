package br.edu.ifsudestemg.saapi.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.AjusteFinanceiro;
import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.Fatura;
import br.edu.ifsudestemg.saapi.model.entity.FormaPagamento;
import br.edu.ifsudestemg.saapi.model.entity.Plano;
import br.edu.ifsudestemg.saapi.model.repository.AjusteFinanceiroRepository;
import br.edu.ifsudestemg.saapi.model.repository.FaturaRepository;
import br.edu.ifsudestemg.saapi.model.repository.ListaAjusteFinanceiroFaturaRepository;
import org.easymock.EasyMockExtension;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(EasyMockExtension.class)
class FaturaServiceTest extends EasyMockSupport {

    private Fatura fatura;
    private Cliente cliente;
    private FormaPagamento formaPagamento;
    private Plano plano;

    @Mock
    private FaturaRepository faturaRepository;

    @Mock
    private ListaAjusteFinanceiroFaturaRepository listaAjusteFinanceiroFaturaRepository;

    @Mock
    private AjusteFinanceiroRepository ajusteFinanceiroRepository;

    @Mock
    private ClienteService clienteService;

    private FaturaService service;

    @BeforeEach
    void setUp() {
        plano = new Plano();
        plano.setId(1L);
        plano.setValor(100.0f);

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setPlano(plano);

        formaPagamento = new FormaPagamento();
        formaPagamento.setId(1L);

        fatura = new Fatura();
        fatura.setId(1L);

        service = new FaturaService(faturaRepository, listaAjusteFinanceiroFaturaRepository, ajusteFinanceiroRepository, clienteService);
    }

    @Test
    public void deveTestarExcecaoDataVencimentoNula() {
        replayAll();
        fatura.setDataVencimento(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(fatura));
        assertEquals("Data inválida", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoDataVencimentoVazia() {
        replayAll();
        fatura.setDataVencimento("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(fatura));
        assertEquals("Data inválida", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoValorNulo() {
        replayAll();
        fatura.setDataVencimento("10/11/2026");
        fatura.setValor(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(fatura));
        assertEquals("Valor inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoValorIgualZero() {
        replayAll();
        fatura.setDataVencimento("10/11/2026");
        fatura.setValor(0.0f);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(fatura));
        assertEquals("Valor inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoClienteNulo() {
        replayAll();
        fatura.setDataVencimento("10/11/2026");
        fatura.setValor(100.0f);
        fatura.setCliente(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(fatura));
        assertEquals("Cliente inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoClienteIdNulo() {
        replayAll();
        fatura.setDataVencimento("10/11/2026");
        fatura.setValor(100.0f);
        fatura.setCliente(new Cliente());
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(fatura));
        assertEquals("Cliente inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoClienteIdIgualZero() {
        replayAll();
        Cliente clienteZero = new Cliente();
        clienteZero.setId(0L);
        fatura.setDataVencimento("10/11/2026");
        fatura.setValor(100.0f);
        fatura.setCliente(clienteZero);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(fatura));
        assertEquals("Cliente inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoFormaPagamentoNula() {
        replayAll();
        fatura.setDataVencimento("10/11/2026");
        fatura.setValor(100.0f);
        fatura.setCliente(cliente);
        fatura.setFormaPagamento(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(fatura));
        assertEquals("Forma de pagamento inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoFormaPagamentoIdNulo() {
        replayAll();
        fatura.setDataVencimento("10/11/2026");
        fatura.setValor(100.0f);
        fatura.setCliente(cliente);
        fatura.setFormaPagamento(new FormaPagamento());
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(fatura));
        assertEquals("Forma de pagamento inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoFormaPagamentoIdIgualZero() {
        replayAll();
        FormaPagamento fpZero = new FormaPagamento();
        fpZero.setId(0L);
        fatura.setDataVencimento("10/11/2026");
        fatura.setValor(100.0f);
        fatura.setCliente(cliente);
        fatura.setFormaPagamento(fpZero);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(fatura));
        assertEquals("Forma de pagamento inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveValidarTudo() {
        replayAll();
        fatura.setDataVencimento("10/11/2026");
        fatura.setValor(100.0f);
        fatura.setCliente(cliente);
        fatura.setFormaPagamento(formaPagamento);
        assertDoesNotThrow(() -> service.validar(fatura));
        verifyAll();
    }

    @Test
    public void deveLancarExcecaoQuandoClienteNaoEncontradoNoCalculo() {
        expect(clienteService.getClienteById(1L)).andReturn(Optional.empty());
        replayAll();
        Exception e = assertThrows(RegraNegocioException.class, () -> service.calcularValorFatura(1L, List.of(1L)));
        assertEquals("Cliente não encontrado", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveCalcularValorFaturaSemAjustes() {
        expect(clienteService.getClienteById(1L)).andReturn(Optional.of(cliente));
        replayAll();
        Float resultado = service.calcularValorFatura(1L, null);
        assertEquals(100.0f, resultado);
        verifyAll();
    }

    @Test
    public void deveCalcularValorFaturaComDescontoEAcrescimo() {
        AjusteFinanceiro desconto = new AjusteFinanceiro();
        desconto.setId(1L);
        desconto.setTipo("DESCONTO");
        desconto.setValor(20.0f);
        AjusteFinanceiro acrescimo = new AjusteFinanceiro();
        acrescimo.setId(2L);
        acrescimo.setTipo("ACRESCIMO");
        acrescimo.setValor(15.0f);
        expect(clienteService.getClienteById(1L)).andReturn(Optional.of(cliente));
        expect(ajusteFinanceiroRepository.findById(1L)).andReturn(Optional.of(desconto));
        expect(ajusteFinanceiroRepository.findById(2L)).andReturn(Optional.of(acrescimo));
        replayAll();
        Float resultado = service.calcularValorFatura(1L, List.of(1L, 2L));
        assertEquals(95.0f, resultado);
        verifyAll();
    }

    @Test
    public void deveIgnorarAjusteInexistenteNoCalculo() {
        expect(clienteService.getClienteById(1L)).andReturn(Optional.of(cliente));
        expect(ajusteFinanceiroRepository.findById(99L)).andReturn(Optional.empty());
        replayAll();
        Float resultado = service.calcularValorFatura(1L, List.of(99L));
        assertEquals(100.0f, resultado);
        verifyAll();
    }

    @Test
    public void deveRetornarListaFatura() {
        expect(faturaRepository.findAll()).andReturn(List.of(fatura));
        replayAll();
        List<Fatura> resultado = service.getFatura();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verifyAll();
    }

    @Test
    public void deveRetornarFaturaPorIDQuandoExistir() {
        expect(faturaRepository.findById(1L)).andReturn(Optional.of(fatura));
        replayAll();
        Optional<Fatura> resultado = service.getFaturaById(1L);
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verifyAll();
    }

    @Test
    public void deveSalvarFaturaValida() {
        fatura.setDataVencimento("10/11/2026");
        fatura.setValor(100.0f);
        fatura.setCliente(cliente);
        fatura.setFormaPagamento(formaPagamento);
        expect(faturaRepository.save(anyObject(Fatura.class))).andReturn(fatura);
        replayAll();
        Fatura resultado = service.salvar(fatura);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verifyAll();
    }

    @Test
    public void deveExcluirFaturaQuandoPossuirID() {
        listaAjusteFinanceiroFaturaRepository.deleteAllByFatura(fatura);
        expectLastCall().once();
        faturaRepository.delete(fatura);
        expectLastCall().once();
        replayAll();
        assertDoesNotThrow(() -> service.excluir(fatura));
        verifyAll();
    }

    @Test
    public void deveLancarExcecaoQuandoExcluirIdNulo() {
        Fatura faturaSemId = new Fatura();
        replayAll();
        assertThrows(NullPointerException.class, () -> service.excluir(faturaSemId));
        verifyAll();
    }
}