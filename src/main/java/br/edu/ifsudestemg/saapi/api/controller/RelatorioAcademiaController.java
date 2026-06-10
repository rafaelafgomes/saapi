package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.RelatorioAcademiaDTO;
import br.edu.ifsudestemg.saapi.service.RelatorioAcademiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/relatorioacademia")
@RequiredArgsConstructor
@CrossOrigin
public class RelatorioAcademiaController {
    private final RelatorioAcademiaService service;

    @GetMapping
    public ResponseEntity<RelatorioAcademiaDTO> get() {
        return ResponseEntity.ok(service.gerarRelatorio());
    }

    @GetMapping("/mensal")
    public ResponseEntity<RelatorioAcademiaDTO> getMensal(@RequestParam String mes,@RequestParam Integer ano) {
        return ResponseEntity.ok(service.gerarRelatorioMensal(mes, ano));
    }
}
