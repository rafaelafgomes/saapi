package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.PlanoDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Plano;
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
class PlanoControllerTest extends EasyMockSupport {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private PlanoService service;

    private PlanoController controller;
    private Plano plano;
    private PlanoDTO dto;

    @BeforeEach
    void setUp() {
        controller = new PlanoController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        objectMapper = new ObjectMapper();

        plano = new Plano();
        plano.setId(1L);
        plano.setNome("Plano Mensal");

        dto = PlanoDTO.create(plano);
    }


    @Test
    public void deveRetornarListaDePlanos() throws Exception {
        expect(service.getPlano()).andReturn(List.of(plano));
        replayAll();

        mockMvc.perform(get("/api/v1/planos")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Plano Mensal"));

        verifyAll();
    }

    @Test
    public void deveRetornarPlanoPorIdQuandoExistir() throws Exception {
        expect(service.getPlanoById(1L)).andReturn(Optional.of(plano));
        replayAll();

        mockMvc.perform(get("/api/v1/planos/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Plano Mensal"));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoBuscarIdInexistente() throws Exception {
        expect(service.getPlanoById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(get("/api/v1/planos/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Plano não encontrado"));

        verifyAll();
    }


    @Test
    public void deveSalvarPlanoComSucesso() throws Exception {
        expect(service.salvar(anyObject(Plano.class))).andReturn(plano);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/planos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Plano Mensal"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoSalvarLancarRegraNegocioException() throws Exception {
        expect(service.salvar(anyObject(Plano.class)))
                .andThrow(new RegraNegocioException("Nome inválido"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/planos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Nome inválido"));

        verifyAll();
    }


    @Test
    public void deveAtualizarPlanoComSucesso() throws Exception {
        expect(service.getPlanoById(1L)).andReturn(Optional.of(plano));
        expect(service.salvar(anyObject(Plano.class))).andReturn(plano);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/planos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Plano Mensal"));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoAtualizarIdInexistente() throws Exception {
        expect(service.getPlanoById(99L)).andReturn(Optional.empty());
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/planos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Plano não encontrado"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoAtualizarLancarRegraNegocioException() throws Exception {
        expect(service.getPlanoById(1L)).andReturn(Optional.of(plano));
        expect(service.salvar(anyObject(Plano.class)))
                .andThrow(new RegraNegocioException("Nome inválido"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/planos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Nome inválido"));

        verifyAll();
    }


    @Test
    public void deveExcluirPlanoComSucesso() throws Exception {
        expect(service.getPlanoById(1L)).andReturn(Optional.of(plano));
        service.excluir(plano);
        expectLastCall().once();
        replayAll();

        mockMvc.perform(delete("/api/v1/planos/1"))
                .andExpect(status().isNoContent());

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoExcluirIdInexistente() throws Exception {
        expect(service.getPlanoById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(delete("/api/v1/planos/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Plano não encontrado "));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoExcluirLancarRegraNegocioException() throws Exception {
        expect(service.getPlanoById(1L)).andReturn(Optional.of(plano));
        service.excluir(plano);
        expectLastCall().andThrow(new RegraNegocioException("Não é possível excluir"));
        replayAll();

        mockMvc.perform(delete("/api/v1/planos/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Não é possível excluir"));

        verifyAll();
    }
}