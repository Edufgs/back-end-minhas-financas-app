package com.edufgs.minhasfinancas.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edufgs.minhasfinancas.api.dto.LancamentoDTO;
import com.edufgs.minhasfinancas.exception.RegraNegocioException;
import com.edufgs.minhasfinancas.model.entity.Lancamento;
import com.edufgs.minhasfinancas.model.entity.Usuario;
import com.edufgs.minhasfinancas.model.enums.StatusLancamento;
import com.edufgs.minhasfinancas.model.enums.TipoLancamento;
import com.edufgs.minhasfinancas.service.LancamentoService;
import com.edufgs.minhasfinancas.service.UsuarioService;

@RestController //Diz que é o controller
@RequestMapping("/api/lancamentos") //Dia que todas as requisições que começar com "/api/usuarios" vão entrar nesse controller "UsuarioResource". Esse é o endereço padrão e depois pode ser adicionado mais
public class LancamentoResource {
	
	//Esta sendo adicionado uma interface pois o container de injeção de dependendia do Spring vai procurar uma implementação e adicionar aqui
	private LancamentoService service;
	private UsuarioService usuarioService;
	
	//Contrutor para injeção de dependencias
	public LancamentoResource(LancamentoService service) {
		this.service = service;
	}	
	
	/* Metodo para salvar os lancamentos no banco
	 * @RequestBody = Diz ao objeto Json que vem da requisição com os dados do usuario seja transformados no objeto "UsuarioDTO
	 * @PostMapping("/autenticar") = Como é uma requisição tipo POST (enviar dados para um servidor) então coloca essa notação. Como não tem ("") então vai ser o endereço que está definido no @RequestMapping("/api/lancamentos") lá da classe 
	 * */
	@PostMapping
	public ResponseEntity salvar( @RequestBody LancamentoDTO dto ){
		//Tenta salvar o usuario no banco de dados
		try {
			Lancamento entidade = converter(dto);
			//Adiciona no bando e recebe a entidade adicionada
			entidade = service.salvar(entidade);
		
			//Se der certo vai retornar o codigo do HTTP sucesso (Created: 201)
			//Foi utilizado o new ResponseEntity pois é para retornar um objeto
			return ResponseEntity.created(entidade,HttpStatus.CREATED);
		}catch(RegraNegocioException e) {
			//Se der erro retorna o codigo HTTP erro generico (BadRequest: 404) 
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	/* Atualiza um lancamento
	 * @PutMapping("{id}") = atualiza um recurso presente no servidor onde é passado o id. No endereço quando passar um id "/api/lancamentos/7" então automaticamente entrara no id do servidor
	 * @PathVariable("id") Long id = passa o id para atulizar no servidor
	 * */
	@PutMapping("{id}")
	public ResponseEntity atualizar( @PathVariable("id") Long id, @RequestBody LancamentoDTO dto) {
		/* entity é o retorno do obterPorId
		 * Vai retornar um ResponseEntity ou um erro dependendo se encontrar um lancamento
		 * */
		return service.obterPorId(id).map(entity -> {
			//Pode dar erro no banco de dados
			try {
				Lancamento lancamento = converter(dto);
				lancamento.setId(entity.getId());
				service.atualizar(lancamento);
				return ResponseEntity.ok(lancamento);
			}catch(RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
			
		}).orElseGet( () -> new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST) );
	}
	
	/* Transforma o LancamentoDTO em Lancamento entidade
	 * */
	private Lancamento converter(LancamentoDTO dto) {
		Lancamento lancamento = new Lancamento();
		lancamento.setId(dto.getId());
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setAno(dto.getAno());
		lancamento.setMes(dto.getMes());
		lancamento.setValor(dto.getValor());
		
		//orElseThrow = se estiver vazio então lança um erro
		Usuario usuario = usuarioService
				.obterPorId(dto.getId())
				.orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o Id informado."));
		
		lancamento.setUsuario(usuario);
		
		//Recebe como parametro uma string/nome da constante e retorna um valor Enum 
		lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		
		//Recebe como parametro uma string/nome da constante e retorna um valor Enum 
		lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
		
		return lancamento;
	}
}
