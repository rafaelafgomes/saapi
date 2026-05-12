package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.ExercicioDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Exercicio;
import br.edu.ifsudestemg.saapi.service.ExercicioService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/exercicios")
@RequiredArgsConstructor
@CrossOrigin

public class ExercicioController {

    private final ExercicioService service;

    @GetMapping()
    public ResponseEntity get() {
        List<Exercicio> exercicios = service.getExercicio();
        return ResponseEntity.ok(exercicios.stream().map(ExercicioDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Exercicio> exercicio = service.getExercicioById(id);
        if (!exercicio.isPresent()) {
            return new ResponseEntity("Exercicio não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(exercicio.map(ExercicioDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody ExercicioDTO dto) {
        try{
            Exercicio exercicio = converter(dto);
            exercicio = service.salvar(exercicio);
            return new ResponseEntity(exercicio, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Exercicio converter (ExercicioDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Exercicio exercicio = modelMapper.map(dto, Exercicio.class);
        return exercicio;
    }
}
