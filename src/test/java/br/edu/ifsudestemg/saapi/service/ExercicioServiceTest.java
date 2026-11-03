package br.edu.ifsudestemg.saapi.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Exercicio;
import br.edu.ifsudestemg.saapi.model.repository.ExercicioRepository;
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
class ExercicioServiceTest extends EasyMockSupport {

    private Exercicio exercicio;

    @Mock
    private ExercicioRepository exercicioRepository;

    private ExercicioService service;

    @BeforeEach
    void setUp() {
        exercicio = new Exercicio();
        exercicio.setId(1L);

        // Instanciação manual no setUp com o mock injetado pelo EasyMockExtension
        service = new ExercicioService(exercicioRepository);
    }

    // --- TESTES DE VALIDAÇÃO ---

    @Test
    public void deveTestarExcecaoNomeNulo() {
        replayAll();
        exercicio.setNome(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(exercicio));
        assertEquals("Nome inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoNomeVazio() {
        replayAll();
        exercicio.setNome("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(exercicio));
        assertEquals("Nome inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveValidarTudo() {
        replayAll();
        exercicio.setNome("Supino Reto");

        assertDoesNotThrow(() -> service.validar(exercicio));
        verifyAll();
    }

    // --- TESTES DOS MÉTODOS DO SERVIÇO ---

    @Test
    public void deveRetornarListaExercicio() {
        expect(exercicioRepository.findAll()).andReturn(List.of(exercicio));
        replayAll();

        List<Exercicio> resultado = service.getExercicio();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verifyAll();
    }

    @Test
    public void deveRetornarExercicioPorIDQuandoExistir() {
        expect(exercicioRepository.findById(1L)).andReturn(Optional.of(exercicio));
        replayAll();

        Optional<Exercicio> resultado = service.getExercicioById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verifyAll();
    }

    @Test
    public void deveSalvarExercicioValido() {
        exercicio.setNome("Supino Reto");

        expect(exercicioRepository.save(anyObject(Exercicio.class))).andReturn(exercicio);
        replayAll();

        Exercicio resultado = service.salvar(exercicio);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verifyAll();
    }

    @Test
    public void deveExcluirExercicioQuandoPossuirID() {
        exercicioRepository.delete(exercicio);
        expectLastCall().once();

        replayAll();

        assertDoesNotThrow(() -> service.excluir(exercicio));
        verifyAll();
    }

    @Test
    public void deveLancarExcecaoQuandoExcluirIdNulo() {
        Exercicio exercicioSemId = new Exercicio();
        replayAll();

        assertThrows(NullPointerException.class, () -> service.excluir(exercicioSemId));
        verifyAll();
    }
}