package br.edu.ifsudestemg.saapi.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.ListaExercicioTreino;
import br.edu.ifsudestemg.saapi.model.repository.ListaExercicioTreinoRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
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

    @Transactional
    public ListaExercicioTreino salvar (ListaExercicioTreino listaExercicioTreino) {
        validar(listaExercicioTreino);
        return repository.save(listaExercicioTreino);
    }

    @Transactional
    public void excluir (ListaExercicioTreino listaExercicioTreino) {
        Objects.requireNonNull(listaExercicioTreino.getId());
        repository.delete(listaExercicioTreino);
    }

    public void validar (ListaExercicioTreino listaExercicioTreino) {
        if (listaExercicioTreino.getSerie() == null || listaExercicioTreino.getSerie() == 0) {
            throw new RegraNegocioException("Série inválido");
        }

        if (listaExercicioTreino.getCarga() == null || listaExercicioTreino.getCarga() == 0) {
            throw new RegraNegocioException("Carga inválido");
        }

        if (listaExercicioTreino.getRepeticao() == null || listaExercicioTreino.getRepeticao() == 0) {
            throw new RegraNegocioException("Repetição inválido");
        }

        if(listaExercicioTreino.getExercicio() == null || listaExercicioTreino.getExercicio().getId() == null || listaExercicioTreino.getExercicio().getId() == 0) {
            throw new RegraNegocioException("Exercicio inválido");
        }

        if(listaExercicioTreino.getTreino() == null || listaExercicioTreino.getTreino().getId() == null || listaExercicioTreino.getTreino().getId() == 0) {
            throw new RegraNegocioException("Treino inválido");
        }
    }
}
