package br.edu.ifsudestemg.saapi.controller;


import br.edu.ifsudestemg.saapi.api.dto.FichaAvaliacaoFisicaDTO;
import br.edu.ifsudestemg.saapi.model.entity.FichaAvaliacaoFisica;
import br.edu.ifsudestemg.saapi.model.service.FichaAvaliacaoFisicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/fichaAvaliacaoFisica")
@RequiredArgsConstructor
@CrossOrigin

public class FichaAvaliacaoFisicaController {
    private final FichaAvaliacaoFisicaService service;
    @GetMapping()
    public ResponseEntity get() {
        List<FichaAvaliacaoFisica> fichaAvaliacaoFisica = service.getFichaAvaliacaoFisica();
        return ResponseEntity.ok(fichaAvaliacaoFisica.stream().map(FichaAvaliacaoFisicaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<FichaAvaliacaoFisica> fichaAvaliacaoFisica = service.getFichaAvaliacaoFisicaById(id);
        if (!fichaAvaliacaoFisica .isPresent()) {
            return new ResponseEntity("Ficha de avaliação não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(fichaAvaliacaoFisica.map(FichaAvaliacaoFisicaDTO::create));
    }
}
