package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.RelatorioAcademiaDTO;
import br.edu.ifsudestemg.saapi.service.RelatorioAcademiaService;
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

import static org.easymock.EasyMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(EasyMockExtension.class)
class RelatorioAcademiaControllerTest extends EasyMockSupport {

    private MockMvc mockMvc;

    @Mock
    private RelatorioAcademiaService service;

    private RelatorioAcademiaController controller;
    private RelatorioAcademiaDTO relatorioDTO;

    @BeforeEach
    void setUp() {
        controller = new RelatorioAcademiaController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();

        relatorioDTO = new RelatorioAcademiaDTO();
    }

    // --- GET ---

    @Test
    public void deveGerarRelatorioGeralComSucesso() throws Exception {
        expect(service.gerarRelatorio()).andReturn(relatorioDTO);
        replayAll();

        mockMvc.perform(get("/api/v1/relatorioacademia")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verifyAll();
    }

    // --- GET /mensal ---

    @Test
    public void deveGerarRelatorioMensalComSucesso() throws Exception {
        expect(service.gerarRelatorioMensal("11", 2026)).andReturn(relatorioDTO);
        replayAll();

        mockMvc.perform(get("/api/v1/relatorioacademia/mensal")
                        .param("mes", "11")
                        .param("ano", "2026")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verifyAll();
    }

    @Test
    public void deveRetornarBadRequestQuandoFaltarParametroNoRelatorioMensal() throws Exception {
        mockMvc.perform(get("/api/v1/relatorioacademia/mensal")
                        .param("mes", "11")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}