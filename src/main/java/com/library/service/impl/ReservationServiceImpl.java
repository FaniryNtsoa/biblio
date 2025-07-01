package com.library.service.impl;

import com.library.model.Reservation;
import com.library.model.Adherent;
import com.library.model.Livre;
import com.library.model.StatusReservation;
import com.library.model.HistoriqueStatusReservation;
import com.library.repository.ReservationRepository;
import com.library.repository.HistoriqueStatusReservationRepository;
import com.library.repository.AdherentRepository;
import com.library.repository.LivreRepository;
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
    private final AdherentRepository adherentRepository;
    private final LivreRepository livreRepository;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                 HistoriqueStatusReservationRepository historiqueStatusReservationRepository,
                                 AdherentRepository adherentRepository,
                                 LivreRepository livreRepository) {
        this.reservationRepository = reservationRepository;
        this.historiqueStatusReservationRepository = historiqueStatusReservationRepository;
        this.adherentRepository = adherentRepository;
        this.livreRepository = livreRepository;
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
    public List<Reservation> findByAdherent(Adherent adherent) {
        return reservationRepository.findByAdherent(adherent);
    }

    @Override
    public List<Reservation> findByLivre(Livre livre) {
        return reservationRepository.findByLivre(livre);
    }

    @Override
    public List<Reservation> findByDateReservationBetween(LocalDate dateDebut, LocalDate dateFin) {
        return reservationRepository.findByDateReservationBetween(dateDebut, dateFin);
    }

    @Override
    public List<Reservation> findByDateExpirationBefore(LocalDate date) {
        return reservationRepository.findByDateExpirationBefore(date);
    }

    @Override
    public Optional<Reservation> findByAdherentAndLivre(Adherent adherent, Livre livre) {
        return reservationRepository.findByAdherentAndLivre(adherent, livre);
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

    @Override
    public boolean isLivreReservedByAdherent(Long livreId, Long adherentId) {
        Livre livre = livreRepository.findById(livreId).orElse(null);
        Adherent adherent = adherentRepository.findById(adherentId).orElse(null);
        
        if (livre == null || adherent == null) {
            return false;
        }
        
        return reservationRepository.findByAdherentAndLivre(adherent, livre).isPresent();
    }
}
