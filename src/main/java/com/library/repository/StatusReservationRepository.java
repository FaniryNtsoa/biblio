package com.library.repository;

import com.library.model.StatusReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StatusReservationRepository extends JpaRepository<StatusReservation, Long> {
    Optional<StatusReservation> findByNom(String nom);
}
