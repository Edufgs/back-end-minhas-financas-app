package com.edufgs.minhasfinancas.model.repository;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.edufgs.minhasfinancas.model.entity.Usuario;

//Anotação para teste
@RunWith(SpringRunner.class)
@SpringBootTest //Faz com que o contexto do spring boot teste suba
@ActiveProfiles("test") //Seleciona o perfil do teste usando h2
public class UsuarioRepositoryTest {
	
	@Autowired //Injeta essa classe no contexto do spring boot
	UsuarioRepository repository; //Classe que quero testar
	
	/* Todo metodo do tipo teste tem retorno void
	 * Para fazer o teste precisa de tres elemento:
	 * Cenário,
	 * ação/execução e
	 * verificação
	 * */
	//Anotação para criar o teste
	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		//Cenario
		//builder ajuda a constroir o objeto 
		Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").build();
		//Salva na base de dados
		repository.save(usuario);
		
		//Ação/Execução
		boolean result = repository.existsByEmail("usuario@email.com");
		
		//Verificação
		//Assertions = vem junto com o modulo do spring boot
		//Verifica se o result é true
		//Se for false então o teste falha
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuariosCadastrados() {
		//cenario
		repository.deleteAll();
		
		//acao
		boolean result = repository.existsByEmail("usuario@email.com");
		
		//verificacao
		//Assertions = vem junto com o modulo do spring boot
		//Verifica se o result é true
		//Se for false então o teste falha
		Assertions.assertThat(result).isFalse();
	}
}
