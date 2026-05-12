package br.edu.ifsudestemg.saapi.controller;


import br.edu.ifsudestemg.saapi.api.dto.FichaAvaliacaoFisicaDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.FichaAvaliacaoFisica;
import br.edu.ifsudestemg.saapi.model.service.FichaAvaliacaoFisicaService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/fichasavaliacaofisica")
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

    @PostMapping()
    public ResponseEntity post(@RequestBody FichaAvaliacaoFisicaDTO dto) {
        try {
            FichaAvaliacaoFisica fichaAvaliacaoFisica = converter(dto);
            fichaAvaliacaoFisica = service.salvar(fichaAvaliacaoFisica);
            return new ResponseEntity(fichaAvaliacaoFisica, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public FichaAvaliacaoFisica converter(FichaAvaliacaoFisicaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        FichaAvaliacaoFisica fichaAvaliacaoFisica = modelMapper.map(dto, FichaAvaliacaoFisica.class);
        return fichaAvaliacaoFisica;
    }
}
