package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.ListaExercicioTreinoDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.Exercicio;
import br.edu.ifsudestemg.saapi.model.entity.Funcionario;
import br.edu.ifsudestemg.saapi.model.entity.ListaExercicioTreino;
import br.edu.ifsudestemg.saapi.model.entity.Treino;
import br.edu.ifsudestemg.saapi.service.ExercicioService;
import br.edu.ifsudestemg.saapi.service.ListaExercicioTreinoService;
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
class ListaExercicioTreinoControllerTest extends EasyMockSupport {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ListaExercicioTreinoService service;

    @Mock
    private ExercicioService exercicioService;

    @Mock
    private TreinoService treinoService;

    private ListaExercicioTreinoController controller;
    private ListaExercicioTreino item;
    private Exercicio exercicio;
    private Treino treino;
    private ListaExercicioTreinoDTO dto;

    @BeforeEach
    void setUp() {
        controller = new ListaExercicioTreinoController(service, exercicioService, treinoService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        objectMapper = new ObjectMapper();

        Cliente cliente = new Cliente();
        cliente.setId(5L);
        cliente.setNome("Mariana Costa");

        Funcionario funcionario = new Funcionario();
        funcionario.setId(6L);
        funcionario.setNome("Roberto Alves");

        exercicio = new Exercicio();
        exercicio.setId(10L);
        exercicio.setNome("Supino Reto");

        treino = new Treino();
        treino.setId(20L);
        treino.setNome("Treino A");
        treino.setCliente(cliente);
        treino.setFuncionario(funcionario);

        item = new ListaExercicioTreino();
        item.setId(1L);
        item.setExercicio(exercicio);
        item.setTreino(treino);

        dto = ListaExercicioTreinoDTO.create(item);
    }


    @Test
    public void deveRetornarTodasAsListas() throws Exception {
        expect(service.getListaExercicioTreino()).andReturn(List.of(item));
        replayAll();

        mockMvc.perform(get("/api/v1/listasexerciciostreinos")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verifyAll();
    }

    @Test
    public void deveRetornarListaPorTreinoQuandoExistir() throws Exception {
        expect(treinoService.getTreinoById(20L)).andReturn(Optional.of(treino));
        expect(service.getListaExercicioTreinoByTreino(treino)).andReturn(List.of(item));
        replayAll();

        mockMvc.perform(get("/api/v1/listasexerciciostreinos/treino/20")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoBuscarPorTreinoInexistente() throws Exception {
        expect(treinoService.getTreinoById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(get("/api/v1/listasexerciciostreinos/treino/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Treino não encontrado"));

        verifyAll();
    }

    @Test
    public void deveRetornarItemPorIdQuandoExistir() throws Exception {
        expect(service.getListaExercicioTreinoById(1L)).andReturn(Optional.of(item));
        replayAll();

        mockMvc.perform(get("/api/v1/listasexerciciostreinos/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoBuscarIdInexistente() throws Exception {
        expect(service.getListaExercicioTreinoById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(get("/api/v1/listasexerciciostreinos/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Lista exercicio de treino não encontrada"));

        verifyAll();
    }


    @Test
    public void deveSalvarItemComExercicioETreinoExistentes() throws Exception {
        expect(exercicioService.getExercicioById(10L)).andReturn(Optional.of(exercicio));
        expect(treinoService.getTreinoById(20L)).andReturn(Optional.of(treino));
        expect(service.salvar(anyObject(ListaExercicioTreino.class))).andReturn(item);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/listasexerciciostreinos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveSalvarItemQuandoExercicioETreinoNaoExistirem() throws Exception {
        dto.setIdExercicio(99L);
        dto.setIdTreino(88L);
        expect(exercicioService.getExercicioById(99L)).andReturn(Optional.empty());
        expect(treinoService.getTreinoById(88L)).andReturn(Optional.empty());
        expect(service.salvar(anyObject(ListaExercicioTreino.class))).andReturn(item);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/listasexerciciostreinos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveSalvarItemSemExercicioETreinoInformados() throws Exception {
        dto.setIdExercicio(null);
        dto.setIdTreino(null);
        expect(service.salvar(anyObject(ListaExercicioTreino.class))).andReturn(item);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/listasexerciciostreinos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoSalvarLancarRegraNegocioException() throws Exception {
        expect(exercicioService.getExercicioById(10L)).andReturn(Optional.of(exercicio));
        expect(treinoService.getTreinoById(20L)).andReturn(Optional.of(treino));
        expect(service.salvar(anyObject(ListaExercicioTreino.class)))
                .andThrow(new RegraNegocioException("Séries inválidas"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/listasexerciciostreinos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Séries inválidas"));

        verifyAll();
    }


    @Test
    public void deveAtualizarItemComSucesso() throws Exception {
        expect(service.getListaExercicioTreinoById(1L)).andReturn(Optional.of(item));
        expect(exercicioService.getExercicioById(10L)).andReturn(Optional.of(exercicio));
        expect(treinoService.getTreinoById(20L)).andReturn(Optional.of(treino));
        expect(service.salvar(anyObject(ListaExercicioTreino.class))).andReturn(item);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/listasexerciciostreinos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoAtualizarIdInexistente() throws Exception {
        expect(service.getListaExercicioTreinoById(99L)).andReturn(Optional.empty());
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/listasexerciciostreinos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Lista exercicio de treino não encontrada"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoAtualizarLancarRegraNegocioException() throws Exception {
        expect(service.getListaExercicioTreinoById(1L)).andReturn(Optional.of(item));
        expect(exercicioService.getExercicioById(10L)).andReturn(Optional.of(exercicio));
        expect(treinoService.getTreinoById(20L)).andReturn(Optional.of(treino));
        expect(service.salvar(anyObject(ListaExercicioTreino.class)))
                .andThrow(new RegraNegocioException("Repetições inválidas"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/listasexerciciostreinos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Repetições inválidas"));

        verifyAll();
    }


    @Test
    public void deveExcluirItemComSucesso() throws Exception {
        expect(service.getListaExercicioTreinoById(1L)).andReturn(Optional.of(item));
        service.excluir(item);
        expectLastCall().once();
        replayAll();

        mockMvc.perform(delete("/api/v1/listasexerciciostreinos/1"))
                .andExpect(status().isNoContent());

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoExcluirIdInexistente() throws Exception {
        expect(service.getListaExercicioTreinoById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(delete("/api/v1/listasexerciciostreinos/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Lista de exercícios com treino não encontrada "));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoExcluirLancarRegraNegocioException() throws Exception {
        expect(service.getListaExercicioTreinoById(1L)).andReturn(Optional.of(item));
        service.excluir(item);
        expectLastCall().andThrow(new RegraNegocioException("Não é possível excluir"));
        replayAll();

        mockMvc.perform(delete("/api/v1/listasexerciciostreinos/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Não é possível excluir"));

        verifyAll();
    }
}