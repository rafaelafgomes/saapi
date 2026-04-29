package br.edu.ifsudestemg.saapi.controller;


import br.edu.ifsudestemg.saapi.api.dto.TreinoDTO;
import br.edu.ifsudestemg.saapi.model.entity.Treino;
import br.edu.ifsudestemg.saapi.model.service.TreinoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/treinos")
@RequiredArgsConstructor
@CrossOrigin

public class TreinoController {
    private final TreinoService service;

    @GetMapping()
    public ResponseEntity get() {
        List<Treino> treino = service.getTreino();
        return ResponseEntity.ok(treino.stream().map(TreinoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Treino> treino = service.getTreinoById(id);
        if (!treino .isPresent()) {
            return new ResponseEntity("Treino não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(treino.map(TreinoDTO::create));
    }
}
