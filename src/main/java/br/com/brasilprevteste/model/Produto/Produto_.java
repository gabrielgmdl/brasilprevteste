package br.com.brasilprevteste.model.Produto;

import br.com.brasilprevteste.model.Pedido.Pedido;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Produto.class)
public class Produto_ {

    public static volatile SingularAttribute<Produto, Long> id;
    public static volatile SingularAttribute<Produto, String> codigo_de_barra;
    public static volatile SingularAttribute<Produto, String> preco;
    public static volatile SingularAttribute<Produto, Integer> estoque;
    public static volatile SingularAttribute<Produto, String> descricao;
    public static volatile ListAttribute<Produto, Pedido> pedidos;
}
