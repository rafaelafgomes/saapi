package br.edu.ifsudestemg.saapi.model.service;


import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Funcionario;
import br.edu.ifsudestemg.saapi.model.repository.FuncionarioRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FuncionarioService {
    private FuncionarioRepository repository;

    public FuncionarioService(FuncionarioRepository repository) {
        this.repository = repository;
    }

    public List<Funcionario> getFuncionario() {
        return repository.findAll();
    }

    public Optional<Funcionario> getFuncionarioById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Funcionario salvar(Funcionario funcionario) {
        validar(funcionario);
        return repository.save(funcionario);
    }

    @Transactional
    public void excluir(Funcionario funcionario) {
        Objects.requireNonNull(funcionario.getId());
        repository.delete(funcionario);
    }

    public void validar(Funcionario funcionario) {

        if (funcionario.getEmail() == null || funcionario.getEmail().trim().equals("")) {
            throw new RegraNegocioException("Email inválido");
        }

        if (funcionario.getDataAdmissao() == null || funcionario.getDataAdmissao().trim().equals("")) {
            throw new RegraNegocioException("Data de Admissão inválida");
        }

        if (funcionario.getObservacao() == null || funcionario.getObservacao().trim().equals("")) {
            throw new RegraNegocioException("Observação inválida");
        }

        if (funcionario.getNome() == null || funcionario.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }

        if (funcionario.getCpf() == null || funcionario.getCpf().trim().isEmpty() || funcionario.getCpf().length() != 11) {
            throw new RegraNegocioException("Cpf inválido");
        }

        if (funcionario.getTelefone() == null || funcionario.getTelefone().trim().equals("")) {
            throw new RegraNegocioException("Telefone inválido");
        }

        if (funcionario.getStatus() == null || funcionario.getStatus() == "") {
            throw new RegraNegocioException("Status inválido");
        }

        if (funcionario.getLogradouro() == null || funcionario.getLogradouro().trim().equals("")) {
            throw new RegraNegocioException("Logradouro inválido");
        }

        if (funcionario.getNumero() == null || funcionario.getNumero() == 0) {
            throw new RegraNegocioException("Numero Inválido");
        }

        if (funcionario.getComplemento() == null || funcionario.getComplemento().trim().equals("")) {
            throw new RegraNegocioException("Complemento Inválido");
        }

        if (funcionario.getBairro() == null || funcionario.getBairro().trim().equals("")) {
            throw new RegraNegocioException("Bairro Inválido");
        }

        if (funcionario.getCidade() == null || funcionario.getCidade().trim().equals("")) {
            throw new RegraNegocioException("Cidade Inválido");
        }

        if (funcionario.getEstado() == null || funcionario.getEstado().trim().equals("")) {
            throw new RegraNegocioException("Estado Inválido");
        }

        if (funcionario.getCep() == null || funcionario.getCep().trim().isEmpty() || funcionario.getCep().length() != 8) {
            throw new RegraNegocioException("CEP inválido");
        }

        if (funcionario.getDataNascimento() == null || funcionario.getDataNascimento().trim().equals("")) {
            throw new RegraNegocioException("Data de Nascimento inválida");
        }

        if(funcionario.getCargo() == null || funcionario.getCargo().getId() == null || funcionario.getCargo().getId() == 0){
            throw new RegraNegocioException("Cargo inválido");
        }
    }
}
