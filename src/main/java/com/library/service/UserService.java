package com.library.service;

import com.library.model.User;
import com.library.model.TypeUser;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User saveUser(User user);
    void deleteUser(Long id);
    List<User> findByTypeUser(TypeUser typeUser);
    boolean authenticate(Long id, String motDePasse);
}
