package br.edu.ifsudestemg.saapi.model.service;

import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.repository.AulaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AulaService {
    private AulaRepository repository;

    public AulaService(AulaRepository repository) {
        this.repository = repository;
    }

    public List<Aula> getAula() {
        return repository.findAll();
    }

    public Optional<Aula> getAulaById(Long id) {
        return repository.findById(id);
    }
}
