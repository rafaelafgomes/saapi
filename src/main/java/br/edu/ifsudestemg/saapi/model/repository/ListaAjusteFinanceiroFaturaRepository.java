package br.edu.ifsudestemg.saapi.model.repository;

import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.entity.Fatura;
import br.edu.ifsudestemg.saapi.model.entity.ListaAjusteFinanceiroFatura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListaAjusteFinanceiroFaturaRepository extends JpaRepository<ListaAjusteFinanceiroFatura, Long> {

    void deleteAllByFatura(Fatura fatura);
    List<ListaAjusteFinanceiroFatura> findAllByFatura(Fatura fatura);

}
