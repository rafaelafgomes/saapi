package br.edu.ifsudestemg.saapi.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.repository.AulaRepository;
import br.edu.ifsudestemg.saapi.model.repository.ListaClienteAulaRepository;
import br.edu.ifsudestemg.saapi.model.repository.ListaEquipamentoAulaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AulaService {
    private AulaRepository repository;
    private final ListaClienteAulaRepository listaClienteAulaRepository;
    private final ListaEquipamentoAulaRepository listaEquipamentoAulaRepository;

    public AulaService(AulaRepository repository, ListaClienteAulaRepository listaClienteAulaRepository,ListaEquipamentoAulaRepository listaEquipamentoAulaRepository) {
        this.repository = repository;
        this.listaClienteAulaRepository = listaClienteAulaRepository;
        this.listaEquipamentoAulaRepository = listaEquipamentoAulaRepository;
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
        listaClienteAulaRepository.deleteAllByAula(aula);
        listaEquipamentoAulaRepository.deleteAllByAula(aula);
        repository.delete(aula);
    }

    public void validar (Aula aula){
        if (aula.getNome() == null || aula.getNome().trim().equals("")) {
            throw new RegraNegocioException("Aula inválido");
        }

        if (aula.getDataAula() == null){
            throw new RegraNegocioException("Data inválido");
        }

        if (aula.getHoraAula() == null){
            throw new RegraNegocioException("Hora inválido");
        }

        if (aula.getStatus() == null) {
            throw new RegraNegocioException("Status inválido");
        }

        if (aula.getCapacidade() == null || aula.getCapacidade() == 0) {
            throw new RegraNegocioException("Capacidade inválido");
        }

        if (aula.getDescricao() == null || aula.getDescricao().trim().equals("")) {
            throw new RegraNegocioException("Descrição inválido");
        }

        if (aula.getFuncionario() == null || aula.getFuncionario().getId() == null || aula.getFuncionario().getId() == 0) {
            throw new RegraNegocioException("Funcionário inválido");
        }
    }
}
