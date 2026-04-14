package br.edu.ifsudestemg.saapi.model.service;


import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.entity.Plano;
import br.edu.ifsudestemg.saapi.model.repository.PlanoRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
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

    @Transactional
    public Plano salvar (Plano plano) {
        validar(plano);
        return repository.save(plano);
    }

    @Transactional
    public void excluir (Plano plano) {
        Objects.requireNonNull(plano.getId());
        repository.delete(plano);
    }

}
