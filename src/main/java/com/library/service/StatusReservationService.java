package com.library.service;

import com.library.model.StatusReservation;
import java.util.List;
import java.util.Optional;

public interface StatusReservationService {
    List<StatusReservation> getAllStatusReservations();
    Optional<StatusReservation> getStatusReservationById(Long id);
    StatusReservation saveStatusReservation(StatusReservation statusReservation);
    void deleteStatusReservation(Long id);
}
