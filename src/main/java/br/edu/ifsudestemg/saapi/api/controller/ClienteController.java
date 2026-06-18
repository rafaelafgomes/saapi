package br.edu.ifsudestemg.saapi.api.controller;


import br.edu.ifsudestemg.saapi.api.dto.ClienteDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.Plano;
import br.edu.ifsudestemg.saapi.service.ClienteService;
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
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
@Api("API de Clientes")
@CrossOrigin

public class ClienteController {
    private final ClienteService service;
    private final PlanoService planoService;

    @GetMapping()
    @ApiOperation("Obter detalhes de todos os clientes")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Lista de Clientes encontrados"),
            @ApiResponse(code = 404, message = "Lista de Clientes não encontrados"),
            @ApiResponse(code = 403, message = "Usuário não permitido")
    })
    public ResponseEntity get() {
        List<Cliente> clientes = service.getCliente();
        return ResponseEntity.ok(clientes.stream().map(ClienteDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente encontrado(a)"),
            @ApiResponse(code = 404, message = "Cliente não encontrado(a)"),
            @ApiResponse(code = 403, message = "Usuário não permitido")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Cliente> cliente = service.getClienteById(id);
        if (!cliente.isPresent()) {
            return new ResponseEntity("Cliente não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(cliente.map(ClienteDTO::create));
    }

    @GetMapping("/ativos")
    @ApiOperation("Obter clientes ativos")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Clientes ativos encontrados"),
            @ApiResponse(code = 404, message = "Clientes ativos não encontrados"),
            @ApiResponse(code = 403, message = "Usuário não permitido")
    })
    public ResponseEntity getClientesAtivos() {
        List<Cliente> clientes = service.getClientesAtivos();
        return ResponseEntity.ok(clientes.stream().map(ClienteDTO::create).collect(Collectors.toList()));
    }

    @PostMapping()
    @ApiOperation("Salva um novo(a) cliente")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cliente salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar o(a) cliente"),
            @ApiResponse(code = 403, message = "Usuário não permitido")
    })
    public ResponseEntity post(@RequestBody ClienteDTO dto) {
        try {
            Cliente cliente = converter(dto);
            cliente = service.salvar(cliente);
            return new ResponseEntity(cliente, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar um cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente atualizado(a)"),
            @ApiResponse(code = 404, message = "Erro ao atualizar o(a) cliente"),
            @ApiResponse(code = 403, message = "Usuário não permitido")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ClienteDTO dto) {
        if (!service.getClienteById(id).isPresent()) {
            return new ResponseEntity("Cliente não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Cliente cliente = converter(dto);
            cliente.setId(id);
            service.salvar(cliente);
            return ResponseEntity.ok(cliente);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir um cliente")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Cliente excluído(a)"),
            @ApiResponse(code = 404, message = "Erro ao excluir o(a) cliente"),
            @ApiResponse(code = 403, message = "Usuário não permitido")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Cliente> cliente = service.getClienteById(id);
        if (!cliente.isPresent()) {
            return new ResponseEntity("Cliente não encontrado (a)", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(cliente.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Cliente converter(ClienteDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Cliente cliente = modelMapper.map(dto, Cliente.class);
        if (dto.getIdPlano() != null) {
            Optional<Plano> plano = planoService.getPlanoById(dto.getIdPlano());
            if (!plano.isPresent()) {
                cliente.setPlano(null);
            } else {
                cliente.setPlano(plano.get());
            }
        }
        return cliente;
    }
}
