package br.edu.ifsudestemg.saapi.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.exception.SenhaInvalidaException;
import br.edu.ifsudestemg.saapi.model.entity.Usuario;
import br.edu.ifsudestemg.saapi.model.repository.UsuarioRepository;
import org.easymock.EasyMockExtension;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(EasyMockExtension.class)
class UsuarioServiceTest extends EasyMockSupport {

    private Usuario usuario;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UsuarioService service;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setLogin("admin");
        usuario.setSenha("123456");
        usuario.setCpf("12345678900");
        usuario.setAdmin(false);

        service = new UsuarioService();
        // Injeção manual via reflection para campos @Autowired da classe de serviço
        ReflectionTestUtils.setField(service, "repository", usuarioRepository);
        ReflectionTestUtils.setField(service, "encoder", passwordEncoder);
    }


    @Test
    public void deveTestarExcecaoLoginNulo() {
        replayAll();
        usuario.setLogin(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(usuario));
        assertEquals("Login inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoLoginVazio() {
        replayAll();
        usuario.setLogin("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(usuario));
        assertEquals("Login inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoCpfNulo() {
        replayAll();
        usuario.setCpf(null);
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(usuario));
        assertEquals("CPF inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveTestarExcecaoCpfVazio() {
        replayAll();
        usuario.setCpf("");
        Exception e = assertThrows(RegraNegocioException.class, () -> service.validar(usuario));
        assertEquals("CPF inválido", e.getMessage());
        verifyAll();
    }

    @Test
    public void deveValidarTudo() {
        replayAll();
        assertDoesNotThrow(() -> service.validar(usuario));
        verifyAll();
    }

    @Test
    public void deveCarregarUsuarioComRoleUserQuandoNaoAdmin() {
        usuario.setAdmin(false);
        expect(usuarioRepository.findByLogin("admin")).andReturn(Optional.of(usuario));
        replayAll();

        UserDetails userDetails = service.loadUserByUsername("admin");

        assertNotNull(userDetails);
        assertEquals("admin", userDetails.getUsername());
        assertEquals("123456", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        assertFalse(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        verifyAll();
    }

    @Test
    public void deveCarregarUsuarioComRoleAdminEUserQuandoAdmin() {
        usuario.setAdmin(true);
        expect(usuarioRepository.findByLogin("admin")).andReturn(Optional.of(usuario));
        replayAll();

        UserDetails userDetails = service.loadUserByUsername("admin");

        assertNotNull(userDetails);
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        verifyAll();
    }

    @Test
    public void deveLancarExcecaoQuandoUsuarioNaoEncontradoNoLoadUser() {
        expect(usuarioRepository.findByLogin("inexistente")).andReturn(Optional.empty());
        replayAll();

        Exception e = assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("inexistente"));
        assertEquals("Usuário não encontrado", e.getMessage());
        verifyAll();
    }


    @Test
    public void deveAutenticarComSucesso() {
        expect(usuarioRepository.findByLogin("admin")).andReturn(Optional.of(usuario));
        expect(passwordEncoder.matches("123456", "123456")).andReturn(true);
        replayAll();

        UserDetails userDetails = service.autenticar(usuario);

        assertNotNull(userDetails);
        assertEquals("admin", userDetails.getUsername());
        verifyAll();
    }

    @Test
    public void deveLancarExcecaoQuandoSenhaInvalida() {
        expect(usuarioRepository.findByLogin("admin")).andReturn(Optional.of(usuario));
        expect(passwordEncoder.matches("123456", "123456")).andReturn(false);
        replayAll();

        assertThrows(SenhaInvalidaException.class, () -> service.autenticar(usuario));
        verifyAll();
    }

    @Test
    public void deveRetornarListaUsuarios() {
        expect(usuarioRepository.findAll()).andReturn(List.of(usuario));
        replayAll();

        List<Usuario> resultado = service.getUsuarios();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verifyAll();
    }

    @Test
    public void deveRetornarUsuarioPorIDQuandoExistir() {
        expect(usuarioRepository.findById(1L)).andReturn(Optional.of(usuario));
        replayAll();

        Optional<Usuario> resultado = service.getUsuarioById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verifyAll();
    }

    @Test
    public void deveRetornarUsuarioPorLoginQuandoExistir() {
        expect(usuarioRepository.findByLogin("admin")).andReturn(Optional.of(usuario));
        replayAll();

        Optional<Usuario> resultado = service.getUsuarioByLogin("admin");

        assertTrue(resultado.isPresent());
        assertEquals("admin", resultado.get().getLogin());
        verifyAll();
    }

    @Test
    public void deveSalvarUsuarioValido() {
        expect(usuarioRepository.save(anyObject(Usuario.class))).andReturn(usuario);
        replayAll();

        Usuario resultado = service.salvar(usuario);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verifyAll();
    }

    @Test
    public void deveExcluirUsuarioQuandoPossuirID() {
        usuarioRepository.delete(usuario);
        expectLastCall().once();

        replayAll();

        assertDoesNotThrow(() -> service.excluir(usuario));
        verifyAll();
    }

    @Test
    public void deveLancarExcecaoQuandoExcluirIdNulo() {
        Usuario usuarioSemId = new Usuario();
        replayAll();

        assertThrows(NullPointerException.class, () -> service.excluir(usuarioSemId));
        verifyAll();
    }
}