package com.library.repository;

import com.library.model.Prolongement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProlongementRepository extends JpaRepository<Prolongement, Long> {
    // Interface vide - utilise uniquement les méthodes héritées de JpaRepository
}

