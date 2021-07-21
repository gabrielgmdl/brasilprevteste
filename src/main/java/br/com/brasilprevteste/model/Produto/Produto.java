package br.com.brasilprevteste.model.Produto;

import br.com.brasilprevteste.model.Pedido.Pedido;
import br.com.brasilprevteste.model.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "produtos", uniqueConstraints = {@UniqueConstraint(columnNames = {"codigo_de_barra"})})
//@TableGenerator(name = "produto_id", table = "sequencia_tabelas", pkColumnName = "tabela", valueColumnName = "identificador", pkColumnValue = "produtos", allocationSize = 1)
public class Produto extends UserDateAudit {

//    @GeneratedValue(strategy = GenerationType.TABLE, generator = "produto_id")
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;
    @NotNull
    private String codigo_de_barra;
    @NotNull
    private Double preco;
    @NotNull
    private Integer estoque;
    @NotNull
    private String descricao;

    @ManyToMany(mappedBy = "produtos")
    @JsonBackReference("produtos")
    private List<Pedido> pedidos;
}
