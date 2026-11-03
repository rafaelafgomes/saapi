package br.edu.ifsudestemg.saapi.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Equipamento;
import br.edu.ifsudestemg.saapi.model.repository.EquipamentoRepository;
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
class EquipamentoServiceTest extends EasyMockSupport {

    private Equipamento equipamento;

    @Mock
    private EquipamentoRepository equipamentoRepository;

    private EquipamentoService service;

    @BeforeEach
    void setUp() {
        equipamento = new Equipamento();
        equipamento.setId(1L);

        // Instanciação manual no setUp com o mock injetado pelo EasyMockExtension
        service = new EquipamentoService(equipamentoRepository);
    }

    // --- TESTES DE VALIDAÇÃO ---

    @Test
    public void deveTestarExcecaoNomeNulo() {
        replayAll();
        equipamento.setNome(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(equipamento));
        assertEquals("Nome inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoNomeVazio() {
        replayAll();
        equipamento.setNome("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(equipamento));
        assertEquals("Nome inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoModeloNulo() {
        replayAll();
        equipamento.setNome("Esteira");
        equipamento.setModelo(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(equipamento));
        assertEquals("Modelo inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoModeloVazio() {
        replayAll();
        equipamento.setNome("Esteira");
        equipamento.setModelo("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(equipamento));
        assertEquals("Modelo inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoQuantidadeNula() {
        replayAll();
        equipamento.setNome("Esteira");
        equipamento.setModelo("LX160");
        equipamento.setQuantidade(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(equipamento));
        assertEquals("Quantidade inválida", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoQuantidadeIgualZero() {
        replayAll();
        equipamento.setNome("Esteira");
        equipamento.setModelo("LX160");
        equipamento.setQuantidade(0);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(equipamento));
        assertEquals("Quantidade inválida", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoDisponivelNulo() {
        replayAll();
        equipamento.setNome("Esteira");
        equipamento.setModelo("LX160");
        equipamento.setQuantidade(5);
        equipamento.setDisponivel(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(equipamento));
        assertEquals("Disponibilidade inválida", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoLocalNulo() {
        replayAll();
        equipamento.setNome("Esteira");
        equipamento.setModelo("LX160");
        equipamento.setQuantidade(5);
        equipamento.setDisponivel(true);
        equipamento.setLocal(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(equipamento));
        assertEquals("Local inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoLocalVazio() {
        replayAll();
        equipamento.setNome("Esteira");
        equipamento.setModelo("LX160");
        equipamento.setQuantidade(5);
        equipamento.setDisponivel(true);
        equipamento.setLocal("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(equipamento));
        assertEquals("Local inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveValidarTudo() {
        replayAll();
        equipamento.setNome("Esteira");
        equipamento.setModelo("LX160");
        equipamento.setQuantidade(5);
        equipamento.setDisponivel(true);
        equipamento.setLocal("Sala de Cardio");

        assertDoesNotThrow(() -> service.validar(equipamento));
        verifyAll();
    }

    // --- TESTES DOS MÉTODOS DO SERVIÇO ---

    @Test
    public void deveRetornarListaEquipamento() {
        expect(equipamentoRepository.findAll()).andReturn(List.of(equipamento));
        replayAll();

        List<Equipamento> resultado = service.getEquipamento();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verifyAll();
    }

    @Test
    public void deveRetornarEquipamentosAtivos() {
        equipamento.setDisponivel(true);
        expect(equipamentoRepository.findByDisponivelTrue()).andReturn(List.of(equipamento));
        replayAll();

        List<Equipamento> resultado = service.getEquipamentosAtivos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getDisponivel());
        verifyAll();
    }

    @Test
    public void deveRetornarEquipamentoPorIDQuandoExistir() {
        expect(equipamentoRepository.findById(1L)).andReturn(Optional.of(equipamento));
        replayAll();

        Optional<Equipamento> resultado = service.getEquipamentoById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verifyAll();
    }

    @Test
    public void deveSalvarEquipamentoValido() {
        equipamento.setNome("Esteira");
        equipamento.setModelo("LX160");
        equipamento.setQuantidade(5);
        equipamento.setDisponivel(true);
        equipamento.setLocal("Sala de Cardio");

        expect(equipamentoRepository.save(anyObject(Equipamento.class))).andReturn(equipamento);
        replayAll();

        Equipamento resultado = service.salvar(equipamento);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verifyAll();
    }

    @Test
    public void deveExcluirEquipamentoQuandoPossuirID() {
        equipamentoRepository.delete(equipamento);
        expectLastCall().once();

        replayAll();

        assertDoesNotThrow(() -> service.excluir(equipamento));
        verifyAll();
    }

    @Test
    public void deveLancarExcecaoQuandoExcluirIdNulo() {
        Equipamento equipamentoSemId = new Equipamento();
        replayAll();

        assertThrows(NullPointerException.class, () -> service.excluir(equipamentoSemId));
        verifyAll();
    }
}