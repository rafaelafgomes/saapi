package br.edu.ifsudestemg.saapi.api.controller;


import br.edu.ifsudestemg.saapi.api.dto.TreinoDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.Funcionario;
import br.edu.ifsudestemg.saapi.model.entity.Treino;
import br.edu.ifsudestemg.saapi.service.ClienteService;
import br.edu.ifsudestemg.saapi.service.FuncionarioService;
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
@RequestMapping("/api/v1/treinos")
@RequiredArgsConstructor
@Api("API de Treinos")
@CrossOrigin

public class TreinoController {
    private final TreinoService service;
    private final ClienteService clienteService;
    private final FuncionarioService funcionarioService;

    @GetMapping()
    @ApiOperation("Obter detalhes de todos os treinos")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Lista de treinos encontrados"),
            @ApiResponse(code = 404, message = "Lista de treinos não encontrados")
    })
    public ResponseEntity get() {
        List<Treino> treino = service.getTreino();
        return ResponseEntity.ok(treino.stream().map(TreinoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um treino")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Treino encontrado"),
            @ApiResponse(code = 404, message = "Treino não encontrado")
    })

    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Treino> treino = service.getTreinoById(id);
        if (!treino.isPresent()) {
            return new ResponseEntity("Treino não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(treino.map(TreinoDTO::create));
    }

    @PostMapping()
    @ApiOperation("Salva uma nova lista de treinos")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Treino salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar um treino")
    })
    public ResponseEntity post(@RequestBody TreinoDTO dto) {
        try {
            Treino treino = converter(dto);
            treino = service.salvar(treino);
            return new ResponseEntity(treino, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar detalhes de um treino")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Treino atualizado com sucesso"),
            @ApiResponse(code = 404, message = "Erro ao atualizar um treino")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody TreinoDTO dto) {
        if (!service.getTreinoById(id).isPresent()) {
            return new ResponseEntity("Treino não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Treino treino = converter(dto);
            treino.setId(id);
            service.salvar(treino);
            return ResponseEntity.ok(treino);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir detalhes de um treino")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Treino excluído com sucesso"),
            @ApiResponse(code = 404, message = "Erro ao excluir um treino")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Treino> treino = service.getTreinoById(id);
        if (!treino.isPresent()) {
            return new ResponseEntity("Treino não encontrado ", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(treino.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Treino converter(TreinoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Treino treino = modelMapper.map(dto, Treino.class);
        if (dto.getIdCliente() != null) {
            Optional<Cliente> cliente = clienteService.getClienteById(dto.getIdCliente());
            if (!cliente.isPresent()) {
                treino.setCliente(null);
            } else {
                treino.setCliente(cliente.get());
            }
        }
        if (dto.getIdFuncionario() != null) {
            Optional<Funcionario> funcionario = funcionarioService.getFuncionarioById(dto.getIdFuncionario());
            if (!funcionario.isPresent()) {
                treino.setFuncionario(null);
            } else {
                treino.setFuncionario(funcionario.get());
            }
        }
        return treino;
    }
}
