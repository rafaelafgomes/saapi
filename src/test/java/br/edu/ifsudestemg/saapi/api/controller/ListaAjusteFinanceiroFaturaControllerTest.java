package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.ListaAjusteFinanceiroFaturaDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.AjusteFinanceiro;
import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.Fatura;
import br.edu.ifsudestemg.saapi.model.entity.ListaAjusteFinanceiroFatura;
import br.edu.ifsudestemg.saapi.service.AjusteFinanceiroService;
import br.edu.ifsudestemg.saapi.service.ClienteService;
import br.edu.ifsudestemg.saapi.service.FaturaService;
import br.edu.ifsudestemg.saapi.service.ListaAjusteFinanceiroFaturaService;
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
class ListaAjusteFinanceiroFaturaControllerTest extends EasyMockSupport {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ListaAjusteFinanceiroFaturaService service;

    @Mock
    private ClienteService clienteService;

    @Mock
    private AjusteFinanceiroService ajusteFinanceiroService;

    @Mock
    private FaturaService faturaService;

    private ListaAjusteFinanceiroFaturaController controller;
    private ListaAjusteFinanceiroFatura item;
    private AjusteFinanceiro ajusteFinanceiro;
    private Fatura fatura;
    private ListaAjusteFinanceiroFaturaDTO dto;

    @BeforeEach
    void setUp() {
        controller = new ListaAjusteFinanceiroFaturaController(
                service, clienteService, ajusteFinanceiroService, faturaService
        );
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        objectMapper = new ObjectMapper();

        ajusteFinanceiro = new AjusteFinanceiro();
        ajusteFinanceiro.setId(10L);
        ajusteFinanceiro.setNome("Desconto Pontualidade");

        Cliente cliente = new Cliente();
        cliente.setId(5L);
        cliente.setNome("Mariana Costa");

        fatura = new Fatura();
        fatura.setId(20L);
        fatura.setValor(120.0f);
        fatura.setCliente(cliente);

        item = new ListaAjusteFinanceiroFatura();
        item.setId(1L);
        item.setAjusteFinanceiro(ajusteFinanceiro);
        item.setFatura(fatura);

        dto = ListaAjusteFinanceiroFaturaDTO.create(item);
    }


    @Test
    public void deveRetornarTodasAsListas() throws Exception {
        expect(service.getListaAjusteFinanceiroFatura()).andReturn(List.of(item));
        replayAll();

        mockMvc.perform(get("/api/v1/listasajustesfinanceirosfaturas")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verifyAll();
    }


    @Test
    public void deveRetornarListaPorFaturaQuandoExistir() throws Exception {
        expect(faturaService.getFaturaById(20L)).andReturn(Optional.of(fatura));
        expect(service.getListaAjusteFinanceiroFaturaByFatura(fatura)).andReturn(List.of(item));
        replayAll();

        mockMvc.perform(get("/api/v1/listasajustesfinanceirosfaturas/fatura/20")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoBuscarPorFaturaInexistente() throws Exception {
        expect(faturaService.getFaturaById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(get("/api/v1/listasajustesfinanceirosfaturas/fatura/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Fatura não encontrada"));

        verifyAll();
    }


    @Test
    public void deveRetornarItemPorIdQuandoExistir() throws Exception {
        expect(service.getListaAjusteFinanceiroFaturaById(1L)).andReturn(Optional.of(item));
        replayAll();

        mockMvc.perform(get("/api/v1/listasajustesfinanceirosfaturas/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoBuscarIdInexistente() throws Exception {
        expect(service.getListaAjusteFinanceiroFaturaById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(get("/api/v1/listasajustesfinanceirosfaturas/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Lista de ajuste financeiro não encontrada"));

        verifyAll();
    }


    @Test
    public void deveSalvarItemComAjusteEFaturaExistentes() throws Exception {
        expect(ajusteFinanceiroService.getAjusteFinanceiroById(10L)).andReturn(Optional.of(ajusteFinanceiro));
        expect(faturaService.getFaturaById(20L)).andReturn(Optional.of(fatura));
        expect(service.salvar(anyObject(ListaAjusteFinanceiroFatura.class))).andReturn(item);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/listasajustesfinanceirosfaturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveSalvarItemQuandoAjusteEFaturaNaoExistirem() throws Exception {
        dto.setIdAjusteFinanceiro(99L);
        dto.setIdFatura(88L);
        expect(ajusteFinanceiroService.getAjusteFinanceiroById(99L)).andReturn(Optional.empty());
        expect(faturaService.getFaturaById(88L)).andReturn(Optional.empty());
        expect(service.salvar(anyObject(ListaAjusteFinanceiroFatura.class))).andReturn(item);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/listasajustesfinanceirosfaturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveSalvarItemSemAjusteEFaturaInformados() throws Exception {
        dto.setIdAjusteFinanceiro(null);
        dto.setIdFatura(null);
        expect(service.salvar(anyObject(ListaAjusteFinanceiroFatura.class))).andReturn(item);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/listasajustesfinanceirosfaturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoSalvarLancarRegraNegocioException() throws Exception {
        expect(ajusteFinanceiroService.getAjusteFinanceiroById(10L)).andReturn(Optional.of(ajusteFinanceiro));
        expect(faturaService.getFaturaById(20L)).andReturn(Optional.of(fatura));
        expect(service.salvar(anyObject(ListaAjusteFinanceiroFatura.class)))
                .andThrow(new RegraNegocioException("Fatura inválida"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/listasajustesfinanceirosfaturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Fatura inválida"));

        verifyAll();
    }


    @Test
    public void deveAtualizarItemComSucesso() throws Exception {
        expect(service.getListaAjusteFinanceiroFaturaById(1L)).andReturn(Optional.of(item));
        expect(ajusteFinanceiroService.getAjusteFinanceiroById(10L)).andReturn(Optional.of(ajusteFinanceiro));
        expect(faturaService.getFaturaById(20L)).andReturn(Optional.of(fatura));
        expect(service.salvar(anyObject(ListaAjusteFinanceiroFatura.class))).andReturn(item);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/listasajustesfinanceirosfaturas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoAtualizarIdInexistente() throws Exception {
        expect(service.getListaAjusteFinanceiroFaturaById(99L)).andReturn(Optional.empty());
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/listasajustesfinanceirosfaturas/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Lista de ajuste financeiro não encontrada"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoAtualizarLancarRegraNegocioException() throws Exception {
        expect(service.getListaAjusteFinanceiroFaturaById(1L)).andReturn(Optional.of(item));
        expect(ajusteFinanceiroService.getAjusteFinanceiroById(10L)).andReturn(Optional.of(ajusteFinanceiro));
        expect(faturaService.getFaturaById(20L)).andReturn(Optional.of(fatura));
        expect(service.salvar(anyObject(ListaAjusteFinanceiroFatura.class)))
                .andThrow(new RegraNegocioException("Ajuste inválido"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/listasajustesfinanceirosfaturas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Ajuste inválido"));

        verifyAll();
    }


    @Test
    public void deveExcluirItemComSucesso() throws Exception {
        expect(service.getListaAjusteFinanceiroFaturaById(1L)).andReturn(Optional.of(item));
        service.excluir(item);
        expectLastCall().once();
        replayAll();

        mockMvc.perform(delete("/api/v1/listasajustesfinanceirosfaturas/1"))
                .andExpect(status().isNoContent());

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoExcluirIdInexistente() throws Exception {
        expect(service.getListaAjusteFinanceiroFaturaById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(delete("/api/v1/listasajustesfinanceirosfaturas/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Lista de ajuste financeiro não encontrada "));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoExcluirLancarRegraNegocioException() throws Exception {
        expect(service.getListaAjusteFinanceiroFaturaById(1L)).andReturn(Optional.of(item));
        service.excluir(item);
        expectLastCall().andThrow(new RegraNegocioException("Não é possível excluir"));
        replayAll();

        mockMvc.perform(delete("/api/v1/listasajustesfinanceirosfaturas/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Não é possível excluir"));

        verifyAll();
    }
}