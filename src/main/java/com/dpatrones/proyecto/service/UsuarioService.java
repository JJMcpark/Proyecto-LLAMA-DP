package com.dpatrones.proyecto.service;

import com.dpatrones.proyecto.model.Usuario;
import com.dpatrones.proyecto.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
    
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }
    
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
    
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }
    
    /**
     * Login simple sin JWT
     */
    public Optional<Usuario> login(String email, String password) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if (usuario.isPresent() && usuario.get().getPassword().equals(password)) {
            return usuario;
        }
        return Optional.empty();
    }
    
    public boolean existeEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}
