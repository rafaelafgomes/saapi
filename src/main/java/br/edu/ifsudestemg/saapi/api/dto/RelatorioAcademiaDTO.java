package br.edu.ifsudestemg.saapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class RelatorioAcademiaDTO {
    private Integer clientesAtivos;
    private Integer clientesInativos;

    private Integer funcionariosAtivos;
    private Integer funcionariosInativos;

    private Integer faturasPagas;
    private Integer faturasNaoPagas;

    private Integer  equipamentosDisponiveis;
    private Integer  equipamentosIndisponiveis;

    private Integer  aulasAtivas;
    private Integer  aulasInativas;

    private Integer clientesTotais;
    private Integer aulasMes;
    private Integer inadimplentes;

    private Float receitaMes;
    private Float crescimento;

    private List<Float> receitaAnual;
}
