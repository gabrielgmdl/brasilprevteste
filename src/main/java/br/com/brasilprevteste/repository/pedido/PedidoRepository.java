package br.com.brasilprevteste.repository.pedido;


import br.com.brasilprevteste.model.Pedido.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long>, PedidoRepositoryQuery {

}
