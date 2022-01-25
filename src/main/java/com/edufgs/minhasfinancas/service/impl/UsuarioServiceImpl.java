package com.edufgs.minhasfinancas.service.impl;

import com.edufgs.minhasfinancas.model.entity.Usuario;
import com.edufgs.minhasfinancas.model.repository.UsuarioRepository;
import com.edufgs.minhasfinancas.service.UsuarioService;

/* Classe que implementa o usuario de acordo com a a interface */
public class UsuarioServiceImpl implements UsuarioService{
	
	//Como o usuario service não pode acessar o banco de dados diretamente então ele tem um usuario repository para isso.
	private UsuarioRepository repository;

	//Agora para usuario service funcionar, tem que construir com o usuario repository
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario salvarUsuario(Usuario usuario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validarEmail(String email) {
		// TODO Auto-generated method stub
		
	}

}
