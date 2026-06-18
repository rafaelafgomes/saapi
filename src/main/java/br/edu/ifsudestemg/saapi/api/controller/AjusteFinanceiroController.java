package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import br.edu.ifsudestemg.saapi.api.dto.AjusteFinanceiroDTO;
import br.edu.ifsudestemg.saapi.model.entity.AjusteFinanceiro;
import br.edu.ifsudestemg.saapi.service.AjusteFinanceiroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/ajustesfinanceiros")
@RequiredArgsConstructor
@Api("API de Ajustes Financeiros")
@CrossOrigin
public class AjusteFinanceiroController {
    private final AjusteFinanceiroService service;


    @GetMapping()
    @ApiOperation("Obter detalhes de todos os ajustes financeiros")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Lista de Ajuste financeiro encontrado"),
            @ApiResponse(code = 404, message = "Lista de Ajuste financeiro não encontrado")
    })
    public ResponseEntity get() {
        List<AjusteFinanceiro> ajusteFinanceiros = service.getAjusteFinanceiros();
        return ResponseEntity.ok(ajusteFinanceiros.stream().map(AjusteFinanceiroDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um ajuste financeiro")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ajuste financeiro encontrado"),
            @ApiResponse(code = 404, message = "Ajuste financeiro não encontrado")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<AjusteFinanceiro> ajusteFinanceiro = service.getAjusteFinanceiroById(id);
        if (!ajusteFinanceiro.isPresent()) {
            return new ResponseEntity("Ajuste não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ajusteFinanceiro.map(AjusteFinanceiroDTO::create));
    }

    @PostMapping()
    @ApiOperation("Salva um novo ajuste financeiro")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Ajuste financeiro salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar o ajuste financeiro")
    })
    public ResponseEntity post(@RequestBody AjusteFinanceiroDTO dto) {
        try {
            AjusteFinanceiro ajusteFinanceiro = converter(dto);
            ajusteFinanceiro = service.salvar(ajusteFinanceiro);
            return new ResponseEntity(ajusteFinanceiro, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar um ajuste financeiro")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ajuste financeiro atualizado"),
            @ApiResponse(code = 404, message = "Erro ao atualizar o ajuste financeiro")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody AjusteFinanceiroDTO dto) {
        if (!service.getAjusteFinanceiroById(id).isPresent()) {
            return new ResponseEntity("Ajuste Financeiro não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            AjusteFinanceiro ajusteFinanceiro = converter(dto);
            ajusteFinanceiro.setId(id);
            service.salvar(ajusteFinanceiro);
            return ResponseEntity.ok(ajusteFinanceiro);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir um ajuste financeiro")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Ajuste financeiro excluído"),
            @ApiResponse(code = 404, message = "Erro ao excluir o ajuste financeiro")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<AjusteFinanceiro> ajusteFinanceiro = service.getAjusteFinanceiroById(id);
        if (!ajusteFinanceiro.isPresent()) {
            return new ResponseEntity("Ajuste financeiro não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(ajusteFinanceiro.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public AjusteFinanceiro converter(AjusteFinanceiroDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        AjusteFinanceiro ajusteFinanceiro = modelMapper.map(dto, AjusteFinanceiro.class);
        return ajusteFinanceiro;
    }
}
