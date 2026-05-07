package br.edu.ifsudestemg.saapi.model.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ClienteService {
    private ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public List<Cliente> getCliente() {
        return repository.findAll();
    }

    public Optional<Cliente> getClienteById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Cliente salvar(Cliente cliente) {
        validar(cliente);
        return repository.save(cliente);
    }

    @Transactional
    public void excluir(Cliente cliente) {
        Objects.requireNonNull(cliente.getId());
        repository.delete(cliente);
    }

    public void validar(Cliente cliente) {

        if (cliente.getNome() == null || cliente.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }

        if (cliente.getCpf() == null || cliente.getCpf().trim().isEmpty() || cliente.getCpf().length() != 11) {
            throw new RegraNegocioException("Cpf inválido");
        }

        if (cliente.getTelefone() == null || cliente.getTelefone().trim().equals("")) {
            throw new RegraNegocioException("Telefone inválido");
        }

        if (cliente.getStatus() == null) {
            throw new RegraNegocioException("Status inválido");
        }

        if (cliente.getLogradouro() == null || cliente.getLogradouro().trim().equals("")) {
            throw new RegraNegocioException("Logradouro inválido");
        }

        if (cliente.getNumero() == null || cliente.getNumero() == 0) {
            throw new RegraNegocioException("Numero inválido");
        }

        if (cliente.getComplemento() == null || cliente.getComplemento().trim().equals("")) {
            throw new RegraNegocioException("Complemento Inválido");
        }

        if (cliente.getBairro() == null || cliente.getBairro().trim().equals("")) {
            throw new RegraNegocioException("Bairro Inválido");
        }

        if (cliente.getCidade() == null || cliente.getCidade().trim().equals("")) {
            throw new RegraNegocioException("Cidade Inválido");
        }

        if (cliente.getEstado() == null || cliente.getEstado().trim().equals("")) {
            throw new RegraNegocioException("Estado Inválido");
        }

        if (cliente.getCep() == null || cliente.getCep().trim().isEmpty() || cliente.getCep().length() != 8) {
            throw new RegraNegocioException("CEP inválido");
        }

        if (cliente.getDataNascimento() == null || cliente.getDataNascimento().trim().equals("")) {
            throw new RegraNegocioException("Data de Nascimento inválida");
        }

        if(cliente.getPlano() == null || cliente.getPlano().getId() == null || cliente.getPlano().getId() == 0) {
            throw new RegraNegocioException("Plano inválido");
        }
    }
}
