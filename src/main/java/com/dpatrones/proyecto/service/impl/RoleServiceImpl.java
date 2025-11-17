package com.dpatrones.proyecto.service.impl;

import com.dpatrones.proyecto.domain.model.Role;
import com.dpatrones.proyecto.exception.ResourceNotFoundException;
import com.dpatrones.proyecto.repository.RoleRepository;
import com.dpatrones.proyecto.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public Role createRole(String name, String description) {
        Role role = Role.builder()
                .name(name)
                .description(description)
                .build();
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role no encontrado: " + id);
        }
        roleRepository.deleteById(id);
    }
}
