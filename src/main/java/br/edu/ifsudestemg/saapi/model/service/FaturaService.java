package br.edu.ifsudestemg.saapi.model.service;

import br.edu.ifsudestemg.saapi.model.entity.Fatura;
import br.edu.ifsudestemg.saapi.model.repository.FaturaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FaturaService {
    private FaturaRepository repository;

    public FaturaService(FaturaRepository repository) {
        this.repository = repository;
    }

    public List<Fatura> getFatura() {
        return repository.findAll();
    }

    public Optional<Fatura> getFaturaById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Fatura fatura (Fatura fatura) {
        validar(fatura);
        return repository.save(fatura);
    }

    @Transactional
    public void excluir (Fatura fatura) {
        Objects.requireNonNull(fatura.getId());
        repository.delete(fatura);
    }

}
