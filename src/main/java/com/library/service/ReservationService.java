package com.library.service;

import com.library.model.Reservation;
import com.library.model.Adherent;
import com.library.model.Livre;
import com.library.model.StatusReservation;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationService {
    List<Reservation> getAllReservations();
    Optional<Reservation> getReservationById(Long id);
    Reservation saveReservation(Reservation reservation);
    void deleteReservation(Long id);
    List<Reservation> findByAdherent(Adherent adherent);
    List<Reservation> findByLivre(Livre livre);
    List<Reservation> findByDateReservationBetween(LocalDate dateDebut, LocalDate dateFin);
    List<Reservation> findByDateExpirationBefore(LocalDate date);
    Optional<Reservation> findByAdherentAndLivre(Adherent adherent, Livre livre);
    Reservation updateStatusReservation(Long reservationId, StatusReservation statusReservation);
    boolean isLivreReservedByAdherent(Long livreId, Long adherentId);
}
