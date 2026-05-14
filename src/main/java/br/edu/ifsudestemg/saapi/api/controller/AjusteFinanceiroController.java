package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
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
@CrossOrigin
public class AjusteFinanceiroController {
    private final AjusteFinanceiroService service;


    @GetMapping()
    public ResponseEntity get() {
        List<AjusteFinanceiro> ajusteFinanceiros = service.getAjusteFinanceiros();
        return ResponseEntity.ok(ajusteFinanceiros.stream().map(AjusteFinanceiroDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<AjusteFinanceiro> ajusteFinanceiro = service.getAjusteFinanceiroById(id);
        if (!ajusteFinanceiro.isPresent()) {
            return new ResponseEntity("Ajuste não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ajusteFinanceiro.map(AjusteFinanceiroDTO::create));
    }

    @PostMapping()
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
