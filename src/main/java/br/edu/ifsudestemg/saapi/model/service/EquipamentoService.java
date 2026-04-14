package br.edu.ifsudestemg.saapi.model.service;

import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.entity.Equipamento;
import br.edu.ifsudestemg.saapi.model.repository.EquipamentoRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EquipamentoService {
    private EquipamentoRepository repository;

    public EquipamentoService(EquipamentoRepository repository) {
        this.repository = repository;
    }

    public List<Equipamento> getEquipamento() {
        return repository.findAll();
    }

    public Optional<Equipamento> getEquipamentoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Equipamento salvar (Equipamento equipamento) {
        validar(equipamento);
        return repository.save(equipamento);
    }

    @Transactional
    public void excluir (Equipamento equipamento) {
        Objects.requireNonNull(equipamento.getId());
        repository.delete(equipamento);
    }

}
