package web.controleacademia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.controleacademia.model.Item;
import web.controleacademia.service.ItemService;

@Controller
@RequestMapping("/itens")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    public String listarItens(Model model) {
        model.addAttribute("itens", itemService.listarTodos());
        return "item/list";
    }

    @GetMapping("/novo")
    public String novoItem(Model model) {
        model.addAttribute("item", new Item());
        return "item/form";
    }

    @PostMapping
    public String salvarItem(@ModelAttribute Item item) {
        itemService.salvar(item);
        return "redirect:/itens";
    }

    @GetMapping("/editar/{id}")
    public String editarItem(@PathVariable Long id, Model model) {
        model.addAttribute("item", itemService.buscarPorId(id).orElse(new Item()));
        return "item/form";
    }

    @GetMapping("/deletar/{id}")
    public String deletarItem(@PathVariable Long id) {
        itemService.deletar(id);
        return "redirect:/itens";
    }
}