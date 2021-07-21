package br.com.brasilprevteste.controller;

import br.com.brasilprevteste.event.RecursoCriadoEvent;
import br.com.brasilprevteste.model.cliente.Cliente;
import br.com.brasilprevteste.model.cliente.UsuarioTO;
import br.com.brasilprevteste.repository.Filter.UsuarioFilter;
import br.com.brasilprevteste.repository.projection.UsuarioResumo;
import br.com.brasilprevteste.service.UsuarioService;
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
@RequestMapping("/usuarios")
class UsuarioEndPoint {

    @Autowired
    private final UsuarioService service;

    @Autowired
    private final ApplicationEventPublisher publisher;

    @GetMapping
    @PreAuthorize("hasAuthority('PESQUISAR_CLIENTE') and #oauth2.hasScope('read')")
    public Page<Cliente> pesquisar(UsuarioFilter usuarioFilter, Pageable pageable) {
        return service.filtrar(usuarioFilter, pageable);
    }

    @GetMapping(params = "resumo")
    @PreAuthorize("hasAuthority('PESQUISAR_CLIENTE') and #oauth2.hasScope('read')")
    public Page<UsuarioResumo> resumir(UsuarioFilter usuarioFilter, Pageable pageable) {
        return service.resumir(usuarioFilter, pageable);
    }

    @PostMapping(value = {""})
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('CADASTRAR_CLIENTE') and #oauth2.hasScope('write')")
    ResponseEntity<Cliente> cadastrar(@Valid @RequestBody Cliente cliente, HttpServletResponse response) {
        Cliente clienteSalvar = service.salvar(cliente);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, clienteSalvar.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvar);
    }

    @GetMapping(value = {"/{id}"})
    @PreAuthorize("hasAuthority('PESQUISAR_CLIENTE') and #oauth2.hasScope('read')")
    public ResponseEntity<Cliente> detalhar(@Valid @PathVariable("id") Long id) {
        return ResponseEntity.ok(service.detalhar(id));
    }

    @PutMapping(value = {"/{id}"})
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('CADASTRAR_CLIENTE') and #oauth2.hasScope('write')")
    ResponseEntity<Cliente> atualizar(@Valid @RequestBody Cliente cliente, @PathVariable Long id, HttpServletResponse response) {
        Cliente novaCliente = service.atualizar(id, cliente);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, novaCliente.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(novaCliente);
    }

    @DeleteMapping(value = {"/{id}"})
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('REMOVER_CLIENTE') and #oauth2.hasScope('write')")
    public ResponseEntity<Cliente> remover(@PathVariable Long id) {
        return ResponseEntity.ok(service.deletar(id));
    }

    @PutMapping(value = {"/{id}/ativo"})
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('CADASTRAR_CLIENTE') and #oauth2.hasScope('write')")
    public ResponseEntity<Cliente> atualizarPropriedadeEnabled(@PathVariable Long id, @RequestBody Boolean enabled) {
        service.atualizarPropriedadeEnabled(id, enabled);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = {"/username/{username}"})
    @PreAuthorize("hasAuthority('PESQUISAR_CLIENTE') and #oauth2.hasScope('read')")
    ResponseEntity<Cliente> encontrarPeloNome(@Valid @PathVariable String username) {
        return ResponseEntity.ok(service.findByNome(username));
    }

    @PutMapping(value = {"/{id}/atualizar"})
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('CADASTRAR_CLIENTE') and #oauth2.hasScope('write')")
    ResponseEntity<Cliente> atualizarUsuarioDTO(@PathVariable Long id, @RequestBody UsuarioTO usuario, HttpServletResponse response) {
        Cliente novoCliente = service.atualizarUsuarioDTO(id, usuario);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, novoCliente.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
    }

}
