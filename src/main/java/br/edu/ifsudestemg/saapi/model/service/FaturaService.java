package br.edu.ifsudestemg.saapi.model.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Fatura;
import br.edu.ifsudestemg.saapi.model.repository.FaturaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FaturaService {
    private FaturaRepository repository;

    public FaturaService(FaturaRepository repository) {
        this.repository = repository;
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
        repository.delete(fatura);
    }

    public void validar (Fatura fatura){
        if (fatura.getDataVencimento() == null || fatura.getDataVencimento().trim().equals("")) {
            throw new RegraNegocioException("Data inválida");
        }

        if (fatura.getDataPagamento() == null || fatura.getDataPagamento().trim().equals("")) {
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

}
