package br.com.brasilprevteste.repository.pedido;

import br.com.brasilprevteste.model.Pedido.Pedido;
import br.com.brasilprevteste.repository.Filter.PedidoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepositoryQuery {
    Page<Pedido> filtrar(PedidoFilter pedidoFilter, Pageable pageable);
}
