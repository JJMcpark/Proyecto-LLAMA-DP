package com.dpatrones.proyecto.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dpatrones.proyecto.model.Admin;
import com.dpatrones.proyecto.repository.AdminRepository;

import lombok.RequiredArgsConstructor;

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

    public Optional<Admin> login(String email, String password) {
        return adminRepository.findByEmail(email)
                .filter(a -> a.getPassword().equals(password));
    }
}
