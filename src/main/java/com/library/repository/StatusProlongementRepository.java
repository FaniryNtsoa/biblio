package com.library.repository;

import com.library.model.StatusProlongement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusProlongementRepository extends JpaRepository<StatusProlongement, Long> {
    // Interface vide - utilise uniquement les méthodes héritées de JpaRepository
}
