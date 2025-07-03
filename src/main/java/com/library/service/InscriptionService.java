package com.library.service;

import com.library.model.Inscription;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InscriptionService {
    List<Inscription> getAllInscriptions();
    Optional<Inscription> getInscriptionById(Long id);
    Inscription saveInscription(Inscription inscription);
    void deleteInscription(Long id);
    Inscription renewInscription(Long inscriptionId, LocalDate newExpirationDate);
}
