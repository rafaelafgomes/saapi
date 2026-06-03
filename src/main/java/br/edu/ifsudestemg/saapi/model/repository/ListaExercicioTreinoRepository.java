package br.edu.ifsudestemg.saapi.model.repository;

import br.edu.ifsudestemg.saapi.model.entity.ListaExercicioTreino;
import br.edu.ifsudestemg.saapi.model.entity.Treino;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListaExercicioTreinoRepository extends JpaRepository<ListaExercicioTreino, Long> {
    void deleteAllByTreino(Treino treino);
    List<ListaExercicioTreino> findAllByTreino(Treino treino);

}