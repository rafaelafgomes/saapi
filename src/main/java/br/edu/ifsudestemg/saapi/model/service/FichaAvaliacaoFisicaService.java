package br.edu.ifsudestemg.saapi.model.service;

import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.entity.FichaAvaliacaoFisica;
import br.edu.ifsudestemg.saapi.model.repository.FichaAvaliacaoFisicaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FichaAvaliacaoFisicaService {
    private FichaAvaliacaoFisicaRepository repository;

    public FichaAvaliacaoFisicaService(FichaAvaliacaoFisicaRepository repository) {
        this.repository = repository;
    }

    public List<FichaAvaliacaoFisica> getFichaAvaliacaoFisica() {
        return repository.findAll();
    }

    public Optional<FichaAvaliacaoFisica> getFichaAvaliacaoFisicaById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public FichaAvaliacaoFisica salvar (FichaAvaliacaoFisica fichaAvaliacaoFisica) {
        validar(fichaAvaliacaoFisica);
        return repository.save(fichaAvaliacaoFisica);
    }

    @Transactional
    public void excluir (FichaAvaliacaoFisica fichaAvaliacaoFisica) {
        Objects.requireNonNull(fichaAvaliacaoFisica.getId());
        repository.delete(fichaAvaliacaoFisica);
    }

}
