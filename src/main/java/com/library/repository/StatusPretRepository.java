package com.library.repository;

import com.library.model.StatusPret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StatusPretRepository extends JpaRepository<StatusPret, Long> {
    // Interface vide - utilise uniquement les méthodes héritées de JpaRepository
}
