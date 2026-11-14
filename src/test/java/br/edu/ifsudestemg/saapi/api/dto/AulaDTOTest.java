package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.entity.Funcionario;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AulaDTOTest {

    @Test
    public void deveConverterEntidadeParaDTOComSucesso() {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(2L);
        funcionario.setNome("Carlos Instrutor");

        Aula entidade = new Aula();
        entidade.setId(1L);
        entidade.setNome("Aula teste");
        entidade.setDataAula("12/12/2020");
        entidade.setHoraAula("12:00");
        entidade.setStatus(true);
        entidade.setCapacidade(10);
        entidade.setDescricao("descrição teste");
        entidade.setFuncionario(funcionario);

        AulaDTO dto = AulaDTO.create(entidade);

        assertNotNull(dto);
        assertEquals(entidade.getId(), dto.getId());
        assertEquals(entidade.getNome(), dto.getNome());
        assertEquals(entidade.getDataAula(), dto.getDataAula());
        assertEquals(entidade.getHoraAula(), dto.getHoraAula());
        assertEquals(entidade.getStatus(), dto.getStatus());
        assertEquals(entidade.getCapacidade(), dto.getCapacidade());
        assertEquals(entidade.getDescricao(), dto.getDescricao());
        assertEquals(funcionario.getId(), dto.getIdFuncionario());
        assertEquals(funcionario.getNome(), dto.getNomeFuncionario());
    }


}