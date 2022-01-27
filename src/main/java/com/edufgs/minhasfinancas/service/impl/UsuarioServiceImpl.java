package com.edufgs.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edufgs.minhasfinancas.exception.ErroAutenticacao;
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
	
	//Autentica usuario na hora do login
	@Override
	public Usuario autenticar(String email, String senha) {
		//Verifica se tem um email cadastrado
		Optional<Usuario> usuario =  repository.findByEmail(email);
		
		//Verifica se não está presente
		if(!usuario.isPresent()) {
			//Se não estiver presente então vai encadear um erro
			throw new ErroAutenticacao("Usuário não encontrado para o email informado.");
		}
		
		//Verifica a senha
		//get() = pega o objeto original
		//Se a senha não for igual
		if(!usuario.get().getSenha().equals(senha)){
			//senha não for igual então vai encadear um erro
			throw new ErroAutenticacao("Senha Inválida.");
		}
		
		//Se passsar nos dois testes então retorna a instancia do usuario
		return usuario.get();
	}

	@Override
	@Transactional //Cria na base de dados uma transação, executa o metodo de salvar o usuario quando pedir e depois que salvar vai commitar
	public Usuario salvarUsuario(Usuario usuario) {
		//primeiro valida o email para ver se ja está cadastrado		
		validarEmail(usuario.getEmail());
		//Depois salva no banco se não tiver cadastrado
		return repository.save(usuario);
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
