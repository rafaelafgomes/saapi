package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.CargoDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Cargo;
import br.edu.ifsudestemg.saapi.service.CargoService;
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
class CargoControllerTest extends EasyMockSupport {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private CargoService service;

    private Cargo cargo;
    private CargoController controller;
    private CargoDTO dto;

    @BeforeEach
    void setUp() {
        controller = new CargoController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        objectMapper = new ObjectMapper();

        cargo = new Cargo();
        cargo.setId(1L);
        cargo.setNome("Cargo teste");
        cargo.setDescricao("Descrição teste");

        dto = CargoDTO.create(cargo);
    }

    @Test
    public void deveRetornarListaDeCargos() throws Exception {
        expect(service.getCargo()).andReturn(List.of(cargo));
        replayAll();

        mockMvc.perform(get("/api/v1/cargos")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Cargo teste"));
        verifyAll();
    }

    @Test
    public void deveRetornarCargoPorIdQuandoExistir() throws Exception {
        expect(service.getCargoById(1L)).andReturn(Optional.of(cargo));
        replayAll();

        mockMvc.perform(get("/api/v1/cargos/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Cargo teste"));
        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoBuscarIdInexistente() throws Exception {
        expect(service.getCargoById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(get("/api/v1/cargos/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cargo não encontrado"));
        verifyAll();
    }

    @Test
    public void deveSalvarCargoComSucesso() throws Exception {
        expect(service.salvar(anyObject(Cargo.class))).andReturn(cargo);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/cargos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Cargo teste"));
        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoSalvarLancarRegraNegocioException() throws Exception {
        expect(service.salvar(anyObject(Cargo.class))).andThrow(new RegraNegocioException("Nome inválido"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/cargos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Nome inválido"));
        verifyAll();
    }

    @Test
    public void deveAtualizarAjusteFinanceiroComSucesso() throws Exception {
        expect(service.getCargoById(1L)).andReturn(Optional.of(cargo));
        expect(service.salvar(anyObject(Cargo.class))).andReturn(cargo);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/cargos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Cargo teste"));
        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoAtualizarIdInexistente() throws Exception {
        expect(service.getCargoById(99L)).andReturn(Optional.empty());
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/cargos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cargo não encontrado"));
        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoAtualizarLancarRegraNegocioException() throws Exception {
        expect(service.getCargoById(1L)).andReturn(Optional.of(cargo));
        expect(service.salvar(anyObject(Cargo.class))).andThrow(new RegraNegocioException("Valor inválido"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/cargos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Valor inválido"));
        verifyAll();
    }

    @Test
    public void deveExcluirAjusteFinanceiroComSucesso() throws Exception {
        expect(service.getCargoById(1L)).andReturn(Optional.of(cargo));
        service.excluir(cargo);
        expectLastCall().once();
        replayAll();

        mockMvc.perform(delete("/api/v1/cargos/1"))
                .andExpect(status().isNoContent());
        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoExcluirIdInexistente() throws Exception {
        expect(service.getCargoById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(delete("/api/v1/cargos/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cargo não encontrado"));
        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoExcluirLancarRegraNegocioException() throws Exception {
        expect(service.getCargoById(1L)).andReturn(Optional.of(cargo));
        service.excluir(cargo);
        expectLastCall().andThrow(new RegraNegocioException("Não é possivel excluir"));
        replayAll();

        mockMvc.perform(delete("/api/v1/cargos/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Não é possivel excluir"));
        verifyAll();
    }
























}