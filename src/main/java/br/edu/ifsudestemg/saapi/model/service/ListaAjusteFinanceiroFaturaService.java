package br.edu.ifsudestemg.saapi.model.service;

import br.edu.ifsudestemg.saapi.model.entity.ListaAjusteFinanceiroFatura;
import br.edu.ifsudestemg.saapi.model.repository.ListaAjusteFinanceiroFaturaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public Optional<ListaAjusteFinanceiroFatura> getListaAjusteFinanceiroFaturaById(Long id) {
        return repository.findById(id);
    }

}
