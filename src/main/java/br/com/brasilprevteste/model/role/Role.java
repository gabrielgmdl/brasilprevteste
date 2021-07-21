package br.com.brasilprevteste.model.role;

import br.com.brasilprevteste.model.cliente.Cliente;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles", uniqueConstraints = { @UniqueConstraint(columnNames = { "nome" }) })
//@TableGenerator(name = "role_id", table = "sequencia_tabelas", pkColumnName = "tabela", valueColumnName = "identificador", pkColumnValue = "roles", allocationSize = 1)
public class Role implements GrantedAuthority {

	private static final long serialVersionUID = 1L;

//	@GeneratedValue(strategy = GenerationType.TABLE, generator = "role_id")
	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private Long id;

	@Column(name = "nome")
	@Size(min = 4, max = 100, message = "{produto.nome.size}")
	@NotNull(message = "{nome.null}")
	private String nome;

	@ManyToMany(mappedBy = "roles")
	@JsonBackReference("roles")
	private Set<Cliente> clientes;

	public Role(String nome) {
		this.nome = nome;
	}
	
	@Override
	@JsonIgnore
	public String getAuthority() {
		return this.nome;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
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
