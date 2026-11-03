package br.edu.ifsudestemg.saapi.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.entity.Equipamento;
import br.edu.ifsudestemg.saapi.model.entity.ListaEquipamentoAula;
import br.edu.ifsudestemg.saapi.model.repository.ListaEquipamentoAulaRepository;
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
class ListaEquipamentoAulaServiceTest extends EasyMockSupport {

    private ListaEquipamentoAula listaEquipamentoAula;
    private Equipamento equipamento;
    private Aula aula;

    @Mock
    private ListaEquipamentoAulaRepository repository;

    private ListaEquipamentoAulaService service;

    @BeforeEach
    void setUp() {
        equipamento = new Equipamento();
        equipamento.setId(1L);

        aula = new Aula();
        aula.setId(1L);

        listaEquipamentoAula = new ListaEquipamentoAula();
        listaEquipamentoAula.setId(1L);

        service = new ListaEquipamentoAulaService(repository);
    }

    // --- TESTES DE VALIDAÇÃO ---

    @Test
    public void deveTestarExcecaoQuantidadeNula() {
        replayAll();
        listaEquipamentoAula.setQuantidade(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(listaEquipamentoAula));
        assertEquals("Quantidade inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoQuantidadeIgualZero() {
        replayAll();
        listaEquipamentoAula.setQuantidade(0);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(listaEquipamentoAula));
        assertEquals("Quantidade inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoEquipamentoNulo() {
        replayAll();
        listaEquipamentoAula.setQuantidade(5);
        listaEquipamentoAula.setEquipamento(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(listaEquipamentoAula));
        assertEquals("Equipamento inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoEquipamentoIdNulo() {
        replayAll();
        listaEquipamentoAula.setQuantidade(5);
        listaEquipamentoAula.setEquipamento(new Equipamento());
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(listaEquipamentoAula));
        assertEquals("Equipamento inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoEquipamentoIdIgualZero() {
        replayAll();
        Equipamento eqZero = new Equipamento();
        eqZero.setId(0L);
        listaEquipamentoAula.setQuantidade(5);
        listaEquipamentoAula.setEquipamento(eqZero);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(listaEquipamentoAula));
        assertEquals("Equipamento inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoAulaNula() {
        replayAll();
        listaEquipamentoAula.setQuantidade(5);
        listaEquipamentoAula.setEquipamento(equipamento);
        listaEquipamentoAula.setAula(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(listaEquipamentoAula));
        assertEquals("Aula inválida", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoAulaIdNulo() {
        replayAll();
        listaEquipamentoAula.setQuantidade(5);
        listaEquipamentoAula.setEquipamento(equipamento);
        listaEquipamentoAula.setAula(new Aula());
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(listaEquipamentoAula));
        assertEquals("Aula inválida", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoAulaIdIgualZero() {
        replayAll();
        Aula aulaZero = new Aula();
        aulaZero.setId(0L);
        listaEquipamentoAula.setQuantidade(5);
        listaEquipamentoAula.setEquipamento(equipamento);
        listaEquipamentoAula.setAula(aulaZero);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(listaEquipamentoAula));
        assertEquals("Aula inválida", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveValidarTudo() {
        replayAll();
        listaEquipamentoAula.setQuantidade(5);
        listaEquipamentoAula.setEquipamento(equipamento);
        listaEquipamentoAula.setAula(aula);

        assertDoesNotThrow(() -> service.validar(listaEquipamentoAula));
        verifyAll();
    }

    // --- TESTES DOS MÉTODOS DO SERVIÇO ---

    @Test
    public void deveRetornarListaEquipamentoAula() {
        expect(repository.findAll()).andReturn(List.of(listaEquipamentoAula));
        replayAll();

        List<ListaEquipamentoAula> resultado = service.getListaEquipamentoAula();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verifyAll();
    }

    @Test
    public void deveRetornarListaPorAula() {
        expect(repository.findAllByAula(aula)).andReturn(List.of(listaEquipamentoAula));
        replayAll();

        List<ListaEquipamentoAula> resultado = service.getListaEquipamentoAulaByAula(aula);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verifyAll();
    }

    @Test
    public void deveRetornarListaEquipamentoAulaPorIDQuandoExistir() {
        expect(repository.findById(1L)).andReturn(Optional.of(listaEquipamentoAula));
        replayAll();

        Optional<ListaEquipamentoAula> resultado = service.getListaEquipamentoAulaById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verifyAll();
    }

    @Test
    public void deveSalvarListaEquipamentoAulaValida() {
        listaEquipamentoAula.setQuantidade(5);
        listaEquipamentoAula.setEquipamento(equipamento);
        listaEquipamentoAula.setAula(aula);

        expect(repository.save(anyObject(ListaEquipamentoAula.class))).andReturn(listaEquipamentoAula);
        replayAll();

        ListaEquipamentoAula resultado = service.salvar(listaEquipamentoAula);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verifyAll();
    }

    @Test
    public void deveExcluirListaEquipamentoAulaQuandoPossuirID() {
        repository.delete(listaEquipamentoAula);
        expectLastCall().once();

        replayAll();

        assertDoesNotThrow(() -> service.excluir(listaEquipamentoAula));
        verifyAll();
    }

    @Test
    public void deveLancarExcecaoQuandoExcluirIdNulo() {
        ListaEquipamentoAula objetoSemId = new ListaEquipamentoAula();
        replayAll();

        assertThrows(NullPointerException.class, () -> service.excluir(objetoSemId));
        verifyAll();
    }
}