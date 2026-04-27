package br.edu.ifsudestemg.saapi.model.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.entity.ListaClienteAula;
import br.edu.ifsudestemg.saapi.model.repository.ListaClienteAulaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ListaClienteAulaService {
    private ListaClienteAulaRepository repository;

    public ListaClienteAulaService(ListaClienteAulaRepository repository) {
        this.repository = repository;
    }

    public List<ListaClienteAula> getListaClienteAula() {
        return repository.findAll();
    }

    public Optional<ListaClienteAula> getListaClienteAulaById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public ListaClienteAula salvar (ListaClienteAula listaClienteAula) {
        validar(listaClienteAula);
        return repository.save(listaClienteAula);
    }

    @Transactional
    public void excluir (ListaClienteAula listaClienteAula) {
        Objects.requireNonNull(listaClienteAula.getId());
        repository.delete(listaClienteAula);
    }

    public void validar (ListaClienteAula listaClienteAula){

        if(listaClienteAula.getCliente() == null || listaClienteAula.getCliente().getId() == null || listaClienteAula.getCliente().getId() == 0){
            throw new RegraNegocioException("Cliente inválido");
        }

        if(listaClienteAula.getAula() == null || listaClienteAula.getAula().getId() == null || listaClienteAula.getAula().getId() == 0){
            throw new RegraNegocioException("Aula inválida");
        }
    }

}
