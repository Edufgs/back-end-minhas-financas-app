package com.edufgs.minhasfinancas.model.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.edufgs.minhasfinancas.model.entity.Lancamento;
import com.edufgs.minhasfinancas.model.enums.StatusLancamento;
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
	public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
		
	}
}
