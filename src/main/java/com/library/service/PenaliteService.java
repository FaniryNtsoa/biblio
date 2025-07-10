package com.library.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.library.model.Adherent;
import com.library.model.Penalite;
import com.library.model.Pret;

public interface PenaliteService {
    List<Penalite> getAllPenalites();
    Optional<Penalite> getPenaliteById(Long id);
    Penalite savePenalite(Penalite penalite);
    void deletePenalite(Long id);
    List<Penalite> findByPret(Pret pret);
    List<Penalite> findByDateBetween(LocalDateTime dateDebut, LocalDateTime dateFin);
    Penalite createPenaliteForPret(Long pretId, String motif);
    List<Penalite> getRecentPenalites(int limit);
    
    // Méthodes pour la gestion des pénalités
    boolean hasActivePenalites(Adherent adherent);
    LocalDateTime getDateFinPenalite(Adherent adherent);
    Penalite createPenaliteForRetard(Pret pret, LocalDateTime dateRetour);
    int calculerJoursRetard(Pret pret, LocalDateTime dateRetour);
    List<Penalite> findActiveByAdherent(Adherent adherent);
    List<Penalite> findAllByAdherent(Adherent adherent);
    void updatePenaliteStatuses();
    
    // Nouvelle méthode pour vérifier si une date tombe pendant une période de pénalité
    boolean isDateInPenalitePeriod(Adherent adherent, LocalDateTime date);
    boolean isPenalityActiveOnDate(Adherent adherent, LocalDateTime date);
}
