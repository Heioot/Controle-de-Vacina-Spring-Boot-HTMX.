package web.controlevacinacao.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxLocation;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxLocation;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxTriggerAfterSwap;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import web.controlevacinacao.filter.EquipamentoFilter;
import web.controlevacinacao.model.Equipamento;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.notificacao.NotificacaoSweetAlert2;
import web.controlevacinacao.notificacao.TipoNotificaoSweetAlert2;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.EquipamentoRepository;
import web.controlevacinacao.service.EquipamentoService;

@Controller
@RequestMapping("/equipamentos")
public class EquipamentoController {

    private static final Logger logger = LoggerFactory.getLogger(EquipamentoController.class);

    private EquipamentoRepository equipamentoRepository;
    private EquipamentoService equipamentoService;

    public EquipamentoController(EquipamentoRepository equipamentoRepository, EquipamentoService equipamentoService) {
        this.equipamentoRepository = equipamentoRepository;
        this.equipamentoService = equipamentoService;
    }

    @GetMapping("/todas")
    public String mostrarTodosEquipamentos(Model model) {
        List<Equipamento> equipamentos = equipamentoRepository.findAll();
        logger.info("Equipamentos buscados: {}", equipamentos);
        model.addAttribute("equipamentos", equipamentos);
        return "equipamentos/todas";
    }

    @GetMapping("/cadastrar")
    public String abrirPaginaCadastro(Equipamento equipamento) {
        return "equipamentos/cadastro";
    }

    @HxRequest
    @GetMapping("/cadastrar")
    public String abrirCadastroEquipamentoHTMX(Equipamento equipamento) {
        return "equipamentos/cadastro :: formulario";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(Equipamento equipamento) {
        equipamentoService.salvar(equipamento);
        return "redirect:/equipamentos/sucesso";
    }

    @GetMapping("/sucesso")
    public String abrirSucesso(Model model) {
        model.addAttribute("mensagem", "Cadastro de Equipamento Efetuado com Sucesso");
        return "mensagem";
    }

    @HxRequest
    @PostMapping("/cadastrar")
    public String cadastrarEquipamentoHTMX(@Valid Equipamento equipamento, BindingResult resultado,
                                           HtmxResponse.Builder htmxResponse) {
        if (resultado.hasErrors()) {
            logger.info("O equipamento recebido para cadastrar não é válido.");
            logger.info("Erros encontrados:");
            for (FieldError erro : resultado.getFieldErrors()) {
                logger.info("{}", erro);
            }
            return "equipamentos/cadastro :: formulario";
        } else {
            equipamentoService.salvar(equipamento);
            HtmxLocation hl = new HtmxLocation("/equipamentos/sucesso");
            hl.setTarget("#main");
            hl.setSwap("outerHTML");
            htmxResponse.location(hl);
            return "mensagem";
        }
    }

    @HxRequest
    @HxTriggerAfterSwap("htmlAtualizado")
    @GetMapping("/sucesso")
    public String abrirMensagemSucessoHTMX(Equipamento equipamento, Model model) {
        model.addAttribute("notificacao", new NotificacaoSweetAlert2("Equipamento cadastrado com sucesso", TipoNotificaoSweetAlert2.SUCCESS, 4000));
        return "equipamentos/cadastro :: formulario";
    }

    @GetMapping("/abrirpesquisar")
    public String abrirPaginaPesquisa() {
        return "equipamentos/pesquisar";
    }

    @HxRequest
    @GetMapping("/abrirpesquisar")
    public String abrirPaginaPesquisaHTMX() {
        return "equipamentos/pesquisar :: formulario";
    }

    @GetMapping("/pesquisar")
    public String pesquisar(EquipamentoFilter filtro, Model model,
                            @PageableDefault(size = 7) @SortDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                            HttpServletRequest request) {
        Page<Equipamento> pagina = equipamentoRepository.pesquisar(filtro, pageable);
        logger.info("Equipamentos pesquisados: {}", pagina.getContent());
        PageWrapper<Equipamento> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "equipamentos/equipamentos";
    }

    @HxRequest
    @HxTriggerAfterSwap("htmlAtualizado")
    @GetMapping("/pesquisar")
    public String pesquisarHTMX(EquipamentoFilter filtro, Model model,
                                @PageableDefault(size = 7) @SortDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                HttpServletRequest request) {
        Page<Equipamento> pagina = equipamentoRepository.pesquisar(filtro, pageable);
        logger.info("Equipamentos pesquisados: {}", pagina);
        PageWrapper<Equipamento> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "equipamentos/equipamentos :: tabela";
    }

    @PostMapping("/abriralterar")
    public String abrirAlterar(Equipamento equipamento) {
        return "equipamentos/alterar";
    }

    @HxRequest
    @PostMapping("/abriralterar")
    public String abrirAlterarHTMX(Equipamento equipamento) {
        return "equipamentos/alterar :: formulario";
    }

    @PostMapping("/alterar")
    public String alterar(Equipamento equipamento) {
        equipamentoService.alterar(equipamento);
        return "redirect:/equipamentos/sucesso2";
    }

    @GetMapping("/sucesso2")
    public String abrirSucesso2(Model model) {
        model.addAttribute("mensagem", "Alteração de Equipamento Efetuada com Sucesso");
        return "mensagem";
    }

    @HxRequest
    @PostMapping("/alterar")
    public String alterarHTMX(@Valid Equipamento equipamento, BindingResult resultado, HtmxResponse.Builder htmxResponse) {
        if (resultado.hasErrors()) {
            logger.info("O equipamento recebido para alterar não é válido.");
            logger.info("Erros encontrados:");
            for (FieldError erro : resultado.getFieldErrors()) {
                logger.info("{}", erro);
            }
            return "equipamentos/alterar :: formulario";
        } else {
            equipamentoService.alterar(equipamento);
            HtmxLocation hl = new HtmxLocation("/equipamentos/sucesso2");
            hl.setTarget("#main");
            hl.setSwap("outerHTML");
            htmxResponse.location(hl);
            return "mensagem";
        }
    }

    @HxRequest
    @HxTriggerAfterSwap("htmlAtualizado")
    @GetMapping("/sucesso2")
    public String abrirMensagemSucesso2HTMX(Model model) {
        model.addAttribute("notificacao", new NotificacaoSweetAlert2("Equipamento alterado com sucesso", TipoNotificaoSweetAlert2.SUCCESS, 4000));
        return "equipamentos/pesquisar :: formulario";
    }

    @PostMapping("/remover")
    public String remover(Equipamento equipamento) {
        equipamento.setStatus(Status.INATIVO);
        equipamentoService.alterar(equipamento);
        return "redirect:/equipamentos/sucesso3";
    }

    @GetMapping("/sucesso3")
    public String abrirSucesso3(Model model) {
        model.addAttribute("mensagem", "Remoção de Equipamento Efetuada com Sucesso");
        return "mensagem";
    }

    @HxRequest
    @HxLocation(path = "/equipamentos/sucesso3", target = "#main", swap = "outerHTML")
    @PostMapping("/remover")
    public String removerHTMX(Equipamento equipamento) {
        equipamento.setStatus(Status.INATIVO);
        equipamentoService.alterar(equipamento);
        return "mensagem";
    }

    @HxRequest
    @HxTriggerAfterSwap("htmlAtualizado")
    @GetMapping("/sucesso3")
    public String abrirMensagemSucesso3HTMX(Model model) {
        model.addAttribute("notificacao", new NotificacaoSweetAlert2("Equipamento removido com sucesso", TipoNotificaoSweetAlert2.SUCCESS, 4000));
        return "equipamentos/pesquisar :: formulario";
    }
}
