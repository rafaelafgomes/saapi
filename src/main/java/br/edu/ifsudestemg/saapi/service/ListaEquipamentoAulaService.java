package br.edu.ifsudestemg.saapi.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.ListaEquipamentoAula;
import br.edu.ifsudestemg.saapi.model.repository.ListaEquipamentoAulaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ListaEquipamentoAulaService {
    private ListaEquipamentoAulaRepository repository;

    public ListaEquipamentoAulaService(ListaEquipamentoAulaRepository repository) {
        this.repository = repository;
    }

    public List<ListaEquipamentoAula> getListaEquipamentoAula() {
        return repository.findAll();
    }

    public Optional<ListaEquipamentoAula> getListaEquipamentoAulaById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public ListaEquipamentoAula salvar (ListaEquipamentoAula listaEquipamentoAula) {
        validar(listaEquipamentoAula);
        return repository.save(listaEquipamentoAula);
    }

    @Transactional
    public void excluir (ListaEquipamentoAula listaEquipamentoAula) {
        Objects.requireNonNull(listaEquipamentoAula.getId());
        repository.delete(listaEquipamentoAula);
    }

    public void validar (ListaEquipamentoAula listaEquipamentoAula) {
        if (listaEquipamentoAula.getQuantidade() == null || listaEquipamentoAula.getQuantidade() == 0) {
            throw new RegraNegocioException("Quantidade inválido");
        }

        if(listaEquipamentoAula.getEquipamento() == null || listaEquipamentoAula.getEquipamento().getId() == null || listaEquipamentoAula.getEquipamento().getId() == 0) {
            throw new RegraNegocioException("Equipamento inválido");
        }

        if(listaEquipamentoAula.getAula() == null || listaEquipamentoAula.getAula().getId() == null || listaEquipamentoAula.getAula().getId() == 0) {
            throw new RegraNegocioException("Aula inválida");
        }
    }
}
