package com.dpatrones.proyecto.service;

import com.dpatrones.proyecto.domain.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User createUser(String username, String email, String password);
    void deleteUser(Long id);
}
