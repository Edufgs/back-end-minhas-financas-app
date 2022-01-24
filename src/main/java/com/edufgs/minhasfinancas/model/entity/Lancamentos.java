package com.edufgs.minhasfinancas.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity // Diz pada o JPA que é uma entidade
@Table(name = "lancamentos", schema = "financas") //Definição da tabela que vai criar no banco de dados. Vai ter o nome e o schema que vai estar no banco de dados
public class Lancamentos {
	
	//Mapeamento das colunas
	@Id //Diz que é o Id primario
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Indica que o provedor de persistência deve atribuir chaves primárias para a entidade usando uma coluna de identidade do banco de dados.
	@Column(name="id")
	private Long Id;
	
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

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public LocalDate getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDate dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public TipoLancamento getTipo() {
		return tipo;
	}

	public void setTipo(TipoLancamento tipo) {
		this.tipo = tipo;
	}

	public StatusLancamento getStatus() {
		return status;
	}

	public void setStatus(StatusLancamento status) {
		this.status = status;
	}
	
	//Ajuda na comparação de objetos
	@Override
	public int hashCode() {
		return Objects.hash(Id, ano, dataCadastro, mes, status, tipo, usuario, valor);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lancamentos other = (Lancamentos) obj;
		return Objects.equals(Id, other.Id) && Objects.equals(ano, other.ano)
				&& Objects.equals(dataCadastro, other.dataCadastro) && Objects.equals(mes, other.mes)
				&& status == other.status && tipo == other.tipo && Objects.equals(usuario, other.usuario)
				&& Objects.equals(valor, other.valor);
	}

	@Override
	public String toString() {
		return "Lancamentos [Id=" + Id + ", mes=" + mes + ", ano=" + ano + ", usuario=" + usuario + ", valor=" + valor
				+ ", dataCadastro=" + dataCadastro + ", tipo=" + tipo + ", status=" + status + "]";
	}
	
}
