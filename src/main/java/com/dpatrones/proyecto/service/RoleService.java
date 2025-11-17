package com.dpatrones.proyecto.service;

import com.dpatrones.proyecto.domain.model.Role;
import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> getAllRoles();
    Optional<Role> getRoleById(Long id);
    Role createRole(String name, String description);
    void deleteRole(Long id);
}
