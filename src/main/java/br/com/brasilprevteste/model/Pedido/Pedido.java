package br.com.brasilprevteste.model.Pedido;

import br.com.brasilprevteste.model.Produto.Produto;
import br.com.brasilprevteste.model.audit.UserDateAudit;
import br.com.brasilprevteste.model.cliente.Cliente;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pedidos")
//@TableGenerator(name = "pedido_id", table = "sequencia_tabelas", pkColumnName = "tabela", valueColumnName = "identificador", pkColumnValue = "pedidos", allocationSize = 1)
public class Pedido extends UserDateAudit {


    //@GeneratedValue(strategy = GenerationType.TABLE, generator = "pedido_id")
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @NotNull(message = "{cliente.isNull}")
    private Cliente cliente;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("pedidos")
    @JoinTable(name = "pedido_produto", joinColumns = @JoinColumn(name = "pedido_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "produto_id", referencedColumnName = "id"))
    private List<Produto> produtos;

    private Double valorTotal;

    @NotNull
    private LocalDateTime data_pedido;

    private String descricao;
}
