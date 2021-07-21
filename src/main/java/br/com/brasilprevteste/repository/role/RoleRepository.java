package br.com.brasilprevteste.repository.role;


import br.com.brasilprevteste.model.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long>, RoleRepositoryQuery {
}
