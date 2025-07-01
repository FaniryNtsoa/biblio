package com.library.service.impl;

import com.library.model.TypeUser;
import com.library.repository.TypeUserRepository;
import com.library.service.TypeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TypeUserServiceImpl implements TypeUserService {

    private final TypeUserRepository typeUserRepository;

    @Autowired
    public TypeUserServiceImpl(TypeUserRepository typeUserRepository) {
        this.typeUserRepository = typeUserRepository;
    }

    @Override
    public List<TypeUser> getAllTypeUsers() {
        return typeUserRepository.findAll();
    }

    @Override
    public Optional<TypeUser> getTypeUserById(Long id) {
        return typeUserRepository.findById(id);
    }

    @Override
    public TypeUser saveTypeUser(TypeUser typeUser) {
        return typeUserRepository.save(typeUser);
    }

    @Override
    public void deleteTypeUser(Long id) {
        typeUserRepository.deleteById(id);
    }

    @Override
    public Optional<TypeUser> findByNom(String nom) {
        return typeUserRepository.findByNom(nom);
    }
}
