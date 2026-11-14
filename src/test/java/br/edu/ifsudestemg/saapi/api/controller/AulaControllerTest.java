package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.AulaDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.entity.Funcionario;
import br.edu.ifsudestemg.saapi.service.AulaService;
import br.edu.ifsudestemg.saapi.service.ClienteService;
import br.edu.ifsudestemg.saapi.service.FuncionarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.easymock.EasyMockExtension;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;
import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(EasyMockExtension.class)
class AulaControllerTest extends EasyMockSupport {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private AulaService service;

    @Mock
    private FuncionarioService funcionarioService;

    @Mock
    private ClienteService clienteService;

    private AulaController controller;
    private Aula aula;
    private Funcionario funcionario;
    private AulaDTO dto;

    @BeforeEach
    void setUp() {
        controller = new AulaController(service, funcionarioService, clienteService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        objectMapper = new ObjectMapper();

        funcionario = new Funcionario();
        funcionario.setId(2L);
        funcionario.setNome("Carlos Instrutor");

        aula = new Aula();
        aula.setId(1L);
        aula.setNome("Spinning");
        aula.setDataAula("2026-11-15");
        aula.setHoraAula("18:00");
        aula.setStatus(true);
        aula.setCapacidade(20);
        aula.setFuncionario(funcionario);

        dto = AulaDTO.create(aula);
    }


    @Test
    public void deveRetornarListaDeAulas() throws Exception {
        expect(service.getAula()).andReturn(List.of(aula));
        replayAll();

        mockMvc.perform(get("/api/v1/aulas")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Spinning"));

        verifyAll();
    }


    @Test
    public void deveRetornarAulaPorIdQuandoExistir() throws Exception {
        expect(service.getAulaById(1L)).andReturn(Optional.of(aula));
        replayAll();

        mockMvc.perform(get("/api/v1/aulas/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Spinning"));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoBuscarIdInexistente() throws Exception {
        expect(service.getAulaById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(get("/api/v1/aulas/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Aula não encontrada"));

        verifyAll();
    }


    @Test
    public void deveSalvarAulaComFuncionarioExistenteComSucesso() throws Exception {
        expect(funcionarioService.getFuncionarioById(2L)).andReturn(Optional.of(funcionario));
        expect(service.salvar(anyObject(Aula.class))).andReturn(aula);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/aulas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Spinning"));

        verifyAll();
    }

    @Test
    public void deveSalvarAulaQuandoFuncionarioNaoExistir() throws Exception {
        dto.setIdFuncionario(99L);
        expect(funcionarioService.getFuncionarioById(99L)).andReturn(Optional.empty());
        expect(service.salvar(anyObject(Aula.class))).andReturn(aula);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/aulas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveSalvarAulaSemFuncionarioInformado() throws Exception {
        dto.setIdFuncionario(null);
        expect(service.salvar(anyObject(Aula.class))).andReturn(aula);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/aulas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoSalvarLancarRegraNegocioException() throws Exception {
        expect(funcionarioService.getFuncionarioById(2L)).andReturn(Optional.of(funcionario));
        expect(service.salvar(anyObject(Aula.class)))
                .andThrow(new RegraNegocioException("Capacidade inválida"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/aulas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Capacidade inválida"));

        verifyAll();
    }


    @Test
    public void deveAtualizarAulaComSucesso() throws Exception {
        expect(service.getAulaById(1L)).andReturn(Optional.of(aula));
        expect(funcionarioService.getFuncionarioById(2L)).andReturn(Optional.of(funcionario));
        expect(service.salvar(anyObject(Aula.class))).andReturn(aula);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/aulas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Spinning"));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoAtualizarIdInexistente() throws Exception {
        expect(service.getAulaById(99L)).andReturn(Optional.empty());
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/aulas/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Aula não encontrada"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoAtualizarLancarRegraNegocioException() throws Exception {
        expect(service.getAulaById(1L)).andReturn(Optional.of(aula));
        expect(funcionarioService.getFuncionarioById(2L)).andReturn(Optional.of(funcionario));
        expect(service.salvar(anyObject(Aula.class)))
                .andThrow(new RegraNegocioException("Horário inválido"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/aulas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Horário inválido"));

        verifyAll();
    }


    @Test
    public void deveExcluirAulaComSucesso() throws Exception {
        expect(service.getAulaById(1L)).andReturn(Optional.of(aula));
        service.excluir(aula);
        expectLastCall().once();
        replayAll();

        mockMvc.perform(delete("/api/v1/aulas/1"))
                .andExpect(status().isNoContent());

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoExcluirIdInexistente() throws Exception {
        expect(service.getAulaById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(delete("/api/v1/aulas/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Aula não encontrada"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoExcluirLancarRegraNegocioException() throws Exception {
        expect(service.getAulaById(1L)).andReturn(Optional.of(aula));
        service.excluir(aula);
        expectLastCall().andThrow(new RegraNegocioException("Não é possível excluir"));
        replayAll();

        mockMvc.perform(delete("/api/v1/aulas/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Não é possível excluir"));

        verifyAll();
    }
}