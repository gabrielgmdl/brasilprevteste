package br.com.brasilprevteste.repository.pedido;

import br.com.brasilprevteste.model.Pedido.Pedido;
import br.com.brasilprevteste.model.Pedido.Pedido_;
import br.com.brasilprevteste.repository.Filter.PedidoFilter;
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
public class PedidoRepositoryImpl implements PedidoRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Pedido> filtrar(PedidoFilter pedidoFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteria = builder.createQuery(Pedido.class);
        Root<Pedido> root = criteria.from(Pedido.class);

        Predicate[] predicates = criarRestricoes(pedidoFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Pedido> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(pedidoFilter));
    }

    private Predicate[] criarRestricoes(PedidoFilter pedidoFilter, CriteriaBuilder builder, Root<Pedido> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.isNotBlank(pedidoFilter.getDescricao())) {
            predicates.add(builder.like(builder.lower(root.get(Pedido_.descricao)), "%" + pedidoFilter.getDescricao().toLowerCase() + "%"));
        }
        if (pedidoFilter.getValorTotal() != null) {
            predicates.add(builder.equal(root.get(Pedido_.descricao), pedidoFilter.getValorTotal()));
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

    private Long total(PedidoFilter pedidoFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Pedido> root = criteria.from(Pedido.class);
        Predicate[] predicates = criarRestricoes(pedidoFilter, builder, root);
        criteria.where(predicates);
        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
