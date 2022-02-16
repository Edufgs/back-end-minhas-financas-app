package com.edufgs.minhasfinancas.resource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.edufgs.minhasfinancas.api.dto.UsuarioDTO;
import com.edufgs.minhasfinancas.api.resource.UsuarioResource;
import com.edufgs.minhasfinancas.exception.ErroAutenticacao;
import com.edufgs.minhasfinancas.exception.RegraNegocioException;
import com.edufgs.minhasfinancas.model.entity.Usuario;
import com.edufgs.minhasfinancas.service.LancamentoService;
import com.edufgs.minhasfinancas.service.UsuarioService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

//Anotação para teste
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test") //Seleciona o perfil do teste usando h2
//Notação para teste do controller
@WebMvcTest(controllers = UsuarioResource.class) //Sobe o contexto Rest para testar o controller e só lança o  UsuarioResource
@AutoConfigureMockMvc //Serve para ter acesso ao objeto chamado de MockMvc ajuda a fazer as execuções de teste para a api
public class UsuarioResourceTest {
	
	//Endereço da api
	static final String API = "/api/usuarios";
	static final MediaType JSON = MediaType.APPLICATION_JSON;
	
	@Autowired 
	MockMvc mvc; //Objeto de requisição
	
	@MockBean //Com essa anotação o spring boot faz automatico o processo de mock. Então agora ele cria uma instacia mockada.
	UsuarioService service;
	
	/* Como o UsuarioResource tem um LancamentoService lancamentoService; então é preciso mocka ela */
	@MockBean //Com essa anotação o spring boot faz automatico o processo de mock. Então agora ele cria uma instacia mockada.
	LancamentoService lancamentoService;
	
	@Test
	public void deveAutenticarUmUsuario() throws Exception {
		//cenario
		String email = "usuario@email.com";
		String senha = "123";
		
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
		Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();
		
		//Se o servico chamar o autenticar com esse email e essa senha então retorna o usuario
		Mockito.when(service.autenticar(email, senha)).thenReturn(usuario);
		
		//Cria um objeto JSON
		//writeValueAsString pega um objeto e transforma em uma string JSON
		String json = new ObjectMapper().writeValueAsString(dto);
		
		//Execução e verificação
		//MockMvcRequestBuilders = serve para criar uma requisição. Como é uma requisição do tipo post então coloca .post e o endereco
		//A requisição recebe de volta uma requisição
		MockHttpServletRequestBuilder request =  MockMvcRequestBuilders
														.post(API.concat("/autenticar"))
														.accept(JSON) //Aceita JSON
														.contentType(JSON) //Conteudo do mesmo tipo
														.content(json); //Envia o objeto json
		
		//Verifica os dados da requisição
		mvc.perform(request) //Executa o request
			.andExpect(MockMvcResultMatchers.status().isOk()) //É esperado que o status seja ok. Quando é autenticado é retornado o status de ok 
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))  //Verifica se o id do Json é igual o id do usuario
			.andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome())) //Verifica se o nome do Json é igual o nome do usuario
			.andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail())) //Verifica se o email do Json é igual o email do usuario
			//Não é usado pois foi adicionado "@JsonIgnore //Diz para não mandar a senha via JSON"
			//.andExpect(MockMvcResultMatchers.jsonPath("senha").value(usuario.getSenha())) //Verifica se a senha do JSON é igual a senha do usuario. 
			;
	}
	
	@Test
	public void deveRetornarBadRequestAoObterErroDeAutenticacao() throws Exception {
		//cenario
		String email = "usuario@email.com";
		String senha = "123";
		
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
		
		//Se o servico chamar o autenticar com esse email e essa senha então retorna ErroAutenticacao
		Mockito.when(service.autenticar(email, senha)).thenThrow(ErroAutenticacao.class);
		
		//Cria um objeto JSON
		//writeValueAsString pega um objeto e transforma em uma string JSON
		String json = new ObjectMapper().writeValueAsString(dto);
		
		//Execução e verificação
		//MockMvcRequestBuilders = serve para criar uma requisição. Como é uma requisição do tipo post então coloca .post e o endereco
		//A requisição recebe de volta uma requisição
		MockHttpServletRequestBuilder request =  MockMvcRequestBuilders
														.post(API.concat("/autenticar"))
														.accept(JSON) //Aceita JSON
														.contentType(JSON) //Conteudo do mesmo tipo
														.content(json); //Envia o objeto json
		
		//Verifica os dados da requisição
		mvc.perform(request) //Executa o request
			.andExpect(MockMvcResultMatchers.status().isBadRequest()); //É esperado que o status seja BadRequest.
	}
	
	@Test
	public void deveCriarUmNovoUsuario() throws Exception {
		//cenario
		String email = "usuario@email.com";
		String senha = "123";
		
		UsuarioDTO dto = UsuarioDTO.builder().email("usuario@email.com").senha("123").build();
		Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();
		
		//Se o servico chamar o salvarUsuario mandando qualquer usuario retorna o usuario
		Mockito.when(service.salvarUsuario(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		//Cria um objeto JSON
		//writeValueAsString pega um objeto e transforma em uma string JSON
		String json = new ObjectMapper().writeValueAsString(dto);
		
		//Execução e verificação
		//MockMvcRequestBuilders = serve para criar uma requisição. Como é uma requisição do tipo post então coloca .post e o endereco
		//A requisição recebe de volta uma requisição
		MockHttpServletRequestBuilder request =  MockMvcRequestBuilders
														.post(API)
														.accept(JSON) //Aceita JSON
														.contentType(JSON) //Conteudo do mesmo tipo
														.content(json); //Envia o objeto json
		
		//Verifica os dados da requisição
		mvc.perform(request) //Executa o request
			.andExpect(MockMvcResultMatchers.status().isCreated()) //É esperado que o status seja Created.
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))  //Verifica se o id do Json é igual o id do usuario
			.andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome())) //Verifica se o nome do Json é igual o nome do usuario
			.andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail())) //Verifica se o email do Json é igual o email do usuario
			//Não é usado pois foi adicionado "@JsonIgnore //Diz para não mandar a senha via JSON"
			//.andExpect(MockMvcResultMatchers.jsonPath("senha").value(usuario.getSenha())) //Verifica se a senha do JSON é igual a senha do usuario. 
			;
	}
	
	@Test
	public void deveRetornarBadRequestAoTentarCriarUmUsuarioInvalido() throws Exception {
		//cenario
		String email = "usuario@email.com";
		String senha = "123";
		
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
		
		//Se o servico chamar o salvarUsuario mandando qualquer usuario lança um erro RegraNegocioException
		Mockito.when(service.salvarUsuario(Mockito.any(Usuario.class))).thenThrow(RegraNegocioException.class);
		
		//Cria um objeto JSON
		//writeValueAsString pega um objeto e transforma em uma string JSON
		String json = new ObjectMapper().writeValueAsString(dto);
		
		//Execução e verificação
		//MockMvcRequestBuilders = serve para criar uma requisição. Como é uma requisição do tipo post então coloca .post e o endereco
		//A requisição recebe de volta uma requisição
		MockHttpServletRequestBuilder request =  MockMvcRequestBuilders
														.post(API)
														.accept(JSON) //Aceita JSON
														.contentType(JSON) //Conteudo do mesmo tipo
														.content(json); //Envia o objeto json
		
		//Verifica os dados da requisição
		mvc.perform(request) //Executa o request
			.andExpect(MockMvcResultMatchers.status().isBadRequest()); //É esperado que o status seja BadRequest.
	}
}
