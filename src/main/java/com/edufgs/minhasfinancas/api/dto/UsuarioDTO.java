package com.edufgs.minhasfinancas.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/* É apenas uma classe que vai receber alguns atributos simples
 * É usado para ser preenchido e depois transformado em um objeto usuario e JSON
 * */

//Usando o Lombok em vez de digitar o set e get entre outros
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
	
	private String email;
	private String nome;
	private String senha;

}
