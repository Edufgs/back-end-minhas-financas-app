package com.edufgs.minhasfinancas.api.resource;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edufgs.minhasfinancas.api.dto.TokenDTO;
import com.edufgs.minhasfinancas.api.dto.UsuarioDTO;
import com.edufgs.minhasfinancas.exception.ErroAutenticacao;
import com.edufgs.minhasfinancas.exception.RegraNegocioException;
import com.edufgs.minhasfinancas.model.entity.Usuario;
import com.edufgs.minhasfinancas.service.JwtService;
import com.edufgs.minhasfinancas.service.LancamentoService;
import com.edufgs.minhasfinancas.service.UsuarioService;

import lombok.RequiredArgsConstructor;

/* Classe Controller que controla as requisições web*/

@RestController //Diz que é o controller
@RequestMapping("/api/usuarios") //Dia que todas as requisições que começar com "/api/usuarios" vão entrar nesse controller "UsuarioResource"
@RequiredArgsConstructor //Cria um construtor com todos os argumentos obrigatorios que termina com "final". É da API Lombok
public class UsuarioResource {
	
	/* Antes precisava disso
	//Esta sendo adicionado uma interface pois o container de injeção de dependendia do Spring vai procurar uma implementação e adicionar aqui
	private UsuarioService service;
	
	//Contrutor para injeção de dependencias
	public UsuarioResource(UsuarioService service) {
		this.service = service;
	}
	*/
	
	//Agora deixa as variaveis do tipo final e adiciona @RequiredArgsConstructor que cria um construtor com todos os argumentos obrigatorios que termina com "final". É da API Lombok
	private final UsuarioService service;
	private final LancamentoService lancamentoService;
	private final JwtService jwtServise;
	
	/* Para testar é executar a classe MinhasfinancasApplication.java
	 * Depois de compilar é só ir no navegador e colocar http://localhost:8080/
	 * A "/" no final é oq foi colocado no GetMapping("/")
	 **/
	
	/* Metodo para fazer a autenticação do usuario
	 * @RequestBody = Diz ao objeto Json que vem da requisição com os dados do usuario seja transformados no objeto "UsuarioDTO
	 * @PostMapping("/autenticar") = Diz que a requisição desse metodo vai terminar com "/autenticar". Então vai ficar com "/api/usuarios/autenticar" para esse metodo receber um Post.
	 * <?> = diz que pode retornar mais de um objeto dentro da resposta 
	 * */
	@PostMapping("/autenticar") //Dia que a requisição desse metodo vai terminar com "/autenticar". Então vai ficar com "/api/usuarios/autenticar" para esse metodo receber um Post. 
	public ResponseEntity<?> autenticar(@RequestBody UsuarioDTO dto) {
		
		//tem que colocar entre try e catch para tratar o erro que o autenticar pode lançar
		try{
			//Pega o usuario
			Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
			//Cria o token com o usuario
			String token = jwtServise.gerarToken(usuarioAutenticado);
			//Cria um TokenDTO para ser retornado
			TokenDTO tokenDTO = new TokenDTO(usuarioAutenticado.getNome(), token);
			//Ok é o codigo 200 que significa operação realizada com sucesso do http
			return ResponseEntity.ok(tokenDTO); //Retorna o tokenDTO com o nome e o token
			
		}catch(ErroAutenticacao e) {
			//Se der erro retorna o codigo HTTP erro generico (BadRequest: 404) 
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	/* Metodo para salvar um novo usuario no banco de dados
	 * ResponseEntity= Representa o corpo da resposta
	 * @RequestBody = Diz ao objeto Json que vem da requisição com os dados do usuario seja transformados no objeto "UsuarioDTO"
	 * @PostMapping = Como é uma requisição tipo POST (enviar dados para um servidor) então coloca essa notação. Como não tem ("") então vai ser o endereço que está definido no @RequestMapping("/api/usuarios") lá da classe 
	 * */
	@PostMapping
	public ResponseEntity salvar( @RequestBody UsuarioDTO dto) {
		Usuario usuario = Usuario.builder()
				.nome(dto.getNome())
				.email(dto.getEmail())
				.senha(dto.getSenha())
				.build();
		
		//Tenta salvar o usuario no banco de dados
		try {
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			//Se der certo vai retornar o codigo do HTTP sucesso (Created: 201)
			//Foi utilizado o new ResponseEntity pois é para retornar um objeto
			return new ResponseEntity(usuarioSalvo,HttpStatus.CREATED);
			
		}catch(RegraNegocioException e) {
			//Se der erro retorna o codigo HTTP erro generico (BadRequest: 404) 
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	/* Retorna a o saldo de acordo com os lançamentos (RECEITA ou DESPESA)
	 * @GetMapping = requisita algo como um get normal
	 */
	@GetMapping("{id}/saldo")
	public ResponseEntity obterSaldo(@PathVariable("id") Long id) {
		//verifica se o id é valido
		Optional<Usuario> usuario = service.obterPorId(id);
		
		if(!usuario.isPresent()) {
			//Retorna um NOT_FOUND para indicar que não foi encontrado o recurso no servidor
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		
		BigDecimal saldo = lancamentoService.obterSaldoPorUsuario(id);
		return ResponseEntity.ok(saldo);
	}
}
