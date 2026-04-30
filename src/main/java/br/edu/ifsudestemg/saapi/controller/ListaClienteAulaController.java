package br.edu.ifsudestemg.saapi.controller;

import br.edu.ifsudestemg.saapi.api.dto.ListaClienteAulaDTO;
import br.edu.ifsudestemg.saapi.model.entity.ListaClienteAula;
import br.edu.ifsudestemg.saapi.model.service.ListaClienteAulaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/listaclienteaulas")
@RequiredArgsConstructor
@CrossOrigin

public class ListaClienteAulaController {
    private final ListaClienteAulaService service;

    @GetMapping()
    public ResponseEntity get() {
        List<ListaClienteAula> listaClienteAulas = service.getListaClienteAula();
        return ResponseEntity.ok(listaClienteAulas.stream().map(ListaClienteAulaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable ("id") Long id) {
        Optional<ListaClienteAula> listaClienteAula = service.getListaClienteAulaById(id);
        if (!listaClienteAula.isPresent()) {
            return new ResponseEntity("Lista cliente por aula não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(listaClienteAula.map(ListaClienteAulaDTO::create));

    }

}
