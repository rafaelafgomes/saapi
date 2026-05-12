package br.edu.ifsudestemg.saapi.controller;


import br.edu.ifsudestemg.saapi.api.dto.TreinoDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Cliente;
import br.edu.ifsudestemg.saapi.model.entity.Funcionario;
import br.edu.ifsudestemg.saapi.model.entity.Treino;
import br.edu.ifsudestemg.saapi.model.service.ClienteService;
import br.edu.ifsudestemg.saapi.model.service.FuncionarioService;
import br.edu.ifsudestemg.saapi.model.service.TreinoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/treinos")
@RequiredArgsConstructor
@CrossOrigin

public class TreinoController {
    private final TreinoService service;
    private final ClienteService clienteService;
    private final FuncionarioService funcionarioService;

    @GetMapping()
    public ResponseEntity get() {
        List<Treino> treino = service.getTreino();
        return ResponseEntity.ok(treino.stream().map(TreinoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Treino> treino = service.getTreinoById(id);
        if (!treino .isPresent()) {
            return new ResponseEntity("Treino não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(treino.map(TreinoDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody TreinoDTO dto) {
        try {
            Treino treino = converter(dto);
            treino = service.salvar(treino);
            return new ResponseEntity(treino, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Treino converter(TreinoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Treino treino = modelMapper.map(dto, Treino.class);
        if(dto.getIdCliente() != null) {
            Optional<Cliente> cliente =clienteService.getClienteById(dto.getIdCliente());
            if(!cliente.isPresent()) {
                treino.setCliente(null);
            } else {
                treino.setCliente(cliente.get());
            }
        }
        if (dto.getIdFuncionario() != null) {
            Optional<Funcionario> funcionario = funcionarioService.getFuncionarioById(dto.getIdFuncionario());
            if(!funcionario.isPresent()) {
                treino.setFuncionario(null);
            } else {
                treino.setFuncionario(funcionario.get());
            }
        }
        return treino;
    }
}
