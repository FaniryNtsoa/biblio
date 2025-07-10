package com.library.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.library.model.Adherent;
import com.library.model.Penalite;
import com.library.model.Pret;
import com.library.model.Reservation;
import com.library.model.TypePret;

public interface PretService {
    List<Pret> getAllPrets();
    Optional<Pret> getPretById(Long id);
    Pret savePret(Pret pret);
    void deletePret(Long id);
    
    // Méthodes de recherche
    List<Pret> findByAdherent(Adherent adherent);
    List<Pret> findByTypePret(TypePret typePret);
    List<Pret> findByDatePretBetween(LocalDateTime dateDebut, LocalDateTime dateFin);
    long countByAdherent(Adherent adherent);
    
    // Nouvelles méthodes pour gérer les filtres et les retours
    List<Pret> findPretEnCoursByAdherent(Adherent adherent);
    List<Pret> findPretRetournesByAdherent(Adherent adherent);
    List<Pret> findPretEnRetardByAdherent(Adherent adherent);
    List<Pret> findPretARendreBientotByAdherent(Adherent adherent, int joursRestants);
    
    int countPretEnCoursByAdherent(Adherent adherent);
    int countPretEnRetardByAdherent(Adherent adherent);
    int countPretARendreBientotByAdherent(Adherent adherent, int joursRestants);
    
    boolean isPretRendu(Pret pret);
    Penalite savePenalite(Penalite penalite);
    
    // Nouvelle méthode pour créer un prêt à partir d'une réservation acceptée
    Pret createPretFromReservation(Reservation reservation);
}
