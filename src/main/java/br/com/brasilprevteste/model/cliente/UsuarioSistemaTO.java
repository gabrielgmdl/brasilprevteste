package br.com.brasilprevteste.model.cliente;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UsuarioSistemaTO extends User {

	private static final long serialVersionUID = 1L;

	private Cliente username;

	public UsuarioSistemaTO(Cliente username, Collection<? extends GrantedAuthority> authorities) {
		super(username.getUsername(), username.getPassword(), authorities);
		this.username = username;
	}

	public Cliente getUsuario() {
		return username;
	}
}