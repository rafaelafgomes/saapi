package br.edu.ifsudestemg.saapi.model.repository;

import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.entity.ListaClienteAula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListaClienteAulaRepository extends JpaRepository<ListaClienteAula, Long> {

    void deleteAllByAula(Aula aula);
    List<ListaClienteAula> findAllByAula(Aula aula);
}
