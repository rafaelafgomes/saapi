package br.edu.ifsudestemg.saapi.model.service;

import br.edu.ifsudestemg.saapi.model.entity.AjusteFinanceiro;
import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.repository.AulaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
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

    @Transactional
    public Aula salvar (Aula aula) {
        validar(aula);
        return repository.save(aula);
    }

    @Transactional
    public void excluir (Aula aula) {
        Objects.requireNonNull(aula.getId());
        repository.delete(aula);
    }
}
