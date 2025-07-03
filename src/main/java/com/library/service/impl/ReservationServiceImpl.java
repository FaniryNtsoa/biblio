package com.library.service.impl;

import com.library.model.Reservation;
import com.library.model.StatusReservation;
import com.library.model.HistoriqueStatusReservation;
import com.library.repository.ReservationRepository;
import com.library.repository.HistoriqueStatusReservationRepository;
import com.library.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final HistoriqueStatusReservationRepository historiqueStatusReservationRepository;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                 HistoriqueStatusReservationRepository historiqueStatusReservationRepository) {
        this.reservationRepository = reservationRepository;
        this.historiqueStatusReservationRepository = historiqueStatusReservationRepository;

    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    @Override
    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Reservation updateStatusReservation(Long reservationId, StatusReservation statusReservation) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + reservationId));

        HistoriqueStatusReservation historique = new HistoriqueStatusReservation();
        historique.setReservation(reservation);
        historique.setStatusReservation(statusReservation);
        historique.setDateReservation(LocalDate.now());
        
        historiqueStatusReservationRepository.save(historique);
        
        return reservation;
    }
}
