package br.edu.ifsudestemg.saapi.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.AjusteFinanceiro;
import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.Fatura;
import br.edu.ifsudestemg.saapi.model.repository.AjusteFinanceiroRepository;
import br.edu.ifsudestemg.saapi.model.repository.FaturaRepository;
import br.edu.ifsudestemg.saapi.model.repository.ListaAjusteFinanceiroFaturaRepository;
import br.edu.ifsudestemg.saapi.model.repository.ListaExercicioTreinoRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FaturaService {
    private final ClienteService clienteService;
    private FaturaRepository repository;
    private final ListaAjusteFinanceiroFaturaRepository listaAjusteFinanceiroFaturaRepository;
    private final AjusteFinanceiroRepository ajusteFinanceiroRepository;

    public FaturaService(FaturaRepository repository, ListaAjusteFinanceiroFaturaRepository listaAjusteFinanceiroFaturaRepository, AjusteFinanceiroRepository ajusteFinanceiroRepository, ClienteService clienteService) {
        this.repository = repository;
        this.listaAjusteFinanceiroFaturaRepository = listaAjusteFinanceiroFaturaRepository;
        this.ajusteFinanceiroRepository = ajusteFinanceiroRepository;
        this.clienteService = clienteService;
    }

    public List<Fatura> getFatura() {
        return repository.findAll();
    }

    public Optional<Fatura> getFaturaById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Fatura salvar (Fatura fatura) {
        validar(fatura);
        return repository.save(fatura);
    }

    @Transactional
    public void excluir (Fatura fatura) {
        Objects.requireNonNull(fatura.getId());
        listaAjusteFinanceiroFaturaRepository.deleteAllByFatura(fatura);
        repository.delete(fatura);
    }

    public void validar (Fatura fatura){
        if (fatura.getDataVencimento() == null || fatura.getDataVencimento().trim().equals("")) {
            throw new RegraNegocioException("Data inválida");
        }

        if (fatura.getValor() == null || fatura.getValor() == 0) {
            throw new RegraNegocioException("Valor inválido");
        }

        if (fatura.getCliente() == null || fatura.getCliente().getId() == null || fatura.getCliente().getId() == 0) {
            throw new RegraNegocioException("Cliente inválido");
        }

        if (fatura.getFormaPagamento() == null || fatura.getFormaPagamento().getId() == null || fatura.getFormaPagamento().getId() == 0) {
            throw new RegraNegocioException("Forma de pagamento inválido");
        }
    }

    public Float calcularValorFatura(Long idCliente, List<Long> idsAjustes) {
        if (idCliente == null || idCliente <= 0) {
            throw new RegraNegocioException("ID do cliente inválido");
        }
        Optional<Cliente> clienteOptional = clienteService.getClienteById(idCliente);
        if(!clienteOptional.isPresent()){
            throw new RegraNegocioException("Cliente não encontrado");
        }
        Cliente cliente = clienteOptional.get();
        if(cliente.getPlano() == null || cliente.getPlano().getValor() == null) {
            throw new RegraNegocioException("Cliente não possui um plano válido associado");
        }
        Float total = cliente.getPlano().getValor();
        if(idsAjustes != null && !idsAjustes.isEmpty()) {
            for(Long idAjuste: idsAjustes){
                if(idAjuste == null){
                    continue;
                }
                Optional<AjusteFinanceiro> ajusteOptional = ajusteFinanceiroRepository.findById(idAjuste);
                if(!ajusteOptional.isPresent()){
                    continue;
                }
                AjusteFinanceiro ajuste = ajusteOptional.get();
                if("DESCONTO".equalsIgnoreCase(ajuste.getTipo())){
                    total -= ajuste.getValor();
                }
                else
                    if ("ACRESCIMO".equalsIgnoreCase(ajuste.getTipo())){
                        total += ajuste.getValor();
                   }
            }
        }
        if (total < 0) {
            total = 0.0f;
        }
        return total;
    }

}
