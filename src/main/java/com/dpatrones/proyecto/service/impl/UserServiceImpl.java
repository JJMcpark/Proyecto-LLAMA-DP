package com.dpatrones.proyecto.service.impl;

import com.dpatrones.proyecto.domain.model.User;
import com.dpatrones.proyecto.exception.ResourceNotFoundException;
import com.dpatrones.proyecto.repository.UserRepository;
import com.dpatrones.proyecto.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User createUser(String username, String email, String password) {
        User user = User.builder()
                .username(username)
                .email(email)
                .password(password) // password encoding can be added in /auth
                .isActive(true)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User no encontrado: " + id);
        }
        userRepository.deleteById(id);
    }
}
