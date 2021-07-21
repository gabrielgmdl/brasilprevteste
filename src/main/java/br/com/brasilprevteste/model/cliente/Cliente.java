package br.com.brasilprevteste.model.cliente;

import br.com.brasilprevteste.model.View;
import br.com.brasilprevteste.model.audit.UserDateAudit;
import br.com.brasilprevteste.model.role.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clientes", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
//@TableGenerator(name = "cliente_id", table = "sequencia_tabelas", pkColumnName = "tabela", valueColumnName = "identificador", pkColumnValue = "clientes", allocationSize = 1)
public class Cliente extends UserDateAudit implements UserDetails {

    private static final long serialVersionUID = 1L;

    public Cliente(Long id){
        this.id = id;
    }
    public Cliente(String username, String nomeCompleto, String email, String password, String confirmarPassword,
                   Boolean enabled, Set<Role> roles) {
        this.username = username;
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.password = password;
        this.confirmarPassword = confirmarPassword;
        this.enabled = enabled;
        this.roles = roles;
    }

//    @GeneratedValue(strategy = GenerationType.TABLE, generator = "cliente_id")
    @Id
    @Column(name = "id")
    @JsonView(View.Usuario.class)
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

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

    @Column(name = "password")
    @Size(min = 6, max = 150, message = "{senha.size}")
    private String password;

    @Transient
    private String confirmarPassword;

    @Column(name = "enabled")
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("clientes")
    @JoinTable(name = "roles_cliente", joinColumns = @JoinColumn(name = "cliente_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Cliente other = (Cliente) obj;
        if (id == null) {
            return other.id == null;
        } else return id.equals(other.id);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
