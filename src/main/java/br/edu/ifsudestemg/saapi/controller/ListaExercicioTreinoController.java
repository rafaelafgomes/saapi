package br.edu.ifsudestemg.saapi.controller;

import br.edu.ifsudestemg.saapi.api.dto.ListaExercicioTreinoDTO;
import br.edu.ifsudestemg.saapi.model.entity.ListaExercicioTreino;
import br.edu.ifsudestemg.saapi.model.service.ListaExercicioTreinoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/listaExercicioTreinos")
@RequiredArgsConstructor
@CrossOrigin

public class ListaExercicioTreinoController {
    private final ListaExercicioTreinoService service;

    @GetMapping()
    public ResponseEntity get() {
        List<ListaExercicioTreino> listaExercicioTreinos = service.getListaExercicioTreino();
        return ResponseEntity.ok(listaExercicioTreinos.stream().map(ListaExercicioTreinoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<ListaExercicioTreino> listaExercicioTreino = service.getListaExercicioTreinoById(id);
        if (!listaExercicioTreino.isPresent()) {
            return new ResponseEntity("Lista exercicio de treino não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(listaExercicioTreino.map(ListaExercicioTreinoDTO::create));
    }
}
