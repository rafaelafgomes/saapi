package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.ListaEquipamentoAulaDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.entity.Equipamento;
import br.edu.ifsudestemg.saapi.model.entity.Funcionario;
import br.edu.ifsudestemg.saapi.model.entity.ListaEquipamentoAula;
import br.edu.ifsudestemg.saapi.service.AulaService;
import br.edu.ifsudestemg.saapi.service.EquipamentoService;
import br.edu.ifsudestemg.saapi.service.ListaEquipamentoAulaService;
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
class ListaEquipamentoAulaControllerTest extends EasyMockSupport {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ListaEquipamentoAulaService service;

    @Mock
    private EquipamentoService equipamentoService;

    @Mock
    private AulaService aulaService;

    private ListaEquipamentoAulaController controller;
    private ListaEquipamentoAula item;
    private Equipamento equipamento;
    private Aula aula;
    private ListaEquipamentoAulaDTO dto;

    @BeforeEach
    void setUp() {
        controller = new ListaEquipamentoAulaController(service, equipamentoService, aulaService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        objectMapper = new ObjectMapper();

        Funcionario funcionario = new Funcionario();
        funcionario.setId(5L);
        funcionario.setNome("Carlos Instrutor");

        equipamento = new Equipamento();
        equipamento.setId(10L);
        equipamento.setNome("Bicicleta Spinning");

        aula = new Aula();
        aula.setId(20L);
        aula.setNome("Spinning");
        aula.setFuncionario(funcionario);

        item = new ListaEquipamentoAula();
        item.setId(1L);
        item.setEquipamento(equipamento);
        item.setAula(aula);

        dto = ListaEquipamentoAulaDTO.create(item);
    }


    @Test
    public void deveRetornarTodasAsListas() throws Exception {
        expect(service.getListaEquipamentoAula()).andReturn(List.of(item));
        replayAll();

        mockMvc.perform(get("/api/v1/listasequipamentosaulas")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verifyAll();
    }


    @Test
    public void deveRetornarListaPorAulaQuandoExistir() throws Exception {
        expect(aulaService.getAulaById(20L)).andReturn(Optional.of(aula));
        expect(service.getListaEquipamentoAulaByAula(aula)).andReturn(List.of(item));
        replayAll();

        mockMvc.perform(get("/api/v1/listasequipamentosaulas/aula/20")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoBuscarPorAulaInexistente() throws Exception {
        expect(aulaService.getAulaById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(get("/api/v1/listasequipamentosaulas/aula/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Aula não encontrada"));

        verifyAll();
    }

    @Test
    public void deveRetornarItemPorIdQuandoExistir() throws Exception {
        expect(service.getListaEquipamentoAulaById(1L)).andReturn(Optional.of(item));
        replayAll();

        mockMvc.perform(get("/api/v1/listasequipamentosaulas/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoBuscarIdInexistente() throws Exception {
        expect(service.getListaEquipamentoAulaById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(get("/api/v1/listasequipamentosaulas/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Lista Equipamento por Aula não encontrada"));

        verifyAll();
    }


    @Test
    public void deveSalvarItemComEquipamentoEAulaExistentes() throws Exception {
        expect(equipamentoService.getEquipamentoById(10L)).andReturn(Optional.of(equipamento));
        expect(aulaService.getAulaById(20L)).andReturn(Optional.of(aula));
        expect(service.salvar(anyObject(ListaEquipamentoAula.class))).andReturn(item);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/listasequipamentosaulas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveSalvarItemQuandoEquipamentoEAulaNaoExistirem() throws Exception {
        dto.setIdEquipamento(99L);
        dto.setIdAula(88L);
        expect(equipamentoService.getEquipamentoById(99L)).andReturn(Optional.empty());
        expect(aulaService.getAulaById(88L)).andReturn(Optional.empty());
        expect(service.salvar(anyObject(ListaEquipamentoAula.class))).andReturn(item);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/listasequipamentosaulas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveSalvarItemSemEquipamentoEAulaInformados() throws Exception {
        dto.setIdEquipamento(null);
        dto.setIdAula(null);
        expect(service.salvar(anyObject(ListaEquipamentoAula.class))).andReturn(item);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/listasequipamentosaulas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoSalvarLancarRegraNegocioException() throws Exception {
        expect(equipamentoService.getEquipamentoById(10L)).andReturn(Optional.of(equipamento));
        expect(aulaService.getAulaById(20L)).andReturn(Optional.of(aula));
        expect(service.salvar(anyObject(ListaEquipamentoAula.class)))
                .andThrow(new RegraNegocioException("Equipamento indisponível"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/listasequipamentosaulas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Equipamento indisponível"));

        verifyAll();
    }


    @Test
    public void deveAtualizarItemComSucesso() throws Exception {
        expect(service.getListaEquipamentoAulaById(1L)).andReturn(Optional.of(item));
        expect(equipamentoService.getEquipamentoById(10L)).andReturn(Optional.of(equipamento));
        expect(aulaService.getAulaById(20L)).andReturn(Optional.of(aula));
        expect(service.salvar(anyObject(ListaEquipamentoAula.class))).andReturn(item);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/listasequipamentosaulas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoAtualizarIdInexistente() throws Exception {
        expect(service.getListaEquipamentoAulaById(99L)).andReturn(Optional.empty());
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/listasequipamentosaulas/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Lista Equipamento por Aula não encontrada"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoAtualizarLancarRegraNegocioException() throws Exception {
        expect(service.getListaEquipamentoAulaById(1L)).andReturn(Optional.of(item));
        expect(equipamentoService.getEquipamentoById(10L)).andReturn(Optional.of(equipamento));
        expect(aulaService.getAulaById(20L)).andReturn(Optional.of(aula));
        expect(service.salvar(anyObject(ListaEquipamentoAula.class)))
                .andThrow(new RegraNegocioException("Quantidade inválida"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/listasequipamentosaulas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Quantidade inválida"));

        verifyAll();
    }


    @Test
    public void deveExcluirItemComSucesso() throws Exception {
        expect(service.getListaEquipamentoAulaById(1L)).andReturn(Optional.of(item));
        service.excluir(item);
        expectLastCall().once();
        replayAll();

        mockMvc.perform(delete("/api/v1/listasequipamentosaulas/1"))
                .andExpect(status().isNoContent());

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoExcluirIdInexistente() throws Exception {
        expect(service.getListaEquipamentoAulaById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(delete("/api/v1/listasequipamentosaulas/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Lista de equipamentos com aula não encontrada "));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoExcluirLancarRegraNegocioException() throws Exception {
        expect(service.getListaEquipamentoAulaById(1L)).andReturn(Optional.of(item));
        service.excluir(item);
        expectLastCall().andThrow(new RegraNegocioException("Não é possível excluir"));
        replayAll();

        mockMvc.perform(delete("/api/v1/listasequipamentosaulas/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Não é possível excluir"));

        verifyAll();
    }
}