package br.com.brasilprevteste.service.impl;

import br.com.brasilprevteste.errorValidate.ErroMessage;
import br.com.brasilprevteste.model.Pedido.Pedido;
import br.com.brasilprevteste.model.Produto.Produto;
import br.com.brasilprevteste.repository.Filter.PedidoFilter;
import br.com.brasilprevteste.repository.pedido.PedidoRepository;
import br.com.brasilprevteste.repository.produto.ProdutoRepository;
import br.com.brasilprevteste.repository.usuario.UsuarioRepository;
import br.com.brasilprevteste.service.PedidoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j // TODO: criar um log para os métodos
@Service
public class PedidoServiceImpl extends ErroMessage implements PedidoService {

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Override
    public Page<Pedido> filtrar(PedidoFilter pedidoFilter, Pageable pageable) {
        return repository.filtrar(pedidoFilter, pageable);
    }

    @Override
    public Pedido salvar(Pedido pedido) {
        List<Produto> produtos = new ArrayList<>();
        pedido.setCliente(usuarioRepository.findById(pedido.getCliente().getId()).orElse(null));
        pedido.getProdutos().forEach(produto -> {
            Produto p = produtoRepository.findById(produto.getId()).orElse(null);
            if (p != null) {
                if(p.getEstoque() > 0) {
                    pedido.setValorTotal(pedido.getValorTotal() + p.getPreco());
                    produtos.add(p);
                    p.setEstoque(p.getEstoque() - 1);
                    produtoRepository.save(p);
                } else {
                    otherMensagemBadRequest("Não temos esta quantidade de estoque. Pedido acima do estoque disponível");
                }
            }
        });
        pedido.setProdutos(produtos);
        return repository.save(pedido);
    }

    @Override
    public Pedido atualizar(Long id, Pedido pedido) {
        Pedido campanhaSalvo = repository.findById(id).orElseThrow(() -> notFouldId(id, "o pedido"));
        BeanUtils.copyProperties(pedido, campanhaSalvo, "id");
        return repository.save(campanhaSalvo);
    }

    @Override
    public Pedido deletar(Long id) {
        Pedido campanhaSalvo = repository.findById(id).orElseThrow(() -> notFouldId(id, "o pedido"));
        repository.deleteById(id);
        return campanhaSalvo;
    }

    @Override
    public Pedido detalhar(Long id) {
        return repository.findById(id).orElseThrow(() -> notFouldId(id, "o pedido"));
    }
}
