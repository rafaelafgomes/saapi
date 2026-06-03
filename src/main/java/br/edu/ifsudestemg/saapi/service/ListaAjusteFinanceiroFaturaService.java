package br.edu.ifsudestemg.saapi.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.entity.Fatura;
import br.edu.ifsudestemg.saapi.model.entity.ListaAjusteFinanceiroFatura;
import br.edu.ifsudestemg.saapi.model.repository.ListaAjusteFinanceiroFaturaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ListaAjusteFinanceiroFaturaService {
    private ListaAjusteFinanceiroFaturaRepository repository;

    public ListaAjusteFinanceiroFaturaService(ListaAjusteFinanceiroFaturaRepository repository) {
        this.repository = repository;
    }

    public List<ListaAjusteFinanceiroFatura> getListaAjusteFinanceiroFatura() {
        return repository.findAll();
    }

    public List<ListaAjusteFinanceiroFatura> getListaAjusteFinanceiroFaturaByFatura(Fatura fatura) {
        return repository.findAllByFatura(fatura);
    }

    public Optional<ListaAjusteFinanceiroFatura> getListaAjusteFinanceiroFaturaById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public ListaAjusteFinanceiroFatura salvar (ListaAjusteFinanceiroFatura listaAjusteFinanceiroFatura) {
        validar(listaAjusteFinanceiroFatura);
        return repository.save(listaAjusteFinanceiroFatura);
    }

    @Transactional
    public void excluir (ListaAjusteFinanceiroFatura listaAjusteFinanceiroFatura) {
        Objects.requireNonNull(listaAjusteFinanceiroFatura.getId());
        repository.delete(listaAjusteFinanceiroFatura);
    }

    public void validar (ListaAjusteFinanceiroFatura listaAjusteFinanceiroFatura){

        if(listaAjusteFinanceiroFatura.getAjusteFinanceiro() == null || listaAjusteFinanceiroFatura.getAjusteFinanceiro().getId() == null || listaAjusteFinanceiroFatura.getAjusteFinanceiro().getId() == 0) {
            throw new RegraNegocioException("Ajuste financeiro inválido");
        }

        if(listaAjusteFinanceiroFatura.getFatura() == null || listaAjusteFinanceiroFatura.getFatura().getId() == null || listaAjusteFinanceiroFatura.getFatura().getId() == 0) {
            throw new RegraNegocioException("Fatura inválida");
        }

    }

}
