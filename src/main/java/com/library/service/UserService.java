package com.library.service;

import com.library.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);
    Optional<User> getUserById(Long id);
    void deleteUser(Long id);
    boolean authenticateBibliothecaire(String username, String password);
    boolean authenticateUser(String username, String password);
    List<User> getAllUsers();
    User saveUser(User user);
}
