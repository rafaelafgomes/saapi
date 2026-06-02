package br.edu.ifsudestemg.saapi.api.controller;


import br.edu.ifsudestemg.saapi.api.dto.FaturaDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.Fatura;
import br.edu.ifsudestemg.saapi.model.entity.FormaPagamento;
import br.edu.ifsudestemg.saapi.service.ClienteService;
import br.edu.ifsudestemg.saapi.service.FaturaService;
import br.edu.ifsudestemg.saapi.service.FormaPagamentoService;
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
@RequestMapping("/api/v1/faturas")
@RequiredArgsConstructor
@Api("API de Faturas")
@CrossOrigin

public class FaturaController {
    private final FaturaService service;
    private final ClienteService clienteService;
    private final FormaPagamentoService formaPagamentoService;

    @GetMapping()
    @ApiOperation("Obter detalhes de todas as faturas")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Fatura encontrada"),
            @ApiResponse(code = 404, message = "Fatura não encontrada")
    })
    public ResponseEntity get() {
        List<Fatura> faturas = service.getFatura();
        return ResponseEntity.ok(faturas.stream().map(FaturaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de uma fatura")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Fatura encontrada"),
            @ApiResponse(code = 404, message = "Fatura não encontrada")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Fatura> fatura = service.getFaturaById(id);
        if (!fatura.isPresent()) {
            return new ResponseEntity("Fatura não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(fatura.map(FaturaDTO::create));
    }

    @PostMapping()
    @ApiOperation("Salva uma nova fatura")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Fatura salva com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar o(a) fatura")
    })
    public ResponseEntity post(@RequestBody FaturaDTO dto) {
        try {
            Fatura fatura = converter(dto);
            fatura = service.salvar(fatura);
            return new ResponseEntity(fatura, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation("Atualizar uma fatura")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Fatura encontrada"),
            @ApiResponse(code = 404, message = "Fatura não encontrada")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody FaturaDTO dto) {
        if (!service.getFaturaById(id).isPresent()) {
            return new ResponseEntity("Fatura não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Fatura fatura = converter(dto);
            fatura.setId(id);
            service.salvar(fatura);
            return ResponseEntity.ok(fatura);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation("Excluir uma fatura")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Fatura encontrada"),
            @ApiResponse(code = 404, message = "Fatura não encontrada")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Fatura> fatura = service.getFaturaById(id);
        if (!fatura.isPresent()) {
            return new ResponseEntity("Fatura não encontrada ", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(fatura.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Fatura converter(FaturaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Fatura fatura = modelMapper.map(dto, Fatura.class);

        if (dto.getIdCliente() != null) {
            Optional<Cliente> cliente = clienteService.getClienteById(dto.getIdCliente());
            if (!cliente.isPresent()) {
                fatura.setCliente(null);
            } else {
                fatura.setCliente(cliente.get());
            }
        }
        if (dto.getIdFormaPagamento() != null) {
            Optional<FormaPagamento> formaPagamento = formaPagamentoService.getFormaPagamentoById(dto.getIdFormaPagamento());
            if (!formaPagamento.isPresent()) {
                fatura.setFormaPagamento(null);
            } else {
                fatura.setFormaPagamento(formaPagamento.get());
            }
        }
        return fatura;
    }
}
