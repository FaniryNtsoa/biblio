package com.library.service.impl;

import com.library.model.User;
import com.library.repository.UserRepository;
import com.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findAll().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }
    
    @Override
    public boolean authenticateUser(String username, String password) {
        return findByUsername(username)
                .map(user -> user.getMotDePasse().equals(password) && !user.isBibliothecaire())
                .orElse(false);
    }
    
    @Override
    public boolean authenticateBibliothecaire(String username, String password) {
        // Pour des fins de test, créons un compte bibliothécaire si aucun n'existe
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setMotDePasse("admin");
            admin.setRole("BIBLIOTHECAIRE");
            userRepository.save(admin);
        }
        
        return userRepository.findAll().stream()
                .filter(user -> user.getUsername().equals(username) && 
                               user.getMotDePasse().equals(password) &&
                               user.getRole().equals("BIBLIOTHECAIRE"))
                .findFirst()
                .isPresent();
    }
    
    // @Override
    // public boolean isAdmin(User user) {
    //     return user.getTypeUser().getNom().equalsIgnoreCase("admin");
    // }
    // @Override
    // public boolean isBibliothecaire(User user) {
    //     return user.getTypeUser().getNom().equalsIgnoreCase("bibliothecaire");
    // }
}

