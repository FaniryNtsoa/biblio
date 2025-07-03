package com.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.model.HistoriqueProlongement;

@Repository
public interface HistoriqueProlongementRepository extends JpaRepository<HistoriqueProlongement, Long> {
    // Interface vide - utilise uniquement les méthodes héritées de JpaRepository
}
