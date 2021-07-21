package br.com.brasilprevteste.repository.usuario;

import br.com.brasilprevteste.model.cliente.Cliente;
import br.com.brasilprevteste.model.cliente.Cliente_;
import br.com.brasilprevteste.repository.Filter.UsuarioFilter;
import br.com.brasilprevteste.repository.projection.UsuarioResumo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepositoryImpl implements UsuarioRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public Page<Cliente> filtrar(UsuarioFilter usuarioFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Cliente> criteria = builder.createQuery(Cliente.class);
		Root<Cliente> root = criteria.from(Cliente.class);

		Predicate[] predicates = criarRestricoes(usuarioFilter, builder, root);
		criteria.where(predicates);

		TypedQuery<Cliente> query = manager.createQuery(criteria);
		adicionarRestricoesDePaginacao(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(usuarioFilter));
	}

	@Override
	public Page<UsuarioResumo> resumir(UsuarioFilter usuarioFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<UsuarioResumo> criteria = builder.createQuery(UsuarioResumo.class);
		Root<Cliente> root = criteria.from(Cliente.class);
		criteria.select(builder.construct(UsuarioResumo.class, root.get(Cliente_.id), root.get(Cliente_.username), root.get(Cliente_.nomeCompleto), root.get(Cliente_.email), root.get(Cliente_.enabled)));
		Predicate[] predicates = criarRestricoes(usuarioFilter, builder, root);
		criteria.where(predicates);
		TypedQuery<UsuarioResumo> query = manager.createQuery(criteria);
		adicionarRestricoesDePaginacao(query, pageable);
		return new PageImpl<>(query.getResultList(), pageable, total(usuarioFilter));
	}

	private Predicate[] criarRestricoes(UsuarioFilter usuarioFilter, CriteriaBuilder builder, Root<Cliente> root) {
		List<Predicate> predicates = new ArrayList<>();
		if (StringUtils.isNotBlank(usuarioFilter.getNomeCompleto())) {
			predicates.add(builder.like(builder.lower(root.get(Cliente_.nomeCompleto)), "%" + usuarioFilter.getNomeCompleto().toLowerCase() + "%"));
		}
		if (StringUtils.isNotBlank(usuarioFilter.getUsername())) {
			predicates.add(builder.like(builder.lower(root.get(Cliente_.username)), "%" + usuarioFilter.getUsername().toLowerCase() + "%"));
		}
		if (StringUtils.isNotBlank(usuarioFilter.getEmail())) {
			predicates.add(builder.like(builder.lower(root.get(Cliente_.email)), "%" + usuarioFilter.getEmail().toLowerCase() + "%"));
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

	private Long total(UsuarioFilter usuarioFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Cliente> root = criteria.from(Cliente.class);
		Predicate[] predicates = criarRestricoes(usuarioFilter, builder, root);
		criteria.where(predicates);
		criteria.select(builder.count(root));
		return manager.createQuery(criteria).getSingleResult();
	}

}
