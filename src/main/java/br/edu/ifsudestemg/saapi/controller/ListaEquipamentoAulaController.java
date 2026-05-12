package br.edu.ifsudestemg.saapi.controller;

import br.edu.ifsudestemg.saapi.api.dto.CargoDTO;
import br.edu.ifsudestemg.saapi.api.dto.ListaEquipamentoAulaDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.ListaEquipamentoAula;
import br.edu.ifsudestemg.saapi.model.service.ListaEquipamentoAulaService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/listasequipamentosaulas")
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

    @PostMapping()
    public ResponseEntity post(@RequestBody ListaEquipamentoAulaDTO dto) {
        try {
            ListaEquipamentoAula listaEquipamentoAula = converter(dto);
            listaEquipamentoAula = service.salvar(listaEquipamentoAula);
            return new ResponseEntity(listaEquipamentoAula, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ListaEquipamentoAula converter(ListaEquipamentoAulaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        ListaEquipamentoAula listaEquipamentoAula = modelMapper.map(dto, ListaEquipamentoAula.class);
        return listaEquipamentoAula;
    }
}
