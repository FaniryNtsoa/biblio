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
    List<Inscription> findByAdherent(Adherent adherent);
    List<Inscription> findByDateInscriptionBetween(LocalDate dateDebut, LocalDate dateFin);
    List<Inscription> findByDateExpirationBefore(LocalDate date);
    boolean isInscriptionActive(Long adherentId);
    Inscription renewInscription(Long inscriptionId, LocalDate newExpirationDate);
}
