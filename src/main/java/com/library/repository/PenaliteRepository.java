package com.library.repository;

import com.library.model.Penalite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PenaliteRepository extends JpaRepository<Penalite, Long> {
    // Interface vide - utilise uniquement les méthodes héritées de JpaRepository
}
