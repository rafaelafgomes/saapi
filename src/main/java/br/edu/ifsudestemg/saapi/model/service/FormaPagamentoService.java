package br.edu.ifsudestemg.saapi.model.service;

import br.edu.ifsudestemg.saapi.model.entity.FormaPagamento;
import br.edu.ifsudestemg.saapi.model.repository.FormaPagamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FormaPagamentoService {

    private FormaPagamentoRepository repository;

    public FormaPagamentoService(FormaPagamentoRepository repository) {
        this.repository = repository;
    }

    public List<FormaPagamento> getFormaPagamento() {
        return repository.findAll();
    }

    public Optional<FormaPagamento> getFormaPagamentoById(Long id) {
        return repository.findById(id);
    }

}
