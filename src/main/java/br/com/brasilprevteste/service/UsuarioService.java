package br.com.brasilprevteste.service;


import br.com.brasilprevteste.model.cliente.Cliente;
import br.com.brasilprevteste.model.cliente.UsuarioTO;
import br.com.brasilprevteste.repository.Filter.UsuarioFilter;
import br.com.brasilprevteste.repository.projection.UsuarioResumo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface UsuarioService {
	Cliente atualizarUsuarioDTO(Long id, UsuarioTO usuario);

	Cliente salvar(Cliente cliente);

	Cliente atualizar(Long id, Cliente cliente);

	void atualizarPropriedadeEnabled(Long id, Boolean enabled);

	String findLoggedInLogin();

	Cliente deletar(Long id);

	Cliente detalhar(Long id);

	Page<Cliente> filtrar(UsuarioFilter usuarioFilter, Pageable pageable);
	
	Page<UsuarioResumo> resumir(UsuarioFilter usuarioFilter, Pageable pageable);
	
	Cliente findByNome(String username);
}
