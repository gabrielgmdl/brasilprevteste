package br.com.brasilprevteste.errorValidate;

public abstract class ErroMessage{
	
	protected ResourceBadRequestException notFouldId(Long id, String nome) {
		return new ResourceBadRequestException("Não foi possivel encontrar "+nome+" com o id: " + id);
	}
	
	protected ResourceNotFoundException notFould(String nome) {
		return new ResourceNotFoundException("Não foi possivel encontrar "+nome);
	}
	
	protected ResourceNotFoundException notFouldError() {
		return new ResourceNotFoundException("Erro diverso, favor entrar em contato com o administrador da ferramenta.");
	}
	
	protected ResourceNotFoundException otherMensagemNotFound(String msg) {
		return new ResourceNotFoundException(msg);
	}
	protected ResourceBadRequestException otherMensagemBadRequest(String msg) {
		return new ResourceBadRequestException(msg);
	}
	
}
