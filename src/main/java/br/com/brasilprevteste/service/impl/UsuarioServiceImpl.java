package br.com.brasilprevteste.service.impl;

import br.com.brasilprevteste.errorValidate.ErroMessage;
import br.com.brasilprevteste.model.cliente.Cliente;
import br.com.brasilprevteste.model.cliente.UsuarioTO;
import br.com.brasilprevteste.repository.Filter.UsuarioFilter;
import br.com.brasilprevteste.repository.projection.UsuarioResumo;
import br.com.brasilprevteste.repository.usuario.UsuarioRepository;
import br.com.brasilprevteste.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Slf4j // TODO: criar um log para os métodos
@Service
public class UsuarioServiceImpl extends ErroMessage implements UsuarioService {

	@Autowired
	UsuarioRepository repository;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public Cliente atualizarUsuarioDTO(Long id, UsuarioTO usuario) {
		Cliente clienteSalva = repository.findById(id).orElseThrow(() -> notFouldId(id, "o usuario"));
		clienteSalva.getRoles().clear();
		clienteSalva.getRoles().addAll(usuario.getRoles());
		BeanUtils.copyProperties(usuario, clienteSalva, "id", "roles");
		return repository.save(clienteSalva);
	}

	@Override
	public Cliente salvar(Cliente cliente) {
		if (!cliente.getConfirmarPassword().equals(cliente.getPassword()))
			throw otherMensagemBadRequest("O campo de senha não corresponde com o confirmar senha.");
		else if (repository.findByUsername(cliente.getUsername()).isPresent())
			throw otherMensagemBadRequest("O nome de usuario já existe.");
		cliente.setPassword(bCryptPasswordEncoder.encode(cliente.getPassword()));
		cliente.setRoles(new HashSet(cliente.getRoles()));
		return repository.save(cliente);
	}

	@Override
	public Cliente atualizar(Long id, Cliente cliente) {
		Cliente clienteSalva = repository.findById(id).orElseThrow(() -> notFouldId(id, "o usuario"));
		cliente.setPassword(bCryptPasswordEncoder.encode(cliente.getPassword()));
		clienteSalva.getRoles().clear();
		clienteSalva.getRoles().addAll(cliente.getRoles());
		clienteSalva.setRoles(new HashSet(clienteSalva.getRoles()));
		BeanUtils.copyProperties(cliente, clienteSalva, "id", "roles");
		return repository.save(clienteSalva);
	}

	@Override
	public void atualizarPropriedadeEnabled(Long id, Boolean enabled) {
		Cliente clienteSalva = repository.findById(id).orElseThrow(() -> notFouldId(id, "a produto"));
		clienteSalva.setEnabled(enabled);
		repository.save(clienteSalva);
	}

	@Override
	public String findLoggedInLogin() {
		Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
		if (userDetails instanceof UserDetails) {
			return ((UserDetails) userDetails).getUsername();
		}
		return null;
	}

	@Override
	public Page<Cliente> filtrar(UsuarioFilter usuarioFilter, Pageable pageable) {
		return repository.filtrar(usuarioFilter, pageable);
	}

	@Override
	public Cliente deletar(Long id) {
		Cliente clienteSalvo = repository.findById(id).orElseThrow(() -> notFouldId(id, "a produto"));
		repository.deleteById(id);
		return clienteSalvo;
	}

	@Override
	public Cliente detalhar(Long id) {
		return repository.findById(id).orElseThrow(() -> notFouldId(id, "a produto"));
	}

	@Override
	public Page<UsuarioResumo> resumir(UsuarioFilter usuarioFilter, Pageable pageable) {
		return repository.resumir(usuarioFilter, pageable);
	}

	@Override
	public Cliente findByNome(String username) {
		return repository.findByUsername(username).orElseThrow(() -> notFould("o usuario"));
	}
}