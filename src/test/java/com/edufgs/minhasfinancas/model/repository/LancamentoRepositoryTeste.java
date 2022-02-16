package com.edufgs.minhasfinancas.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*; //Deixa static todos metodos desse classe

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.edufgs.minhasfinancas.model.entity.Lancamento;
import com.edufgs.minhasfinancas.model.enums.StatusLancamento;
import com.edufgs.minhasfinancas.model.enums.TipoLancamento;

//Anotação para teste
@ExtendWith(SpringExtension.class)
@DataJpaTest //Essa notação faz cria uma instacia do banco na memoria e deleta depois que o teste é feito. Tambem depois de um teste acontece um rowback no banco
@AutoConfigureTestDatabase(replace = Replace.NONE) //Faz não cria uma instacia propria e não sobreescreve as configurações feitas no arquivo application.properties ou application-test.properties (A notação DataJpaTest faz sobreescrever as configurações feita)
@ActiveProfiles("test") //Seleciona o perfil do teste usando h2
public class LancamentoRepositoryTeste {
	
	@Autowired //Injeta essa classe no contexto do spring boot
	LancamentoRepository repository; //Classe que quero testar
	
	@Autowired
	/* EntityManager é a classe responsavel por fazer operações na base de dados. 
	 * O UsuarioRepository tem uma instacia do EntityManager. 
	 * Então vai ser usado o EntityManagar para manipular o cenario.
	 * Ele não é  oficial, ele é só um teste olhando a declaração TestEntityManager. Ele é configurado somente para teste.
	 * */
	TestEntityManager entityManager; 
	
	/* Todo metodo do tipo teste tem retorno void
	 * Para fazer o teste precisa de tres elemento:
	 * Cenário,
	 * ação/execução e
	 * verificação
	 * */
	//Anotação para criar o teste
	@Test
	//Testa Salvar um lancamento
	public void deveSalvarUmLancamento() {
		//Cenario
		Lancamento lancamento = criarLancamento();
		
		//Acao
		lancamento = repository.save(lancamento);
		
		//Verificacao
		assertThat(lancamento.getId()).isNotNull(); //Não tem o Assertions. pois eu importei todos os metodos static do Assertion lá na importação
	}

	
	
	@Test
	//Testa o deletar um lancamento
	public void deveDeletarUmLancamento() {
		//Cenario
		Lancamento lancamento = criaEPersistirUmLancamento();
		//entityManager.find = Onde recebe a classe que vai ser buscado e o id
		lancamento = entityManager.find(Lancamento.class, lancamento.getId());
		
		//Acao
		//Deleta
		repository.delete(lancamento);
		//Busca o mesmo lancamento mas depois de deletar
		Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());
		
		//Verificação
		//Verifica se é nulo
		assertThat(lancamentoInexistente).isNull(); //Não tem o Assertions. pois eu importei todos os metodos static do Assertion lá na importação
	}

	
	
	@Test
	public void deveAtualizarUmLancamento() {
		//Cenario
		Lancamento lancamento = criaEPersistirUmLancamento();
		
		//Acao
		lancamento.setAno(2018);
		lancamento.setDescricao("Teste atualizar");
		lancamento.setStatus(StatusLancamento.CANCELADO);
		
		//Atualiza o lancamento que ja está na base
		repository.save(lancamento);
		
		Lancamento lancamentoAtualizado = entityManager.find(Lancamento.class, lancamento.getId());
		
		//Verifica
		//Verifica de o ano é igual ao atualizado
		assertThat(lancamentoAtualizado.getAno()).isEqualTo(2018); //Não tem o Assertions. pois eu importei todos os metodos static do Assertion lá na importação
		//Verifica se a descrição é igual ao atulizado
		assertThat(lancamentoAtualizado.getDescricao()).isEqualTo("Teste atualizar"); //Não tem o Assertions. pois eu importei todos os metodos static do Assertion lá na importação
		//Verifica de o status é igual ao atualizado
		assertThat(lancamentoAtualizado.getStatus()).isEqualTo(StatusLancamento.CANCELADO); //Não tem o Assertions. pois eu importei todos os metodos static do Assertion lá na importação
	}
	
	@Test
	public void deveBuscaUmLancamentoPorId() {
		//Cenario
		Lancamento lancamento = criaEPersistirUmLancamento();
		
		//Acao
		//Busca por id
		Optional<Lancamento> lancamentoEncontrado = repository.findById(lancamento.getId());
		
		//Verificação
		//Verifica se foi encontrado
		assertThat(lancamentoEncontrado.isPresent()).isTrue(); //Não tem o Assertions. pois eu importei todos os metodos static do Assertion lá na importação	
		
	}
	
	//Cria um lancamento
	public static Lancamento criarLancamento() {
		return Lancamento.builder()
				.ano(2019)
				.mes(2)
				.descricao("Lancamento qualquer")
				.valor(BigDecimal.valueOf(10))
				.tipo(TipoLancamento.RECEITA)
				.status(StatusLancamento.PENDENTE)
				.dataCadastro(LocalDate.now())
				.build();
	}
	
	//Cria e Persiste um lancamento
	private Lancamento criaEPersistirUmLancamento() {
		Lancamento lancamento = criarLancamento();
		//Retorna o lancamento salvo
		lancamento = entityManager.persist(lancamento);
		//Busca o lancamento que foi persistido
		return lancamento;
	}
}
