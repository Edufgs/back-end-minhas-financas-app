package com.edufgs.minhasfinancas.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import com.edufgs.minhasfinancas.model.enums.StatusLancamento;
import com.edufgs.minhasfinancas.model.enums.TipoLancamento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity // Diz pada o JPA que é uma entidade
@Table(name = "lancamentos", schema = "financas") //Definição da tabela que vai criar no banco de dados. Vai ter o nome e o schema que vai estar no banco de dados
//Usando o Lombok em vez de digitar o set e get entre outros
@Data //Já tem tudo que get, set, constructor, entre outros. Mas tambem pode colocar um por um.(@Setter, @Getter, .....)
@Builder //Construir um objeto mais facil
@NoArgsConstructor //Cria um construtor sem argumento 
@AllArgsConstructor //Cria um construtor com argumento
public class Lancamento {
	
	//Mapeamento das colunas
	@Id //Diz que é o Id primario
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Indica que o provedor de persistência deve atribuir chaves primárias para a entidade usando uma coluna de identidade do banco de dados.
	@Column(name="id")
	private Long Id;
	
	@Column(name="descricao")
	private String descricao;
	
	@Column(name="mes") 
	private Integer mes;
	
	@Column(name="ano")
	private Integer ano;
	
	@ManyToOne //Relacionamento: muitos para um, muitos lançamentos para um usuario 
	@JoinColumn(name ="id_usuario") //JoinColumn = Relaciona com outra coluna
	private Usuario usuario;
	
	//Valor do lançamento
	@Column(name="valor")
	private BigDecimal valor;
	
	@Column(name="data_cadastro")
	@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class) //O SQL não tem compatibilidade com LocalDate, então é preciso fazer o Spring Boot convertera data para uma compativel. 
	private LocalDate dataCadastro; //Api de datas do java 8
	
	@Column(name="tipo")
	@Enumerated(value = EnumType.STRING) //Diz que é uma numeração, e o valor vai ser em string
	private TipoLancamento tipo;
	
	@Column(name="status")
	@Enumerated(value= EnumType.STRING) //Diz que é uma numeração, e o valor vai ser em string
	private StatusLancamento status;
	
}
