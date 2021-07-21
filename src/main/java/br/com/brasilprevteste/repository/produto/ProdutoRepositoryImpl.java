package br.com.brasilprevteste.repository.produto;

import br.com.brasilprevteste.model.Produto.Produto;
import br.com.brasilprevteste.model.Produto.Produto_;
import br.com.brasilprevteste.repository.Filter.ProdutoFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProdutoRepositoryImpl implements ProdutoRepositoryQuery{

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Produto> filtrar(ProdutoFilter produtoFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteria = builder.createQuery(Produto.class);
        Root<Produto> root = criteria.from(Produto.class);

        Predicate[] predicates = criarRestricoes(produtoFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Produto> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(produtoFilter));
    }

    private Predicate[] criarRestricoes(ProdutoFilter produtoFilter, CriteriaBuilder builder, Root<Produto> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.isNotBlank(produtoFilter.getDescricao())) {
            predicates.add(builder.like(builder.lower(root.get(Produto_.descricao)), "%" + produtoFilter.getDescricao().toLowerCase() + "%"));
        }
        if (produtoFilter.getEstoque() != null) {
            predicates.add(builder.equal((root.get(Produto_.estoque)), produtoFilter.getEstoque()));
        }
        if (produtoFilter.getPreco() != null) {
            predicates.add(builder.equal((root.get(Produto_.preco)), produtoFilter.getPreco()));
        }
        if (produtoFilter.getEstoque() != null) {
            predicates.add(builder.equal((root.get(Produto_.estoque)), produtoFilter.getEstoque()));
        }
        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
        query.setFirstResult(primeiroRegistroDaPagina);
        query.setMaxResults(totalRegistrosPorPagina);
    }

    private Long total(ProdutoFilter produtoFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Produto> root = criteria.from(Produto.class);
        Predicate[] predicates = criarRestricoes(produtoFilter, builder, root);
        criteria.where(predicates);
        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
