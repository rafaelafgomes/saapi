package br.edu.ifsudestemg.saapi.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.AjusteFinanceiro;
import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.repository.AjusteFinanceiroRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
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

    @Transactional
    public AjusteFinanceiro salvar (AjusteFinanceiro ajusteFinanceiro) {
        validar(ajusteFinanceiro);
        return repository.save(ajusteFinanceiro);
    }

    @Transactional
    public void excluir (AjusteFinanceiro ajusteFinanceiro) {
        Objects.requireNonNull(ajusteFinanceiro.getId());
        repository.delete(ajusteFinanceiro);
    }

    public void validar(AjusteFinanceiro ajusteFinanceiro) {

        if (ajusteFinanceiro.getNome() == null || ajusteFinanceiro.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }

        if (ajusteFinanceiro.getTipo() == null || ajusteFinanceiro.getTipo().trim().equals("")) {
            throw new RegraNegocioException("Tipo inválido");
        }

        if (ajusteFinanceiro.getUnidade() == null || ajusteFinanceiro.getUnidade().trim().equals("")) {
            throw new RegraNegocioException("Unidade inválida");
        }

        if (ajusteFinanceiro.getValor() == null || ajusteFinanceiro.getValor() == 0) {
            throw new RegraNegocioException("Valor inválido");
        }

    }
}
