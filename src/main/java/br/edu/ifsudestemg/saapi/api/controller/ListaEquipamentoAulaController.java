package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.ListaEquipamentoAulaDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.entity.Equipamento;
import br.edu.ifsudestemg.saapi.model.entity.ListaEquipamentoAula;
import br.edu.ifsudestemg.saapi.service.AulaService;
import br.edu.ifsudestemg.saapi.service.EquipamentoService;
import br.edu.ifsudestemg.saapi.service.ListaEquipamentoAulaService;
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
@RequestMapping("/api/v1/listasequipamentosaulas")
@RequiredArgsConstructor
@Api("API de Lista de equipamentos em aulas")
@CrossOrigin

public class ListaEquipamentoAulaController {
    private final ListaEquipamentoAulaService service;
    private final EquipamentoService equipamentoService;
    private final AulaService aulaService;

    @GetMapping()
    @ApiOperation("Obter detalhes de todas as listas de equipamentos em aulas")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Lista de equipamentos em aulas encontradas"),
            @ApiResponse(code = 404, message = "Lista de equipamentos em aulas não encontradas"),
            @ApiResponse(code = 403, message = "Usuário não permitido")
    })
    public ResponseEntity get() {
        List<ListaEquipamentoAula> listaEquipamentoAulas = service.getListaEquipamentoAula();
        return ResponseEntity.ok(listaEquipamentoAulas.stream().map(ListaEquipamentoAulaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("aula/{id}")
    @ApiOperation("Obter detalhes de uma lista de equipamentos em aulas pelo id da Aula")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Lista de equipamentos em aulas encontrada"),
            @ApiResponse(code = 404, message = "Lista de equipamentos em aulas não encontrada"),
            @ApiResponse(code = 403, message = "Usuário não permitido")
    })

    public ResponseEntity getByaula(@PathVariable("id") Long id) {
        Optional<Aula> aula = aulaService.getAulaById(id);
        if (!aula.isPresent()) {
            return new ResponseEntity("Aula não encontrada", HttpStatus.NOT_FOUND);
        }

        List<ListaEquipamentoAula> listaEquipamentoAulas = service.getListaEquipamentoAulaByAula(aula.get());
        return ResponseEntity.ok(listaEquipamentoAulas.stream().map(ListaEquipamentoAulaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de uma lista de equipamentos em aulas")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Lista de equipamentos em aulas encontrada"),
            @ApiResponse(code = 404, message = "Lista de equipamentos em aulas não encontrada"),
            @ApiResponse(code = 403, message = "Usuário não permitido")
    })

    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<ListaEquipamentoAula> listaEquipamentoAula = service.getListaEquipamentoAulaById(id);
        if (!listaEquipamentoAula.isPresent()) {
            return new ResponseEntity("Lista Equipamento por Aula não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(listaEquipamentoAula.map(ListaEquipamentoAulaDTO::create));

    }

    @PostMapping()
    @ApiOperation("Salva uma nova lista de equipamentos em aulas")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Lista de equipamentos em aulas salva com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar a lista de equipamentos em aulas"),
            @ApiResponse(code = 403, message = "Usuário não permitido")
    })
    public ResponseEntity post(@RequestBody ListaEquipamentoAulaDTO dto) {
        try {
            ListaEquipamentoAula listaEquipamentoAula = converter(dto);
            listaEquipamentoAula = service.salvar(listaEquipamentoAula);
            return new ResponseEntity(listaEquipamentoAula, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar uma lista de equipamentos em aulas")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Lista de equipamentos em aulas atualizada"),
            @ApiResponse(code = 404, message = "Erro ao atualizar a lista de equipamentos em aulas"),
            @ApiResponse(code = 403, message = "Usuário não permitido")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ListaEquipamentoAulaDTO dto) {
        if (!service.getListaEquipamentoAulaById(id).isPresent()) {
            return new ResponseEntity("Lista Equipamento por Aula não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            ListaEquipamentoAula listaEquipamentoAula = converter(dto);
            listaEquipamentoAula.setId(id);
            service.salvar(listaEquipamentoAula);
            return ResponseEntity.ok(listaEquipamentoAula);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir uma lista de equipamentos em aulas")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Lista de equipamentos em aulas excluída"),
            @ApiResponse(code = 404, message = "Erro ao excluir a lista de equipamentos em aulas"),
            @ApiResponse(code = 403, message = "Usuário não permitido")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<ListaEquipamentoAula> listaEquipamentoAula = service.getListaEquipamentoAulaById(id);
        if (!listaEquipamentoAula.isPresent()) {
            return new ResponseEntity("Lista de equipamentos com aula não encontrada ", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(listaEquipamentoAula.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ListaEquipamentoAula converter(ListaEquipamentoAulaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        ListaEquipamentoAula listaEquipamentoAula = modelMapper.map(dto, ListaEquipamentoAula.class);

        if (dto.getIdEquipamento() != null) {
            Optional<Equipamento> equipamento = equipamentoService.getEquipamentoById(dto.getIdEquipamento());
            if (!equipamento.isPresent()) {
                listaEquipamentoAula.setEquipamento(null);
            } else {
                listaEquipamentoAula.setEquipamento(equipamento.get());
            }
        }
        if (dto.getIdAula() != null) {
            Optional<Aula> aula = aulaService.getAulaById(dto.getIdAula());
            if (!aula.isPresent()) {
                listaEquipamentoAula.setAula(null);
            } else {
                listaEquipamentoAula.setAula(aula.get());
            }
        }
        return listaEquipamentoAula;
    }
}
