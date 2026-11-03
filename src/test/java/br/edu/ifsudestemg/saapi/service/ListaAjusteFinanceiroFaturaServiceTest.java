package br.edu.ifsudestemg.saapi.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.AjusteFinanceiro;
import br.edu.ifsudestemg.saapi.model.entity.Fatura;
import br.edu.ifsudestemg.saapi.model.entity.ListaAjusteFinanceiroFatura;
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
class ListaAjusteFinanceiroFaturaServiceTest extends EasyMockSupport {

    private ListaAjusteFinanceiroFatura listaAjusteFinanceiroFatura;
    private AjusteFinanceiro ajusteFinanceiro;
    private Fatura fatura;

    @Mock
    private ListaAjusteFinanceiroFaturaRepository repository;

    private ListaAjusteFinanceiroFaturaService service;

    @BeforeEach
    void setUp() {
        ajusteFinanceiro = new AjusteFinanceiro();
        ajusteFinanceiro.setId(1L);

        fatura = new Fatura();
        fatura.setId(1L);

        listaAjusteFinanceiroFatura = new ListaAjusteFinanceiroFatura();
        listaAjusteFinanceiroFatura.setId(1L);

        service = new ListaAjusteFinanceiroFaturaService(repository);
    }

    @Test
    public void deveTestarExcecaoAjusteFinanceiroNulo() {
        replayAll();
        listaAjusteFinanceiroFatura.setAjusteFinanceiro(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(listaAjusteFinanceiroFatura));
        assertEquals("Ajuste financeiro inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoAjusteFinanceiroIdNulo() {
        replayAll();
        listaAjusteFinanceiroFatura.setAjusteFinanceiro(new AjusteFinanceiro());
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(listaAjusteFinanceiroFatura));
        assertEquals("Ajuste financeiro inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoAjusteFinanceiroIdIgualZero() {
        replayAll();
        AjusteFinanceiro ajusteZero = new AjusteFinanceiro();
        ajusteZero.setId(0L);
        listaAjusteFinanceiroFatura.setAjusteFinanceiro(ajusteZero);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(listaAjusteFinanceiroFatura));
        assertEquals("Ajuste financeiro inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoFaturaNula() {
        replayAll();
        listaAjusteFinanceiroFatura.setAjusteFinanceiro(ajusteFinanceiro);
        listaAjusteFinanceiroFatura.setFatura(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(listaAjusteFinanceiroFatura));
        assertEquals("Fatura inválida", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoFaturaIdNulo() {
        replayAll();
        listaAjusteFinanceiroFatura.setAjusteFinanceiro(ajusteFinanceiro);
        listaAjusteFinanceiroFatura.setFatura(new Fatura());
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(listaAjusteFinanceiroFatura));
        assertEquals("Fatura inválida", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoFaturaIdIgualZero() {
        replayAll();
        Fatura faturaZero = new Fatura();
        faturaZero.setId(0L);
        listaAjusteFinanceiroFatura.setAjusteFinanceiro(ajusteFinanceiro);
        listaAjusteFinanceiroFatura.setFatura(faturaZero);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(listaAjusteFinanceiroFatura));
        assertEquals("Fatura inválida", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveValidarTudo() {
        replayAll();
        listaAjusteFinanceiroFatura.setAjusteFinanceiro(ajusteFinanceiro);
        listaAjusteFinanceiroFatura.setFatura(fatura);

        assertDoesNotThrow(() -> service.validar(listaAjusteFinanceiroFatura));
        verifyAll();
    }

    @Test
    public void deveRetornarListaAjusteFinanceiroFatura() {
        expect(repository.findAll()).andReturn(List.of(listaAjusteFinanceiroFatura));
        replayAll();

        List<ListaAjusteFinanceiroFatura> resultado = service.getListaAjusteFinanceiroFatura();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verifyAll();
    }

    @Test
    public void deveRetornarListaPorFatura() {
        expect(repository.findAllByFatura(fatura)).andReturn(List.of(listaAjusteFinanceiroFatura));
        replayAll();

        List<ListaAjusteFinanceiroFatura> resultado = service.getListaAjusteFinanceiroFaturaByFatura(fatura);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verifyAll();
    }

    @Test
    public void deveRetornarListaAjusteFinanceiroFaturaPorIDQuandoExistir() {
        expect(repository.findById(1L)).andReturn(Optional.of(listaAjusteFinanceiroFatura));
        replayAll();

        Optional<ListaAjusteFinanceiroFatura> resultado = service.getListaAjusteFinanceiroFaturaById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verifyAll();
    }

    @Test
    public void deveSalvarListaAjusteFinanceiroFaturaValida() {
        listaAjusteFinanceiroFatura.setAjusteFinanceiro(ajusteFinanceiro);
        listaAjusteFinanceiroFatura.setFatura(fatura);

        expect(repository.save(anyObject(ListaAjusteFinanceiroFatura.class))).andReturn(listaAjusteFinanceiroFatura);
        replayAll();

        ListaAjusteFinanceiroFatura resultado = service.salvar(listaAjusteFinanceiroFatura);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verifyAll();
    }

    @Test
    public void deveExcluirListaAjusteFinanceiroFaturaQuandoPossuirID() {
        repository.delete(listaAjusteFinanceiroFatura);
        expectLastCall().once();

        replayAll();

        assertDoesNotThrow(() -> service.excluir(listaAjusteFinanceiroFatura));
        verifyAll();
    }

    @Test
    public void deveLancarExcecaoQuandoExcluirIdNulo() {
        ListaAjusteFinanceiroFatura objetoSemId = new ListaAjusteFinanceiroFatura();
        replayAll();

        assertThrows(NullPointerException.class, () -> service.excluir(objetoSemId));
        verifyAll();
    }
}