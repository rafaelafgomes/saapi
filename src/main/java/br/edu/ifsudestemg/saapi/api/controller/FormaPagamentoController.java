package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.FormaPagamentoDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.FormaPagamento;
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
@RequestMapping("/api/v1/formaspagamentos")
@RequiredArgsConstructor
@Api("API de Forma de Pagamento")
@CrossOrigin

public class FormaPagamentoController {
    private final FormaPagamentoService service;

    @GetMapping()
    public ResponseEntity get() {
        List<FormaPagamento> formaPagamento = service.getFormaPagamento();
        return ResponseEntity.ok(formaPagamento.stream().map(FormaPagamentoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de uma forma de pagamento")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Forma de pagamento encontrada"),
            @ApiResponse(code = 404, message = "Forma de pagamento não encontrada")
    })

    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<FormaPagamento> formaPagamento = service.getFormaPagamentoById(id);
        if (!formaPagamento.isPresent()) {
            return new ResponseEntity("Forma de pagamento não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(formaPagamento.map(FormaPagamentoDTO::create));
    }

    @PostMapping()
    @ApiOperation("Salva uma nova forma de pagamento")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Forma de pagamento salva com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar a forma de pagamento")
    })
    public ResponseEntity post(@RequestBody FormaPagamentoDTO dto) {
        try {
            FormaPagamento formaPagamento = converter(dto);
            formaPagamento = service.salvar(formaPagamento);
            return new ResponseEntity(formaPagamento, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody FormaPagamentoDTO dto) {
        if (!service.getFormaPagamentoById(id).isPresent()) {
            return new ResponseEntity("Forma de Pagamento não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            FormaPagamento formaPagamento = converter(dto);
            formaPagamento.setId(id);
            service.salvar(formaPagamento);
            return ResponseEntity.ok(formaPagamento);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<FormaPagamento> formaPagamento = service.getFormaPagamentoById(id);
        if (!formaPagamento.isPresent()) {
            return new ResponseEntity("Forma de pagamento não encontrada ", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(formaPagamento.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public FormaPagamento converter(FormaPagamentoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        FormaPagamento formaPagamento = modelMapper.map(dto, FormaPagamento.class);
        return formaPagamento;
    }
}
