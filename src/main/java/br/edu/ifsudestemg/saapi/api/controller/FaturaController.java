package br.edu.ifsudestemg.saapi.api.controller;


import br.edu.ifsudestemg.saapi.api.dto.FaturaDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.Fatura;
import br.edu.ifsudestemg.saapi.model.entity.FormaPagamento;
import br.edu.ifsudestemg.saapi.service.ClienteService;
import br.edu.ifsudestemg.saapi.service.FaturaService;
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
@RequestMapping("/api/v1/faturas")
@RequiredArgsConstructor
@CrossOrigin

public class FaturaController {
    private final FaturaService service;
    private final ClienteService clienteService;
    private final FormaPagamentoService formaPagamentoService;

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

    @PostMapping()
    public ResponseEntity post (@RequestBody FaturaDTO dto) {
        try{
            Fatura fatura = converter(dto);
            fatura = service.salvar(fatura);
            return new ResponseEntity(fatura, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Fatura converter (FaturaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Fatura fatura = modelMapper.map(dto, Fatura.class);

        if(dto.getIdCliente() != null) {
            Optional<Cliente> cliente = clienteService.getClienteById(dto.getIdCliente());
            if(!cliente.isPresent()) {
                fatura.setCliente(null);
            } else {
                fatura.setCliente(cliente.get());
            }
        }
        if(dto.getIdFormaPagamento() != null) {
            Optional<FormaPagamento> formaPagamento = formaPagamentoService.getFormaPagamentoById(dto.getIdFormaPagamento());
            if(!formaPagamento.isPresent()) {
                fatura.setFormaPagamento(null);
            } else {
                fatura.setFormaPagamento(formaPagamento.get());
            }
        }
        return fatura;
    }
}
