package br.edu.ifsudestemg.saapi.controller;
import br.edu.ifsudestemg.saapi.api.dto.ListaAjusteFinanceiroFaturaDTO;
import br.edu.ifsudestemg.saapi.model.entity.ListaAjusteFinanceiroFatura;
import br.edu.ifsudestemg.saapi.model.service.ListaAjusteFinanceiroFaturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/listaAjusteFinanceiros")
@RequiredArgsConstructor
@CrossOrigin

public class ListaAjusteFinanceiroFaturaController {
    private final ListaAjusteFinanceiroFaturaService service;

    @GetMapping()
    public ResponseEntity get() {
        List<ListaAjusteFinanceiroFatura> listaAjusteFinanceiro = service.getListaAjusteFinanceiroFatura();
        return ResponseEntity.ok(listaAjusteFinanceiro.stream().map(ListaAjusteFinanceiroFaturaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<ListaAjusteFinanceiroFatura> listaAjusteFinanceiro = service.getListaAjusteFinanceiroFaturaById(id);
        if (!listaAjusteFinanceiro .isPresent()) {
            return new ResponseEntity("Lista de ajustes financeiros não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(listaAjusteFinanceiro.map(ListaAjusteFinanceiroFaturaDTO::create));
    }
}
