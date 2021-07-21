package br.com.brasilprevteste.service.impl;

import br.com.brasilprevteste.errorValidate.ErroMessage;
import br.com.brasilprevteste.model.role.Role;
import br.com.brasilprevteste.repository.Filter.RoleFilter;
import br.com.brasilprevteste.repository.projection.RoleResumo;
import br.com.brasilprevteste.repository.role.RoleRepository;
import br.com.brasilprevteste.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ErroMessage implements RoleService {

    @Autowired
    private RoleRepository repository;

    @Override
    public Page<Role> filtrar(RoleFilter roleFilter, Pageable pageable) {
        return repository.filtrar(roleFilter, pageable);
    }

    @Override
    public Page<RoleResumo> resumir(RoleFilter roleFilter, Pageable pageable) {
        return repository.resumir(roleFilter, pageable);
    }

}
