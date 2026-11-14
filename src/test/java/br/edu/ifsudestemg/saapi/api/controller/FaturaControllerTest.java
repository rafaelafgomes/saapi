package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.FaturaDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.Fatura;
import br.edu.ifsudestemg.saapi.model.entity.FormaPagamento;
import br.edu.ifsudestemg.saapi.model.entity.Plano;
import br.edu.ifsudestemg.saapi.service.ClienteService;
import br.edu.ifsudestemg.saapi.service.FaturaService;
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
class FaturaControllerTest extends EasyMockSupport {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private FaturaService service;

    @Mock
    private ClienteService clienteService;

    @Mock
    private FormaPagamentoService formaPagamentoService;

    private FaturaController controller;
    private Fatura fatura;
    private Cliente cliente;
    private Plano plano;
    private FormaPagamento formaPagamento;
    private FaturaDTO dto;

    @BeforeEach
    void setUp() {
        controller = new FaturaController(service, clienteService, formaPagamentoService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        objectMapper = new ObjectMapper();

        plano = new Plano();
        plano.setId(5L);
        plano.setNome("Plano Mensal");

        cliente = new Cliente();
        cliente.setId(10L);
        cliente.setNome("Mariana Costa");
        cliente.setPlano(plano);

        formaPagamento = new FormaPagamento();
        formaPagamento.setId(20L);
        formaPagamento.setNome("PIX");

        fatura = new Fatura();
        fatura.setId(1L);
        fatura.setDataVencimento("2026-11-20");
        fatura.setDataPagamento("2026-11-18");
        fatura.setValor(120.0f);
        fatura.setCliente(cliente);
        fatura.setFormaPagamento(formaPagamento);

        dto = FaturaDTO.create(fatura);
        dto.setIdsAjustesFinanceiros(List.of(100L, 200L));
    }

    @Test
    public void deveRetornarListaDeFaturas() throws Exception {
        expect(service.getFatura()).andReturn(List.of(fatura));
        replayAll();

        mockMvc.perform(get("/api/v1/faturas")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].valor").value(120.0));

        verifyAll();
    }


    @Test
    public void deveRetornarFaturaPorIdQuandoExistir() throws Exception {
        expect(service.getFaturaById(1L)).andReturn(Optional.of(fatura));
        replayAll();

        mockMvc.perform(get("/api/v1/faturas/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.valor").value(120.0));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoBuscarIdInexistente() throws Exception {
        expect(service.getFaturaById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(get("/api/v1/faturas/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Fatura não encontrada"));

        verifyAll();
    }


    @Test
    public void deveSalvarFaturaComClienteEFormaPagamentoExistentes() throws Exception {
        expect(clienteService.getClienteById(10L)).andReturn(Optional.of(cliente));
        expect(formaPagamentoService.getFormaPagamentoById(20L)).andReturn(Optional.of(formaPagamento));
        expect(service.salvar(anyObject(Fatura.class))).andReturn(fatura);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/faturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.valor").value(120.0));

        verifyAll();
    }

    @Test
    public void deveSalvarFaturaQuandoClienteEFormaPagamentoNaoExistirem() throws Exception {
        dto.setIdCliente(99L);
        dto.setIdFormaPagamento(88L);
        expect(clienteService.getClienteById(99L)).andReturn(Optional.empty());
        expect(formaPagamentoService.getFormaPagamentoById(88L)).andReturn(Optional.empty());
        expect(service.salvar(anyObject(Fatura.class))).andReturn(fatura);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/faturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveSalvarFaturaSemClienteEFormaPagamentoInformados() throws Exception {
        dto.setIdCliente(null);
        dto.setIdFormaPagamento(null);
        expect(service.salvar(anyObject(Fatura.class))).andReturn(fatura);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/faturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoSalvarLancarRegraNegocioException() throws Exception {
        expect(clienteService.getClienteById(10L)).andReturn(Optional.of(cliente));
        expect(formaPagamentoService.getFormaPagamentoById(20L)).andReturn(Optional.of(formaPagamento));
        expect(service.salvar(anyObject(Fatura.class)))
                .andThrow(new RegraNegocioException("Valor inválido"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/faturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Valor inválido"));

        verifyAll();
    }


    @Test
    public void deveAtualizarFaturaComSucesso() throws Exception {
        expect(service.getFaturaById(1L)).andReturn(Optional.of(fatura));
        expect(clienteService.getClienteById(10L)).andReturn(Optional.of(cliente));
        expect(formaPagamentoService.getFormaPagamentoById(20L)).andReturn(Optional.of(formaPagamento));
        expect(service.salvar(anyObject(Fatura.class))).andReturn(fatura);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/faturas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.valor").value(120.0));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoAtualizarIdInexistente() throws Exception {
        expect(service.getFaturaById(99L)).andReturn(Optional.empty());
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/faturas/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Fatura não encontrada"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoAtualizarLancarRegraNegocioException() throws Exception {
        expect(service.getFaturaById(1L)).andReturn(Optional.of(fatura));
        expect(clienteService.getClienteById(10L)).andReturn(Optional.of(cliente));
        expect(formaPagamentoService.getFormaPagamentoById(20L)).andReturn(Optional.of(formaPagamento));
        expect(service.salvar(anyObject(Fatura.class)))
                .andThrow(new RegraNegocioException("Data de vencimento inválida"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/faturas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Data de vencimento inválida"));

        verifyAll();
    }


    @Test
    public void deveExcluirFaturaComSucesso() throws Exception {
        expect(service.getFaturaById(1L)).andReturn(Optional.of(fatura));
        service.excluir(fatura);
        expectLastCall().once();
        replayAll();

        mockMvc.perform(delete("/api/v1/faturas/1"))
                .andExpect(status().isNoContent());

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoExcluirIdInexistente() throws Exception {
        expect(service.getFaturaById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(delete("/api/v1/faturas/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Fatura não encontrada "));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoExcluirLancarRegraNegocioException() throws Exception {
        expect(service.getFaturaById(1L)).andReturn(Optional.of(fatura));
        service.excluir(fatura);
        expectLastCall().andThrow(new RegraNegocioException("Não é possível excluir"));
        replayAll();

        mockMvc.perform(delete("/api/v1/faturas/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Não é possível excluir"));

        verifyAll();
    }


    @Test
    public void deveCalcularValorFaturaComSucesso() throws Exception {
        expect(service.calcularValorFatura(10L, List.of(100L, 200L))).andReturn(110.0f);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/faturas/calcular")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("110.0"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoCalcularValorLancarRegraNegocioException() throws Exception {
        expect(service.calcularValorFatura(10L, List.of(100L, 200L)))
                .andThrow(new RegraNegocioException("Cliente não encontrado"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/faturas/calcular")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cliente não encontrado"));

        verifyAll();
    }
}