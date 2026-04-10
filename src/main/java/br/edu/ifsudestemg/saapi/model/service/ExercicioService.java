package br.edu.ifsudestemg.saapi.model.service;

import br.edu.ifsudestemg.saapi.model.entity.Exercicio;
import br.edu.ifsudestemg.saapi.model.repository.ExercicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExercicioService {
    private ExercicioRepository repository;

    public ExercicioService(ExercicioRepository repository) {
        this.repository = repository;
    }

    public List<Exercicio> getExercicio() {
        return repository.findAll();
    }

    public Optional<Exercicio> getExercicioById(Long id) {
        return repository.findById(id);
    }
}
