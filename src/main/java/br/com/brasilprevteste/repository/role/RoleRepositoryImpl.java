package br.com.brasilprevteste.repository.role;


import br.com.brasilprevteste.model.role.Role;
import br.com.brasilprevteste.model.role.Role_;
import br.com.brasilprevteste.repository.Filter.RoleFilter;
import br.com.brasilprevteste.repository.projection.RoleResumo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoleRepositoryImpl implements RoleRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public Page<Role> filtrar(RoleFilter roleFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Role> criteria = builder.createQuery(Role.class);
		Root<Role> root = criteria.from(Role.class);

		Predicate[] predicates = criarRestricoes(roleFilter, builder, root);
		criteria.where(predicates);

		TypedQuery<Role> query = manager.createQuery(criteria);
		adicionarRestricoesDePaginacao(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(roleFilter));
	}

	@Override
	public Page<RoleResumo> resumir(RoleFilter usuarioFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<RoleResumo> criteria = builder.createQuery(RoleResumo.class);
		Root<Role> root = criteria.from(Role.class);
		criteria.select(builder.construct(RoleResumo.class, root.get(Role_.id), root.get(Role_.nome)));
		Predicate[] predicates = criarRestricoes(usuarioFilter, builder, root);
		criteria.where(predicates);
		TypedQuery<RoleResumo> query = manager.createQuery(criteria);
		adicionarRestricoesDePaginacao(query, pageable);
		return new PageImpl<>(query.getResultList(), pageable, total(usuarioFilter));
	}

	private Predicate[] criarRestricoes(RoleFilter roleFilter, CriteriaBuilder builder, Root<Role> root) {
		List<Predicate> predicates = new ArrayList<>();
		if (StringUtils.isNotBlank(roleFilter.getNome())) {
			predicates.add(builder.like(builder.lower(root.get(Role_.nome)), "%" + roleFilter.getNome().toLowerCase() + "%"));
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

	private Long total(RoleFilter produtoFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Role> root = criteria.from(Role.class);
		Predicate[] predicates = criarRestricoes(produtoFilter, builder, root);
		criteria.where(predicates);
		criteria.select(builder.count(root));
		return manager.createQuery(criteria).getSingleResult();
	}

}
