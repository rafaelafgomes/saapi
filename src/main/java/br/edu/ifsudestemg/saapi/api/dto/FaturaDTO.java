package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.Fatura;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaturaDTO {

    private Long id;
    private String dataVencimento;
    private String dataPagamento;
    private Float valor;
    private Long idCliente;
    private String nomeCliente;
    private String planoCliente;
    private Long idFormaPagamento;
    private String nomeFormaPagamento;
    private List<Long> idsAjustesFinanceiros;

    public static FaturaDTO create (Fatura fatura) {
        ModelMapper modelMapper = new ModelMapper();
        FaturaDTO dto = modelMapper.map(fatura, FaturaDTO.class);
        dto.nomeCliente = fatura.getCliente().getNome();
        dto.planoCliente = fatura.getCliente().getPlano().getNome();
        dto.nomeFormaPagamento = fatura.getFormaPagamento().getNome();
        return dto;
    }
}
