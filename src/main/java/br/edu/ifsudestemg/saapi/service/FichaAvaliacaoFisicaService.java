package br.edu.ifsudestemg.saapi.service;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.FichaAvaliacaoFisica;
import br.edu.ifsudestemg.saapi.model.repository.FichaAvaliacaoFisicaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FichaAvaliacaoFisicaService {
    private FichaAvaliacaoFisicaRepository repository;

    public FichaAvaliacaoFisicaService(FichaAvaliacaoFisicaRepository repository) {
        this.repository = repository;
    }

    public List<FichaAvaliacaoFisica> getFichaAvaliacaoFisica() {
        return repository.findAll();
    }

    public Optional<FichaAvaliacaoFisica> getFichaAvaliacaoFisicaById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public FichaAvaliacaoFisica salvar (FichaAvaliacaoFisica fichaAvaliacaoFisica) {
        validar(fichaAvaliacaoFisica);
        return repository.save(fichaAvaliacaoFisica);
    }

    @Transactional
    public void excluir (FichaAvaliacaoFisica fichaAvaliacaoFisica) {
        Objects.requireNonNull(fichaAvaliacaoFisica.getId());
        repository.delete(fichaAvaliacaoFisica);
    }

    public void validar (FichaAvaliacaoFisica fichaAvaliacaoFisica) {
        if (fichaAvaliacaoFisica.getDataFicha() == null || fichaAvaliacaoFisica.getDataFicha().trim().equals("")) {
            throw new RegraNegocioException("Data inválido");
        }

        if (fichaAvaliacaoFisica.getPeso() == null || fichaAvaliacaoFisica.getPeso() == 0) {
            throw new RegraNegocioException("Peso inválido");
        }

        if (fichaAvaliacaoFisica.getAltura() == null || fichaAvaliacaoFisica.getAltura() == 0) {
            throw new RegraNegocioException("Altura inválido");
        }

        if(fichaAvaliacaoFisica.getCliente() == null || fichaAvaliacaoFisica.getCliente().getId() == null || fichaAvaliacaoFisica.getCliente().getId() == 0) {
            throw new RegraNegocioException("Cliente inválido");
        }

        if(fichaAvaliacaoFisica.getFuncionario() == null || fichaAvaliacaoFisica.getFuncionario().getId() == null || fichaAvaliacaoFisica.getFuncionario().getId() == 0) {
            throw new RegraNegocioException("Funcionário inválido");
        }
    }

    public String calcularIMC(Float peso, Float altura, Boolean sexo) {
        float imc = peso / (altura * altura);
        String classificacao;
        if (sexo) {
            if (imc < 20.7f) {
                classificacao = "Abaixo do peso";
            }
            else
                if (imc < 26.5f) {
                    classificacao = "Peso normal";
                }
                else
                    if (imc < 27.9f) {
                        classificacao = "Marginalmente acima do peso";
                    }
                    else
                        if (imc < 31.2f) {
                            classificacao = "Acima do peso ideal";
                        }
                        else
                            if (imc < 35.0f){
                                classificacao = "Obesidade Grau I";
                            }
                            else
                                if (imc < 40.0f){
                                    classificacao = "Obesidade Grau II";
                                }
                                else {
                                    classificacao = "Obesidade Grau III";
                                }
        }
        else {
            if (imc < 19.1f) {
                classificacao = "Abaixo do peso";
            }
            else
                if (imc < 25.9f) {
                    classificacao = "Peso normal";
                }
                else
                    if (imc < 27.4f) {
                        classificacao = "Marginalmente acima do peso";
                    }
                    else
                        if (imc < 32.4f) {
                            classificacao = "Acima do peso ideal";
                        }
                        else
                            if (imc < 35.0f){
                                classificacao = "Obesidade Grau I";
                            }
                            else
                                if (imc < 40.0f){
                                    classificacao = "Obesidade Grau II";
                                }
                                else {
                                    classificacao = "Obesidade Grau III";
                                }
        }
        return String.format("IMC: %.2f - %s", imc, classificacao);
    }
}
