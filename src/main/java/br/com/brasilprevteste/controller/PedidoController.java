package br.com.brasilprevteste.controller;

import br.com.brasilprevteste.event.RecursoCriadoEvent;
import br.com.brasilprevteste.model.Pedido.Pedido;
import br.com.brasilprevteste.repository.Filter.PedidoFilter;
import br.com.brasilprevteste.service.PedidoService;
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
@RequestMapping(value = "/pedidos")
public class PedidoController {

    @Autowired
    private final PedidoService service;

    @Autowired
    private final ApplicationEventPublisher publisher;

    @GetMapping(value = {""})
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('PESQUISAR_PEDIDO') and #oauth2.hasScope('read')")
    public Page<Pedido> pesquisar(PedidoFilter pedidoFilter, Pageable pageable) {
        return service.filtrar(pedidoFilter, pageable);
    }

    @PostMapping(value = {""})
    @PreAuthorize("hasAuthority('CADASTRAR_PEDIDO') and #oauth2.hasScope('write')")
    public ResponseEntity<Pedido> cadastrar(@Valid @RequestBody Pedido pedido, HttpServletResponse response) {
        Pedido criar = service.salvar(pedido);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, criar.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(criar);
    }

    @PutMapping(value = {"/{id}"})
    @PreAuthorize("hasAuthority('CADASTRAR_PEDIDO') and #oauth2.hasScope('write')")
    public ResponseEntity<Pedido> atualizar(@Valid @RequestBody Pedido pedido, @PathVariable Long id, HttpServletResponse response) {
        Pedido novaPedido = service.atualizar(id, pedido);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, novaPedido.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(novaPedido);
    }

    @DeleteMapping(value = {"/{id}"})
    @PreAuthorize("hasAuthority('REMOVER_PEDIDO') and #oauth2.hasScope('write')")
    public ResponseEntity<Pedido> deletar(@PathVariable Long id) {
        return ResponseEntity.ok(service.deletar(id));
    }

    @GetMapping(value = {"/{id}"})
    @PreAuthorize("hasAuthority('PESQUISAR_PEDIDO') and #oauth2.hasScope('read')")
    public ResponseEntity<Pedido> detalhar(@Valid @PathVariable("id") Long id, HttpServletResponse response) {
        publisher.publishEvent(new RecursoCriadoEvent(this, response, id));
        return ResponseEntity.ok(service.detalhar(id));
    }

}
