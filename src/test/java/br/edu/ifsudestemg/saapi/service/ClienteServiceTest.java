package br.edu.ifsudestemg.saapi.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.Plano;
import br.edu.ifsudestemg.saapi.model.repository.ClienteRepository;
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
class ClienteServiceTest extends EasyMockSupport {

    private Cliente cliente;
    private Plano plano;

    @Mock
    private ClienteRepository clienteRepository;

    private ClienteService service;

    @BeforeEach
    void setUp() {
        plano = new Plano();
        plano.setId(1L);

        cliente = new Cliente();
        cliente.setId(1L);

        service = new ClienteService(clienteRepository);
    }

    private void preencherClienteValido() {
        cliente.setNome("João Souza");
        cliente.setCpf("12345678901");
        cliente.setTelefone("32988887777");
        cliente.setStatus(true);
        cliente.setSexo(true); // Boolean
        cliente.setLogradouro("Rua Principal");
        cliente.setNumero(50);
        cliente.setBairro("Centro");
        cliente.setCidade("Juiz de Fora");
        cliente.setEstado("MG");
        cliente.setCep("36000000");
        cliente.setDataNascimento("1995-10-20");
        cliente.setPlano(plano);
    }

    @Test
    public void deveTestarExcecaoNomeNulo() {
        replayAll();
        preencherClienteValido();
        cliente.setNome(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cliente));
        assertEquals("Nome inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoNomeVazio() {
        replayAll();
        preencherClienteValido();
        cliente.setNome("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cliente));
        assertEquals("Nome inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoCpfNulo() {
        replayAll();
        preencherClienteValido();
        cliente.setCpf(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cliente));
        assertEquals("Cpf inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoCpfVazio() {
        replayAll();
        preencherClienteValido();
        cliente.setCpf("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cliente));
        assertEquals("Cpf inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoCpfTamanhoInvalido() {
        replayAll();
        preencherClienteValido();
        cliente.setCpf("123456789"); // Menos de 11 dígitos
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cliente));
        assertEquals("Cpf inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoTelefoneNulo() {
        replayAll();
        preencherClienteValido();
        cliente.setTelefone(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cliente));
        assertEquals("Telefone inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoTelefoneVazio() {
        replayAll();
        preencherClienteValido();
        cliente.setTelefone("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cliente));
        assertEquals("Telefone inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoStatusNulo() {
        replayAll();
        preencherClienteValido();
        cliente.setStatus(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cliente));
        assertEquals("Status inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoSexoNulo() {
        replayAll();
        preencherClienteValido();
        cliente.setSexo(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cliente));
        assertEquals("Sexo inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoLogradouroNulo() {
        replayAll();
        preencherClienteValido();
        cliente.setLogradouro(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cliente));
        assertEquals("Logradouro inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoLogradouroVazio() {
        replayAll();
        preencherClienteValido();
        cliente.setLogradouro("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cliente));
        assertEquals("Logradouro inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoNumeroNulo() {
        replayAll();
        preencherClienteValido();
        cliente.setNumero(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cliente));
        assertEquals("Numero inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoNumeroIgualZero() {
        replayAll();
        preencherClienteValido();
        cliente.setNumero(0);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cliente));
        assertEquals("Numero inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoBairroNulo() {
        replayAll();
        preencherClienteValido();
        cliente.setBairro(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cliente));
        assertEquals("Bairro Inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoBairroVazio() {
        replayAll();
        preencherClienteValido();
        cliente.setBairro("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cliente));
        assertEquals("Bairro Inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoCidadeNula() {
        replayAll();
        preencherClienteValido();
        cliente.setCidade(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cliente));
        assertEquals("Cidade Inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoCidadeVazia() {
        replayAll();
        preencherClienteValido();
        cliente.setCidade("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cliente));
        assertEquals("Cidade Inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoEstadoNulo() {
        replayAll();
        preencherClienteValido();
        cliente.setEstado(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cliente));
        assertEquals("Estado Inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoEstadoVazio() {
        replayAll();
        preencherClienteValido();
        cliente.setEstado("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cliente));
        assertEquals("Estado Inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoCepNulo() {
        replayAll();
        preencherClienteValido();
        cliente.setCep(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cliente));
        assertEquals("CEP inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoCepVazio() {
        replayAll();
        preencherClienteValido();
        cliente.setCep("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cliente));
        assertEquals("CEP inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoCepTamanhoInvalido() {
        replayAll();
        preencherClienteValido();
        cliente.setCep("360000"); // Diferente de 8 dígitos
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cliente));
        assertEquals("CEP inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoDataNascimentoNula() {
        replayAll();
        preencherClienteValido();
        cliente.setDataNascimento(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cliente));
        assertEquals("Data de Nascimento inválida", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoDataNascimentoVazia() {
        replayAll();
        preencherClienteValido();
        cliente.setDataNascimento("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cliente));
        assertEquals("Data de Nascimento inválida", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoPlanoNulo() {
        replayAll();
        preencherClienteValido();
        cliente.setPlano(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cliente));
        assertEquals("Plano inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoPlanoIdNulo() {
        replayAll();
        preencherClienteValido();
        cliente.setPlano(new Plano());
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cliente));
        assertEquals("Plano inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoPlanoIdIgualZero() {
        replayAll();
        Plano planoZero = new Plano();
        planoZero.setId(0L);
        preencherClienteValido();
        cliente.setPlano(planoZero);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(cliente));
        assertEquals("Plano inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveValidarTudo() {
        replayAll();
        preencherClienteValido();

        assertDoesNotThrow(() -> service.validar(cliente));
        verifyAll();
    }

    @Test
    public void deveRetornarListaCliente() {
        expect(clienteRepository.findAll()).andReturn(List.of(cliente));
        replayAll();

        List<Cliente> resultado = service.getCliente();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verifyAll();
    }

    @Test
    public void deveRetornarClientesAtivos() {
        cliente.setStatus(true);
        expect(clienteRepository.findByStatusTrue()).andReturn(List.of(cliente));
        replayAll();

        List<Cliente> resultado = service.getClientesAtivos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getStatus());
        verifyAll();
    }

    @Test
    public void deveRetornarClientePorIDQuandoExistir() {
        expect(clienteRepository.findById(1L)).andReturn(Optional.of(cliente));
        replayAll();

        Optional<Cliente> resultado = service.getClienteById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verifyAll();
    }

    @Test
    public void deveSalvarClienteValido() {
        preencherClienteValido();

        expect(clienteRepository.save(anyObject(Cliente.class))).andReturn(cliente);
        replayAll();

        Cliente resultado = service.salvar(cliente);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verifyAll();
    }

    @Test
    public void deveExcluirClienteQuandoPossuirID() {
        clienteRepository.delete(cliente);
        expectLastCall().once();

        replayAll();

        assertDoesNotThrow(() -> service.excluir(cliente));
        verifyAll();
    }

    @Test
    public void deveLancarExcecaoQuandoExcluirIdNulo() {
        Cliente clienteSemId = new Cliente();
        replayAll();

        assertThrows(NullPointerException.class, () -> service.excluir(clienteSemId));
        verifyAll();
    }
}