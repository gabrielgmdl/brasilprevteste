package br.com.brasilprevteste.repository.role;

import br.com.brasilprevteste.model.role.Role;
import br.com.brasilprevteste.repository.Filter.RoleFilter;
import br.com.brasilprevteste.repository.projection.RoleResumo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepositoryQuery {
    Page<Role> filtrar(RoleFilter produtoFilter, Pageable pageable);

    Page<RoleResumo> resumir(RoleFilter roleFilter, Pageable pageable);
}
