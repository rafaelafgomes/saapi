package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.TreinoDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.Funcionario;
import br.edu.ifsudestemg.saapi.model.entity.Treino;
import br.edu.ifsudestemg.saapi.service.ClienteService;
import br.edu.ifsudestemg.saapi.service.FuncionarioService;
import br.edu.ifsudestemg.saapi.service.TreinoService;
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
class TreinoControllerTest extends EasyMockSupport {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private TreinoService service;

    @Mock
    private ClienteService clienteService;

    @Mock
    private FuncionarioService funcionarioService;

    private TreinoController controller;
    private Treino treino;
    private Cliente cliente;
    private Funcionario funcionario;
    private TreinoDTO dto;

    @BeforeEach
    void setUp() {
        controller = new TreinoController(service, clienteService, funcionarioService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        objectMapper = new ObjectMapper();

        cliente = new Cliente();
        cliente.setId(10L);
        cliente.setNome("Mariana Costa");

        funcionario = new Funcionario();
        funcionario.setId(20L);
        funcionario.setNome("Roberto Alves");

        treino = new Treino();
        treino.setId(1L);
        treino.setNome("Treino A - Hipertrofia");
        treino.setData("2026-11-01");
        treino.setCliente(cliente);
        treino.setFuncionario(funcionario);
        treino.setDescricao("treino teste");

        dto = TreinoDTO.create(treino);
    }

    // --- GET ALL ---

    @Test
    public void deveRetornarListaDeTreinos() throws Exception {
        expect(service.getTreino()).andReturn(List.of(treino));
        replayAll();

        mockMvc.perform(get("/api/v1/treinos")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Treino A - Hipertrofia"));

        verifyAll();
    }

    // --- GET BY ID ---

    @Test
    public void deveRetornarTreinoPorIdQuandoExistir() throws Exception {
        expect(service.getTreinoById(1L)).andReturn(Optional.of(treino));
        replayAll();

        mockMvc.perform(get("/api/v1/treinos/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Treino A - Hipertrofia"));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoBuscarIdInexistente() throws Exception {
        expect(service.getTreinoById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(get("/api/v1/treinos/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Treino não encontrado"));

        verifyAll();
    }

    // --- POST ---

    @Test
    public void deveSalvarTreinoComClienteEFuncionarioExistentes() throws Exception {
        expect(clienteService.getClienteById(10L)).andReturn(Optional.of(cliente));
        expect(funcionarioService.getFuncionarioById(20L)).andReturn(Optional.of(funcionario));
        expect(service.salvar(anyObject(Treino.class))).andReturn(treino);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/treinos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Treino A - Hipertrofia"));

        verifyAll();
    }

    @Test
    public void deveSalvarTreinoQuandoClienteEFuncionarioNaoExistirem() throws Exception {
        dto.setIdCliente(99L);
        dto.setIdFuncionario(88L);
        expect(clienteService.getClienteById(99L)).andReturn(Optional.empty());
        expect(funcionarioService.getFuncionarioById(88L)).andReturn(Optional.empty());
        expect(service.salvar(anyObject(Treino.class))).andReturn(treino);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/treinos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveSalvarTreinoSemClienteEFuncionarioInformados() throws Exception {
        dto.setIdCliente(null);
        dto.setIdFuncionario(null);
        expect(service.salvar(anyObject(Treino.class))).andReturn(treino);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/treinos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoSalvarLancarRegraNegocioException() throws Exception {
        expect(clienteService.getClienteById(10L)).andReturn(Optional.of(cliente));
        expect(funcionarioService.getFuncionarioById(20L)).andReturn(Optional.of(funcionario));
        expect(service.salvar(anyObject(Treino.class)))
                .andThrow(new RegraNegocioException("Nome inválido"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/treinos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Nome inválido"));

        verifyAll();
    }

    // --- PUT ---

    @Test
    public void deveAtualizarTreinoComSucesso() throws Exception {
        expect(service.getTreinoById(1L)).andReturn(Optional.of(treino));
        expect(clienteService.getClienteById(10L)).andReturn(Optional.of(cliente));
        expect(funcionarioService.getFuncionarioById(20L)).andReturn(Optional.of(funcionario));
        expect(service.salvar(anyObject(Treino.class))).andReturn(treino);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/treinos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Treino A - Hipertrofia"));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoAtualizarIdInexistente() throws Exception {
        expect(service.getTreinoById(99L)).andReturn(Optional.empty());
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/treinos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Treino não encontrado"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoAtualizarLancarRegraNegocioException() throws Exception {
        expect(service.getTreinoById(1L)).andReturn(Optional.of(treino));
        expect(clienteService.getClienteById(10L)).andReturn(Optional.of(cliente));
        expect(funcionarioService.getFuncionarioById(20L)).andReturn(Optional.of(funcionario));
        expect(service.salvar(anyObject(Treino.class)))
                .andThrow(new RegraNegocioException("Data inválida"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/treinos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Data inválida"));

        verifyAll();
    }

    // --- DELETE ---

    @Test
    public void deveExcluirTreinoComSucesso() throws Exception {
        expect(service.getTreinoById(1L)).andReturn(Optional.of(treino));
        service.excluir(treino);
        expectLastCall().once();
        replayAll();

        mockMvc.perform(delete("/api/v1/treinos/1"))
                .andExpect(status().isNoContent());

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoExcluirIdInexistente() throws Exception {
        expect(service.getTreinoById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(delete("/api/v1/treinos/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Treino não encontrado "));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoExcluirLancarRegraNegocioException() throws Exception {
        expect(service.getTreinoById(1L)).andReturn(Optional.of(treino));
        service.excluir(treino);
        expectLastCall().andThrow(new RegraNegocioException("Não é possível excluir"));
        replayAll();

        mockMvc.perform(delete("/api/v1/treinos/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Não é possível excluir"));

        verifyAll();
    }
}