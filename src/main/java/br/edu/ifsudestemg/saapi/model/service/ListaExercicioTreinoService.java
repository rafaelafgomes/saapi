package br.edu.ifsudestemg.saapi.model.service;

import br.edu.ifsudestemg.saapi.model.entity.ListaExercicioTreino;
import br.edu.ifsudestemg.saapi.model.repository.ListaExercicioTreinoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ListaExercicioTreinoService {
    private ListaExercicioTreinoRepository repository;

    public ListaExercicioTreinoService(ListaExercicioTreinoRepository repository) {
        this.repository = repository;
    }

    public List<ListaExercicioTreino> getListaExercicioTreino() {
        return repository.findAll();
    }

    public Optional<ListaExercicioTreino> getListaExercicioTreinoById(Long id) {
        return repository.findById(id);
    }
}
