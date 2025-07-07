package com.library.service.impl;

import com.library.model.Adherent;
import com.library.model.HistoriqueStatusReservation;
import com.library.model.Livre;
import com.library.model.Reservation;
import com.library.model.StatusReservation;
import com.library.repository.ReservationRepository;
import com.library.repository.HistoriqueStatusReservationRepository;
import com.library.repository.StatusReservationRepository;
import com.library.service.ReservationService;
import com.library.service.HistoriqueStatusReservationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final HistoriqueStatusReservationRepository historiqueStatusReservationRepository;
    private final StatusReservationRepository statusReservationRepository;
    private final HistoriqueStatusReservationService historiqueStatusReservationService;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                 HistoriqueStatusReservationRepository historiqueStatusReservationRepository,
                                 StatusReservationRepository statusReservationRepository,
                                 HistoriqueStatusReservationService historiqueStatusReservationService) {
        this.reservationRepository = reservationRepository;
        this.historiqueStatusReservationRepository = historiqueStatusReservationRepository;
        this.statusReservationRepository = statusReservationRepository;
        this.historiqueStatusReservationService = historiqueStatusReservationService;
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
        historique.setDateChangement(LocalDate.now()); // Ajout de la date de changement
        
        historiqueStatusReservationRepository.save(historique);
        
        return reservation;
    }
    
    @Override
    @Transactional
    public Reservation createReservation(Adherent adherent, Livre livre, LocalDate dateReservation) {
        // Créer la réservation
        Reservation reservation = new Reservation();
        reservation.setAdherent(adherent);
        reservation.setLivre(livre);
        reservation.setDateReservation(dateReservation);
        
        // Pas de date d'expiration ici comme spécifié
        
        // Sauvegarder la réservation
        Reservation savedReservation = reservationRepository.save(reservation);
        
        // Trouver le statut EN_ATTENTE
        StatusReservation statusEnAttente = statusReservationRepository.findAll().stream()
                .filter(status -> "EN_ATTENTE".equals(status.getNom()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Statut EN_ATTENTE non trouvé"));
        
        // Créer l'historique initial
        HistoriqueStatusReservation historique = new HistoriqueStatusReservation();
        historique.setReservation(savedReservation);
        historique.setStatusReservation(statusEnAttente);
        historique.setDateReservation(LocalDate.now());
        historique.setDateChangement(LocalDate.now()); // Ajout de la date de changement
        
        historiqueStatusReservationRepository.save(historique);
        
        return savedReservation;
    }
    
    @Override
    public List<Reservation> findReservationsByAdherent(Adherent adherent) {
        return reservationRepository.findAll().stream()
                .filter(reservation -> reservation.getAdherent().getId().equals(adherent.getId()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Reservation> findReservationsByStatus(String statusNom) {
        return getAllReservations().stream()
                .filter(reservation -> statusNom.equals(getLatestStatusName(reservation)))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Reservation> findReservationsByAdherentAndStatus(Adherent adherent, String statusNom) {
        return findReservationsByAdherent(adherent).stream()
                .filter(reservation -> statusNom.equals(getLatestStatusName(reservation)))
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean hasActiveReservationForLivre(Adherent adherent, Livre livre) {
        List<String> activeStatuses = List.of("EN_ATTENTE", "CONFIRMEE");
        
        return findReservationsByAdherent(adherent).stream()
                .filter(reservation -> reservation.getLivre().getId().equals(livre.getId()))
                .anyMatch(reservation -> {
                    String status = getLatestStatusName(reservation);
                    return activeStatuses.contains(status);
                });
    }
    
    @Override
    @Transactional
    public boolean cancelReservation(Long reservationId, Adherent adherent) {
        Optional<Reservation> reservationOpt = getReservationById(reservationId);
        
        if (reservationOpt.isEmpty()) {
            return false;
        }
        
        Reservation reservation = reservationOpt.get();
        
        // Vérifier que la réservation appartient à l'adhérent
        if (!reservation.getAdherent().getId().equals(adherent.getId())) {
            return false;
        }
        
        // Vérifier que la réservation est bien en attente
        if (!isReservationEnAttente(reservation)) {
            return false;
        }
        
        // Trouver le statut ANNULEE
        StatusReservation statusAnnulee = statusReservationRepository.findAll().stream()
                .filter(status -> "ANNULEE".equals(status.getNom()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Statut ANNULEE non trouvé"));
        
        // Mettre à jour le statut
        updateStatusReservation(reservationId, statusAnnulee);
        
        return true;
    }
    
    @Override
    @Transactional
    public void updateExpiredReservations() {
        LocalDate today = LocalDate.now();
        
        // Trouver toutes les réservations en attente avec une date de réservation passée
        List<Reservation> expiredReservations = getAllReservations().stream()
                .filter(this::isReservationEnAttente)
                .filter(reservation -> reservation.getDateReservation().isBefore(today))
                .collect(Collectors.toList());
        
        if (!expiredReservations.isEmpty()) {
            System.out.println("Nombre de réservations expirées à traiter: " + expiredReservations.size());
            
            // Trouver le statut EXPIREE
            StatusReservation statusExpiree = statusReservationRepository.findAll().stream()
                    .filter(status -> "EXPIREE".equals(status.getNom()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Statut EXPIREE non trouvé"));
            
            // Mettre à jour le statut de chaque réservation expirée
            for (Reservation reservation : expiredReservations) {
                System.out.println("Mise à jour de la réservation expirée ID: " + reservation.getId() + 
                                  " pour l'adhérent: " + reservation.getAdherent().getNom() + 
                                  ", date de réservation: " + reservation.getDateReservation());
                
                updateStatusReservation(reservation.getId(), statusExpiree);
            }
        }
    }
    
    @Override
    public boolean isReservationEnAttente(Reservation reservation) {
        return "EN_ATTENTE".equals(getLatestStatusName(reservation));
    }
    
    @Override
    public String getLatestStatusName(Reservation reservation) {
        return historiqueStatusReservationRepository.findAll().stream()
                .filter(historique -> historique.getReservation().getId().equals(reservation.getId()))
                .max(Comparator.comparing(HistoriqueStatusReservation::getDateReservation))
                .map(historique -> historique.getStatusReservation().getNom())
                .orElse(null);
    }
}
