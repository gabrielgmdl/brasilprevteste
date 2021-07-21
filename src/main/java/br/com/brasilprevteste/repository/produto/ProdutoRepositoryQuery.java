package br.com.brasilprevteste.repository.produto;

import br.com.brasilprevteste.model.Produto.Produto;
import br.com.brasilprevteste.repository.Filter.ProdutoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepositoryQuery {
    Page<Produto> filtrar(ProdutoFilter campanhaFilter, Pageable pageable);
}
