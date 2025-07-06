package com.library.repository;

import com.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Interface vide - utilise uniquement les méthodes héritées de JpaRepository
}
