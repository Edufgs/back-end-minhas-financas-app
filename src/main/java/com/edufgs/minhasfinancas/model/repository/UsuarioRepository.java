package com.edufgs.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edufgs.minhasfinancas.model.entity.Usuario;
/* Os repositórios Spring Data JPA são interfaces que você pode definir para acessar dados. 
 * As consultas JPA são criadas automaticamente a partir de seus nomes de métodos. 
 * Assim faz um extends a JpaRepository colocando qual é a entidade e o tipo da chave primaria.
 * Ele faz tudo mas só para uma entidade que foi declarada
 * */ 
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
		
	/* Metodo que retorna um Optional (pode retornar algo ou não)
	 * O findByEmail é um quary metods: não é preciso dizer como vai gerar um sql para consultar um usuario na base de dados.
	 * Quando coloca a sintaxe findBy"propriedade buscada" ele ja busca uma propriedade com o nome indicado no repositorio indicado a cima ("JpaRepository<Usuario, Long>")
	 * Então o spring data ja busca automatico no banco o parametro indicado
	 * */
	
	/* Outro exemplo é se fosse buscar um usuario com dois parametro (nome e email), assim ficaria desse jeito:
	 * Optional<Usuario> findByEmailAndNome(String email, String nome)
	 * Tem que passar os parametros na ordem da definição do metodo.
	 * */

	//Optional<Usuario> findByEmail(String email);

	/* Aqui busca um parametro e se encontrar retorna true ou false dependendo se encontrar ou não
	 * */	
	boolean existsByEmail(String email);
}
