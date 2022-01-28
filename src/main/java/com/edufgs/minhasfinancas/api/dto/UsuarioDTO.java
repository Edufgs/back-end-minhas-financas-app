package com.edufgs.minhasfinancas.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/* É apenas uma classe que vai receber alguns atributos simples
 * É usado para ser preenchido e depois transformado em um objeto usuario
 * */

//Usando o Lombok em vez de digitar o set e get entre outros
@Getter
@Setter
@Builder
public class UsuarioDTO {
	
	private String email;
	private String nome;
	private String senha;

}
