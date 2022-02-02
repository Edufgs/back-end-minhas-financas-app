package com.edufgs.minhasfinancas.model.service;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;

import java.math.BigDecimal;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.edufgs.minhasfinancas.exception.RegraNegocioException;
import com.edufgs.minhasfinancas.model.entity.Lancamento;
import com.edufgs.minhasfinancas.model.entity.Usuario;
import com.edufgs.minhasfinancas.model.enums.StatusLancamento;
import com.edufgs.minhasfinancas.model.enums.TipoLancamento;
import com.edufgs.minhasfinancas.model.repository.LancamentoRepository;
import com.edufgs.minhasfinancas.model.repository.LancamentoRepositoryTeste;
import com.edufgs.minhasfinancas.service.impl.LancamentoServiceImpl;

//Anotação para teste
@RunWith(SpringRunner.class)
@ActiveProfiles("test") //Seleciona o perfil do teste usando h2
public class LancamentoServiceTest {
	//Ele chama o metodo real ao contrario o mock
	//Spy: Usa os metodos originais da classe diferente do Mock que não usa
	@SpyBean
	LancamentoServiceImpl service;
	
	//@Autowired //Injeta essa classe no contexto do spring boot. Não precisa mais pq vai ser feito testes unitarios e não precisa das instanncias oficiais
	@MockBean //Com essa anotação o spring boot faz automatico o processo de mock. Então agora ele cria uma instacia mockada.
	LancamentoRepository repository; //Banco de dados
	
	@Test
	//Testa a validação dos dados
	public void deveSalvarUmLancamento() {
		//Cenario
		Lancamento lancamentoASalvar = LancamentoRepositoryTeste.criarLancamento();
		
		//Não faça nada quando chamar o service.validar(lancamentoASalvar), assim não lanca erro
		Mockito.doNothing().when(service).validar(lancamentoASalvar);
		
		Lancamento lancamentoSalvo = LancamentoRepositoryTeste.criarLancamento();
		//Quando for persistido é ganhado um id então set id igual a 1
		lancamentoSalvo.setId(1l);
		//Adiciona o status PENDENTE
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		//Quando salvar algo no repository (repository.save(lancamentoASalvar)) então retorna o lancamentoSalvo
		Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);
		
		//acao
		//Manda salvar
		Lancamento lancamento = service.salvar(lancamentoASalvar);
		
		//Verificacao
		//Verifica se o lancamento salvo é igual ao lancamento recebido
		Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
		//Verifica o status
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
	}
	
	@Test
	//Testa a validação
	public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
		//Cenario
		Lancamento lancamentoASalvar = LancamentoRepositoryTeste.criarLancamento();
		//Vai lançar uma RegraNegocioException quando o service chamar o metodo validar
		Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoASalvar);
		
		//Acao e verificação
		//() -> = expressão lambida, função sem declaração, isto é, não é necessário colocar um nome, um tipo de retorno e o modificador de acesso
		//Verifia se vai dar o erro RegraNegocioException no metodo service.salvar
		Assertions.catchThrowableOfType( () -> service.salvar(lancamentoASalvar), RegraNegocioException.class);

		//Verifica se o Mockito não chegou no metodo .save(lancamentoASalvar no metodo service.salvar(lancamentoASalvar)
		Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
	}
	
	@Test
	//Testa o metodo atualizar
	public void deveAtualizarUmLancamento() {
		//Cenario
		Lancamento lancamentoSalvo = LancamentoRepositoryTeste.criarLancamento();
		//Quando for persistido é ganhado um id então set id igual a 1
		lancamentoSalvo.setId(1l);
		//Adiciona o status PENDENTE
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		
		//Não faça nada quando chamar o service.validar(lancamentoASalvar), assim não lanca erro
		Mockito.doNothing().when(service).validar(lancamentoSalvo);
		

		//Quando salvar algo no repository (repository.save(lancamentoASalvar)) então retorna o lancamentoSalvo
		Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);
		
		//acao
		//Manda salvar
		Lancamento lancamento = service.atualizar(lancamentoSalvo);
		
		//Verificacao
		//Verifique que o repository chamou 1 vez o metodo salvar passando lancamentoSalvo
		Mockito.verify(repository,Mockito.times(1)).save(lancamentoSalvo);
	}
	
	@Test
	public void deveLancarErroAoTentarAutalizarUmLancamentoQueAindaNcaoFoiSalvo() {
		//Cenario
		Lancamento lancamentoASalvar = LancamentoRepositoryTeste.criarLancamento();
		
		//Acao e verificação
		//() -> = expressão lambida, função sem declaração, isto é, não é necessário colocar um nome, um tipo de retorno e o modificador de acesso
		//Verifia se vai dar o erro NullPointerException no metodo service.salvar
		Assertions.catchThrowableOfType( () -> service.atualizar(lancamentoASalvar), NullPointerException.class);

		//Verifica se o Mockito não chegou no metodo .save(lancamentoASalvar no metodo service.salvar(lancamentoASalvar)
		Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
	}
	
	@Test
	public void deveDeletarLancamento() {
		//Cenario
		Lancamento lancamento = LancamentoRepositoryTeste.criarLancamento();
		lancamento.setId(1l);
		
		//Execucao
		service.deletar(lancamento);
		
		//Verificacao
		//Verifica se o objeto lancamento foi deletado do repository
		Mockito.verify(repository).delete(lancamento);
	}
	
	@Test
	public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo(){
		//Cenario
		Lancamento lancamento = LancamentoRepositoryTeste.criarLancamento();
				
		//Execucao e Verificação
		//() -> = expressão lambida, função sem declaração, isto é, não é necessário colocar um nome, um tipo de retorno e o modificador de acesso
		//Verifia se vai dar o erro NullPointerException no metodo service.deletar
		Assertions.catchThrowableOfType( () -> service.deletar(lancamento), NullPointerException.class);
				
		//Verifica se nunca foi chamado o metodo delete do repository
		Mockito.verify(repository, Mockito.never()).delete(lancamento);
	}
	
	@Test
	public void deveFiltrarLancamentos() {
		//Cenario
		Lancamento lancamento = LancamentoRepositoryTeste.criarLancamento();		
		lancamento.setId(1l);
		
		//Arrays.asList(lancamento, lancamento, lancamento) = Transforma varios obejtos em uma lista desses objetos
		List<Lancamento> lista = Arrays.asList(lancamento);
		
		//Quando chamar o repository.findAll passando qualquer Example, então retorna a lista
		Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);
		
		//Execucao
		List<Lancamento> resultado = service.buscar(lancamento);
		
		//verificação
		//Espera que traga o lancamento
		//Afirma que o resultado não está vazio, tem o size de 1 e contem lancamento
		Assertions
			.assertThat(resultado)
			.isNotEmpty()
			.hasSize(1)
			.contains(lancamento);
	}
	
	@Test
	public void deveAtualizarOStatusDeUmLancamento() {
		//Cenario
		Lancamento lancamento = LancamentoRepositoryTeste.criarLancamento();
		lancamento.setId(1l);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		
		StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
		//Retorna lancamento quando o metodo atualizar do service for chamado
		Mockito.doReturn(lancamento).when(service).atualizar(lancamento);
				
		//Execucao
		service.atualizarStatus(lancamento,novoStatus);
		
		//Verificação
		//Verifica se o status do lancamento é igual ao novo status
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
		//Verifica se chamoi o metodo atualizar no service
		Mockito.verify(service).atualizar(lancamento);	
		
	}
	
	@Test
	public void deveObterUmLancamentoPorId() {
		//Cenario
		Long id = 1l;
		Lancamento lancamento = LancamentoRepositoryTeste.criarLancamento();
		lancamento.setId(id);
		//Quando chamar o repository.findById(id) retorne lancamento
		Mockito.when(repository.findById(id)).thenReturn(Optional.of(lancamento));
		
		//Execucao
		Optional<Lancamento> resultado =  service.obterPorId(id);
		
		//Verificacao
		//Verifique que o resultado está presente assim sendo verdadeiro
		Assertions.assertThat(resultado.isPresent()).isTrue();
	}
	
	@Test
	public void deveRetornarVazioQuandoOLancamentoNaoExiste() {
		//Cenario
		Long id = 1l;
		Lancamento lancamento = LancamentoRepositoryTeste.criarLancamento();
		lancamento.setId(id); 
		//Quando chamar o repository.findById(id) retorne lancamento
		Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
		
		//Execucao
		Optional<Lancamento> resultado =  service.obterPorId(id);
		
		//Verificacao
		//Verifique que o resultado está presente assim sendo falso (Então vai estar vazio)
		Assertions.assertThat(resultado.isPresent()).isFalse();
	}
	
	@Test
	public void deveLancarErrosAoValidarUmlancamento() {
		//Cenario, execucao e verificação tudo junto por cada verificação
		
		//=========================================  Verifica se a descrição é valida  =========================================
		Lancamento lancamento = new Lancamento();
		
		//Verifica se é null
		
		//Capitura o erro
		Throwable erro = Assertions.catchThrowable( () -> service.validar(lancamento));		
		//Verifica se o erro é uma instancia de RegraNegocioException com a mensagem "Informe uma Descrição válida."
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Descrição válida.");
		
		//Adiciona a descrição mas vazio
		lancamento.setDescricao("");
		
		//Verifica se está vazio
		
		//Capitura o erro
		erro = Assertions.catchThrowable( () -> service.validar(lancamento));		
		//Verifica se o erro é uma instancia de RegraNegocioException com a mensagem "Informe uma Descrição válida."
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Descrição válida.");
		//=======================================================================================================================
		
		//Adiciona a descricao para verificar o erro do mes
		lancamento.setDescricao("Descrição 1");
		
		//===========================================  Verifica se o mes é valido  ==============================================
		
		//Verifica se é null
		
		//Capitura o erro
		erro = Assertions.catchThrowable( () -> service.validar(lancamento));
		//Verifica se o erro é uma instancia de RegraNegocioException com a mensagem "Informe um Mês válido."
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");
		
		//Adiciona mes menor que 1
		lancamento.setMes(0);
		
		//Verifica se é < 1
		
		//Capitura o erro
		erro = Assertions.catchThrowable( () -> service.validar(lancamento));
		//Verifica se o erro é uma instancia de RegraNegocioException com a mensagem "Informe um Mês válido."
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");
		
		//Adiciona mes maior que 12
		lancamento.setMes(13);
				
		//Verifica se é > 12
				
		//Capitura o erro
		erro = Assertions.catchThrowable( () -> service.validar(lancamento));
		//Verifica se o erro é uma instancia de RegraNegocioException com a mensagem "Informe um Mês válido."
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");
		//=======================================================================================================================
		
		//Adiciona o mes para verificar o erro do ano
		lancamento.setMes(10);
		
		//============================================  Verifica se o ano é valido  =============================================
		
		//Verifica se é null
		
		//Capitura o erro
		erro = Assertions.catchThrowable( () -> service.validar(lancamento));
		//Verifica se o erro é uma instancia de RegraNegocioException com a mensagem "Informe um Ano válido."
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido.");
		
		//Adiciona um ano com menos ou mais de 4 digitos
		lancamento.setAno(202);
		
		//Verifica se o ano tem menos ou mais de 4 digitos
		
		//Capitura o erro
		erro = Assertions.catchThrowable( () -> service.validar(lancamento));
		//Verifica se o erro é uma instancia de RegraNegocioException com a mensagem "Informe um Ano válido."
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido.");
		//=======================================================================================================================
		
		//Adiciona um ano valido para verificar o mes
		lancamento.setAno(2020);
		
		//===========================================  Verifica se o usuario é valido  ==========================================
		
		//Verifica se é null
		
		//Capitura o erro
		erro = Assertions.catchThrowable( () -> service.validar(lancamento));
		//Verifica se o erro é uma instancia de RegraNegocioException com a mensagem "Informe um Ano válido."
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário.");
		
		//Adiciona um usuario sem id
		Usuario usuario = Usuario.builder().build();
		lancamento.setUsuario(usuario);
		
		//Verifica se o usuario tem id
		
		//Capitura o erro
		erro = Assertions.catchThrowable( () -> service.validar(lancamento));
		//Verifica se o erro é uma instancia de RegraNegocioException com a mensagem "Informe um Ano válido."
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário.");
		//=======================================================================================================================
		
		//Adiciona um id para o usuario
		usuario.setId(1l);
		
		//==========================================  Verifica se o valor é valido  =============================================
		
		//Verifica se é null
		
		//Capitura o erro
		erro = Assertions.catchThrowable( () -> service.validar(lancamento));
		//Verifica se o erro é uma instancia de RegraNegocioException com a mensagem "Informe um Ano válido."
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido.");
		
		//Adiciona o valor de 0
		lancamento.setValor(BigDecimal.valueOf(0));
		
		//Verifica o valor é menor que 0
		
		//Capitura o erro
		erro = Assertions.catchThrowable( () -> service.validar(lancamento));
		//Verifica se o erro é uma instancia de RegraNegocioException com a mensagem "Informe um Ano válido."
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido.");
		
		//=======================================================================================================================
		
		//Adiciona o valor para verificar o tipo lancamento
		lancamento.setValor(BigDecimal.valueOf(50));
				
		//=========================================  Verifica se o tipo lancamento é valido  ===================================
		
		//Verifica se é null
		
		//Capitura o erro
		erro = Assertions.catchThrowable( () -> service.validar(lancamento));
		//Verifica se o erro é uma instancia de RegraNegocioException com a mensagem "Informe um Ano válido."
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Tipo de Lançamento.");
		//=======================================================================================================================
		
		//Adiciona o tipo lacamento verificar se tudo está valido
		lancamento.setTipo(TipoLancamento.DESPESA);;
						
		//============================================  Verifica se tudo está valido  ==========================================
						
		//Capitura o erro
		erro = Assertions.catchThrowable( () -> service.validar(lancamento));
		//Verifica se o erro é vazio, se não der nenhum erro então passa no teste
		Assertions.assertThat(erro).isNull();
		//=======================================================================================================================
		
	}
}
