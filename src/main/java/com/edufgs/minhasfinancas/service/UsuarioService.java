package com.edufgs.minhasfinancas.service;

import java.util.Optional;

import com.edufgs.minhasfinancas.model.entity.Usuario;

public interface UsuarioService{
	
	//Autentica usuarios
	/* Vai no banco e verifica se tem alguem cadastrado com o email.
	 * Se tiver então verifica a senha. 
	 * Se for arpovado então retorna uma instacia de usuario altenticado*/
	Usuario autenticar(String email, String senha);
	
	/* Salva o usuario no banco de dados */
	Usuario salvarUsuario(Usuario usuario);
	
	/* Verifica se tem o email cadastrado uma vez no banco */
	void validarEmail(String email);
	
	//Busca um usuario por id
	Optional<Usuario> obterPorId(Long id);
	
}
