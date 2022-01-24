package com.edufgs.minhasfinancas.model.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity // Diz pada o JPA que é uma entidade
@Table(name = "usuario", schema = "financas") //Definição da tabela que vai criar no banco de dados. Vai ter o nome e o schema que vai estar no banco de dados
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	//Ajuda na comparação de objetos
	@Override
	public int hashCode() {
		return Objects.hash(email, id, nome, senha);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(email, other.email) && Objects.equals(id, other.id) && Objects.equals(nome, other.nome)
				&& Objects.equals(senha, other.senha);
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nome=" + nome + ", email=" + email + ", senha=" + senha + "]";
	}	
	
}
