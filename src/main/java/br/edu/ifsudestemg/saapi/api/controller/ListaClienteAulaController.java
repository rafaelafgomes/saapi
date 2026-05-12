package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.ListaClienteAulaDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.ListaClienteAula;
import br.edu.ifsudestemg.saapi.service.AulaService;
import br.edu.ifsudestemg.saapi.service.ClienteService;
import br.edu.ifsudestemg.saapi.service.ListaClienteAulaService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/listasclientesaulas")
@RequiredArgsConstructor
@CrossOrigin

public class ListaClienteAulaController {
    private final ListaClienteAulaService service;
    private final ClienteService clienteService;
    private final AulaService aulaService;

    @GetMapping()
    public ResponseEntity get() {
        List<ListaClienteAula> listaClienteAulas = service.getListaClienteAula();
        return ResponseEntity.ok(listaClienteAulas.stream().map(ListaClienteAulaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<ListaClienteAula> listaClienteAula = service.getListaClienteAulaById(id);
        if (!listaClienteAula.isPresent()) {
            return new ResponseEntity("Lista cliente por aula não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(listaClienteAula.map(ListaClienteAulaDTO::create));

    }

    @PostMapping()
    public ResponseEntity post(@RequestBody ListaClienteAulaDTO dto) {
        try {
            ListaClienteAula listaClienteAula = converter(dto);
            listaClienteAula = service.salvar(listaClienteAula);
            return new ResponseEntity(listaClienteAula, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ListaClienteAulaDTO dto) {
        if (!service.getListaClienteAulaById(id).isPresent()) {
            return new ResponseEntity("Lista cliente por aula não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            ListaClienteAula listaClienteAula = converter(dto);
            listaClienteAula.setId(id);
            service.salvar(listaClienteAula);
            return ResponseEntity.ok(listaClienteAula);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ListaClienteAula converter(ListaClienteAulaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        ListaClienteAula listaClienteAula = modelMapper.map(dto, ListaClienteAula.class);

        if (dto.getIdCliente() != null) {
            Optional<Cliente> cliente = clienteService.getClienteById(dto.getIdCliente());
            if (!cliente.isPresent()) {
                listaClienteAula.setCliente(null);
            } else {
                listaClienteAula.setCliente(cliente.get());
            }

        }
        if (dto.getIdAula() != null) {
            Optional<Aula> aula = aulaService.getAulaById(dto.getIdAula());
            if (!aula.isPresent()) {
                listaClienteAula.setAula(null);
            } else {
                listaClienteAula.setAula(aula.get());
            }
        }
        return listaClienteAula;
    }
}
