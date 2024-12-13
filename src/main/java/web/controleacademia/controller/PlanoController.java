package web.controleacademia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.controleacademia.model.Plano;
import web.controleacademia.service.PlanoService;

@Controller
@RequestMapping("/planos")
public class PlanoController {

    @Autowired
    private PlanoService planoService;

    @GetMapping
    public String listarPlanos(Model model) {
        model.addAttribute("planos", planoService.listarTodos());
        return "plano/list";
    }

    @GetMapping("/novo")
    public String novoPlano(Model model) {
        model.addAttribute("plano", new Plano());
        return "plano/form";
    }

    @PostMapping
    public String salvarPlano(@ModelAttribute Plano plano) {
        planoService.salvar(plano);
        return "redirect:/planos";
    }

    @GetMapping("/editar/{id}")
    public String editarPlano(@PathVariable Long id, Model model) {
        model.addAttribute("plano", planoService.buscarPorId(id).orElse(new Plano()));
        return "plano/form";
    }

    @GetMapping("/deletar/{id}")
    public String deletarPlano(@PathVariable Long id) {
        planoService.deletar(id);
        return "redirect:/planos";
    }
}