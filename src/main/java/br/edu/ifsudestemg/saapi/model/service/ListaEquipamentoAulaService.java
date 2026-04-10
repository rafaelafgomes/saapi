package br.edu.ifsudestemg.saapi.model.service;

import br.edu.ifsudestemg.saapi.model.entity.ListaEquipamentoAula;
import br.edu.ifsudestemg.saapi.model.repository.ListaEquipamentoAulaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ListaEquipamentoAulaService {
    private ListaEquipamentoAulaRepository repository;

    public ListaEquipamentoAulaService(ListaEquipamentoAulaRepository repository) {
        this.repository = repository;
    }

    public List<ListaEquipamentoAula> getListaEquipamentoAula() {
        return repository.findAll();
    }

    public Optional<ListaEquipamentoAula> getListaEquipamentoAulaById(Long id) {
        return repository.findById(id);
    }

}
