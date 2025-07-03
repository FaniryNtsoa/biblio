package com.library.service;

import com.library.model.Inscription;
import com.library.model.Adherent;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InscriptionService {
    List<Inscription> getAllInscriptions();
    Optional<Inscription> getInscriptionById(Long id);
    Inscription saveInscription(Inscription inscription);
    void deleteInscription(Long id);
    Inscription renewInscription(Long inscriptionId, LocalDate newExpirationDate);
    
    // Méthodes modifiées et existantes
    boolean isAdherentActiveMember(Adherent adherent);
    Inscription createNewInscription(Adherent adherent, LocalDate dateExpiration);
    Inscription createNewInscription(Adherent adherent, int durationInMonths); // Conservée pour compatibilité
    Optional<Inscription> findLatestInscriptionByAdherent(Adherent adherent);
    List<Inscription> findInscriptionsByAdherent(Adherent adherent);
}
