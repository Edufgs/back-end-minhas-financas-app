package com.edufgs.minhasfinancas.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/* Não precisa colocar o @Configuration //Para o Spring reconhecer como configuração, pois o @EnableWebSecurity ja tem um @Configuration */
@EnableWebSecurity //Adiciona o as configurações de segurança do spring boot
//Classe abstrata WebSecurityConfigurerAdapter ja tem alguns metodos que vem configurados com configuração padrão
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	/* Metodo para mudar o processo de login */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		super.configure(auth);
	}
	
	/* Metodo para mudar o processo de login */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable() // Desabilita a autenticação por formulario que era o padrão
			.authorizeRequests().anyRequest().authenticated() //Diz que qualquer requisição tem que estar autenticado
			.and() //Volta para o http novamente
				.httpBasic(); //Abilita a configuração http basic para teste no insomnia
	}
}
