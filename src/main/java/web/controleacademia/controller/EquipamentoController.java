package web.controleacademia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.controleacademia.model.Equipamento;
import web.controleacademia.service.EquipamentoService;

@Controller
@RequestMapping("/equipamentos")
public class EquipamentoController {

    @Autowired
    private EquipamentoService equipamentoService;

    @GetMapping
    public String listarEquipamentos(Model model) {
        model.addAttribute("equipamentos", equipamentoService.listarTodos());
        return "equipamento/list";
    }

    @GetMapping("/novo")
    public String novoEquipamento(Model model) {
        model.addAttribute("equipamento", new Equipamento());
        return "equipamento/form";
    }

    @PostMapping
    public String salvarEquipamento(@ModelAttribute Equipamento equipamento) {
        equipamentoService.salvar(equipamento);
        return "redirect:/equipamentos";
    }

    @GetMapping("/editar/{id}")
    public String editarEquipamento(@PathVariable Long id, Model model) {
        model.addAttribute("equipamento", equipamentoService.buscarPorId(id).orElse(new Equipamento()));
        return "equipamento/form";
    }

    @GetMapping("/deletar/{id}")
    public String deletarEquipamento(@PathVariable Long id) {
        equipamentoService.deletar(id);
        return "redirect:/equipamentos";
    }
}
