package web.controlevacinacao.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxTriggerAfterSwap;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import web.controlevacinacao.filter.EquipamentoFilter;
import web.controlevacinacao.filter.UsuarioFilter;
import web.controlevacinacao.model.Equipamento;
import web.controlevacinacao.model.Papel;
import web.controlevacinacao.model.Usuario;
import web.controlevacinacao.notificacao.NotificacaoSweetAlert2;
import web.controlevacinacao.notificacao.TipoNotificaoSweetAlert2;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.PapelRepository;
import web.controlevacinacao.repository.UsuarioRepository;
import web.controlevacinacao.service.CadastroUsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

	private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
	
	private PapelRepository papelRepository;
	private CadastroUsuarioService cadastroUsuarioService;
	private PasswordEncoder passwordEncoder;
	private UsuarioRepository usuarioRepository;
	
	public UsuarioController(PapelRepository papelRepository, CadastroUsuarioService cadastroUsuarioService,
			PasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository) {
		this.papelRepository = papelRepository;
		this.cadastroUsuarioService = cadastroUsuarioService;
		this.passwordEncoder = passwordEncoder;
		this.usuarioRepository = usuarioRepository;
	}

	@GetMapping("/todos")
	public String mostrarTodosUsuarios(Model model) {
		List<Usuario> usuarios = usuarioRepository.findAll();
		logger.info("Usuários buscados: {}", usuarios);
		model.addAttribute("usuarios", usuarios);
		return "usuarios/todos";
	}

	@GetMapping("/abrirpesquisar")
	public String abrirPaginaPesquisa() {
		return "usuarios/pesquisar";
	}

	@HxRequest
	@GetMapping("/abrirpesquisar")
	public String abrirPaginaPesquisaHTMX() {
		return "usuarios/pesquisar :: formulario";
	}

	@GetMapping("/pesquisar")
	public String pesquisar(UsuarioFilter filtro, Model model,
							@PageableDefault(size = 7) @SortDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
							HttpServletRequest request) {
		Page<Usuario> pagina = usuarioRepository.pesquisar(filtro, pageable);
		logger.info("Usuários pesquisados: {}", pagina.getContent());
		PageWrapper<Usuario> paginaWrapper = new PageWrapper<>(pagina, request);
		model.addAttribute("pagina", paginaWrapper);
		return "usuarios/usuarios";
	}

	@HxRequest
	@HxTriggerAfterSwap("htmlAtualizado")
	@GetMapping("/pesquisar")
	public String pesquisarHTMX(UsuarioFilter filtro, Model model,
								@PageableDefault(size = 7) @SortDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
								HttpServletRequest request) {
		Page<Usuario> pagina = usuarioRepository.pesquisar(filtro, pageable);
		logger.info("Usuários pesquisados: {}", pagina);
		PageWrapper<Usuario> paginaWrapper = new PageWrapper<>(pagina, request);
		model.addAttribute("pagina", paginaWrapper);
		return "usuarios/usuarios :: tabela";
	}

	@GetMapping("/cadastrar")
	@HxRequest
	@HxTriggerAfterSwap("htmlAtualizado")
	public String abrirCadastroUsuario(Usuario usuario, Model model) {
		List<Papel> papeis = papelRepository.findAll();
		model.addAttribute("todosPapeis", papeis);
		return "usuarios/cadastrar :: formulario";
	}
	
	@PostMapping("/cadastrar")
	@HxRequest
	@HxTriggerAfterSwap("htmlAtualizado")
	public String cadastrarNovoUsuario(@Valid Usuario usuario, BindingResult resultado, Model model, RedirectAttributes redirectAttributes) {
		if (resultado.hasErrors()) {
			logger.info("O usuario recebido para cadastrar não é válido.");
			logger.info("Erros encontrados:");
			for (FieldError erro : resultado.getFieldErrors()) {
				logger.info("{}", erro);
			}
			List<Papel> papeis = papelRepository.findAll();
			model.addAttribute("todosPapeis", papeis);
			return "usuarios/cadastrar :: formulario";
		} else {
			usuario.setAtivo(true);
			usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
			cadastroUsuarioService.salvar(usuario);
			redirectAttributes.addAttribute("mensagem", "Cadastro de usuário efetuado com sucesso.");
			return "redirect:/usuarios/cadastrosucesso";
		}
	}
	
	@GetMapping("/cadastrosucesso")
	@HxRequest
	@HxTriggerAfterSwap("htmlAtualizado") 
	public String mostrarCadastroSucesso(String mensagem, Usuario usuario, Model model) {
		List<Papel> papeis = papelRepository.findAll();
		model.addAttribute("todosPapeis", papeis);
		if (mensagem != null && !mensagem.isEmpty()) {
            model.addAttribute("notificacao", new NotificacaoSweetAlert2(mensagem,
                    TipoNotificaoSweetAlert2.SUCCESS, 4000));
        }
		return "usuarios/cadastrar :: formulario";
	}
}