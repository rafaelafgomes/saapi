package br.edu.ifsudestemg.saapi.service;

import br.edu.ifsudestemg.saapi.api.dto.RelatorioAcademiaDTO;
import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.entity.Fatura;
import br.edu.ifsudestemg.saapi.model.repository.AulaRepository;
import br.edu.ifsudestemg.saapi.model.repository.ClienteRepository;
import br.edu.ifsudestemg.saapi.model.repository.EquipamentoRepository;
import br.edu.ifsudestemg.saapi.model.repository.FaturaRepository;
import br.edu.ifsudestemg.saapi.model.repository.FuncionarioRepository;
import org.easymock.EasyMockExtension;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(EasyMockExtension.class)
class RelatorioAcademiaServiceTest extends EasyMockSupport {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private FaturaRepository faturaRepository;

    @Mock
    private EquipamentoRepository equipamentoRepository;

    @Mock
    private AulaRepository aulaRepository;

    private RelatorioAcademiaService service;

    @BeforeEach
    void setUp() {
        service = new RelatorioAcademiaService(
                clienteRepository,
                funcionarioRepository,
                faturaRepository,
                equipamentoRepository,
                aulaRepository
        );
    }

    @Test
    public void deveGerarRelatorioGeralComSucesso() {
        expect(clienteRepository.countByStatusTrue()).andReturn(10L);
        expect(clienteRepository.countByStatusFalse()).andReturn(2L);

        expect(funcionarioRepository.countByStatusTrue()).andReturn(5L);
        expect(funcionarioRepository.countByStatusFalse()).andReturn(1L);

        expect(faturaRepository.countByDataPagamentoIsNotNull()).andReturn(20L);
        expect(faturaRepository.countByDataPagamentoIsNull()).andReturn(4L);

        expect(equipamentoRepository.countByDisponivelTrue()).andReturn(15L);
        expect(equipamentoRepository.countByDisponivelFalse()).andReturn(3L);

        expect(aulaRepository.countByStatusTrue()).andReturn(8L);
        expect(aulaRepository.countByStatusFalse()).andReturn(0L);

        replayAll();

        RelatorioAcademiaDTO dto = service.gerarRelatorio();

        assertNotNull(dto);
        assertEquals(10, dto.getClientesAtivos());
        assertEquals(2, dto.getClientesInativos());
        assertEquals(5, dto.getFuncionariosAtivos());
        assertEquals(1, dto.getFuncionariosInativos());
        assertEquals(20, dto.getFaturasPagas());
        assertEquals(4, dto.getFaturasNaoPagas());
        assertEquals(15, dto.getEquipamentosDisponiveis());
        assertEquals(3, dto.getEquipamentosIndisponiveis());
        assertEquals(8, dto.getAulasAtivas());
        assertEquals(0, dto.getAulasInativas());
        verifyAll();
    }

    @Test
    public void deveGerarRelatorioMensalComCalculosDeReceitaEInadimplencia() {
        String mes = "2026-10";
        Integer ano = 2026;

        Fatura faturaMesAtual = new Fatura();
        faturaMesAtual.setDataPagamento("2026-10-15");
        faturaMesAtual.setValor(200.0f);

        Fatura faturaMesAnterior = new Fatura();
        faturaMesAnterior.setDataPagamento("2026-09-10");
        faturaMesAnterior.setValor(100.0f);

        List<Fatura> faturasPagas = List.of(faturaMesAtual, faturaMesAnterior);
        expect(faturaRepository.findByDataPagamentoIsNotNull()).andReturn(faturasPagas).atLeastOnce();

        Fatura faturaVencida = new Fatura();
        faturaVencida.setDataVencimento("2026-10-25");
        faturaVencida.setDataPagamento(null);

        List<Fatura> faturasNaoPagas = List.of(faturaVencida);
        expect(faturaRepository.findByDataPagamentoIsNull()).andReturn(faturasNaoPagas);

        Aula aulaNoMes = new Aula();
        aulaNoMes.setDataAula("2026-10-05");

        Aula aulaOutroMes = new Aula();
        aulaOutroMes.setDataAula("2026-11-05");

        expect(aulaRepository.findAll()).andReturn(List.of(aulaNoMes, aulaOutroMes));

        expect(clienteRepository.countByStatusTrue()).andReturn(50L);
        expect(clienteRepository.countByStatusFalse()).andReturn(5L);

        expect(funcionarioRepository.countByStatusTrue()).andReturn(8L);
        expect(funcionarioRepository.countByStatusFalse()).andReturn(1L);

        expect(faturaRepository.countByDataPagamentoIsNotNull()).andReturn(30L);
        expect(faturaRepository.countByDataPagamentoIsNull()).andReturn(10L);

        expect(equipamentoRepository.countByDisponivelTrue()).andReturn(40L);
        expect(equipamentoRepository.countByDisponivelFalse()).andReturn(2L);

        expect(aulaRepository.countByStatusTrue()).andReturn(12L);
        expect(aulaRepository.countByStatusFalse()).andReturn(1L);

        expect(clienteRepository.count()).andReturn(55L);

        replayAll();

        RelatorioAcademiaDTO dto = service.gerarRelatorioMensal(mes, ano);

        assertNotNull(dto);
        assertEquals(55, dto.getClientesTotais());
        assertEquals(1, dto.getAulasMes());
        assertEquals(1, dto.getInadimplentes());
        assertEquals(200.0f, dto.getReceitaMes());

        assertEquals(100.0f, dto.getCrescimento());

        assertNotNull(dto.getReceitaAnual());
        assertEquals(12, dto.getReceitaAnual().size());
        assertEquals(100.0f, dto.getReceitaAnual().get(8));
        assertEquals(200.0f, dto.getReceitaAnual().get(9));
        assertEquals(0.0f, dto.getReceitaAnual().get(0));

        verifyAll();
    }
}