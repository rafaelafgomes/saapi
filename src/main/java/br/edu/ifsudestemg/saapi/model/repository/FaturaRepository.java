package br.edu.ifsudestemg.saapi.model.repository;

import br.edu.ifsudestemg.saapi.model.entity.Fatura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaturaRepository extends JpaRepository<Fatura, Long> {
    Long countByDataPagamentoIsNotNull();

    Long countByDataPagamentoIsNull();

    List<Fatura> findByDataPagamentoIsNotNull();

    List<Fatura> findByDataPagamentoIsNull();
}
