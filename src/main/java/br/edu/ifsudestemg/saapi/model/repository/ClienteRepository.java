package br.edu.ifsudestemg.saapi.model.repository;

import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
