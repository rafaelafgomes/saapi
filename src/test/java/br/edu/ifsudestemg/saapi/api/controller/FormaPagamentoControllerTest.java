package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.FormaPagamentoDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.FormaPagamento;
import br.edu.ifsudestemg.saapi.service.FormaPagamentoService;
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
class FormaPagamentoControllerTest extends EasyMockSupport {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private FormaPagamentoService service;

    private FormaPagamentoController controller;
    private FormaPagamento formaPagamento;
    private FormaPagamentoDTO dto;

    @BeforeEach
    void setUp() {
        controller = new FormaPagamentoController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        objectMapper = new ObjectMapper();

        formaPagamento = new FormaPagamento();
        formaPagamento.setId(1L);
        formaPagamento.setNome("PIX");

        dto = FormaPagamentoDTO.create(formaPagamento);
    }


    @Test
    public void deveRetornarListaDeFormasPagamento() throws Exception {
        expect(service.getFormaPagamento()).andReturn(List.of(formaPagamento));
        replayAll();

        mockMvc.perform(get("/api/v1/formaspagamentos")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("PIX"));

        verifyAll();
    }


    @Test
    public void deveRetornarFormaPagamentoPorIdQuandoExistir() throws Exception {
        expect(service.getFormaPagamentoById(1L)).andReturn(Optional.of(formaPagamento));
        replayAll();

        mockMvc.perform(get("/api/v1/formaspagamentos/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("PIX"));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoBuscarIdInexistente() throws Exception {
        expect(service.getFormaPagamentoById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(get("/api/v1/formaspagamentos/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Forma de pagamento não encontrada"));

        verifyAll();
    }


    @Test
    public void deveSalvarFormaPagamentoComSucesso() throws Exception {
        expect(service.salvar(anyObject(FormaPagamento.class))).andReturn(formaPagamento);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/formaspagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("PIX"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoSalvarLancarRegraNegocioException() throws Exception {
        expect(service.salvar(anyObject(FormaPagamento.class)))
                .andThrow(new RegraNegocioException("Nome inválido"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/formaspagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Nome inválido"));

        verifyAll();
    }


    @Test
    public void deveAtualizarFormaPagamentoComSucesso() throws Exception {
        expect(service.getFormaPagamentoById(1L)).andReturn(Optional.of(formaPagamento));
        expect(service.salvar(anyObject(FormaPagamento.class))).andReturn(formaPagamento);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/formaspagamentos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("PIX"));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoAtualizarIdInexistente() throws Exception {
        expect(service.getFormaPagamentoById(99L)).andReturn(Optional.empty());
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/formaspagamentos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Forma de Pagamento não encontrada"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoAtualizarLancarRegraNegocioException() throws Exception {
        expect(service.getFormaPagamentoById(1L)).andReturn(Optional.of(formaPagamento));
        expect(service.salvar(anyObject(FormaPagamento.class)))
                .andThrow(new RegraNegocioException("Nome inválido"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/formaspagamentos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Nome inválido"));

        verifyAll();
    }


    @Test
    public void deveExcluirFormaPagamentoComSucesso() throws Exception {
        expect(service.getFormaPagamentoById(1L)).andReturn(Optional.of(formaPagamento));
        service.excluir(formaPagamento);
        expectLastCall().once();
        replayAll();

        mockMvc.perform(delete("/api/v1/formaspagamentos/1"))
                .andExpect(status().isNoContent());

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoExcluirIdInexistente() throws Exception {
        expect(service.getFormaPagamentoById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(delete("/api/v1/formaspagamentos/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Forma de pagamento não encontrada "));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoExcluirLancarRegraNegocioException() throws Exception {
        expect(service.getFormaPagamentoById(1L)).andReturn(Optional.of(formaPagamento));
        service.excluir(formaPagamento);
        expectLastCall().andThrow(new RegraNegocioException("Não é possível excluir"));
        replayAll();

        mockMvc.perform(delete("/api/v1/formaspagamentos/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Não é possível excluir"));

        verifyAll();
    }
}