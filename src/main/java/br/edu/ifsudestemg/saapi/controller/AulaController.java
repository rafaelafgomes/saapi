package br.edu.ifsudestemg.saapi.controller;
//import com.example.saapi.api.dto.AulaDTO;

import br.edu.ifsudestemg.saapi.api.dto.AulaDTO;
import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.service.AulaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/aulas")
@RequiredArgsConstructor
@CrossOrigin
public class AulaController {
    private final AulaService service;

    @GetMapping()
    public ResponseEntity get() {
        List<Aula> alunos = service.getAula();
        return ResponseEntity.ok(alunos.stream().map(AulaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Aula> aula = service.getAulaById(id);
        if (!aula.isPresent()) {
            return new ResponseEntity("Aula não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(aula.map(AulaDTO::create));
    }
}
