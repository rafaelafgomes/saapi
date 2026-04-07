package br.edu.ifsudestemg.saapi.model.repository;

import br.edu.ifsudestemg.saapi.model.entity.Fatura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaturaRepository extends JpaRepository<Fatura, Long> {
}
