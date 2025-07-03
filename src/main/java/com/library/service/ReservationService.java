package com.library.service;

import java.util.List;
import java.util.Optional;

import com.library.model.Reservation;
import com.library.model.StatusReservation;

public interface ReservationService {
    List<Reservation> getAllReservations();
    Optional<Reservation> getReservationById(Long id);
    Reservation saveReservation(Reservation reservation);
    void deleteReservation(Long id);
    Reservation updateStatusReservation(Long reservationId, StatusReservation statusReservation);
}
