package com.edufgs.minhasfinancas.service;

import com.edufgs.minhasfinancas.model.entity.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

public interface JwtService {
	
	//Recebe o usuario e gera o token
	String gerarToken(Usuario usuario);
	
	//Claims são as informações que tem no token (PAYLOAD)
	Claims obterClaims(String token) throws ExpiredJwtException;
	
	//Verifica se o token está valido
	boolean isTokenValido(String token);
	
	//Atraves do token é possivel retornar o login do usuario
	String obterLoginUsuario(String token);
}
