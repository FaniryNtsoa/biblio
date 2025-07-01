package com.library.service;

import com.library.model.HistoriqueStatusReservation;
import com.library.model.Reservation;
import com.library.model.StatusReservation;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HistoriqueStatusReservationService {
    List<HistoriqueStatusReservation> getAllHistoriqueStatusReservations();
    Optional<HistoriqueStatusReservation> getHistoriqueStatusReservationById(Long id);
    HistoriqueStatusReservation saveHistoriqueStatusReservation(HistoriqueStatusReservation historiqueStatusReservation);
    void deleteHistoriqueStatusReservation(Long id);
    List<HistoriqueStatusReservation> findByReservation(Reservation reservation);
    List<HistoriqueStatusReservation> findByStatusReservation(StatusReservation statusReservation);
    List<HistoriqueStatusReservation> findByDateReservationBetween(LocalDate dateDebut, LocalDate dateFin);
    HistoriqueStatusReservation createHistoriqueStatusReservation(Long reservationId, Long statusReservationId);
}
