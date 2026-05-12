package br.edu.ifsudestemg.saapi.controller;

import br.edu.ifsudestemg.saapi.api.dto.ListaExercicioTreinoDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Exercicio;
import br.edu.ifsudestemg.saapi.model.entity.ListaExercicioTreino;
import br.edu.ifsudestemg.saapi.model.entity.Treino;
import br.edu.ifsudestemg.saapi.model.service.ExercicioService;
import br.edu.ifsudestemg.saapi.model.service.ListaExercicioTreinoService;
import br.edu.ifsudestemg.saapi.model.service.TreinoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/listasexerciciostreinos")
@RequiredArgsConstructor
@CrossOrigin

public class ListaExercicioTreinoController {
    private final ListaExercicioTreinoService service;
    private final ExercicioService exercicioService;
    private final TreinoService treinoService;

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

    @PostMapping()
    public ResponseEntity post(@RequestBody ListaExercicioTreinoDTO dto) {
        try {
            ListaExercicioTreino listaExercicioTreino = converter(dto);
            listaExercicioTreino = service.salvar(listaExercicioTreino);
            return new ResponseEntity(listaExercicioTreino, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ListaExercicioTreino converter(ListaExercicioTreinoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        ListaExercicioTreino listaExercicioTreino = modelMapper.map(dto, ListaExercicioTreino.class);

        if(dto.getIdExercicio() != null) {
            Optional<Exercicio> exercicio = exercicioService.getExercicioById(dto.getIdExercicio());
            if(!exercicio.isPresent()) {
                listaExercicioTreino.setExercicio(null);
            } else {
                listaExercicioTreino.setExercicio(exercicio.get());
            }
        }
        if(dto.getIdTreino() != null) {
            Optional<Treino> treino = treinoService.getTreinoById(dto.getIdTreino());
            if(!treino.isPresent()) {
                listaExercicioTreino.setTreino(null);
            } else {
                listaExercicioTreino.setTreino(treino.get());
            }
        }
        return listaExercicioTreino;
    }
}
