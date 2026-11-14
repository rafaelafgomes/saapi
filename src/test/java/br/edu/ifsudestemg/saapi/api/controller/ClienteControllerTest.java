package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.ClienteDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.Plano;
import br.edu.ifsudestemg.saapi.service.ClienteService;
import br.edu.ifsudestemg.saapi.service.PlanoService;
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
class ClienteControllerTest extends EasyMockSupport {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ClienteService service;

    @Mock
    private PlanoService planoService;

    private ClienteController controller;
    private Cliente cliente;
    private Plano plano;
    private ClienteDTO dto;

    @BeforeEach
    void setUp() {
        controller = new ClienteController(service, planoService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        objectMapper = new ObjectMapper();

        plano = new Plano();
        plano.setId(10L);
        plano.setNome("Plano Mensal");

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Mariana Costa");
        cliente.setCpf("12345678901");
        cliente.setStatus(true);
        cliente.setPlano(plano);

        dto = ClienteDTO.create(cliente);
    }


    @Test
    public void deveRetornarListaDeClientes() throws Exception {
        expect(service.getCliente()).andReturn(List.of(cliente));
        replayAll();

        mockMvc.perform(get("/api/v1/clientes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Mariana Costa"));

        verifyAll();
    }



    @Test
    public void deveRetornarClientePorIdQuandoExistir() throws Exception {
        expect(service.getClienteById(1L)).andReturn(Optional.of(cliente));
        replayAll();

        mockMvc.perform(get("/api/v1/clientes/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Mariana Costa"));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoBuscarIdInexistente() throws Exception {
        expect(service.getClienteById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(get("/api/v1/clientes/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cliente não encontrado"));

        verifyAll();
    }


    @Test
    public void deveRetornarClientesAtivos() throws Exception {
        expect(service.getClientesAtivos()).andReturn(List.of(cliente));
        replayAll();

        mockMvc.perform(get("/api/v1/clientes/ativos")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Mariana Costa"));

        verifyAll();
    }


    @Test
    public void deveSalvarClienteComPlanoExistenteComSucesso() throws Exception {
        expect(planoService.getPlanoById(10L)).andReturn(Optional.of(plano));
        expect(service.salvar(anyObject(Cliente.class))).andReturn(cliente);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Mariana Costa"));

        verifyAll();
    }

    @Test
    public void deveSalvarClienteQuandoPlanoNaoExistir() throws Exception {
        dto.setIdPlano(99L);
        expect(planoService.getPlanoById(99L)).andReturn(Optional.empty());
        expect(service.salvar(anyObject(Cliente.class))).andReturn(cliente);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveSalvarClienteSemPlanoInformado() throws Exception {
        dto.setIdPlano(null);
        expect(service.salvar(anyObject(Cliente.class))).andReturn(cliente);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoSalvarLancarRegraNegocioException() throws Exception {
        expect(planoService.getPlanoById(10L)).andReturn(Optional.of(plano));
        expect(service.salvar(anyObject(Cliente.class)))
                .andThrow(new RegraNegocioException("CPF inválido"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("CPF inválido"));

        verifyAll();
    }


    @Test
    public void deveAtualizarClienteComSucesso() throws Exception {
        expect(service.getClienteById(1L)).andReturn(Optional.of(cliente));
        expect(planoService.getPlanoById(10L)).andReturn(Optional.of(plano));
        expect(service.salvar(anyObject(Cliente.class))).andReturn(cliente);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Mariana Costa"));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoAtualizarIdInexistente() throws Exception {
        expect(service.getClienteById(99L)).andReturn(Optional.empty());
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/clientes/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cliente não encontrado"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoAtualizarLancarRegraNegocioException() throws Exception {
        expect(service.getClienteById(1L)).andReturn(Optional.of(cliente));
        expect(planoService.getPlanoById(10L)).andReturn(Optional.of(plano));
        expect(service.salvar(anyObject(Cliente.class)))
                .andThrow(new RegraNegocioException("Nome inválido"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Nome inválido"));

        verifyAll();
    }

    @Test
    public void deveExcluirClienteComSucesso() throws Exception {
        expect(service.getClienteById(1L)).andReturn(Optional.of(cliente));
        service.excluir(cliente);
        expectLastCall().once();
        replayAll();

        mockMvc.perform(delete("/api/v1/clientes/1"))
                .andExpect(status().isNoContent());

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoExcluirIdInexistente() throws Exception {
        expect(service.getClienteById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(delete("/api/v1/clientes/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cliente não encontrado (a)"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoExcluirLancarRegraNegocioException() throws Exception {
        expect(service.getClienteById(1L)).andReturn(Optional.of(cliente));
        service.excluir(cliente);
        expectLastCall().andThrow(new RegraNegocioException("Não é possível excluir"));
        replayAll();

        mockMvc.perform(delete("/api/v1/clientes/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Não é possível excluir"));

        verifyAll();
    }
}