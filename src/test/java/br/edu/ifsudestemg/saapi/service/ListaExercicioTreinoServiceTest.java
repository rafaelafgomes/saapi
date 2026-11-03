package br.edu.ifsudestemg.saapi.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Exercicio;
import br.edu.ifsudestemg.saapi.model.entity.ListaExercicioTreino;
import br.edu.ifsudestemg.saapi.model.entity.Treino;
import br.edu.ifsudestemg.saapi.model.repository.ListaExercicioTreinoRepository;
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
class ListaExercicioTreinoServiceTest extends EasyMockSupport {

    private ListaExercicioTreino listaExercicioTreino;
    private Exercicio exercicio;
    private Treino treino;

    @Mock
    private ListaExercicioTreinoRepository repository;

    private ListaExercicioTreinoService service;

    @BeforeEach
    void setUp() {
        exercicio = new Exercicio();
        exercicio.setId(1L);

        treino = new Treino();
        treino.setId(1L);

        listaExercicioTreino = new ListaExercicioTreino();
        listaExercicioTreino.setId(1L);

        service = new ListaExercicioTreinoService(repository);
    }


    @Test
    public void deveTestarExcecaoExercicioNulo() {
        replayAll();
        listaExercicioTreino.setExercicio(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(listaExercicioTreino));
        assertEquals("Exercicio inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoExercicioIdNulo() {
        replayAll();
        listaExercicioTreino.setExercicio(new Exercicio());
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(listaExercicioTreino));
        assertEquals("Exercicio inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoExercicioIdIgualZero() {
        replayAll();
        Exercicio exercicioZero = new Exercicio();
        exercicioZero.setId(0L);
        listaExercicioTreino.setExercicio(exercicioZero);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(listaExercicioTreino));
        assertEquals("Exercicio inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoTreinoNulo() {
        replayAll();
        listaExercicioTreino.setExercicio(exercicio);
        listaExercicioTreino.setTreino(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(listaExercicioTreino));
        assertEquals("Treino inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoTreinoIdNulo() {
        replayAll();
        listaExercicioTreino.setExercicio(exercicio);
        listaExercicioTreino.setTreino(new Treino());
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(listaExercicioTreino));
        assertEquals("Treino inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoTreinoIdIgualZero() {
        replayAll();
        Treino treinoZero = new Treino();
        treinoZero.setId(0L);
        listaExercicioTreino.setExercicio(exercicio);
        listaExercicioTreino.setTreino(treinoZero);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(listaExercicioTreino));
        assertEquals("Treino inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveValidarTudo() {
        replayAll();
        listaExercicioTreino.setExercicio(exercicio);
        listaExercicioTreino.setTreino(treino);

        assertDoesNotThrow(() -> service.validar(listaExercicioTreino));
        verifyAll();
    }


    @Test
    public void deveRetornarListaExercicioTreino() {
        expect(repository.findAll()).andReturn(List.of(listaExercicioTreino));
        replayAll();

        List<ListaExercicioTreino> resultado = service.getListaExercicioTreino();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verifyAll();
    }

    @Test
    public void deveRetornarListaPorTreino() {
        expect(repository.findAllByTreino(treino)).andReturn(List.of(listaExercicioTreino));
        replayAll();

        List<ListaExercicioTreino> resultado = service.getListaExercicioTreinoByTreino(treino);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verifyAll();
    }

    @Test
    public void deveRetornarListaExercicioTreinoPorIDQuandoExistir() {
        expect(repository.findById(1L)).andReturn(Optional.of(listaExercicioTreino));
        replayAll();

        Optional<ListaExercicioTreino> resultado = service.getListaExercicioTreinoById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verifyAll();
    }

    @Test
    public void deveSalvarListaExercicioTreinoValida() {
        listaExercicioTreino.setExercicio(exercicio);
        listaExercicioTreino.setTreino(treino);

        expect(repository.save(anyObject(ListaExercicioTreino.class))).andReturn(listaExercicioTreino);
        replayAll();

        ListaExercicioTreino resultado = service.salvar(listaExercicioTreino);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verifyAll();
    }

    @Test
    public void deveExcluirListaExercicioTreinoQuandoPossuirID() {
        repository.delete(listaExercicioTreino);
        expectLastCall().once();

        replayAll();

        assertDoesNotThrow(() -> service.excluir(listaExercicioTreino));
        verifyAll();
    }

    @Test
    public void deveLancarExcecaoQuandoExcluirIdNulo() {
        ListaExercicioTreino objetoSemId = new ListaExercicioTreino();
        replayAll();

        assertThrows(NullPointerException.class, () -> service.excluir(objetoSemId));
        verifyAll();
    }
}