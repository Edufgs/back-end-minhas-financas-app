package com.edufgs.minhasfinancas.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
	
	//Valida os dados preenchidos
	void validar(Lancamento lancamento);
	
	//Busca lancamento por id
	Optional<Lancamento> obterPorId(Long id);
	
	//Obter saldo do Usuario
	BigDecimal obterSaldoPorUsuario(Long id);
}
