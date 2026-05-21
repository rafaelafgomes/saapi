package br.edu.ifsudestemg.saapi.api.controller;


import br.edu.ifsudestemg.saapi.api.dto.FichaAvaliacaoFisicaDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.FichaAvaliacaoFisica;
import br.edu.ifsudestemg.saapi.model.entity.Funcionario;
import br.edu.ifsudestemg.saapi.service.ClienteService;
import br.edu.ifsudestemg.saapi.service.FichaAvaliacaoFisicaService;
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
@RequestMapping("/api/v1/fichasavaliacaofisica")
@RequiredArgsConstructor
@Api("API de Ficha de avaliação física")
@CrossOrigin

public class FichaAvaliacaoFisicaController {
    private final FichaAvaliacaoFisicaService service;
    private final ClienteService clienteService;
    private final FuncionarioService funcionarioService;

    @GetMapping()
    @ApiOperation("Obter detalhes de todas as fichas de avaliação física")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ficha de avaliação física encontrada"),
            @ApiResponse(code = 404, message = "Ficha de avaliação física não encontrada")
    })
    public ResponseEntity get() {
        List<FichaAvaliacaoFisica> fichaAvaliacaoFisica = service.getFichaAvaliacaoFisica();
        return ResponseEntity.ok(fichaAvaliacaoFisica.stream().map(FichaAvaliacaoFisicaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de uma ficha de avaliação física")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ficha de avaliação física encontrada"),
            @ApiResponse(code = 404, message = "Ficha de avaliação física não encontrada")
    })

    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<FichaAvaliacaoFisica> fichaAvaliacaoFisica = service.getFichaAvaliacaoFisicaById(id);
        if (!fichaAvaliacaoFisica.isPresent()) {
            return new ResponseEntity("Ficha de avaliação não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(fichaAvaliacaoFisica.map(FichaAvaliacaoFisicaDTO::create));
    }

    @PostMapping()
    @ApiOperation("Salva uma nova ficha de avaliação física")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Ficha de avaliação física salva com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar a ficha de avaliação física")
    })
    public ResponseEntity post(@RequestBody FichaAvaliacaoFisicaDTO dto) {
        try {
            FichaAvaliacaoFisica fichaAvaliacaoFisica = converter(dto);
            fichaAvaliacaoFisica = service.salvar(fichaAvaliacaoFisica);
            return new ResponseEntity(fichaAvaliacaoFisica, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar uma ficha de avaliação física")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ficha de avaliação física encontrada"),
            @ApiResponse(code = 404, message = "Ficha de avaliação física não encontrada")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody FichaAvaliacaoFisicaDTO dto) {
        if (!service.getFichaAvaliacaoFisicaById(id).isPresent()) {
            return new ResponseEntity("Ficha de Avaliacao Fisica não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            FichaAvaliacaoFisica fichaAvaliacaoFisica = converter(dto);
            fichaAvaliacaoFisica.setId(id);
            service.salvar(fichaAvaliacaoFisica);
            return ResponseEntity.ok(fichaAvaliacaoFisica);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir uma ficha de avaliação física")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ficha de avaliação física encontrada"),
            @ApiResponse(code = 404, message = "Ficha de avaliação física não encontrada")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<FichaAvaliacaoFisica> fichaAvaliacaoFisica = service.getFichaAvaliacaoFisicaById(id);
        if (!fichaAvaliacaoFisica.isPresent()) {
            return new ResponseEntity("Ficha de avaliação fisica não encontrada ", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(fichaAvaliacaoFisica.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public FichaAvaliacaoFisica converter(FichaAvaliacaoFisicaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        FichaAvaliacaoFisica fichaAvaliacaoFisica = modelMapper.map(dto, FichaAvaliacaoFisica.class);

        if (dto.getIdCliente() != null) {
            Optional<Cliente> cliente = clienteService.getClienteById(dto.getIdCliente());
            if (!cliente.isPresent()) {
                fichaAvaliacaoFisica.setCliente(null);
            } else {
                fichaAvaliacaoFisica.setCliente(cliente.get());
            }
        }
        if (dto.getIdFuncionario() != null) {
            Optional<Funcionario> funcionario = funcionarioService.getFuncionarioById(dto.getIdFuncionario());
            if (!funcionario.isPresent()) {
                fichaAvaliacaoFisica.setFuncionario(null);
            } else {
                fichaAvaliacaoFisica.setFuncionario(funcionario.get());
            }
        }
        return fichaAvaliacaoFisica;
    }
}
