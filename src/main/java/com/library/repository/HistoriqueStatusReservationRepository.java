package com.library.repository;

import com.library.model.HistoriqueStatusReservation;
import com.library.model.Reservation;
import com.library.model.StatusReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface HistoriqueStatusReservationRepository extends JpaRepository<HistoriqueStatusReservation, Long> {
    List<HistoriqueStatusReservation> findByReservation(Reservation reservation);
    List<HistoriqueStatusReservation> findByStatusReservation(StatusReservation statusReservation);
    List<HistoriqueStatusReservation> findByDateReservationBetween(LocalDate dateDebut, LocalDate dateFin);
}
