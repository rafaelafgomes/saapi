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

    @Test
    public void deveGarantirCoberturaDeBranchesComDadosInvalidosENulos() {
        String mes = "2026-10";
        Integer ano = 2026;

        // 1. Faturas pagas incluindo casos com dataPagamento nula e outro ano (2025)
        Fatura faturaSemDataPagamento = new Fatura();
        faturaSemDataPagamento.setDataPagamento(null);

        Fatura faturaOutroAno = new Fatura();
        faturaOutroAno.setDataPagamento("2025-10-10");
        faturaOutroAno.setValor(50.0f);

        List<Fatura> faturasPagas = List.of(faturaSemDataPagamento, faturaOutroAno);
        expect(faturaRepository.findByDataPagamentoIsNotNull()).andReturn(faturasPagas).atLeastOnce();

        // 2. Faturas não pagas testando: vencimento nulo, vencimento curto, vencimento no ultimo dia (isEqual) e vencimento no mes seguinte (isAfter)
        Fatura faturaVencimentoNulo = new Fatura();
        faturaVencimentoNulo.setDataVencimento(null);

        Fatura faturaVencimentoCurto = new Fatura();
        faturaVencimentoCurto.setDataVencimento("2026-10");

        Fatura faturaVencimentoUltimoDia = new Fatura();
        faturaVencimentoUltimoDia.setDataVencimento("2026-10-31"); // isEqual(fimMes) -> true
        faturaVencimentoUltimoDia.setDataPagamento(null);

        Fatura faturaVencimentoMesSeguinte = new Fatura();
        faturaVencimentoMesSeguinte.setDataVencimento("2026-11-05"); // isAfter(fimMes) -> false

        Fatura faturaComPagamentoPreenchido = new Fatura();
        faturaComPagamentoPreenchido.setDataVencimento("2026-10-10");
        faturaComPagamentoPreenchido.setDataPagamento("2026-10-10"); // f.getDataPagamento() == null -> false

        List<Fatura> faturasNaoPagas = List.of(
                faturaVencimentoNulo,
                faturaVencimentoCurto,
                faturaVencimentoUltimoDia,
                faturaVencimentoMesSeguinte,
                faturaComPagamentoPreenchido
        );
        expect(faturaRepository.findByDataPagamentoIsNull()).andReturn(faturasNaoPagas);

        // 3. Aulas com dataAula nula
        Aula aulaSemData = new Aula();
        aulaSemData.setDataAula(null);

        expect(aulaRepository.findAll()).andReturn(List.of(aulaSemData));

        // Mocks padrão de contagem
        expect(clienteRepository.countByStatusTrue()).andReturn(10L);
        expect(clienteRepository.countByStatusFalse()).andReturn(1L);

        expect(funcionarioRepository.countByStatusTrue()).andReturn(5L);
        expect(funcionarioRepository.countByStatusFalse()).andReturn(1L);

        expect(faturaRepository.countByDataPagamentoIsNotNull()).andReturn(10L);
        expect(faturaRepository.countByDataPagamentoIsNull()).andReturn(5L);

        expect(equipamentoRepository.countByDisponivelTrue()).andReturn(8L);
        expect(equipamentoRepository.countByDisponivelFalse()).andReturn(2L);

        expect(aulaRepository.countByStatusTrue()).andReturn(3L);
        expect(aulaRepository.countByStatusFalse()).andReturn(0L);

        expect(clienteRepository.count()).andReturn(11L);

        replayAll();

        RelatorioAcademiaDTO dto = service.gerarRelatorioMensal(mes, ano);

        assertNotNull(dto);
        // receitaMesAnterior foi 0, então crescimento deve ser 0f (cobre o else do receitaMesAnterior > 0)
        assertEquals(0.0f, dto.getCrescimento());
        // 1 inadimplente capturado na borda do ultimo dia do mes
        assertEquals(1, dto.getInadimplentes());
        assertEquals(0, dto.getAulasMes());

        verifyAll();
    }
}