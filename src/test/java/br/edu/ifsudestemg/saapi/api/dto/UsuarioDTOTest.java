package br.edu.ifsudestemg.saapi.api.dto;

import br.edu.ifsudestemg.saapi.model.entity.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioDTOTest {
    @Test
    public void deveConverterEntidadeParaDTOComSucesso() {
        Usuario entidade = new Usuario();
        entidade.setId(1L);
        entidade.setLogin("admin");
        entidade.setSenha("123456");
        entidade.setCpf("12345678933");
        entidade.setAdmin(true);

        UsuarioDTO dto = UsuarioDTO.create(entidade);
        dto.setSenhaRepeticao("123456");

        assertNotNull(dto);
        assertEquals(entidade.getId(), dto.getId());
        assertEquals(entidade.getLogin(), dto.getLogin());
        assertEquals(entidade.getSenha(), dto.getSenha());
        assertEquals("123456", dto.getSenhaRepeticao());
        assertEquals(entidade.getCpf(), dto.getCpf());
        assertTrue(dto.isAdmin());
    }

}