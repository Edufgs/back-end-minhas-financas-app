package com.edufgs.minhasfinancas.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edufgs.minhasfinancas.exception.RegraNegocioException;
import com.edufgs.minhasfinancas.model.entity.Lancamento;
import com.edufgs.minhasfinancas.model.enums.StatusLancamento;
import com.edufgs.minhasfinancas.model.enums.TipoLancamento;
import com.edufgs.minhasfinancas.model.repository.LancamentoRepository;
import com.edufgs.minhasfinancas.service.LancamentoService;

/* Classe que implementa o usuario de acordo com a a interface */
@Service //Diz para o container do Spring boot que gerencie essa classe. Então ele adiciona um container quando precisar.
public class LancamentoServiceImpl implements LancamentoService {
	
	//Como o lancamento service nao pode acessar o banco de dados diretamente então ele tem um lancamento repository para isso.
	private LancamentoRepository repository;
	
	/* Agora para lancamento service funcionar, tem que construir com o lancamento repository
	 * nao pricesa de @Autowired pois ele ja injeta com o @Service em cima da classe e como o construtor tem LancamentoRepository repository e o @Service é bean gerenciado então ja faz automatico
	 * @Autowired //Cria uma instacia e coloca automatico no repository. Então automatico ele prove as dependencia no construtor
	 * */
	public LancamentoServiceImpl(LancamentoRepository repository) {
		this.repository = repository;
	}
	
	@Override
	@Transactional //Cria na base de dados uma transação, executa o metodo de salvar o usuario quando pedir e depois que salvar vai commitar. Se acontecer algum erro é feito o rollback.
	public Lancamento salvar(Lancamento lancamento) {
		//Valida os dados do lancamento
		validar(lancamento);
		//Automatico o status fica PENDENTE
		lancamento.setStatus(StatusLancamento.PENDENTE);
		return repository.save(lancamento); //O metodo save salva e atualiza, se nao tiver id então ele salva e se tiver id então é atualizado o dados.
	}

	@Override
	@Transactional //Cria na base de dados uma transação, executa o metodo de salvar o usuario quando pedir e depois que salvar vai commitar. Se acontecer algum erro é feito o rollback.
	public Lancamento atualizar(Lancamento lancamento) {	
		//O id nao pode ser nulo se nao vai dar erro
		Objects.requireNonNull(lancamento.getId());
		//Valida os dados do lancamento
		validar(lancamento);
				
		return repository.save(lancamento); //O metodo save salva e atualiza, se nao tiver id então ele salva e se tiver id então é atualizado o dados.
	}

	@Override
	@Transactional //Cria na base de dados uma transação, executa o metodo de salvar o usuario quando pedir e depois que salvar vai commitar. Se acontecer algum erro é feito o rollback.
	public void deletar(Lancamento lancamento) {
		//O id nao pode ser nulo se nao vai dar erro pois o metodo deletar so pode deletar lancamentos que nao tem na base de dados.
		Objects.requireNonNull(lancamento.getId());
		
		repository.delete(lancamento); //So pode deletar lancamentos que nao tem na base de dados 		
	}

	@Override
	/* Cria na base de dados uma transação, executa o metodo de salvar o usuario quando pedir e depois que salvar vai commitar. Se acontecer algum erro é feito o rollback.
	 * (readOnly = true) diz que é feita somente leitura e o Spring faz umas otimizacoes.
	 * */
	@Transactional(readOnly = true)
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		/* Vai pegar uma instacia do objeto lancamentoFiltro, depois passar o objeto Example para o repository e assim acontece a consulta baseado no objeto Example
		 * Example.of faz uma preparacao com objeto lancamentoFiltro para retirar oq é null.
		 * ExampleMatcher.matching() faz algumas configuracoes a mais
		 * withIgnoreCase() ignora caixa alta ou caixa baixa.
		 * StringMatcher.CONTAINING busca mesmo se colocar so um pouco escrito
		 * */
		Example example = Example.of( lancamentoFiltro,ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING) );
		
		return repository.findAll(example);
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		//Muda o status de lancamento
		lancamento.setStatus(status);
		
		//Manda para o metodo atualizar
		atualizar(lancamento);
	}

	@Override
	public void validar(Lancamento lancamento) {
		//Se lancamento for vazio ou nulo entao lanca um erro
		if(lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
			throw new RegraNegocioException("Informe uma Descrição válida.");
		}
		
		//Verifica se o mes esta nulo ou é menor que 1 ou maior que 12
		if(lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12) {
			throw new RegraNegocioException("Informe um Mês válido.");
		}
		
		//Verifica se o ano é nulo ou se é diferente de 4 digitos
		if(lancamento.getAno() == null || lancamento.getAno().toString().length() != 4) {
			throw new RegraNegocioException("Informe um Ano válido.");
		}
		
		//verifica se tem usuario selecionado
		if(lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null) {
			throw new RegraNegocioException("Informe um Usuário.");
		}
		
		/* Verifica de o valor é nulo e se o valor é negativo
		 * Como é do tipo BigDecimal é preciso usar o metodo .compareTo(BigDecimal.ZERO) onde compara com o numero zero
		 * Se for maior que zero o compareTo retorna 1,
		 * Se for igual que zero o compareTo retorna 0 e
		 * Se for menor que zero o compareTo retorna -1
		 * */
		if(lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {
			throw new RegraNegocioException("Informe um Valor válido.");
		}
		
		//Verifica se é vazio pois só tem dois tipo de lancamento
		if(lancamento.getTipo() == null) {
			throw new RegraNegocioException("Informe um Tipo de Lançamento.");
		}
		
	}

	@Override
	public Optional<Lancamento> obterPorId(Long id) {	
		return repository.findById(id);
	}

	@Override
	/* Cria na base de dados uma transação, executa o metodo de salvar o usuario quando pedir e depois que salvar vai commitar. Se acontecer algum erro é feito o rollback.
	 * (readOnly = true) diz que é feita somente leitura e o Spring faz umas otimizacoes.
	 * */
	@Transactional(readOnly = true) 
	public BigDecimal obterSaldoPorUsuario(Long id) {
		
		//Passa o id para obterSaldoPorTipoLancamentoEUsuario, o tipo Enum RECEITA em formato de String (.name()) e status de EFETIVADO para ser procurado no banco de dados
		//A soma é adicionada na receitas
		BigDecimal receitas = repository.obterSaldoPorTipoLancamentoEUsuarioEStatus(id, TipoLancamento.RECEITA, StatusLancamento.EFETIVADO);
		//Passa o id para obterSaldoPorTipoLancamentoEUsuario, o tipo Enum DESPESA em formato de String (.name()) e status de EFETIVADO para ser procurado no banco de dados
		//A soma é adicionada na despesas
		BigDecimal despesas = repository.obterSaldoPorTipoLancamentoEUsuarioEStatus(id, TipoLancamento.DESPESA, StatusLancamento.EFETIVADO);
		
		if(receitas == null) {
			//BigDecimal.ZERO é a constante 0
			receitas = BigDecimal.ZERO;
		}
		
		if(despesas == null) {
			//BigDecimal.ZERO é a constante 0
			despesas = BigDecimal.ZERO;
		}
		
		//subtract é o metodo de subtrai de algum valor do tipo BigDecimal
		return receitas.subtract(despesas);
	}
	
	

}
