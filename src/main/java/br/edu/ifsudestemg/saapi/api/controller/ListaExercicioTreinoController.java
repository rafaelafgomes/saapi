package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.ListaExercicioTreinoDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Exercicio;
import br.edu.ifsudestemg.saapi.model.entity.ListaExercicioTreino;
import br.edu.ifsudestemg.saapi.model.entity.Treino;
import br.edu.ifsudestemg.saapi.service.ExercicioService;
import br.edu.ifsudestemg.saapi.service.ListaExercicioTreinoService;
import br.edu.ifsudestemg.saapi.service.TreinoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Api("API de Lista de exercícios em treinos")
@CrossOrigin

public class ListaExercicioTreinoController {
    private final ListaExercicioTreinoService service;
    private final ExercicioService exercicioService;
    private final TreinoService treinoService;

    @GetMapping()
    @ApiOperation("Obter detalhes de todas as listas de equipamentos em aulas")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Lista de exercícios em treinos encontradas"),
            @ApiResponse(code = 404, message = "Lista de exercícios em treinos não encontradas"),
            @ApiResponse(code = 403, message = "Usuário não permitido")
    })
    public ResponseEntity get() {
        List<ListaExercicioTreino> listaExercicioTreinos = service.getListaExercicioTreino();
        return ResponseEntity.ok(listaExercicioTreinos.stream().map(ListaExercicioTreinoDTO::create).collect(Collectors.toList()));
    }


    @GetMapping("treino/{id}")
    @ApiOperation("Obter detalhes de uma lista de equipamentos em aulas pelo id de treino")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Lista de exercícios em treinos encontrada"),
            @ApiResponse(code = 404, message = "Lista de exercícios em treinos não encontrada"),
            @ApiResponse(code = 403, message = "Usuário não permitido")
    })
    public ResponseEntity getBytreino(@PathVariable("id") Long id) {
        Optional<Treino> treino = treinoService.getTreinoById(id);
        if (!treino.isPresent()) {
            return new ResponseEntity("Treino não encontrado", HttpStatus.NOT_FOUND);
        }
        List<ListaExercicioTreino> listaExercicioTreinos = service.getListaExercicioTreinoByTreino(treino.get());
        return ResponseEntity.ok(listaExercicioTreinos.stream().map(ListaExercicioTreinoDTO::create).collect(Collectors.toList()));

    }


    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de uma lista de equipamentos em aulas")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Lista de exercícios em treinos encontrada"),
            @ApiResponse(code = 404, message = "Lista de exercícios em treinos não encontrada"),
            @ApiResponse(code = 403, message = "Usuário não permitido")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<ListaExercicioTreino> listaExercicioTreino = service.getListaExercicioTreinoById(id);
        if (!listaExercicioTreino.isPresent()) {
            return new ResponseEntity("Lista exercicio de treino não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(listaExercicioTreino.map(ListaExercicioTreinoDTO::create));
    }


    @PostMapping()
    @ApiOperation("Salva uma nova lista de exercícios em treinos")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Lista de exercícios em treinos salva com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar a lista de exercícios em treinos"),
            @ApiResponse(code = 403, message = "Usuário não permitido")
    })
    public ResponseEntity post(@RequestBody ListaExercicioTreinoDTO dto) {
        try {
            ListaExercicioTreino listaExercicioTreino = converter(dto);
            listaExercicioTreino = service.salvar(listaExercicioTreino);
            return new ResponseEntity(listaExercicioTreino, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar uma lista de equipamentos em aulas")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Lista de exercícios em treinos atualizado"),
            @ApiResponse(code = 404, message = "Erro ao atualizar a lista de exercícios em treinos"),
            @ApiResponse(code = 403, message = "Usuário não permitido")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ListaExercicioTreinoDTO dto) {
        if (!service.getListaExercicioTreinoById(id).isPresent()) {
            return new ResponseEntity("Lista exercicio de treino não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            ListaExercicioTreino listaExercicioTreino = converter(dto);
            listaExercicioTreino.setId(id);
            service.salvar(listaExercicioTreino);
            return ResponseEntity.ok(listaExercicioTreino);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir uma lista de equipamentos em aulas")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Lista de exercícios em treinos excluído"),
            @ApiResponse(code = 404, message = "Erro ao excluir a lista de exercícios em treinos"),
            @ApiResponse(code = 403, message = "Usuário não permitido")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<ListaExercicioTreino> listaExercicioTreino = service.getListaExercicioTreinoById(id);
        if (!listaExercicioTreino.isPresent()) {
            return new ResponseEntity("Lista de exercícios com treino não encontrada ", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(listaExercicioTreino.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ListaExercicioTreino converter(ListaExercicioTreinoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        ListaExercicioTreino listaExercicioTreino = modelMapper.map(dto, ListaExercicioTreino.class);

        if (dto.getIdExercicio() != null) {
            Optional<Exercicio> exercicio = exercicioService.getExercicioById(dto.getIdExercicio());
            if (!exercicio.isPresent()) {
                listaExercicioTreino.setExercicio(null);
            } else {
                listaExercicioTreino.setExercicio(exercicio.get());
            }
        }
        if (dto.getIdTreino() != null) {
            Optional<Treino> treino = treinoService.getTreinoById(dto.getIdTreino());
            if (!treino.isPresent()) {
                listaExercicioTreino.setTreino(null);
            } else {
                listaExercicioTreino.setTreino(treino.get());
            }
        }
        return listaExercicioTreino;
    }
}
