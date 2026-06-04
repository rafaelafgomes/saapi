package br.edu.ifsudestemg.saapi.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Exercicio;
import br.edu.ifsudestemg.saapi.model.repository.ExercicioRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
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

    @Transactional
    public Exercicio salvar (Exercicio exercicio) {
        validar(exercicio);
        return repository.save(exercicio);
    }

    @Transactional
    public void excluir (Exercicio exercicio) {
        Objects.requireNonNull(exercicio.getId());
        repository.delete(exercicio);
    }

    public void validar (Exercicio exercicio){
        if (exercicio.getNome() == null || exercicio.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }

    }
}
