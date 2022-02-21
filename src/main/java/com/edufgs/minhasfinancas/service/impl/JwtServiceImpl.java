package com.edufgs.minhasfinancas.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.edufgs.minhasfinancas.model.entity.Usuario;
import com.edufgs.minhasfinancas.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service //Diz para o container do Spring boot que gerencie essa classe. Então ele adiciona um container quando precisar.
public class JwtServiceImpl implements JwtService{
	
	@Value("${jwt.expiracao}") //Vai pegar o valor da application.properties com o mesmo nome entre chaves e sempre pega em String
	private String expiracao; //Guarda o tempo de expiração da sessão
	
	@Value("${jwt.chave.assinatura}") //Vai pegar o valor da application.properties com o mesmo nome entre chaves e sempre pega em String
	private String chaveAssinatura; //Quarda a chave de assinatura
	
	//Gera o token
	@Override
	public String gerarToken(Usuario usuario) {
		
		//Pega o valor do tempo expiracao e transforma em long
		long exp = Long.valueOf(expiracao);
		
		//Pega a data e hora atual e soma com o tempo de expiração da sessão e o now().plusMinutes() faz isso, soma o tempo na data atual (Como é minutos então é plusMinutes())
		LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(exp);
		Instant instant = dataHoraExpiracao.atZone( ZoneId.systemDefault() ).toInstant(); //Cria uma instacia onde a zona da data é a padrão do sistema operacional
		java.util.Date data = Date.from(instant); //Como o Date.from() recebe um Instant então foi criado a cima
		
		//Pega a hora da expiração e formata em hora e minutos
		String horaExpiracaoToken = dataHoraExpiracao.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
		
		//Pega o token criado
		String token = Jwts
						//Configura a criação do token
						.builder()
						.setExpiration(data) //Set o tempo de expliração onde ele recebe um tempo do tipo date (Date do java.util)
						//Com o claim da para mandar alguns dados. Não mande a senha pois então não vai servir de nada o token
						.claim("userid", usuario.getId()) //Passa o id do usuario
						.claim("nome", usuario.getNome()) //Mandando o nome
						.claim("horaExpiracao", horaExpiracaoToken) //Manda a hora de expiração formatado
						.setSubject(usuario.getEmail()) //Identifica o usuario onde o sub é a propriedade padrão do JWT, como o email não se repete nos usuarios então vai ser usado ele, tb da para usar o id.
						.signWith(SignatureAlgorithm.HS512, chaveAssinatura) //Assina o token onde o SignatureAlgorithm tem varias assinatura, vai ser usado o ES512. Tb vai ser usado a chave de assinatura criada no application.properties
						.compact(); //Manda criar a chave com essas configurações
	
		return token;
	}

	/*
	 * Claims: São informações contida no token
	 * Obtem as informações contida no token
	 */
	@Override
	public Claims obterClaims(String token) throws ExpiredJwtException {
		
		return Jwts
					.parser() //Faz o passe do token para extratir as informações
					.setSigningKey(chaveAssinatura) //Passa a chave de assinatura
					.parseClaimsJws(token) //Analisa a string JWS serializada compacta especificada com base no estado de configuração atual do construtor e retorna a instância de Claims JWS resultante.
					.getBody(); //Retorna o Claims desse token
	}

	@Override
	public boolean isTokenValido(String token) {
		try {
			Claims claims = obterClaims(token); //Obtem o claims
			java.util.Date dataExp = claims.getExpiration(); //Pega a expiração do token
			
			LocalDateTime dataExpiracao = dataExp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			
			//Pega a data atual e verifica se é posterior à data-hora especificada.
			//Se for retorna true e se não for retorna false
			return !LocalDateTime.now().isAfter(dataExpiracao);
		}catch (ExpiredJwtException  e) {
			return false;
		}
		
	}

	@Override
	public String obterLoginUsuario(String token) {
		Claims claims = obterClaims(token); //pega o claims
		return claims.getSubject(); //retorna o email do usuario que foi colocado no Subject
	}

}
