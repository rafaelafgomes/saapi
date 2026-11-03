package br.edu.ifsudestemg.saapi.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Plano;
import br.edu.ifsudestemg.saapi.model.repository.PlanoRepository;
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
class PlanoServiceTest extends EasyMockSupport {

    private Plano plano;

    @Mock
    private PlanoRepository planoRepository;

    private PlanoService service;

    @BeforeEach
    void setUp() {
        plano = new Plano();
        plano.setId(1L);

        // Instanciação manual no setUp com o mock injetado pelo EasyMockExtension
        service = new PlanoService(planoRepository);
    }

    // --- TESTES DE VALIDAÇÃO ---

    @Test
    public void deveTestarExcecaoNomeNulo() {
        replayAll();
        plano.setNome(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(plano));
        assertEquals("Nome inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoNomeVazio() {
        replayAll();
        plano.setNome("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(plano));
        assertEquals("Nome inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoDuracaoNula() {
        replayAll();
        plano.setNome("Plano Anual");
        plano.setDuracao(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(plano));
        assertEquals("Duração inválida", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoDuracaoIgualZero() {
        replayAll();
        plano.setNome("Plano Anual");
        plano.setDuracao(0);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(plano));
        assertEquals("Duração inválida", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoValorNulo() {
        replayAll();
        plano.setNome("Plano Anual");
        plano.setDuracao(12);
        plano.setValor(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(plano));
        assertEquals("Valor inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoValorIgualZero() {
        replayAll();
        plano.setNome("Plano Anual");
        plano.setDuracao(12);
        plano.setValor(0.0f); // ou 0 dependendo da tipagem do seu atributo
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(plano));
        assertEquals("Valor inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveValidarTudo() {
        replayAll();
        plano.setNome("Plano Anual");
        plano.setDuracao(12);
        plano.setValor(100.0f); // ou 100 dependendo do tipo do campo

        assertDoesNotThrow(() -> service.validar(plano));
        verifyAll();
    }

    // --- TESTES DOS MÉTODOS DO SERVIÇO ---

    @Test
    public void deveRetornarListaPlano() {
        expect(planoRepository.findAll()).andReturn(List.of(plano));
        replayAll();

        List<Plano> resultado = service.getPlano();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verifyAll();
    }

    @Test
    public void deveRetornarPlanoPorIDQuandoExistir() {
        expect(planoRepository.findById(1L)).andReturn(Optional.of(plano));
        replayAll();

        Optional<Plano> resultado = service.getPlanoById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verifyAll();
    }

    @Test
    public void deveSalvarPlanoValido() {
        plano.setNome("Plano Anual");
        plano.setDuracao(12);
        plano.setValor(100.0f);

        expect(planoRepository.save(anyObject(Plano.class))).andReturn(plano);
        replayAll();

        Plano resultado = service.salvar(plano);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verifyAll();
    }

    @Test
    public void deveExcluirPlanoQuandoPossuirID() {
        planoRepository.delete(plano);
        expectLastCall().once();

        replayAll();

        assertDoesNotThrow(() -> service.excluir(plano));
        verifyAll();
    }

    @Test
    public void deveLancarExcecaoQuandoExcluirIdNulo() {
        Plano planoSemId = new Plano();
        replayAll();

        assertThrows(NullPointerException.class, () -> service.excluir(planoSemId));
        verifyAll();
    }
}