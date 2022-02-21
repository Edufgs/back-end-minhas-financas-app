package com.edufgs.minhasfinancas.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.edufgs.minhasfinancas.model.entity.Usuario;
import com.edufgs.minhasfinancas.model.repository.UsuarioRepository;

@Service //Diz para o container do Spring boot que gerencie essa classe. Então ele adiciona um container quando precisar.
public class SecurityUserDetailsService implements UserDetailsService {
	
	private UsuarioRepository usuarioRepository;

	public SecurityUserDetailsService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
		
	}

	//Carrega o usuario pelo nome login
	//Pode usar qualquer repositorio de usuarios então pode estar em um arquivo txt, planilha
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuarioEncontrado = usuarioRepository
					.findByEmail(email)
					//Se não encontrar o usuario pelo email então lança um erro UsernameNotFoundException com a mensagem
					.orElseThrow( () -> new UsernameNotFoundException("Email não encontrado"));
		
		//Agora tem que retornar um UserDetails então:
		return User.builder()
				.username(usuarioEncontrado.getEmail()) //Passa o email
				.password(usuarioEncontrado.getSenha()) //Passa a senha
				.roles("USER") //Passa o perfil de usuario, está sendo usado o usuaio padrão do Spring Boot
				.build();
	}
	
}
