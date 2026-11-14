package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.FuncionarioDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Cargo;
import br.edu.ifsudestemg.saapi.model.entity.Funcionario;
import br.edu.ifsudestemg.saapi.service.CargoService;
import br.edu.ifsudestemg.saapi.service.FuncionarioService;
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
class FuncionarioControllerTest extends EasyMockSupport {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private FuncionarioService service;

    @Mock
    private PlanoService planoService;

    @Mock
    private CargoService cargoService;

    private FuncionarioController controller;
    private Funcionario funcionario;
    private Cargo cargo;
    private FuncionarioDTO dto;

    @BeforeEach
    void setUp() {
        controller = new FuncionarioController(service, planoService, cargoService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
        objectMapper = new ObjectMapper();

        cargo = new Cargo();
        cargo.setId(5L);
        cargo.setNome("Personal Trainer");

        funcionario = new Funcionario();
        funcionario.setId(1L);
        funcionario.setNome("Roberto Alves");
        funcionario.setCpf("12345678901");
        funcionario.setStatus(true);
        funcionario.setCargo(cargo);

        dto = FuncionarioDTO.create(funcionario);
    }


    @Test
    public void deveRetornarListaDeFuncionarios() throws Exception {
        expect(service.getFuncionario()).andReturn(List.of(funcionario));
        replayAll();

        mockMvc.perform(get("/api/v1/funcionarios")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Roberto Alves"));

        verifyAll();
    }


    @Test
    public void deveRetornarFuncionarioPorIdQuandoExistir() throws Exception {
        expect(service.getFuncionarioById(1L)).andReturn(Optional.of(funcionario));
        replayAll();

        mockMvc.perform(get("/api/v1/funcionarios/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Roberto Alves"));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoBuscarIdInexistente() throws Exception {
        expect(service.getFuncionarioById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(get("/api/v1/funcionarios/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Funcionario(a) não encontrado(a)"));

        verifyAll();
    }


    @Test
    public void deveRetornarFuncionariosAtivos() throws Exception {
        expect(service.getFuncionariosAtivos()).andReturn(List.of(funcionario));
        replayAll();

        mockMvc.perform(get("/api/v1/funcionarios/ativos")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Roberto Alves"));

        verifyAll();
    }


    @Test
    public void deveSalvarFuncionarioComCargoExistenteComSucesso() throws Exception {
        expect(cargoService.getCargoById(5L)).andReturn(Optional.of(cargo));
        expect(service.salvar(anyObject(Funcionario.class))).andReturn(funcionario);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Roberto Alves"));

        verifyAll();
    }

    @Test
    public void deveSalvarFuncionarioQuandoCargoNaoExistir() throws Exception {
        dto.setIdCargo(99L);
        expect(cargoService.getCargoById(99L)).andReturn(Optional.empty());
        expect(service.salvar(anyObject(Funcionario.class))).andReturn(funcionario);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveSalvarFuncionarioSemCargoInformado() throws Exception {
        dto.setIdCargo(null);
        expect(service.salvar(anyObject(Funcionario.class))).andReturn(funcionario);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoSalvarLancarRegraNegocioException() throws Exception {
        expect(cargoService.getCargoById(5L)).andReturn(Optional.of(cargo));
        expect(service.salvar(anyObject(Funcionario.class)))
                .andThrow(new RegraNegocioException("CPF inválido"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("CPF inválido"));

        verifyAll();
    }


    @Test
    public void deveAtualizarFuncionarioComSucesso() throws Exception {
        expect(service.getFuncionarioById(1L)).andReturn(Optional.of(funcionario));
        expect(cargoService.getCargoById(5L)).andReturn(Optional.of(cargo));
        expect(service.salvar(anyObject(Funcionario.class))).andReturn(funcionario);
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/funcionarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Roberto Alves"));

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoAtualizarIdInexistente() throws Exception {
        expect(service.getFuncionarioById(99L)).andReturn(Optional.empty());
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/funcionarios/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Funcionario (a) não encontrado(a)"));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoAtualizarLancarRegraNegocioException() throws Exception {
        expect(service.getFuncionarioById(1L)).andReturn(Optional.of(funcionario));
        expect(cargoService.getCargoById(5L)).andReturn(Optional.of(cargo));
        expect(service.salvar(anyObject(Funcionario.class)))
                .andThrow(new RegraNegocioException("Nome inválido"));
        replayAll();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/funcionarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Nome inválido"));

        verifyAll();
    }


    @Test
    public void deveExcluirFuncionarioComSucesso() throws Exception {
        expect(service.getFuncionarioById(1L)).andReturn(Optional.of(funcionario));
        service.excluir(funcionario);
        expectLastCall().once();
        replayAll();

        mockMvc.perform(delete("/api/v1/funcionarios/1"))
                .andExpect(status().isNoContent());

        verifyAll();
    }

    @Test
    public void deveRetornarNotFoundQuandoExcluirIdInexistente() throws Exception {
        expect(service.getFuncionarioById(99L)).andReturn(Optional.empty());
        replayAll();

        mockMvc.perform(delete("/api/v1/funcionarios/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Funcionario não encontrado (a) "));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoExcluirLancarRegraNegocioException() throws Exception {
        expect(service.getFuncionarioById(1L)).andReturn(Optional.of(funcionario));
        service.excluir(funcionario);
        expectLastCall().andThrow(new RegraNegocioException("Não é possível excluir"));
        replayAll();

        mockMvc.perform(delete("/api/v1/funcionarios/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Não é possível excluir"));

        verifyAll();
    }
}