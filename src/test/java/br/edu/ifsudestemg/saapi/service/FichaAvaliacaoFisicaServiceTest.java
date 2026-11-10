package br.edu.ifsudestemg.saapi.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.FichaAvaliacaoFisica;
import br.edu.ifsudestemg.saapi.model.entity.Funcionario;
import br.edu.ifsudestemg.saapi.model.repository.FichaAvaliacaoFisicaRepository;
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
class FichaAvaliacaoFisicaServiceTest extends EasyMockSupport {

    private FichaAvaliacaoFisica ficha;
    private Cliente cliente;
    private Funcionario funcionario;

    @Mock
    private FichaAvaliacaoFisicaRepository repository;

    private FichaAvaliacaoFisicaService service;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);

        funcionario = new Funcionario();
        funcionario.setId(1L);

        ficha = new FichaAvaliacaoFisica();
        ficha.setId(1L);

        service = new FichaAvaliacaoFisicaService(repository);
    }

    private void preencherFichaValida() {
        ficha.setDataFicha("2026-11-03");
        ficha.setPeso(70.0f);
        ficha.setAltura(1.75f);
        ficha.setCliente(cliente);
        ficha.setFuncionario(funcionario);
    }

    @Test
    public void deveTestarExcecaoDataFichaNula() {
        replayAll();
        preencherFichaValida();
        ficha.setDataFicha(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(ficha));
        assertEquals("Data inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoDataFichaVazia() {
        replayAll();
        preencherFichaValida();
        ficha.setDataFicha("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(ficha));
        assertEquals("Data inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoPesoNulo() {
        replayAll();
        preencherFichaValida();
        ficha.setPeso(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(ficha));
        assertEquals("Peso inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoPesoIgualZero() {
        replayAll();
        preencherFichaValida();
        ficha.setPeso(0.0f);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(ficha));
        assertEquals("Peso inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoAlturaNula() {
        replayAll();
        preencherFichaValida();
        ficha.setAltura(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(ficha));
        assertEquals("Altura inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoAlturaIgualZero() {
        replayAll();
        preencherFichaValida();
        ficha.setAltura(0.0f);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(ficha));
        assertEquals("Altura inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoClienteNulo() {
        replayAll();
        preencherFichaValida();
        ficha.setCliente(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(ficha));
        assertEquals("Cliente inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoClienteIdNulo() {
        replayAll();
        preencherFichaValida();
        ficha.setCliente(new Cliente());
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(ficha));
        assertEquals("Cliente inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoClienteIdIgualZero() {
        replayAll();
        Cliente clienteZero = new Cliente();
        clienteZero.setId(0L);
        preencherFichaValida();
        ficha.setCliente(clienteZero);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(ficha));
        assertEquals("Cliente inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoFuncionarioNulo() {
        replayAll();
        preencherFichaValida();
        ficha.setFuncionario(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(ficha));
        assertEquals("Funcionário inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoFuncionarioIdNulo() {
        replayAll();
        preencherFichaValida();
        ficha.setFuncionario(new Funcionario());
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(ficha));
        assertEquals("Funcionário inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoFuncionarioIdIgualZero() {
        replayAll();
        Funcionario funcZero = new Funcionario();
        funcZero.setId(0L);
        preencherFichaValida();
        ficha.setFuncionario(funcZero);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(ficha));
        assertEquals("Funcionário inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveValidarTudo() {
        replayAll();
        preencherFichaValida();

        assertDoesNotThrow(() -> service.validar(ficha));
        verifyAll();
    }

    @Test
    public void deveCalcularIMCMasculinoAbaixoDoPeso() {
        replayAll();
        String resultado = service.calcularIMC(20.6f, 1.0f, true); // IMC ~ 15.43
        assertTrue(resultado.contains("Abaixo do peso"));
        verifyAll();
    }

    @Test
    public void deveCalcularIMCMasculinoPesoNormal() {
        replayAll();
        String resultado = service.calcularIMC(26.4f, 1.0f, true); // IMC ~ 23.15
        assertTrue(resultado.contains("Peso normal"));
        verifyAll();
    }

    @Test
    public void deveCalcularIMCMasculinoMarginalmenteAcimaDoPeso() {
        replayAll();
        String resultado = service.calcularIMC(27.8f, 1.0f, true); // IMC ~ 27.16
        assertTrue(resultado.contains("Marginalmente acima do peso"));
        verifyAll();
    }

    @Test
    public void deveCalcularIMCMasculinoAcimaDoPesoIdeal() {
        replayAll();
        String resultado = service.calcularIMC(31.1f, 1.0f, true); // IMC ~ 29.32
        assertTrue(resultado.contains("Acima do peso ideal"));
        verifyAll();
    }

    @Test
    public void deveCalcularIMCMasculinoObesidadeGrauI() {
        replayAll();
        String resultado = service.calcularIMC(34.9f, 1.0f, true); // IMC ~ 33.95
        assertTrue(resultado.contains("Obesidade Grau I"));
        verifyAll();
    }

    @Test
    public void deveCalcularIMCMasculinoObesidadeGrauII() {
        replayAll();
        String resultado = service.calcularIMC(39.9f, 1.0f, true); // IMC ~ 33.95
        assertTrue(resultado.contains("Obesidade Grau II"));
        verifyAll();
    }

    @Test
    public void deveCalcularIMCMasculinoObesidadeGrauIII() {
        replayAll();
        String resultado = service.calcularIMC(40.0f, 1.0f, true); // IMC ~ 33.95
        assertTrue(resultado.contains("Obesidade Grau III"));
        verifyAll();
    }

    @Test
    public void deveCalcularIMCFemininoAbaixoDoPeso() {
        replayAll();
        String resultado = service.calcularIMC(19.0f, 1.0f, false); // IMC ~ 16.53
        assertTrue(resultado.contains("Abaixo do peso"));
        verifyAll();
    }

    @Test
    public void deveCalcularIMCFemininoPesoNormal() {
        replayAll();
        String resultado = service.calcularIMC(25.8f, 1.0f, false); // IMC ~ 22.04
        assertTrue(resultado.contains("Peso normal"));
        verifyAll();
    }

    @Test
    public void deveCalcularIMCFemininoMarginalmenteAcimaDoPeso() {
        replayAll();
        String resultado = service.calcularIMC(27.3f, 1.0f, false); // IMC ~ 26.45
        assertTrue(resultado.contains("Marginalmente acima do peso"));
        verifyAll();
    }

    @Test
    public void deveCalcularIMCFemininoAcimaDoPesoIdeal() {
        replayAll();
        String resultado = service.calcularIMC(32.3f, 1.0f, false); // IMC ~ 29.38
        assertTrue(resultado.contains("Acima do peso ideal"));
        verifyAll();
    }

    @Test
    public void deveCalcularIMCFemininoObesidadeGrauI() {
        replayAll();
        String resultado = service.calcularIMC(34.9f, 1.0f, false); // IMC ~ 34.89
        assertTrue(resultado.contains("Obesidade Grau I"));
        verifyAll();
    }

    @Test
    public void deveCalcularIMCFemininoObesidadeGrauII() {
        replayAll();
        String resultado = service.calcularIMC(39.9f, 1.0f, false); // IMC ~ 34.89
        assertTrue(resultado.contains("Obesidade Grau II"));
        verifyAll();
    }

    @Test
    public void deveCalcularIMCFemininoObesidadeGrauIII() {
        replayAll();
        String resultado = service.calcularIMC(40.0f, 1.0f, false); // IMC ~ 34.89
        assertTrue(resultado.contains("Obesidade Grau III"));
        verifyAll();
    }

    @Test
    public void deveRetornarListaFichaAvaliacaoFisica() {
        expect(repository.findAll()).andReturn(List.of(ficha));
        replayAll();

        List<FichaAvaliacaoFisica> resultado = service.getFichaAvaliacaoFisica();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verifyAll();
    }

    @Test
    public void deveRetornarFichaAvaliacaoFisicaPorIDQuandoExistir() {
        expect(repository.findById(1L)).andReturn(Optional.of(ficha));
        replayAll();

        Optional<FichaAvaliacaoFisica> resultado = service.getFichaAvaliacaoFisicaById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verifyAll();
    }

    @Test
    public void deveSalvarFichaAvaliacaoFisicaValida() {
        preencherFichaValida();

        expect(repository.save(anyObject(FichaAvaliacaoFisica.class))).andReturn(ficha);
        replayAll();

        FichaAvaliacaoFisica resultado = service.salvar(ficha);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verifyAll();
    }

    @Test
    public void deveExcluirFichaAvaliacaoFisicaQuandoPossuirID() {
        repository.delete(ficha);
        expectLastCall().once();

        replayAll();

        assertDoesNotThrow(() -> service.excluir(ficha));
        verifyAll();
    }

    @Test
    public void deveLancarExcecaoQuandoExcluirIdNulo() {
        FichaAvaliacaoFisica fichaSemId = new FichaAvaliacaoFisica();
        replayAll();

        assertThrows(NullPointerException.class, () -> service.excluir(fichaSemId));
        verifyAll();
    }
}