package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.ListaClienteAulaDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.Funcionario;
import br.edu.ifsudestemg.saapi.model.entity.ListaClienteAula;
import br.edu.ifsudestemg.saapi.service.AulaService;
import br.edu.ifsudestemg.saapi.service.ClienteService;
import br.edu.ifsudestemg.saapi.service.ListaClienteAulaService;
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
class ListaClienteAulaControllerTest extends EasyMockSupport {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ListaClienteAulaService service;

    @Mock
    private ClienteService clienteService;

    @Mock
    private AulaService aulaService;

    private ListaClienteAulaController controller;
    private ListaClienteAula item;
    private Cliente cliente;
    private Aula aula;
    private ListaClienteAulaDTO dto;

    @BeforeEach
    void setUp() {
        controller = new ListaClienteAulaController(service, clienteService, aulaService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        objectMapper = new ObjectMapper();

        Funcionario funcionario = new Funcionario();
        funcionario.setId(5L);
        funcionario.setNome("Carlos Instrutor");

        cliente = new Cliente();
        cliente.setId(10L);
        cliente.setNome("Mariana Costa");

        aula = new Aula();
        aula.setId(20L);
        aula.setNome("Spinning");
        aula.setFuncionario(funcionario);

        item = new ListaClienteAula();
        item.setId(1L);
        item.setCliente(cliente);
        item.setAula(aula);

        dto = ListaClienteAulaDTO.create(item);
    }


    @Test
    public void deveRetornarTodasAsListas() throws Exception {
        expect(service.getListaClienteAula()).andReturn(List.of(item));
        replayAll();

        mockMvc.perform(get("/api/v1/listasclientesaulas")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verifyAll();
    }


    @Test
    public void deveRetornarItemPorIdQuandoExistir() throws Exception {
        expect(service.getListaClienteAulaById(1L)).andReturn(Optional.of(item));
        replayAll();

        mockMvc.perform(get("/api/v1/listasclientesaulas/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoBuscarIdInexistente() throws Exception {
        expect(service.getListaClienteAulaById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(get("/api/v1/listasclientesaulas/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Lista cliente por aula não encontrada"));

        verifyAll();
    }


    @Test
    public void deveRetornarListaPorAulaQuandoExistir() throws Exception {
        expect(aulaService.getAulaById(20L)).andReturn(Optional.of(aula));
        expect(service.getListaClienteAulaByAula(aula)).andReturn(List.of(item));
        replayAll();

        mockMvc.perform(get("/api/v1/listasclientesaulas/aula/20")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoBuscarPorAulaInexistente() throws Exception {
        expect(aulaService.getAulaById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(get("/api/v1/listasclientesaulas/aula/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Aula não encontrada"));

        verifyAll();
    }


    @Test
    public void deveSalvarItemComClienteEAulaExistentes() throws Exception {
        expect(clienteService.getClienteById(10L)).andReturn(Optional.of(cliente));
        expect(aulaService.getAulaById(20L)).andReturn(Optional.of(aula));
        expect(service.salvar(anyObject(ListaClienteAula.class))).andReturn(item);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/listasclientesaulas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveSalvarItemQuandoClienteEAulaNaoExistirem() throws Exception {
        dto.setIdCliente(99L);
        dto.setIdAula(88L);
        expect(clienteService.getClienteById(99L)).andReturn(Optional.empty());
        expect(aulaService.getAulaById(88L)).andReturn(Optional.empty());
        expect(service.salvar(anyObject(ListaClienteAula.class))).andReturn(item);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/listasclientesaulas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveSalvarItemSemClienteEAulaInformados() throws Exception {
        dto.setIdCliente(null);
        dto.setIdAula(null);
        expect(service.salvar(anyObject(ListaClienteAula.class))).andReturn(item);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/listasclientesaulas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoSalvarLancarRegraNegocioException() throws Exception {
        expect(clienteService.getClienteById(10L)).andReturn(Optional.of(cliente));
        expect(aulaService.getAulaById(20L)).andReturn(Optional.of(aula));
        expect(service.salvar(anyObject(ListaClienteAula.class)))
                .andThrow(new RegraNegocioException("Aula lotada"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/listasclientesaulas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Aula lotada"));

        verifyAll();
    }


    @Test
    public void deveAtualizarItemComSucesso() throws Exception {
        expect(service.getListaClienteAulaById(1L)).andReturn(Optional.of(item));
        expect(clienteService.getClienteById(10L)).andReturn(Optional.of(cliente));
        expect(aulaService.getAulaById(20L)).andReturn(Optional.of(aula));
        expect(service.salvar(anyObject(ListaClienteAula.class))).andReturn(item);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/listasclientesaulas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoAtualizarIdInexistente() throws Exception {
        expect(service.getListaClienteAulaById(99L)).andReturn(Optional.empty());
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/listasclientesaulas/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Lista cliente por aula não encontrada"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoAtualizarLancarRegraNegocioException() throws Exception {
        expect(service.getListaClienteAulaById(1L)).andReturn(Optional.of(item));
        expect(clienteService.getClienteById(10L)).andReturn(Optional.of(cliente));
        expect(aulaService.getAulaById(20L)).andReturn(Optional.of(aula));
        expect(service.salvar(anyObject(ListaClienteAula.class)))
                .andThrow(new RegraNegocioException("Cliente inválido"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/listasclientesaulas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cliente inválido"));

        verifyAll();
    }

    @Test
    public void deveExcluirItemComSucesso() throws Exception {
        expect(service.getListaClienteAulaById(1L)).andReturn(Optional.of(item));
        service.excluir(item);
        expectLastCall().once();
        replayAll();

        mockMvc.perform(delete("/api/v1/listasclientesaulas/1"))
                .andExpect(status().isNoContent());

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoExcluirIdInexistente() throws Exception {
        expect(service.getListaClienteAulaById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(delete("/api/v1/listasclientesaulas/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Lista de cliente com aula não encontrada "));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoExcluirLancarRegraNegocioException() throws Exception {
        expect(service.getListaClienteAulaById(1L)).andReturn(Optional.of(item));
        service.excluir(item);
        expectLastCall().andThrow(new RegraNegocioException("Não é possível excluir"));
        replayAll();

        mockMvc.perform(delete("/api/v1/listasclientesaulas/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Não é possível excluir"));

        verifyAll();
    }
}