package br.com.brasilprevteste.model.cliente;


import br.com.brasilprevteste.model.role.Role;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Cliente.class)
public abstract class Cliente_ {
    public static volatile SingularAttribute<Cliente, Long> id;
    public static volatile SingularAttribute<Cliente, String> username;
    public static volatile SingularAttribute<Cliente, String> nomeCompleto;
    public static volatile SingularAttribute<Cliente, String> email;
    public static volatile SingularAttribute<Cliente, Boolean> enabled;
    public static volatile SingularAttribute<Cliente, String> foto;
    public static volatile SingularAttribute<Cliente, String> urlFoto;
    public static volatile SetAttribute<Cliente, Role> roles;
    public static volatile SingularAttribute<Cliente, String> password;
}