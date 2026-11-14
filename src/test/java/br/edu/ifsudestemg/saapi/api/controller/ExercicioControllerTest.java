package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.ExercicioDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Exercicio;
import br.edu.ifsudestemg.saapi.service.ExercicioService;
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
class ExercicioControllerTest extends EasyMockSupport {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ExercicioService service;

    private ExercicioController controller;
    private Exercicio exercicio;
    private ExercicioDTO dto;

    @BeforeEach
    void setUp() {
        controller = new ExercicioController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        objectMapper = new ObjectMapper();

        exercicio = new Exercicio();
        exercicio.setId(1L);
        exercicio.setNome("Supino Reto");

        dto = ExercicioDTO.create(exercicio);
    }


    @Test
    public void deveRetornarListaDeExercicios() throws Exception {
        expect(service.getExercicio()).andReturn(List.of(exercicio));
        replayAll();

        mockMvc.perform(get("/api/v1/exercicios")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Supino Reto"));

        verifyAll();
    }


    @Test
    public void deveRetornarExercicioPorIdQuandoExistir() throws Exception {
        expect(service.getExercicioById(1L)).andReturn(Optional.of(exercicio));
        replayAll();

        mockMvc.perform(get("/api/v1/exercicios/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Supino Reto"));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoBuscarIdInexistente() throws Exception {
        expect(service.getExercicioById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(get("/api/v1/exercicios/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Exercicio não encontrado"));

        verifyAll();
    }


    @Test
    public void deveSalvarExercicioComSucesso() throws Exception {
        expect(service.salvar(anyObject(Exercicio.class))).andReturn(exercicio);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/exercicios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Supino Reto"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoSalvarLancarRegraNegocioException() throws Exception {
        expect(service.salvar(anyObject(Exercicio.class)))
                .andThrow(new RegraNegocioException("Nome inválido"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/exercicios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Nome inválido"));

        verifyAll();
    }


    @Test
    public void deveAtualizarExercicioComSucesso() throws Exception {
        expect(service.getExercicioById(1L)).andReturn(Optional.of(exercicio));
        expect(service.salvar(anyObject(Exercicio.class))).andReturn(exercicio);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/exercicios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Supino Reto"));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoAtualizarIdInexistente() throws Exception {
        expect(service.getExercicioById(99L)).andReturn(Optional.empty());
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/exercicios/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Exercicio não encontrado"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoAtualizarLancarRegraNegocioException() throws Exception {
        expect(service.getExercicioById(1L)).andReturn(Optional.of(exercicio));
        expect(service.salvar(anyObject(Exercicio.class)))
                .andThrow(new RegraNegocioException("Nome inválido"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/exercicios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Nome inválido"));

        verifyAll();
    }


    @Test
    public void deveExcluirExercicioComSucesso() throws Exception {
        expect(service.getExercicioById(1L)).andReturn(Optional.of(exercicio));
        service.excluir(exercicio);
        expectLastCall().once();
        replayAll();

        mockMvc.perform(delete("/api/v1/exercicios/1"))
                .andExpect(status().isNoContent());

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoExcluirIdInexistente() throws Exception {
        expect(service.getExercicioById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(delete("/api/v1/exercicios/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Exercicio não encontrado "));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoExcluirLancarRegraNegocioException() throws Exception {
        expect(service.getExercicioById(1L)).andReturn(Optional.of(exercicio));
        service.excluir(exercicio);
        expectLastCall().andThrow(new RegraNegocioException("Não é possível excluir"));
        replayAll();

        mockMvc.perform(delete("/api/v1/exercicios/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Não é possível excluir"));

        verifyAll();
    }
}