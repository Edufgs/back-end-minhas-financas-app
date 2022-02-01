package com.edufgs.minhasfinancas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//Anotação para trazer aglomerados de outras anotações para aplicar as configurações do Spring boot
@SpringBootApplication
public class MinhasfinancasApplication {
	
	public static void main(String[] args) {
		//Aplica as configuração para subira a aplicação
		SpringApplication.run(MinhasfinancasApplication.class, args);
	}

}
