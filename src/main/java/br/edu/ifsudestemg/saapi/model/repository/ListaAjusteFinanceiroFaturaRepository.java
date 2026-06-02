package br.edu.ifsudestemg.saapi.model.repository;

import br.edu.ifsudestemg.saapi.model.entity.Fatura;
import br.edu.ifsudestemg.saapi.model.entity.ListaAjusteFinanceiroFatura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListaAjusteFinanceiroFaturaRepository extends JpaRepository<ListaAjusteFinanceiroFatura, Long> {

    void deleteAllByFatura(Fatura fatura);

}
