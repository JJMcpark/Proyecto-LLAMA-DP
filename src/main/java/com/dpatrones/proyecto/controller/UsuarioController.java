package com.dpatrones.proyecto.controller;

import com.dpatrones.proyecto.model.Usuario;
import com.dpatrones.proyecto.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {
    
    private final UsuarioService usuarioService;
    
    @GetMapping
    public List<Usuario> listarTodos() {
        return usuarioService.listarTodos();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        if (usuarioService.existeEmail(usuario.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("error", "El email ya está registrado"));
        }
        return ResponseEntity.ok(usuarioService.guardar(usuario));
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String email = credenciales.get("email");
        String password = credenciales.get("password");
        
        return usuarioService.login(email, password)
            .map(u -> ResponseEntity.ok(Map.of(
                "mensaje", "Login exitoso",
                "usuario", u
            )))
            .orElse(ResponseEntity.badRequest().body(Map.of("error", "Credenciales inválidas")));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        return usuarioService.buscarPorId(id)
            .map(u -> {
                usuario.setId(id);
                return ResponseEntity.ok(usuarioService.guardar(usuario));
            })
            .orElse(ResponseEntity.notFound().build());
    }
}
