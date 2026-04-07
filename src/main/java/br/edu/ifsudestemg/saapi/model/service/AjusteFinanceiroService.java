package br.edu.ifsudestemg.saapi.model.service;

import br.edu.ifsudestemg.saapi.model.entity.AjusteFinanceiro;
import br.edu.ifsudestemg.saapi.model.repository.AjusteFinanceiroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AjusteFinanceiroService {

    private AjusteFinanceiroRepository repository;

    public AjusteFinanceiroService(AjusteFinanceiroRepository repository) {
        this.repository = repository;
    }

    public List<AjusteFinanceiro> getAjusteFinanceiros() {
        return repository.findAll();
    }

    public Optional<AjusteFinanceiro> getAjusteFinanceiroById(Long id) {
        return repository.findById(id);
    }
}
