package br.com.brasilprevteste.model.cliente;

import br.com.brasilprevteste.model.role.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioTO {

    @Column(name = "username", unique = true)
    @NotNull(message = "{login.null}")
    @Size(min = 4, max = 32, message = "{login.size}")
    private String username;

    @Column(name = "nome_completo")
    @Size(min = 4, max = 50, message = "{nome.size}")
    private String nomeCompleto;

    @Column(name = "email")
    @Email(message = "{email.not.valid}")
    @NotBlank(message = "{email.not.blank}")
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @Valid
    @JsonIgnoreProperties("usuarios")
    @JoinTable(name = "roles_usuarios", joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;
}
