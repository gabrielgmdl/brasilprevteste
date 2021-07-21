package br.com.brasilprevteste.jdbc;

import br.com.brasilprevteste.model.role.Role;
import br.com.brasilprevteste.model.cliente.Cliente;
import br.com.brasilprevteste.repository.role.RoleRepository;
import br.com.brasilprevteste.service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.HashSet;

@Profile("teste")
@Configuration
class LoadDataBaseTeste {
//    @Bean
//    CommandLineRunner initTableRole(RoleRepository repository) {
//        return args -> {
//            repository.save(new Role("CADASTRAR_CLIENTE"));
//            repository.save(new Role("REMOVER_CLIENTE"));
//            repository.save(new Role("PESQUISAR_CLIENTE"));
//
//            repository.save(new Role("CADASTRAR_PRODUTO"));
//            repository.save(new Role("REMOVER_PRODUTO"));
//            repository.save(new Role("PESQUISAR_PRODUTO"));
//
//            repository.save(new Role("CADASTRAR_PEDIDO"));
//            repository.save(new Role("REMOVER_PEDIDO"));
//            repository.save(new Role("PESQUISAR_PEDIDO"));
//        };
//    }
//
//    @Bean
//    CommandLineRunner initTableUsuario(UsuarioService service, RoleRepository roleRepository) {
//        return args -> {
//            service.salvar(new Cliente("admin", "Gabriel Molina D. Leon", "oogmdl@gmail.com", "123321", "123321", true,
//                    new HashSet<>(roleRepository.findAll())));
//
//        };
//    }
}
