package br.edu.ifsudestemg.saapi.controller;

import br.edu.ifsudestemg.saapi.api.dto.CargoDTO;
import br.edu.ifsudestemg.saapi.api.dto.ListaEquipamentoAulaDTO;
import br.edu.ifsudestemg.saapi.model.entity.ListaEquipamentoAula;
import br.edu.ifsudestemg.saapi.model.service.ListaEquipamentoAulaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/listaequipamentoaulas")
@RequiredArgsConstructor
@CrossOrigin

public class ListaEquipamentoAulaController {
    private final ListaEquipamentoAulaService service;

    @GetMapping()
    public ResponseEntity get() {
        List<ListaEquipamentoAula> listaEquipamentoAulas = service.getListaEquipamentoAula();
        return ResponseEntity.ok(listaEquipamentoAulas.stream().map(ListaEquipamentoAulaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<ListaEquipamentoAula> listaEquipamentoAula = service.getListaEquipamentoAulaById(id);
        if (!listaEquipamentoAula.isPresent()) {
            return new ResponseEntity("Lista Equipamento por Aula não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(listaEquipamentoAula.map(ListaEquipamentoAulaDTO::create));

    }
}
