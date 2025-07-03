package com.library.repository;

import com.library.model.HistoriqueStatusReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoriqueStatusReservationRepository extends JpaRepository<HistoriqueStatusReservation, Long> {
    // Interface vide - utilise uniquement les méthodes héritées de JpaRepository
}
