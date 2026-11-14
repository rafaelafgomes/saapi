package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.AjusteFinanceiroDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.AjusteFinanceiro;
import br.edu.ifsudestemg.saapi.service.AjusteFinanceiroService;
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
class AjusteFinanceiroControllerTest extends EasyMockSupport {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private AjusteFinanceiroService service;

    private AjusteFinanceiroController controller;
    private AjusteFinanceiro ajusteFinanceiro;
    private AjusteFinanceiroDTO dto;

    @BeforeEach
    void setUp() {
        controller = new AjusteFinanceiroController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        objectMapper = new ObjectMapper();

        ajusteFinanceiro = new AjusteFinanceiro();
        ajusteFinanceiro.setId(1L);
        ajusteFinanceiro.setNome("Desconto Pontualidade");
        ajusteFinanceiro.setTipo("DESCONTO");
        ajusteFinanceiro.setUnidade("PORCENTAGEM");
        ajusteFinanceiro.setValor(10.0f);
        ajusteFinanceiro.setDescricao("Desconto para pagamentos até o dia 5");

        dto = AjusteFinanceiroDTO.create(ajusteFinanceiro);
    }

    @Test
    public void deveRetornarListaDeAjustesFinanceiros() throws Exception {
        expect(service.getAjusteFinanceiros()).andReturn(List.of(ajusteFinanceiro));
        replayAll();

        mockMvc.perform(get("/api/v1/ajustesfinanceiros")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Desconto Pontualidade"));

        verifyAll();
    }

    @Test
    public void deveRetornarAjusteFinanceiroPorIdQuandoExistir() throws Exception {
        expect(service.getAjusteFinanceiroById(1L)).andReturn(Optional.of(ajusteFinanceiro));
        replayAll();

        mockMvc.perform(get("/api/v1/ajustesfinanceiros/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Desconto Pontualidade"));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoBuscarIdInexistente() throws Exception {
        expect(service.getAjusteFinanceiroById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(get("/api/v1/ajustesfinanceiros/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Ajuste não encontrado"));

        verifyAll();
    }

    @Test
    public void deveSalvarAjusteFinanceiroComSucesso() throws Exception {
        expect(service.salvar(anyObject(AjusteFinanceiro.class))).andReturn(ajusteFinanceiro);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/ajustesfinanceiros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Desconto Pontualidade"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoSalvarLancarRegraNegocioException() throws Exception {
        expect(service.salvar(anyObject(AjusteFinanceiro.class)))
                .andThrow(new RegraNegocioException("Nome inválido"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/ajustesfinanceiros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Nome inválido"));

        verifyAll();
    }

    @Test
    public void deveAtualizarAjusteFinanceiroComSucesso() throws Exception {
        expect(service.getAjusteFinanceiroById(1L)).andReturn(Optional.of(ajusteFinanceiro));
        expect(service.salvar(anyObject(AjusteFinanceiro.class))).andReturn(ajusteFinanceiro);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/ajustesfinanceiros/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Desconto Pontualidade"));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoAtualizarIdInexistente() throws Exception {
        expect(service.getAjusteFinanceiroById(99L)).andReturn(Optional.empty());
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/ajustesfinanceiros/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Ajuste Financeiro não encontrado"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoAtualizarLancarRegraNegocioException() throws Exception {
        expect(service.getAjusteFinanceiroById(1L)).andReturn(Optional.of(ajusteFinanceiro));
        expect(service.salvar(anyObject(AjusteFinanceiro.class)))
                .andThrow(new RegraNegocioException("Valor inválido"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/ajustesfinanceiros/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Valor inválido"));

        verifyAll();
    }

    @Test
    public void deveExcluirAjusteFinanceiroComSucesso() throws Exception {
        expect(service.getAjusteFinanceiroById(1L)).andReturn(Optional.of(ajusteFinanceiro));
        service.excluir(ajusteFinanceiro);
        expectLastCall().once();
        replayAll();

        mockMvc.perform(delete("/api/v1/ajustesfinanceiros/1"))
                .andExpect(status().isNoContent());

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoExcluirIdInexistente() throws Exception {
        expect(service.getAjusteFinanceiroById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(delete("/api/v1/ajustesfinanceiros/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Ajuste financeiro não encontrado"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoExcluirLancarRegraNegocioException() throws Exception {
        expect(service.getAjusteFinanceiroById(1L)).andReturn(Optional.of(ajusteFinanceiro));
        service.excluir(ajusteFinanceiro);
        expectLastCall().andThrow(new RegraNegocioException("Não é possível excluir"));
        replayAll();

        mockMvc.perform(delete("/api/v1/ajustesfinanceiros/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Não é possível excluir"));

        verifyAll();
    }
}