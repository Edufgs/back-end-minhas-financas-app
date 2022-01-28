package com.edufgs.minhasfinancas.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edufgs.minhasfinancas.model.entity.Lancamento;
import com.edufgs.minhasfinancas.model.enums.StatusLancamento;
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
		
		// TODO Auto-generated method stub
		
		return repository.save(lancamento); //O metodo save salva e atualiza, se nao tiver id então ele salva e se tiver id então é atualizado o dados.
	}

	@Override
	@Transactional //Cria na base de dados uma transação, executa o metodo de salvar o usuario quando pedir e depois que salvar vai commitar. Se acontecer algum erro é feito o rollback.
	public Lancamento atualizar(Lancamento lancamento) {
		
		//O id nao pode ser nulo se nao vai dar erro
		Objects.requireNonNull(lancamento.getId());
				
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
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		//Muda o status de lancamento
		lancamento.setStatus(status);
		
		//Manda para o metodo atualizar
		atualizar(lancamento);
	}
	
	

}
