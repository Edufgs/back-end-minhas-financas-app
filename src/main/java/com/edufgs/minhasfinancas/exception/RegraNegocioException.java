package com.edufgs.minhasfinancas.exception;

/* Lança um erro
 * Ela vai ser uma exceção de RunTimeException pq vai ser em tempo de execução e não precisa ser tratada onde foi lançada
 */
public class RegraNegocioException extends RuntimeException {
	
	//Construtor
	public RegraNegocioException(String msg) {
		super(msg);
	}
}
