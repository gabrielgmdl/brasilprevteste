package br.com.brasilprevteste.model.Pedido;

import br.com.brasilprevteste.model.Produto.Produto;
import br.com.brasilprevteste.model.cliente.Cliente;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Pedido.class)
public class Pedido_ {
    public static volatile SingularAttribute<Pedido, Long> id;
    public static volatile SingularAttribute<Pedido, Cliente> usuario;
    public static volatile ListAttribute<Pedido, Produto> produtos;
    public static volatile SingularAttribute<Pedido, Date> data_pedido;
    public static volatile SingularAttribute<Pedido, String> descricao;
    public static volatile SingularAttribute<Pedido, Double> valorTotal;
}
