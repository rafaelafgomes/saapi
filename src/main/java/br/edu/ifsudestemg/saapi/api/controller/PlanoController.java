package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.PlanoDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Plano;
import br.edu.ifsudestemg.saapi.service.PlanoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/planos")
@RequiredArgsConstructor
@CrossOrigin

public class PlanoController {
    private final PlanoService service;

    @GetMapping()
    public ResponseEntity get() {
        List<Plano> plano = service.getPlano();
        return ResponseEntity.ok(plano.stream().map(PlanoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Plano> plano = service.getPlanoById(id);
        if (!plano.isPresent()) {
            return new ResponseEntity("Plano não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(plano.map(PlanoDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody PlanoDTO dto) {
        try {
            Plano plano = converter(dto);
            plano = service.salvar(plano);
            return new ResponseEntity(plano, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody PlanoDTO dto) {
        if (!service.getPlanoById(id).isPresent()) {
            return new ResponseEntity("Plano não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Plano plano = converter(dto);
            plano.setId(id);
            service.salvar(plano);
            return ResponseEntity.ok(plano);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Plano converter(PlanoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Plano plano = modelMapper.map(dto, Plano.class);
        return plano;
    }

}
