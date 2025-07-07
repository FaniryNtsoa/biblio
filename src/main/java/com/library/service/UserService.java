package com.library.service;

import com.library.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    boolean authenticateBibliothecaire(String username, String password);
    List<User> getAllUsers();
    User saveUser(User user);
}
    boolean authenticateUser(String username, String password);
    boolean authenticateBibliothecaire(String username, String password);
}
