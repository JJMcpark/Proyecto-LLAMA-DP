package com.dpatrones.proyecto.controller;

import com.dpatrones.proyecto.service.ProductoService;
import com.dpatrones.proyecto.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PublicWebController {

    private final ProductoService productoService;
    private final UsuarioService usuarioService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("productos", productoService.listarTodos());
        return "index";
    }

    @GetMapping("/usuarios")
    public String mostrarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios";
    }

    @GetMapping("/carrito")
    public String verCarrito() {
        return "carrito";
    }
}