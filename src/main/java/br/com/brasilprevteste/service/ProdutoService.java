package br.com.brasilprevteste.service;

import br.com.brasilprevteste.model.Produto.Produto;
import br.com.brasilprevteste.repository.Filter.ProdutoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ProdutoService {
    Produto salvar(Produto produto);

    Produto atualizar(Long id, Produto produto);

    Produto deletar(Long id);

    Produto detalhar(Long id);

    Page<Produto> filtrar(ProdutoFilter produtoFilter, Pageable pageable);
}
