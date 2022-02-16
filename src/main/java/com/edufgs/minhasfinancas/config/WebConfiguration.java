package com.edufgs.minhasfinancas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc //Serve para abilibar o recebimento de requisições de outro site que é o site do front-ends
@Configuration //Para o Spring reconhecer como configuração
public class WebConfiguration implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		/* addMapping("/**") = Diz qual URL da api vai ser acessado
		 * .allowedOrigins(null) = qual URL vai vir as requisições
		 * .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") = diz qual os metodos utilizado nas requisições como POST, PUT, entre outros
		 * */
		registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
	}
	
}
