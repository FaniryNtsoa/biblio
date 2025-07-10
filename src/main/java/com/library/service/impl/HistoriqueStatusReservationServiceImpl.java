package com.library.service.impl;

import com.library.model.HistoriqueStatusReservation;
import com.library.model.Reservation;
import com.library.model.StatusReservation;
import com.library.repository.HistoriqueStatusReservationRepository;
import com.library.repository.ReservationRepository;
import com.library.repository.StatusReservationRepository;
import com.library.service.HistoriqueStatusReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class HistoriqueStatusReservationServiceImpl implements HistoriqueStatusReservationService {

    private final HistoriqueStatusReservationRepository historiqueStatusReservationRepository;
    private final ReservationRepository reservationRepository;
    private final StatusReservationRepository statusReservationRepository;

    @Autowired
    public HistoriqueStatusReservationServiceImpl(HistoriqueStatusReservationRepository historiqueStatusReservationRepository,
                                                 ReservationRepository reservationRepository,
                                                 StatusReservationRepository statusReservationRepository) {
        this.historiqueStatusReservationRepository = historiqueStatusReservationRepository;
        this.reservationRepository = reservationRepository;
        this.statusReservationRepository = statusReservationRepository;
    }

    @Override
    public List<HistoriqueStatusReservation> getAllHistoriqueStatusReservations() {
        return historiqueStatusReservationRepository.findAll();
    }

    @Override
    public Optional<HistoriqueStatusReservation> getHistoriqueStatusReservationById(Long id) {
        return historiqueStatusReservationRepository.findById(id);
    }

    @Override
    public HistoriqueStatusReservation saveHistoriqueStatusReservation(HistoriqueStatusReservation historiqueStatusReservation) {
        return historiqueStatusReservationRepository.save(historiqueStatusReservation);
    }

    @Override
    public void deleteHistoriqueStatusReservation(Long id) {
        historiqueStatusReservationRepository.deleteById(id);
    }   

    @Override
    @Transactional
    public HistoriqueStatusReservation createHistoriqueStatusReservation(Long reservationId, Long statusReservationId, String dateReservationStr) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + reservationId));
        
        StatusReservation statusReservation = statusReservationRepository.findById(statusReservationId)
                .orElseThrow(() -> new RuntimeException("StatusReservation not found with id: " + statusReservationId));
        
        HistoriqueStatusReservation historique = new HistoriqueStatusReservation();
        historique.setReservation(reservation);
        historique.setStatusReservation(statusReservation);
        
        // Convertir la date de réservation si fournie
        if (dateReservationStr != null && !dateReservationStr.isEmpty()) {
            LocalDate dateResDate = LocalDate.parse(dateReservationStr);
            LocalTime heureActuelle = LocalTime.now();
            LocalDateTime dateReservation = LocalDateTime.of(dateResDate, heureActuelle);
            historique.setDateReservation(dateReservation);
        } else {
            historique.setDateReservation(LocalDateTime.now());
        }
        
        historique.setDateChangement(LocalDateTime.now());
        
        return historiqueStatusReservationRepository.save(historique);
    }
}
