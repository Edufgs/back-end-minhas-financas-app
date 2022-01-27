package com.edufgs.minhasfinancas.model.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.edufgs.minhasfinancas.exception.ErroAutenticacao;
import com.edufgs.minhasfinancas.exception.RegraNegocioException;
import com.edufgs.minhasfinancas.model.entity.Usuario;
import com.edufgs.minhasfinancas.model.repository.UsuarioRepository;
import com.edufgs.minhasfinancas.service.impl.UsuarioServiceImpl;

//Anotação para teste
//@SpringBootTest //No começo foi usada essa notação para testar mas depois de um tempo não precisa mais. Essa notação faz com que o contexto do spring boot teste suba para essa classe.
@RunWith(SpringRunner.class)
@ActiveProfiles("test") //Seleciona o perfil do teste usando h2
public class UsuarioServiceTest {
	
	//@Autowired //Injeta essa classe no contexto do spring boot. Não precisa mais pq vai ser feito testes unitarios e não precisa das instanncias oficiais
	//UsuarioService service; //Interface que valida e testa os dados
	
	//Ele chama o metodo real ao contrario o mock
	//Spy: Usa os metodos originais da classe diferente do Mock que não usa
	@SpyBean
	UsuarioServiceImpl service;
	
	//@Autowired //Injeta essa classe no contexto do spring boot. Não precisa mais pq vai ser feito testes unitarios e não precisa das instanncias oficiais
	@MockBean //Com essa anotação o spring boot faz automatico o processo de mock. Então agora ele cria uma instacia mockada.
	UsuarioRepository repository; //Banco de dados
	
	//Metodo para configurar os testes
	/* Não precisa mais desse metodo
	@Before //Notação faz executar antes dos testes
	public void setUp() {
		//Classe mock onde ela cria instanncias fake e simula que chamou algum metodo 
		//repository = Mockito.mock(UsuarioRepository.class); //Não precisa mais dessa linha pois oi adicionado a notação @MockBean no atributo repository onde faz esse processo automatico. Então ele vai simular os metodos para não precisar executar os reais
		
		//Agora vai receber um spy
		//service = Mockito.spy(UsuarioServiceImpl.class);
		
		//instanncia real do UsuarioService passando o repository mokado
		//service = new UsuarioServiceImpl(repository);
	}*/
	
	@Test(expected = Test.None.class) //Espera que não lance erro nesse teste
	public void deveSalvarUsuario() {
		//Cenario
		//doNothing() diz para não fazer nada, não da nenhum erro no metodo validarEmail passando qualquer string (anyString)
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder()
				.id(1l)//1l pois é só numero long
				.nome("Nome")
				.email("email@email.com")
				.senha("senha")
				.build();
		//Diz que quando passar para salvar qualquer usuario então vai ser retornado o usuario de cima
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		//Acao
		//Não importa o usuario passado pois como diz a cima, vai ser retornado o usuario criado em cima
		//Lembrando que o salvarUsuario retorna o usuario salvo
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		
		//Verificacao
		//verifica se o usuario esta nulo
		Assertions.assertThat(usuarioSalvo).isNotNull();
		//Verifica o id se é igual o digitado
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
		//Verifica o nome se é igual o digitado
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("Nome");
		//Verifica se o email é igual ao email digitado
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
		//Verifica se a senha é igual a digitada
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
		
	}
	
	@Test(expected = RegraNegocioException.class)  // Diz para o teste esperar uma exceção/erro
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
		//Cenario
		String email = "email@email.com";
		Usuario usuario = Usuario.builder().email(email).build();
		//Quando chamar o service validaEmail jogue uma RegraNegocioException
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		
		//Acao
		service.salvarUsuario(usuario);
		
		//Verificacao
		//Metodo verify recebe o metodo que nunca deve ser chamado
		//Então ele verifica se o metodo repository.save(usuario) nunca foi chamado
		Mockito.verify(repository,Mockito.never()).save(usuario);
		
	}
	
	/*(expected = Test.None.class) = Diz para o teste esperar que não lance nem uma exceção */
	@Test(expected = Test.None.class)
	public void deveAutenticarUsuarioComSucesso() {
		//Cenario
		String email = "email@email.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).build();
		
		//Classe mock onde ela cria instancias fake e simula que chamou algum metodo
		//Quando usar o comando repository.findByEmail(email) então vai retornar o usuario a cima
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		//Acao
		//Usa a classe UsuarioServiceImpl para testar o autentificador
		Usuario result = service.autenticar(email, senha);
		
		//Verificação
		//Verifica se o result não é null
		Assertions.assertThat(result).isNotNull();
	}
	
	@Test//Não precisa mais de (expected = ErroAutenticacao.class), pois vai ser testado com Assertion. // Diz para o teste esperar uma exceção
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComEmailInformado() {
		
		//Cenario
		//Não importa o email passado ele sempte vai retorna vazio
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		//Acao
		Throwable exception = Assertions.catchThrowable( () -> service.autenticar("email@email.com", "senha"));
		
		//Verifiar
		//Esta veificando se acontece um erro na notação @Test(expected = ErroAutenticacao.class) // Diz para o teste esperar uma exceção RegraNegocioException
		//Agora vai ser verifiacdo com Assertions
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Usuário não encontrado para o email informado.");
		
	}
	
	@Test //Não precisa mais de (expected = ErroAutenticacao.class), pois vai ser testado com Assertion. // Diz para o teste esperar uma exceção
	public void deveLancarErroQuandoSenhaNaoBater() {
		//Cenario
		String senha = "senha";		
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();		
		//Não importa o email passado ele sempte vai retornar o usuario a cima
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		//Acao
		//Assertions.catchThrowable capitura o erro lançado e retorna
		//() -> = O Assertions.catchThrowable espera a chamada de um metodo então usa a expressão lambda
		Throwable exception = Assertions.catchThrowable( () -> service.autenticar("email@email.com", "123"));
		
		//Verificacao
		//Verifica se o erro a cima "exception" pe instaciado pelo "ErroAutenticacao" com a mensagem "Senha Inválida."
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha Inválida.");
		
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
