package br.edu.ifsudestemg.saapi.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.Treino;
import br.edu.ifsudestemg.saapi.model.repository.ListaExercicioTreinoRepository;
import br.edu.ifsudestemg.saapi.model.repository.TreinoRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TreinoService {
    private TreinoRepository repository;
    private final ListaExercicioTreinoRepository listaExercicioTreinoRepository;

    public TreinoService(TreinoRepository repository, ListaExercicioTreinoRepository listaExercicioTreinoRepository) {
        this.repository = repository;
        this.listaExercicioTreinoRepository = listaExercicioTreinoRepository;
    }

    public List<Treino> getTreino() {
        return repository.findAll();
    }

    public Optional<Treino> getTreinoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Treino salvar(Treino treino) {
        validar(treino);
        return repository.save(treino);
    }

    @Transactional
    public void excluir(Treino treino) {
        Objects.requireNonNull(treino.getId());
        listaExercicioTreinoRepository.deleteAllByTreino(treino);
        repository.delete(treino);
    }

    public void validar(Treino treino) {

        if (treino.getNome() == null || treino.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }

        if (treino.getData() == null || treino.getData().trim().equals("")) {
            throw new RegraNegocioException("Data inválida");
        }

        if(treino.getCliente() == null || treino.getCliente().getId() == null || treino.getCliente().getId() == 0) {
            throw new RegraNegocioException("Cliente inválido");
        }

        if(treino.getFuncionario() == null || treino.getFuncionario().getId() == null || treino.getFuncionario().getId() == 0) {
            throw new RegraNegocioException("Funcionário inválido");
        }

    }
}
