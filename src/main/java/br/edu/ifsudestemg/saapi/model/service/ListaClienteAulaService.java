package br.edu.ifsudestemg.saapi.model.service;

import br.edu.ifsudestemg.saapi.model.entity.ListaClienteAula;
import br.edu.ifsudestemg.saapi.model.repository.ListaClienteAulaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ListaClienteAulaService {
    private ListaClienteAulaRepository repository;

    public ListaClienteAulaService(ListaClienteAulaRepository repository) {
        this.repository = repository;
    }

    public List<ListaClienteAula> getListaClienteAula() {
        return repository.findAll();
    }

    public Optional<ListaClienteAula> getListaClienteAulaById(Long id) {
        return repository.findById(id);
    }

}
