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
    public boolean isAdmin(User user) {
        return user.getTypeUser().getNom().equalsIgnoreCase("admin");
    }
    @Override
    public boolean isBibliothecaire(User user) {
        return user.getTypeUser().getNom().equalsIgnoreCase("bibliothecaire");
    }
}

