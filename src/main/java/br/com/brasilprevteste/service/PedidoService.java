package br.com.brasilprevteste.service;

import br.com.brasilprevteste.model.Pedido.Pedido;
import br.com.brasilprevteste.repository.Filter.PedidoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface PedidoService {
    Pedido salvar(Pedido pedido);

    Pedido atualizar(Long id, Pedido pedido);

    Pedido deletar(Long id);

    Pedido detalhar(Long id);

    Page<Pedido> filtrar(PedidoFilter pedidoFilter, Pageable pageable);
}
