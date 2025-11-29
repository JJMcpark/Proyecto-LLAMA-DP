package com.dpatrones.proyecto.controller;

import com.dpatrones.proyecto.model.Admin;
import com.dpatrones.proyecto.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {
    
    private final AdminService adminService;
    
    @GetMapping
    public List<Admin> listarTodos() {
        return adminService.listarTodos();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Admin> buscarPorId(@PathVariable Long id) {
        return adminService.buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/registro")
    public Admin registrar(@RequestBody Admin admin) {
        return adminService.guardar(admin);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String email = credenciales.get("email");
        String password = credenciales.get("password");
        
        return adminService.login(email, password)
            .map(admin -> ResponseEntity.ok(Map.of(
                "mensaje", "Login de administrador exitoso",
                "admin", admin
            )))
            .orElse(ResponseEntity.badRequest().body(Map.of("error", "Credenciales inválidas")));
    }
}
