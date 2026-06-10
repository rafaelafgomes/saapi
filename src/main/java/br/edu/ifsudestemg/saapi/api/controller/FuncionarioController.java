package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.FuncionarioDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Cargo;
import br.edu.ifsudestemg.saapi.model.entity.Funcionario;
import br.edu.ifsudestemg.saapi.service.CargoService;
import br.edu.ifsudestemg.saapi.service.FuncionarioService;
import br.edu.ifsudestemg.saapi.service.PlanoService;
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
@RequestMapping("/api/v1/funcionarios")
@Api("API de Funcionário(a)")
@RequiredArgsConstructor
@CrossOrigin

public class FuncionarioController {
    private final FuncionarioService service;
    private final PlanoService planoService;
    private final CargoService cargoService;

    @GetMapping()
    @ApiOperation("Obter detalhes de todos os funcionários")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Funcionário(a) encontrado(a)"),
            @ApiResponse(code = 404, message = "Funcionário(a) não encontrado(a)")
    })
    public ResponseEntity get() {
        List<Funcionario> funcionario = service.getFuncionario();
        return ResponseEntity.ok(funcionario.stream().map(FuncionarioDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um(a) funcionário(a)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Funcionário(a) encontrado(a)"),
            @ApiResponse(code = 404, message = "Funcionário(a) não encontrado(a)")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Funcionario> funcionario = service.getFuncionarioById(id);
        if (!funcionario.isPresent()) {
            return new ResponseEntity("Funcionario(a) não encontrado(a)", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(funcionario.map(FuncionarioDTO::create));
    }

    @GetMapping("/ativos")
    @ApiOperation("Obter funcionários ativos")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Funcionários ativos encontrados"),
            @ApiResponse(code = 404, message = "Funcionários ativos não encontrados")
    })
    public ResponseEntity getFuncionariosAtivos() {
        List<Funcionario> funcionario = service.getFuncionariosAtivos();
        return ResponseEntity.ok(funcionario.stream().map(FuncionarioDTO::create).collect(Collectors.toList()));
    }

    @PostMapping()
    @ApiOperation("Salva um(a) novo(a) funcionário(a)")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Funcionário(a) salvo(a) com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar funcionário(a)")
    })
    public ResponseEntity post(@RequestBody FuncionarioDTO dto) {
        try {
            Funcionario funcionario = converter(dto);
            funcionario = service.salvar(funcionario);
            return new ResponseEntity(funcionario, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar um(a) funcionário(a)")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Funcionário(a) encontrado(a)"),
            @ApiResponse(code = 404, message = "Funcionário(a) não encontrado(a)")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody FuncionarioDTO dto) {
        if (!service.getFuncionarioById(id).isPresent()) {
            return new ResponseEntity("Funcionario (a) não encontrado(a)", HttpStatus.NOT_FOUND);
        }
        try {
            Funcionario funcionario = converter(dto);
            funcionario.setId(id);
            service.salvar(funcionario);
            return ResponseEntity.ok(funcionario);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir um(a) funcionário(a)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Funcionário(a) encontrado(a)"),
            @ApiResponse(code = 404, message = "Funcionário(a) não encontrado(a)")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Funcionario> funcionario = service.getFuncionarioById(id);
        if (!funcionario.isPresent()) {
            return new ResponseEntity("Funcionario não encontrado (a) ", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(funcionario.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Funcionario converter(FuncionarioDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Funcionario funcionario = modelMapper.map(dto, Funcionario.class);
        if (dto.getIdCargo() != null) {
            Optional<Cargo> cargo = cargoService.getCargoById(dto.getIdCargo());
            if (!cargo.isPresent()) {
                funcionario.setCargo(null);
            } else {
                funcionario.setCargo(cargo.get());
            }
        }
        return funcionario;
    }
}
