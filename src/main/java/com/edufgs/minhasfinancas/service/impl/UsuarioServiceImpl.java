package com.edufgs.minhasfinancas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edufgs.minhasfinancas.exception.RegraNegocioException;
import com.edufgs.minhasfinancas.model.entity.Usuario;
import com.edufgs.minhasfinancas.model.repository.UsuarioRepository;
import com.edufgs.minhasfinancas.service.UsuarioService;

/* Classe que implementa o usuario de acordo com a a interface */
@Service //Diz para o container do Spring boot que gerencie essa classe. Então ele adiciona um container quando precisar.
public class UsuarioServiceImpl implements UsuarioService{
	
	//Como o usuario service não pode acessar o banco de dados diretamente então ele tem um usuario repository para isso.
	private UsuarioRepository repository;

	//Agora para usuario service funcionar, tem que construir com o usuario repository
	@Autowired //Cria uma instacia e coloca automatico no repository. Então automatico ele prove as dependencia no construtor
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
		
		//Busca no repositorio algum com esse email
		boolean existe = repository.existsByEmail(email);
		
		if(existe) {
			//Lança uma exceção
			throw new RegraNegocioException("Já existe um usuario cadastrado com este email");
		}
	}

}
