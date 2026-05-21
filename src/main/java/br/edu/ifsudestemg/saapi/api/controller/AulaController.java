package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.AulaDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.entity.Funcionario;
import br.edu.ifsudestemg.saapi.service.AulaService;
import br.edu.ifsudestemg.saapi.service.ClienteService;
import br.edu.ifsudestemg.saapi.service.FuncionarioService;
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
@RequestMapping("/api/v1/aulas")
@RequiredArgsConstructor
@Api("API de Aulas")
@CrossOrigin
public class AulaController {
    private final AulaService service;
    private final FuncionarioService funcionarioService;
    private final ClienteService clienteService;

    @GetMapping()
    @ApiOperation("Obter detalhes de todas as aulas")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Aula encontrada"),
            @ApiResponse(code = 404, message = "Aula não encontrada")
    })
    public ResponseEntity get() {
        List<Aula> alunos = service.getAula();
        return ResponseEntity.ok(alunos.stream().map(AulaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de uma aula")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Aula encontrada"),
            @ApiResponse(code = 404, message = "Aula não encontrada")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Aula> aula = service.getAulaById(id);
        if (!aula.isPresent()) {
            return new ResponseEntity("Aula não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(aula.map(AulaDTO::create));
    }

    @PostMapping()
    @ApiOperation("Salva uma nova aula")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Aula salva com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar a aula")
    })
    public ResponseEntity post(@RequestBody AulaDTO dto) {
        try {
            Aula aula = converter(dto);
            aula = service.salvar(aula);
            return new ResponseEntity(aula, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar uma aula")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Aula encontrada"),
            @ApiResponse(code = 404, message = "Aula não encontrada")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody AulaDTO dto) {
        if (!service.getAulaById(id).isPresent()) {
            return new ResponseEntity("Aula não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Aula aula = converter(dto);
            aula.setId(id);
            service.salvar(aula);
            return ResponseEntity.ok(aula);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir uma aula")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Aula encontrada"),
            @ApiResponse(code = 404, message = "Aula não encontrada")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Aula> aula = service.getAulaById(id);
        if (!aula.isPresent()) {
            return new ResponseEntity("Aula não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(aula.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Aula converter(AulaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Aula aula = modelMapper.map(dto, Aula.class);

        if (dto.getIdFuncionario() != null) {
            Optional<Funcionario> funcionario = funcionarioService.getFuncionarioById(dto.getIdFuncionario());
            if (!funcionario.isPresent()) {
                aula.setFuncionario(null);
            } else {
                aula.setFuncionario(funcionario.get());
            }
        }
        return aula;
    }
}

