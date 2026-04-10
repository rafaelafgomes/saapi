package br.edu.ifsudestemg.saapi.model.service;

import br.edu.ifsudestemg.saapi.model.entity.FichaAvaliacaoFisica;
import br.edu.ifsudestemg.saapi.model.repository.FichaAvaliacaoFisicaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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

}
