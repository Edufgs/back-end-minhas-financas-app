package com.edufgs.minhasfinancas.model.entity;

<<<<<<< HEAD
import javax.annotation.processing.Generated;
=======
>>>>>>> 29b462220554fded6bf8e0b4516c64bb86b0f9d1
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
=======
import lombok.Builder;
import lombok.Data;
>>>>>>> 29b462220554fded6bf8e0b4516c64bb86b0f9d1

@Entity // Diz pada o JPA que é uma entidade
@Table(name = "usuario", schema = "financas") //Definição da tabela que vai criar no banco de dados. Vai ter o nome e o schema que vai estar no banco de dados
//Usando o Lombok em vez de digitar o set e get entre outros
@Data //Já tem tudo que get, set, constructor, entre outros. Mas tambem pode colocar um por um.(@Setter, @Getter, .....)
@Builder //Construir um objeto mais facil
<<<<<<< HEAD
@NoArgsConstructor
@AllArgsConstructor
=======
>>>>>>> 29b462220554fded6bf8e0b4516c64bb86b0f9d1
public class Usuario {
	
	//Mapeamento das colunas
	@Id //Diz que é o Id primario
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Indica que o provedor de persistência deve atribuir chaves primárias para a entidade usando uma coluna de identidade do banco de dados.
	private Long id;
	
	//Mapeamento das colunas
	@Column(name = "nome")
	private String nome;
	
	//Mapeamento das colunas
	@Column(name = "email")
	private String email;
	
	//Mapeamento das colunas
	@Column(name = "senha")
	private String senha;
	
}
