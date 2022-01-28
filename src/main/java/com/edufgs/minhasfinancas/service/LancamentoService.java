package com.edufgs.minhasfinancas.service;

import java.util.List;

import com.edufgs.minhasfinancas.model.entity.Lancamento;
import com.edufgs.minhasfinancas.model.enums.StatusLancamento;

public interface LancamentoService {
	
	//Salva lancamentos no banco e retorna a instacia do lancamento com ID
	Lancamento salvar(Lancamento lancamento);
	
	//Atualiza os lancamentos no banco. Tem que receber o lancamento com id
	Lancamento atualizar(Lancamento lancamento);
	
	//Deleta o lancamento
	void deletar(Lancamento lancamento);
	
	//Busca a lista de lancamentos por um lancamento filtro
	List<Lancamento> buscar(Lancamento lancamentoFiltro);
	
	//Atualiza os status do lancamento
	void atualizarStatus(Lancamento lancamento, StatusLancamento status);
	
}
