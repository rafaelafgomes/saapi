package br.edu.ifsudestemg.saapi.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.Funcionario;
import br.edu.ifsudestemg.saapi.model.entity.Treino;
import br.edu.ifsudestemg.saapi.model.repository.ListaExercicioTreinoRepository;
import br.edu.ifsudestemg.saapi.model.repository.TreinoRepository;
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
class TreinoServiceTest extends EasyMockSupport {

    private Treino treino;
    private Cliente cliente;
    private Funcionario funcionario;

    @Mock
    private TreinoRepository treinoRepository;

    @Mock
    private ListaExercicioTreinoRepository listaExercicioTreinoRepository;

    private TreinoService service;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);

        funcionario = new Funcionario();
        funcionario.setId(1L);

        treino = new Treino();
        treino.setId(1L);

        service = new TreinoService(treinoRepository, listaExercicioTreinoRepository);
    }

    @Test
    public void deveTestarExcecaoNomeNulo() {
        replayAll();
        treino.setNome(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(treino));
        assertEquals("Nome inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoNomeVazio() {
        replayAll();
        treino.setNome("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(treino));
        assertEquals("Nome inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoDataNula() {
        replayAll();
        treino.setNome("Treino A - Hipertrofia");
        treino.setData(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(treino));
        assertEquals("Data inválida", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoDataVazia() {
        replayAll();
        treino.setNome("Treino A - Hipertrofia");
        treino.setData("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(treino));
        assertEquals("Data inválida", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoClienteNulo() {
        replayAll();
        treino.setNome("Treino A - Hipertrofia");
        treino.setData("2026-11-03");
        treino.setCliente(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(treino));
        assertEquals("Cliente inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoClienteIdNulo() {
        replayAll();
        treino.setNome("Treino A - Hipertrofia");
        treino.setData("2026-11-03");
        treino.setCliente(new Cliente());
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(treino));
        assertEquals("Cliente inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoClienteIdIgualZero() {
        replayAll();
        Cliente clienteZero = new Cliente();
        clienteZero.setId(0L);
        treino.setNome("Treino A - Hipertrofia");
        treino.setData("2026-11-03");
        treino.setCliente(clienteZero);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(treino));
        assertEquals("Cliente inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoFuncionarioNulo() {
        replayAll();
        treino.setNome("Treino A - Hipertrofia");
        treino.setData("2026-11-03");
        treino.setCliente(cliente);
        treino.setFuncionario(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(treino));
        assertEquals("Funcionário inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoFuncionarioIdNulo() {
        replayAll();
        treino.setNome("Treino A - Hipertrofia");
        treino.setData("2026-11-03");
        treino.setCliente(cliente);
        treino.setFuncionario(new Funcionario());
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(treino));
        assertEquals("Funcionário inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoFuncionarioIdIgualZero() {
        replayAll();
        Funcionario funcZero = new Funcionario();
        funcZero.setId(0L);
        treino.setNome("Treino A - Hipertrofia");
        treino.setData("2026-11-03");
        treino.setCliente(cliente);
        treino.setFuncionario(funcZero);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(treino));
        assertEquals("Funcionário inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveValidarTudo() {
        replayAll();
        treino.setNome("Treino A - Hipertrofia");
        treino.setData("2026-11-03");
        treino.setCliente(cliente);
        treino.setFuncionario(funcionario);

        assertDoesNotThrow(() -> service.validar(treino));
        verifyAll();
    }

    @Test
    public void deveRetornarListaTreino() {
        expect(treinoRepository.findAll()).andReturn(List.of(treino));
        replayAll();

        List<Treino> resultado = service.getTreino();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verifyAll();
    }

    @Test
    public void deveRetornarTreinoPorIDQuandoExistir() {
        expect(treinoRepository.findById(1L)).andReturn(Optional.of(treino));
        replayAll();

        Optional<Treino> resultado = service.getTreinoById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verifyAll();
    }

    @Test
    public void deveSalvarTreinoValido() {
        treino.setNome("Treino A - Hipertrofia");
        treino.setData("2026-11-03");
        treino.setCliente(cliente);
        treino.setFuncionario(funcionario);

        expect(treinoRepository.save(anyObject(Treino.class))).andReturn(treino);
        replayAll();

        Treino resultado = service.salvar(treino);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verifyAll();
    }

    @Test
    public void deveExcluirTreinoQuandoPossuirID() {
        listaExercicioTreinoRepository.deleteAllByTreino(treino);
        expectLastCall().once();

        treinoRepository.delete(treino);
        expectLastCall().once();

        replayAll();

        assertDoesNotThrow(() -> service.excluir(treino));
        verifyAll();
    }

    @Test
    public void deveLancarExcecaoQuandoExcluirIdNulo() {
        Treino treinoSemId = new Treino();
        replayAll();

        assertThrows(NullPointerException.class, () -> service.excluir(treinoSemId));
        verifyAll();
    }
}