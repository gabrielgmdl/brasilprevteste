package br.com.brasilprevteste.controller;

import br.com.brasilprevteste.model.role.Role;
import br.com.brasilprevteste.repository.Filter.RoleFilter;
import br.com.brasilprevteste.repository.projection.RoleResumo;
import br.com.brasilprevteste.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/roles")
class RoleEndPoint {

    @Autowired
    private final RoleService service;

    @GetMapping(params = "resumo")
    @PreAuthorize("hasAuthority('PESQUISAR_USUARIO') and #oauth2.hasScope('read')")
    public Page<RoleResumo> resumir(RoleFilter roleFilter, Pageable pageable) {
        return service.resumir(roleFilter, pageable);
    }

    @GetMapping(value = {""})
    @PreAuthorize("hasAuthority('PESQUISAR_USUARIO') and #oauth2.hasScope('read')")
    public Page<Role> pesquisar(RoleFilter usuarioFilter, Pageable pageable) {
        return service.filtrar(usuarioFilter, pageable);
    }
}
