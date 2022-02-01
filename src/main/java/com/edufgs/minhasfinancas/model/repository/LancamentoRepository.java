package com.edufgs.minhasfinancas.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.edufgs.minhasfinancas.model.entity.Lancamento;

/* Os repositórios Spring Data JPA são interfaces que você pode definir para acessar dados. 
 * As consultas JPA são criadas automaticamente a partir de seus nomes de métodos. 
 * Assim faz um extends a JpaRepository colocando qual é a entidade e o tipo da chave primaria.
 * Ele faz tudo mas só para uma entidade que foi declarada
 * */ 
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
	
	/* Obter saldo por tipo lançamento e usuario
	 * Não vai ser usado query metodo pois normalmente é usado para consulta mais simples
	 * @Query manda uma string de consulta do tipo JQPL
	 * Então é mandado essa String onde sum(l.valor) soma o valor dos lancamentos, onde l vem do l.usuario que é lancamento do usuario,
	 * o u.id vem da variavel idUsuario que vai ser passado e o l.tipo vem do tipo que vai ser passado.
	 * A somatoria é por usuario então foi colocado group by u.
	 * @Param("idUsuario") = Relaciona  a variavel com a anotação em @Query
	 * */
	@Query( value = "select sum(l.valor) from Lancamento l join l.usuario u where u.id = :idUsuario and l.tipo =:tipo group by u " )
	BigDecimal obterSaldoPorTipoLancamentoEUsuario( @Param("idUsuario") Long idUsuario, @Param("tipo") String tipo);
	
}
