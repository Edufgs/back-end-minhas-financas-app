package com.edufgs.minhasfinancas.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/*
 * Retorna a informação do token para o usuario
 * */

//Notação do lombok onde adiciona gets, sets e construtor com todos os argumentos
@Getter
@Setter
@AllArgsConstructor
public class TokenDTO {
	
	private String nome;
	private String Token;
	
}
