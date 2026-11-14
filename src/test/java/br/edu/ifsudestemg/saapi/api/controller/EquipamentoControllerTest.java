package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.EquipamentoDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Equipamento;
import br.edu.ifsudestemg.saapi.service.EquipamentoService;
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
class EquipamentoControllerTest extends EasyMockSupport {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private EquipamentoService service;

    private EquipamentoController controller;
    private Equipamento equipamento;
    private EquipamentoDTO dto;

    @BeforeEach
    void setUp() {
        controller = new EquipamentoController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        objectMapper = new ObjectMapper();

        equipamento = new Equipamento();
        equipamento.setId(1L);
        equipamento.setNome("Esteira Ergométrica");
        equipamento.setDisponivel(true);

        dto = EquipamentoDTO.create(equipamento);
    }


    @Test
    public void deveRetornarListaDeEquipamentos() throws Exception {
        expect(service.getEquipamento()).andReturn(List.of(equipamento));
        replayAll();

        mockMvc.perform(get("/api/v1/equipamentos")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Esteira Ergométrica"));

        verifyAll();
    }


    @Test
    public void deveRetornarEquipamentoPorIdQuandoExistir() throws Exception {
        expect(service.getEquipamentoById(1L)).andReturn(Optional.of(equipamento));
        replayAll();

        mockMvc.perform(get("/api/v1/equipamentos/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Esteira Ergométrica"));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoBuscarIdInexistente() throws Exception {
        expect(service.getEquipamentoById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(get("/api/v1/equipamentos/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Equipamento não encontrado"));

        verifyAll();
    }


    @Test
    public void deveRetornarEquipamentosAtivos() throws Exception {
        expect(service.getEquipamentosAtivos()).andReturn(List.of(equipamento));
        replayAll();

        mockMvc.perform(get("/api/v1/equipamentos/ativos")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Esteira Ergométrica"));

        verifyAll();
    }


    @Test
    public void deveSalvarEquipamentoComSucesso() throws Exception {
        expect(service.salvar(anyObject(Equipamento.class))).andReturn(equipamento);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/equipamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Esteira Ergométrica"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoSalvarLancarRegraNegocioException() throws Exception {
        expect(service.salvar(anyObject(Equipamento.class)))
                .andThrow(new RegraNegocioException("Nome inválido"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/equipamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Nome inválido"));

        verifyAll();
    }


    @Test
    public void deveAtualizarEquipamentoComSucesso() throws Exception {
        expect(service.getEquipamentoById(1L)).andReturn(Optional.of(equipamento));
        expect(service.salvar(anyObject(Equipamento.class))).andReturn(equipamento);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/equipamentos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Esteira Ergométrica"));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoAtualizarIdInexistente() throws Exception {
        expect(service.getEquipamentoById(99L)).andReturn(Optional.empty());
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/equipamentos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Equipamento não encontrado"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoAtualizarLancarRegraNegocioException() throws Exception {
        expect(service.getEquipamentoById(1L)).andReturn(Optional.of(equipamento));
        expect(service.salvar(anyObject(Equipamento.class)))
                .andThrow(new RegraNegocioException("Status inválido"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/equipamentos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Status inválido"));

        verifyAll();
    }


    @Test
    public void deveExcluirEquipamentoComSucesso() throws Exception {
        expect(service.getEquipamentoById(1L)).andReturn(Optional.of(equipamento));
        service.excluir(equipamento);
        expectLastCall().once();
        replayAll();

        mockMvc.perform(delete("/api/v1/equipamentos/1"))
                .andExpect(status().isNoContent());

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoExcluirIdInexistente() throws Exception {
        expect(service.getEquipamentoById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(delete("/api/v1/equipamentos/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Equipamento não encontrado "));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoExcluirLancarRegraNegocioException() throws Exception {
        expect(service.getEquipamentoById(1L)).andReturn(Optional.of(equipamento));
        service.excluir(equipamento);
        expectLastCall().andThrow(new RegraNegocioException("Não é possível excluir"));
        replayAll();

        mockMvc.perform(delete("/api/v1/equipamentos/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Não é possível excluir"));

        verifyAll();
    }
}