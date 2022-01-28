package com.edufgs.minhasfinancas.api.dto;

/* É apenas uma classe que vai receber alguns atributos simples
 * É usado para ser preenchido e depois transformado em um objeto usuario e JSON
 * */

import java.math.BigDecimal;

import com.edufgs.minhasfinancas.model.entity.Usuario;

import lombok.Builder;
import lombok.Data;

//Usando o Lombok em vez de digitar o set e get entre outros
@Data //Já tem tudo que get, set, constructor, entre outros. Mas tambem pode colocar um por um.(@Setter, @Getter, .....)
@Builder
public class LancamentoDTO {
	
	private Long id;
	private String descricao;
	private Integer mes;
	private Integer ano;
	private BigDecimal valor;
	//Usuario não vai ser como objeto Usuario mas sim long pois o JSON entregara um ID
	private Long usuario;
	private String tipo;
	private String status;
}
