package com.edufgs.minhasfinancas.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


import com.edufgs.minhasfinancas.api.JwtTokenFilter;
import com.edufgs.minhasfinancas.service.JwtService;
import com.edufgs.minhasfinancas.service.impl.SecurityUserDetailsService;

/* Não precisa colocar o @Configuration //Para o Spring reconhecer como configuração, pois o @EnableWebSecurity ja tem um @Configuration */
@EnableWebSecurity //Adiciona o as configurações de segurança do spring boot
//Classe abstrata WebSecurityConfigurerAdapter ja tem alguns metodos que vem configurados com configuração padrão
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	//Injeta automatico pois lá na classe tem @Service //Diz para o container do Spring boot que gerencie essa classe. Então ele adiciona um container quando precisar.
	@Autowired
	private SecurityUserDetailsService userDetailsService;
	
	//Injeta automatico pois lá na classe tem @Service //Diz para o container do Spring boot que gerencie essa classe. Então ele adiciona um container quando precisar.
	@Autowired
	private JwtService jwtService;
	
	/*
	 * Toda vez que criar um objeto que queira que seja registrado no contexto do spring então é só anotar com @Bean dentro de uma classe de configuração (Tem que ter @
	 * */
	@Bean 
	public PasswordEncoder passwordEncoder() {
		/*
		 * BCryptPasswordEncoder é um dos algoritmos de encoder security e é um dos mais seguros que tem
		 * Toda vez que codificar uma string então vai gerar um hash diferente
		 * Existe outros algoritmo de codificação
		 * */
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder;
	}
	
	/*
	 * Vai fazer o filtro ser executado uma vez por requisição
	 * */
	@Bean //Para fazer parte do contexto Spring para poder interceptar
	public JwtTokenFilter jwtTokenFilter() {
		return new JwtTokenFilter(jwtService, userDetailsService);
	}
	
	/* Metodo para mudar o processo de login */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//Manda para a função paa codificar
		//String senhaCodificada = passwordEncoder().encode("qwe123");
		
		/*
		 * Faz a autenticação de usuarios mas na memorias
		 * Ele que busca os usuarios no banco de dados
		 * Agora com esse codigo a baixo o usuario e senha vai ser essa colocada, Usuario = usuario e senha = qwe123
		 * 
		auth
			.inMemoryAuthentication()
			.withUser("usuario") //Nome do usuario que pode logar
			.password(senhaCodificada) //Senha para logar (Precisa ser criptografada)
			.roles("USER"); //Padrão do spring 
		*/
		
		auth
			.userDetailsService(userDetailsService) //Outro forma de autenticação que é atravez de serviço de usuario que vai prover a autenticação
			.passwordEncoder(passwordEncoder()); //Faz a parte de verificação de senha
	}
	
	/* Metodo para mudar o processo de login */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable() // Desabilita a autenticação por formulario que era o padrão
			.authorizeRequests()
			/*
			 * antMatchers: recebe dois parametros: qual é o metodo http(post, put, etc) que vai ser configurado a permissão e qual URL para configurar a permissão. 
			 * permiteAll:  diz que qualquer pessoa pode fazer post sem estar autenticado
			 * Tb tem como colocar no lugar de "permiteAll" outras coisas como:
			 * 	hasRole("ADMIN") onde role é um grupo de usuaio ou perfil de usuario que pode acessar, está ali o perfil de ADMIN
			 * 	Ou hasAuthority("CADASTRAR_USUARIOS") Diz que qualquer pessoa com autoridade "CADASTRAR_USUARIOS" pode ter acesso a esse metodo e URL
			 * 	hasAnyRole("RH","ADMIN") onde varios grupo de usuaio ou perfil de usuario que pode acessar, está ali o perfil de ADMIN e RH mas pode ter varios
			 * */
			.antMatchers(HttpMethod.POST, "/api/usuarios/autenticar").permitAll() //Então diz que o metodo post usando a URL indicada não precisa de autenticação
			.antMatchers(HttpMethod.POST, "/api/usuarios").permitAll() //Então diz que o metodo post usando a URL indicada não precisa de autenticação
			.anyRequest().authenticated() //Diz que qualquer outra requisição precisa de autenticação
			.and() //Volta para o http novamente
				//Configura para que não guarde sessão de usuario em coockies				
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //Controle de sessão, criação de politica de sessão e assim toda a requisição que fizer agora tem que ter todos os elementos de sessão para acontecer 
			.and() //Volta para o http novamente
				.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class); //Coloca a chamada do filtro antes do filtro do Spring Security que faz a autenticação
	}
	
	/*
	 * Registra um filtro para o contexto do spring security
	 * */
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter(){
		
		List<String> all = Arrays.asList("*");
		
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedMethods(all);
		config.setAllowedOrigins(all);
		config.setAllowedHeaders(all);
		config.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		
		CorsFilter corFilter = new CorsFilter(source);
		
		FilterRegistrationBean<CorsFilter> filter = 
				new FilterRegistrationBean<CorsFilter>(corFilter);
		filter.setOrder(Ordered.HIGHEST_PRECEDENCE);
		
		return filter;
	}
		
}
