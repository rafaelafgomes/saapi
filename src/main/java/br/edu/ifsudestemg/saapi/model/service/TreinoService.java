package br.edu.ifsudestemg.saapi.model.service;

import br.edu.ifsudestemg.saapi.model.entity.Treino;
import br.edu.ifsudestemg.saapi.model.repository.TreinoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TreinoService {
    private TreinoRepository repository;

    public TreinoService(TreinoRepository repository) {
        this.repository = repository;
    }

    public List<Treino> getTreino() {
        return repository.findAll();
    }

    public Optional<Treino> getTreinoById(Long id) {
        return repository.findById(id);
    }
}
