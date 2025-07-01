package com.library.service;

import com.library.model.TypeUser;
import java.util.List;
import java.util.Optional;

public interface TypeUserService {
    List<TypeUser> getAllTypeUsers();
    Optional<TypeUser> getTypeUserById(Long id);
    TypeUser saveTypeUser(TypeUser typeUser);
    void deleteTypeUser(Long id);
    Optional<TypeUser> findByNom(String nom);
}
