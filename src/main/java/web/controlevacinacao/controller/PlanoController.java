package web.controlevacinacao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxLocation;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxTriggerAfterSwap;
import jakarta.validation.Valid;
import web.controlevacinacao.model.Plano;
import web.controlevacinacao.model.Usuario;
import web.controlevacinacao.notificacao.NotificacaoSweetAlert2;
import web.controlevacinacao.notificacao.TipoNotificaoSweetAlert2;
import web.controlevacinacao.service.PlanoService;
import web.controlevacinacao.service.UsuarioService;

import java.util.List;

@Controller
@RequestMapping("/planos")
public class PlanoController {

    private static final Logger logger = LoggerFactory.getLogger(PlanoController.class);

    private final PlanoService planoService;
    private final UsuarioService usuarioService;

    public PlanoController(PlanoService planoService, UsuarioService usuarioService) {
        this.planoService = planoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/cadastrar")
    public String abrirPaginaCadastro(Plano plano, Model model) {
        List<Usuario> usuarios = usuarioService.listarTodos(); // Lista de usuários disponíveis
        model.addAttribute("usuarios", usuarios);
        return "planos/cadastro";
    }

    @HxRequest
    @GetMapping("/cadastrar")
    public String abrirCadastroPlanoHTMX(Plano plano, Model model) {
        List<Usuario> usuarios = usuarioService.listarTodos();
        model.addAttribute("usuarios", usuarios);
        return "planos/cadastro :: formulario";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@Valid Plano plano, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.info("O plano recebido para cadastrar não é válido.");
            for (FieldError erro : result.getFieldErrors()) {
                logger.info("Erro: {}", erro);
            }
            List<Usuario> usuarios = usuarioService.listarTodos();
            model.addAttribute("usuarios", usuarios); // Recarrega a lista de usuários em caso de erro
            return "planos/cadastro";
        }

        // Recupera o usuário pelo ID selecionado
        Usuario usuario = usuarioService.buscarPorId(plano.getUsuario().getCodigo())
                .orElseThrow(() -> new IllegalArgumentException("Usuário inválido"));

        plano.setUsuario(usuario); // Associa o usuário ao plano
        planoService.salvar(plano);

        return "redirect:/planos/sucesso";
    }

    @GetMapping("/sucesso")
    public String abrirSucesso(Model model) {
        model.addAttribute("mensagem", "Etapa 1 do Cadastro de Plano Efetuado com Sucesso");
        return "mensagem";
    }

    @HxRequest
    @PostMapping("/cadastrar")
    public String cadastrarPlanoHTMX(@Valid Plano plano, BindingResult resultado,
                                     HtmxResponse.Builder htmxResponse, Model model) {
        if (resultado.hasErrors()) {
            logger.info("O plano recebido para cadastrar não é válido.");
            for (FieldError erro : resultado.getFieldErrors()) {
                logger.info("Erro: {}", erro);
            }
            List<Usuario> usuarios = usuarioService.listarTodos();
            model.addAttribute("usuarios", usuarios); // Recarrega a lista de usuários em caso de erro
            return "planos/cadastro :: formulario";
        }

        // Recupera o usuário pelo ID selecionado
        Usuario usuario = usuarioService.buscarPorId(plano.getUsuario().getCodigo())
                .orElseThrow(() -> new IllegalArgumentException("Usuário inválido"));

        plano.setUsuario(usuario); // Associa o usuário ao plano
        planoService.salvar(plano);

        HtmxLocation hl = new HtmxLocation("/planos/sucesso");
        hl.setTarget("#main");
        hl.setSwap("outerHTML");
        htmxResponse.location(hl);
        return "mensagem";
    }

    @HxRequest
    @HxTriggerAfterSwap("htmlAtualizado")
    @GetMapping("/sucesso")
    public String abrirMensagemSucessoHTMX(Model model) {
        model.addAttribute("notificacao", new NotificacaoSweetAlert2(
                "Etapa 1 do Plano cadastrado com sucesso",
                TipoNotificaoSweetAlert2.SUCCESS,
                4000
        ));
        return "planos/cadastro :: formulario";
    }
}
