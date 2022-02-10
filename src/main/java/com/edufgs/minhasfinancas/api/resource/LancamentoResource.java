package com.edufgs.minhasfinancas.api.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edufgs.minhasfinancas.api.dto.AtualizaStatusDTO;
import com.edufgs.minhasfinancas.api.dto.LancamentoDTO;
import com.edufgs.minhasfinancas.exception.RegraNegocioException;
import com.edufgs.minhasfinancas.model.entity.Lancamento;
import com.edufgs.minhasfinancas.model.entity.Usuario;
import com.edufgs.minhasfinancas.model.enums.StatusLancamento;
import com.edufgs.minhasfinancas.model.enums.TipoLancamento;
import com.edufgs.minhasfinancas.service.LancamentoService;
import com.edufgs.minhasfinancas.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController //Diz que é o controller
@RequestMapping("/api/lancamentos") //Dia que todas as requisições que começar com "/api/usuarios" vão entrar nesse controller "UsuarioResource". Esse é o endereço padrão e depois pode ser adicionado mais
@RequiredArgsConstructor //Cria um construtor com todos os argumentos obrigatorios que termina com "final"
public class LancamentoResource {
	
	/* Antes precisava disso
	//Esta sendo adicionado uma interface pois o container de injeção de dependendia do Spring vai procurar uma implementação e adicionar aqui
	private LancamentoService service;
	private UsuarioService usuarioService;
	
	//Contrutor para injeção de dependencias
	public LancamentoResource(LancamentoService service, UsuarioService usuarioService) {
		this.service = service;
		this.usuarioService = usuarioService;
	}
	*/
	
	//Agora deixa as variaveis do tipo final e adiciona @RequiredArgsConstructor que cria um construtor com todos os argumentos obrigatorios que termina com "final". É da API Lombok
	private final LancamentoService service;
	private final UsuarioService usuarioService;
	
	/* @RequestParem = pega a requição via URL
	 * required = false = Diz que não é obrigatorio, só passa se quiser
	 * @GetMapping = requisita algo como um get normal
	 * */
	@GetMapping
	public ResponseEntity buscar( 
			@RequestParam(value ="descricao", required = false) String descricao,
			@RequestParam(value ="mes", required = false) Integer mes,
			@RequestParam(value ="ano", required = false) Integer ano,
			@RequestParam("usuario") Long idUsuario //É obrigatorio por isso não tem o "required = false"
			) {
		
		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
		
		Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
		
		//Verifica se o usuario está presente
		if(usuario.isPresent()){
			lancamentoFiltro.setUsuario(usuario.get());
		}else {
			return ResponseEntity.badRequest().body("Não foi possivel realizar a consulta. Usuário não encontrado para o Id informado.");	
		}
		
		//Busca os lançamentos no banco
		List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);
		
		//Ok é o codigo 200 que significa operação realizada com sucesso do http
		return ResponseEntity.ok(lancamentos);
	}
	
	/* Obtem lançamento por id
	 * @PathVariable("id") Long id = passa o id para atulizar no servidor
	 * @GetMapping("{id}") = requisita algo como um get normal onde vai estar passando um id junto da url
	 * */
	@GetMapping("{id}")
	public ResponseEntity obterLancamento( @PathVariable("id") Long id) {
		return service.obterPorId(id)
				//Se o Lancamento foi encontrado ele retorna o lançamento e o codigo http ok
				.map(lancamento -> new ResponseEntity(converter(lancamento), HttpStatus.OK))
				//Se não encontrar nada então retorna o codigo http Not_Found
				.orElseGet(() ->  new ResponseEntity(HttpStatus.NOT_FOUND));
	}
	
	/* Metodo para salvar os lancamentos no banco
	 * @RequestBody = Diz ao objeto Json que vem da requisição com os dados que seja transformados no objeto "LancamentoDTO" 
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
			return new ResponseEntity(entidade,HttpStatus.CREATED);
		}catch(RegraNegocioException e) {
			//Se der erro retorna o codigo HTTP erro generico (BadRequest: 404) 
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	/* Atualiza um lancamento
	 * @PutMapping("{id}") = atualiza um recurso presente no servidor onde é passado o id. No endereço quando passar um id "/api/lancamentos/7" então automaticamente entrara no id do servidor
	 * @PathVariable("id") Long id = passa o id para atulizar no servidor
	 * @RequestBody = Diz ao objeto Json que vem da requisição com os dados que seja transformados no objeto "LancamentoDTO"
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
	
	/* Atualiza os status de lançamento
	 * @PutMapping("{id}/atualiza-status") = atualiza um recurso presente no servidor onde é passado o id. No endereço quando passar um id "/api/lancamentos/7/atualiza-status" então automaticamente entrara no id do servidor
	 * Foi passado o /atualiza-status pois se coloca-se só o id, então entraria no metodo a cima "atualizar" que está com a mesma anotação @PutMapping
	 * @RequestBody = Diz ao objeto Json que vem da requisição com os dados que seja transformados no objeto "AtualizaStatusDTO"
	 * @PathVariable("id") Long id = passa o id para atulizar no servidor
	 * */
	@PutMapping("{id}/atualiza-status")
	public ResponseEntity atualizarStatus( @PathVariable("id") Long id, @RequestBody AtualizaStatusDTO dto ) {
		//map é mapear por id e o obterPorId(id) pega o id e retorna para a entity onde é executado uma função
		return service.obterPorId(id).map( entity -> {
			StatusLancamento statusSelecionado =  StatusLancamento.valueOf(dto.getStatus());
			
			if(statusSelecionado == null) {
				return ResponseEntity.badRequest().body("Não foi opossivel atualizar o status do lancamento, envie um status válido.");
			}
			
			try {
				entity.setStatus(statusSelecionado);			
				service.atualizar(entity);
				return ResponseEntity.ok(entity);
			}catch(RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
			
		}).orElseGet( () -> new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST) );
	}
	
	/* Deletar lancamento
	 * Para deletar precisa apenas do id que vai receber pela url
	 * DeleteMapping("{id}") = Vai receber o id via URL para deletar com a requisição deletar
	 * */
	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		return service.obterPorId(id).map(entidade -> {
			service.deletar(entidade);
			//Como esta retornando uma entidade, então não precisa de mensagem
			return new ResponseEntity (HttpStatus.NO_CONTENT);
			//Se não encontrar nada então retorna o erro
		}).orElseGet( () -> new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
	}
	
	private LancamentoDTO converter (Lancamento lancamento) {
		return LancamentoDTO.builder()
				.id(lancamento.getId())
				.descricao(lancamento.getDescricao())
				.valor(lancamento.getValor())
				.mes(lancamento.getMes())
				.ano(lancamento.getAno())
				.status(lancamento.getStatus().name())
				.tipo(lancamento.getTipo().name())
				.usuario(lancamento.getUsuario().getId())
				.build();
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
				.obterPorId(dto.getUsuario())
				.orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o Id informado."));
		
		lancamento.setUsuario(usuario);
		
		//Verifica se não é nulo
		if(dto.getTipo() != null) {
			//Recebe como parametro uma string/nome da constante e retorna um valor Enum 
			lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		}
		
		//Verifica se não é nulo
		if(dto.getStatus() != null) {
			//Recebe como parametro uma string/nome da constante e retorna um valor Enum 
			lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
		}
		
		return lancamento;
	}
}
