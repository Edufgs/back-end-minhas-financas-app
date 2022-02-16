package com.edufgs.minhasfinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.edufgs.minhasfinancas.model.entity.Usuario;

//Anotação para teste
@ExtendWith(SpringExtension.class)
//@SpringBootTest //No começo foi usada essa notação para testar mas depois de um tempo não precisa mais. Essa notação faz com que o contexto do spring boot teste suba para essa classe.
@ActiveProfiles("test") //Seleciona o perfil do teste usando h2
@DataJpaTest //Essa notação faz cria uma instacia do banco na memoria e deleta depois que o teste é feito. Tambem depois de um teste acontece um rowback no banco
@AutoConfigureTestDatabase(replace = Replace.NONE) //Faz não cria uma instacia propria e não sobreescreve as configurações feitas no arquivo application.properties ou application-test.properties (A notação DataJpaTest faz sobreescrever as configurações feita)
public class UsuarioRepositoryTest {
	
	@Autowired //Injeta essa classe no contexto do spring boot
	UsuarioRepository repository; //Classe que quero testar
	
	
	/* EntityManager é a classe responsavel por fazer operações na base de dados. 
	 * O UsuarioRepository tem uma instacia do EntityManager. 
	 * Então vai ser usado o EntityManagar para manipular o cenario.
	 * Ele não é  oficial, ele é só um teste olhando a declaração TestEntityManager. Ele é configurado somente para teste.
	 * */
	@Autowired
	TestEntityManager entityManager; 
	
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
		Usuario usuario = criarUsuario();
		//Salva na base de dados
		entityManager.persist(usuario); //Parecido com repository.save(usuario);. É mais certo se fazer assim.
		
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
		//Não precia mais do repository.deleteAll(); para deletar tudo por causa do @DataJpaTest declarado lá em cima
		
		//acao
		boolean result = repository.existsByEmail("usuario@email.com");
		
		//verificacao
		//Assertions = vem junto com o modulo do spring boot
		//Verifica se o result é true
		//Se for false então o teste falha
		Assertions.assertThat(result).isFalse();
	}
	
	@Test
	public void devePersistirUsuarioNaBaseDeDados() {
		//cenario
		Usuario usuario = criarUsuario();
		
		//acao
		Usuario usuarioSalvo = repository.save(usuario);
		
		//Verificação
		//Verifica se é nulo o id
		//O usuario ganha o id depois de ser inserido no banco então está verificando se foi adicionando no banco
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	}
	
	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		//cenario
		Usuario usuario = criarUsuario();
		
		//Acao
		entityManager.persist(usuario); //Parecido com repository.save(usuario);. Não pode ter id se não vai lançar uma exceção
		
		//Verificação
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");
		
		//verifica se tem algo lá dentro então se tiver vai dar verdadeiro
		Assertions.assertThat(result.isPresent()).isTrue();
		
		
	}
	
	@Test
	public void deveRetornarVazioAoBuscarUmUsuarioPorEmailQuandoNaoExisteNaBase(){
		//cenario 
		
		//Acao
		
		//Verificação
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");
		
		//verifica se tem algo lá dentro então se não tiver vai dar false
		Assertions.assertThat(result.isPresent()).isFalse();
		
		
	}
	
	//metodo para criar um usuario com obejtivo de não repetir codigo
	public static Usuario criarUsuario() {
		//cenario
		//builder ajuda a constroir o objeto
		return Usuario.builder()
						.nome("Usuario")
						.email("usuario@email.com")
						.senha("senha")
						.build();
	}
}
