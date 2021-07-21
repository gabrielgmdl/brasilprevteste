package br.com.brasilprevteste.repository.usuario;

import br.com.brasilprevteste.model.cliente.Cliente;
import br.com.brasilprevteste.repository.Filter.UsuarioFilter;
import br.com.brasilprevteste.repository.projection.UsuarioResumo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositoryQuery {

	Page<Cliente> filtrar(UsuarioFilter usuarioFilter, Pageable pageable);

	Page<UsuarioResumo> resumir(UsuarioFilter usuarioFilter, Pageable pageable);
}
