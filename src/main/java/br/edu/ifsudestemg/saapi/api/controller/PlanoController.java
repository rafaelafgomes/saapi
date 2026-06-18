package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.PlanoDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Plano;
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
@RequestMapping("/api/v1/planos")
@RequiredArgsConstructor
@Api("API de Planos")
@CrossOrigin

public class PlanoController {
    private final PlanoService service;

    @GetMapping()
    @ApiOperation("Obter detalhes de todos os plano")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Lista de planos encontrados"),
            @ApiResponse(code = 404, message = "Lista de planos não encontrados"),
            @ApiResponse(code = 403, message = "Usuário não permitido")
    })
    public ResponseEntity get() {
        List<Plano> plano = service.getPlano();
        return ResponseEntity.ok(plano.stream().map(PlanoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um plano")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Plano encontrado"),
            @ApiResponse(code = 404, message = "Plano não encontrado"),
            @ApiResponse(code = 403, message = "Usuário não permitido")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Plano> plano = service.getPlanoById(id);
        if (!plano.isPresent()) {
            return new ResponseEntity("Plano não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(plano.map(PlanoDTO::create));
    }

    @PostMapping()
    @ApiOperation("Salva um novo plano")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Planos salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar o plano"),
            @ApiResponse(code = 403, message = "Usuário não permitido")
    })
    public ResponseEntity post(@RequestBody PlanoDTO dto) {
        try {
            Plano plano = converter(dto);
            plano = service.salvar(plano);
            return new ResponseEntity(plano, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar detalhes de um plano")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Planos atualizado com sucesso"),
            @ApiResponse(code = 404, message = "Erro ao atualizar o plano"),
            @ApiResponse(code = 403, message = "Usuário não permitido")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody PlanoDTO dto) {
        if (!service.getPlanoById(id).isPresent()) {
            return new ResponseEntity("Plano não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Plano plano = converter(dto);
            plano.setId(id);
            service.salvar(plano);
            return ResponseEntity.ok(plano);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir um plano")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Planos excluído com sucesso"),
            @ApiResponse(code = 404, message = "Erro ao excluir o plano"),
            @ApiResponse(code = 403, message = "Usuário não permitido")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Plano> plano = service.getPlanoById(id);
        if (!plano.isPresent()) {
            return new ResponseEntity("Plano não encontrado ", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(plano.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Plano converter(PlanoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Plano plano = modelMapper.map(dto, Plano.class);
        return plano;
    }

}
