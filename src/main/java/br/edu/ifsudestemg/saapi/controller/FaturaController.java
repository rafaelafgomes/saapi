package br.edu.ifsudestemg.saapi.controller;


import br.edu.ifsudestemg.saapi.api.dto.FaturaDTO;
import br.edu.ifsudestemg.saapi.model.entity.Fatura;
import br.edu.ifsudestemg.saapi.model.service.FaturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/faturas")
@RequiredArgsConstructor
@CrossOrigin

public class FaturaController {
    private final FaturaService service;

    @GetMapping()
    public ResponseEntity get() {
        List<Fatura> faturas = service.getFatura();
        return ResponseEntity.ok(faturas.stream().map(FaturaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Fatura> fatura = service.getFaturaById(id);
        if (!fatura.isPresent()) {
            return new ResponseEntity("Fatura não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(fatura.map(FaturaDTO::create));
    }

}
