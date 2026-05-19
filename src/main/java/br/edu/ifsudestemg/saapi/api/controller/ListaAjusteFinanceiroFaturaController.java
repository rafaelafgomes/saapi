package br.edu.ifsudestemg.saapi.api.controller;

import br.edu.ifsudestemg.saapi.api.dto.ListaAjusteFinanceiroFaturaDTO;
import br.edu.ifsudestemg.saapi.exception.RegraNegocioException;
import br.edu.ifsudestemg.saapi.model.entity.AjusteFinanceiro;
import br.edu.ifsudestemg.saapi.model.entity.Fatura;
import br.edu.ifsudestemg.saapi.model.entity.ListaAjusteFinanceiroFatura;
import br.edu.ifsudestemg.saapi.service.AjusteFinanceiroService;
import br.edu.ifsudestemg.saapi.service.ClienteService;
import br.edu.ifsudestemg.saapi.service.FaturaService;
import br.edu.ifsudestemg.saapi.service.ListaAjusteFinanceiroFaturaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/listasajustesfinanceirosfaturas")
@RequiredArgsConstructor
@Api("API de Lista de ajustes financeiros e faturas")
@CrossOrigin

public class ListaAjusteFinanceiroFaturaController {
    private final ListaAjusteFinanceiroFaturaService service;
    private final ClienteService clienteService;
    private final AjusteFinanceiroService ajusteFinanceiroService;
    private final FaturaService faturaService;

    @GetMapping()
    public ResponseEntity get() {
        List<ListaAjusteFinanceiroFatura> listaAjusteFinanceiro = service.getListaAjusteFinanceiroFatura();
        return ResponseEntity.ok(listaAjusteFinanceiro.stream().map(ListaAjusteFinanceiroFaturaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de uma lista de ajustes financeiros e faturas")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Lista de ajustes financeiros e faturas encontradas"),
            @ApiResponse(code = 404, message = "Lista de ajustes financeiros e faturas não encontradas")
    })

    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<ListaAjusteFinanceiroFatura> listaAjusteFinanceiro = service.getListaAjusteFinanceiroFaturaById(id);
        if (!listaAjusteFinanceiro.isPresent()) {
            return new ResponseEntity("Lista de ajuste financeiro não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(listaAjusteFinanceiro.map(ListaAjusteFinanceiroFaturaDTO::create));
    }

    @PostMapping()
    @ApiOperation("Salva uma nova lista de ajustes financeiros e faturas")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Lista de ajustes financeiros e faturas salva com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar a lista de ajustes financeiros e faturas")
    })
    public ResponseEntity post(@RequestBody ListaAjusteFinanceiroFaturaDTO dto) {
        try {
            ListaAjusteFinanceiroFatura listaAjusteFinanceiroFatura = converter(dto);
            listaAjusteFinanceiroFatura = service.salvar(listaAjusteFinanceiroFatura);
            return new ResponseEntity(listaAjusteFinanceiroFatura, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ListaAjusteFinanceiroFaturaDTO dto) {
        if (!service.getListaAjusteFinanceiroFaturaById(id).isPresent()) {
            return new ResponseEntity("Lista de ajuste financeiro não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            ListaAjusteFinanceiroFatura listaAjusteFinanceiroFatura = converter(dto);
            listaAjusteFinanceiroFatura.setId(id);
            service.salvar(listaAjusteFinanceiroFatura);
            return ResponseEntity.ok(listaAjusteFinanceiroFatura);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<ListaAjusteFinanceiroFatura> listaAjusteFinanceiroFatura = service.getListaAjusteFinanceiroFaturaById(id);
        if (!listaAjusteFinanceiroFatura.isPresent()) {
            return new ResponseEntity("Lista de ajuste financeiro não encontrada ", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(listaAjusteFinanceiroFatura.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ListaAjusteFinanceiroFatura converter(ListaAjusteFinanceiroFaturaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        ListaAjusteFinanceiroFatura listaAjusteFinanceiroFatura = modelMapper.map(dto, ListaAjusteFinanceiroFatura.class);

        if (dto.getIdAjusteFinanceiro() != null) {
            Optional<AjusteFinanceiro> ajusteFinanceiro = ajusteFinanceiroService.getAjusteFinanceiroById(dto.getIdAjusteFinanceiro());
            if (!ajusteFinanceiro.isPresent()) {
                listaAjusteFinanceiroFatura.setAjusteFinanceiro(null);
            } else {
                listaAjusteFinanceiroFatura.setAjusteFinanceiro(ajusteFinanceiro.get());
            }
        }
        if (dto.getIdFatura() != null) {
            Optional<Fatura> fatura = faturaService.getFaturaById(dto.getIdFatura());
            if (!fatura.isPresent()) {
                listaAjusteFinanceiroFatura.setFatura(null);
            } else {
                listaAjusteFinanceiroFatura.setFatura(fatura.get());
            }
        }
        return listaAjusteFinanceiroFatura;
    }
}
