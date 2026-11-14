package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.FichaAvaliacaoFisicaDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.FichaAvaliacaoFisica;
import br.edu.ifsudestemg.saapi.model.entity.Funcionario;
import br.edu.ifsudestemg.saapi.service.ClienteService;
import br.edu.ifsudestemg.saapi.service.FichaAvaliacaoFisicaService;
import br.edu.ifsudestemg.saapi.service.FuncionarioService;
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
class FichaAvaliacaoFisicaControllerTest extends EasyMockSupport {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private FichaAvaliacaoFisicaService service;

    @Mock
    private ClienteService clienteService;

    @Mock
    private FuncionarioService funcionarioService;

    private FichaAvaliacaoFisicaController controller;
    private FichaAvaliacaoFisica ficha;
    private Cliente cliente;
    private Funcionario funcionario;
    private FichaAvaliacaoFisicaDTO dto;

    @BeforeEach
    void setUp() {
        controller = new FichaAvaliacaoFisicaController(service, clienteService, funcionarioService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        objectMapper = new ObjectMapper();

        cliente = new Cliente();
        cliente.setId(10L);
        cliente.setNome("Mariana Costa");
        cliente.setSexo(false);

        funcionario = new Funcionario();
        funcionario.setId(20L);
        funcionario.setNome("Roberto Alves");

        ficha = new FichaAvaliacaoFisica();
        ficha.setId(1L);
        ficha.setPeso(65.0f);
        ficha.setAltura(1.70f);
        ficha.setCliente(cliente);
        ficha.setFuncionario(funcionario);

        dto = FichaAvaliacaoFisicaDTO.create(ficha);
        dto.setSexoCliente(false);
    }


    @Test
    public void deveRetornarListaDeFichas() throws Exception {
        expect(service.getFichaAvaliacaoFisica()).andReturn(List.of(ficha));
        replayAll();

        mockMvc.perform(get("/api/v1/fichasavaliacaofisica")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].peso").value(65.0));

        verifyAll();
    }


    @Test
    public void deveRetornarFichaPorIdQuandoExistir() throws Exception {
        expect(service.getFichaAvaliacaoFisicaById(1L)).andReturn(Optional.of(ficha));
        replayAll();

        mockMvc.perform(get("/api/v1/fichasavaliacaofisica/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.peso").value(65.0));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoBuscarIdInexistente() throws Exception {
        expect(service.getFichaAvaliacaoFisicaById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(get("/api/v1/fichasavaliacaofisica/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Ficha de avaliação não encontrada"));

        verifyAll();
    }


    @Test
    public void deveSalvarFichaComClienteEFuncionarioExistentes() throws Exception {
        expect(clienteService.getClienteById(10L)).andReturn(Optional.of(cliente));
        expect(funcionarioService.getFuncionarioById(20L)).andReturn(Optional.of(funcionario));
        expect(service.salvar(anyObject(FichaAvaliacaoFisica.class))).andReturn(ficha);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/fichasavaliacaofisica")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveSalvarFichaQuandoClienteEFuncionarioNaoExistirem() throws Exception {
        dto.setIdCliente(99L);
        dto.setIdFuncionario(88L);
        expect(clienteService.getClienteById(99L)).andReturn(Optional.empty());
        expect(funcionarioService.getFuncionarioById(88L)).andReturn(Optional.empty());
        expect(service.salvar(anyObject(FichaAvaliacaoFisica.class))).andReturn(ficha);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/fichasavaliacaofisica")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveSalvarFichaSemClienteEFuncionarioInformados() throws Exception {
        dto.setIdCliente(null);
        dto.setIdFuncionario(null);
        expect(service.salvar(anyObject(FichaAvaliacaoFisica.class))).andReturn(ficha);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/fichasavaliacaofisica")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoSalvarLancarRegraNegocioException() throws Exception {
        expect(clienteService.getClienteById(10L)).andReturn(Optional.of(cliente));
        expect(funcionarioService.getFuncionarioById(20L)).andReturn(Optional.of(funcionario));
        expect(service.salvar(anyObject(FichaAvaliacaoFisica.class)))
                .andThrow(new RegraNegocioException("Peso inválido"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/fichasavaliacaofisica")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Peso inválido"));

        verifyAll();
    }


    @Test
    public void deveCalcularIMCComSucesso() throws Exception {
        expect(service.calcularIMC(65.0f, 1.70f, false)).andReturn("Peso normal");
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/fichasavaliacaofisica/calcular-imc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Peso normal"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoPesoForNuloOuZeroNoCalculoIMC() throws Exception {
        dto.setPeso(null);
        String jsonNulo = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/fichasavaliacaofisica/calcular-imc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonNulo))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Peso inválido"));

        dto.setPeso(0.0f);
        String jsonZero = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/fichasavaliacaofisica/calcular-imc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonZero))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Peso inválido"));
    }

    @Test
    public void deveRetornarBadRequestQuandoAlturaForNulaOuZeroNoCalculoIMC() throws Exception {
        dto.setAltura(null);
        String jsonNula = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/fichasavaliacaofisica/calcular-imc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonNula))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Altura inválida"));

        dto.setAltura(0.0f);
        String jsonZero = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/fichasavaliacaofisica/calcular-imc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonZero))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Altura inválida"));
    }

    @Test
    public void deveRetornarBadRequestQuandoSexoForNuloNoCalculoIMC() throws Exception {
        dto.setSexoCliente(null);
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/fichasavaliacaofisica/calcular-imc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Sexo inválida"));
    }

    @Test
    public void deveRetornarBadRequestQuandoCalculoIMCLancarExcecao() throws Exception {
        expect(service.calcularIMC(65.0f, 1.70f, false))
                .andThrow(new RuntimeException("Erro ao calcular IMC"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/fichasavaliacaofisica/calcular-imc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erro ao calcular IMC"));

        verifyAll();
    }


    @Test
    public void deveAtualizarFichaComSucesso() throws Exception {
        expect(service.getFichaAvaliacaoFisicaById(1L)).andReturn(Optional.of(ficha));
        expect(clienteService.getClienteById(10L)).andReturn(Optional.of(cliente));
        expect(funcionarioService.getFuncionarioById(20L)).andReturn(Optional.of(funcionario));
        expect(service.salvar(anyObject(FichaAvaliacaoFisica.class))).andReturn(ficha);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/fichasavaliacaofisica/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoAtualizarIdInexistente() throws Exception {
        expect(service.getFichaAvaliacaoFisicaById(99L)).andReturn(Optional.empty());
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/fichasavaliacaofisica/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Ficha de Avaliacao Fisica não encontrada"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoAtualizarLancarRegraNegocioException() throws Exception {
        expect(service.getFichaAvaliacaoFisicaById(1L)).andReturn(Optional.of(ficha));
        expect(clienteService.getClienteById(10L)).andReturn(Optional.of(cliente));
        expect(funcionarioService.getFuncionarioById(20L)).andReturn(Optional.of(funcionario));
        expect(service.salvar(anyObject(FichaAvaliacaoFisica.class)))
                .andThrow(new RegraNegocioException("Altura inválida"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/fichasavaliacaofisica/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Altura inválida"));

        verifyAll();
    }


    @Test
    public void deveExcluirFichaComSucesso() throws Exception {
        expect(service.getFichaAvaliacaoFisicaById(1L)).andReturn(Optional.of(ficha));
        service.excluir(ficha);
        expectLastCall().once();
        replayAll();

        mockMvc.perform(delete("/api/v1/fichasavaliacaofisica/1"))
                .andExpect(status().isNoContent());

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoExcluirIdInexistente() throws Exception {
        expect(service.getFichaAvaliacaoFisicaById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(delete("/api/v1/fichasavaliacaofisica/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Ficha de avaliação fisica não encontrada "));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoExcluirLancarRegraNegocioException() throws Exception {
        expect(service.getFichaAvaliacaoFisicaById(1L)).andReturn(Optional.of(ficha));
        service.excluir(ficha);
        expectLastCall().andThrow(new RegraNegocioException("Não é possível excluir"));
        replayAll();

        mockMvc.perform(delete("/api/v1/fichasavaliacaofisica/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Não é possível excluir"));

        verifyAll();
    }
}