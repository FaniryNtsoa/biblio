package com.library.repository;

import com.library.model.Adherent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdherentRepository extends JpaRepository<Adherent, Long> {
    // Interface vide - utilise uniquement les méthodes héritées de JpaRepository
}