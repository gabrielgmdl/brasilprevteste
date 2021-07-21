package br.com.brasilprevteste.repository.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResumo {
	private Long id;
	private String username;
	private String nomeCompleto;
	private String email;
	private Boolean enabled;
}
