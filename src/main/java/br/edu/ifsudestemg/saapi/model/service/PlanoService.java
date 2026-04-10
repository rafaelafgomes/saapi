package br.edu.ifsudestemg.saapi.model.service;


import br.edu.ifsudestemg.saapi.model.entity.Plano;
import br.edu.ifsudestemg.saapi.model.repository.PlanoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanoService {
    private PlanoRepository repository;

    public PlanoService(PlanoRepository repository) {
        this.repository = repository;
    }

    public List<Plano> getPlano() {
        return repository.findAll();
    }

    public Optional<Plano> getPlanoById(Long id) {
        return repository.findById(id);
    }

}
