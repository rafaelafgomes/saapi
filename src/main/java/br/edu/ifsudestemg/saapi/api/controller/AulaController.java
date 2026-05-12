package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.AulaDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.Aula;
import br.edu.ifsudestemg.saapi.model.entity.Funcionario;
import br.edu.ifsudestemg.saapi.service.AulaService;
import br.edu.ifsudestemg.saapi.service.ClienteService;
import br.edu.ifsudestemg.saapi.service.FuncionarioService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/aulas")
@RequiredArgsConstructor
@CrossOrigin
public class AulaController {
    private final AulaService service;
    private final FuncionarioService funcionarioService;
    private final ClienteService clienteService;

    @GetMapping()
    public ResponseEntity get() {
        List<Aula> alunos = service.getAula();
        return ResponseEntity.ok(alunos.stream().map(AulaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Aula> aula = service.getAulaById(id);
        if (!aula.isPresent()) {
            return new ResponseEntity("Aula não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(aula.map(AulaDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody AulaDTO dto) {
        try {
            Aula aula = converter(dto);
            aula = service.salvar(aula);
            return new ResponseEntity(aula, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody AulaDTO dto) {
        if (!service.getAulaById(id).isPresent()) {
            return new ResponseEntity("Aula não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Aula aula = converter(dto);
            aula.setId(id);
            service.salvar(aula);
            return ResponseEntity.ok(aula);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Aula converter(AulaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Aula aula = modelMapper.map(dto, Aula.class);

        if (dto.getIdFuncionario() != null) {
            Optional<Funcionario> funcionario = funcionarioService.getFuncionarioById(dto.getIdFuncionario());
            if (!funcionario.isPresent()) {
                aula.setFuncionario(null);
            } else {
                aula.setFuncionario(funcionario.get());
            }
        }
        return aula;
    }
}

