package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.CargoDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Cargo;
import br.edu.ifsudestemg.saapi.service.CargoService;
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
@RequestMapping("/api/v1/cargos")
@RequiredArgsConstructor
@Api("API de Cargos")
@CrossOrigin
public class CargoController {
    private final CargoService service;

    @GetMapping()
    @ApiOperation("Obter detalhes de todos os cargo")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cargo encontrado"),
            @ApiResponse(code = 404, message = "Cargo não encontrado")
    })
    public ResponseEntity get() {
        List<Cargo> cargos = service.getCargo();
        return ResponseEntity.ok(cargos.stream().map(CargoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um cargo")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cargo encontrado"),
            @ApiResponse(code = 404, message = "Cargo não encontrado")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Cargo> cargo = service.getCargoById(id);
        if (!cargo.isPresent()) {
            return new ResponseEntity("Cargo não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(cargo.map(CargoDTO::create));
    }

    @PostMapping()
    @ApiOperation("Salva um novo cargo")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cargo salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar o cargo")
    })
    public ResponseEntity post(@RequestBody CargoDTO dto) {
        try {
            Cargo cargo = converter(dto);
            cargo = service.salvar(cargo);
            return new ResponseEntity(cargo, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar um cargo")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cargo encontrado"),
            @ApiResponse(code = 404, message = "Cargo não encontrado")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody CargoDTO dto) {
        if (!service.getCargoById(id).isPresent()) {
            return new ResponseEntity("Cargo não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Cargo cargo = converter(dto);
            cargo.setId(id);
            service.salvar(cargo);
            return ResponseEntity.ok(cargo);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir um cargo")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cargo encontrado"),
            @ApiResponse(code = 404, message = "Cargo não encontrado")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Cargo> cargo = service.getCargoById(id);
        if (!cargo.isPresent()) {
            return new ResponseEntity("Cargo não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(cargo.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Cargo converter(CargoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Cargo cargo = modelMapper.map(dto, Cargo.class);
        return cargo;
    }
}
