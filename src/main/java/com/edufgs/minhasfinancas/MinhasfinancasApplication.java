package com.edufgs.minhasfinancas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//Anotação para trazer aglomerados de outras anotações para aplicar as configurações do Spring boot
@SpringBootApplication
@EnableWebMvc //Serve para abilibar o recebimento de requisições de outro site que é o site do front-end
public class MinhasfinancasApplication implements WebMvcConfigurer{ //É preciso implementar o WebMvcConfigurer para abilibar o recebimento de requisições de outro site que é o site do front-end
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		/* addMapping("/**") = Diz qual URL da api vai ser acessado
		 * .allowedOrigins(null) = qual URL vai vir as requisições
		 * .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") = diz qual os metodos utilizado nas requisições como POST, PUT, entre outros
		 * */
		registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
	}
	
	public static void main(String[] args) {
		//Aplica as configuração para subira a aplicação
		SpringApplication.run(MinhasfinancasApplication.class, args);
	}

}
