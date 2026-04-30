package br.edu.ifsudestemg.saapi.controller;
import br.edu.ifsudestemg.saapi.api.dto.AjusteFinanceiroDTO;
import br.edu.ifsudestemg.saapi.model.entity.AjusteFinanceiro;
import br.edu.ifsudestemg.saapi.model.service.AjusteFinanceiroService;
//import com.example.saaapi.api.dto.AjusteFinanceiroDTO;
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
}
