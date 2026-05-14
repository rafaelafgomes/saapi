package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.ListaEquipamentoAulaDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.entity.Equipamento;
import br.edu.ifsudestemg.saapi.model.entity.ListaEquipamentoAula;
import br.edu.ifsudestemg.saapi.service.AulaService;
import br.edu.ifsudestemg.saapi.service.EquipamentoService;
import br.edu.ifsudestemg.saapi.service.ListaEquipamentoAulaService;
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
    private final EquipamentoService equipamentoService;
    private final AulaService aulaService;

    @GetMapping()
    public ResponseEntity get() {
        List<ListaEquipamentoAula> listaEquipamentoAulas = service.getListaEquipamentoAula();
        return ResponseEntity.ok(listaEquipamentoAulas.stream().map(ListaEquipamentoAulaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<ListaEquipamentoAula> listaEquipamentoAula = service.getListaEquipamentoAulaById(id);
        if (!listaEquipamentoAula.isPresent()) {
            return new ResponseEntity("Lista Equipamento por Aula não encontrada", HttpStatus.NOT_FOUND);
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

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ListaEquipamentoAulaDTO dto) {
        if (!service.getListaEquipamentoAulaById(id).isPresent()) {
            return new ResponseEntity("Lista Equipamento por Aula não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            ListaEquipamentoAula listaEquipamentoAula = converter(dto);
            listaEquipamentoAula.setId(id);
            service.salvar(listaEquipamentoAula);
            return ResponseEntity.ok(listaEquipamentoAula);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<ListaEquipamentoAula> listaEquipamentoAula = service.getListaEquipamentoAulaById(id);
        if (!listaEquipamentoAula.isPresent()) {
            return new ResponseEntity("Lista de equipamentos com aula não encontrada ", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(listaEquipamentoAula.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ListaEquipamentoAula converter(ListaEquipamentoAulaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        ListaEquipamentoAula listaEquipamentoAula = modelMapper.map(dto, ListaEquipamentoAula.class);

        if (dto.getIdEquipamento() != null) {
            Optional<Equipamento> equipamento = equipamentoService.getEquipamentoById(dto.getIdEquipamento());
            if (!equipamento.isPresent()) {
                listaEquipamentoAula.setEquipamento(null);
            } else {
                listaEquipamentoAula.setEquipamento(equipamento.get());
            }
        }
        if (dto.getIdAula() != null) {
            Optional<Aula> aula = aulaService.getAulaById(dto.getIdAula());
            if (!aula.isPresent()) {
                listaEquipamentoAula.setAula(null);
            } else {
                listaEquipamentoAula.setAula(aula.get());
            }
        }
        return listaEquipamentoAula;
    }
}
