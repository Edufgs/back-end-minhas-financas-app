package com.edufgs.minhasfinancas.exception;

/* Lança um erro
 * Ela vai ser uma exceção de RunTimeException pq vai ser em tempo de execução e não precisa ser tratada onde foi lançada
 */
public class ErroAutenticacao extends RuntimeException{
	
	public ErroAutenticacao(String msg) {
		super(msg);
	}
}
