package com.edufgs.minhasfinancas.api;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.edufgs.minhasfinancas.service.JwtService;
import com.edufgs.minhasfinancas.service.impl.SecurityUserDetailsService;

/*
 * Filtro é um interceptador que vai receber a requisição antes de chegar no Spring Security
 * Nessa requisição vai ser pego o token, decodificar ele e fazer a validação
 * Se tiver tudo ok vai ser pego o usuario que fizer essa requisição e lançar dentro do contexto do spring security, contexto de autenticação
 * */

public class JwtTokenFilter extends OncePerRequestFilter {

	private JwtService jwtService;
	private SecurityUserDetailsService userDetailsService;

	public JwtTokenFilter (
			JwtService jwtService, //Vai ser usado para autenticar o token
			SecurityUserDetailsService userDetailsService
			) {
				this.jwtService = jwtService;
				this.userDetailsService = userDetailsService;
	}
	
	/* Metodo que intercepta a requisição */
	@Override
	protected void doFilterInternal(
			HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain filterChain)
			throws ServletException, IOException {
		//Pegou o header autorization
		String authorization = request.getHeader("Authorization");
		
		/*
		 * Como vai ser utilizado um token de autorização do tipo Bearer (antes era Basic) então vai ser mandado um token como um padrão
		 * O padrão é: Bearer eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2NDUxODk2MjAsIm5vbWUiOiJVc3VhcmlvIDEiLCJob3JhRXhwaXJhY2FvIjoiMDk6MDciLCJzdWIiOiJ1c3VhcmlvMUBlbWFpbC5jb20ifQ.f_jR-vyGScRW8DaRwL_GpNh3dHSEDgXkPwg08LFCQm5qm0nWBLcRqgogmGYercy7PezPBh93YmE7AAxqw9Mtrw
		 * Assim verifica se o token não está nulo ou se ele começa com Bearer que é do padrão
		 * */
		if(authorization != null && authorization.startsWith("Bearer")) {
			//Separa em array apartir do espaço. Onde tiver espaço vai ser retirado esse espaço e separado em partes de um array
			String token = authorization.split(" ")[1];
			//verifica se o token é valido
			boolean isTokenValid = jwtService.isTokenValido(token);
			
			if(isTokenValid) {
				//recuoera o login do usuario que é o email
				String login = jwtService.obterLoginUsuario(token);
				//Carrega o usuario no banco com as permissoes
				UserDetails usuarioAutenticado = userDetailsService.loadUserByUsername(login);
				//Vai pegar o UserDetails e transformar em um token de autenticação do Spring Security
				UsernamePasswordAuthenticationToken user = 
						new UsernamePasswordAuthenticationToken (
								usuarioAutenticado, //Qual é o usuario que está logado (Principal)
								null, //Credenciais
								usuarioAutenticado.getAuthorities()); //Autorizações
				//Faz a configuração
				//Como está sendo criado uma autorização dentro do Spring security então é exigido que passe dentro das interfaces e manda construir
				user.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				//Pega o contexto do Spring Security e joga na autenticação para quando chegar no AuthenticationManagerBuilder na classe SecurityConfiguration vai estar com as credenciais para fazer as validações do usuario 
				SecurityContextHolder.getContext().setAuthentication(user);
			}
		}
		
		//Da continuidade a execução da requisição pois é interceptado e manda continuar o resto da requisição
		filterChain.doFilter(request, response);
	}

}
