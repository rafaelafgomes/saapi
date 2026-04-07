package br.edu.ifsudestemg.saapi.model.repository;

import br.edu.ifsudestemg.saapi.model.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {


}