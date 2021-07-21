package br.com.brasilprevteste.repository.usuario;

import br.com.brasilprevteste.model.cliente.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Cliente, Long>, UsuarioRepositoryQuery {

    Optional<Cliente> findByUsername(String username);

    List<Cliente> findByRoles(String nomeRole);

    List<Cliente> findByRolesNome(String permissaoDescricao);
}
