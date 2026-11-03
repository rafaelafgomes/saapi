package br.edu.ifsudestemg.saapi.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.FormaPagamento;
import br.edu.ifsudestemg.saapi.model.repository.FormaPagamentoRepository;
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
class FormaPagamentoServiceTest extends EasyMockSupport {

    private FormaPagamento formaPagamento;

    @Mock
    private FormaPagamentoRepository formaPagamentoRepository;

    private FormaPagamentoService service;

    @BeforeEach
    void setUp() {
        formaPagamento = new FormaPagamento();
        formaPagamento.setId(1L);

        // Instanciação manual no setUp com o mock injetado pelo EasyMockExtension
        service = new FormaPagamentoService(formaPagamentoRepository);
    }

    // --- TESTES DE VALIDAÇÃO ---

    @Test
    public void deveTestarExcecaoNomeNulo() {
        replayAll();
        formaPagamento.setNome(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(formaPagamento));
        assertEquals("Nome inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoNomeVazio() {
        replayAll();
        formaPagamento.setNome("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(formaPagamento));
        assertEquals("Nome inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveValidarTudo() {
        replayAll();
        formaPagamento.setNome("Cartão de Crédito");

        assertDoesNotThrow(() -> service.validar(formaPagamento));
        verifyAll();
    }

    // --- TESTES DOS MÉTODOS DO SERVIÇO ---

    @Test
    public void deveRetornarListaFormaPagamento() {
        expect(formaPagamentoRepository.findAll()).andReturn(List.of(formaPagamento));
        replayAll();

        List<FormaPagamento> resultado = service.getFormaPagamento();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verifyAll();
    }

    @Test
    public void deveRetornarFormaPagamentoPorIDQuandoExistir() {
        expect(formaPagamentoRepository.findById(1L)).andReturn(Optional.of(formaPagamento));
        replayAll();

        Optional<FormaPagamento> resultado = service.getFormaPagamentoById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verifyAll();
    }

    @Test
    public void deveSalvarFormaPagamentoValida() {
        formaPagamento.setNome("Cartão de Crédito");

        expect(formaPagamentoRepository.save(anyObject(FormaPagamento.class))).andReturn(formaPagamento);
        replayAll();

        FormaPagamento resultado = service.salvar(formaPagamento);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verifyAll();
    }

    @Test
    public void deveExcluirFormaPagamentoQuandoPossuirID() {
        formaPagamentoRepository.delete(formaPagamento);
        expectLastCall().once();

        replayAll();

        assertDoesNotThrow(() -> service.excluir(formaPagamento));
        verifyAll();
    }

    @Test
    public void deveLancarExcecaoQuandoExcluirIdNulo() {
        FormaPagamento formaPagamentoSemId = new FormaPagamento();
        replayAll();

        assertThrows(NullPointerException.class, () -> service.excluir(formaPagamentoSemId));
        verifyAll();
    }
}