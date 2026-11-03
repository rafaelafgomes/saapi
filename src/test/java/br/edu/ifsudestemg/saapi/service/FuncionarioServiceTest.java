package br.edu.ifsudestemg.saapi.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Cargo;
import br.edu.ifsudestemg.saapi.model.entity.Funcionario;
import br.edu.ifsudestemg.saapi.model.repository.FuncionarioRepository;
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
class FuncionarioServiceTest extends EasyMockSupport {

    private Funcionario funcionario;
    private Cargo cargo;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    private FuncionarioService service;

    @BeforeEach
    void setUp() {
        cargo = new Cargo();
        cargo.setId(1L);

        funcionario = new Funcionario();
        funcionario.setId(1L);

        service = new FuncionarioService(funcionarioRepository);
    }

    private void preencherFuncionarioValido() {
        funcionario.setEmail("funcionario@email.com");
        funcionario.setDataAdmissao("2026-01-10");
        funcionario.setNome("Carlos Silva");
        funcionario.setCpf("12345678901");
        funcionario.setTelefone("32999998888");
        funcionario.setStatus(true);
        funcionario.setLogradouro("Rua das Flores");
        funcionario.setNumero(100);
        funcionario.setBairro("Centro");
        funcionario.setCidade("Juiz de Fora");
        funcionario.setEstado("MG");
        funcionario.setCep("36000000");
        funcionario.setDataNascimento("1990-05-15");
        funcionario.setCargo(cargo);
    }

    @Test
    public void deveTestarExcecaoEmailNulo() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setEmail(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("Email inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoEmailVazio() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setEmail("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("Email inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoDataAdmissaoNula() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setDataAdmissao(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("Data de Admissão inválida", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoDataAdmissaoVazia() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setDataAdmissao("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("Data de Admissão inválida", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoNomeNulo() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setNome(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("Nome inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoNomeVazio() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setNome("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("Nome inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoCpfNulo() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setCpf(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("Cpf inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoCpfVazio() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setCpf("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("Cpf inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoCpfTamanhoInvalido() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setCpf("123456789"); // Menos de 11 dígitos
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("Cpf inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoTelefoneNulo() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setTelefone(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("Telefone inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoTelefoneVazio() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setTelefone("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("Telefone inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoStatusNulo() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setStatus(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("Status inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoLogradouroNulo() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setLogradouro(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("Logradouro inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoLogradouroVazio() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setLogradouro("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("Logradouro inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoNumeroNulo() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setNumero(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("Numero Inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoNumeroIgualZero() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setNumero(0);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("Numero Inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoBairroNulo() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setBairro(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("Bairro Inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoBairroVazio() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setBairro("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("Bairro Inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoCidadeNula() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setCidade(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("Cidade Inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoCidadeVazia() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setCidade("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("Cidade Inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoEstadoNulo() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setEstado(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("Estado Inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoEstadoVazio() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setEstado("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("Estado Inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoCepNulo() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setCep(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("CEP inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoCepVazio() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setCep("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("CEP inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoCepTamanhoInvalido() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setCep("360000"); // Diferente de 8 dígitos
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("CEP inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoDataNascimentoNula() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setDataNascimento(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("Data de Nascimento inválida", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoDataNascimentoVazia() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setDataNascimento("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("Data de Nascimento inválida", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoCargoNulo() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setCargo(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("Cargo inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoCargoIdNulo() {
        replayAll();
        preencherFuncionarioValido();
        funcionario.setCargo(new Cargo());
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("Cargo inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoCargoIdIgualZero() {
        replayAll();
        Cargo cargoZero = new Cargo();
        cargoZero.setId(0L);
        preencherFuncionarioValido();
        funcionario.setCargo(cargoZero);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(funcionario));
        assertEquals("Cargo inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveValidarTudo() {
        replayAll();
        preencherFuncionarioValido();

        assertDoesNotThrow(() -> service.validar(funcionario));
        verifyAll();
    }

    @Test
    public void deveRetornarListaFuncionario() {
        expect(funcionarioRepository.findAll()).andReturn(List.of(funcionario));
        replayAll();

        List<Funcionario> resultado = service.getFuncionario();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verifyAll();
    }

    @Test
    public void deveRetornarFuncionariosAtivos() {
        funcionario.setStatus(true);
        expect(funcionarioRepository.findByStatusTrue()).andReturn(List.of(funcionario));
        replayAll();

        List<Funcionario> resultado = service.getFuncionariosAtivos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getStatus());
        verifyAll();
    }

    @Test
    public void deveRetornarFuncionarioPorIDQuandoExistir() {
        expect(funcionarioRepository.findById(1L)).andReturn(Optional.of(funcionario));
        replayAll();

        Optional<Funcionario> resultado = service.getFuncionarioById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verifyAll();
    }

    @Test
    public void deveSalvarFuncionarioValido() {
        preencherFuncionarioValido();

        expect(funcionarioRepository.save(anyObject(Funcionario.class))).andReturn(funcionario);
        replayAll();

        Funcionario resultado = service.salvar(funcionario);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verifyAll();
    }

    @Test
    public void deveExcluirFuncionarioQuandoPossuirID() {
        funcionarioRepository.delete(funcionario);
        expectLastCall().once();

        replayAll();

        assertDoesNotThrow(() -> service.excluir(funcionario));
        verifyAll();
    }

    @Test
    public void deveLancarExcecaoQuandoExcluirIdNulo() {
        Funcionario funcionarioSemId = new Funcionario();
        replayAll();

        assertThrows(NullPointerException.class, () -> service.excluir(funcionarioSemId));
        verifyAll();
    }
}