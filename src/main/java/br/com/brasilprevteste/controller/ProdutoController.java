package br.com.brasilprevteste.controller;

import br.com.brasilprevteste.event.RecursoCriadoEvent;
import br.com.brasilprevteste.model.Produto.Produto;
import br.com.brasilprevteste.repository.Filter.ProdutoFilter;
import br.com.brasilprevteste.service.ProdutoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/produtos")
public class ProdutoController {

    @Autowired
    private final ProdutoService service;

    @Autowired
    private final ApplicationEventPublisher publisher;

    @GetMapping(value = {""})
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('PESQUISAR_PRODUTO') and #oauth2.hasScope('read')")
    public Page<Produto> pesquisar(ProdutoFilter produtoFilter, Pageable pageable) {
        return service.filtrar(produtoFilter, pageable);
    }

    @PostMapping(value = {""})
    @PreAuthorize("hasAuthority('CADASTRAR_PRODUTO') and #oauth2.hasScope('write')")
    public ResponseEntity<Produto> cadastrar(@Valid @RequestBody Produto produto, HttpServletResponse response) {
        Produto criar = service.salvar(produto);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, criar.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(criar);
    }

    @PutMapping(value = {"/{id}"})
    @PreAuthorize("hasAuthority('CADASTRAR_PRODUTO') and #oauth2.hasScope('write')")
    public ResponseEntity<Produto> atualizar(@Valid @RequestBody Produto produto, @PathVariable Long id, HttpServletResponse response) {
        Produto novaCampanha = service.atualizar(id, produto);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, novaCampanha.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(novaCampanha);
    }

    @DeleteMapping(value = {"/{id}"})
    @PreAuthorize("hasAuthority('REMOVER_PRODUTO') and #oauth2.hasScope('write')")
    public ResponseEntity<Produto> deletar(@PathVariable Long id) {
        return ResponseEntity.ok(service.deletar(id));
    }

    @GetMapping(value = {"/{id}"})
    @PreAuthorize("hasAuthority('PESQUISAR_PRODUTO') and #oauth2.hasScope('read')")
    public ResponseEntity<Produto> detalhar(@Valid @PathVariable("id") Long id, HttpServletResponse response) {
        publisher.publishEvent(new RecursoCriadoEvent(this, response, id));
        return ResponseEntity.ok(service.detalhar(id));
    }
}
