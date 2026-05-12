package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.EquipamentoDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Equipamento;
import br.edu.ifsudestemg.saapi.service.EquipamentoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/equipamentos")
@RequiredArgsConstructor
@CrossOrigin

public class EquipamentoController {
    private final EquipamentoService service;

    @GetMapping()
    public ResponseEntity get() {
        List<Equipamento> equipamentos = service.getEquipamento();
        return ResponseEntity.ok(equipamentos.stream().map(EquipamentoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Equipamento> equipamento = service.getEquipamentoById(id);
        if (!equipamento.isPresent()) {
            return new ResponseEntity("Equipamento não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(equipamento.map(EquipamentoDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody EquipamentoDTO dto) {
        try {
            Equipamento equipamento = converter(dto);
            equipamento = service.salvar(equipamento);
            return new ResponseEntity(equipamento, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody EquipamentoDTO dto) {
        if (!service.getEquipamentoById(id).isPresent()) {
            return new ResponseEntity("Equipamento não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Equipamento equipamento = converter(dto);
            equipamento.setId(id);
            service.salvar(equipamento);
            return ResponseEntity.ok(equipamento);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Equipamento converter(EquipamentoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Equipamento equipamento = modelMapper.map(dto, Equipamento.class);
        return equipamento;
    }

}
