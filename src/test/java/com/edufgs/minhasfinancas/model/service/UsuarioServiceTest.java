package com.edufgs.minhasfinancas.model.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.edufgs.minhasfinancas.exception.RegraNegocioException;
import com.edufgs.minhasfinancas.model.entity.Usuario;
import com.edufgs.minhasfinancas.model.repository.UsuarioRepository;
import com.edufgs.minhasfinancas.service.UsuarioService;

//Anotação para teste
@SpringBootTest //Faz com que o contexto do spring boot teste suba
@RunWith(SpringRunner.class)
@ActiveProfiles("test") //Seleciona o perfil do teste usando h2
public class UsuarioServiceTest {
	
	@Autowired //Injeta essa classe no contexto do spring boot
	UsuarioService service; //Interface que valida e testa os dados
	
	@Autowired //Injeta essa classe no contexto do spring boot
	UsuarioRepository repository; //Banco de dados
	
	/*(expected = Test.None.class) = Diz para o teste esperar que não lance nem uma exceção */
	@Test(expected = Test.None.class)
	public void deveValidarEmail() {
		//cenario
		//Exclui tudo do repositorio
		repository.deleteAll();
		
		//acao
		//Esse metodo retorna uma exceção se tiver um email cadastrado, por isso foi colocado (expected = Test.None.class) para verificar se retorna uma exceção.
		service.validarEmail("usuario@email.com");
	}
	
	/* Metodo que retorna um erro se tiver email cadastrado (Ao contrario do metodo de cima */
	@Test(expected = RegraNegocioException.class) // Diz para o teste esperar uma exceção RegraNegocioException
	public void deveRetornarErroAoValidarEmailQuandoExistirEmailCadastrado(){
		//cenario
		//builder ajuda a constroir o objeto
		Usuario usuario = Usuario.builder().nome("Usuario").email("email@email.com").build();
		repository.save(usuario); //Salva no banco
		
		//acao
		service.validarEmail("email@email.com");
	}
}
