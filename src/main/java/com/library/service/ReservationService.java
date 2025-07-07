package com.library.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.library.model.Adherent;
import com.library.model.Livre;
import com.library.model.Reservation;
import com.library.model.StatusReservation;

public interface ReservationService {
    List<Reservation> getAllReservations();
    Optional<Reservation> getReservationById(Long id);
    Reservation saveReservation(Reservation reservation);
    void deleteReservation(Long id);
    Reservation updateStatusReservation(Long reservationId, StatusReservation statusReservation);
    
    // Nouvelles méthodes pour le système de réservation
    Reservation createReservation(Adherent adherent, Livre livre, LocalDate dateReservation);
    List<Reservation> findReservationsByAdherent(Adherent adherent);
    List<Reservation> findReservationsByStatus(String statusNom);
    List<Reservation> findReservationsByAdherentAndStatus(Adherent adherent, String statusNom);
    boolean hasActiveReservationForLivre(Adherent adherent, Livre livre);
    boolean cancelReservation(Long reservationId, Adherent adherent);
    void updateExpiredReservations();
    boolean isReservationEnAttente(Reservation reservation);
    String getLatestStatusName(Reservation reservation);
}
