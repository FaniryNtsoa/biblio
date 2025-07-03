package com.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.model.Livre;

@Repository
public interface LivreRepository extends JpaRepository<Livre, Long> {
    // Utilise uniquement les méthodes héritées de JpaRepository
}
