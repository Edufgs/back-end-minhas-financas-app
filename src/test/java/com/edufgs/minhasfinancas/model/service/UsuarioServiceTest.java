package com.edufgs.minhasfinancas.model.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.edufgs.minhasfinancas.exception.RegraNegocioException;
import com.edufgs.minhasfinancas.model.repository.UsuarioRepository;
import com.edufgs.minhasfinancas.service.UsuarioService;
import com.edufgs.minhasfinancas.service.impl.UsuarioServiceImpl;

//Anotação para teste
//@SpringBootTest //No começo foi usada essa notação para testar mas depois de um tempo não precisa mais. Essa notação faz com que o contexto do spring boot teste suba para essa classe.
@RunWith(SpringRunner.class)
@ActiveProfiles("test") //Seleciona o perfil do teste usando h2
public class UsuarioServiceTest {
	
	//@Autowired //Injeta essa classe no contexto do spring boot. Não precisa mais pq vai ser feito testes unitarios e não precisa das instanncias oficiais
	UsuarioService service; //Interface que valida e testa os dados
	
	//@Autowired //Injeta essa classe no contexto do spring boot. Não precisa mais pq vai ser feito testes unitarios e não precisa das instanncias oficiais
	@MockBean //Com essa anotação o spring boot faz automatico o processo de mock. Então agora ele cria uma instacia mockada.
	UsuarioRepository repository; //Banco de dados
	
	//Metodo para configurar os testes
	@Before //Notação faz executar antes dos testes
	public void setUp() {
		//Classe mock onde ela cria instanncias fake e simula que chamou algum metodo 
		//repository = Mockito.mock(UsuarioRepository.class); //Não precisa mais dessa linha pois oi adicionado a notação @MockBean no atributo repository onde faz esse processo automatico. Então ele vai simular os metodos para não precisar executar os reais
		
		//instanncia real do UsuarioService passando o repository mokado
		service = new UsuarioServiceImpl(repository);
	}
	
	/*(expected = Test.None.class) = Diz para o teste esperar que não lance nem uma exceção */
	@Test(expected = Test.None.class)
	public void deveValidarEmail() {
		//cenario
		//Classe mock onde ela cria instancias fake e simula que chamou algum metodo 
		//UsuarioRepository usuarioRepositoryMock = Mockito.mock(UsuarioRepository.class); //Então ele vai simular os metodos para não precisar executar os reais
		
		//Quando fizer a chamada para o mocki repository o metodo existsByEmail passando qualquer string não interessando a string passada vai dar return false
		//Resumindo: Quando chamar o metodo repository.existsByEmail passando qualquer string como parametro vai retornar false 
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);		
		
		//Exclui tudo do repositorio
		//repository.deleteAll();
		
		//acao
		//Esse metodo retorna uma exceção se tiver um email cadastrado, por isso foi colocado (expected = Test.None.class) para verificar se retorna uma exceção.
		service.validarEmail("usuario@email.com");
	}
	
	/* Metodo que retorna um erro se tiver email cadastrado (Ao contrario do metodo de cima */
	@Test(expected = RegraNegocioException.class) // Diz para o teste esperar uma exceção RegraNegocioException
	public void deveRetornarErroAoValidarEmailQuandoExistirEmailCadastrado(){
		//cenario
		//builder ajuda a constroir o objeto
		//Usuario usuario = Usuario.builder().nome("Usuario").email("email@email.com").build();
		//repository.save(usuario); //Salva no banco
		
		//Simulando com o mock
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		//acao
		service.validarEmail("email@email.com");
	}
}
