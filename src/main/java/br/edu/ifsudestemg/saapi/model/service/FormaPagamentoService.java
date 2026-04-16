package br.edu.ifsudestemg.saapi.model.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.FormaPagamento;
import br.edu.ifsudestemg.saapi.model.repository.FormaPagamentoRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
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

    @Transactional
    public FormaPagamento salvar (FormaPagamento formaPagamento) {
        validar(formaPagamento);
        return repository.save(formaPagamento);
    }

    @Transactional
    public void excluir (FormaPagamento formaPagamento) {
        Objects.requireNonNull(formaPagamento.getId());
        repository.delete(formaPagamento);
    }
    
    public void validar (FormaPagamento formaPagamento) {
        if (formaPagamento.getNome() == null || formaPagamento.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }

        if (formaPagamento.getDescricao() == null || formaPagamento.getDescricao().trim().equals("")) {
            throw new RegraNegocioException("Descrição inválida");
        }
    }
}
