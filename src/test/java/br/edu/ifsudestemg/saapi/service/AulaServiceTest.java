package br.edu.ifsudestemg.saapi.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.entity.Funcionario;
import br.edu.ifsudestemg.saapi.model.repository.AulaRepository;
import br.edu.ifsudestemg.saapi.model.repository.ListaClienteAulaRepository;
import br.edu.ifsudestemg.saapi.model.repository.ListaEquipamentoAulaRepository;
import org.easymock.EasyMockExtension;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(EasyMockExtension.class)
class AulaServiceTest extends EasyMockSupport {

    private Aula aula;
    private Funcionario funcionario;

    @Mock
    private AulaRepository aulaRepository;

    @Mock
    private ListaClienteAulaRepository listaClienteAulaRepository;

    @Mock
    private ListaEquipamentoAulaRepository listaEquipamentoAulaRepository;

    private AulaService service;

    @BeforeEach
    void setUp() {
        funcionario = new Funcionario();
        funcionario.setId(1L);

        aula = new Aula();
        aula.setId(1L);
        service = new AulaService(aulaRepository, listaClienteAulaRepository, listaEquipamentoAulaRepository);
    }

    @Test
    public void deveTestarExcecaoNomeNulo() {
        replayAll();
        aula.setNome(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(aula));
        assertEquals("Nome inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoNomeVazio() {
        replayAll();
        aula.setNome("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(aula));
        assertEquals("Nome inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoDataNula() {
        replayAll();
        aula.setNome("Pilates");
        aula.setDataAula(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(aula));
        assertEquals("Data inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoHoraNula() {
        replayAll();
        aula.setNome("Pilates");
        aula.setDataAula("20/10/2026");
        aula.setHoraAula(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(aula));
        assertEquals("Hora inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoStatusNulo() {
        replayAll();
        aula.setNome("Pilates");
        aula.setDataAula("20/10/2026");
        aula.setHoraAula("14:00");
        aula.setStatus(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(aula));
        assertEquals("Status inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoCapacidadeNula() {
        replayAll();
        aula.setNome("Pilates");
        aula.setDataAula("20/10/2026");
        aula.setHoraAula("14:00");
        aula.setStatus(true);
        aula.setCapacidade(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(aula));
        assertEquals("Capacidade inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoCapacidadeIgualZero() {
        replayAll();
        aula.setNome("Pilates");
        aula.setDataAula("20/10/2026");
        aula.setHoraAula("14:00");
        aula.setStatus(true);
        aula.setCapacidade(0);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(aula));
        assertEquals("Capacidade inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoFuncionarioNulo() {
        replayAll();
        aula.setNome("Pilates");
        aula.setDataAula("20/10/2026");
        aula.setHoraAula("14:00");
        aula.setStatus(true);
        aula.setCapacidade(10);
        aula.setFuncionario(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(aula));
        assertEquals("Funcionário inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoFuncionarioIdNulo() {
        replayAll();
        aula.setNome("Pilates");
        aula.setDataAula("20/10/2026");
        aula.setHoraAula("14:00");
        aula.setStatus(true);
        aula.setCapacidade(10);
        aula.setFuncionario(new Funcionario());
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(aula));
        assertEquals("Funcionário inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoFuncionarioIdIgualZero() {
        replayAll();
        Funcionario funcSemId = new Funcionario();
        funcSemId.setId(0L);
        aula.setNome("Pilates");
        aula.setDataAula("20/10/2026");
        aula.setHoraAula("14:00");
        aula.setStatus(true);
        aula.setCapacidade(10);
        aula.setFuncionario(funcSemId);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(aula));
        assertEquals("Funcionário inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveValidarTudo() {
        replayAll();
        aula.setNome("Pilates");
        aula.setDataAula("20/10/2026");
        aula.setHoraAula("14:00");
        aula.setStatus(true);
        aula.setCapacidade(15);
        aula.setFuncionario(funcionario);

        assertDoesNotThrow(() -> service.validar(aula));
        verifyAll();
    }

    // --- TESTES DOS MÉTODOS DO SERVIÇO ---

    @Test
    public void deveRetornarListaAula() {
        expect(aulaRepository.findAll()).andReturn(List.of(aula));
        replayAll();

        List<Aula> resultado = service.getAula();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verifyAll();
    }

    @Test
    public void deveRetornarAulaPorIDQuandoExistir() {
        expect(aulaRepository.findById(1L)).andReturn(Optional.of(aula));
        replayAll();

        Optional<Aula> resultado = service.getAulaById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verifyAll();
    }

    @Test
    public void deveSalvarAulaValida() {
        aula.setNome("Pilates");
        aula.setDataAula("20/10/2026");
        aula.setHoraAula("14:00");
        aula.setStatus(true);
        aula.setCapacidade(15);
        aula.setFuncionario(funcionario);

        expect(aulaRepository.save(anyObject(Aula.class))).andReturn(aula);
        replayAll();

        Aula resultado = service.salvar(aula);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verifyAll();
    }

    @Test
    public void deveExcluirAulaQuandoPossuirID() {
        listaClienteAulaRepository.deleteAllByAula(aula);
        expectLastCall().once();

        listaEquipamentoAulaRepository.deleteAllByAula(aula);
        expectLastCall().once();

        aulaRepository.delete(aula);
        expectLastCall().once();

        replayAll();

        assertDoesNotThrow(() -> service.excluir(aula));
        verifyAll();
    }

    @Test
    public void deveLancarExcecaoQuandoExcluirIdNulo() {
        Aula aulaSemId = new Aula();
        replayAll();

        assertThrows(NullPointerException.class, () -> service.excluir(aulaSemId));
        verifyAll();
    }
}