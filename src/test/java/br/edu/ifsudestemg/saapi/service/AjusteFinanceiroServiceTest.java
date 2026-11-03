package br.edu.ifsudestemg.saapi.service;

import br.edu.ifsudestemg.saapi.model.entity.AjusteFinanceiro;
import br.edu.ifsudestemg.saapi.model.repository.AjusteFinanceiroRepository;
import org.easymock.EasyMockExtension;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(EasyMockExtension.class)
class AjusteFinanceiroServiceTest extends EasyMockSupport {

    private AjusteFinanceiro ajusteFinanceiro;

    @Mock
    private AjusteFinanceiroRepository ajusteFinanceiroRepository;

    @TestSubject
    private AjusteFinanceiroService service = new AjusteFinanceiroService(ajusteFinanceiroRepository);

    @BeforeEach
    void setUp() {
        ajusteFinanceiro = new AjusteFinanceiro();
        ajusteFinanceiro.setId(1L);
    }

    @Test
    public void deveTestarExcecaoNomeNulo() {
        replayAll(); // Ativa o mock, mesmo que não seja chamado diretamente nesse teste
        ajusteFinanceiro.setNome(null);
        try {
            service.validar(ajusteFinanceiro);
            Assertions.fail("Deve haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Nome inválido", e.getMessage());
        }
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoNomeVazio() {
        replayAll();
        ajusteFinanceiro.setNome("");
        try {
            service.validar(ajusteFinanceiro);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Nome inválido", e.getMessage());
        }
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoTipoNulo() {
        replayAll();
        ajusteFinanceiro.setNome("Nome");
        ajusteFinanceiro.setTipo(null);
        try {
            service.validar(ajusteFinanceiro);
            Assertions.fail("Deveria haver uma excecao");
        } catch (Exception e) {
            Assertions.assertEquals("Tipo inválido", e.getMessage());
        }
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoTipoVazio() {
        replayAll();
        ajusteFinanceiro.setNome("Nome");
        ajusteFinanceiro.setTipo("");
        try {
            service.validar(ajusteFinanceiro);
            Assertions.fail("Deveria haver uma excecao");
        } catch (Exception e) {
            Assertions.assertEquals("Tipo inválido", e.getMessage());
        }
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoUnidadeNulo() {
        replayAll();
        ajusteFinanceiro.setNome("Nome");
        ajusteFinanceiro.setTipo("Tipo");
        ajusteFinanceiro.setUnidade(null);
        try {
            service.validar(ajusteFinanceiro);
            Assertions.fail("Deveria haver uma excecao");
        } catch (Exception e) {
            Assertions.assertEquals("Unidade inválida", e.getMessage());
        }
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoUnidadeVazio() {
        replayAll();
        ajusteFinanceiro.setNome("Nome");
        ajusteFinanceiro.setTipo("Tipo");
        ajusteFinanceiro.setUnidade("");
        try {
            service.validar(ajusteFinanceiro);
            Assertions.fail("Deveria haver uma excecao");
        } catch (Exception e) {
            Assertions.assertEquals("Unidade inválida", e.getMessage());
        }
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoValorNulo() {
        replayAll();
        ajusteFinanceiro.setNome("Nome");
        ajusteFinanceiro.setTipo("Tipo");
        ajusteFinanceiro.setUnidade("Unidade");
        ajusteFinanceiro.setValor(null);
        try {
            service.validar(ajusteFinanceiro);
            Assertions.fail("Deveria haver uma excecao");
        } catch (Exception e) {
            Assertions.assertEquals("Valor inválido", e.getMessage());
        }
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoValorIgualZero() {
        replayAll();
        ajusteFinanceiro.setNome("Nome");
        ajusteFinanceiro.setTipo("Tipo");
        ajusteFinanceiro.setUnidade("Unidade");
        ajusteFinanceiro.setValor(0.0f);
        try {
            service.validar(ajusteFinanceiro);
            Assertions.fail("Deveria haver uma excecao");
        } catch (Exception e) {
            Assertions.assertEquals("Valor inválido", e.getMessage());
        }
        verifyAll();
    }

    @Test
    public void deveValidarTudo() {
        replayAll();
        ajusteFinanceiro.setNome("Nome");
        ajusteFinanceiro.setTipo("Tipo");
        ajusteFinanceiro.setUnidade("Unidade");
        ajusteFinanceiro.setValor(0.1f);
        try {
            service.validar(ajusteFinanceiro);
            assertTrue(true);
        } catch (Exception e) {
            Assertions.fail("Não deveria haver uma excecao");
        }
        verifyAll();
    }

    @Test
    public void DeveRetornarListaAjusteFinanceiro() {
        expect(ajusteFinanceiroRepository.findAll()).andReturn(List.of(ajusteFinanceiro));
        replayAll();
        List<AjusteFinanceiro> resultado = service.getAjusteFinanceiros();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verifyAll();
    }

    @Test
    public void DeveRetornarAjusteFinanceiroPorIDQuandoExistir() {
        expect(ajusteFinanceiroRepository.findById(1L)).andReturn(Optional.of(ajusteFinanceiro));
        replayAll();
        Optional<AjusteFinanceiro> resultado = service.getAjusteFinanceiroById(1L);
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verifyAll();
    }

    @Test
    public void DeveSalvarAjusteFinanceiroVálido() {
        ajusteFinanceiro.setNome("Nome");
        ajusteFinanceiro.setTipo("Tipo");
        ajusteFinanceiro.setUnidade("Unidade");
        ajusteFinanceiro.setValor(0.1f);
        expect(ajusteFinanceiroRepository.save(anyObject(AjusteFinanceiro.class))).andReturn(ajusteFinanceiro);
        replayAll();
        AjusteFinanceiro resultado = service.salvar(ajusteFinanceiro);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verifyAll();
    }

    @Test
    public void DeveExcluirAjusteFinanceiroQuandoPossuirID() {
        ajusteFinanceiroRepository.delete(ajusteFinanceiro);
        expectLastCall().once();
        replayAll();
        assertDoesNotThrow(() -> service.excluir(ajusteFinanceiro));
        verifyAll();
    }

    @Test
    void DeveLancarExcecaoQuandoExcluirIdNulo() {
        AjusteFinanceiro ajusteSemId = new AjusteFinanceiro();
        replayAll();
        assertThrows(NullPointerException.class, () -> service.excluir(ajusteSemId));
        verifyAll();
    }
}