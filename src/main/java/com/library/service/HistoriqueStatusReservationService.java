package com.library.service;

import com.library.model.HistoriqueStatusReservation;
import java.util.List;
import java.util.Optional;

public interface HistoriqueStatusReservationService {
    List<HistoriqueStatusReservation> getAllHistoriqueStatusReservations();
    Optional<HistoriqueStatusReservation> getHistoriqueStatusReservationById(Long id);
    HistoriqueStatusReservation saveHistoriqueStatusReservation(HistoriqueStatusReservation historiqueStatusReservation);
    void deleteHistoriqueStatusReservation(Long id);
    HistoriqueStatusReservation createHistoriqueStatusReservation(Long reservationId, Long statusReservationId);
}
