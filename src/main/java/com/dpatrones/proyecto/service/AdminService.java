package com.dpatrones.proyecto.service;

import com.dpatrones.proyecto.model.Admin;
import com.dpatrones.proyecto.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    
    private final AdminRepository adminRepository;
    
    public List<Admin> listarTodos() {
        return adminRepository.findAll();
    }
    
    public Optional<Admin> buscarPorId(Long id) {
        return adminRepository.findById(id);
    }
    
    public Admin guardar(Admin admin) {
        return adminRepository.save(admin);
    }
    
    /**
     * Login de administrador (simple, sin JWT)
     */
    public Optional<Admin> login(String email, String password) {
        Optional<Admin> admin = adminRepository.findByEmail(email);
        if (admin.isPresent() && admin.get().getPassword().equals(password)) {
            return admin;
        }
        return Optional.empty();
    }
}
