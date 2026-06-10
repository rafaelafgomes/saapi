package br.edu.ifsudestemg.saapi.service;

import br.edu.ifsudestemg.saapi.api.dto.RelatorioAcademiaDTO;
import br.edu.ifsudestemg.saapi.model.entity.Fatura;
import br.edu.ifsudestemg.saapi.model.repository.AulaRepository;
import br.edu.ifsudestemg.saapi.model.repository.ClienteRepository;
import br.edu.ifsudestemg.saapi.model.repository.EquipamentoRepository;
import br.edu.ifsudestemg.saapi.model.repository.FaturaRepository;
import br.edu.ifsudestemg.saapi.model.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class RelatorioAcademiaService {
    private final ClienteRepository clienteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final FaturaRepository faturaRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final AulaRepository aulaRepository;

    public RelatorioAcademiaDTO gerarRelatorio() {
        return new RelatorioAcademiaDTO(
                clienteRepository.countByStatusTrue().intValue(),
                clienteRepository.countByStatusFalse().intValue(),

                funcionarioRepository.countByStatusTrue().intValue(),
                funcionarioRepository.countByStatusFalse().intValue(),

                faturaRepository.countByDataPagamentoIsNotNull().intValue(),
                faturaRepository.countByDataPagamentoIsNull().intValue(),

                equipamentoRepository.countByDisponivelTrue().intValue(),
                equipamentoRepository.countByDisponivelFalse().intValue(),

                aulaRepository.countByStatusTrue().intValue(),
                aulaRepository.countByStatusFalse().intValue(),

                0, 0, 0, 0f, 0f, null
        );
    }

    public RelatorioAcademiaDTO gerarRelatorioMensal(String mes, Integer ano) {

        String mesAnterior = LocalDate.parse(mes + "-01").minusMonths(1).toString().substring(0,7);

        List<Fatura> faturasPagas = faturaRepository.findByDataPagamentoIsNotNull();
        float receitaMes = 0f;
        float receitaMesAnterior = 0f;
        for (Fatura f : faturasPagas) {
            if (f.getDataPagamento() != null) {
                String dataPagamento = f.getDataPagamento().substring(0,7);
                if (dataPagamento.equals(mes)) {
                    receitaMes += f.getValor();
                }
                if (dataPagamento.equals(mesAnterior)) {
                    receitaMesAnterior += f.getValor();
                }
            }
        }

        List<Fatura> naoPagas = faturaRepository.findByDataPagamentoIsNull();
        int inadimplentes = 0;
        YearMonth ym = YearMonth.parse(mes);
        LocalDate fimMes = ym.atEndOfMonth();
        for (Fatura f : naoPagas) {
            if (f.getDataVencimento() != null && f.getDataVencimento().length() >= 10) {
                LocalDate vencimento = LocalDate.parse(f.getDataVencimento().substring(0,10));
                if (vencimento.isBefore(fimMes) || vencimento.isEqual(fimMes)) {
                    if (f.getDataPagamento() == null) {
                        inadimplentes++;
                    }
                }
            }
        }

        List<Float> receitaAnual = new ArrayList<>();
        for (int mesAtual = 1; mesAtual <= 12; mesAtual++) {
            float totalMes = 0f;
            for (Fatura f : faturasPagas) {
                if (f.getDataPagamento() != null) {
                    LocalDate data = LocalDate.parse(f.getDataPagamento().substring(0, 10));
                    if (data.getYear() == ano
                            && data.getMonthValue() == mesAtual) {
                        totalMes += f.getValor();
                    }
                }
            }
            receitaAnual.add(totalMes);
        }

        int aulasMes = (int) aulaRepository.findAll().stream().filter(a -> a.getDataAula() != null && a.getDataAula().substring(0,7).equals(mes)).count();
        float crescimento = 0f;
        if (receitaMesAnterior > 0) {
            crescimento = ((receitaMes - receitaMesAnterior)/ receitaMesAnterior) * 100;
        }

        return new RelatorioAcademiaDTO(
                clienteRepository.countByStatusTrue().intValue(),
                clienteRepository.countByStatusFalse().intValue(),

                funcionarioRepository.countByStatusTrue().intValue(),
                funcionarioRepository.countByStatusFalse().intValue(),

                faturaRepository.countByDataPagamentoIsNotNull().intValue(),
                faturaRepository.countByDataPagamentoIsNull().intValue(),

                equipamentoRepository.countByDisponivelTrue().intValue(),
                equipamentoRepository.countByDisponivelFalse().intValue(),

                aulaRepository.countByStatusTrue().intValue(),
                aulaRepository.countByStatusFalse().intValue(),

                (int) clienteRepository.count(),
                aulasMes, inadimplentes, receitaMes, crescimento,
                receitaAnual
        );
    }
}
