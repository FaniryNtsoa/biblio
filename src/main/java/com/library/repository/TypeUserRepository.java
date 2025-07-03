package com.library.repository;

import com.library.model.TypeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TypeUserRepository extends JpaRepository<TypeUser, Long> {
    // Interface vide - utilise uniquement les méthodes héritées de JpaRepository
}
