package br.edu.ifsudestemg.saapi.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Cargo;
import br.edu.ifsudestemg.saapi.model.repository.CargoRepository;
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
class CargoServiceTest extends EasyMockSupport {

    private Cargo cargo;

    @Mock
    private CargoRepository cargoRepository;

    private CargoService service;

    @BeforeEach
    void setUp() {
        cargo = new Cargo();
        cargo.setId(1L);

        service = new CargoService(cargoRepository);
    }

    @Test
    public void deveTestarExcecaoNomeNulo() {
        replayAll();
        cargo.setNome(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cargo));
        assertEquals("Nome inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoNomeVazio() {
        replayAll();
        cargo.setNome("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cargo));
        assertEquals("Nome inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveValidarTudo() {
        replayAll();
        cargo.setNome("Gerente");

        assertDoesNotThrow(() -> service.validar(cargo));
        verifyAll();
    }

    @Test
    public void deveRetornarListaCargo() {
        expect(cargoRepository.findAll()).andReturn(List.of(cargo));
        replayAll();

        List<Cargo> resultado = service.getCargo();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verifyAll();
    }

    @Test
    public void deveRetornarCargoPorIDQuandoExistir() {
        expect(cargoRepository.findById(1L)).andReturn(Optional.of(cargo));
        replayAll();

        Optional<Cargo> resultado = service.getCargoById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verifyAll();
    }

    @Test
    public void deveSalvarCargoValido() {
        cargo.setNome("Gerente");

        expect(cargoRepository.save(anyObject(Cargo.class))).andReturn(cargo);
        replayAll();

        Cargo resultado = service.salvar(cargo);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verifyAll();
    }

    @Test
    public void deveExcluirCargoQuandoPossuirID() {
        cargoRepository.delete(cargo);
        expectLastCall().once();

        replayAll();

        assertDoesNotThrow(() -> service.excluir(cargo));
        verifyAll();
    }

    @Test
    public void deveLancarExcecaoQuandoExcluirIdNulo() {
        Cargo cargoSemId = new Cargo();
        replayAll();

        assertThrows(NullPointerException.class, () -> service.excluir(cargoSemId));
        verifyAll();
    }
}