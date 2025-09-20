package br.com.nca.domain.exceptions;

public class RecursoNaoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = -9068014459575335929L;

	@Override
	public String getMessage() {
		return "Recurso não encontrado";
	}
}
