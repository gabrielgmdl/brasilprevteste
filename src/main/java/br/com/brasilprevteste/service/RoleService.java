package br.com.brasilprevteste.service;

import br.com.brasilprevteste.model.role.Role;
import br.com.brasilprevteste.repository.Filter.RoleFilter;
import br.com.brasilprevteste.repository.projection.RoleResumo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {
    Page<Role> filtrar(RoleFilter roleFilter, Pageable pageable);

    Page<RoleResumo> resumir(RoleFilter roleFilter, Pageable pageable);
}
