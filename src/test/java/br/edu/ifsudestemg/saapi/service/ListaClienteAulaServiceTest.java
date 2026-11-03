package br.edu.ifsudestemg.saapi.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.ListaClienteAula;
import br.edu.ifsudestemg.saapi.model.repository.ListaClienteAulaRepository;
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
class ListaClienteAulaServiceTest extends EasyMockSupport {

    private ListaClienteAula listaClienteAula;
    private Cliente cliente;
    private Aula aula;

    @Mock
    private ListaClienteAulaRepository repository;

    private ListaClienteAulaService service;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);

        aula = new Aula();
        aula.setId(1L);

        listaClienteAula = new ListaClienteAula();
        listaClienteAula.setId(1L);

        service = new ListaClienteAulaService(repository);
    }


    @Test
    public void deveTestarExcecaoClienteNulo() {
        replayAll();
        listaClienteAula.setCliente(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(listaClienteAula));
        assertEquals("Cliente inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoClienteIdNulo() {
        replayAll();
        listaClienteAula.setCliente(new Cliente());
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(listaClienteAula));
        assertEquals("Cliente inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoClienteIdIgualZero() {
        replayAll();
        Cliente clienteZero = new Cliente();
        clienteZero.setId(0L);
        listaClienteAula.setCliente(clienteZero);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(listaClienteAula));
        assertEquals("Cliente inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoAulaNula() {
        replayAll();
        listaClienteAula.setCliente(cliente);
        listaClienteAula.setAula(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(listaClienteAula));
        assertEquals("Aula inválida", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoAulaIdNulo() {
        replayAll();
        listaClienteAula.setCliente(cliente);
        listaClienteAula.setAula(new Aula());
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(listaClienteAula));
        assertEquals("Aula inválida", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoAulaIdIgualZero() {
        replayAll();
        Aula aulaZero = new Aula();
        aulaZero.setId(0L);
        listaClienteAula.setCliente(cliente);
        listaClienteAula.setAula(aulaZero);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(listaClienteAula));
        assertEquals("Aula inválida", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveValidarTudo() {
        replayAll();
        listaClienteAula.setCliente(cliente);
        listaClienteAula.setAula(aula);

        assertDoesNotThrow(() -> service.validar(listaClienteAula));
        verifyAll();
    }

    @Test
    public void deveRetornarListaClienteAula() {
        expect(repository.findAll()).andReturn(List.of(listaClienteAula));
        replayAll();

        List<ListaClienteAula> resultado = service.getListaClienteAula();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verifyAll();
    }

    @Test
    public void deveRetornarListaPorAula() {
        expect(repository.findAllByAula(aula)).andReturn(List.of(listaClienteAula));
        replayAll();

        List<ListaClienteAula> resultado = service.getListaClienteAulaByAula(aula);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verifyAll();
    }

    @Test
    public void deveRetornarListaClienteAulaPorIDQuandoExistir() {
        expect(repository.findById(1L)).andReturn(Optional.of(listaClienteAula));
        replayAll();

        Optional<ListaClienteAula> resultado = service.getListaClienteAulaById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verifyAll();
    }

    @Test
    public void deveSalvarListaClienteAulaValida() {
        listaClienteAula.setCliente(cliente);
        listaClienteAula.setAula(aula);

        expect(repository.save(anyObject(ListaClienteAula.class))).andReturn(listaClienteAula);
        replayAll();

        ListaClienteAula resultado = service.salvar(listaClienteAula);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verifyAll();
    }

    @Test
    public void deveExcluirListaClienteAulaQuandoPossuirID() {
        repository.delete(listaClienteAula);
        expectLastCall().once();

        replayAll();

        assertDoesNotThrow(() -> service.excluir(listaClienteAula));
        verifyAll();
    }

    @Test
    public void deveLancarExcecaoQuandoExcluirIdNulo() {
        ListaClienteAula objetoSemId = new ListaClienteAula();
        replayAll();

        assertThrows(NullPointerException.class, () -> service.excluir(objetoSemId));
        verifyAll();
    }
}