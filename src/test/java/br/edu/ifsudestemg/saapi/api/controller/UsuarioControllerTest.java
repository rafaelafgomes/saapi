package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.CredenciaisDTO;
import br.edu.ifsudestemg.saapi.api.dto.UsuarioDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.exception.SenhaInvalidaException;
import br.edu.ifsudestemg.saapi.model.entity.Usuario;
import br.edu.ifsudestemg.saapi.security.JwtService;
import br.edu.ifsudestemg.saapi.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.easymock.EasyMockExtension;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(EasyMockExtension.class)
class UsuarioControllerTest extends EasyMockSupport {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    private UsuarioController controller;
    private Usuario usuario;
    private UsuarioDTO dto;

    @BeforeEach
    void setUp() {
        controller = new UsuarioController(usuarioService, passwordEncoder, jwtService, usuarioService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        objectMapper = new ObjectMapper();

        usuario = Usuario.builder()
                .id(1L)
                .login("admin")
                .senha("senha123")
                .admin(true)
                .build();

        dto = UsuarioDTO.create(usuario);
        dto.setSenha("senha123");
        dto.setSenhaRepeticao("senha123");
    }


    @Test
    public void deveRetornarListaDeUsuarios() throws Exception {
        expect(usuarioService.getUsuarios()).andReturn(List.of(usuario));
        replayAll();

        mockMvc.perform(get("/api/v1/usuarios")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].login").value("admin"));

        verifyAll();
    }


    @Test
    public void deveRetornarUsuarioPorIdQuandoExistir() throws Exception {
        expect(usuarioService.getUsuarioById(1L)).andReturn(Optional.of(usuario));
        replayAll();

        mockMvc.perform(get("/api/v1/usuarios/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.login").value("admin"));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoBuscarIdInexistente() throws Exception {
        expect(usuarioService.getUsuarioById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(get("/api/v1/usuarios/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuário não encontrado"));

        verifyAll();
    }


    @Test
    public void deveSalvarUsuarioComSucesso() throws Exception {
        expect(passwordEncoder.encode("senha123")).andReturn("senhaCriptografada");
        expect(usuarioService.salvar(anyObject(Usuario.class))).andReturn(usuario);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.login").value("admin"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoSenhaForInvalidaNoPost() throws Exception {
        dto.setSenha(null);
        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Senha inválida"));

        dto.setSenha("   ");
        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Senha inválida"));

        dto.setSenha("senha123");
        dto.setSenhaRepeticao(null);
        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Senha inválida"));

        dto.setSenhaRepeticao("   ");
        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Senha inválida"));
    }

    @Test
    public void deveRetornarBadRequestQuandoSenhasNaoConferiremNoPost() throws Exception {
        dto.setSenhaRepeticao("outraSenha");

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Senhas não conferem"));
    }

    @Test
    public void deveRetornarBadRequestQuandoSalvarLancarRegraNegocioException() throws Exception {
        expect(passwordEncoder.encode("senha123")).andReturn("senhaCriptografada");
        expect(usuarioService.salvar(anyObject(Usuario.class)))
                .andThrow(new RegraNegocioException("Login já cadastrado"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Login já cadastrado"));

        verifyAll();
    }


    @Test
    public void deveAutenticarUsuarioComSucesso() throws Exception {
        CredenciaisDTO credenciais = new CredenciaisDTO();
        credenciais.setLogin("admin");
        credenciais.setSenha("senha123");

        UserDetails userDetails = new User("admin", "senha123", Collections.emptyList());

        expect(usuarioService.autenticar(anyObject(Usuario.class))).andReturn(userDetails);
        expect(jwtService.gerarToken(anyObject(Usuario.class))).andReturn("token-jwt-valido");
        expect(usuarioService.getUsuarioByLogin("admin")).andReturn(Optional.of(usuario));
        replayAll();

        mockMvc.perform(post("/api/v1/usuarios/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credenciais)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("admin"))
                .andExpect(jsonPath("$.token").value("token-jwt-valido"))
                .andExpect(jsonPath("$.admin").value(true));

        verifyAll();
    }

    @Test
    public void deveRetornarUnauthorizedQuandoUsuarioNaoForEncontradoNaAutenticacao() throws Exception {
        CredenciaisDTO credenciais = new CredenciaisDTO();
        credenciais.setLogin("inexistente");
        credenciais.setSenha("senha123");

        expect(usuarioService.autenticar(anyObject(Usuario.class)))
                .andThrow(new UsernameNotFoundException("Usuário não encontrado"));
        replayAll();

        mockMvc.perform(post("/api/v1/usuarios/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credenciais)))
                .andExpect(status().isUnauthorized());

        verifyAll();
    }

    @Test
    public void deveRetornarUnauthorizedQuandoSenhaForInvalidaNaAutenticacao() throws Exception {
        CredenciaisDTO credenciais = new CredenciaisDTO();
        credenciais.setLogin("admin");
        credenciais.setSenha("senhaErrada");

        expect(usuarioService.autenticar(anyObject(Usuario.class)))
                .andThrow(new SenhaInvalidaException());
        replayAll();

        mockMvc.perform(post("/api/v1/usuarios/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credenciais)))
                .andExpect(status().isUnauthorized());

        verifyAll();
    }

    @Test
    public void deveAtualizarUsuarioComSucesso() throws Exception {
        expect(usuarioService.getUsuarioById(1L)).andReturn(Optional.of(usuario));
        expect(usuarioService.salvar(anyObject(Usuario.class))).andReturn(usuario);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.login").value("admin"));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoAtualizarIdInexistente() throws Exception {
        expect(usuarioService.getUsuarioById(99L)).andReturn(Optional.empty());
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/usuarios/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuário não encontrado"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoSenhaForInvalidaNoPut() throws Exception {
        expect(usuarioService.getUsuarioById(1L)).andReturn(Optional.of(usuario)).times(4);
        replayAll();

        dto.setSenha(null);
        mockMvc.perform(put("/api/v1/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Senha inválida"));

        dto.setSenha("  ");
        mockMvc.perform(put("/api/v1/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Senha inválida"));

        dto.setSenha("senha123");
        dto.setSenhaRepeticao(null);
        mockMvc.perform(put("/api/v1/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Senha inválida"));

        dto.setSenhaRepeticao("  ");
        mockMvc.perform(put("/api/v1/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Senha inválida"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoSenhasNaoConferiremNoPut() throws Exception {
        expect(usuarioService.getUsuarioById(1L)).andReturn(Optional.of(usuario));
        replayAll();

        dto.setSenhaRepeticao("outraSenha");

        mockMvc.perform(put("/api/v1/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Senhas não conferem"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoAtualizarLancarRegraNegocioException() throws Exception {
        expect(usuarioService.getUsuarioById(1L)).andReturn(Optional.of(usuario));
        expect(usuarioService.salvar(anyObject(Usuario.class)))
                .andThrow(new RegraNegocioException("Login inválido"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Login inválido"));

        verifyAll();
    }


    @Test
    public void deveExcluirUsuarioComSucesso() throws Exception {
        expect(usuarioService.getUsuarioById(1L)).andReturn(Optional.of(usuario));
        usuarioService.excluir(usuario);
        expectLastCall().once();
        replayAll();

        mockMvc.perform(delete("/api/v1/usuarios/1"))
                .andExpect(status().isNoContent());

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoExcluirIdInexistente() throws Exception {
        expect(usuarioService.getUsuarioById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(delete("/api/v1/usuarios/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuário não encontrado"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoExcluirLancarRegraNegocioException() throws Exception {
        expect(usuarioService.getUsuarioById(1L)).andReturn(Optional.of(usuario));
        usuarioService.excluir(usuario);
        expectLastCall().andThrow(new RegraNegocioException("Não é possível excluir"));
        replayAll();

        mockMvc.perform(delete("/api/v1/usuarios/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Não é possível excluir"));

        verifyAll();
    }
}