package com.edufgs.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edufgs.minhasfinancas.model.entity.Lancamento;

/* Os repositórios Spring Data JPA são interfaces que você pode definir para acessar dados. 
 * As consultas JPA são criadas automaticamente a partir de seus nomes de métodos. 
 * Assim faz um extends a JpaRepository colocando qual é a entidade e o tipo da chave primaria.
 * Ele faz tudo mas só para uma entidade que foi declarada
 * */ 
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
