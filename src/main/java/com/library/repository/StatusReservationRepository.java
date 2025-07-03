package com.library.repository;

import com.library.model.StatusReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusReservationRepository extends JpaRepository<StatusReservation, Long> {
    // Interface vide - utilise uniquement les méthodes héritées de JpaRepository
}
