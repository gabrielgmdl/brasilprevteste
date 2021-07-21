package br.com.brasilprevteste.service.impl;

import br.com.brasilprevteste.model.cliente.Cliente;
import br.com.brasilprevteste.model.cliente.UsuarioSistemaTO;
import br.com.brasilprevteste.repository.usuario.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j // TODO: criar um log para os métodos
@Service
public class UsuarioDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Cliente> usuarioOptional = usuarioRepository.findByUsername(username);
		Cliente cliente = usuarioOptional.orElseThrow(() -> new UsernameNotFoundException("Usuário ou senha incorretos"));
		if (!cliente.isEnabled()) {
			throw new UserDeniedAuthorizationException("Usuário inativo");
		}
		return new UsuarioSistemaTO(cliente, getRoles(cliente));
		
	}
	
	private Collection<? extends GrantedAuthority> getRoles(Cliente cliente) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		cliente.getAuthorities().forEach(p -> authorities.add(new SimpleGrantedAuthority(p.getAuthority().toUpperCase())));
		return authorities;
	}

}
