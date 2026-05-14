package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.FormaPagamentoDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.FormaPagamento;
import br.edu.ifsudestemg.saapi.service.FormaPagamentoService;
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
@CrossOrigin

public class FormaPagamentoController {
    private final FormaPagamentoService service;

    @GetMapping()
    public ResponseEntity get() {
        List<FormaPagamento> formaPagamento = service.getFormaPagamento();
        return ResponseEntity.ok(formaPagamento.stream().map(FormaPagamentoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<FormaPagamento> formaPagamento = service.getFormaPagamentoById(id);
        if (!formaPagamento.isPresent()) {
            return new ResponseEntity("Forma de pagamento não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(formaPagamento.map(FormaPagamentoDTO::create));
    }

    @PostMapping()
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
