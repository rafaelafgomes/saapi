package br.edu.ifsudestemg.saapi.model.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.entity.Cliente;
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
    public Equipamento salvar(Equipamento equipamento) {
        validar(equipamento);
        return repository.save(equipamento);
    }

    @Transactional
    public void excluir(Equipamento equipamento) {
        Objects.requireNonNull(equipamento.getId());
        repository.delete(equipamento);
    }

    public void validar(Equipamento equipamento) {

        if (equipamento.getNome() == null || equipamento.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }

        if (equipamento.getModelo() == null || equipamento.getModelo().trim().equals("")) {
            throw new RegraNegocioException("Modelo inválido");
        }

        if (equipamento.getUltManutencao() == null || equipamento.getUltManutencao().trim().equals("")) {
            throw new RegraNegocioException("Última Manutenção inválida");
        }

        if (equipamento.getQuantidade() == null || equipamento.getQuantidade() == 0) {
            throw new RegraNegocioException("Quantidade inválida");
        }

        if (equipamento.getDisponivel() == null) {
            throw new RegraNegocioException("Disponibilidade inválida");
        }

        if (equipamento.getLocal() == null || equipamento.getLocal().trim().equals("")) {
            throw new RegraNegocioException("Local inválido");
        }

        if (equipamento.getDescricao() == null || equipamento.getDescricao().trim().equals("")) {
            throw new RegraNegocioException("Descrição Inválida");
        }

    }
}
